package milu.kiriu2010.exdb1.opengl04.w043v

import android.opengl.GLES20
import milu.kiriu2010.gui.basic.MyGLES20Func
import milu.kiriu2010.gui.model.MgModelAbs
import milu.kiriu2010.gui.shader.es20.ES20MgShader
import milu.kiriu2010.gui.vbo.es20.ES20VBOAbs

// -------------------------------------
// シェーダ for 視差マッピング
// -------------------------------------
// https://wgld.org/d/webgl/w043.html
// -------------------------------------
class WV043Shader: ES20MgShader() {
    // 頂点シェーダ
    private val scv =
            """
            attribute vec3  a_Position;
            attribute vec3  a_Normal;
            attribute vec4  a_Color;
            attribute vec2  a_TextureCoord;
            uniform   mat4  u_matM;
            uniform   mat4  u_matMVP;
            uniform   mat4  u_matINV;
            uniform   vec3  u_vecLight;
            uniform   vec3  u_vecEye;
            varying   vec4  v_Color;
            varying   vec2  v_TextureCoord;
            varying   vec3  v_vecLight;
            varying   vec3  v_vecEye;

            void main() {
                vec3 pos      = (u_matM   * vec4(a_Position,0.0)).xyz;
                vec3 invEye   = (u_matINV * vec4(u_vecEye  ,0.0)).xyz;
                vec3 invLight = (u_matINV * vec4(u_vecLight,0.0)).xyz;
                vec3 eye      = invEye   - pos;
                vec3 light    = invLight - pos;
                // 法線ベクトル
                vec3 n = normalize(a_Normal);
                // 接線ベクトル
                // Y軸と法線ベクトルとの間で外積を取ることで算出する
                vec3 t = normalize(cross(a_Normal,vec3(0.0,1.0,0.0)));
                // 従法線ベクトル
                // 接線ベクトルと法線ベクトルとの間で外積をとることで算出する
                vec3 b = cross(n,t);
                // 視線ベクトルとライトベクトルを接空間上に変換する
                v_vecEye.x = dot(t,eye);
                v_vecEye.y = dot(b,eye);
                v_vecEye.z = dot(n,eye);
                normalize(v_vecEye);
                v_vecLight.x = dot(t,light);
                v_vecLight.y = dot(b,light);
                v_vecLight.z = dot(n,light);
                normalize(v_vecLight);
                v_Color        = a_Color;
                v_TextureCoord = a_TextureCoord;
                gl_Position    = u_matMVP * vec4(a_Position,1.0);
            }
            """.trimIndent()

    // フラグメントシェーダ
    private val scf =
            """
            precision mediump   float;

            // 法線マップのユニット番号
            uniform   sampler2D u_Texture0;
            // 高さマップのユニット番号
            uniform   sampler2D u_Texture1;
            uniform   float     u_Height;
            varying   vec4      v_Color;
            varying   vec2      v_TextureCoord;
            varying   vec3      v_vecLight;
            varying   vec3      v_vecEye;

            void main() {
                vec3  light     = normalize(v_vecLight);
                vec3  eye       = normalize(v_vecEye);
                // 高さマップのイメージから、高さに関する情報を抜き出している
                // この高さを使って法線マップへの参照点をずらす
                float hScale    = texture2D(u_Texture1, v_TextureCoord).r * u_Height;
                // 本来のテクスチャ座標から高さと視点を考慮した分の値を減算してずらす
                // こうして得られたテクスチャ座標を使ってバンプマッピング同様の処理を行うことで、
                // 高さと視線を考慮した視差マッピングを実現する
                vec2  hTexCoord = v_TextureCoord - hScale * eye.xy;
                vec3  halfLE    = normalize(light+eye);
                // 法線マップからRGB値を抜き出し、法線として扱う
                // 法線マップ上の色データは負の値がない(0～1)
                // 一方、法線は-1～1の範囲をとるので、"２倍して１引く"という処理になっている
                vec3  mNormal   = (texture2D(u_Texture0, hTexCoord) * 2.0 - 1.0).rgb;
                float diffuse   = clamp(dot(mNormal, light), 0.1, 1.0);
                float specular  = pow(clamp(dot(mNormal,halfLE) ,0.0,1.0), 100.0);
                vec4  destColor = v_Color * vec4(vec3(diffuse),1.0) + vec4(vec3(specular),1.0);
                gl_FragColor  = destColor;
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
        hUNI = IntArray(8)
        // uniform(モデル)
        hUNI[0] = GLES20.glGetUniformLocation(programHandle,"u_matM")
        MyGLES20Func.checkGlError("u_matM:glGetUniformLocation")

        // uniform(モデル×ビュー×プロジェクション)
        hUNI[1] = GLES20.glGetUniformLocation(programHandle,"u_matMVP")
        MyGLES20Func.checkGlError("u_matMVP:glGetUniformLocation")

        // uniform(逆行列)
        hUNI[2] = GLES20.glGetUniformLocation(programHandle,"u_matINV")
        MyGLES20Func.checkGlError("u_matINV:glGetUniformLocation")

        // uniform(光源位置)
        hUNI[3] = GLES20.glGetUniformLocation(programHandle,"u_vecLight")
        MyGLES20Func.checkGlError("u_vecLight:glGetUniformLocation")

        // uniform(視点座標)
        hUNI[4] = GLES20.glGetUniformLocation(programHandle,"u_vecEye")
        MyGLES20Func.checkGlError("u_vecEye:glGetUniformLocation")

        // uniform(テクスチャユニット0)
        hUNI[5] = GLES20.glGetUniformLocation(programHandle, "u_Texture0")
        MyGLES20Func.checkGlError("u_Texture0:glGetUniformLocation")

        // uniform(テクスチャユニット1)
        hUNI[6] = GLES20.glGetUniformLocation(programHandle, "u_Texture1")
        MyGLES20Func.checkGlError("u_Texture1:glGetUniformLocation")

        // uniform(高さ)
        hUNI[7] = GLES20.glGetUniformLocation(programHandle, "u_Height")
        MyGLES20Func.checkGlError("u_Height:glGetUniformLocation")

        return this
    }

    fun draw(model: MgModelAbs,
             bo: ES20VBOAbs,
             u_matM: FloatArray,
             u_matMVP: FloatArray,
             u_matI: FloatArray,
             u_vecLight: FloatArray,
             u_vecEye: FloatArray,
             u_Texture0: Int,
             u_Texture1: Int,
             u_Height: Float) {

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

        // uniform(逆行列)
        GLES20.glUniformMatrix4fv(hUNI[2],1,false,u_matI,0)
        MyGLES20Func.checkGlError2("u_matINV",this,model)

        // uniform(光源位置)
        GLES20.glUniform3fv(hUNI[3],1,u_vecLight,0)
        MyGLES20Func.checkGlError2("u_vecLight",this,model)

        // uniform(視点座標)
        GLES20.glUniform3fv(hUNI[4],1,u_vecEye,0)
        MyGLES20Func.checkGlError2("u_vecEye",this,model)

        // uniform(テクスチャユニット)
        GLES20.glUniform1i(hUNI[5], u_Texture0)
        MyGLES20Func.checkGlError2("u_Texture0",this,model)

        // uniform(テクスチャユニット1)
        GLES20.glUniform1i(hUNI[6], u_Texture1)
        MyGLES20Func.checkGlError2("u_Texture1",this,model)

        // uniform(高さ)
        GLES20.glUniform1f(hUNI[7], u_Height)
        MyGLES20Func.checkGlError2("u_Height",this,model)

        // モデルを描画
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, model.datIdx.size, GLES20.GL_UNSIGNED_SHORT, model.bufIdx)

        // リソース解放
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,0)
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER,0)
    }
}
