package milu.kiriu2010.exdb1.opengl04.w043

import android.opengl.GLES20
import milu.kiriu2010.gui.basic.MyGLES20Func
import milu.kiriu2010.gui.model.MgModelAbs
import milu.kiriu2010.gui.shader.MgShader

// -------------------------------------
// 視差マッピング
// -------------------------------------
// https://wgld.org/d/webgl/w043.html
// -------------------------------------
class W043Shader: MgShader() {
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
                vec3 invEye   = (u_matINV * vec4(u_vecEye,0.0)).xyz;
                vec3 invLight = (u_matINV * vec4(u_vecLight,0.0)).xyz;
                vec3 eye      = invEye - pos;
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

    override fun loadShader(): MgShader {
        // 頂点シェーダを生成
        val svhandle = MyGLES20Func.loadShader(GLES20.GL_VERTEX_SHADER, scv)
        // フラグメントシェーダを生成
        val sfhandle = MyGLES20Func.loadShader(GLES20.GL_FRAGMENT_SHADER, scf)

        // プログラムオブジェクトの生成とリンク
        programHandle = MyGLES20Func.createProgram(svhandle,sfhandle, arrayOf("a_Position","a_Normal","a_Color","a_TextureCoord") )
        return this
    }


    fun draw(model: MgModelAbs,
             matM: FloatArray,
             matMVP: FloatArray,
             matI: FloatArray,
             u_vecLight: FloatArray,
             u_vecEye: FloatArray,
             u_Texture0: Int,
             u_Texture1: Int,
             u_Height: Float) {

        GLES20.glUseProgram(programHandle)
        MyGLES20Func.checkGlError2("UseProgram",this,model)

        // attribute(頂点)
        model.bufPos.position(0)
        GLES20.glGetAttribLocation(programHandle,"a_Position").also {
            GLES20.glVertexAttribPointer(it,3,GLES20.GL_FLOAT,false, 3*4, model.bufPos)
            GLES20.glEnableVertexAttribArray(it)
        }
        MyGLES20Func.checkGlError2("a_Position",this,model)

        // attribute(法線)
        model.bufNor.position(0)
        GLES20.glGetAttribLocation(programHandle,"a_Normal").also {
            GLES20.glVertexAttribPointer(it,3,GLES20.GL_FLOAT,false, 3*4, model.bufNor)
            GLES20.glEnableVertexAttribArray(it)
        }
        MyGLES20Func.checkGlError2("a_Normal",this,model)

        // attribute(色)
        model.bufCol.position(0)
        GLES20.glGetAttribLocation(programHandle,"a_Color").also {
            GLES20.glVertexAttribPointer(it,4,GLES20.GL_FLOAT,false, 4*4, model.bufCol)
            GLES20.glEnableVertexAttribArray(it)
        }
        MyGLES20Func.checkGlError2("a_Color",this,model)

        // attribute(テクスチャ座標)
        model.bufTxc.position(0)
        GLES20.glGetAttribLocation(programHandle,"a_TextureCoord").also {
            GLES20.glVertexAttribPointer(it,2,GLES20.GL_FLOAT,false, 2*4, model.bufTxc)
            GLES20.glEnableVertexAttribArray(it)
        }
        MyGLES20Func.checkGlError2("a_TextureCoord",this,model)

        // uniform(モデル)
        GLES20.glGetUniformLocation(programHandle,"u_matM").also {
            GLES20.glUniformMatrix4fv(it,1,false,matM,0)
        }
        MyGLES20Func.checkGlError2("u_matM",this,model)

        // uniform(モデル×ビュー×プロジェクション)
        GLES20.glGetUniformLocation(programHandle,"u_matMVP").also {
            GLES20.glUniformMatrix4fv(it,1,false,matMVP,0)
        }
        MyGLES20Func.checkGlError2("u_matMVP",this,model)

        // uniform(逆行列)
        GLES20.glGetUniformLocation(programHandle,"u_matINV").also {
            GLES20.glUniformMatrix4fv(it,1,false,matI,0)
        }
        MyGLES20Func.checkGlError2("u_matINV",this,model)

        // uniform(光源位置)
        GLES20.glGetUniformLocation(programHandle,"u_vecLight").also {
            GLES20.glUniform3fv(it,1,u_vecLight,0)
        }
        MyGLES20Func.checkGlError2("u_vecLight",this,model)

        // uniform(視点座標)
        GLES20.glGetUniformLocation(programHandle,"u_vecEye").also {
            GLES20.glUniform3fv(it,1,u_vecEye,0)
        }
        MyGLES20Func.checkGlError2("u_vecEye",this,model)

        // uniform(テクスチャユニット0)
        GLES20.glGetUniformLocation(programHandle, "u_Texture0").also {
            GLES20.glUniform1i(it, u_Texture0)
        }
        MyGLES20Func.checkGlError2("u_Texture0",this,model)

        // uniform(テクスチャユニット1)
        GLES20.glGetUniformLocation(programHandle, "u_Texture1").also {
            GLES20.glUniform1i(it, u_Texture1)
        }
        MyGLES20Func.checkGlError2("u_Texture1",this,model)

        // uniform(高さ)
        GLES20.glGetUniformLocation(programHandle, "u_Height").also {
            GLES20.glUniform1f(it, u_Height)
        }
        MyGLES20Func.checkGlError2("u_Height",this,model)

        // モデルを描画
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, model.datIdx.size, GLES20.GL_UNSIGNED_SHORT, model.bufIdx)
    }

}
