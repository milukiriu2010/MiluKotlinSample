package milu.kiriu2010.exdb1.opengl05.w051v

import android.opengl.GLES20
import milu.kiriu2010.gui.basic.MyGLES20Func
import milu.kiriu2010.gui.model.MgModelAbs
import milu.kiriu2010.gui.shader.es20.ES20MgShader
import milu.kiriu2010.gui.vbo.es20.ES20VBOAbs

// -------------------------------------
// シェーダ(スクリーンレンダリング用)
// -------------------------------------
// https://wgld.org/d/webgl/w051.html
// -------------------------------------
class WV051ShaderScreen: ES20MgShader() {
    // 頂点シェーダ
    private val scv =
            """
            attribute vec3  a_Position;
            attribute vec3  a_Normal;
            attribute vec4  a_Color;
            // カメラ視点のモデル座標変換行列
            uniform   mat4  u_matM;
            // カメラ視点のモデルxビューxプロジェクション座標変換行列
            uniform   mat4  u_matMVP;
            // 射影テクスチャマッピング用行列
            uniform   mat4  u_matVPT;
            // ライト視点のモデルxビューxプロジェクション座標変換行列
            uniform   mat4  u_matLight;
            varying   vec3  v_Position;
            varying   vec3  v_Normal;
            varying   vec4  v_Color;
            varying   vec4  v_TexCoord;
            varying   vec4  v_Depth;

            void main() {
                v_Position  = (u_matM * vec4(a_Position, 1.0)).xyz;
                v_Normal    = a_Normal;
                v_Color     = a_Color;
                // 影を射影テクスチャマッピングによって投影するために使う
                v_TexCoord  = u_matVPT   * vec4(v_Position, 1.0);
                // ライト視点での座標変換行列で変換した頂点座標が格納される
                v_Depth     = u_matLight * vec4(a_Position, 1.0);
                gl_Position = u_matMVP   * vec4(a_Position, 1.0);
            }
            """.trimIndent()

    // フラグメントシェーダ
    private val scf =
            """
            precision mediump   float;

            uniform   mat4        u_matINV;
            uniform   vec3        u_vecEye;
            uniform   sampler2D   u_Texture0;
            uniform   int         u_depthBuffer;
            varying   vec3        v_Position;
            varying   vec3        v_Normal;
            varying   vec4        v_Color;
            varying   vec4        v_TexCoord;
            varying   vec4        v_Depth;

            // フレームバッファに描きこまれた深度値を本来の値に変換する
            float restDepth(vec4 RGBA) {
                const float rMask = 1.0;
                const float gMask = 1.0/255.0;
                const float bMask = 1.0/(255.0*255.0);
                const float aMask = 1.0/(255.0*255.0*255.0);
                float depth = dot(RGBA, vec4(rMask, gMask, bMask, aMask));
                return depth;
            }

            // フレームバッファに描かれた深度値を読み出し、
            // ライト視点で座標変換した頂点の深度値と比較する
            void main() {
                // 点光源による拡散光のライティング計算
                vec3  light      = u_vecEye - v_Position;
                vec3  invLight   = normalize(u_matINV * vec4(light, 0.0)).xyz;
                float diffuse    = clamp(dot(v_Normal, invLight), 0.2, 1.0);
                // フレームバッファに描かれた深度値を読み出し、本来の値を取り出す
                float shadow     = restDepth(texture2DProj(u_Texture0, v_TexCoord));
                vec4  depthColor = vec4(1.0);
                if (v_Depth.w > 0.0) {
                    // フレームバッファへの描きこみ時にデプスバッファの値を使った場合、
                    // ライト視点でみたときの座標変換を行った頂点Zの値とshadowの値を比較
                    if (bool(u_depthBuffer)) {
                        vec4 lightCoord = v_Depth/v_Depth.w;
                        // 0.0001は、深度値が完全に一致した場合、
                        // マッハバンドと呼ばれる縞模様が発生するのを避けるための措置
                        if ( (lightCoord.z - 0.0001) > shadow ) {
                            depthColor = vec4(0.5,0.5,0.5,1.0);
                        }
                    }
                    // フレームバッファの描きこみで、ダイレクトに頂点位置を用いた場合
                    else {
                        float  near = 0.1;
                        float  far  = 150.0;
                        float  linearDepth = 1.0/(far-near);
                        linearDepth *= length(v_Position.xyz - u_vecEye);
                        if ( (linearDepth - 0.0001) > shadow ) {
                            depthColor = vec4(0.5,0.5,0.5,1.0);
                        }
                    }
                }

                gl_FragColor = v_Color * vec4(vec3(diffuse), 1.0) * depthColor;
            }
            """.trimIndent()

    override fun loadShader(): ES20MgShader {
        // 頂点シェーダを生成
        val svhandle = MyGLES20Func.loadShader(GLES20.GL_VERTEX_SHADER, scv)
        // フラグメントシェーダを生成
        val sfhandle = MyGLES20Func.loadShader(GLES20.GL_FRAGMENT_SHADER, scf)

        // プログラムオブジェクトの生成とリンク
        programHandle = MyGLES20Func.createProgram(svhandle,sfhandle)

        // ----------------------------------------------
        // attributeハンドルに値をセット
        // ----------------------------------------------
        hATTR = IntArray(3)
        // 属性(頂点)
        hATTR[0] = GLES20.glGetAttribLocation(programHandle, "a_Position").also {
            // attribute属性を有効にする
            // ここで呼ばないと描画されない
            GLES20.glEnableVertexAttribArray(it)
            MyGLES20Func.checkGlError("a_Position:glEnableVertexAttribArray")
            // attribute属性を登録
            GLES20.glVertexAttribPointer(it,3,GLES20.GL_FLOAT,false,0,0)
            MyGLES20Func.checkGlError("a_Position:glVertexAttribPointer")
        }
        MyGLES20Func.checkGlError("a_Position:glGetAttribLocation")

        // 属性(法線)
        hATTR[1] = GLES20.glGetAttribLocation(programHandle, "a_Normal").also {
            // attribute属性を有効にする
            // ここで呼ばないと描画されない
            GLES20.glEnableVertexAttribArray(it)
            MyGLES20Func.checkGlError("a_Normal:glEnableVertexAttribArray")
            // attribute属性を登録
            GLES20.glVertexAttribPointer(it,3,GLES20.GL_FLOAT,false,0,0)
            MyGLES20Func.checkGlError("a_Normal:glVertexAttribPointer")
        }
        MyGLES20Func.checkGlError("a_Normal:glGetAttribLocation")

        // 属性(色)
        hATTR[2] = GLES20.glGetAttribLocation(programHandle, "a_Color").also {
            // attribute属性を有効にする
            // ここで呼ばないと描画されない
            GLES20.glEnableVertexAttribArray(it)
            MyGLES20Func.checkGlError("a_Color:glEnableVertexAttribArray")
            // attribute属性を登録
            GLES20.glVertexAttribPointer(it,4,GLES20.GL_FLOAT,false,0,0)
            MyGLES20Func.checkGlError("a_Color:glVertexAttribPointer")
        }
        MyGLES20Func.checkGlError("a_Color:glGetAttribLocation")

        // ----------------------------------------------
        // uniformハンドルに値をセット
        // ----------------------------------------------
        hUNI = IntArray(8)

        // uniform(モデル)
        hUNI[0] = GLES20.glGetUniformLocation(programHandle,"u_matM")
        MyGLES20Func.checkGlError("u_matM:glGetUniformLocation")

        // uniform(モデル×ビュー×プロジェクション)
        hUNI[1] = GLES20.glGetUniformLocation(programHandle,"u_matMVP")
        MyGLES20Func.checkGlError("u_matMVP:glGetUniformLocation")

        // uniform(逆行列)
        hUNI[2] = GLES20.glGetUniformLocation(programHandle,"u_matINV")
        MyGLES20Func.checkGlError("u_matINV:glGetUniformLocation")

        // uniform(テクスチャ射影変換用行列)
        hUNI[3] = GLES20.glGetUniformLocation(programHandle,"u_matVPT")
        MyGLES20Func.checkGlError("u_matVPT:glGetUniformLocation")

        // uniform(ライト視点の座標変換行列)
        hUNI[4] = GLES20.glGetUniformLocation(programHandle,"u_matLight")
        MyGLES20Func.checkGlError("u_matLight:glGetUniformLocation")

        // uniform(光源位置)
        hUNI[5] = GLES20.glGetUniformLocation(programHandle,"u_vecLight")
        MyGLES20Func.checkGlError("u_vecLight:glGetUniformLocation")

        // uniform(テクスチャユニット)
        hUNI[6] = GLES20.glGetUniformLocation(programHandle, "u_Texture0")
        MyGLES20Func.checkGlError("u_Texture0:glGetUniformLocation")

        // uniform(深度値を使うかどうか)
        hUNI[7] = GLES20.glGetUniformLocation(programHandle, "u_depthBuffer")
        MyGLES20Func.checkGlError("u_depthBuffer:glGetUniformLocation")

        return this
    }

    fun draw(model: MgModelAbs,
             bo: ES20VBOAbs,
             u_matM: FloatArray,
             u_matMVP: FloatArray,
             u_matI: FloatArray,
             u_matVPT: FloatArray,
             u_matLight: FloatArray,
             u_vecLight: FloatArray,
             u_Texture0: Int,
             u_depthBuffer: Int) {
        GLES20.glUseProgram(programHandle)
        MyGLES20Func.checkGlError2("UseProgram",this,model)

        // attribute(頂点)
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,bo.hVBO[0])
        GLES20.glVertexAttribPointer(hATTR[0],3,GLES20.GL_FLOAT,false,0,0)
        MyGLES20Func.checkGlError2("a_Position",this,model)

        // attribute(法線)
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,bo.hVBO[1])
        GLES20.glVertexAttribPointer(hATTR[1],3,GLES20.GL_FLOAT,false,0,0)
        MyGLES20Func.checkGlError2("a_Normal",this,model)

        // attribute(色)
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,bo.hVBO[2])
        GLES20.glVertexAttribPointer(hATTR[2],4,GLES20.GL_FLOAT,false,0,0)
        MyGLES20Func.checkGlError2("a_Color",this,model)

        // uniform(モデル)
        GLES20.glUniformMatrix4fv(hUNI[0],1,false,u_matM,0)
        MyGLES20Func.checkGlError2("u_matM",this,model)

        // uniform(モデル×ビュー×プロジェクション)
        GLES20.glUniformMatrix4fv(hUNI[1],1,false,u_matMVP,0)
        MyGLES20Func.checkGlError2("u_matMVP",this,model)

        // uniform(逆行列)
        GLES20.glUniformMatrix4fv(hUNI[2],1,false,u_matI,0)
        MyGLES20Func.checkGlError2("u_matINV",this,model)

        // uniform(テクスチャ射影変換用行列)
        GLES20.glUniformMatrix4fv(hUNI[3],1,false,u_matVPT,0)
        MyGLES20Func.checkGlError2("u_matVPT",this,model)

        // uniform(ライト視点の座標変換行列)
        GLES20.glUniformMatrix4fv(hUNI[4],1,false,u_matLight,0)
        MyGLES20Func.checkGlError2("u_matLight",this,model)

        // uniform(光源位置)
        GLES20.glUniform3fv(hUNI[5],1,u_vecLight,0)
        MyGLES20Func.checkGlError2("u_vecLight",this,model)

        // uniform(テクスチャユニット)
        GLES20.glUniform1i(hUNI[6], u_Texture0)
        MyGLES20Func.checkGlError2("u_Texture0",this,model)

        // uniform(深度値を使うかどうか)
        GLES20.glUniform1i(hUNI[7], u_depthBuffer)
        MyGLES20Func.checkGlError2("u_depthBuffer",this,model)

        // モデルを描画
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, model.datIdx.size, GLES20.GL_UNSIGNED_SHORT, model.bufIdx)

        // リソース解放
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,0)
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER,0)
    }
}
