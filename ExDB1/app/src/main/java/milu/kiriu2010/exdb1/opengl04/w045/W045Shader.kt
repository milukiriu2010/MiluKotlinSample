package milu.kiriu2010.exdb1.opengl04.w045

import android.opengl.GLES20
import milu.kiriu2010.gui.basic.MyGLFunc
import milu.kiriu2010.gui.model.MgModelAbs
import milu.kiriu2010.gui.shader.MgShader

class W045Shader: MgShader() {
    // 頂点シェーダ
    private val scv =
            """
            attribute vec3  a_Position;
            attribute vec3  a_Normal;
            attribute vec4  a_Color;
            attribute vec2  a_TextureCoord;
            uniform   mat4  u_matM;
            uniform   mat4  u_matMVP;
            varying   vec3  v_Position;
            varying   vec4  v_Color;
            varying   vec2  v_TextureCoord;
            varying   vec3  v_tNormal;
            varying   vec3  v_tTangent;

            void main() {
                v_Position     = (u_matM * vec4(a_Position,1.0)).xyz;
                v_Color        = a_Color;
                v_TextureCoord = a_TextureCoord;
                v_tNormal      = (u_matM * vec4(a_Normal  ,0.0)).xyz;
                // 接線ベクトル
                // = 法線ベクトルとY軸の外積
                v_tTangent     = cross(v_tNormal,vec3(0.0,1.0,0.0));
                gl_Position    = u_matMVP * vec4(a_Position,1.0);
            }
            """.trimIndent()

    // フラグメントシェーダ
    private val scf =
            """
            precision mediump     float;

            uniform   vec3        u_vecEye;
            uniform   sampler2D   u_normalMap;
            uniform   samplerCube u_CubeTexture;
            uniform   int         u_Reflection;
            varying   vec3        v_Position;
            varying   vec4        v_Color;
            varying   vec2        v_TextureCoord;
            varying   vec3        v_tNormal;
            varying   vec3        v_tTangent;

            void main() {
                // 法線と接線ベクトルを使って従法線ベクトルを算出
                vec3  tBinormal = cross(v_tNormal, v_tTangent);
                // 法線マップから抜き出したバンプマッピング用の法線情報を
                // 視線空間上へと変換するために3x3の行列を生成
                mat3  mView     = mat3(v_tTangent, tBinormal, v_tNormal);
                vec3  mNormal   = mView * (texture2D(u_normalMap,v_TextureCoord)*2.0-1.0).rgb;
                vec3  ref;
                if (bool(u_Reflection)) {
                    ref = reflect(v_Position - u_vecEye, mNormal);
                }
                else {
                    ref = v_tNormal;
                }
                vec4  envColor  = textureCube(u_CubeTexture, ref);
                vec4  destColor = v_Color * envColor;
                gl_FragColor    = destColor;
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
             u_vecEye: FloatArray,
             u_normalMap: Int,
             u_CubeTexture: Int,
             u_Reflection: Int) {

        // attribute(頂点)
        model.bufPos.position(0)
        GLES20.glGetAttribLocation(programHandle,"a_Position").also {
            GLES20.glVertexAttribPointer(it,3,GLES20.GL_FLOAT,false, 3*4, model.bufPos)
            GLES20.glEnableVertexAttribArray(it)
        }
        MyGLFunc.checkGlError("a_Position:${model.javaClass.simpleName}")

        // attribute(法線)
        model.bufNor.position(0)
        GLES20.glGetAttribLocation(programHandle,"a_Normal").also {
            GLES20.glVertexAttribPointer(it,3,GLES20.GL_FLOAT,false, 3*4, model.bufNor)
            GLES20.glEnableVertexAttribArray(it)
        }
        MyGLFunc.checkGlError("a_Normal:${model.javaClass.simpleName}")

        // attribute(色)
        model.bufCol.position(0)
        GLES20.glGetAttribLocation(programHandle,"a_Color").also {
            GLES20.glVertexAttribPointer(it,4,GLES20.GL_FLOAT,false, 4*4, model.bufCol)
            GLES20.glEnableVertexAttribArray(it)
        }
        MyGLFunc.checkGlError("a_Color:${model.javaClass.simpleName}")

        // uniform(モデル)
        GLES20.glGetUniformLocation(programHandle,"u_matM").also {
            GLES20.glUniformMatrix4fv(it,1,false,matM,0)
        }
        MyGLFunc.checkGlError("u_matM:${model.javaClass.simpleName}")

        // uniform(モデル×ビュー×プロジェクション)
        GLES20.glGetUniformLocation(programHandle,"u_matMVP").also {
            GLES20.glUniformMatrix4fv(it,1,false,matMVP,0)
        }
        MyGLFunc.checkGlError("u_matMVP:${model.javaClass.simpleName}")

        // uniform(視点座標)
        GLES20.glGetUniformLocation(programHandle,"u_vecEye").also {
            GLES20.glUniform3fv(it,1,u_vecEye,0)
        }
        MyGLFunc.checkGlError("u_vecEye:${model.javaClass.simpleName}")

        if ( u_normalMap != -1 ) {
            // uniform(法線マップテクスチャ)
            GLES20.glGetUniformLocation(programHandle, "u_normalMap").also {
                GLES20.glUniform1i(it, u_normalMap)
            }
            MyGLFunc.checkGlError("u_normalMap:${model.javaClass.simpleName}")
        }

        if ( u_CubeTexture != -1 ) {
            // uniform(キューブテクスチャ)
            GLES20.glGetUniformLocation(programHandle, "u_CubeTexture").also {
                GLES20.glUniform1i(it, u_CubeTexture)
            }
            MyGLFunc.checkGlError("u_CubeTexture:${model.javaClass.simpleName}")
        }

        // uniform(反射)
        GLES20.glGetUniformLocation(programHandle,"u_Reflection").also {
            GLES20.glUniform1i(it,u_Reflection)
        }
        MyGLFunc.checkGlError("u_Reflection:${model.javaClass.simpleName}")

        // モデルを描画
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, model.datIdx.size, GLES20.GL_UNSIGNED_SHORT, model.bufIdx)
    }
}
