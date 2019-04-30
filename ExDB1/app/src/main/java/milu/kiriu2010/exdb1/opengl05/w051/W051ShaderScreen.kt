package milu.kiriu2010.exdb1.opengl05.w051

import android.opengl.GLES20
import milu.kiriu2010.gui.basic.MyGLFunc
import milu.kiriu2010.gui.model.MgModelAbs
import milu.kiriu2010.gui.shader.MgShader

// スクリーンレンダリング用シェーダ
class W051ShaderScreen: MgShader() {
    // 頂点シェーダ
    private val scv =
            """
            attribute vec3  a_Position;
            attribute vec3  a_Normal;
            attribute vec4  a_Color;
            // カメラ視点のモデル座標変換行列
            uniform   mat4  u_matM;
            // カメラ支店の座標変換行列
            uniform   mat4  u_matMVP;
            // 射影テクスチャマッピング用行列
            uniform   mat4  u_matTex;
            // ライト視点の座標変換行列
            uniform   mat4  u_matLight;
            varying   vec3  v_Position;
            varying   vec3  v_Normal;
            varying   vec4  v_Color;
            varying   vec4  v_TexCoord;
            varying   vec4  v_Depth;

            void main() {
                v_Position  = (u_matM * vec4(a_Position, 1.0)).xyz;
                v_Normal    = a_Normal;
                v_Color     = a_Color;
                v_TexCoord  = u_matTex   * vec4(v_Position, 1.0);
                v_Depth     = u_matLight * vec4(a_Position, 1.0);
                gl_Position = u_matMVP   * vec4(a_Position, 1.0);
            }
            """.trimIndent()

    // フラグメントシェーダ
    private val scf =
            """
            precision mediump   float;

            uniform   mat4        u_matINV;
            uniform   vec3        u_vecEye;
            uniform   sampler2D   u_Texture0;
            uniform   int         u_depthBuffer;
            varying   vec3        v_Position;
            varying   vec3        v_Normal;
            varying   vec4        v_Color;
            varying   vec4        v_TexCoord;
            varying   vec4        v_Depth;

            float restDepth(vec4 RGBA) {
                const float rMask = 1.0;
                const float gMask = 1.0/255.0;
                const float bMask = 1.0/(255.0*255.0);
                const float aMask = 1.0/(255.0*255.0*255.0);
                float depth = dot(RGBA, vec4(rMask, gMask, bMask, aMask));
                return depth;
            }

            void main() {
                vec3  light      = u_vecEye - v_Position;
                vec3  invLight   = normalize(u_matINV * vec4(light, 0.0)).xyz;
                float diffuse    = clamp(dot(v_Normal, invLight), 0.2, 1.0);
                float shadow     = restDepth(texture2DProj(u_Texture0, v_TexCoord));
                vec4  depthColor = vec4(1.0);
                if (v_Depth.w > 0.0) {
                    if (bool(u_depthBuffer)) {
                        vec4 lightCoord = v_Depth/v_Depth.w;
                        if ( (lightCoord.z - 0.0001) > shadow ) {
                            depthColor = vec4(0.5,0.5,0.5,1.0);
                        }
                    }
                    else {
                        float  near = 0.1;
                        float  far  = 150.0;
                        float  linearDepth = 1.0/(far-near);
                        linearDepth *= length(v_Position.xyz - u_vecEye);
                        if ( (linearDepth - 0.0001) > shadow ) {
                            depthColor = vec4(0.5,0.5,0.5,1.0);
                        }
                    }
                }

                gl_FragColor = v_Color * vec4(vec3(diffuse), 1.0) * depthColor;
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
             matM: FloatArray,
             matMVP: FloatArray,
             matINV: FloatArray,
             matTex: FloatArray,
             matLight: FloatArray,
             u_vecLight: FloatArray,
             u_Texture0: Int,
             u_depthBuffer: Int) {
        GLES20.glUseProgram(programHandle)

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

        // uniform(逆行列)
        GLES20.glGetUniformLocation(programHandle,"u_matINV").also {
            GLES20.glUniformMatrix4fv(it,1,false,matINV,0)
        }
        MyGLFunc.checkGlError("u_matINV:${model.javaClass.simpleName}")

        // uniform(テクスチャ射影変換用行列)
        GLES20.glGetUniformLocation(programHandle,"u_matTex").also {
            GLES20.glUniformMatrix4fv(it,1,false,matTex,0)
        }
        MyGLFunc.checkGlError("u_matTex:${model.javaClass.simpleName}")

        // uniform(ライト視点の座標変換行列)
        GLES20.glGetUniformLocation(programHandle,"u_matLight").also {
            GLES20.glUniformMatrix4fv(it,1,false,matLight,0)
        }
        MyGLFunc.checkGlError("u_matLight:${model.javaClass.simpleName}")

        // uniform(ライティング)
        GLES20.glGetUniformLocation(programHandle,"u_vecLight").also {
            GLES20.glUniform3fv(it,1,u_vecLight,0)
        }
        MyGLFunc.checkGlError("u_vecLight:${model.javaClass.simpleName}")

        // uniform(テクスチャ0)
        GLES20.glGetUniformLocation(programHandle, "u_Texture0").also {
            GLES20.glUniform1i(it, u_Texture0)
        }
        MyGLFunc.checkGlError("u_Texture0:${model.javaClass.simpleName}")

        // uniform(深度値を使うかどうか)
        GLES20.glGetUniformLocation(programHandle, "u_depthBuffer").also {
            GLES20.glUniform1i(it, u_depthBuffer)
        }
        MyGLFunc.checkGlError("u_depthBuffer:${model.javaClass.simpleName}")

        // モデルを描画
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, model.datIdx.size, GLES20.GL_UNSIGNED_SHORT, model.bufIdx)
    }
}
