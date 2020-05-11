package milu.kiriu2010.exdb1.es30x01.a03

import android.opengl.GLES30
import milu.kiriu2010.gui.model.MgModelAbs
import milu.kiriu2010.gui.basic.MyGLES30Func
import milu.kiriu2010.gui.shader.es30.ES30MgShader

// ----------------------------------------------
// GLSL ES 3.0:VBOなし
// OpenGL ES 3.0
// シェーダA
// ----------------------------------------------
// https://wgld.org/d/webgl2/w003.html
// ----------------------------------------------
class ES30a03ShaderA: ES30MgShader() {
    // 頂点シェーダ
    private val scv =
            """#version 300 es

            in vec3  a_Position;
            in vec3  a_Normal;
            in vec2  a_TexCoord;

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
             matM: FloatArray,
             matMVP: FloatArray,
             matN: FloatArray,
             u_vecLight: FloatArray,
             u_vecEye: FloatArray,
             u_Texture: Int) {

        GLES30.glUseProgram(programHandle)
        MyGLES30Func.checkGlError2("UseProgram",this,model)

        // attribute(頂点)
        model.bufPos.position(0)
        val hPosition = GLES30.glGetAttribLocation(programHandle,"a_Position").also {
            if ( it != -1 ) {
                GLES30.glVertexAttribPointer(it,3,GLES30.GL_FLOAT,false, 3*4, model.bufPos)
                GLES30.glEnableVertexAttribArray(it)
            }
        }
        MyGLES30Func.checkGlError2("a_Position",this,model)

        // attribute(法線)
        model.bufNor.position(0)
        val hNormal = GLES30.glGetAttribLocation(programHandle,"a_Normal").also {
            if ( it != -1 ) {
                GLES30.glVertexAttribPointer(it,3,GLES30.GL_FLOAT,false, 3*4, model.bufNor)
                GLES30.glEnableVertexAttribArray(it)
            }
        }
        MyGLES30Func.checkGlError2("a_Normal",this,model)

        // attribute(テクスチャ座標)
        model.bufTxc.position(0)
        val hTexCoord = GLES30.glGetAttribLocation(programHandle,"a_TexCoord").also {
            if ( it != -1 ) {
                GLES30.glVertexAttribPointer(it,4,GLES30.GL_FLOAT,false, 2*4, model.bufTxc)
                GLES30.glEnableVertexAttribArray(it)
            }
        }
        MyGLES30Func.checkGlError2("a_TexCoord",this,model)

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

        // リソース解放
        if ( hPosition != -1 ) {
            GLES30.glDisableVertexAttribArray(hPosition)
        }
        if ( hNormal != -1 ) {
            GLES30.glDisableVertexAttribArray(hNormal)
        }
        if ( hTexCoord != -1 ) {
            GLES30.glDisableVertexAttribArray(hTexCoord)
        }
    }
}
