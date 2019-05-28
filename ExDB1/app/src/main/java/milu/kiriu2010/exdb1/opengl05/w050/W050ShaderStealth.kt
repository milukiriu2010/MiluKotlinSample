package milu.kiriu2010.exdb1.opengl05.w050

import android.opengl.GLES20
import milu.kiriu2010.gui.basic.MyGLES20Func
import milu.kiriu2010.gui.model.MgModelAbs
import milu.kiriu2010.gui.shader.MgShader

// ---------------------------------------------
// シェーダ(光学迷彩)
// ---------------------------------------------
// https://wgld.org/d/webgl/w050.html
// ---------------------------------------------
class W050ShaderStealth: MgShader() {
    // 頂点シェーダ
    private val scv =
            """
            attribute vec3  a_Position;
            attribute vec3  a_Normal;
            attribute vec4  a_Color;
            uniform   mat4  u_matM;
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
                // 係数と法線を掛け合わせた数値を加算することでテクスチャ座標をずらしている
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

    override fun loadShader(): MgShader {
        // 頂点シェーダを生成
        val svhandle = MyGLES20Func.loadShader(GLES20.GL_VERTEX_SHADER, scv)
        // フラグメントシェーダを生成
        val sfhandle = MyGLES20Func.loadShader(GLES20.GL_FRAGMENT_SHADER, scf)

        // プログラムオブジェクトの生成とリンク
        programHandle = MyGLES20Func.createProgram(svhandle,sfhandle, arrayOf("a_Position","a_Normal","a_Color") )
        return this
    }


    // 光学迷彩
    fun draw(model: MgModelAbs,
             matM: FloatArray,
             matVPT: FloatArray,
             matMVP: FloatArray,
             u_coefficient: Float,
             u_Texture0: Int) {
        GLES20.glUseProgram(programHandle)
        MyGLES20Func.checkGlError2("UserProgram",this,model)

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

        // uniform(モデル)
        GLES20.glGetUniformLocation(programHandle,"u_matM").also {
            GLES20.glUniformMatrix4fv(it,1,false,matM,0)
        }
        MyGLES20Func.checkGlError2("u_matM",this,model)

        // uniform(ビュー×プロジェクション×テクスチャ座標変換行列)
        GLES20.glGetUniformLocation(programHandle,"u_matVPT").also {
            GLES20.glUniformMatrix4fv(it,1,false,matVPT,0)
        }
        MyGLES20Func.checkGlError2("u_matVPT",this,model)

        // uniform(モデル×ビュー×プロジェクション)
        GLES20.glGetUniformLocation(programHandle,"u_matMVP").also {
            GLES20.glUniformMatrix4fv(it,1,false,matMVP,0)
        }
        MyGLES20Func.checkGlError2("u_matMVP",this,model)

        // uniform(光学迷彩にかける補正係数)
        GLES20.glGetUniformLocation(programHandle,"u_coefficient").also {
            GLES20.glUniform1f(it,u_coefficient)
        }
        MyGLES20Func.checkGlError2("u_coefficient",this,model)

        // uniform(テクスチャユニット)
        GLES20.glGetUniformLocation(programHandle,"u_Texture0").also {
            GLES20.glUniform1i(it,u_Texture0)
        }
        MyGLES20Func.checkGlError2("u_Texture0",this,model)

        // モデルを描画
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, model.datIdx.size, GLES20.GL_UNSIGNED_SHORT, model.bufIdx)
    }
}
