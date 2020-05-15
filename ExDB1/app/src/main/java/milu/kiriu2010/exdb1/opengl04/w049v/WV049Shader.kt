package milu.kiriu2010.exdb1.opengl04.w049v

import android.opengl.GLES20
import milu.kiriu2010.gui.basic.MyGLES20Func
import milu.kiriu2010.gui.model.MgModelAbs
import milu.kiriu2010.gui.shader.es20.ES20MgShader
import milu.kiriu2010.gui.vbo.es20.ES20VBOAbs

// --------------------------------------
// 射影テクスチャマッピング:VBOあり
// OpenGL ES 2.0
// --------------------------------------
// https://wgld.org/d/webgl/w049.html
// --------------------------------------
class WV049Shader: ES20MgShader() {
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
            varying   vec3  v_Position;
            varying   vec3  v_Normal;
            varying   vec4  v_Color;
            // 射影変換を適用した頂点座標
            varying   vec4  v_TexCoord;

            void main() {
                v_Position    = (u_matM * vec4(a_Position,1.0)).xyz;
                v_Normal      = a_Normal;
                v_Color       = a_Color;
                // モデル座標変換行列を掛け合わせた頂点位置と
                // テクスチャ座標変換行列とをかけあわせることで
                // テクスチャ座標を取得
                v_TexCoord    = u_matVPT * vec4(v_Position,1.0);
                gl_Position   = u_matMVP * vec4(a_Position,1.0);
            }
            """.trimIndent()

    // フラグメントシェーダ
    private val scf =
            """
            precision mediump     float;

            uniform   mat4        u_matINV;
            uniform   vec3        u_vecLight;
            uniform   sampler2D   u_Texture0;
            varying   vec3        v_Position;
            varying   vec3        v_Normal;
            varying   vec4        v_Color;
            varying   vec4        v_TexCoord;

            void main() {
                // 点光源でのライティング
                vec3  light    = u_vecLight - v_Position;
                vec3  invLight = normalize(u_matINV * vec4(light,0.0)).xyz;
                float diffuse  = clamp(dot(v_Normal,invLight), 0.1, 1.0);
                // 頂点を射影変換した結果からテクスチャをサンプリングするため
                // texture2DProjを使う
                vec4  smpColor = texture2DProj(u_Texture0, v_TexCoord);
                gl_FragColor   = v_Color * vec4(vec3(diffuse), 1.0) * smpColor;
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

        // uniform(逆行列)
        hUNI[3] = GLES20.glGetUniformLocation(programHandle,"u_matINV")
        MyGLES20Func.checkGlError("u_matINV:glGetUniformLocation")

        // uniform(光源位置)
        hUNI[4] = GLES20.glGetUniformLocation(programHandle,"u_vecLight")
        MyGLES20Func.checkGlError("u_vecLight:glGetUniformLocation")

        // uniform(テクスチャユニット)
        hUNI[5] = GLES20.glGetUniformLocation(programHandle, "u_Texture0")
        MyGLES20Func.checkGlError("u_Texture0:glGetUniformLocation")

        return this
    }

    fun draw(model: MgModelAbs,
             bo: ES20VBOAbs,
             u_matM: FloatArray,
             u_matVPT: FloatArray,
             u_matMVP: FloatArray,
             u_matI: FloatArray,
             u_vecLight: FloatArray,
             u_Texture0: Int) {

        GLES20.glUseProgram(programHandle)
        MyGLES20Func.checkGlError2("UseProgram",this,model)

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

        // uniform(逆行列)
        GLES20.glUniformMatrix4fv(hUNI[3],1,false,u_matI,0)
        MyGLES20Func.checkGlError2("u_matINV",this,model)

        // uniform(光源位置)
        GLES20.glUniform3fv(hUNI[4],1,u_vecLight,0)
        MyGLES20Func.checkGlError2("u_vecLight",this,model)

        // uniform(テクスチャユニット)
        GLES20.glUniform1i(hUNI[5], u_Texture0)
        MyGLES20Func.checkGlError2("u_Texture0",this,model)

        // モデルを描画
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, model.datIdx.size, GLES20.GL_UNSIGNED_SHORT, model.bufIdx)

        // リソース解放
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,0)
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER,0)
    }
}
