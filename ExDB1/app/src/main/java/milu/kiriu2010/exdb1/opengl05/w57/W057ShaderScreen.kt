package milu.kiriu2010.exdb1.opengl05.w57

import android.opengl.GLES20
import milu.kiriu2010.exdb1.opengl.ModelAbs
import milu.kiriu2010.exdb1.opengl.MyGLFunc

// スクリーンレンダリング用シェーダ
class W057ShaderScreen {
    // 頂点シェーダ
    private val scv =
            """
            attribute vec3  a_Position;
            attribute vec3  a_Normal;
            attribute vec4  a_Color;
            uniform   mat4  u_matMVP;
            uniform   mat4  u_matINV;
            uniform   vec3  u_vecLight;
            uniform   vec3  u_vecEye;
            uniform   vec4  u_ambientColor;
            varying   vec4  v_Color;

            void main() {
                vec3   invLight = normalize(u_matINV * vec4(u_vecLight, 0.0)).xyz;
                vec3   invEye   = normalize(u_matINV * vec4(u_vecEye  , 0.0)).xyz;
                vec3   halfLE   = normalize(invLight + invEye);
                float  diffuse  = clamp(dot(a_Normal,invLight), 0.0, 1.0);
                float  specular = pow(clamp(dot(a_Normal, halfLE), 0.0, 1.0), 50.0);
                vec4   amb      = a_Color * u_ambientColor;
                v_Color         = amb * vec4(vec3(diffuse),1.0) + vec4(vec3(specular),1.0);
                gl_Position     = u_matMVP   * vec4(a_Position, 1.0);
            }
            """.trimIndent()

    // フラグメントシェーダ
    private val scf =
            """
            precision mediump   float;

            varying   vec4      v_Color;

            void main() {
                gl_FragColor = v_Color;
            }
            """.trimIndent()

    var programHandle: Int = -1

    fun loadShader(): Int {
        // 頂点シェーダを生成
        val svhandle = MyGLFunc.loadShader(GLES20.GL_VERTEX_SHADER, scv)
        // フラグメントシェーダを生成
        val sfhandle = MyGLFunc.loadShader(GLES20.GL_FRAGMENT_SHADER, scf)

        // プログラムオブジェクトの生成とリンク
        programHandle = MyGLFunc.createProgram(svhandle,sfhandle, arrayOf("a_Position","a_Normal","a_Color") )

        return programHandle
    }


    fun draw(modelAbs: ModelAbs,
               matMVP: FloatArray,
               matINV: FloatArray,
               u_vecLight: FloatArray,
               u_vecEye: FloatArray,
               u_ambientColor: FloatArray) {

        GLES20.glUseProgram(programHandle)
        MyGLFunc.checkGlError("Torus:Draw:UseProgram")

        // attribute(頂点)
        modelAbs.bufPos.position(0)
        GLES20.glGetAttribLocation(programHandle,"a_Position").also {
            GLES20.glVertexAttribPointer(it,3,GLES20.GL_FLOAT,false, 3*4, modelAbs.bufPos)
            GLES20.glEnableVertexAttribArray(it)
        }
        MyGLFunc.checkGlError("Torus:Draw:a_Position")

        // attribute(法線)
        modelAbs.bufNor.position(0)
        GLES20.glGetAttribLocation(programHandle,"a_Normal").also {
            GLES20.glVertexAttribPointer(it,3,GLES20.GL_FLOAT,false, 3*4, modelAbs.bufNor)
            GLES20.glEnableVertexAttribArray(it)
        }
        MyGLFunc.checkGlError("Torus:Draw:a_Normal")

        // attribute(色)
        modelAbs.bufCol.position(0)
        GLES20.glGetAttribLocation(programHandle,"a_Color").also {
            GLES20.glVertexAttribPointer(it,4,GLES20.GL_FLOAT,false, 4*4, modelAbs.bufCol)
            GLES20.glEnableVertexAttribArray(it)
        }
        MyGLFunc.checkGlError("Torus:Draw:a_Color")

        // uniform(モデル×ビュー×プロジェクション)
        GLES20.glGetUniformLocation(programHandle,"u_matMVP").also {
            GLES20.glUniformMatrix4fv(it,1,false,matMVP,0)
        }
        MyGLFunc.checkGlError("Torus:Draw:u_matMVP")

        // uniform(逆行列)
        GLES20.glGetUniformLocation(programHandle,"u_matINV").also {
            GLES20.glUniformMatrix4fv(it,1,false,matINV,0)
        }
        MyGLFunc.checkGlError("Torus:Draw:u_matINV")

        // uniform(ライティング)
        GLES20.glGetUniformLocation(programHandle,"u_vecLight").also {
            GLES20.glUniform3fv(it,1,u_vecLight,0)
        }
        MyGLFunc.checkGlError("Torus:Draw:u_vecLight")

        // uniform(視点座標)
        GLES20.glGetUniformLocation(programHandle,"u_vecEye").also {
            GLES20.glUniform3fv(it,1,u_vecEye,0)
        }
        MyGLFunc.checkGlError("Torus:Draw:u_vecEye")

        // uniform(環境色)
        GLES20.glGetUniformLocation(programHandle, "u_ambientColor").also {
            GLES20.glUniform4fv(it, 1,u_ambientColor,0)
        }
        MyGLFunc.checkGlError("Torus:Draw:u_ambientColor")

        // モデルを描画
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, modelAbs.datIdx.size, GLES20.GL_UNSIGNED_SHORT, modelAbs.bufIdx)
    }

}
