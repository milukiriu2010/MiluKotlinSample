package milu.kiriu2010.exdb1.opengl05.w048v

import android.opengl.GLES20
import milu.kiriu2010.gui.basic.MyGLES20Func
import milu.kiriu2010.gui.model.MgModelAbs
import milu.kiriu2010.gui.shader.es20.ES20MgShader
import milu.kiriu2010.gui.vbo.es20.ES20VBOAbs

// -----------------------------------------
// トゥーンレンダリング:VBOあり
// OpenGL ES 2.0
// -----------------------------------------
// https://wgld.org/d/webgl/w048.html
// -----------------------------------------
class WV048Shader: ES20MgShader() {
    // 頂点シェーダ
    private val scv =
            """
            attribute vec3  a_Position;
            attribute vec3  a_Normal;
            attribute vec4  a_Color;
            uniform   mat4  u_matMVP;
            uniform   int   u_edge;
            varying   vec3  v_Normal;
            varying   vec4  v_Color;

            void main() {
                vec3 pos      = a_Position;
                // エッジ用モデルを描画する場合、
                // モデルを法線方向に少しだけ膨らませる。
                if (bool(u_edge)) {
                    pos += a_Normal * 0.05;
                }
                v_Normal      = a_Normal;
                v_Color       = a_Color;
                gl_Position   = u_matMVP * vec4(pos,1.0);
            }
            """.trimIndent()

    // フラグメントシェーダ
    private val scf =
            """
            precision mediump     float;

            uniform   mat4        u_matINV;
            uniform   vec3        u_vecLight;
            uniform   sampler2D   u_Texture0;
            uniform   vec4        u_EdgeColor;
            varying   vec3        v_Normal;
            varying   vec4        v_Color;

            void main() {
                // 色のアルファ値
                // 0より大きい⇒エッジ用に使うためuniformの値をそのまま使う
                // 0         ⇒ライティングの計算を行う
                if (u_EdgeColor.a > 0.0) {
                    gl_FragColor    = u_EdgeColor;
                }
                else {
                    vec3  invLight = normalize(u_matINV * vec4(u_vecLight,0.0)).xyz;
                    float diffuse  = clamp(dot(v_Normal,u_vecLight), 0.0, 1.0);
                    // -------------------------------------------------------------
                    // 色として出力する変数diffuseの値を、テクスチャの参照に使っている
                    // -------------------------------------------------------------
                    // 左が黒で右へ、だんだん白くなっていくテクスチャなので、
                    // 光が強く当たっているほど右側のテクセルを参照することになり、
                    // 結果的にモデルの色がそのまま出力される。
                    // 逆に光が当たっていない部分ほど
                    // 左側のテクセルを参照することになるので、
                    // モデルの色が若干暗くなる。
                    // -------------------------------------------------------------
                    vec4  smpColor = texture2D(u_Texture0, vec2(diffuse,0.0));
                    gl_FragColor   = v_Color * smpColor;
                }
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
        hUNI = IntArray(6)

        // uniform(モデル×ビュー×プロジェクション)
        hUNI[0] = GLES20.glGetUniformLocation(programHandle,"u_matMVP")
        MyGLES20Func.checkGlError("u_matMVP:glGetUniformLocation")

        // uniform(逆行列)
        hUNI[1] = GLES20.glGetUniformLocation(programHandle,"u_matINV")
        MyGLES20Func.checkGlError("u_matINV:glGetUniformLocation")

        // uniform(平行光源)
        hUNI[2] = GLES20.glGetUniformLocation(programHandle,"u_vecLight")
        MyGLES20Func.checkGlError("u_vecLight:glGetUniformLocation")

        // uniform(テクスチャユニット)
        hUNI[3] = GLES20.glGetUniformLocation(programHandle, "u_Texture0")
        MyGLES20Func.checkGlError("u_Texture0:glGetUniformLocation")

        // uniform(エッジをつけるかどうか)
        hUNI[4] = GLES20.glGetUniformLocation(programHandle,"u_edge")
        MyGLES20Func.checkGlError("u_edge:glGetUniformLocation")

        // uniform(エッジの色)
        hUNI[5] = GLES20.glGetUniformLocation(programHandle,"u_EdgeColor")
        MyGLES20Func.checkGlError("u_EdgeColor:glGetUniformLocation")

        return this
    }

    fun draw(model: MgModelAbs,
             bo: ES20VBOAbs,
             u_matMVP: FloatArray,
             u_matI: FloatArray,
             u_vecLight: FloatArray,
             u_Texture0: Int,
             u_edge: Int,
             u_EdgeColor: FloatArray) {
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

        // uniform(モデル×ビュー×プロジェクション)
        GLES20.glUniformMatrix4fv(hUNI[0],1,false,u_matMVP,0)
        MyGLES20Func.checkGlError2("u_matMVP",this,model)

        // uniform(逆行列)
        GLES20.glUniformMatrix4fv(hUNI[1],1,false,u_matI,0)
        MyGLES20Func.checkGlError2("u_matINV",this,model)

        // uniform(平行光源)
        GLES20.glUniform3fv(hUNI[2],1,u_vecLight,0)
        MyGLES20Func.checkGlError2("u_vecLight",this,model)

        // uniform(テクスチャユニット)
        GLES20.glUniform1i(hUNI[3], u_Texture0)
        MyGLES20Func.checkGlError2("u_Texture0",this,model)

        // uniform(エッジをつけるかどうか)
        GLES20.glUniform1i(hUNI[4],u_edge)
        MyGLES20Func.checkGlError2("u_edge",this,model)

        // uniform(エッジの色)
        GLES20.glUniform4fv(hUNI[5],1,u_EdgeColor,0)
        MyGLES20Func.checkGlError2("u_EdgeColor",this,model)

        // モデルを描画
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, model.datIdx.size, GLES20.GL_UNSIGNED_SHORT, model.bufIdx)

        // リソース解放
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,0)
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER,0)
    }
}
