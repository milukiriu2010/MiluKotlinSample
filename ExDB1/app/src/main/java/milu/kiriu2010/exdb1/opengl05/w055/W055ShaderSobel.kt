package milu.kiriu2010.exdb1.opengl05.w055

import android.opengl.GLES20
import milu.kiriu2010.gui.model.MgModelAbs
import milu.kiriu2010.gui.basic.MyGLES20Func
import milu.kiriu2010.gui.shader.es20.ES20MgShader

// -------------------------------------------------------------------------------
// sobelフィルタ用シェーダ:VBOなし
// OpenGL ES 2.0
// -------------------------------------------------------------------------------
// sobelフィルタは、エッジ(色の諧調が極端に変化しているところ)の検出が可能になる。
// 一次微分を計算することで、色の諧調差を計算する
// -------------------------------------------------------------------------------
// https://wgld.org/d/webgl/w055.html
// -------------------------------------------------------------------------------
class W055ShaderSobel: ES20MgShader() {
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
            uniform   float       u_renderWH;
            varying   vec2        v_TexCoord;

            // NTSC系加重平均法
            const float redScale   = 0.298912;
            const float greenScale = 0.586611;
            const float blueScale  = 0.114478;
            const vec3  monochromeScale = vec3(redScale, greenScale, blueScale);

            void main() {
                // 3x3の範囲のテクセルにアクセスするためのオフセット値
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

                // 512x512pixelの画像フォーマットをそのまま使う場合
                // float tFrag = 1.0 / 512.0;
                // vec2  fc = vec2(gl_FragCoord.s, 512.0 - gl_FragCoord.t);

                // ---------------------------------------------------------------
                // gl_FragCoord
                //   テクスチャの各テクセルを参照する
                // gl_FragCoord.s
                //   テクスチャの横幅がピクセル単位で格納されている
                // gl_FragCoord.t
                //   テクスチャの縦幅がピクセル単位で格納されている
                // ---------------------------------------------------------------
                // 画像をレンダリングの幅・高さに合わせている場合に、こちらを使う
                float tFrag    = 1.0/u_renderWH;
                // テクスチャ座標は描画が上下逆なので、
                // 第２引数が"テクスチャの大きさ-テクスチャの縦幅"
                vec2  fc = vec2(gl_FragCoord.s, u_renderWH - gl_FragCoord.t);


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

                // sobelフィルタを適用
                if(bool(u_sobel)){
                    destColor = vec4(vec3(sqrt(horizonColor * horizonColor + verticalColor * verticalColor)), 1.0);
                }else{
                    destColor = texture2D(u_Texture0, v_TexCoord);
                }

                // グレースケールを適用
                if(bool(u_sobelGray)){
                    float grayColor = dot(destColor.rgb, monochromeScale);
                    destColor = vec4(vec3(grayColor), 1.0);
                }
                gl_FragColor = destColor;
            }
            """.trimIndent()

    override fun loadShader(): ES20MgShader {
        // 頂点シェーダを生成
        val svhandle = MyGLES20Func.loadShader(GLES20.GL_VERTEX_SHADER, scv)
        // フラグメントシェーダを生成
        val sfhandle = MyGLES20Func.loadShader(GLES20.GL_FRAGMENT_SHADER, scf)

        // プログラムオブジェクトの生成とリンク
        programHandle = MyGLES20Func.createProgram(svhandle,sfhandle, arrayOf("a_Position","a_TexCoord") )
        return this
    }


    fun draw(model: MgModelAbs,
             matMVP: FloatArray,
             u_Texture0: Int,
             u_sobel: Int,
             u_sobelGray: Int,
             u_hCoef: FloatArray,
             u_vCoef: FloatArray,
             u_renderWH: Float) {

        GLES20.glUseProgram(programHandle)
        MyGLES20Func.checkGlError2("UseProgram",this,model)

        // attribute(頂点)
        model.bufPos.position(0)
        GLES20.glGetAttribLocation(programHandle,"a_Position").also {
            GLES20.glVertexAttribPointer(it,3,GLES20.GL_FLOAT,false, 3*4, model.bufPos)
            GLES20.glEnableVertexAttribArray(it)
        }
        MyGLES20Func.checkGlError2("a_Position",this,model)

        // attribute(テクスチャ座標)
        model.bufTxc.position(0)
        GLES20.glGetAttribLocation(programHandle,"a_TexCoord").also {
            GLES20.glVertexAttribPointer(it,2,GLES20.GL_FLOAT,false, 2*4, model.bufTxc)
            GLES20.glEnableVertexAttribArray(it)
        }
        MyGLES20Func.checkGlError2("a_TexCoord",this,model)

        // uniform(モデル×ビュー×プロジェクション)
        GLES20.glGetUniformLocation(programHandle,"u_matMVP").also {
            GLES20.glUniformMatrix4fv(it,1,false,matMVP,0)
        }
        MyGLES20Func.checkGlError2("u_matMVP",this,model)

        // uniform(テクスチャ0)
        GLES20.glGetUniformLocation(programHandle, "u_Texture0").also {
            GLES20.glUniform1i(it, u_Texture0)
        }
        MyGLES20Func.checkGlError2("u_Texture0",this,model)

        // uniform(sobelフィルタを使うかどうか)
        GLES20.glGetUniformLocation(programHandle, "u_sobel").also {
            GLES20.glUniform1i(it, u_sobel)
        }
        MyGLES20Func.checkGlError2("u_sobel",this,model)

        // uniform(グレースケールを使うかどうか)
        GLES20.glGetUniformLocation(programHandle, "u_sobelGray").also {
            GLES20.glUniform1i(it, u_sobelGray)
        }
        MyGLES20Func.checkGlError2("u_sobelGray",this,model)

        // uniform(sobelフィルタの横方向カーネル)
        GLES20.glGetUniformLocation(programHandle, "u_hCoef").also {
            GLES20.glUniform1fv(it, 9,u_hCoef,0)
        }
        MyGLES20Func.checkGlError2("u_hCoef",this,model)

        // uniform(sobelフィルタの縦方向カーネル)
        GLES20.glGetUniformLocation(programHandle, "u_vCoef").also {
            GLES20.glUniform1fv(it, 9,u_vCoef,0)
        }
        MyGLES20Func.checkGlError2("u_vCoef",this,model)

        // uniform(レンダリング領域の大きさ)
        GLES20.glGetUniformLocation(programHandle, "u_renderWH").also {
            GLES20.glUniform1f(it, u_renderWH)
        }
        MyGLES20Func.checkGlError2("u_renderWH",this,model)

        // モデルを描画
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, model.datIdx.size, GLES20.GL_UNSIGNED_SHORT, model.bufIdx)
    }
}
