package milu.kiriu2010.exdb1.opengl06.w064

import android.opengl.GLES20
import milu.kiriu2010.gui.model.MgModelAbs
import milu.kiriu2010.gui.basic.MyGLFunc
import milu.kiriu2010.gui.shader.MgShader

// シェーダ(メイン)
class W064ShaderMain: MgShader() {
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
            // カメラの注視点
            uniform   vec3  u_vecCenter;
            uniform   vec3  u_vecEye;
            uniform   vec4  u_colorRim;
            // リムライトの強さ係数
            uniform   float u_rimCoef;
            varying   vec4  v_Color;

            void main() {
                vec3   invLight   = normalize(u_matINV * vec4(u_vecLight, 0.0)).xyz;
                vec3   invEye     = normalize(u_matINV * vec4(u_vecEye  , 0.0)).xyz;
                vec3   halfLE     = normalize(invLight + invEye);
                float  diffuse    = clamp(dot(a_Normal,invLight), 0.1, 1.0);
                float  specular   = pow(clamp(dot(a_Normal, halfLE), 0.0, 1.0), 50.0);
                // リムライティングの係数
                // ----------------------------------------------------------------------
                // 視線ベクトルと法線ベクトルの角度が直角に近づけば近づくほど、
                // ライティングの係数が大きくなるようにする
                // ----------------------------------------------------------------------
                // rimの値が0の場合、
                // 視線ベクトルとライトベクトルの計算がどのような結果でも
                // リムライトは当たらない
                // ----------------------------------------------------------------------
                // powを使ってコントラストを強くしている
                // ----------------------------------------------------------------------
                float  rim        = pow(1.0 - clamp(dot(a_Normal,invEye),0.0,1.0), 5.0);
                // ----------------------------------------------------------------------
                // 視線ベクトルとライトベクトルとの間で内積をとることで、
                // ２つのベクトルがどの程度向かいあっているかを係数化する
                // ----------------------------------------------------------------------
                // powを使ってコントラストを強くしている
                // ----------------------------------------------------------------------
                float  dotLE      = pow(max(dot(normalize(u_vecCenter-u_vecEye),normalize(u_vecLight)), 0.0), 30.0);
                vec4   ambient    = u_colorRim * u_rimCoef * rim * dotLE;
                v_Color         = a_Color * vec4(vec3(diffuse),1.0) + vec4(vec3(specular),1.0) + vec4(ambient.rgb, 1.0);
                gl_Position     = u_matMVP * vec4(a_Position, 1.0);
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


    override fun loadShader(): MgShader {
        // 頂点シェーダを生成
        val svhandle = MyGLFunc.loadShader(GLES20.GL_VERTEX_SHADER, scv)
        // フラグメントシェーダを生成
        val sfhandle = MyGLFunc.loadShader(GLES20.GL_FRAGMENT_SHADER, scf)

        // プログラムオブジェクトの生成とリンク
        programHandle = MyGLFunc.createProgram(svhandle,sfhandle, arrayOf("a_Position","a_Normal","a_Color") )
        return this
    }

    fun draw(model: MgModelAbs,
             matM: FloatArray,
             matMVP: FloatArray,
             matINV: FloatArray,
             u_vecLight: FloatArray,
             u_vecCenter: FloatArray,
             u_vecEye: FloatArray,
             u_colorRim: FloatArray,
             u_rimCoef: Float) {

        GLES20.glUseProgram(programHandle)

        // attribute(頂点)
        model.bufPos.position(0)
        GLES20.glGetAttribLocation(programHandle,"a_Position").also {
            GLES20.glVertexAttribPointer(it,3,GLES20.GL_FLOAT,false, 3*4, model.bufPos)
            GLES20.glEnableVertexAttribArray(it)
        }
        MyGLFunc.checkGlError("a_Position:${model.javaClass.simpleName}")

        // attribute(法線)
        model.bufNor.position(0)
        GLES20.glGetAttribLocation(programHandle,"a_Normal").also {
            GLES20.glVertexAttribPointer(it,3,GLES20.GL_FLOAT,false, 3*4, model.bufNor)
            GLES20.glEnableVertexAttribArray(it)
        }
        MyGLFunc.checkGlError("a_Normal:${model.javaClass.simpleName}")

        // attribute(色)
        model.bufCol.position(0)
        GLES20.glGetAttribLocation(programHandle,"a_Color").also {
            GLES20.glVertexAttribPointer(it,4,GLES20.GL_FLOAT,false, 4*4, model.bufCol)
            GLES20.glEnableVertexAttribArray(it)
        }
        MyGLFunc.checkGlError("a_Color:${model.javaClass.simpleName}")

        // uniform(モデル座標変換行列)
        GLES20.glGetUniformLocation(programHandle,"u_matM").also {
            GLES20.glUniformMatrix4fv(it,1,false,matM,0)
        }
        MyGLFunc.checkGlError("u_matM:${model.javaClass.simpleName}")

        // uniform(モデル×ビュー×プロジェクション)
        GLES20.glGetUniformLocation(programHandle,"u_matMVP").also {
            GLES20.glUniformMatrix4fv(it,1,false,matMVP,0)
        }
        MyGLFunc.checkGlError("u_matMVP:${model.javaClass.simpleName}")

        // uniform(逆行列)
        GLES20.glGetUniformLocation(programHandle,"u_matINV").also {
            GLES20.glUniformMatrix4fv(it,1,false,matINV,0)
        }
        MyGLFunc.checkGlError("u_matINV:${model.javaClass.simpleName}")

        // uniform(ライトの向き)
        GLES20.glGetUniformLocation(programHandle,"u_vecLight").also {
            GLES20.glUniform3fv(it,1,u_vecLight,0)
        }
        MyGLFunc.checkGlError("u_vecLight:${model.javaClass.simpleName}")

        // uniform()
        GLES20.glGetUniformLocation(programHandle,"u_vecCenter").also {
            GLES20.glUniform3fv(it,1,u_vecCenter,0)
        }
        MyGLFunc.checkGlError("u_vecCenter:${model.javaClass.simpleName}")

        // uniform(視点座標)
        GLES20.glGetUniformLocation(programHandle,"u_vecEye").also {
            GLES20.glUniform3fv(it,1,u_vecEye,0)
        }
        MyGLFunc.checkGlError("u_vecEye:${model.javaClass.simpleName}")

        // uniform()
        GLES20.glGetUniformLocation(programHandle, "u_colorRim").also {
            GLES20.glUniform4fv(it, 1,u_colorRim,0)
        }
        MyGLFunc.checkGlError("u_colorRim:${model.javaClass.simpleName}")

        // uniform()
        GLES20.glGetUniformLocation(programHandle, "u_rimCoef").also {
            GLES20.glUniform1f(it, u_rimCoef)
        }
        MyGLFunc.checkGlError("u_rimCoef:${model.javaClass.simpleName}")

        // モデルを描画
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, model.datIdx.size, GLES20.GL_UNSIGNED_SHORT, model.bufIdx)
    }
}