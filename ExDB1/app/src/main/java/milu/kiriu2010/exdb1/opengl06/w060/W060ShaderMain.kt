package milu.kiriu2010.exdb1.opengl06.w060

import android.opengl.GLES20
import milu.kiriu2010.gui.model.MgModelAbs
import milu.kiriu2010.gui.basic.MyGLES20Func
import milu.kiriu2010.gui.shader.es20.ES20MgShader

// --------------------------------------------------------------
// シェーダ(メイン):VBOなし
// OpenGL ES 2.0
// --------------------------------------------------------------
// https://wgld.org/d/webgl/w060.html
// --------------------------------------------------------------
class W060ShaderMain: ES20MgShader() {
    // 頂点シェーダ
    private val scv =
            """
            attribute vec3  a_Position;
            attribute vec3  a_Normal;
            attribute vec4  a_Color;
            uniform   mat4  u_matM;
            uniform   mat4  u_matMVP;
            uniform   mat4  u_matINV;
            uniform   vec3  u_vecLight;
            uniform   vec3  u_vecEye;
            uniform   vec4  u_ambientColor;
            uniform   float u_fogStart;
            uniform   float u_fogEnd;
            varying   vec4  v_Color;
            varying   float v_fogFactor;

            const float near = 0.1;
            const float far  = 30.0;
            const float linearDepth = 1.0/(far-near);

            void main() {
                vec3   invLight  = normalize(u_matINV * vec4(u_vecLight, 0.0)).xyz;
                vec3   invEye    = normalize(u_matINV * vec4(u_vecEye  , 0.0)).xyz;
                vec3   halfLE    = normalize(invLight + invEye);
                float  diffuse   = clamp(dot(a_Normal,invLight), 0.0, 1.0);
                float  specular  = pow(clamp(dot(a_Normal, halfLE), 0.0, 1.0), 50.0);
                vec4   amb       = a_Color * u_ambientColor;
                v_Color          = amb * vec4(vec3(diffuse),1.0) + vec4(vec3(specular),1.0);

                // モデル座標変換を適用した頂点の座標位置
                vec3   pos       = (u_matM * vec4(a_Position,1.0)).xyz;
                // "カメラとモデル頂点の距離"に定数をかけて正規化する
                //   こうすることで
                //   今処理しようとしている頂点が
                //   シーン全体のどの程度の深度にあるか
                //   0～1の範囲で表される
                float  linearPos = length(u_vecEye-pos) * linearDepth;
                v_fogFactor      = clamp((u_fogEnd-linearPos)/(u_fogEnd-u_fogStart), 0.0, 1.0);

                gl_Position      = u_matMVP   * vec4(a_Position, 1.0);
            }
            """.trimIndent()

    // フラグメントシェーダ
    private val scf =
            """
            precision mediump   float;

            uniform   vec4   u_fogColor;
            varying   vec4   v_Color;
            varying   float  v_fogFactor;

            void main() {
                gl_FragColor = mix(u_fogColor, v_Color, v_fogFactor);
            }
            """.trimIndent()


    override fun loadShader(): ES20MgShader {
        // 頂点シェーダを生成
        val svhandle = MyGLES20Func.loadShader(GLES20.GL_VERTEX_SHADER, scv)
        // フラグメントシェーダを生成
        val sfhandle = MyGLES20Func.loadShader(GLES20.GL_FRAGMENT_SHADER, scf)

        // プログラムオブジェクトの生成とリンク
        programHandle = MyGLES20Func.createProgram(svhandle,sfhandle, arrayOf("a_Position","a_Normal","a_Color") )
        return this
    }

    fun draw(model: MgModelAbs,
             matM: FloatArray,
             matMVP: FloatArray,
             matINV: FloatArray,
             u_vecLight: FloatArray,
             u_vecEye: FloatArray,
             u_ambientColor: FloatArray,
             u_fogStart: Float,
             u_fogEnd: Float,
             u_fogColor: FloatArray) {

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

        // uniform(モデル×ビュー×プロジェクション)
        GLES20.glGetUniformLocation(programHandle,"u_matMVP").also {
            GLES20.glUniformMatrix4fv(it,1,false,matMVP,0)
        }
        MyGLES20Func.checkGlError2("u_matMVP",this,model)

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

        // uniform(フォグが掛かり始める最初の位置)
        GLES20.glGetUniformLocation(programHandle, "u_fogStart").also {
            GLES20.glUniform1f(it, u_fogStart)
        }
        MyGLES20Func.checkGlError2("u_fogStart",this,model)

        // uniform(完全にフォグが掛かりモデルが見えなくなってしまう位置)
        GLES20.glGetUniformLocation(programHandle, "u_fogEnd").also {
            GLES20.glUniform1f(it, u_fogEnd)
        }
        MyGLES20Func.checkGlError2("u_fogEnd",this,model)

        // uniform(フォグの色)
        GLES20.glGetUniformLocation(programHandle, "u_fogColor").also {
            GLES20.glUniform4fv(it, 1,u_fogColor,0)
        }
        MyGLES20Func.checkGlError2("u_fogColor",this,model)

        // モデルを描画
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, model.datIdx.size, GLES20.GL_UNSIGNED_SHORT, model.bufIdx)
    }
}