package milu.kiriu2010.exdb1.opengl03.w041x

import android.opengl.GLES20
import milu.kiriu2010.gui.basic.MyGLFunc
import milu.kiriu2010.gui.model.MgModelAbs
import milu.kiriu2010.gui.shader.MgShader

class W041XBShader: MgShader() {
    // 頂点シェーダ
    private val scv =
            """
            attribute vec3  a_Position;
            attribute vec4  a_Color;
            uniform   mat4  u_matMVP;
            varying   vec4  v_Color;

            void main() {
                v_Color        = a_Color;
                gl_Position    = u_matMVP * vec4(a_Position, 1.0);
            }
            """.trimIndent()

    // フラグメントシェーダ
    private val scf =
            """
            precision mediump   float;

            uniform   sampler2D u_Texture0;
            uniform   int       u_useBlur;
            uniform   float     u_renderWH;
            varying   vec4      v_Color;

            void main() {
                // 256x256pixelの画像フォーマットをそのまま使う場合
                //vec2 tFrag     = vec2(1.0/256);

                // gl_FragCoord
                //   これから描かれようとしているフラグメントのピクセル単位の座標
                // tFrag
                //   gl_FragCoord を参照して得られた値を、テクスチャ座標に変換するために使う
                // ----------------------------------------------------------------------
                // 画像をレンダリングの幅・高さに合わせている場合に、こちらを使う
                vec2 tFrag     = vec2(1.0/u_renderWH);
                vec4 destColor = texture2D(u_Texture0, gl_FragCoord.st * tFrag);
                if(bool(u_useBlur)){
                    destColor *= 0.36;
                    destColor += texture2D(u_Texture0, (gl_FragCoord.st + vec2(-1.0,  1.0)) * tFrag) * 0.04;
                    destColor += texture2D(u_Texture0, (gl_FragCoord.st + vec2( 0.0,  1.0)) * tFrag) * 0.04;
                    destColor += texture2D(u_Texture0, (gl_FragCoord.st + vec2( 1.0,  1.0)) * tFrag) * 0.04;
                    destColor += texture2D(u_Texture0, (gl_FragCoord.st + vec2(-1.0,  0.0)) * tFrag) * 0.04;
                    destColor += texture2D(u_Texture0, (gl_FragCoord.st + vec2( 1.0,  0.0)) * tFrag) * 0.04;
                    destColor += texture2D(u_Texture0, (gl_FragCoord.st + vec2(-1.0, -1.0)) * tFrag) * 0.04;
                    destColor += texture2D(u_Texture0, (gl_FragCoord.st + vec2( 0.0, -1.0)) * tFrag) * 0.04;
                    destColor += texture2D(u_Texture0, (gl_FragCoord.st + vec2( 1.0, -1.0)) * tFrag) * 0.04;
                    destColor += texture2D(u_Texture0, (gl_FragCoord.st + vec2(-2.0,  2.0)) * tFrag) * 0.02;
                    destColor += texture2D(u_Texture0, (gl_FragCoord.st + vec2(-1.0,  2.0)) * tFrag) * 0.02;
                    destColor += texture2D(u_Texture0, (gl_FragCoord.st + vec2( 0.0,  2.0)) * tFrag) * 0.02;
                    destColor += texture2D(u_Texture0, (gl_FragCoord.st + vec2( 1.0,  2.0)) * tFrag) * 0.02;
                    destColor += texture2D(u_Texture0, (gl_FragCoord.st + vec2( 2.0,  2.0)) * tFrag) * 0.02;
                    destColor += texture2D(u_Texture0, (gl_FragCoord.st + vec2(-2.0,  1.0)) * tFrag) * 0.02;
                    destColor += texture2D(u_Texture0, (gl_FragCoord.st + vec2( 2.0,  1.0)) * tFrag) * 0.02;
                    destColor += texture2D(u_Texture0, (gl_FragCoord.st + vec2(-2.0,  0.0)) * tFrag) * 0.02;
                    destColor += texture2D(u_Texture0, (gl_FragCoord.st + vec2( 2.0,  0.0)) * tFrag) * 0.02;
                    destColor += texture2D(u_Texture0, (gl_FragCoord.st + vec2(-2.0, -1.0)) * tFrag) * 0.02;
                    destColor += texture2D(u_Texture0, (gl_FragCoord.st + vec2( 2.0, -1.0)) * tFrag) * 0.02;
                    destColor += texture2D(u_Texture0, (gl_FragCoord.st + vec2(-2.0, -2.0)) * tFrag) * 0.02;
                    destColor += texture2D(u_Texture0, (gl_FragCoord.st + vec2(-1.0, -2.0)) * tFrag) * 0.02;
                    destColor += texture2D(u_Texture0, (gl_FragCoord.st + vec2( 0.0, -2.0)) * tFrag) * 0.02;
                    destColor += texture2D(u_Texture0, (gl_FragCoord.st + vec2( 1.0, -2.0)) * tFrag) * 0.02;
                    destColor += texture2D(u_Texture0, (gl_FragCoord.st + vec2( 2.0, -2.0)) * tFrag) * 0.02;
                }
                gl_FragColor = v_Color * destColor;
            }
            """.trimIndent()

    override fun loadShader(): MgShader {
        // 頂点シェーダを生成
        val svhandle = MyGLFunc.loadShader(GLES20.GL_VERTEX_SHADER, scv)
        // フラグメントシェーダを生成
        val sfhandle = MyGLFunc.loadShader(GLES20.GL_FRAGMENT_SHADER, scf)

        // プログラムオブジェクトの生成とリンク
        programHandle = MyGLFunc.createProgram(svhandle,sfhandle, arrayOf("a_Position","a_Color") )
        return this
    }

    fun draw(model: MgModelAbs,
             matMVP: FloatArray,
             u_useBlur: Int,
             u_Texture0: Int,
             u_renderWH: Float) {

        // プログラムハンドル(ブラー用)を有効化
        GLES20.glUseProgram(programHandle)

        // attribute(頂点)
        model.bufPos.position(0)
        GLES20.glGetAttribLocation(programHandle,"a_Position").also {
            GLES20.glVertexAttribPointer(it,3,GLES20.GL_FLOAT,false, 3*4, model.bufPos)
            GLES20.glEnableVertexAttribArray(it)
        }
        MyGLFunc.checkGlError("a_Position:${javaClass.simpleName}")

        // attribute(色)
        model.bufCol.position(0)
        GLES20.glGetAttribLocation(programHandle,"a_Color").also {
            GLES20.glVertexAttribPointer(it,4,GLES20.GL_FLOAT,false, 4*4, model.bufCol)
            GLES20.glEnableVertexAttribArray(it)
        }
        MyGLFunc.checkGlError("a_Color")

        // uniform(モデル×ビュー×プロジェクション)
        GLES20.glGetUniformLocation(programHandle,"u_matMVP").also {
            GLES20.glUniformMatrix4fv(it,1,false,matMVP,0)
        }
        MyGLFunc.checkGlError("u_matMVP")

        // uniform(ブラーするかどうか)
        GLES20.glGetUniformLocation(programHandle,"u_useBlur").also {
            GLES20.glUniform1i(it,u_useBlur)
        }

        // uniform(テクスチャ0)
        GLES20.glGetUniformLocation(programHandle, "u_Texture0").also {
            GLES20.glUniform1i(it, u_Texture0)
        }
        MyGLFunc.checkGlError("u_Texture0")

        // uniform(レンダ領域の幅・高さ)
        GLES20.glGetUniformLocation(programHandle, "u_renderWH").also {
            GLES20.glUniform1f(it, u_renderWH)
        }

        MyGLFunc.checkGlError("u_renderWH")

        // モデルを描画
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, model.datIdx.size, GLES20.GL_UNSIGNED_SHORT, model.bufIdx)
    }
}
