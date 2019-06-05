package milu.kiriu2010.exdb1.opengl06.w064v

import android.opengl.GLES20
import milu.kiriu2010.gui.model.MgModelAbs
import milu.kiriu2010.gui.basic.MyGLES20Func
import milu.kiriu2010.gui.shader.es20.ES20MgShader
import milu.kiriu2010.gui.vbo.es20.ES20VBOAbs

// -------------------------------------
// シェーダ(メイン)
// -------------------------------------
// https://wgld.org/d/webgl/w064.html
// -------------------------------------
class WV064ShaderMain: ES20MgShader() {
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


    override fun loadShader(): ES20MgShader {
        // 頂点シェーダを生成
        svhandle = MyGLES20Func.loadShader(GLES20.GL_VERTEX_SHADER, scv)
        // フラグメントシェーダを生成
        sfhandle = MyGLES20Func.loadShader(GLES20.GL_FRAGMENT_SHADER, scf)

        // プログラムオブジェクトの生成とリンク
        programHandle = MyGLES20Func.createProgram(svhandle,sfhandle)

        // ----------------------------------------------
        // attributeハンドルに値をセット
        // ----------------------------------------------
        hATTR = IntArray(3)
        // 属性(頂点)
        hATTR[0] = GLES20.glGetAttribLocation(programHandle, "a_Position").also {
            // attribute属性を有効にする
            // ここで呼ばないと描画されない
            GLES20.glEnableVertexAttribArray(it)
            MyGLES20Func.checkGlError("a_Position:glEnableVertexAttribArray")
            // attribute属性を登録
            GLES20.glVertexAttribPointer(it,3,GLES20.GL_FLOAT,false,0,0)
            MyGLES20Func.checkGlError("a_Position:glVertexAttribPointer")
        }
        MyGLES20Func.checkGlError("a_Position:glGetAttribLocation")

        // 属性(法線)
        hATTR[1] = GLES20.glGetAttribLocation(programHandle, "a_Normal").also {
            // attribute属性を有効にする
            // ここで呼ばないと描画されない
            GLES20.glEnableVertexAttribArray(it)
            MyGLES20Func.checkGlError("a_Normal:glEnableVertexAttribArray")
            // attribute属性を登録
            GLES20.glVertexAttribPointer(it,3,GLES20.GL_FLOAT,false,0,0)
            MyGLES20Func.checkGlError("a_Normal:glVertexAttribPointer")
        }
        MyGLES20Func.checkGlError("a_Normal:glGetAttribLocation")

        // 属性(色)
        hATTR[2] = GLES20.glGetAttribLocation(programHandle, "a_Color").also {
            // attribute属性を有効にする
            // ここで呼ばないと描画されない
            GLES20.glEnableVertexAttribArray(it)
            MyGLES20Func.checkGlError("a_Color:glEnableVertexAttribArray")
            // attribute属性を登録
            GLES20.glVertexAttribPointer(it,4,GLES20.GL_FLOAT,false,0,0)
            MyGLES20Func.checkGlError("a_Color:glVertexAttribPointer")
        }
        MyGLES20Func.checkGlError("a_Color:glGetAttribLocation")

        // ----------------------------------------------
        // uniformハンドルに値をセット
        // ----------------------------------------------
        hUNI = IntArray(8)

        // uniform(モデル座標変換行列)
        hUNI[0] = GLES20.glGetUniformLocation(programHandle,"u_matM")
        MyGLES20Func.checkGlError("u_matM:glGetUniformLocation")

        // uniform(モデル×ビュー×プロジェクション)
        hUNI[1] = GLES20.glGetUniformLocation(programHandle,"u_matMVP")
        MyGLES20Func.checkGlError("u_matMVP:glGetUniformLocation")

        // uniform(逆行列)
        hUNI[2] = GLES20.glGetUniformLocation(programHandle,"u_matINV")
        MyGLES20Func.checkGlError("u_matINV:glGetUniformLocation")

        // uniform(光源位置)
        hUNI[3] = GLES20.glGetUniformLocation(programHandle,"u_vecLight")
        MyGLES20Func.checkGlError("u_vecLight:glGetUniformLocation")

        // uniform()
        hUNI[4] = GLES20.glGetUniformLocation(programHandle,"u_vecCenter")
        MyGLES20Func.checkGlError("u_vecCenter:glGetUniformLocation")

        // uniform(視点座標)
        hUNI[5] = GLES20.glGetUniformLocation(programHandle,"u_vecEye")
        MyGLES20Func.checkGlError("u_vecEye:glGetUniformLocation")

        // uniform()
        hUNI[6] = GLES20.glGetUniformLocation(programHandle, "u_colorRim")
        MyGLES20Func.checkGlError("u_colorRim:glGetUniformLocation")

        // uniform()
        hUNI[7] = GLES20.glGetUniformLocation(programHandle, "u_rimCoef")
        MyGLES20Func.checkGlError("u_rimCoef:glGetUniformLocation")

        return this
    }

    fun draw(model: MgModelAbs,
             bo: ES20VBOAbs,
             u_matM: FloatArray,
             u_matMVP: FloatArray,
             u_matI: FloatArray,
             u_vecLight: FloatArray,
             u_vecCenter: FloatArray,
             u_vecEye: FloatArray,
             u_colorRim: FloatArray,
             u_rimCoef: Float) {

        GLES20.glUseProgram(programHandle)
        MyGLES20Func.checkGlError2("UseProgram",this,model)

        // attribute(頂点)
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,bo.hVBO[0])
        GLES20.glVertexAttribPointer(hATTR[0],3,GLES20.GL_FLOAT,false,0,0)
        MyGLES20Func.checkGlError2("a_Position",this,model)

        // attribute(法線)
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,bo.hVBO[1])
        GLES20.glVertexAttribPointer(hATTR[1],3,GLES20.GL_FLOAT,false,0,0)
        MyGLES20Func.checkGlError2("a_Normal",this,model)

        // attribute(色)
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,bo.hVBO[2])
        GLES20.glVertexAttribPointer(hATTR[2],4,GLES20.GL_FLOAT,false,0,0)
        MyGLES20Func.checkGlError2("a_Color",this,model)

        // uniform(モデル座標変換行列)
        GLES20.glUniformMatrix4fv(hUNI[0],1,false,u_matM,0)
        MyGLES20Func.checkGlError2("u_matM",this,model)

        // uniform(モデル×ビュー×プロジェクション)
        GLES20.glUniformMatrix4fv(hUNI[1],1,false,u_matMVP,0)
        MyGLES20Func.checkGlError2("u_matMVP",this,model)

        // uniform(逆行列)
        GLES20.glUniformMatrix4fv(hUNI[2],1,false,u_matI,0)
        MyGLES20Func.checkGlError2("u_matINV",this,model)

        // uniform(光源位置)
        GLES20.glUniform3fv(hUNI[3],1,u_vecLight,0)
        MyGLES20Func.checkGlError2("u_vecLight",this,model)

        // uniform()
        GLES20.glUniform3fv(hUNI[4],1,u_vecCenter,0)
        MyGLES20Func.checkGlError2("u_vecCenter",this,model)

        // uniform(視点座標)
        GLES20.glUniform3fv(hUNI[5],1,u_vecEye,0)
        MyGLES20Func.checkGlError2("u_vecEye",this,model)

        // uniform()
        GLES20.glUniform4fv(hUNI[6], 1,u_colorRim,0)
        MyGLES20Func.checkGlError2("u_colorRim",this,model)

        // uniform()
        GLES20.glUniform1f(hUNI[7], u_rimCoef)
        MyGLES20Func.checkGlError2("u_rimCoef",this,model)

        // モデルを描画
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, model.datIdx.size, GLES20.GL_UNSIGNED_SHORT, model.bufIdx)

        // リソース解放
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,0)
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER,0)
    }
}