package milu.kiriu2010.exdb1.opengl06.w067

import android.opengl.GLES20
import milu.kiriu2010.gui.model.MgModelAbs
import milu.kiriu2010.gui.basic.MyGLES20Func
import milu.kiriu2010.gui.shader.MgShader

// -----------------------------------
// シェーダ(ズームブラー)
// -----------------------------------
// https://wgld.org/d/webgl/w067.html
// -----------------------------------
class W067ShaderZoomBlur: MgShader() {
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
                vec2  centerOffset = vec2(u_renderWH/2.0,u_renderWH/2.0);

                vec3  destColor = vec3(0.0);
                float random = rnd(vec3(12.9898, 78.233, 151.7182), 0.0);
                vec2  fc = vec2(gl_FragCoord.s, u_renderWH - gl_FragCoord.t);
                vec2  fcc = fc - centerOffset;
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


    override fun loadShader(): MgShader {
        // 頂点シェーダを生成
        val svhandle = MyGLES20Func.loadShader(GLES20.GL_VERTEX_SHADER, scv)
        // フラグメントシェーダを生成
        val sfhandle = MyGLES20Func.loadShader(GLES20.GL_FRAGMENT_SHADER, scf)

        // プログラムオブジェクトの生成とリンク
        programHandle = MyGLES20Func.createProgram(svhandle,sfhandle, arrayOf("a_Position","a_TexCoord") )
        return this
    }

    fun draw(model: MgModelAbs,
             matMVP: FloatArray,
             u_Texture: Int,
             u_strength: Float,
             u_renderWH: Float) {

        GLES20.glUseProgram(programHandle)

        // attribute(頂点)
        model.bufPos.position(0)
        GLES20.glGetAttribLocation(programHandle,"a_Position").also {
            GLES20.glVertexAttribPointer(it,3,GLES20.GL_FLOAT,false, 3*4, model.bufPos)
            GLES20.glEnableVertexAttribArray(it)
        }
        MyGLES20Func.checkGlError("a_Position:${model.javaClass.simpleName}")

        // attribute(テクスチャ座標)
        model.bufTxc.position(0)
        GLES20.glGetAttribLocation(programHandle,"a_TexCoord").also {
            GLES20.glVertexAttribPointer(it,2,GLES20.GL_FLOAT,false, 2*4, model.bufTxc)
            GLES20.glEnableVertexAttribArray(it)
        }
        MyGLES20Func.checkGlError("a_TexCoord:${model.javaClass.simpleName}")

        // uniform(モデル×ビュー×プロジェクション)
        GLES20.glGetUniformLocation(programHandle,"u_matMVP").also {
            GLES20.glUniformMatrix4fv(it,1,false,matMVP,0)
        }
        MyGLES20Func.checkGlError("u_matMVP:${model.javaClass.simpleName}")

        // uniform(テクスチャ)
        GLES20.glGetUniformLocation(programHandle, "u_Texture").also {
            GLES20.glUniform1i(it, u_Texture)
        }
        MyGLES20Func.checkGlError("u_Texture:${model.javaClass.simpleName}")

        // uniform()
        GLES20.glGetUniformLocation(programHandle, "u_strength").also {
            GLES20.glUniform1f(it, u_strength)
        }
        MyGLES20Func.checkGlError("u_strength:${model.javaClass.simpleName}")

        // uniform(画像の大きさ)
        GLES20.glGetUniformLocation(programHandle, "u_renderWH").also {
            GLES20.glUniform1f(it, u_renderWH)
        }
        MyGLES20Func.checkGlError("u_renderWH:${model.javaClass.simpleName}")

        // モデルを描画
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, model.datIdx.size, GLES20.GL_UNSIGNED_SHORT, model.bufIdx)
    }
}
