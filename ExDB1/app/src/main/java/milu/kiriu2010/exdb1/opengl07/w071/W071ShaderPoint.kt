package milu.kiriu2010.exdb1.opengl07.w071

import android.opengl.GLES20
import milu.kiriu2010.gui.model.MgModelAbs
import milu.kiriu2010.gui.basic.MyGLES20Func
import milu.kiriu2010.gui.shader.es20.ES20MgShader

// -------------------------------------
// シェーダ(点のレンダリングを行う)
// -------------------------------------
// テクスチャから座標位置を読み出す
// -------------------------------------
// 頂点テクスチャフェッチ
// -------------------------------------
// https://wgld.org/d/webgl/w071.html
// -------------------------------------
class W071ShaderPoint: ES20MgShader() {
    // 頂点シェーダ
    private val scv =
            """
            attribute float a_Index;

            uniform   mat4       u_matMVP;
            uniform   sampler2D  u_Texture;

            // 描きこみの対象となる
            // フレームバッファ(テクスチャ)一辺あたりの長さを１で割る
            const float frag     = 1.0/16.0;
            // さらに半分の値
            const float texShift = 0.5 * frag;

            void main() {
                // fract => x-floor(x)を返す
                // 頂点の識別番号に定数fragをかけ,
                // その結果の小数点以下の部分だけを抜き出す
                float pu = fract(a_Index*frag + texShift);
                float pv = floor(a_Index*frag)*frag + texShift;
                // オフセットさせながらテクスチャを参照
                vec3  tPosition = texture2D(u_Texture,vec2(pu,pv)).rgb*2.0 - 1.0;
                gl_Position     = u_matMVP * vec4(tPosition, 1.0);
                gl_PointSize    = 16.0;
            }
            """.trimIndent()

    // フラグメントシェーダ
    private val scf =
            """
            precision mediump   float;

            uniform sampler2D   u_Texture;

            void main() {
                gl_FragColor = texture2D(u_Texture, gl_PointCoord);
            }
            """.trimIndent()


    override fun loadShader(): ES20MgShader {
        // 頂点シェーダを生成
        svhandle = MyGLES20Func.loadShader(GLES20.GL_VERTEX_SHADER, scv)
        // フラグメントシェーダを生成
        sfhandle = MyGLES20Func.loadShader(GLES20.GL_FRAGMENT_SHADER, scf)

        // プログラムオブジェクトの生成とリンク
        programHandle = MyGLES20Func.createProgram(svhandle,sfhandle, arrayOf("a_Index") )
        return this
    }

    fun draw(model: MgModelAbs,
             matMVP: FloatArray,
             a_Index: Float,
             u_Texture: Int) {

        GLES20.glUseProgram(programHandle)
        MyGLES20Func.checkGlError2("UseProgram",this,model)

        // attribute(インデックス)
        model.bufIdx.position(0)
        GLES20.glGetAttribLocation(programHandle,"a_Index").also {
            GLES20.glVertexAttribPointer(it,1,GLES20.GL_FLOAT,false, 1*2, model.bufIdx)
            GLES20.glEnableVertexAttribArray(it)
        }
        MyGLES20Func.checkGlError2("a_Index",this,model)

        // attribute(インデックス)
        /*
        GLES20.glGetAttribLocation(programHandle,"a_Index").also {
            GLES20.glVertexAttrib1f(it,a_Index)
        }
        */
        model.bufIdx.position(0)
        GLES20.glGetAttribLocation(programHandle,"a_Index").also {
            GLES20.glVertexAttribPointer(it,1,GLES20.GL_FLOAT,false, 4, model.bufIdx)
            GLES20.glEnableVertexAttribArray(it)
        }
        MyGLES20Func.checkGlError2("a_Index",this,model)

        // uniform(モデル×ビュー×プロジェクション)
        GLES20.glGetUniformLocation(programHandle,"u_matMVP").also {
            GLES20.glUniformMatrix4fv(it,1,false,matMVP,0)
        }
        MyGLES20Func.checkGlError2("u_matMVP",this,model)

        // uniform(テクスチャユニット)
        GLES20.glGetUniformLocation(programHandle, "u_Texture").also {
            GLES20.glUniform1i(it, u_Texture)
        }
        MyGLES20Func.checkGlError2("u_Texture",this,model)

        // モデルを描画
        GLES20.glDrawArrays(GLES20.GL_POINTS,0,model.datIdx.size)
    }
}
