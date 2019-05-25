package milu.kiriu2010.exdb1.opengl07.w077

import android.opengl.GLES20
import milu.kiriu2010.gui.model.MgModelAbs
import milu.kiriu2010.gui.basic.MyGLFunc
import milu.kiriu2010.gui.shader.MgShader

// --------------------------------------
// シェーダ(ラインシェード)
// --------------------------------------
// https://wgld.org/d/webgl/w077.html
// --------------------------------------
class W077Shader: MgShader() {
    // 頂点シェーダ
    private val scv =
            """
            attribute vec3  a_Position;
            attribute vec3  a_Normal;
            attribute vec4  a_Color;
            uniform   mat4  u_matMVP;
            uniform   mat4  u_matINV;
            uniform   vec3  u_vecLight;
            varying   float v_Diffuse;
            varying   vec4  v_Color;

            void main() {
                vec3   invLight  = normalize(u_matINV * vec4(u_vecLight, 0.0)).xyz;
                // 頂点シェーダで計算された拡散光の影響力を
                // フラグメントシェーダに渡している
                v_Diffuse   = clamp(dot(a_Normal,invLight), 0.0, 1.0);
                v_Color     = a_Color;
                gl_Position = u_matMVP   * vec4(a_Position, 1.0);
            }
            """.trimIndent()

    // フラグメントシェーダ
    private val scf =
            """
            precision mediump   float;

            uniform   float  u_lineScale;
            varying   float  v_Diffuse;
            varying   vec4   v_Color;

            void main() {
                vec2 v = gl_FragCoord.xy * u_lineScale;

                // cross line
                //	float f = max(sin(v.x + v.y), 0.0);
                //	float g = max(sin(v.x - v.y), 0.0);
                //
                //	float s;
                //	if(v_Diffuse > 0.6){
                //		s = 0.8;
                //	}else if(v_Diffuse > 0.1){
                //		s = 0.6 - pow(f, 5.0);
                //	}else{
                //		s = 0.4 - (pow(f, 5.0) + pow(g, 5.0));
                //	}
                //	gl_FragColor = vec4(vColor.rgb * s, 1.0);

                float f = sin(v.x + v.y);
                float s;
                if(v_Diffuse > 0.7){
                    s = 0.9;
                }else if(v_Diffuse > 0.3){
                    s = 0.6;
                }else{
                    s = 0.3;
                }
                gl_FragColor = vec4(v_Color.rgb * (v_Diffuse+f+s), 1.0);
            }
            """.trimIndent()

    override fun loadShader(): MgShader {
        // 頂点シェーダを生成
        svhandle = MyGLFunc.loadShader(GLES20.GL_VERTEX_SHADER, scv)
        // フラグメントシェーダを生成
        sfhandle = MyGLFunc.loadShader(GLES20.GL_FRAGMENT_SHADER, scf)

        // プログラムオブジェクトの生成とリンク
        programHandle = MyGLFunc.createProgram(svhandle,sfhandle, arrayOf("a_Position","a_Normal","a_Color") )
        return this
    }

    fun draw(model: MgModelAbs,
             matMVP: FloatArray,
             matINV: FloatArray,
             u_vecLight: FloatArray,
             u_dotScale: Float) {

        GLES20.glUseProgram(programHandle)
        MyGLFunc.checkGlError2("UseProgram",this,model)

        // attribute(頂点)
        model.bufPos.position(0)
        val hPosition = GLES20.glGetAttribLocation(programHandle,"a_Position").also {
            if ( it != -1 ) {
                GLES20.glVertexAttribPointer(it,3,GLES20.GL_FLOAT,false, 3*4, model.bufPos)
                GLES20.glEnableVertexAttribArray(it)
            }
        }
        MyGLFunc.checkGlError2("a_Position",this,model)

        // attribute(法線)
        model.bufNor.position(0)
        val hNormal = GLES20.glGetAttribLocation(programHandle,"a_Normal").also {
            if ( it != -1 ) {
                GLES20.glVertexAttribPointer(it,3,GLES20.GL_FLOAT,false, 3*4, model.bufNor)
                GLES20.glEnableVertexAttribArray(it)
            }
        }
        MyGLFunc.checkGlError2("a_Normal",this,model)

        // attribute(色)
        model.bufCol.position(0)
        val hColor = GLES20.glGetAttribLocation(programHandle,"a_Color").also {
            if ( it != -1 ) {
                GLES20.glVertexAttribPointer(it,4,GLES20.GL_FLOAT,false, 4*4, model.bufCol)
                GLES20.glEnableVertexAttribArray(it)
            }
        }
        MyGLFunc.checkGlError2("a_Color",this,model)

        // uniform(モデル×ビュー×プロジェクション)
        GLES20.glGetUniformLocation(programHandle,"u_matMVP").also {
            GLES20.glUniformMatrix4fv(it,1,false,matMVP,0)
        }
        MyGLFunc.checkGlError2("u_matMVP",this,model)

        // uniform(逆行列)
        GLES20.glGetUniformLocation(programHandle,"u_matINV").also {
            GLES20.glUniformMatrix4fv(it,1,false,matINV,0)
        }
        MyGLFunc.checkGlError2("u_matINV",this,model)

        // uniform(光源位置)
        GLES20.glGetUniformLocation(programHandle,"u_vecLight").also {
            GLES20.glUniform3fv(it,1,u_vecLight,0)
        }
        MyGLFunc.checkGlError2("u_vecLight",this,model)

        // uniform()
        GLES20.glGetUniformLocation(programHandle, "u_dotScale").also {
            GLES20.glUniform1f(it, u_dotScale)
        }
        MyGLFunc.checkGlError2("u_dotScale",this,model)

        // モデルを描画
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, model.datIdx.size, GLES20.GL_UNSIGNED_SHORT, model.bufIdx)

        // リソース解放
        if ( hPosition != -1 ) {
            GLES20.glDisableVertexAttribArray(hPosition)
        }
        if ( hNormal != -1 ) {
            GLES20.glDisableVertexAttribArray(hNormal)
        }
        if ( hColor != -1 ) {
            GLES20.glDisableVertexAttribArray(hColor)
        }
    }
}
