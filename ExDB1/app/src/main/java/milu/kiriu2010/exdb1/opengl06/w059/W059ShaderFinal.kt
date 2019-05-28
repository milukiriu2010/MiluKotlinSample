package milu.kiriu2010.exdb1.opengl06.w059

import android.opengl.GLES20
import milu.kiriu2010.gui.basic.MyGLES20Func
import milu.kiriu2010.gui.model.MgModelAbs
import milu.kiriu2010.gui.shader.es20.ES20MgShader

// -----------------------------------------------------------------------
// シェーダ(最終結果)
//   レンダリングされた全てのシーンを合成する
// -----------------------------------------------------------------------
// https://wgld.org/d/webgl/w059.html
// -----------------------------------------------------------------------
class W059ShaderFinal: ES20MgShader() {
    // 頂点シェーダ
    private val scv =
            """
            attribute vec3  a_Position;
            attribute vec2  a_TextureCoord;
            uniform   mat4  u_matMVP;
            varying   vec2  v_TextureCoord;

            void main() {
                v_TextureCoord = a_TextureCoord;
                gl_Position    = u_matMVP   * vec4(a_Position, 1.0);
            }
            """.trimIndent()

    // フラグメントシェーダ
    private val scf =
            """
            precision mediump   float;

            // 深度マップテクスチャ
            uniform   sampler2D   u_TextureDepth;
            // ぼけていないシーンのテクスチャ
            uniform   sampler2D   u_TextureScene;
            // 小さくぼけたシーンのテクスチャ
            uniform   sampler2D   u_TextureBlur1;
            // 大きくぼけたシーンのテクスチャ
            uniform   sampler2D   u_TextureBlur2;
            uniform   int         u_result;
            varying   vec2        v_TextureCoord;

            // フレームバッファに描きこまれた深度値を本来の値に変換する
            float restDepth(vec4 RGBA) {
                const float rMask = 1.0;
                const float gMask = 1.0/255.0;
                const float bMask = 1.0/(255.0*255.0);
                const float aMask = 1.0/(255.0*255.0*255.0);
                float depth = dot(RGBA, vec4(rMask, gMask, bMask, aMask));
                return depth;
            }

            // フレームバッファに描かれた深度値を読み出し、
            // ライト視点で座標変換した頂点の深度値と比較する
            void main() {
                // 深度値を逆変換して、元に戻している
                float d = restDepth(texture2D(u_TextureDepth, vec2(v_TextureCoord.s,1.0-v_TextureCoord.t)));
                // ぼやけたシーンをどの程度合成するか決めている
                float coef = 1.0 - d;
                float coefBlur1 = coef * d;
                float coefBlur2 = coef * coef;
                vec4 colorScene = texture2D(u_TextureScene, vec2(v_TextureCoord.s,1.0-v_TextureCoord.t));
                vec4 colorBlur1 = texture2D(u_TextureBlur1, v_TextureCoord);
                vec4 colorBlur2 = texture2D(u_TextureBlur2, v_TextureCoord);
                vec4 destColor  = colorScene*d + colorBlur1*coefBlur1 + colorBlur2*coefBlur2;

                // 合成
                if ( u_result == 0 ) {
                    gl_FragColor = destColor;
                }
                // モノクロ
                else if ( u_result == 1 ) {
                    gl_FragColor = vec4(vec3(d),1.0);
                }
                // ぼやけていないシーン
                else if ( u_result == 2 ) {
                    gl_FragColor = colorScene;
                }
                // 小さくぼやけたシーン
                else if ( u_result == 3 ) {
                    gl_FragColor = colorBlur1;
                }
                // 大きくぼやけたシーン
                else {
                    gl_FragColor = colorBlur2;
                }
            }
            """.trimIndent()

    override fun loadShader(): ES20MgShader {
        // 頂点シェーダを生成
        val svhandle = MyGLES20Func.loadShader(GLES20.GL_VERTEX_SHADER, scv)
        // フラグメントシェーダを生成
        val sfhandle = MyGLES20Func.loadShader(GLES20.GL_FRAGMENT_SHADER, scf)

        // プログラムオブジェクトの生成とリンク
        programHandle = MyGLES20Func.createProgram(svhandle,sfhandle, arrayOf("a_Position","a_TextureCoord") )
        return this
    }


    fun draw(model: MgModelAbs,
             matMVP: FloatArray,
             u_TextureDepth: Int,
             u_TextureScene: Int,
             u_TextureBlur1: Int,
             u_TextureBlur2: Int,
             u_result: Int) {
        GLES20.glUseProgram(programHandle)
        MyGLES20Func.checkGlError2("UseProgram",this,model)

        // attribute(頂点)
        model.bufPos.position(0)
        GLES20.glGetAttribLocation(programHandle,"a_Position").also {
            GLES20.glVertexAttribPointer(it,3,GLES20.GL_FLOAT,false, 3*4, model.bufPos)
            GLES20.glEnableVertexAttribArray(it)
        }
        MyGLES20Func.checkGlError2("a_Position",this,model)

        // attribute(テクスチャ座標)
        model.bufTxc.position(0)
        GLES20.glGetAttribLocation(programHandle,"a_TextureCoord").also {
            GLES20.glVertexAttribPointer(it,2,GLES20.GL_FLOAT,false, 2*4, model.bufTxc)
            GLES20.glEnableVertexAttribArray(it)
        }
        MyGLES20Func.checkGlError2("a_TextureCoord",this,model)

        // uniform(モデル×ビュー×プロジェクション)
        GLES20.glGetUniformLocation(programHandle,"u_matMVP").also {
            GLES20.glUniformMatrix4fv(it,1,false,matMVP,0)
        }
        MyGLES20Func.checkGlError2("u_matMVP",this,model)

        // uniform(テクスチャユニット)
        GLES20.glGetUniformLocation(programHandle, "u_TextureDepth").also {
            GLES20.glUniform1i(it, u_TextureDepth)
        }
        MyGLES20Func.checkGlError2("u_TextureDepth",this,model)

        // uniform(テクスチャユニット)
        GLES20.glGetUniformLocation(programHandle, "u_TextureScene").also {
            GLES20.glUniform1i(it, u_TextureScene)
        }
        MyGLES20Func.checkGlError2("u_TextureScene",this,model)

        // uniform(テクスチャユニット)
        GLES20.glGetUniformLocation(programHandle, "u_TextureBlur1").also {
            GLES20.glUniform1i(it, u_TextureBlur1)
        }
        MyGLES20Func.checkGlError2("u_TextureBlur1",this,model)

        // uniform(テクスチャユニット)
        GLES20.glGetUniformLocation(programHandle, "u_TextureBlur2").also {
            GLES20.glUniform1i(it, u_TextureBlur2)
        }
        MyGLES20Func.checkGlError2("u_TextureBlur2",this,model)

        // uniform(結果)
        GLES20.glGetUniformLocation(programHandle, "u_result").also {
            GLES20.glUniform1i(it, u_result)
        }
        MyGLES20Func.checkGlError2("u_result",this,model)

        // モデルを描画
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, model.datIdx.size, GLES20.GL_UNSIGNED_SHORT, model.bufIdx)
    }
}
