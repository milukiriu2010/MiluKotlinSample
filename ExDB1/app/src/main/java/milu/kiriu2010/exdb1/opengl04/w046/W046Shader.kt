package milu.kiriu2010.exdb1.opengl04.w046

import android.opengl.GLES20
import milu.kiriu2010.gui.basic.MyGLES20Func
import milu.kiriu2010.gui.model.MgModelAbs
import milu.kiriu2010.gui.shader.es20.ES20MgShader

// -------------------------------------------------------
// 屈折マッピング:VBOなし
// OpenGL ES 2.0
// -------------------------------------------------------
// ガラスのように少しゆがんだ風景の映りこみを表現できる
// ビー玉のような質感を演出できる
// -------------------------------------------------------
// https://wgld.org/d/webgl/w046.html
// -------------------------------------------------------
class W046Shader: ES20MgShader() {
    // 頂点シェーダ
    private val scv =
            """
            attribute vec3  a_Position;
            attribute vec3  a_Normal;
            attribute vec4  a_Color;
            uniform   mat4  u_matM;
            uniform   mat4  u_matMVP;
            varying   vec3  v_Position;
            varying   vec3  v_Normal;
            varying   vec4  v_Color;

            void main() {
                v_Position    = (u_matM * vec4(a_Position,1.0)).xyz;
                v_Normal      = normalize((u_matM * vec4(a_Normal  ,0.0)).xyz);
                v_Color       = a_Color;
                gl_Position   = u_matMVP * vec4(a_Position,1.0);
            }
            """.trimIndent()

    // フラグメントシェーダ
    private val scf =
            """
            precision mediump     float;

            uniform   vec3        u_vecEye;
            uniform   samplerCube u_CubeTexture;
            uniform   int         u_Refraction;
            // 屈折率の比(0.0-1.0)
            uniform   float       u_eta;
            varying   vec3        v_Position;
            varying   vec3        v_Normal;
            varying   vec4        v_Color;

            void main() {
                vec3  ref;
                if (bool(u_Refraction)) {
                    // 屈折ベクトルを算出
                    ref = refract(normalize(v_Position - u_vecEye), v_Normal, u_eta);
                }
                else {
                    ref = v_Normal;
                }
                vec4  envColor  = textureCube(u_CubeTexture, ref);
                vec4  destColor = v_Color * envColor;
                gl_FragColor    = destColor;
            }
            """.trimIndent()

    override fun loadShader(): ES20MgShader {
        // 頂点シェーダを生成
        val svhandle = MyGLES20Func.loadShader(GLES20.GL_VERTEX_SHADER, scv)
        // フラグメントシェーダを生成
        val sfhandle = MyGLES20Func.loadShader(GLES20.GL_FRAGMENT_SHADER, scf)

        // プログラムオブジェクトの生成とリンク
        programHandle = MyGLES20Func.createProgram(svhandle,sfhandle, arrayOf("a_Position","a_Normal","a_Color") )
        return this
    }


    fun draw(model: MgModelAbs,
             matM: FloatArray,
             matMVP: FloatArray,
             u_vecEye: FloatArray,
             u_CubeTexture: Int,
             u_Refraction: Int,
             u_eta: Float) {

        GLES20.glUseProgram(programHandle)
        MyGLES20Func.checkGlError2("UseProgram",this,model)

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

        // uniform(視点座標)
        GLES20.glGetUniformLocation(programHandle,"u_vecEye").also {
            GLES20.glUniform3fv(it,1,u_vecEye,0)
        }
        MyGLES20Func.checkGlError2("u_vecEye",this,model)

        if ( u_CubeTexture != -1 ) {
            // uniform(キューブテクスチャ)
            GLES20.glGetUniformLocation(programHandle, "u_CubeTexture").also {
                GLES20.glUniform1i(it, u_CubeTexture)
            }
            MyGLES20Func.checkGlError2("u_CubeTexture",this,model)
        }

        // uniform(屈折させるかどうか)
        GLES20.glGetUniformLocation(programHandle,"u_Refraction").also {
            GLES20.glUniform1i(it,u_Refraction)
        }
        MyGLES20Func.checkGlError2("u_Refraction",this,model)

        // uniform(屈折率の比)
        GLES20.glGetUniformLocation(programHandle,"u_eta").also {
            GLES20.glUniform1f(it,u_eta)
        }
        MyGLES20Func.checkGlError2("u_eta",this,model)

        // モデルを描画
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, model.datIdx.size, GLES20.GL_UNSIGNED_SHORT, model.bufIdx)
    }
}
