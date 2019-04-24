package milu.kiriu2010.gui.shader

import android.opengl.GLES20
import milu.kiriu2010.gui.model.MgModelAbs
import milu.kiriu2010.gui.basic.MyGLFunc

// 点光源
// https://wgld.org/d/webgl/w025.html
class PointLight01Shader: MgShader() {
    // 頂点シェーダ
    private val scv =
            """
            attribute vec3 a_Position;
            attribute vec3 a_Normal;
            attribute vec4 a_Color;
            uniform   mat4 u_matMVP;
            // モデル座標変換行列
            uniform   mat4 u_matM;
            varying   vec3 v_Position;
            varying   vec3 v_Normal;
            varying   vec4 v_Color;

            void main() {
                v_Position     = (u_matM * vec4(a_Position,1.0)).xyz;
                v_Normal       = a_Normal;
                v_Color        = a_Color;
                gl_Position    = u_matMVP * vec4(a_Position,1.0);
            }
            """.trimIndent()

    // フラグメントシェーダ
    private val scf =
            """
            precision mediump float;
            // 逆行列
            uniform   mat4 u_matINV;
            // 光源位置
            uniform   vec3 u_vecLight;
            // 視点位置
            uniform   vec3 u_vecEye;
            // 環境光の色
            uniform   vec4 u_ambientColor;
            varying   vec3 v_Position;
            varying   vec3 v_Normal;
            varying   vec4 v_Color;

            void main() {
                // 点光源から頂点へ向かうベクトル
                vec3  vecLight  = u_vecLight - v_Position;
                // 光の逆ベクトル
                vec3  invLight  = normalize(u_matINV * vec4(vecLight,0.0)).xyz;
                // 視線の逆ベクトル
                vec3  invEye    = normalize(u_matINV * vec4(u_vecEye,0.0)).xyz;
                // 光ベクトルと視線ベクトルからハーフベクトルを算出
                vec3  halfLE    = normalize(invLight + invEye);
                // 拡散度("頂点の法線ベクトル"と"光の逆ベクトル"の内積をとり、0.0～1.0の値を返す
                float diffuse   = clamp(dot(v_Normal,invLight), 0.0, 1.0) + 0.2;
                // 面の法線ベクトルとハーフベクトルの内積をとることで反射光の強さを決定する
                float specular  = pow(clamp(dot(v_Normal, halfLE),0.0,1.0), 50.0);
                // 頂点の色に拡散光と反射光を足す
                vec4  destColor = v_Color * vec4(vec3(diffuse),1.0) + vec4(vec3(specular),1.0) + u_ambientColor;
                gl_FragColor    = destColor;
            }
            """.trimIndent()

    override fun loadShader(): MgShader {
        // 頂点シェーダを生成
        val svhandle = MyGLFunc.loadShader(GLES20.GL_VERTEX_SHADER, scv)
        // フラグメントシェーダを生成
        val sfhandle = MyGLFunc.loadShader(GLES20.GL_FRAGMENT_SHADER, scf)

        // プログラムオブジェクトの生成とリンク
        programHandle = MyGLFunc.createProgram(svhandle,sfhandle, arrayOf("a_Position","a_Normal","a_Color") )

        return this
    }

    fun draw(modelAbs: MgModelAbs,
             u_matMVP: FloatArray,
             u_matM: FloatArray,
             u_matINV: FloatArray,
             u_vecLight: FloatArray,
             u_vecEye: FloatArray,
             u_ambientColor: FloatArray) {

        GLES20.glUseProgram(programHandle)

        // attribute(頂点)
        modelAbs.bufPos.position(0)
        // get handle to vertex shader's vPosition member
        GLES20.glGetAttribLocation(programHandle, "a_Position").also {
            GLES20.glVertexAttribPointer(it,3,GLES20.GL_FLOAT,false,3*4,modelAbs.bufPos)
            GLES20.glEnableVertexAttribArray(it)
        }
        MyGLFunc.checkGlError("a_Position")

        // attribute(法線)
        modelAbs.bufNor.position(0)
        GLES20.glGetAttribLocation(programHandle,"a_Normal").also {
            GLES20.glVertexAttribPointer(it,3,GLES20.GL_FLOAT,false, 3*4, modelAbs.bufNor)
            GLES20.glEnableVertexAttribArray(it)
        }
        MyGLFunc.checkGlError("a_Normal")

        // attribute(色)
        modelAbs.bufCol.position(0)
        GLES20.glGetAttribLocation(programHandle,"a_Color").also {
            GLES20.glVertexAttribPointer(it,3,GLES20.GL_FLOAT,false, 4*4, modelAbs.bufCol)
            GLES20.glEnableVertexAttribArray(it)
        }
        MyGLFunc.checkGlError("a_Color")

        // uniform(モデル×ビュー×プロジェクション)
        GLES20.glGetUniformLocation(programHandle,"u_matMVP").also {
            GLES20.glUniformMatrix4fv(it,1,false,u_matMVP,0)
        }
        MyGLFunc.checkGlError("u_matMVP")

        // uniform(座標行列)
        GLES20.glGetUniformLocation(programHandle,"u_matM").also {
            GLES20.glUniformMatrix4fv(it,1,false,u_matM,0)
        }
        MyGLFunc.checkGlError("u_matM")

        // uniform(逆行列)
        GLES20.glGetUniformLocation(programHandle,"u_matINV").also {
            GLES20.glUniformMatrix4fv(it,1,false,u_matINV,0)
        }
        MyGLFunc.checkGlError("u_matINV")

        // uniform(光源位置)
        GLES20.glGetUniformLocation(programHandle,"u_vecLight").also {
            GLES20.glUniform3fv(it,1,u_vecLight,0)
        }
        MyGLFunc.checkGlError("u_vecLight")

        // uniform(視点位置)
        GLES20.glGetUniformLocation(programHandle,"u_vecEye").also {
            GLES20.glUniform3fv(it,1,u_vecEye,0)
        }
        MyGLFunc.checkGlError("u_vecEye")

        // uniform(環境光の色)
        GLES20.glGetUniformLocation(programHandle,"u_ambientColor").also {
            GLES20.glUniform4fv(it,1,u_ambientColor,0)
        }
        MyGLFunc.checkGlError("u_ambientColor")

        // モデルを描画
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, modelAbs.datIdx.size, GLES20.GL_UNSIGNED_SHORT, modelAbs.bufIdx)
    }
}