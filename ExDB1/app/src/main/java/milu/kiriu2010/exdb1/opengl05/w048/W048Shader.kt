package milu.kiriu2010.exdb1.opengl05.w048

import android.opengl.GLES20
import milu.kiriu2010.gui.basic.MyGLFunc
import milu.kiriu2010.gui.model.MgModelAbs
import milu.kiriu2010.gui.shader.MgShader

// -----------------------------------------
// トゥーンレンダリング
// -----------------------------------------
// https://wgld.org/d/webgl/w048.html
// -----------------------------------------
class W048Shader: MgShader() {
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
                    // 色として出力する変数diffuseの値を、テクスチャの参照に使っている
                    vec4  smpColor = texture2D(u_Texture0, vec2(diffuse,0.0));
                    gl_FragColor   = v_Color * smpColor;
                }
            }
            """.trimIndent()

    override fun loadShader(): MgShader {
        // 頂点シェーダを生成
        val svhandle = MyGLFunc.loadShader(GLES20.GL_VERTEX_SHADER, scv)
        // フラグメントシェーダを生成
        val sfhandle = MyGLFunc.loadShader(GLES20.GL_FRAGMENT_SHADER, scf)

        // プログラムオブジェクトの生成とリンク
        programHandle = MyGLFunc.createProgram(svhandle,sfhandle, arrayOf("a_Position","a_Normal","a_Color") )
        return this
    }


    fun draw(model: MgModelAbs,
             matMVP: FloatArray,
             matINV: FloatArray,
             u_vecLight: FloatArray,
             u_Texture0: Int,
             u_edge: Int,
             u_EdgeColor: FloatArray) {
        GLES20.glUseProgram(programHandle)
        MyGLFunc.checkGlError2("UserProgram",this,model)

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

        // uniform(エッジをつけるかどうか)
        GLES20.glGetUniformLocation(programHandle,"u_edge").also {
            GLES20.glUniform1i(it,u_edge)
        }
        MyGLFunc.checkGlError2("u_edge",this,model)

        // uniform(エッジの色)
        GLES20.glGetUniformLocation(programHandle,"u_EdgeColor").also {
            GLES20.glUniform4fv(it,1,u_EdgeColor,0)
        }
        MyGLFunc.checkGlError2("u_EdgeColor",this,model)

        // モデルを描画
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, model.datIdx.size, GLES20.GL_UNSIGNED_SHORT, model.bufIdx)
    }
}
