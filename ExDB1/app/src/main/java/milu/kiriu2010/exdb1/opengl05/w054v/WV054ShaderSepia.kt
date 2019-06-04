package milu.kiriu2010.exdb1.opengl05.w054v

import android.opengl.GLES20
import milu.kiriu2010.gui.basic.MyGLES20Func
import milu.kiriu2010.gui.model.MgModelAbs
import milu.kiriu2010.gui.shader.es20.ES20MgShader
import milu.kiriu2010.gui.vbo.es20.ES20VBOAbs

// グレースケール用シェーダ
class WV054ShaderSepia: ES20MgShader() {
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
            uniform   int         u_grayScale;
            uniform   int         u_sepiaScale;
            varying   vec2        v_TexCoord;

            // NTSC系加重平均法
            const float redScale   = 0.298912;
            const float greenScale = 0.586611;
            const float blueScale  = 0.114478;
            const vec3  monochromeScale = vec3(redScale, greenScale, blueScale);

            // セピアは、RGBで表すと(107,74,43)
            const float sRedScale   = 1.07;
            const float sGreenScale = 0.74;
            const float sBlueScale  = 0.43;
            const vec3  sepiaScale = vec3(sRedScale, sGreenScale, sBlueScale);

            // いったんグレースケール化した後で、RGBの比率でセピア変換する
            void main() {
                vec4 smpColor = texture2D(u_Texture0, v_TexCoord);
                float grayColor = dot(smpColor.rgb, monochromeScale);
                if (bool(u_grayScale)) {
                    smpColor = vec4(vec3(grayColor), 1.0);
                } else if (bool(u_sepiaScale)) {
                    vec3 monoColor = vec3(grayColor) * sepiaScale;
                    smpColor = vec4(monoColor, 1.0);
                }
                gl_FragColor = smpColor;
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
        hUNI = IntArray(4)

        // uniform(モデル×ビュー×プロジェクション)
        hUNI[0] = GLES20.glGetUniformLocation(programHandle,"u_matMVP")
        MyGLES20Func.checkGlError("u_matMVP:glGetUniformLocation")

        // テクスチャユニット
        hUNI[1] = GLES20.glGetUniformLocation(programHandle,"u_Texture0")
        MyGLES20Func.checkGlError("u_Texture:glGetUniformLocation")

        // uniform(グレースケールを使うかどうか)
        hUNI[2] = GLES20.glGetUniformLocation(programHandle, "u_grayScale")
        MyGLES20Func.checkGlError("u_grayScale:glGetUniformLocation")

        // uniform(セピア調にするかどうか)
        hUNI[3] = GLES20.glGetUniformLocation(programHandle, "u_sepiaScale")
        MyGLES20Func.checkGlError("u_sepiaScale:glGetUniformLocation")

        return this
    }

    fun draw(model: MgModelAbs,
             bo: ES20VBOAbs,
             u_matMVP: FloatArray,
             u_Texture0: Int,
             u_grayScale: Int,
             u_sepiaScale: Int) {

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

        // uniform(グレースケールを使うかどうか)
        GLES20.glUniform1i(hUNI[2], u_grayScale)
        MyGLES20Func.checkGlError2("u_grayScale",this,model)

        // uniform(セピア調にするかどうか)
        GLES20.glUniform1i(hUNI[3], u_sepiaScale)
        MyGLES20Func.checkGlError2("u_sepiaScale",this,model)

        // モデルを描画
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, model.datIdx.size, GLES20.GL_UNSIGNED_SHORT, model.bufIdx)

        // リソース解放
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,0)
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER,0)
    }
}
