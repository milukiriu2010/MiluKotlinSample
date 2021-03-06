package milu.kiriu2010.exdb1.opengl06.w062

import android.opengl.GLES20
import milu.kiriu2010.gui.model.MgModelAbs
import milu.kiriu2010.gui.basic.MyGLES20Func
import milu.kiriu2010.gui.shader.es20.ES20MgShader

// ------------------------------------
// シェーダ(メイン):VBOなし
// OpenGL ES 2.0
// ------------------------------------
//   鏡面世界側もレンダリングする
// ------------------------------------
// https://wgld.org/d/webgl/w062.html
// ------------------------------------
class W062ShaderMain: ES20MgShader() {
    // 頂点シェーダ
    private val scv =
            """
            // 鏡面世界と通常のワールド空間を同じシェーダでレンダリングできるようにするため、
            // モデル座標変換行列とビュー×プロジェクション座標変換行列を渡している
            attribute vec3  a_Position;
            attribute vec3  a_Normal;
            attribute vec4  a_Color;
            uniform   mat4  u_matM;
            uniform   mat4  u_matVP;
            uniform   mat4  u_matINV;
            uniform   vec3  u_vecLight;
            uniform   vec3  u_vecEye;
            uniform   vec4  u_ambientColor;
            uniform   int   u_mirror;
            varying   vec4  v_Color;

            void main() {
                vec3   invLight = normalize(u_matINV * vec4(u_vecLight, 0.0)).xyz;
                vec3   invEye   = normalize(u_matINV * vec4(u_vecEye  , 0.0)).xyz;
                vec3   halfLE   = normalize(invLight + invEye);
                float  diffuse  = clamp(dot(a_Normal,invLight), 0.1, 1.0);
                float  specular = pow(clamp(dot(a_Normal, halfLE), 0.0, 1.0), 50.0);
                v_Color         = a_Color * vec4(vec3(diffuse),1.0) + vec4(vec3(specular),1.0) + u_ambientColor;
                vec4   pos      = u_matM * vec4(a_Position, 1.0);

                // 鏡面の場合、Y成分だけを反転する
                if (bool(u_mirror)) {
                    pos = vec4(pos.x, -pos.y, pos.zw);
                }
                gl_Position     = u_matVP   * pos;
            }
            """.trimIndent()

    // フラグメントシェーダ
    private val scf =
            """
            precision mediump   float;

            varying   vec4      v_Color;

            void main() {
                gl_FragColor = v_Color;
            }
            """.trimIndent()


    override fun loadShader(): ES20MgShader {
        // 頂点シェーダを生成
        svhandle = MyGLES20Func.loadShader(GLES20.GL_VERTEX_SHADER, scv)
        // フラグメントシェーダを生成
        sfhandle = MyGLES20Func.loadShader(GLES20.GL_FRAGMENT_SHADER, scf)

        // プログラムオブジェクトの生成とリンク
        programHandle = MyGLES20Func.createProgram(svhandle,sfhandle, arrayOf("a_Position","a_Normal","a_Color") )
        return this
    }

    fun draw(model: MgModelAbs,
             matM: FloatArray,
             matVP: FloatArray,
             matINV: FloatArray,
             u_vecLight: FloatArray,
             u_vecEye: FloatArray,
             u_ambientColor: FloatArray,
             u_mirror: Int) {

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

        // uniform(モデル座標変換行列)
        GLES20.glGetUniformLocation(programHandle,"u_matM").also {
            GLES20.glUniformMatrix4fv(it,1,false,matM,0)
        }
        MyGLES20Func.checkGlError2("u_matM",this,model)

        // uniform(ビュー×プロジェクション)
        GLES20.glGetUniformLocation(programHandle,"u_matVP").also {
            GLES20.glUniformMatrix4fv(it,1,false,matVP,0)
        }
        MyGLES20Func.checkGlError2("u_matVP",this,model)

        // uniform(逆行列)
        GLES20.glGetUniformLocation(programHandle,"u_matINV").also {
            GLES20.glUniformMatrix4fv(it,1,false,matINV,0)
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

        // uniform(環境色)
        GLES20.glGetUniformLocation(programHandle, "u_ambientColor").also {
            GLES20.glUniform4fv(it, 1,u_ambientColor,0)
        }
        MyGLES20Func.checkGlError2("u_ambientColor",this,model)

        // uniform(ミラーするかどうか)
        GLES20.glGetUniformLocation(programHandle, "u_mirror").also {
            GLES20.glUniform1i(it, u_mirror)
        }
        MyGLES20Func.checkGlError2("u_mirror",this,model)

        // モデルを描画
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, model.datIdx.size, GLES20.GL_UNSIGNED_SHORT, model.bufIdx)
    }
}