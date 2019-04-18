package milu.kiriu2010.exdb1.opengl05.w055

import android.opengl.GLES20
import milu.kiriu2010.gui.model.ModelAbs
import milu.kiriu2010.gui.basic.MyGLFunc

// sobelフィルタ用シェーダ
class W055ShaderSobel {
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
            uniform   int         u_sobel;
            uniform   int         u_sobelGray;
            uniform   float       u_hCoef[9];
            uniform   float       u_vCoef[9];
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
                vec3  horizonColor  = vec3(0.0);
                vec3  verticalColor = vec3(0.0);
                vec4  destColor     = vec4(0.0);

                horizonColor  += texture2D(u_Texture0, (fc + offset[0]) * tFrag).rgb * u_hCoef[0];
                horizonColor  += texture2D(u_Texture0, (fc + offset[1]) * tFrag).rgb * u_hCoef[1];
                horizonColor  += texture2D(u_Texture0, (fc + offset[2]) * tFrag).rgb * u_hCoef[2];
                horizonColor  += texture2D(u_Texture0, (fc + offset[3]) * tFrag).rgb * u_hCoef[3];
                horizonColor  += texture2D(u_Texture0, (fc + offset[4]) * tFrag).rgb * u_hCoef[4];
                horizonColor  += texture2D(u_Texture0, (fc + offset[5]) * tFrag).rgb * u_hCoef[5];
                horizonColor  += texture2D(u_Texture0, (fc + offset[6]) * tFrag).rgb * u_hCoef[6];
                horizonColor  += texture2D(u_Texture0, (fc + offset[7]) * tFrag).rgb * u_hCoef[7];
                horizonColor  += texture2D(u_Texture0, (fc + offset[8]) * tFrag).rgb * u_hCoef[8];

                verticalColor += texture2D(u_Texture0, (fc + offset[0]) * tFrag).rgb * u_vCoef[0];
                verticalColor += texture2D(u_Texture0, (fc + offset[1]) * tFrag).rgb * u_vCoef[1];
                verticalColor += texture2D(u_Texture0, (fc + offset[2]) * tFrag).rgb * u_vCoef[2];
                verticalColor += texture2D(u_Texture0, (fc + offset[3]) * tFrag).rgb * u_vCoef[3];
                verticalColor += texture2D(u_Texture0, (fc + offset[4]) * tFrag).rgb * u_vCoef[4];
                verticalColor += texture2D(u_Texture0, (fc + offset[5]) * tFrag).rgb * u_vCoef[5];
                verticalColor += texture2D(u_Texture0, (fc + offset[6]) * tFrag).rgb * u_vCoef[6];
                verticalColor += texture2D(u_Texture0, (fc + offset[7]) * tFrag).rgb * u_vCoef[7];
                verticalColor += texture2D(u_Texture0, (fc + offset[8]) * tFrag).rgb * u_vCoef[8];

                if(bool(u_sobel)){
                    destColor = vec4(vec3(sqrt(horizonColor * horizonColor + verticalColor * verticalColor)), 1.0);
                }else{
                    destColor = texture2D(u_Texture0, v_TexCoord);
                }
                if(bool(u_sobelGray)){
                    float grayColor = dot(destColor.rgb, monochromeScale);
                    destColor = vec4(vec3(grayColor), 1.0);
                }
                gl_FragColor = destColor;
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


    fun draw(modelAbs: ModelAbs,
             matMVP: FloatArray,
             u_Texture0: Int,
             u_sobel: Int,
             u_sobelGray: Int,
             u_hCoef: FloatArray,
             u_vCoef: FloatArray) {

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
        GLES20.glGetUniformLocation(programHandle, "u_sobel").also {
            GLES20.glUniform1i(it, u_sobel)
        }
        MyGLFunc.checkGlError("Board:Draw:u_sobel")

        // uniform(グレースケールを使うかどうか)
        GLES20.glGetUniformLocation(programHandle, "u_sobelGray").also {
            GLES20.glUniform1i(it, u_sobelGray)
        }
        MyGLFunc.checkGlError("Board:Draw:u_sobelGray")

        // カーネル
        GLES20.glGetUniformLocation(programHandle, "u_hCoef").also {
            GLES20.glUniform1fv(it, 9,u_hCoef,0)
        }
        MyGLFunc.checkGlError("Board:Draw:u_hCoef")

        // カーネル
        GLES20.glGetUniformLocation(programHandle, "u_vCoef").also {
            GLES20.glUniform1fv(it, 9,u_vCoef,0)
        }
        MyGLFunc.checkGlError("Board:Draw:u_vCoef")


        // モデルを描画
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, modelAbs.datIdx.size, GLES20.GL_UNSIGNED_SHORT, modelAbs.bufIdx)
    }
}
