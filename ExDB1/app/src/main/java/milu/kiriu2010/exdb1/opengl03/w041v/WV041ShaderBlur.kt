package milu.kiriu2010.exdb1.opengl03.w041v

import android.opengl.GLES20
import milu.kiriu2010.gui.basic.MyGLES20Func
import milu.kiriu2010.gui.model.MgModelAbs
import milu.kiriu2010.gui.shader.es20.ES20MgShader
import milu.kiriu2010.gui.vbo.es20.ES20VBOAbs

// -----------------------------------
// シェーダ(ブラー)
// -----------------------------------
// https://wgld.org/d/webgl/w041.html
// -----------------------------------
class WV041ShaderBlur: ES20MgShader() {
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

        // 属性(色)
        hATTR[1] = GLES20.glGetAttribLocation(programHandle, "a_Color").also {
            // attribute属性を有効にする
            // ここで呼ばないと描画されない
            GLES20.glEnableVertexAttribArray(it)
            MyGLES20Func.checkGlError("a_Color:glEnableVertexAttribArray")
            // attribute属性を登録
            GLES20.glVertexAttribPointer(it,4,GLES20.GL_FLOAT,false,0,0)
            MyGLES20Func.checkGlError("a_Color:glVertexAttribPointer")
        }
        MyGLES20Func.checkGlError("a_Color:glGetAttribLocation")

        // ----------------------------------------------
        // uniformハンドルに値をセット
        // ----------------------------------------------
        hUNI = IntArray(4)
        // uniform(モデル×ビュー×プロジェクション)
        hUNI[0] = GLES20.glGetUniformLocation(programHandle,"u_matMVP")
        MyGLES20Func.checkGlError("u_matMVP:glGetUniformLocation")

        // uniform(ブラーするかどうか)
        hUNI[1] = GLES20.glGetUniformLocation(programHandle,"u_useBlur")
        MyGLES20Func.checkGlError("u_useBlur:glGetUniformLocation")

        // uniform(テクスチャユニット)
        hUNI[2] = GLES20.glGetUniformLocation(programHandle, "u_Texture0")
        MyGLES20Func.checkGlError("u_Texture0:glGetUniformLocation")

        // uniform(レンダリング領域の幅・高さ)
        hUNI[3] = GLES20.glGetUniformLocation(programHandle, "u_renderWH")
        MyGLES20Func.checkGlError("u_renderWH:glGetUniformLocation")

        return this
    }

    fun draw(model: MgModelAbs,
             bo: ES20VBOAbs,
             u_matMVP: FloatArray,
             u_useBlur: Int,
             u_Texture0: Int,
             u_renderWH: Float) {
        GLES20.glUseProgram(programHandle)
        MyGLES20Func.checkGlError2("UserProgram",this,model)

        // attribute(位置)
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,bo.hVBO[0])
        GLES20.glVertexAttribPointer(hATTR[0],3,GLES20.GL_FLOAT,false,0,0)
        MyGLES20Func.checkGlError2("a_Position",this,model)

        // attribute(色)
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,bo.hVBO[1])
        GLES20.glVertexAttribPointer(hATTR[1],4,GLES20.GL_FLOAT,false,0,0)
        MyGLES20Func.checkGlError2("a_Color",this,model)

        // uniform(モデル×ビュー×プロジェクション)
        GLES20.glUniformMatrix4fv(hUNI[0],1,false,u_matMVP,0)
        MyGLES20Func.checkGlError2("u_matMVP",this,model)

        // uniform(ブラーするかどうか)
        GLES20.glUniform1i(hUNI[1],u_useBlur)
        MyGLES20Func.checkGlError2("u_useBlur",this,model)

        // uniform(テクスチャユニット)
        GLES20.glUniform1i(hUNI[2], u_Texture0)
        MyGLES20Func.checkGlError2("u_Texture0",this,model)

        // uniform(レンダリング領域の幅・高さ)
        GLES20.glUniform1f(hUNI[3], u_renderWH)
        MyGLES20Func.checkGlError2("u_renderWH",this,model)

        // モデルを描画
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, model.datIdx.size, GLES20.GL_UNSIGNED_SHORT, model.bufIdx)

        // リソース解放
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,0)
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER,0)
    }
}
