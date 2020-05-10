package milu.kiriu2010.exdb1.opengl05.w050v

import android.opengl.GLES20
import milu.kiriu2010.gui.basic.MyGLES20Func
import milu.kiriu2010.gui.model.MgModelAbs
import milu.kiriu2010.gui.shader.es20.ES20MgShader
import milu.kiriu2010.gui.vbo.es20.ES20VBOAbs

// -------------------------------------------------------
// シェーダ(光学迷彩):VBOあり
//// OpenGL ES 2.0
// -------------------------------------------------------
// 透けて見えるため、反射光によるハイライトは入らない。
// ハイライトが入ると、ゼリーのような見た目になるらしい。
// -------------------------------------------------------
// https://wgld.org/d/webgl/w050.html
// -------------------------------------------------------
class WV050ShaderStealth: ES20MgShader() {
    // 頂点シェーダ
    private val scv =
            """
            attribute vec3  a_Position;
            attribute vec3  a_Normal;
            attribute vec4  a_Color;
            uniform   mat4  u_matM;
            // ビュー×プロジェクション×テクスチャ座標変換行列
            uniform   mat4  u_matVPT;
            uniform   mat4  u_matMVP;
            // テクスチャ座標をずらすために使われる係数
            uniform   float u_coefficient;
            varying   vec4  v_Color;
            varying   vec4  v_TexCoord;

            void main() {
                vec3   pos  = (u_matM * vec4(a_Position, 1.0)).xyz;
                vec3   nor  = normalize((u_matM * vec4(a_Normal, 1.0)).xyz);
                v_Color     = a_Color;
                // ----------------------------------------------------------------------
                // モデル座標変換行列を掛け合わせた頂点位置と
                // テクスチャ座標変換行列とをかけあわせることで
                // テクスチャ座標を取得
                // ----------------------------------------------------------------------
                // 係数と法線を掛け合わせた数値を加算することでテクスチャ座標をずらしている
                // ----------------------------------------------------------------------
                v_TexCoord  = u_matVPT * vec4(pos + nor * u_coefficient, 1.0);
                gl_Position = u_matMVP * vec4(a_Position, 1.0);
            }
            """.trimIndent()

    // フラグメントシェーダ
    private val scf =
            """
            precision mediump   float;

            uniform   sampler2D u_Texture0;
            varying   vec4      v_Color;
            varying   vec4      v_TexCoord;

            void main() {
                vec4 smpColor = texture2DProj(u_Texture0, v_TexCoord);
                gl_FragColor  = v_Color * smpColor;
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
        hATTR = IntArray(3)
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

        // 属性(法線)
        hATTR[1] = GLES20.glGetAttribLocation(programHandle, "a_Normal").also {
            // attribute属性を有効にする
            // ここで呼ばないと描画されない
            GLES20.glEnableVertexAttribArray(it)
            MyGLES20Func.checkGlError("a_Normal:glEnableVertexAttribArray")
            // attribute属性を登録
            GLES20.glVertexAttribPointer(it,3,GLES20.GL_FLOAT,false,0,0)
            MyGLES20Func.checkGlError("a_Normal:glVertexAttribPointer")
        }
        MyGLES20Func.checkGlError("a_Normal:glGetAttribLocation")

        // 属性(色)
        hATTR[2] = GLES20.glGetAttribLocation(programHandle, "a_Color").also {
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
        hUNI = IntArray(6)

        // uniform(モデル)
        hUNI[0] = GLES20.glGetUniformLocation(programHandle,"u_matM")
        MyGLES20Func.checkGlError("u_matM:glGetUniformLocation")

        // uniform(ビュー×プロジェクション×テクスチャ座標変換行列)
        hUNI[1] = GLES20.glGetUniformLocation(programHandle,"u_matVPT")
        MyGLES20Func.checkGlError("u_matVPT:glGetUniformLocation")

        // uniform(モデル×ビュー×プロジェクション)
        hUNI[2] = GLES20.glGetUniformLocation(programHandle,"u_matMVP")
        MyGLES20Func.checkGlError("u_matMVP:glGetUniformLocation")

        // uniform(光学迷彩にかける補正係数)
        hUNI[3] = GLES20.glGetUniformLocation(programHandle,"u_coefficient")
        MyGLES20Func.checkGlError("u_coefficient:glGetUniformLocation")

        // uniform(テクスチャユニット)
        hUNI[4] = GLES20.glGetUniformLocation(programHandle,"u_Texture0")
        MyGLES20Func.checkGlError("u_Texture0:glGetUniformLocation")

        return this
    }

    // 光学迷彩
    fun draw(model: MgModelAbs,
             bo: ES20VBOAbs,
             u_matM: FloatArray,
             u_matVPT: FloatArray,
             u_matMVP: FloatArray,
             u_coefficient: Float,
             u_Texture0: Int) {
        GLES20.glUseProgram(programHandle)
        MyGLES20Func.checkGlError2("UserProgram",this,model)

        // attribute(頂点)
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,bo.hVBO[0])
        GLES20.glVertexAttribPointer(hATTR[0],3,GLES20.GL_FLOAT,false,0,0)
        MyGLES20Func.checkGlError2("a_Position",this,model)

        // attribute(法線)
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,bo.hVBO[1])
        GLES20.glVertexAttribPointer(hATTR[1],3,GLES20.GL_FLOAT,false,0,0)
        MyGLES20Func.checkGlError2("a_Normal",this,model)

        // attribute(色)
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,bo.hVBO[2])
        GLES20.glVertexAttribPointer(hATTR[2],4,GLES20.GL_FLOAT,false,0,0)
        MyGLES20Func.checkGlError2("a_Color",this,model)

        // uniform(モデル)
        GLES20.glUniformMatrix4fv(hUNI[0],1,false,u_matM,0)
        MyGLES20Func.checkGlError2("u_matM",this,model)

        // uniform(ビュー×プロジェクション×テクスチャ座標変換行列)
        GLES20.glUniformMatrix4fv(hUNI[1],1,false,u_matVPT,0)
        MyGLES20Func.checkGlError2("u_matVPT",this,model)

        // uniform(モデル×ビュー×プロジェクション)
        GLES20.glUniformMatrix4fv(hUNI[2],1,false,u_matMVP,0)
        MyGLES20Func.checkGlError2("u_matMVP",this,model)

        // uniform(光学迷彩にかける補正係数)
        GLES20.glUniform1f(hUNI[3],u_coefficient)
        MyGLES20Func.checkGlError2("u_coefficient",this,model)

        // uniform(テクスチャユニット)
        GLES20.glUniform1i(hUNI[4],u_Texture0)
        MyGLES20Func.checkGlError2("u_Texture0",this,model)

        // モデルを描画
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, model.datIdx.size, GLES20.GL_UNSIGNED_SHORT, model.bufIdx)

        // リソース解放
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,0)
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER,0)
    }
}
