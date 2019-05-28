package milu.kiriu2010.exdb1.opengl03.w037

import android.opengl.GLES20
import milu.kiriu2010.gui.basic.MyGLES20Func
import milu.kiriu2010.gui.model.MgModelAbs
import milu.kiriu2010.gui.shader.es20.ES20MgShader

// ------------------------------------
// ポイントスプライト
// ------------------------------------
// https://wgld.org/d/webgl/w037.html
// ------------------------------------
class W037Shader: ES20MgShader() {
    // 頂点シェーダ
    private val scv =
            """
            attribute vec3  a_Position;
            attribute vec4  a_Color;
            uniform   mat4  u_matMVP;
            uniform   float u_pointSize;
            varying   vec4  v_Color;

            void main() {
                v_Color        = a_Color;
                gl_Position    = u_matMVP * vec4(a_Position,1.0);
                gl_PointSize   = u_pointSize;
            }
            """.trimIndent()

    // フラグメントシェーダ
    private val scf =
            """
            precision mediump   float;

            uniform   sampler2D u_Texture0;
            uniform   int       u_useTexture;
            varying   vec4      v_Color;

            void main() {
                vec4 smpColor = vec4(1.0);
                if (bool(u_useTexture)) {
                    // -----------------------------------------
                    // gl_PointCoord
                    //   描画される点上のテクスチャ座標が入る
                    //   レンダリングされる点そのものに対して
                    //   テクスチャを適用する場合に指定する
                    // -----------------------------------------
                    smpColor = texture2D(u_Texture0, gl_PointCoord);
                }

                // -------------------------------------------------------------------------
                // 完全に透過している場合、
                // フラグメントシェーダが何もしないようにする
                // -------------------------------------------------------------------------
                // アルファブレンドでは奥にあるものからレンダリングしていくのが基本だが
                // ポイントスプライトを用いた処理では、
                // 点と点との距離が近い場合や、カメラからみて重なってしまうような座標にある場合、
                // 適切にZソートしないと希望通りの結果が得られない。
                // -------------------------------------------------------------------------
                if (smpColor.a == 0.0) {
                    discard;
                }
                else {
                    gl_FragColor  = v_Color * smpColor;
                }
            }
            """.trimIndent()

    override fun loadShader(): ES20MgShader {
        // 頂点シェーダを生成
        val svhandle = MyGLES20Func.loadShader(GLES20.GL_VERTEX_SHADER, scv)
        // フラグメントシェーダを生成
        val sfhandle = MyGLES20Func.loadShader(GLES20.GL_FRAGMENT_SHADER, scf)

        // プログラムオブジェクトの生成とリンク
        programHandle = MyGLES20Func.createProgram(svhandle,sfhandle, arrayOf("a_Position","a_Color","a_TextureCoord") )
        return this
    }


    fun draw(model: MgModelAbs,
             matMVP: FloatArray,
             u_pointSize: Float,
             u_Texture0: Int,
             u_useTexture: Int,
             lineType: Int) {

        // attribute(頂点)
        model.bufPos.position(0)
        GLES20.glGetAttribLocation(programHandle,"a_Position").also {
            GLES20.glVertexAttribPointer(it,3,GLES20.GL_FLOAT,false, 3*4, model.bufPos)
            GLES20.glEnableVertexAttribArray(it)
        }
        MyGLES20Func.checkGlError2("a_Position",this,model)

        // attribute(色)
        model.bufCol.position(0)
        GLES20.glGetAttribLocation(programHandle,"a_Color").also {
            GLES20.glVertexAttribPointer(it,4,GLES20.GL_FLOAT,false, 4*4, model.bufCol)
            GLES20.glEnableVertexAttribArray(it)
        }
        MyGLES20Func.checkGlError2("a_Color",this,model)

        // uniform(モデル×ビュー×プロジェクション)
        GLES20.glGetUniformLocation(programHandle,"u_matMVP").also {
            GLES20.glUniformMatrix4fv(it,1,false,matMVP,0)
        }
        MyGLES20Func.checkGlError2("u_matMVP",this,model)

        // uniform(描画点のサイズ)
        GLES20.glGetUniformLocation(programHandle,"u_pointSize").also {
            GLES20.glUniform1f(it,u_pointSize)
        }
        MyGLES20Func.checkGlError2("u_pointSize",this,model)

        // uniform(テクスチャユニット)
        if ( u_Texture0 >= 0 ) {
            GLES20.glGetUniformLocation(programHandle, "u_Texture0").also {
                GLES20.glUniform1i(it, u_Texture0)
            }
            MyGLES20Func.checkGlError2("u_Texture0",this,model)
        }

        // uniform(テクスチャを使うかどうか)
        GLES20.glGetUniformLocation(programHandle, "u_useTexture").also {
            GLES20.glUniform1i(it, u_useTexture)
        }
        MyGLES20Func.checkGlError2("u_useTexture",this,model)

        // モデルを描画
        GLES20.glDrawArrays(lineType, 0, model.datPos.size/3)
    }
}
