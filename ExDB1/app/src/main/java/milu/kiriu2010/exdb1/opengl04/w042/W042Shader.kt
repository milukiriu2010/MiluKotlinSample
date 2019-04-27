package milu.kiriu2010.exdb1.opengl04.w042

import android.opengl.GLES20
import milu.kiriu2010.gui.basic.MyGLFunc
import milu.kiriu2010.gui.model.MgModelAbs
import milu.kiriu2010.gui.shader.MgShader

class W042Shader: MgShader() {
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
            uniform   vec3  u_vecEye;
            varying   vec4  v_Color;
            varying   vec2  v_TextureCoord;
            varying   vec3  v_vecLight;
            varying   vec3  v_vecEye;

            void main() {
                vec3 pos      = (u_matM   * vec4(a_Position,0.0)).xyz;
                vec3 invEye   = (u_matINV * vec4(u_vecEye,0.0)).xyz;
                vec3 invLight = (u_matINV * vec4(u_vecLight,0.0)).xyz;
                vec3 eye      = invEye - pos;
                vec3 light    = invLight - pos;
                vec3 n = normalize(a_Normal);
                vec3 t = normalize(cross(a_Normal,vec3(0.0,1.0,0.0)));
                vec3 b = cross(n,t);
                v_vecEye.x = dot(t,eye);
                v_vecEye.y = dot(b,eye);
                v_vecEye.z = dot(n,eye);
                normalize(v_vecEye);
                v_vecLight.x = dot(t,light);
                v_vecLight.y = dot(b,light);
                v_vecLight.z = dot(n,light);
                normalize(v_vecLight);
                v_Color        = a_Color;
                v_TextureCoord = a_TextureCoord;
                gl_Position    = u_matMVP * vec4(a_Position,1.0);
            }
            """.trimIndent()

    // フラグメントシェーダ
    private val scf =
            """
            precision mediump   float;

            uniform   sampler2D u_Texture0;
            varying   vec4      v_Color;
            varying   vec2      v_TextureCoord;
            varying   vec3      v_vecLight;
            varying   vec3      v_vecEye;

            void main() {
                vec3  mNormal   = (texture2D(u_Texture0, v_TextureCoord) * 2.0 - 1.0).rgb;
                vec3  light     = normalize(v_vecLight);
                vec3  eye       = normalize(v_vecEye);
                vec3  halfLE    = normalize(light+eye);
                float diffuse   = clamp(dot(mNormal, light), 0.1, 1.0);
                float specular  = pow(clamp(dot(mNormal,halfLE) ,0.0,1.0), 100.0);
                vec4  destColor = v_Color * vec4(vec3(diffuse),1.0) + vec4(vec3(specular),1.0);
                gl_FragColor  = destColor;
            }
            """.trimIndent()

    override fun loadShader(): MgShader {
        // 頂点シェーダを生成
        val svhandle = MyGLFunc.loadShader(GLES20.GL_VERTEX_SHADER, scv)
        // フラグメントシェーダを生成
        val sfhandle = MyGLFunc.loadShader(GLES20.GL_FRAGMENT_SHADER, scf)

        // プログラムオブジェクトの生成とリンク
        programHandle = MyGLFunc.createProgram(svhandle,sfhandle, arrayOf("a_Position","a_Normal","a_Color","a_TextureCoord") )
        return this
    }


    fun draw(model: MgModelAbs,
             matM: FloatArray,
             matMVP: FloatArray,
             matI: FloatArray,
             u_vecLight: FloatArray,
             u_vecEye: FloatArray,
             u_Texture0: Int) {

        GLES20.glUseProgram(programHandle)

        // attribute(頂点)
        model.bufPos.position(0)
        GLES20.glGetAttribLocation(programHandle,"a_Position").also {
            GLES20.glVertexAttribPointer(it,3,GLES20.GL_FLOAT,false, 3*4, model.bufPos)
            GLES20.glEnableVertexAttribArray(it)
        }
        MyGLFunc.checkGlError("a_Position:${javaClass.simpleName}")

        // attribute(法線)
        model.bufNor.position(0)
        GLES20.glGetAttribLocation(programHandle,"a_Normal").also {
            GLES20.glVertexAttribPointer(it,3,GLES20.GL_FLOAT,false, 3*4, model.bufNor)
            GLES20.glEnableVertexAttribArray(it)
        }
        MyGLFunc.checkGlError("a_Normal")

        // attribute(色)
        model.bufCol.position(0)
        GLES20.glGetAttribLocation(programHandle,"a_Color").also {
            GLES20.glVertexAttribPointer(it,4,GLES20.GL_FLOAT,false, 4*4, model.bufCol)
            GLES20.glEnableVertexAttribArray(it)
        }
        MyGLFunc.checkGlError("a_Color")

        // attribute(テクスチャコード)
        model.bufTxc.position(0)
        GLES20.glGetAttribLocation(programHandle,"a_TextureCoord").also {
            GLES20.glVertexAttribPointer(it,2,GLES20.GL_FLOAT,false, 2*4, model.bufTxc)
            GLES20.glEnableVertexAttribArray(it)
        }
        MyGLFunc.checkGlError("a_TextureCoord")

        // uniform(モデル)
        GLES20.glGetUniformLocation(programHandle,"u_matM").also {
            GLES20.glUniformMatrix4fv(it,1,false,matM,0)
        }
        MyGLFunc.checkGlError("u_matM")

        // uniform(モデル×ビュー×プロジェクション)
        GLES20.glGetUniformLocation(programHandle,"u_matMVP").also {
            GLES20.glUniformMatrix4fv(it,1,false,matMVP,0)
        }
        MyGLFunc.checkGlError("u_matMVP")

        // uniform(逆行列)
        GLES20.glGetUniformLocation(programHandle,"u_matINV").also {
            GLES20.glUniformMatrix4fv(it,1,false,matI,0)
        }

        // uniform(ライト座標)
        GLES20.glGetUniformLocation(programHandle,"u_vecLight").also {
            GLES20.glUniform3fv(it,1,u_vecLight,0)
        }

        // uniform(視点座標)
        GLES20.glGetUniformLocation(programHandle,"u_vecEye").also {
            GLES20.glUniform3fv(it,1,u_vecEye,0)
        }

        // uniform(テクスチャ0)
        GLES20.glGetUniformLocation(programHandle, "u_Texture0").also {
            GLES20.glUniform1i(it, u_Texture0)
        }
        MyGLFunc.checkGlError("u_Texture0")

        // モデルを描画
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, model.datIdx.size, GLES20.GL_UNSIGNED_SHORT, model.bufIdx)
    }
}
