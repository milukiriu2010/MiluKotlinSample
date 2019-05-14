package milu.kiriu2010.exdb1.opengl02.w026

import android.opengl.GLES20
import milu.kiriu2010.gui.basic.MyGLFunc
import milu.kiriu2010.gui.model.MgModelAbs
import milu.kiriu2010.gui.shader.MgShader

// ------------------------------------------------------
// シェーダ(テクスチャ)
// ------------------------------------------------------
// テクスチャ座標は0～1の範囲で指定する
// テクスチャ座標は左下が原点なので
// 単純に貼り付けると上下逆になる
// ------------------------------------------------------
// テクスチャユニットの最大数を取得するには
// gl.getParameter(gl.MAX_COMBINED_TEXTURE_IMAGE_UNITS)
// を使うとよいらしいが android は、わからない
// ------------------------------------------------------
class W026Shader: MgShader() {
    // 頂点シェーダ
    private val scv =
            """
            attribute vec3 a_Position;
            attribute vec4 a_Color;
            attribute vec2 a_TextureCoord;
            uniform   mat4 u_matMVP;
            varying   vec4 v_Color;
            varying   vec2 v_TextureCoord;

            void main() {
                v_Color        = a_Color;
                v_TextureCoord = a_TextureCoord;
                gl_Position    = u_matMVP * vec4(a_Position,1.0);
            }
            """.trimIndent()

    // フラグメントシェーダ
    private val scf =
            """
            precision mediump float;

            uniform   sampler2D  u_Texture;
            varying   vec4       v_Color;
            varying   vec2       v_TextureCoord;

            void main() {
                // テクスチャデータからフラグメントの情報(色情報)を抜き出す
                vec4 smpColor = texture2D(u_Texture, v_TextureCoord);
                // 頂点の色とテクスチャの色情報を掛け合わせた色が出力される
                gl_FragColor  = v_Color * smpColor;
            }
            """.trimIndent()

    override fun loadShader(): MgShader {
        // 頂点シェーダを生成
        val svhandle = MyGLFunc.loadShader(GLES20.GL_VERTEX_SHADER, scv)
        // フラグメントシェーダを生成
        val sfhandle = MyGLFunc.loadShader(GLES20.GL_FRAGMENT_SHADER, scf)

        // プログラムオブジェクトの生成とリンク
        programHandle = MyGLFunc.createProgram(svhandle,sfhandle, arrayOf("a_Position","a_Color","a_TextureCoord") )
        return this
    }

    fun draw(model: MgModelAbs,
             matMVP: FloatArray,
             texture: Int
    ) {
        // attribute(頂点)
        model.bufPos.position(0)
        GLES20.glGetAttribLocation(programHandle,"a_Position").also {
            GLES20.glVertexAttribPointer(it,3,GLES20.GL_FLOAT,false, 3*4, model.bufPos)
            GLES20.glEnableVertexAttribArray(it)
        }
        MyGLFunc.checkGlError2("a_Position",this,model)

        // attribute(色)
        model.bufCol.position(0)
        GLES20.glGetAttribLocation(programHandle,"a_Color").also {
            GLES20.glVertexAttribPointer(it,4,GLES20.GL_FLOAT,false, 4*4, model.bufCol)
            GLES20.glEnableVertexAttribArray(it)
        }
        MyGLFunc.checkGlError2("a_Color",this,model)

        // attribute(テクスチャコード)
        model.bufTxc.position(0)
        GLES20.glGetAttribLocation(programHandle,"a_TextureCoord").also {
            GLES20.glVertexAttribPointer(it,2,GLES20.GL_FLOAT,false, 2*4, model.bufTxc)
            GLES20.glEnableVertexAttribArray(it)
        }
        MyGLFunc.checkGlError2("a_TextureCoord",this,model)

        // uniform(モデル×ビュー×プロジェクション)
        GLES20.glGetUniformLocation(programHandle,"u_matMVP").also {
            GLES20.glUniformMatrix4fv(it,1,false,matMVP,0)
        }
        MyGLFunc.checkGlError2("u_matMVP",this,model)

        // uniform(テクスチャユニット)
        GLES20.glGetUniformLocation(programHandle,"u_Texture").also {
            GLES20.glUniform1i(it,texture)
        }
        MyGLFunc.checkGlError2("u_Texture",this,model)

        // モデルを描画
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, model.datIdx.size, GLES20.GL_UNSIGNED_SHORT, model.bufIdx)
    }
}
