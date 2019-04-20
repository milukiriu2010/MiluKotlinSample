package milu.kiriu2010.exdb1.opengl05.w056

import android.opengl.GLES20
import milu.kiriu2010.gui.model.MgModelAbs
import milu.kiriu2010.gui.basic.MyGLFunc

// laplacianフィルタ用シェーダ
class W056ShaderLaplacian {
    // 頂点シェーダ
    private val scv =
            """
            attribute vec3  a_Position;
            attribute vec2  a_TexCoord;
            uniform   mat4  u_matMVP;
            varying   vec2  v_TexCoord;

            void main() {
                v_TexCoord      = a_TexCoord;
                gl_Position     = u_matMVP   * vec4(a_Position, 1.0);
            }
            """.trimIndent()

    // フラグメントシェーダ
    private val scf =
            """
            precision mediump     float;

            uniform   sampler2D   u_Texture0;
            uniform   int         u_laplacian;
            uniform   int         u_laplacianGray;
            uniform   float       u_Coef[9];
            varying   vec2        v_TexCoord;

            const float redScale   = 0.298912;
            const float greenScale = 0.586611;
            const float blueScale  = 0.114478;
            const vec3  monochromeScale = vec3(redScale, greenScale, blueScale);

            void main() {
                vec2 offset[9];
                offset[0] = vec2(-1.0, -1.0);
                offset[1] = vec2( 0.0, -1.0);
                offset[2] = vec2( 1.0, -1.0);
                offset[3] = vec2(-1.0,  0.0);
                offset[4] = vec2( 0.0,  0.0);
                offset[5] = vec2( 1.0,  0.0);
                offset[6] = vec2(-1.0,  1.0);
                offset[7] = vec2( 0.0,  1.0);
                offset[8] = vec2( 1.0,  1.0);
                float tFrag = 1.0 / 512.0;
                vec2  fc = vec2(gl_FragCoord.s, 512.0 - gl_FragCoord.t);
                vec3  destColor     = vec3(0.0);

                destColor  += texture2D(u_Texture0, (fc + offset[0]) * tFrag).rgb * u_Coef[0];
                destColor  += texture2D(u_Texture0, (fc + offset[1]) * tFrag).rgb * u_Coef[1];
                destColor  += texture2D(u_Texture0, (fc + offset[2]) * tFrag).rgb * u_Coef[2];
                destColor  += texture2D(u_Texture0, (fc + offset[3]) * tFrag).rgb * u_Coef[3];
                destColor  += texture2D(u_Texture0, (fc + offset[4]) * tFrag).rgb * u_Coef[4];
                destColor  += texture2D(u_Texture0, (fc + offset[5]) * tFrag).rgb * u_Coef[5];
                destColor  += texture2D(u_Texture0, (fc + offset[6]) * tFrag).rgb * u_Coef[6];
                destColor  += texture2D(u_Texture0, (fc + offset[7]) * tFrag).rgb * u_Coef[7];
                destColor  += texture2D(u_Texture0, (fc + offset[8]) * tFrag).rgb * u_Coef[8];

                if(bool(u_laplacian)){
                    destColor = max(destColor, 0.0);
                }else{
                    destColor = texture2D(u_Texture0, v_TexCoord).rgb;
                }
                if(bool(u_laplacianGray)){
                    float grayColor = dot(destColor.rgb, monochromeScale);
                    destColor = vec3(grayColor);
                }
                gl_FragColor = vec4(destColor, 1.0);
            }
            """.trimIndent()

    var programHandle: Int = -1

    fun loadShader(): Int {
        // 頂点シェーダを生成
        val svhandle = MyGLFunc.loadShader(GLES20.GL_VERTEX_SHADER, scv)
        // フラグメントシェーダを生成
        val sfhandle = MyGLFunc.loadShader(GLES20.GL_FRAGMENT_SHADER, scf)

        // プログラムオブジェクトの生成とリンク
        programHandle = MyGLFunc.createProgram(svhandle,sfhandle, arrayOf("a_Position","a_TexCoord") )

        return programHandle
    }


    fun draw(modelAbs: MgModelAbs,
             matMVP: FloatArray,
             u_Texture0: Int,
             u_sobel: Int,
             u_sobelGray: Int,
             u_Coef: FloatArray) {

        GLES20.glUseProgram(programHandle)
        MyGLFunc.checkGlError("Board:Draw:UseProgram")

        // attribute(頂点)
        modelAbs.bufPos.position(0)
        GLES20.glGetAttribLocation(programHandle,"a_Position").also {
            GLES20.glVertexAttribPointer(it,3,GLES20.GL_FLOAT,false, 3*4, modelAbs.bufPos)
            GLES20.glEnableVertexAttribArray(it)
        }
        MyGLFunc.checkGlError("Board:Draw:a_Position")

        // attribute(テクスチャ座標)
        modelAbs.bufTxc.position(0)
        GLES20.glGetAttribLocation(programHandle,"a_TexCoord").also {
            GLES20.glVertexAttribPointer(it,2,GLES20.GL_FLOAT,false, 2*4, modelAbs.bufTxc)
            GLES20.glEnableVertexAttribArray(it)
        }
        MyGLFunc.checkGlError("Board:Draw:a_TexCoord")

        // uniform(モデル×ビュー×プロジェクション)
        GLES20.glGetUniformLocation(programHandle,"u_matMVP").also {
            GLES20.glUniformMatrix4fv(it,1,false,matMVP,0)
        }
        MyGLFunc.checkGlError("Board:Draw:u_matMVP")

        // uniform(テクスチャ0)
        GLES20.glGetUniformLocation(programHandle, "u_Texture0").also {
            GLES20.glUniform1i(it, u_Texture0)
        }
        MyGLFunc.checkGlError("u_Texture0")

        // uniform(sobelフィルタを使うかどうか)
        GLES20.glGetUniformLocation(programHandle, "u_laplacian").also {
            GLES20.glUniform1i(it, u_sobel)
        }
        MyGLFunc.checkGlError("Board:Draw:u_laplacian")

        // uniform(グレースケールを使うかどうか)
        GLES20.glGetUniformLocation(programHandle, "u_laplacianGray").also {
            GLES20.glUniform1i(it, u_sobelGray)
        }
        MyGLFunc.checkGlError("Board:Draw:u_laplacianGray")

        // カーネル
        GLES20.glGetUniformLocation(programHandle, "u_Coef").also {
            GLES20.glUniform1fv(it, 9,u_Coef,0)
        }
        MyGLFunc.checkGlError("Board:Draw:u_Coef")

        // モデルを描画
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, modelAbs.datIdx.size, GLES20.GL_UNSIGNED_SHORT, modelAbs.bufIdx)
    }
}
