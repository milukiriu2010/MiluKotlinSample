package milu.kiriu2010.exdb1.opengl07.w072

import android.opengl.GLES20
import milu.kiriu2010.gui.model.MgModelAbs
import milu.kiriu2010.gui.basic.MyGLES20Func
import milu.kiriu2010.gui.shader.MgShader

// ----------------------------------------
// シェーダ(テクスチャへの描きこみを行う)
// ----------------------------------------
// 頂点の座標位置をテクスチャへ描きこむ
// ----------------------------------------
// 浮動小数点数VTF
// -------------------------------------
// https://wgld.org/d/webgl/w072.html
// ----------------------------------------
class W072ShaderMapping: MgShader() {
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

            const float rCoef = 1.0;
            const float gCoef = 1.0 / 255.0;
            const float bCoef = 1.0 / (255.0 * 255.0);

            void main() {
                // -1～1の範囲にある頂点の位置を0～1の範囲に収める
                float r   = a_Position.x * rCoef;
                float g   = a_Position.y * gCoef;
                float b   = a_Position.z * bCoef;
                v_Color = (vec3(r,g,b)+1.0) * 0.5;
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


    override fun loadShader(): MgShader {
        // 頂点シェーダを生成
        svhandle = MyGLES20Func.loadShader(GLES20.GL_VERTEX_SHADER, scv)
        // フラグメントシェーダを生成
        sfhandle = MyGLES20Func.loadShader(GLES20.GL_FRAGMENT_SHADER, scf)

        // プログラムオブジェクトの生成とリンク
        programHandle = MyGLES20Func.createProgram(svhandle,sfhandle, arrayOf("a_Position","a_Index") )
        return this
    }

    fun draw(model: MgModelAbs,
             a_Index: Float) {

        GLES20.glUseProgram(programHandle)
        MyGLES20Func.checkGlError2("UseProgram",this,model)

        // attribute(頂点)
        model.bufPos.position(0)
        GLES20.glGetAttribLocation(programHandle,"a_Position").also {
            GLES20.glVertexAttribPointer(it,3,GLES20.GL_FLOAT,false, 3*4, model.bufPos)
            GLES20.glEnableVertexAttribArray(it)
        }
        MyGLES20Func.checkGlError2("a_Position",this,model)

        // attribute(インデックス)
        /*
        GLES20.glGetAttribLocation(programHandle,"a_Index").also {
            GLES20.glVertexAttrib1f(it,a_Index)
        }
        */
        model.bufIdx.position(0)
        GLES20.glGetAttribLocation(programHandle,"a_Index").also {
            GLES20.glVertexAttribPointer(it,1,GLES20.GL_FLOAT,false, 2, model.bufIdx)
            GLES20.glEnableVertexAttribArray(it)
        }
        MyGLES20Func.checkGlError2("a_Index",this,model)

        // モデルを描画
        GLES20.glDrawArrays(GLES20.GL_POINTS,0,model.datIdx.size)
    }
}
