package milu.kiriu2010.exdb1.opengl04.w045v

import android.opengl.GLES20
import milu.kiriu2010.gui.basic.MyGLES20Func
import milu.kiriu2010.gui.model.MgModelAbs
import milu.kiriu2010.gui.shader.es20.ES20MgShader
import milu.kiriu2010.gui.vbo.es20.ES20VBOAbs

// -------------------------------------------------
// シェーダ for キューブ環境バンプマッピング
// -------------------------------------------------
// バンプマッピングでは法線マップを参照するために、
// 頂点属性としてテクスチャ座標を定義する必要がある
// -------------------------------------------------
// https://wgld.org/d/webgl/w045.html
// -------------------------------------------------
class WV045Shader: ES20MgShader() {
    // 頂点シェーダ
    private val scv =
            """
            attribute vec3  a_Position;
            attribute vec3  a_Normal;
            attribute vec4  a_Color;
            attribute vec2  a_TextureCoord;
            uniform   mat4  u_matM;
            uniform   mat4  u_matMVP;
            varying   vec3  v_Position;
            varying   vec4  v_Color;
            varying   vec2  v_TextureCoord;
            varying   vec3  v_tNormal;
            varying   vec3  v_tTangent;

            void main() {
                v_Position     = (u_matM * vec4(a_Position,1.0)).xyz;
                v_Color        = a_Color;
                v_TextureCoord = a_TextureCoord;
                // 法線ベクトル
                v_tNormal      = (u_matM * vec4(a_Normal  ,0.0)).xyz;
                // 接線ベクトル
                // = 法線ベクトルとY軸の外積
                v_tTangent     = cross(v_tNormal,vec3(0.0,1.0,0.0));
                gl_Position    = u_matMVP * vec4(a_Position,1.0);
            }
            """.trimIndent()

    // フラグメントシェーダ
    private val scf =
            """
            precision mediump     float;

            uniform   vec3        u_vecEye;
            uniform   sampler2D   u_normalMap;
            uniform   samplerCube u_CubeTexture;
            uniform   int         u_Reflection;
            varying   vec3        v_Position;
            varying   vec4        v_Color;
            varying   vec2        v_TextureCoord;
            // 法線ベクトル
            varying   vec3        v_tNormal;
            // 接線ベクトル
            varying   vec3        v_tTangent;

            void main() {
                // 法線ベクトルと接線ベクトルを使って従法線ベクトルを算出
                vec3  tBinormal = cross(v_tNormal, v_tTangent);
                // 法線マップから抜き出したバンプマッピング用の法線情報を
                // 視線空間上へと変換するために3x3の行列を生成
                // この行列を法線マップから抜き出した法線ベクトルと掛け合わせることで、
                // 接空間上にある法線ベクトルを視線空間へ変換する。
                mat3  mView     = mat3(v_tTangent, tBinormal, v_tNormal);
                // 法線マップから法線ベクトルを抜き出す＆接空間上の法線ベクトルを視線空間へ変換する
                vec3  mNormal   = mView * (texture2D(u_normalMap,v_TextureCoord)*2.0-1.0).rgb;
                vec3  ref;
                if (bool(u_Reflection)) {
                    ref = reflect(v_Position - u_vecEye, mNormal);
                }
                else {
                    ref = v_tNormal;
                }
                // キューブマップテクスチャからフラグメントの情報を抜き出す
                // u_Reflection=1 ⇒ 反射に対応する色
                // u_Reflection=0 ⇒ 背景の色
                vec4  envColor  = textureCube(u_CubeTexture, ref);
                vec4  destColor = v_Color * envColor;
                gl_FragColor    = destColor;
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
        hATTR = IntArray(4)
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

        // 属性(テクスチャ座標)
        hATTR[3] = GLES20.glGetAttribLocation(programHandle, "a_TextureCoord").also {
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
        hUNI = IntArray(6)
        // uniform(モデル)
        hUNI[0] = GLES20.glGetUniformLocation(programHandle,"u_matM")
        MyGLES20Func.checkGlError("u_matM:glGetUniformLocation")

        // uniform(モデル×ビュー×プロジェクション)
        hUNI[1] = GLES20.glGetUniformLocation(programHandle,"u_matMVP")
        MyGLES20Func.checkGlError("u_matMVP:glGetUniformLocation")

        // uniform(視点座標)
        hUNI[2] = GLES20.glGetUniformLocation(programHandle,"u_vecEye")
        MyGLES20Func.checkGlError("u_vecEye:glGetUniformLocation")

        // uniform(法線マップテクスチャ)
        hUNI[3] = GLES20.glGetUniformLocation(programHandle, "u_normalMap")

        // uniform(キューブテクスチャユニット)
        hUNI[4] = GLES20.glGetUniformLocation(programHandle, "u_CubeTexture")
        MyGLES20Func.checkGlError("u_CubeTexture:glGetUniformLocation")

        // uniform(反射するかどうか)
        hUNI[5] = GLES20.glGetUniformLocation(programHandle, "u_Reflection")
        MyGLES20Func.checkGlError("u_Reflection:glGetUniformLocation")

        return this
    }

    fun draw(model: MgModelAbs,
             bo: ES20VBOAbs,
             u_matM: FloatArray,
             u_matMVP: FloatArray,
             u_vecEye: FloatArray,
             u_normalMap: Int,
             u_CubeTexture: Int,
             u_Reflection: Int) {

        GLES20.glUseProgram(programHandle)
        MyGLES20Func.checkGlError2("UseProgram",this,model)

        // attribute(位置)
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

        // attribute(テクスチャ座標)
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,bo.hVBO[3])
        GLES20.glVertexAttribPointer(hATTR[3],2,GLES20.GL_FLOAT,false,0,0)
        MyGLES20Func.checkGlError2("a_TextureCoord",this,model)

        // uniform(モデル)
        GLES20.glUniformMatrix4fv(hUNI[0],1,false,u_matM,0)
        MyGLES20Func.checkGlError2("u_matM",this,model)

        // uniform(モデル×ビュー×プロジェクション)
        GLES20.glUniformMatrix4fv(hUNI[1],1,false,u_matMVP,0)
        MyGLES20Func.checkGlError2("u_matMVP",this,model)

        // uniform(視点座標)
        GLES20.glUniform3fv(hUNI[2],1,u_vecEye,0)
        MyGLES20Func.checkGlError2("u_vecEye",this,model)

        if ( u_normalMap != -1 ) {
            // uniform(法線マップテクスチャ)
            GLES20.glUniform1i(hUNI[3], u_normalMap)
            MyGLES20Func.checkGlError2("u_normalMap",this,model)
        }

        if ( u_CubeTexture != -1 ) {
            // uniform(キューブテクスチャ)
            GLES20.glUniform1i(hUNI[4], u_CubeTexture)
            MyGLES20Func.checkGlError2("u_CubeTexture",this,model)
        }

        // uniform(反射するかどうか)
        GLES20.glUniform1i(hUNI[5],u_Reflection)
        MyGLES20Func.checkGlError2("u_Reflection",this,model)

        // モデルを描画
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, model.datIdx.size, GLES20.GL_UNSIGNED_SHORT, model.bufIdx)

        // リソース解放
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,0)
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER,0)
    }
}
