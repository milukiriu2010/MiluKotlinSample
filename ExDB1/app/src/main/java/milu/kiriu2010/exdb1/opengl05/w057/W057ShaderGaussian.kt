package milu.kiriu2010.exdb1.opengl05.w057

import android.opengl.GLES20
import milu.kiriu2010.gui.model.MgModelAbs
import milu.kiriu2010.gui.basic.MyGLFunc

// gaussianフィルタ用シェーダ
class W057ShaderGaussian {
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
            uniform   int         u_gaussian;
            uniform   float       u_weight[10];
            uniform   int         u_horizontal;
            varying   vec2        v_TexCoord;

            void main() {
                float tFrag = 1.0 / 512.0;
                vec2  fc;
                vec3  destColor = vec3(0.0);

                if(bool(u_gaussian)){
                    if(bool(u_horizontal)){
                        fc = vec2(gl_FragCoord.s, 512.0 - gl_FragCoord.t);
                        destColor += texture2D(u_Texture0, (fc + vec2(-9.0, 0.0)) * tFrag).rgb * u_weight[9];
                        destColor += texture2D(u_Texture0, (fc + vec2(-8.0, 0.0)) * tFrag).rgb * u_weight[8];
                        destColor += texture2D(u_Texture0, (fc + vec2(-7.0, 0.0)) * tFrag).rgb * u_weight[7];
                        destColor += texture2D(u_Texture0, (fc + vec2(-6.0, 0.0)) * tFrag).rgb * u_weight[6];
                        destColor += texture2D(u_Texture0, (fc + vec2(-5.0, 0.0)) * tFrag).rgb * u_weight[5];
                        destColor += texture2D(u_Texture0, (fc + vec2(-4.0, 0.0)) * tFrag).rgb * u_weight[4];
                        destColor += texture2D(u_Texture0, (fc + vec2(-3.0, 0.0)) * tFrag).rgb * u_weight[3];
                        destColor += texture2D(u_Texture0, (fc + vec2(-2.0, 0.0)) * tFrag).rgb * u_weight[2];
                        destColor += texture2D(u_Texture0, (fc + vec2(-1.0, 0.0)) * tFrag).rgb * u_weight[1];
                        destColor += texture2D(u_Texture0, (fc + vec2( 0.0, 0.0)) * tFrag).rgb * u_weight[0];
                        destColor += texture2D(u_Texture0, (fc + vec2( 1.0, 0.0)) * tFrag).rgb * u_weight[1];
                        destColor += texture2D(u_Texture0, (fc + vec2( 2.0, 0.0)) * tFrag).rgb * u_weight[2];
                        destColor += texture2D(u_Texture0, (fc + vec2( 3.0, 0.0)) * tFrag).rgb * u_weight[3];
                        destColor += texture2D(u_Texture0, (fc + vec2( 4.0, 0.0)) * tFrag).rgb * u_weight[4];
                        destColor += texture2D(u_Texture0, (fc + vec2( 5.0, 0.0)) * tFrag).rgb * u_weight[5];
                        destColor += texture2D(u_Texture0, (fc + vec2( 6.0, 0.0)) * tFrag).rgb * u_weight[6];
                        destColor += texture2D(u_Texture0, (fc + vec2( 7.0, 0.0)) * tFrag).rgb * u_weight[7];
                        destColor += texture2D(u_Texture0, (fc + vec2( 8.0, 0.0)) * tFrag).rgb * u_weight[8];
                        destColor += texture2D(u_Texture0, (fc + vec2( 9.0, 0.0)) * tFrag).rgb * u_weight[9];
                    }else{
                        fc = gl_FragCoord.st;
                        destColor += texture2D(u_Texture0, (fc + vec2(0.0, -9.0)) * tFrag).rgb * u_weight[9];
                        destColor += texture2D(u_Texture0, (fc + vec2(0.0, -8.0)) * tFrag).rgb * u_weight[8];
                        destColor += texture2D(u_Texture0, (fc + vec2(0.0, -7.0)) * tFrag).rgb * u_weight[7];
                        destColor += texture2D(u_Texture0, (fc + vec2(0.0, -6.0)) * tFrag).rgb * u_weight[6];
                        destColor += texture2D(u_Texture0, (fc + vec2(0.0, -5.0)) * tFrag).rgb * u_weight[5];
                        destColor += texture2D(u_Texture0, (fc + vec2(0.0, -4.0)) * tFrag).rgb * u_weight[4];
                        destColor += texture2D(u_Texture0, (fc + vec2(0.0, -3.0)) * tFrag).rgb * u_weight[3];
                        destColor += texture2D(u_Texture0, (fc + vec2(0.0, -2.0)) * tFrag).rgb * u_weight[2];
                        destColor += texture2D(u_Texture0, (fc + vec2(0.0, -1.0)) * tFrag).rgb * u_weight[1];
                        destColor += texture2D(u_Texture0, (fc + vec2(0.0,  0.0)) * tFrag).rgb * u_weight[0];
                        destColor += texture2D(u_Texture0, (fc + vec2(0.0,  1.0)) * tFrag).rgb * u_weight[1];
                        destColor += texture2D(u_Texture0, (fc + vec2(0.0,  2.0)) * tFrag).rgb * u_weight[2];
                        destColor += texture2D(u_Texture0, (fc + vec2(0.0,  3.0)) * tFrag).rgb * u_weight[3];
                        destColor += texture2D(u_Texture0, (fc + vec2(0.0,  4.0)) * tFrag).rgb * u_weight[4];
                        destColor += texture2D(u_Texture0, (fc + vec2(0.0,  5.0)) * tFrag).rgb * u_weight[5];
                        destColor += texture2D(u_Texture0, (fc + vec2(0.0,  6.0)) * tFrag).rgb * u_weight[6];
                        destColor += texture2D(u_Texture0, (fc + vec2(0.0,  7.0)) * tFrag).rgb * u_weight[7];
                        destColor += texture2D(u_Texture0, (fc + vec2(0.0,  8.0)) * tFrag).rgb * u_weight[8];
                        destColor += texture2D(u_Texture0, (fc + vec2(0.0,  9.0)) * tFrag).rgb * u_weight[9];
                    }
                }else{
                    destColor = texture2D(u_Texture0, v_TexCoord).rgb;
                }

                gl_FragColor = vec4(destColor, 1.0);            }
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
             u_gaussian: Int,
             u_weight: FloatArray,
             u_horizontal: Int) {

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

        // uniform(gaussianフィルタを使うかどうか)
        GLES20.glGetUniformLocation(programHandle, "u_gaussian").also {
            GLES20.glUniform1i(it, u_gaussian)
        }
        MyGLFunc.checkGlError("Board:Draw:u_gaussian")

        // カーネル
        GLES20.glGetUniformLocation(programHandle, "u_weight").also {
            GLES20.glUniform1fv(it, 10,u_weight,0)
        }
        MyGLFunc.checkGlError("Board:Draw:u_weight")

        // uniform(水平方向かどうか)
        GLES20.glGetUniformLocation(programHandle, "u_horizontal").also {
            GLES20.glUniform1i(it, u_horizontal)
        }
        MyGLFunc.checkGlError("Board:Draw:u_horizontal")

        // モデルを描画
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, modelAbs.datIdx.size, GLES20.GL_UNSIGNED_SHORT, modelAbs.bufIdx)
    }
}
