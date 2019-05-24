package milu.kiriu2010.exdb1.opengl07.w069

import android.opengl.GLES20
import milu.kiriu2010.gui.basic.MyGLFunc
import milu.kiriu2010.gui.model.MgModelAbs
import milu.kiriu2010.gui.shader.MgShader

// -------------------------------------
// シェーダ(スクリーンレンダリング用)
// -------------------------------------
// w51とは違うらしい
// -------------------------------------
// https://wgld.org/d/webgl/w069.html
// -------------------------------------
class W069ShaderScreen: MgShader() {
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
            uniform   vec3        u_vecLight;
            uniform   sampler2D   u_Texture0;
            varying   vec3        v_Position;
            varying   vec3        v_Normal;
            varying   vec4        v_Color;
            varying   vec4        v_TexCoord;
            varying   vec4        v_Depth;

            // フレームバッファに描かれた深度値を読み出し、
            // ライト視点で座標変換した頂点の深度値と比較する
            void main() {
                float lightCoord = 0.0;
                // 点光源による拡散光のライティング計算
                vec3  light      = u_vecLight - v_Position;
                vec3  invLight   = normalize(u_matINV * vec4(light, 0.0)).xyz;
                float diffuse    = clamp(dot(v_Normal, invLight), 0.2, 1.0);
                // フレームバッファに描かれた深度値を読み出し、本来の値を取り出す
                float shadow     = texture2DProj(u_Texture0, v_TexCoord).r;
                vec4  depthColor = vec4(1.0);
                if (v_Depth.w > 0.0) {
                    lightCoord = (v_Depth.z/v_Depth.w+1.0)*0.5;
                    // 0.0001は、深度値が完全に一致した場合、
                    // マッハバンドと呼ばれる縞模様が発生するのを避けるための措置
                    if ( (lightCoord - 0.01) > shadow ) {
                        depthColor = vec4(0.5,0.5,0.5,1.0);
                    }
                }

                gl_FragColor = v_Color * vec4(vec3(diffuse), 1.0) * depthColor;
            }
            """.trimIndent()

    override fun loadShader(): MgShader {
        // 頂点シェーダを生成
        svhandle = MyGLFunc.loadShader(GLES20.GL_VERTEX_SHADER, scv)
        // フラグメントシェーダを生成
        sfhandle = MyGLFunc.loadShader(GLES20.GL_FRAGMENT_SHADER, scf)

        // プログラムオブジェクトの生成とリンク
        programHandle = MyGLFunc.createProgram(svhandle,sfhandle, arrayOf("a_Position","a_Normal","a_Color") )
        return this
    }

    fun draw(model: MgModelAbs,
             matM: FloatArray,
             matMVP: FloatArray,
             matINV: FloatArray,
             matVPT: FloatArray,
             matLight: FloatArray,
             u_vecLight: FloatArray,
             u_Texture0: Int) {
        GLES20.glUseProgram(programHandle)
        MyGLFunc.checkGlError2("UseProgram",this,model)

        // attribute(頂点)
        model.bufPos.position(0)
        GLES20.glGetAttribLocation(programHandle,"a_Position").also {
            GLES20.glVertexAttribPointer(it,3,GLES20.GL_FLOAT,false, 3*4, model.bufPos)
            GLES20.glEnableVertexAttribArray(it)
        }
        MyGLFunc.checkGlError2("a_Position",this,model)

        // attribute(法線)
        model.bufNor.position(0)
        GLES20.glGetAttribLocation(programHandle,"a_Normal").also {
            GLES20.glVertexAttribPointer(it,3,GLES20.GL_FLOAT,false, 3*4, model.bufNor)
            GLES20.glEnableVertexAttribArray(it)
        }
        MyGLFunc.checkGlError2("a_Normal",this,model)

        // attribute(色)
        model.bufCol.position(0)
        GLES20.glGetAttribLocation(programHandle,"a_Color").also {
            GLES20.glVertexAttribPointer(it,4,GLES20.GL_FLOAT,false, 4*4, model.bufCol)
            GLES20.glEnableVertexAttribArray(it)
        }
        MyGLFunc.checkGlError2("a_Color",this,model)

        // uniform(モデル)
        GLES20.glGetUniformLocation(programHandle,"u_matM").also {
            GLES20.glUniformMatrix4fv(it,1,false,matM,0)
        }
        MyGLFunc.checkGlError2("u_matM",this,model)

        // uniform(モデル×ビュー×プロジェクション)
        GLES20.glGetUniformLocation(programHandle,"u_matMVP").also {
            GLES20.glUniformMatrix4fv(it,1,false,matMVP,0)
        }
        MyGLFunc.checkGlError2("u_matMVP",this,model)

        // uniform(逆行列)
        GLES20.glGetUniformLocation(programHandle,"u_matINV").also {
            GLES20.glUniformMatrix4fv(it,1,false,matINV,0)
        }
        MyGLFunc.checkGlError2("u_matINV",this,model)

        // uniform(テクスチャ射影変換用行列)
        GLES20.glGetUniformLocation(programHandle,"u_matVPT").also {
            GLES20.glUniformMatrix4fv(it,1,false,matVPT,0)
        }
        MyGLFunc.checkGlError2("u_matVPT",this,model)

        // uniform(ライト視点の座標変換行列)
        GLES20.glGetUniformLocation(programHandle,"u_matLight").also {
            GLES20.glUniformMatrix4fv(it,1,false,matLight,0)
        }
        MyGLFunc.checkGlError2("u_matLight",this,model)

        // uniform(光源位置)
        GLES20.glGetUniformLocation(programHandle,"u_vecLight").also {
            GLES20.glUniform3fv(it,1,u_vecLight,0)
        }
        MyGLFunc.checkGlError2("u_vecLight",this,model)

        // uniform(テクスチャユニット)
        GLES20.glGetUniformLocation(programHandle, "u_Texture0").also {
            GLES20.glUniform1i(it, u_Texture0)
        }
        MyGLFunc.checkGlError2("u_Texture0",this,model)

        // モデルを描画
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, model.datIdx.size, GLES20.GL_UNSIGNED_SHORT, model.bufIdx)
    }
}
