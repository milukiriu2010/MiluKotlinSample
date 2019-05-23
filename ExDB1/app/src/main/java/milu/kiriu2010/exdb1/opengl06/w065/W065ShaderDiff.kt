package milu.kiriu2010.exdb1.opengl06.w065

import android.opengl.GLES20
import milu.kiriu2010.gui.basic.MyGLFunc
import milu.kiriu2010.gui.model.MgModelAbs
import milu.kiriu2010.gui.shader.MgShader

// ----------------------------------------
// シェーダ(深度値の差分レンダリング)
// ----------------------------------------
// https://wgld.org/d/webgl/w065.html
// ----------------------------------------
class W065ShaderDiff: MgShader() {
    // 頂点シェーダ
    private val scv =
            """
            attribute vec3  a_Position;
            uniform   mat4  u_matM;
            uniform   mat4  u_matMVP;
            uniform   mat4  u_matO;
            uniform   vec3  u_vecEye;
            varying   float v_Depth;
            varying   vec4  v_TextureCoord;

            const float near = 0.1;
            const float far  = 15.0;
            const float linearDepth = 1.0/(far-near);

            void main() {
                vec3 pos = (u_matM*vec4(a_Position,1.0)).xyz;
                v_Depth  = length(u_vecEye-pos) * linearDepth;
                v_TextureCoord = u_matO * vec4(pos,1.0);
                gl_Position = u_matMVP * vec4(a_Position,1.0);
            }
            """.trimIndent()

    // フラグメントシェーダ
    private val scf =
            """
            precision mediump   float;

            uniform   sampler2D u_TextureBackface;
            varying   float     v_Depth;
            varying   vec4      v_TextureCoord;

            void main() {
                float bDepth     = 1.0 - texture2DProj(u_TextureBackface, v_TextureCoord).r;
                float difference = 1.0 - clamp(bDepth-v_Depth,0.0, 1.0);
                gl_FragColor     = vec4(vec3(difference), 1.0);
            }
            """.trimIndent()

    override fun loadShader(): MgShader {
        // 頂点シェーダを生成
        svhandle = MyGLFunc.loadShader(GLES20.GL_VERTEX_SHADER, scv)
        // フラグメントシェーダを生成
        sfhandle = MyGLFunc.loadShader(GLES20.GL_FRAGMENT_SHADER, scf)

        // プログラムオブジェクトの生成とリンク
        programHandle = MyGLFunc.createProgram(svhandle,sfhandle, arrayOf("a_Position") )
        return this
    }


    fun draw(model: MgModelAbs,
             matM: FloatArray,
             matMVP: FloatArray,
             matO: FloatArray,
             u_vecEye: FloatArray,
             u_TextureBackface: Int) {
        GLES20.glUseProgram(programHandle)
        MyGLFunc.checkGlError2("UseProgram", this,model)

        // attribute(頂点)
        model.bufPos.position(0)
        GLES20.glGetAttribLocation(programHandle,"a_Position").also {
            GLES20.glVertexAttribPointer(it,3,GLES20.GL_FLOAT,false, 3*4, model.bufPos)
            GLES20.glEnableVertexAttribArray(it)
        }
        MyGLFunc.checkGlError2("a_Position",this,model)

        // uniform(モデル座標変換行列)
        GLES20.glGetUniformLocation(programHandle,"u_matM").also {
            GLES20.glUniformMatrix4fv(it,1,false,matM,0)
        }
        MyGLFunc.checkGlError2("u_matM",this,model)

        // uniform(モデル×ビュー×プロジェクション)
        GLES20.glGetUniformLocation(programHandle,"u_matMVP").also {
            GLES20.glUniformMatrix4fv(it,1,false,matMVP,0)
        }
        MyGLFunc.checkGlError2("u_matMVP",this,model)

        // uniform(正射影座標行列)
        GLES20.glGetUniformLocation(programHandle,"u_matO").also {
            GLES20.glUniformMatrix4fv(it,1,false,matO,0)
        }
        MyGLFunc.checkGlError2("u_matO",this,model)

        // uniform(視点座標)
        GLES20.glGetUniformLocation(programHandle,"u_vecEye").also {
            GLES20.glUniform3fv(it,1,u_vecEye,0)
        }
        MyGLFunc.checkGlError2("u_vecEye",this,model)

        // uniform(テクスチャユニット)
        GLES20.glGetUniformLocation(programHandle, "u_TextureBackface").also {
            GLES20.glUniform1i(it, u_TextureBackface)
        }
        MyGLFunc.checkGlError2("u_TextureBackface",this,model)

        // モデルを描画
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, model.datIdx.size, GLES20.GL_UNSIGNED_SHORT, model.bufIdx)
    }
}
