package milu.kiriu2010.exdb1.opengl04.w040

import android.opengl.GLES20
import milu.kiriu2010.gui.basic.MyGLES20Func
import milu.kiriu2010.gui.model.MgModelAbs
import milu.kiriu2010.gui.shader.es20.ES20MgShader

// ---------------------------------------
// フレームバッファ:VBOなし
// OpenGL ES 2.0
// ---------------------------------------
// https://wgld.org/d/webgl/w040.html
// ---------------------------------------
class W040Shader: ES20MgShader() {
    // 頂点シェーダ
    private val scv =
            """
            attribute vec3  a_Position;
            attribute vec3  a_Normal;
            attribute vec4  a_Color;
            attribute vec2  a_TextureCoord;
            uniform   mat4  u_matM;
            uniform   mat4  u_matMVP;
            uniform   mat4  u_matINV;
            uniform   vec3  u_vecLight;
            uniform   bool  u_useLight;
            varying   vec4  v_Color;
            varying   vec2  v_TextureCoord;

            void main() {
                if (u_useLight) {
                    vec3  invLight = normalize(u_matINV * vec4(u_vecLight, 0.0)).xyz;
                    float diffuse  = clamp(dot(a_Normal, invLight), 0.2, 1.0);
                    v_Color        = vec4(a_Color.xyz * vec3(diffuse), 1.0);
                }
                else {
                    v_Color        = a_Color;
                }
                v_TextureCoord = a_TextureCoord;
                gl_Position    = u_matMVP * vec4(a_Position, 1.0);
            }
            """.trimIndent()

    // フラグメントシェーダ
    private val scf =
            """
            precision mediump   float;

            uniform   sampler2D u_Texture0;
            varying   vec4      v_Color;
            varying   vec2      v_TextureCoord;

            void main() {
                vec4 smpColor = texture2D(u_Texture0, v_TextureCoord);
                gl_FragColor  = v_Color * smpColor;
            }
            """.trimIndent()

    override fun loadShader(): ES20MgShader {
        // 頂点シェーダを生成
        val svhandle = MyGLES20Func.loadShader(GLES20.GL_VERTEX_SHADER, scv)
        // フラグメントシェーダを生成
        val sfhandle = MyGLES20Func.loadShader(GLES20.GL_FRAGMENT_SHADER, scf)

        // プログラムオブジェクトの生成とリンク
        programHandle = MyGLES20Func.createProgram(svhandle,sfhandle, arrayOf("a_Position","a_Normal","a_Color","a_TextureCoord") )
        return this
    }


    fun draw(model: MgModelAbs,
             matM: FloatArray,
             matMVP: FloatArray,
             matI: FloatArray,
             u_vecLight: FloatArray,
             u_useLight: Int,
             u_Texture0: Int) {
        GLES20.glUseProgram(programHandle)
        MyGLES20Func.checkGlError2("UserProgram",this,model)

        // attribute(頂点)
        model.bufPos.position(0)
        GLES20.glGetAttribLocation(programHandle,"a_Position").also {
            GLES20.glVertexAttribPointer(it,3,GLES20.GL_FLOAT,false, 3*4, model.bufPos)
            GLES20.glEnableVertexAttribArray(it)
        }
        MyGLES20Func.checkGlError2("a_Position",this,model)

        // attribute(法線)
        model.bufNor.position(0)
        GLES20.glGetAttribLocation(programHandle,"a_Normal").also {
            GLES20.glVertexAttribPointer(it,3,GLES20.GL_FLOAT,false, 3*4, model.bufNor)
            GLES20.glEnableVertexAttribArray(it)
        }
        MyGLES20Func.checkGlError2("a_Normal",this,model)

        // attribute(色)
        model.bufCol.position(0)
        GLES20.glGetAttribLocation(programHandle,"a_Color").also {
            GLES20.glVertexAttribPointer(it,4,GLES20.GL_FLOAT,false, 4*4, model.bufCol)
            GLES20.glEnableVertexAttribArray(it)
        }
        MyGLES20Func.checkGlError2("a_Color",this,model)

        // attribute(テクスチャ座標)
        model.bufTxc.position(0)
        GLES20.glGetAttribLocation(programHandle,"a_TextureCoord").also {
            GLES20.glVertexAttribPointer(it,2,GLES20.GL_FLOAT,false, 2*4, model.bufTxc)
            GLES20.glEnableVertexAttribArray(it)
        }
        MyGLES20Func.checkGlError2("a_TextureCoord",this,model)

        // uniform(モデル)
        GLES20.glGetUniformLocation(programHandle,"u_matM").also {
            GLES20.glUniformMatrix4fv(it,1,false,matM,0)
        }
        MyGLES20Func.checkGlError2("u_matM",this,model)

        // uniform(モデル×ビュー×プロジェクション)
        GLES20.glGetUniformLocation(programHandle,"u_matMVP").also {
            GLES20.glUniformMatrix4fv(it,1,false,matMVP,0)
        }
        MyGLES20Func.checkGlError2("u_matMVP",this,model)

        // uniform(逆行列)
        GLES20.glGetUniformLocation(programHandle,"u_matINV").also {
            GLES20.glUniformMatrix4fv(it,1,false,matI,0)
        }
        MyGLES20Func.checkGlError2("u_matINV",this,model)

        // uniform(光源位置)
        GLES20.glGetUniformLocation(programHandle,"u_vecLight").also {
            GLES20.glUniform3fv(it,1,u_vecLight,0)
        }
        MyGLES20Func.checkGlError2("u_vecLight",this,model)

        // uniform(ライティングを使うかどうか)
        GLES20.glGetUniformLocation(programHandle,"u_useLight").also {
            GLES20.glUniform1i(it,u_useLight)
        }
        MyGLES20Func.checkGlError2("u_useLight",this,model)

        // uniform(テクスチャユニット)
        GLES20.glGetUniformLocation(programHandle, "u_Texture0").also {
            GLES20.glUniform1i(it, u_Texture0)
        }
        MyGLES20Func.checkGlError2("u_Texture0",this,model)

        // モデルを描画
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, model.datIdx.size, GLES20.GL_UNSIGNED_SHORT, model.bufIdx)
    }
}
