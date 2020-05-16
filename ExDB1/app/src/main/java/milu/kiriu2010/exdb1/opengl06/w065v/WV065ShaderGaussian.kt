package milu.kiriu2010.exdb1.opengl06.w065v

import android.opengl.GLES20
import milu.kiriu2010.gui.model.MgModelAbs
import milu.kiriu2010.gui.basic.MyGLES20Func
import milu.kiriu2010.gui.shader.es20.ES20MgShader
import milu.kiriu2010.gui.vbo.es20.ES20VBOAbs

// -----------------------------------------------------
// gaussianフィルタ用シェーダ:VBOあり
// OpenGL ES 2.0
// -----------------------------------------------------
// ぼかしフィルタ
// -----------------------------------------------------
// https://wgld.org/d/webgl/w065.html
// -----------------------------------------------------
class WV065ShaderGaussian: ES20MgShader() {
    // 頂点シェーダ
    private val scv =
            """
            attribute vec3  a_Position;
            attribute vec2  a_TextureCoord;
            uniform   mat4  u_matO;
            varying   vec2  v_TexCoord;

            void main() {
                v_TexCoord  = a_TextureCoord;
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

    override fun loadShader(): ES20MgShader {
        // 頂点シェーダを生成
        svhandle = MyGLES20Func.loadShader(GLES20.GL_VERTEX_SHADER, scv)
        // フラグメントシェーダを生成
        sfhandle = MyGLES20Func.loadShader(GLES20.GL_FRAGMENT_SHADER, scf)

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
        hUNI = IntArray(5)

        // uniform()
        hUNI[0] = GLES20.glGetUniformLocation(programHandle,"u_matO")
        MyGLES20Func.checkGlError("u_matO:glGetUniformLocation")

        // uniform(テクスチャユニット)
        hUNI[1] = GLES20.glGetUniformLocation(programHandle,"u_Texture0")
        MyGLES20Func.checkGlError("u_Texture:glGetUniformLocation")

        // uniform(カーネル)
        hUNI[2] = GLES20.glGetUniformLocation(programHandle, "u_weight")
        MyGLES20Func.checkGlError("u_weight:glGetUniformLocation")

        // uniform(水平方向かどうか)
        hUNI[3] = GLES20.glGetUniformLocation(programHandle, "u_horizontal")
        MyGLES20Func.checkGlError("u_horizontal:glGetUniformLocation")

        // uniform(レンダリング領域の大きさ)
        hUNI[4] = GLES20.glGetUniformLocation(programHandle, "u_renderWH")
        MyGLES20Func.checkGlError("u_renderWH:glGetUniformLocation")

        return this
    }

    fun draw(model: MgModelAbs,
             bo: ES20VBOAbs,
             u_matO: FloatArray,
             u_Texture0: Int,
             u_weight: FloatArray,
             u_horizontal: Int,
             u_renderWH: Float) {

        GLES20.glUseProgram(programHandle)
        MyGLES20Func.checkGlError2("UseProgram", this,model)

        // attribute(頂点)
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,bo.hVBO[0])
        GLES20.glVertexAttribPointer(hATTR[0],3,GLES20.GL_FLOAT,false,0,0)
        MyGLES20Func.checkGlError2("a_Position",this,model)

        // attribute(テクスチャ座標)
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,bo.hVBO[1])
        GLES20.glVertexAttribPointer(hATTR[1],2,GLES20.GL_FLOAT,false,0,0)
        MyGLES20Func.checkGlError2("a_TexCoord",this,model)

        // uniform()
        GLES20.glUniformMatrix4fv(hUNI[0],1,false,u_matO,0)
        MyGLES20Func.checkGlError2("u_matO", this,model)

        // uniform(テクスチャユニット)
        GLES20.glUniform1i(hUNI[1], u_Texture0)
        MyGLES20Func.checkGlError2("u_Texture0", this,model)

        // uniform(カーネル)
        GLES20.glUniform1fv(hUNI[2], 5,u_weight,0)
        MyGLES20Func.checkGlError2("u_weight", this,model)

        // uniform(水平方向かどうか)
        GLES20.glUniform1i(hUNI[3], u_horizontal)
        MyGLES20Func.checkGlError2("u_horizontal", this,model)

        // uniform(画像の大きさ)
        GLES20.glUniform1f(hUNI[4], u_renderWH)
        MyGLES20Func.checkGlError2("u_renderWH", this,model)

        // モデルを描画
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, model.datIdx.size, GLES20.GL_UNSIGNED_SHORT, model.bufIdx)

        // リソース解放
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,0)
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER,0)
    }
}
