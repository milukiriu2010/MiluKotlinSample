package milu.kiriu2010.exdb1.opengl06.w068

import android.opengl.GLES20
import milu.kiriu2010.gui.model.MgModelAbs
import milu.kiriu2010.gui.basic.MyGLES20Func
import milu.kiriu2010.gui.shader.es20.ES20MgShader

// ------------------------------------
// シェーダ(ズームブラー)
// ------------------------------------
// https://wgld.org/d/webgl/w068.html
// ------------------------------------
class W068ShaderZoomBlur: ES20MgShader() {
    // 頂点シェーダ
    private val scv =
            """
            attribute vec3  a_Position;
            attribute vec2  a_TexCoord;
            uniform   mat4  u_matMVP;
            varying   vec2  v_TexCoord;

            void main() {
                v_TexCoord  = a_TexCoord;
                gl_Position = u_matMVP   * vec4(a_Position, 1.0);
            }
            """.trimIndent()

    // フラグメントシェーダ
    private val scf =
            """
            precision mediump   float;

            uniform  sampler2D u_Texture;
            uniform  float     u_strength;
            uniform  float     u_renderWH;
            uniform  vec2      u_center;
            varying  vec2      v_TexCoord;

            float rnd(vec3 scale, float seed){
                return fract(sin(dot(gl_FragCoord.stp + seed, scale)) * 43758.5453 + seed);
            }

            // スクリーン上を区切るボックスのサイズを8x8に設定している
            void main() {
                // 512x512の描画領域を扱う場合
                //float tFrag = 1.0/512.0;
                //float nFrag = 1.0/30.0;
                //vec2  centerOffset = vec2(256.0,256.0);

                float tFrag = 1.0/u_renderWH;
                // 色を出力する際、正規化するための係数
                float nFrag = 1.0/30.0;

                vec3  destColor = vec3(0.0);
                float random = rnd(vec3(12.9898, 78.233, 151.7182), 0.0);
                vec2  fc = vec2(gl_FragCoord.s, u_renderWH - gl_FragCoord.t);
                vec2  fcc = fc - u_center;
                float totalWeight = 0.0;

                for(float i = 0.0; i <= 30.0; i++){
                    float percent = (i + random) * nFrag;
                    float weight = percent - percent * percent;
                    vec2  t = fc - fcc * percent * u_strength * nFrag;
                    destColor += texture2D(u_Texture, t * tFrag).rgb * weight;
                    totalWeight += weight;
                }
                gl_FragColor = vec4(destColor / totalWeight, 1.0);
            }
            """.trimIndent()


    override fun loadShader(): ES20MgShader {
        // 頂点シェーダを生成
        svhandle = MyGLES20Func.loadShader(GLES20.GL_VERTEX_SHADER, scv)
        // フラグメントシェーダを生成
        sfhandle = MyGLES20Func.loadShader(GLES20.GL_FRAGMENT_SHADER, scf)

        // プログラムオブジェクトの生成とリンク
        programHandle = MyGLES20Func.createProgram(svhandle,sfhandle, arrayOf("a_Position","a_TexCoord") )
        return this
    }

    fun draw(model: MgModelAbs,
             matMVP: FloatArray,
             u_Texture: Int,
             u_strength: Float,
             u_renderWH: Float,
             u_center: FloatArray) {

        GLES20.glUseProgram(programHandle)
        MyGLES20Func.checkGlError2("UseProgram",this,model)

        // attribute(頂点)
        model.bufPos.position(0)
        val hPosition = GLES20.glGetAttribLocation(programHandle,"a_Position").also {
            if ( it != -1 ) {
                GLES20.glVertexAttribPointer(it,3,GLES20.GL_FLOAT,false, 3*4, model.bufPos)
                GLES20.glEnableVertexAttribArray(it)
            }
        }
        MyGLES20Func.checkGlError2("a_Position",this,model)

        // attribute(テクスチャ座標)
        model.bufTxc.position(0)
        val hTexCoord = GLES20.glGetAttribLocation(programHandle,"a_TexCoord").also {
            if ( it != -1 ) {
                GLES20.glVertexAttribPointer(it,2,GLES20.GL_FLOAT,false, 2*4, model.bufTxc)
                GLES20.glEnableVertexAttribArray(it)
            }
        }
        MyGLES20Func.checkGlError2("a_TexCoord",this,model)

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

        // uniform()
        GLES20.glGetUniformLocation(programHandle, "u_strength").also {
            GLES20.glUniform1f(it, u_strength)
        }
        MyGLES20Func.checkGlError2("u_strength",this,model)

        // uniform(画像の大きさ)
        GLES20.glGetUniformLocation(programHandle, "u_renderWH").also {
            GLES20.glUniform1f(it, u_renderWH)
        }
        MyGLES20Func.checkGlError2("u_renderWH",this,model)

        // uniform(マウス位置)
        GLES20.glGetUniformLocation(programHandle, "u_center").also {
            GLES20.glUniform2fv(it,1, u_center,0)
        }
        MyGLES20Func.checkGlError2("u_center",this,model)

        // モデルを描画
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, model.datIdx.size, GLES20.GL_UNSIGNED_SHORT, model.bufIdx)

        // リソース解放
        if ( hPosition != -1 ) {
            GLES20.glDisableVertexAttribArray(hPosition)
        }
        if ( hTexCoord != -1 ) {
            GLES20.glDisableVertexAttribArray(hTexCoord)
        }
    }
}
