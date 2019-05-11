package milu.kiriu2010.exdb1.opengl06.w065

import android.opengl.GLES20
import milu.kiriu2010.gui.model.MgModelAbs
import milu.kiriu2010.gui.basic.MyGLFunc
import milu.kiriu2010.gui.shader.MgShader

// gaussianフィルタ用シェーダ
// -------------------------------------------------------------------------------
// ぼかしフィルタ
// -------------------------------------------------------------------------------
class W065ShaderGaussian: MgShader() {
    // 頂点シェーダ
    private val scv =
            """
            attribute vec3  a_Position;
            attribute vec2  a_TexCoord;
            uniform   mat4  u_matO;
            varying   vec2  v_TexCoord;

            void main() {
                v_TexCoord  = a_TexCoord;
                gl_Position = u_matO   * vec4(a_Position, 1.0);
            }
            """.trimIndent()

    // フラグメントシェーダ
    private val scf =
            """
            precision mediump     float;

            uniform   sampler2D   u_Texture0;
            uniform   float       u_weight[5];
            uniform   int         u_horizontal;
            uniform   float       u_renderWH;
            varying   vec2        v_TexCoord;

            void main() {
                // 512x512pixelの画像フォーマットをそのまま使う場合
                // float tFrag = 1.0 / 512.0;

                // 画像をレンダリングの幅・高さに合わせている場合に、こちらを使う
                float tFrag    = 1.0/u_renderWH;

                vec2  fc = gl_FragCoord.st;
                vec3  destColor = vec3(0.0);

                // 横方向にガウスフィルタをかける
                if(bool(u_horizontal)){
                    destColor += texture2D(u_Texture0, (fc + vec2(-4.0, 0.0)) * tFrag).rgb * u_weight[4];
                    destColor += texture2D(u_Texture0, (fc + vec2(-3.0, 0.0)) * tFrag).rgb * u_weight[3];
                    destColor += texture2D(u_Texture0, (fc + vec2(-2.0, 0.0)) * tFrag).rgb * u_weight[2];
                    destColor += texture2D(u_Texture0, (fc + vec2(-1.0, 0.0)) * tFrag).rgb * u_weight[1];
                    destColor += texture2D(u_Texture0, (fc + vec2( 0.0, 0.0)) * tFrag).rgb * u_weight[0];
                    destColor += texture2D(u_Texture0, (fc + vec2( 1.0, 0.0)) * tFrag).rgb * u_weight[1];
                    destColor += texture2D(u_Texture0, (fc + vec2( 2.0, 0.0)) * tFrag).rgb * u_weight[2];
                    destColor += texture2D(u_Texture0, (fc + vec2( 3.0, 0.0)) * tFrag).rgb * u_weight[3];
                    destColor += texture2D(u_Texture0, (fc + vec2( 4.0, 0.0)) * tFrag).rgb * u_weight[4];
                }
                // 縦方向にガウスフィルタをかける
                else{
                    destColor += texture2D(u_Texture0, (fc + vec2(0.0, -4.0)) * tFrag).rgb * u_weight[4];
                    destColor += texture2D(u_Texture0, (fc + vec2(0.0, -3.0)) * tFrag).rgb * u_weight[3];
                    destColor += texture2D(u_Texture0, (fc + vec2(0.0, -2.0)) * tFrag).rgb * u_weight[2];
                    destColor += texture2D(u_Texture0, (fc + vec2(0.0, -1.0)) * tFrag).rgb * u_weight[1];
                    destColor += texture2D(u_Texture0, (fc + vec2(0.0,  0.0)) * tFrag).rgb * u_weight[0];
                    destColor += texture2D(u_Texture0, (fc + vec2(0.0,  1.0)) * tFrag).rgb * u_weight[1];
                    destColor += texture2D(u_Texture0, (fc + vec2(0.0,  2.0)) * tFrag).rgb * u_weight[2];
                    destColor += texture2D(u_Texture0, (fc + vec2(0.0,  3.0)) * tFrag).rgb * u_weight[3];
                    destColor += texture2D(u_Texture0, (fc + vec2(0.0,  4.0)) * tFrag).rgb * u_weight[4];
                }

                gl_FragColor = vec4(vec3(1.0)-destColor, 1.0);
            }
            """.trimIndent()

    override fun loadShader(): MgShader {
        // 頂点シェーダを生成
        val svhandle = MyGLFunc.loadShader(GLES20.GL_VERTEX_SHADER, scv)
        // フラグメントシェーダを生成
        val sfhandle = MyGLFunc.loadShader(GLES20.GL_FRAGMENT_SHADER, scf)

        // プログラムオブジェクトの生成とリンク
        programHandle = MyGLFunc.createProgram(svhandle,sfhandle, arrayOf("a_Position","a_TexCoord") )
        return this
    }

    fun draw(model: MgModelAbs,
             matO: FloatArray,
             u_Texture0: Int,
             u_weight: FloatArray,
             u_horizontal: Int,
             u_renderWH: Float) {

        GLES20.glUseProgram(programHandle)
        MyGLFunc.checkGlError2("UseProgram", this,model)

        // attribute(頂点)
        model.bufPos.position(0)
        GLES20.glGetAttribLocation(programHandle,"a_Position").also {
            GLES20.glVertexAttribPointer(it,3,GLES20.GL_FLOAT,false, 3*4, model.bufPos)
            GLES20.glEnableVertexAttribArray(it)
        }
        MyGLFunc.checkGlError2("a_Position", this,model)

        // attribute(テクスチャ座標)
        model.bufTxc.position(0)
        GLES20.glGetAttribLocation(programHandle,"a_TexCoord").also {
            GLES20.glVertexAttribPointer(it,2,GLES20.GL_FLOAT,false, 2*4, model.bufTxc)
            GLES20.glEnableVertexAttribArray(it)
        }
        MyGLFunc.checkGlError2("a_TexCoord", this,model)

        // uniform()
        GLES20.glGetUniformLocation(programHandle,"u_matO").also {
            GLES20.glUniformMatrix4fv(it,1,false,matO,0)
        }
        MyGLFunc.checkGlError2("u_matO", this,model)

        // uniform(テクスチャ0)
        GLES20.glGetUniformLocation(programHandle, "u_Texture0").also {
            GLES20.glUniform1i(it, u_Texture0)
        }
        MyGLFunc.checkGlError2("u_Texture0", this,model)

        // uniform(カーネル)
        GLES20.glGetUniformLocation(programHandle, "u_weight").also {
            GLES20.glUniform1fv(it, 5,u_weight,0)
        }
        MyGLFunc.checkGlError2("u_weight", this,model)

        // uniform(水平方向かどうか)
        GLES20.glGetUniformLocation(programHandle, "u_horizontal").also {
            GLES20.glUniform1i(it, u_horizontal)
        }
        MyGLFunc.checkGlError2("u_horizontal", this,model)

        // uniform(画像の大きさ)
        GLES20.glGetUniformLocation(programHandle, "u_renderWH").also {
            GLES20.glUniform1f(it, u_renderWH)
        }
        MyGLFunc.checkGlError2("u_renderWH", this,model)

        // モデルを描画
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, model.datIdx.size, GLES20.GL_UNSIGNED_SHORT, model.bufIdx)
    }
}
