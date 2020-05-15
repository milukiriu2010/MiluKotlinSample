package milu.kiriu2010.exdb1.opengl06.w058v

import android.opengl.GLES20
import milu.kiriu2010.gui.basic.MyGLES20Func
import milu.kiriu2010.gui.model.MgModelAbs
import milu.kiriu2010.gui.shader.es20.ES20MgShader
import milu.kiriu2010.gui.vbo.es20.ES20VBOAbs

// --------------------------------------------
// シェーダ(正射影でレンダリング結果を合成)
//   レンダリングされた全てのシーンを合成する
// OpenGL ES 2.0:VBOあり
// --------------------------------------------
// https://wgld.org/d/webgl/w058.html
// --------------------------------------------
class WV058ShaderFinal: ES20MgShader() {
    // 頂点シェーダ
    private val scv =
            """
            attribute vec3  a_Position;
            attribute vec2  a_TextureCoord;
            uniform   mat4  u_matMVP;
            varying   vec2  v_TextureCoord;

            void main() {
                v_TextureCoord = a_TextureCoord;
                gl_Position    = u_matMVP   * vec4(a_Position, 1.0);
            }
            """.trimIndent()

    // フラグメントシェーダ
    private val scf =
            """
            precision mediump   float;

            // フルカラーの本来のレンダリング結果
            uniform   sampler2D   u_Texture1;
            // ブラーをかけた反射光のレンダリング結果
            uniform   sampler2D   u_Texture2;
            uniform   int         u_glare;
            varying   vec2        v_TextureCoord;

            void main() {
                vec4 destColor = texture2D(u_Texture1, v_TextureCoord);
                vec4 smpColor  = texture2D(u_Texture2, vec2(v_TextureCoord.s,1.0-v_TextureCoord.t));

                if ( bool(u_glare) ) {
                    // 反射光を２倍している
                    destColor += smpColor * 2.0;
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

        // uniform(テクスチャユニット１)
        hUNI[1] = GLES20.glGetUniformLocation(programHandle, "u_Texture1")
        MyGLES20Func.checkGlError("u_Texture1:glGetUniformLocation")

        // uniform(テクスチャユニット２)
        hUNI[2] = GLES20.glGetUniformLocation(programHandle, "u_Texture2")
        MyGLES20Func.checkGlError("u_Texture2:glGetUniformLocation")

        // uniform(グレアをかけるかどうか)
        hUNI[3] = GLES20.glGetUniformLocation(programHandle, "u_glare")
        MyGLES20Func.checkGlError("u_glare:glGetUniformLocation")

        return this
    }


    fun draw(model: MgModelAbs,
             bo: ES20VBOAbs,
             u_matMVP: FloatArray,
             u_Texture1: Int,
             u_Texture2: Int,
             u_glare: Int) {
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

        // uniform(テクスチャユニット１)
        GLES20.glUniform1i(hUNI[1], u_Texture1)
        MyGLES20Func.checkGlError2("u_Texture1",this,model)

        // uniform(テクスチャユニット２)
        GLES20.glUniform1i(hUNI[2], u_Texture2)
        MyGLES20Func.checkGlError2("u_Texture2",this,model)

        // uniform(グレアをかけるかどうか)
        GLES20.glUniform1i(hUNI[3], u_glare)
        MyGLES20Func.checkGlError2("u_glare",this,model)

        // モデルを描画
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, model.datIdx.size, GLES20.GL_UNSIGNED_SHORT, model.bufIdx)

        // リソース解放
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,0)
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER,0)
    }
}
