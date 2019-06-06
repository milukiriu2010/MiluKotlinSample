package milu.kiriu2010.exdb1.opengl07.w071v

import android.opengl.GLES20
import milu.kiriu2010.gui.model.MgModelAbs
import milu.kiriu2010.gui.basic.MyGLES20Func
import milu.kiriu2010.gui.shader.es20.ES20MgShader
import milu.kiriu2010.gui.vbo.es20.ES20VBOAbs

// ----------------------------------------
// シェーダ(テクスチャへの描きこみを行う)
// ----------------------------------------
// 頂点の座標位置をテクスチャへ描きこむ
// ----------------------------------------
// 頂点テクスチャフェッチ
// ----------------------------------------
// https://wgld.org/d/webgl/w071.html
// ----------------------------------------
class WV071ShaderMapping: ES20MgShader() {
    // 頂点シェーダ
    private val scv =
            """
            attribute vec3  a_Position;
            attribute float a_Index;

            varying   vec3  v_Color;

            // 描きこみの対象となる
            // フレームバッファ(テクスチャ)一辺あたりの長さを１で割る
            const float frag     = 1.0/16.0;
            // さらに半分の値
            const float texShift = 0.5 * frag;

            void main() {
                // -1～1の範囲にある頂点の位置を0～1の範囲に収める
                v_Color   = (normalize(a_Position)+1.0) * 0.5;
                // fract => x-floor(x)を返す
                // 頂点の識別番号に定数fragをかけ,
                // その結果の小数点以下の部分だけを抜き出す
                float pu = fract(a_Index*frag)*2.0 - 1.0;
                float pv = floor(a_Index*frag)*frag*2.0 - 1.0;
                gl_Position  = vec4(pu+texShift, pv+texShift, 0.0, 1.0);
                gl_PointSize = 1.0;
            }
            """.trimIndent()

    // フラグメントシェーダ
    private val scf =
            """
            precision mediump   float;

            varying  vec3      v_Color;

            void main() {
                gl_FragColor = vec4(v_Color, 1.0);
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

        // 属性(インデックス)
        hATTR[1] = GLES20.glGetAttribLocation(programHandle,"a_Index").also {
            // attribute属性を有効にする
            // ここで呼ばないと描画されない
            GLES20.glEnableVertexAttribArray(it)
            MyGLES20Func.checkGlError("a_Index:glEnableVertexAttribArray")
            // attribute属性を登録
            GLES20.glVertexAttribPointer(it,1,GLES20.GL_FLOAT,false,0,0)
            MyGLES20Func.checkGlError("a_Index:glVertexAttribPointer")
        }
        MyGLES20Func.checkGlError("a_Index:glGetAttribLocation")

        return this
    }

    fun draw(model: MgModelAbs,
             bo: ES20VBOAbs) {

        GLES20.glUseProgram(programHandle)
        MyGLES20Func.checkGlError2("UseProgram",this,model)

        // attribute(頂点)
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,bo.hVBO[0])
        GLES20.glVertexAttribPointer(hATTR[0],3,GLES20.GL_FLOAT,false,0,0)
        MyGLES20Func.checkGlError2("a_Position",this,model)

        // attribute(インデックス)
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,bo.hVBO[1])
        GLES20.glVertexAttribPointer(hATTR[1],1,GLES20.GL_FLOAT,false,0,0)
        MyGLES20Func.checkGlError2("a_Index",this,model)

        // モデルを描画
        GLES20.glDrawArrays(GLES20.GL_POINTS,0,model.datIdx.size)

        // リソース解放
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,0)
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER,0)
    }
}
