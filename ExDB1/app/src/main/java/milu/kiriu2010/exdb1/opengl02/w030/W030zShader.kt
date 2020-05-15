package milu.kiriu2010.exdb1.opengl02.w030

import android.opengl.GLES20
import milu.kiriu2010.gui.basic.MyGLES20Func
import milu.kiriu2010.gui.model.MgModelAbs
import milu.kiriu2010.gui.shader.es20.ES20MgShader

// ---------------------------------------------------
// ブレンドファクター:VBOなし
// OpenGL ES 2.0
// ---------------------------------------------------
// https://wgld.org/d/webgl/w030.html
// ---------------------------------------------------
class W030zShader: ES20MgShader() {
    // 頂点シェーダ
    private val scv =
            """
            attribute vec3  a_Position;
            attribute vec4  a_Color;
            attribute vec2  a_TextureCoord;
            uniform   mat4  u_matMVP;
            uniform   float u_vertexAlpha;
            varying   vec4  v_Color;
            varying   vec2  v_TextureCoord;

            void main() {
                v_Color        = vec4(a_Color.rgb, a_Color.a * u_vertexAlpha);
                v_TextureCoord = a_TextureCoord;
                gl_Position    = u_matMVP * vec4(a_Position,1.0);
            }
            """.trimIndent()

    // フラグメントシェーダ
    private val scf =
            """
            precision mediump float;

            uniform   sampler2D  u_Texture0;
            uniform   int        u_useTexture;
            varying   vec4       v_Color;
            varying   vec2       v_TextureCoord;

            void main() {
                vec4 destColor = vec4(0.0);
                if (bool(u_useTexture)) {
                    vec4 smpColor = texture2D(u_Texture0, v_TextureCoord);
                    destColor = v_Color * smpColor;
                }
                else {
                    destColor = v_Color;
                }
                gl_FragColor   = destColor;
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
             u_vertexAlpha: Float,
             u_Texture0: Int,
             u_useTexture: Int
    ) {
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

        // attribute(テクスチャコード)
        model.bufTxc.position(0)
        GLES20.glGetAttribLocation(programHandle,"a_TextureCoord").also {
            GLES20.glVertexAttribPointer(it,2,GLES20.GL_FLOAT,false, 2*4, model.bufTxc)
            GLES20.glEnableVertexAttribArray(it)
        }
        MyGLES20Func.checkGlError2("a_TextureCoord",this,model)

        // uniform(モデル×ビュー×プロジェクション)
        GLES20.glGetUniformLocation(programHandle,"u_matMVP").also {
            GLES20.glUniformMatrix4fv(it,1,false,matMVP,0)
        }
        MyGLES20Func.checkGlError2("u_matMVP",this,model)

        // uniform(ブレンドするアルファ成分の割合)
        GLES20.glGetUniformLocation(programHandle, "u_vertexAlpha").also {
            GLES20.glUniform1f(it, u_vertexAlpha)
        }
        MyGLES20Func.checkGlError2("u_vertexAlpha",this,model)

        // uniform(テクスチャユニット0)
        GLES20.glGetUniformLocation(programHandle, "u_Texture0").also {
            GLES20.glUniform1i(it, u_Texture0)
        }
        MyGLES20Func.checkGlError2("u_Texture0",this,model)

        // uniform(テクスチャのレンダリングをするかどうか)
        GLES20.glGetUniformLocation(programHandle, "u_useTexture").also {
            GLES20.glUniform1i(it, u_useTexture)
        }
        MyGLES20Func.checkGlError2("u_useTexture",this,model)

        // モデルを描画
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, model.datIdx.size, GLES20.GL_UNSIGNED_SHORT, model.bufIdx)
    }
}
