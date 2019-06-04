package milu.kiriu2010.exdb1.opengl05.w056v

import android.opengl.GLES20
import milu.kiriu2010.gui.model.MgModelAbs
import milu.kiriu2010.gui.basic.MyGLES20Func
import milu.kiriu2010.gui.shader.es20.ES20MgShader
import milu.kiriu2010.gui.vbo.es20.ES20VBOAbs

// -------------------------------------------------------------------------------
// シェーダ(laplacianフィルタ)
// -------------------------------------------------------------------------------
// laplacianフィルタは、エッジ(色の諧調が極端に変化しているところ)の検出が可能になる。
// 二次微分を計算することで、色の諧調差を計算する
// sobelフィルタに比べ繊細で細い線によるエッジの検出ができる
// -------------------------------------------------------------------------------
// // https://wgld.org/d/webgl/w056.html
// -------------------------------------------------------------------------------
class WV056ShaderLaplacian: ES20MgShader() {
    // 頂点シェーダ
    private val scv =
            """
            attribute vec3  a_Position;
            attribute vec2  a_TextureCoord;
            uniform   mat4  u_matMVP;
            varying   vec2  v_TexCoord;

            void main() {
                v_TexCoord      = a_TextureCoord;
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

                // laplacianフィルタを適用
                if(bool(u_laplacian)){
                    destColor = max(destColor, 0.0);
                }else{
                    destColor = texture2D(u_Texture0, v_TexCoord).rgb;
                }

                // グレースケールを適用
                if(bool(u_laplacianGray)){
                    float grayColor = dot(destColor.rgb, monochromeScale);
                    destColor = vec3(grayColor);
                }
                gl_FragColor = vec4(destColor, 1.0);
            }
            """.trimIndent()

    override fun loadShader(): ES20MgShader {
        // 頂点シェーダを生成
        val svhandle = MyGLES20Func.loadShader(GLES20.GL_VERTEX_SHADER, scv)
        // フラグメントシェーダを生成
        val sfhandle = MyGLES20Func.loadShader(GLES20.GL_FRAGMENT_SHADER, scf)

        // プログラムオブジェクトの生成とリンク
        programHandle = MyGLES20Func.createProgram(svhandle,sfhandle)

        // ----------------------------------------------
        // attributeハンドルに値をセット
        // ----------------------------------------------
        hATTR = IntArray(2)
        // 属性(頂点)
        hATTR[0] = GLES20.glGetAttribLocation(programHandle, "a_Position").also {
            // attribute属性を有効にする
            // ここで呼ばないと描画されない
            GLES20.glEnableVertexAttribArray(it)
            MyGLES20Func.checkGlError("a_Position:glEnableVertexAttribArray")
            // attribute属性を登録
            GLES20.glVertexAttribPointer(it,3,GLES20.GL_FLOAT,false,0,0)
            MyGLES20Func.checkGlError("a_Position:glVertexAttribPointer")
        }
        MyGLES20Func.checkGlError("a_Position:glGetAttribLocation")

        // 属性(テクスチャ座標)
        hATTR[1] = GLES20.glGetAttribLocation(programHandle, "a_TextureCoord").also {
            // attribute属性を有効にする
            // ここで呼ばないと描画されない
            GLES20.glEnableVertexAttribArray(it)
            MyGLES20Func.checkGlError("a_TextureCoord:glEnableVertexAttribArray")
            // attribute属性を登録
            GLES20.glVertexAttribPointer(it,2,GLES20.GL_FLOAT,false,0,0)
            MyGLES20Func.checkGlError("a_TextureCoord:glVertexAttribPointer")
        }
        MyGLES20Func.checkGlError("a_TextureCoord:glGetAttribLocation")

        // ----------------------------------------------
        // uniformハンドルに値をセット
        // ----------------------------------------------
        hUNI = IntArray(6)

        // uniform(モデル×ビュー×プロジェクション)
        hUNI[0] = GLES20.glGetUniformLocation(programHandle,"u_matMVP")
        MyGLES20Func.checkGlError("u_matMVP:glGetUniformLocation")

        // uniform(テクスチャユニット)
        hUNI[1] = GLES20.glGetUniformLocation(programHandle,"u_Texture0")
        MyGLES20Func.checkGlError("u_Texture:glGetUniformLocation")

        // uniform(laplacianフィルタを使うかどうか)
        hUNI[2] = GLES20.glGetUniformLocation(programHandle, "u_laplacian")
        MyGLES20Func.checkGlError("u_laplacian:glGetUniformLocation")

        // uniform(グレースケールを使うかどうか)
        hUNI[3] = GLES20.glGetUniformLocation(programHandle, "u_laplacianGray")
        MyGLES20Func.checkGlError("u_laplacianGray:glGetUniformLocation")

        // uniform(カーネル)
        hUNI[4] = GLES20.glGetUniformLocation(programHandle, "u_Coef")
        MyGLES20Func.checkGlError("u_Coef:glGetUniformLocation")

        // uniform(レンダリング領域の大きさ)
        hUNI[5] = GLES20.glGetUniformLocation(programHandle, "u_renderWH")
        MyGLES20Func.checkGlError("u_renderWH:glGetUniformLocation")

        return this
    }


    fun draw(model: MgModelAbs,
             bo: ES20VBOAbs,
             u_matMVP: FloatArray,
             u_Texture0: Int,
             u_laplacian: Int,
             u_laplacianGray: Int,
             u_Coef: FloatArray,
             u_renderWH: Float) {

        GLES20.glUseProgram(programHandle)
        MyGLES20Func.checkGlError2("UseProgram",this,model)

        // attribute(頂点)
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,bo.hVBO[0])
        GLES20.glVertexAttribPointer(hATTR[0],3,GLES20.GL_FLOAT,false,0,0)
        MyGLES20Func.checkGlError2("a_Position",this,model)

        // attribute(テクスチャ座標)
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,bo.hVBO[1])
        GLES20.glVertexAttribPointer(hATTR[1],2,GLES20.GL_FLOAT,false,0,0)
        MyGLES20Func.checkGlError2("a_TexCoord",this,model)

        // uniform(モデル×ビュー×プロジェクション)
        GLES20.glUniformMatrix4fv(hUNI[0],1,false,u_matMVP,0)
        MyGLES20Func.checkGlError2("u_matMVP",this,model)

        // uniform(テクスチャユニット)
        GLES20.glUniform1i(hUNI[1],u_Texture0)
        MyGLES20Func.checkGlError2("u_Texture0",this,model)

        // uniform(laplacianフィルタを使うかどうか)
        GLES20.glUniform1i(hUNI[2], u_laplacian)
        MyGLES20Func.checkGlError2("u_laplacian",this,model)

        // uniform(グレースケールを使うかどうか)
        GLES20.glUniform1i(hUNI[3], u_laplacianGray)
        MyGLES20Func.checkGlError2("u_laplacianGray",this,model)

        // uniform(カーネル)
        GLES20.glUniform1fv(hUNI[4], 9,u_Coef,0)
        MyGLES20Func.checkGlError2("u_Coef",this,model)

        // uniform(レンダリング領域の大きさ)
        GLES20.glUniform1f(hUNI[5], u_renderWH)
        MyGLES20Func.checkGlError2("u_renderWH",this,model)

        // モデルを描画
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, model.datIdx.size, GLES20.GL_UNSIGNED_SHORT, model.bufIdx)

        // リソース解放
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,0)
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER,0)
    }
}
