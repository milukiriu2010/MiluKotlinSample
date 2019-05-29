package milu.kiriu2010.exdb1.es30x01.a06

import android.opengl.GLES30
import milu.kiriu2010.gui.model.MgModelAbs
import milu.kiriu2010.gui.basic.MyGLES30Func
import milu.kiriu2010.gui.shader.es30.ES30MgShader

// ------------------------------------
// シェーダA
// ------------------------------------
// https://wgld.org/d/webgl2/w006.html
// https://github.com/danginsburg/opengles3-book/blob/master/Android_Java/Chapter_6/VertexArrayObjects/src/com/openglesbook/VertexArrayObjects/VAORenderer.java
// ------------------------------------
class ES30a06ShaderA: ES30MgShader() {
    // 頂点シェーダ
    private val scv =
            """#version 300 es

            layout (location = 0) in vec3  a_Position;
            layout (location = 1) in vec3  a_Normal;
            layout (location = 2) in vec2  a_TexCoord;

            uniform  mat4  u_matM;
            uniform  mat4  u_matMVP;
            uniform  mat4  u_matN;

            out vec3  v_Position;
            out vec3  v_Normal;
            out vec2  v_TexCoord;

            void main() {
                v_Position  = (u_matM * vec4(a_Position,1.0)).xyz;
                v_Normal    = (u_matN * vec4(a_Normal  ,0.0)).xyz;
                v_TexCoord  = a_TexCoord;
                gl_Position = u_matMVP   * vec4(a_Position, 1.0);
            }
            """.trimIndent()

    // フラグメントシェーダ
    private val scf =
            """#version 300 es

            precision highp   float;

            uniform  vec3      u_vecLight;
            uniform  vec3      u_vecEye;
            uniform  sampler2D u_Texture;

            in  vec3  v_Position;
            in  vec3  v_Normal;
            in  vec2  v_TexCoord;

            out vec4  o_Color;

            void main() {
                vec3  light    = normalize(u_vecLight - v_Position);
                vec3  eye      = normalize(v_Position - u_vecEye);
                vec3  ref      = normalize(reflect(eye,v_Normal));
                float diffuse  = max(dot(light,v_Normal),0.2);
                float specular = max(dot(light,ref)     ,0.0);
                specular = pow(specular,20.0);
                vec4 samplerColor = texture(u_Texture,v_TexCoord);
                o_Color = vec4(samplerColor.rgb*diffuse + specular, samplerColor.a);
            }
            """.trimIndent()


    override fun loadShader(): ES30MgShader {
        // 頂点シェーダを生成
        svhandle = MyGLES30Func.loadShader(GLES30.GL_VERTEX_SHADER, scv)
        // フラグメントシェーダを生成
        sfhandle = MyGLES30Func.loadShader(GLES30.GL_FRAGMENT_SHADER, scf)

        // プログラムオブジェクトの生成とリンク
        programHandle = MyGLES30Func.createProgram(svhandle,sfhandle, arrayOf("a_Position","a_Normal","a_TexCoord") )
        return this
    }


    fun draw(model: MgModelAbs,
             vao: A06VaoA,
             matM: FloatArray,
             matMVP: FloatArray,
             matN: FloatArray,
             u_vecLight: FloatArray,
             u_vecEye: FloatArray,
             u_Texture: Int) {

        GLES30.glUseProgram(programHandle)
        MyGLES30Func.checkGlError2("UseProgram",this,model)

        // VAOをバインド
        GLES30.glBindVertexArray(vao.mVAOIds[0])
        MyGLES30Func.checkGlError2("BindVertexArray",this,model)

        // uniform(モデル)
        GLES30.glGetUniformLocation(programHandle,"u_matM").also {
            GLES30.glUniformMatrix4fv(it,1,false,matM,0)
        }
        MyGLES30Func.checkGlError2("u_matM",this,model)

        // uniform(モデル×ビュー×プロジェクション)
        GLES30.glGetUniformLocation(programHandle,"u_matMVP").also {
            GLES30.glUniformMatrix4fv(it,1,false,matMVP,0)
        }
        MyGLES30Func.checkGlError2("u_matMVP",this,model)

        // uniform()
        GLES30.glGetUniformLocation(programHandle,"u_matN").also {
            GLES30.glUniformMatrix4fv(it,1,false,matN,0)
        }
        MyGLES30Func.checkGlError2("u_matN",this,model)

        // uniform(光源位置)
        GLES30.glGetUniformLocation(programHandle,"u_vecLight").also {
            GLES30.glUniform3fv(it,1,u_vecLight,0)
        }
        MyGLES30Func.checkGlError2("u_vecLight",this,model)

        // uniform(視点座標)
        GLES30.glGetUniformLocation(programHandle,"u_vecEye").also {
            GLES30.glUniform3fv(it,1,u_vecEye,0)
        }
        MyGLES30Func.checkGlError2("u_vecEye",this,model)

        // uniform(テクスチャ座標)
        GLES30.glGetUniformLocation(programHandle, "u_Texture").also {
            GLES30.glUniform1i(it, u_Texture)
        }
        MyGLES30Func.checkGlError2("u_Texture",this,model)

        // モデルを描画
        GLES30.glDrawElements(GLES30.GL_TRIANGLES, model.datIdx.size, GLES30.GL_UNSIGNED_SHORT, model.bufIdx)

        // VAO解放
        GLES30.glBindVertexArray(0)
    }
}
