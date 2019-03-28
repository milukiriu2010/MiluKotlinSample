package milu.kiriu2010.exdb1.opengl01.w019

import android.graphics.Bitmap
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.GLUtils
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import android.opengl.Matrix
import android.os.SystemClock
import android.view.MotionEvent
import milu.kiriu2010.exdb1.opengl.MyGLFunc
import milu.kiriu2010.exdb1.opengl02.w029.W029Shader
import java.lang.RuntimeException

// ---------------------------------------------------
// テクスチャ
// ---------------------------------------------------
// https://wgld.org/d/webgl/w029.html
// ---------------------------------------------------
class W029Renderer: GLSurfaceView.Renderer {
    // 描画オブジェクト
    private lateinit var drawObj: W029Model

    // プログラムハンドル
    private var programHandle: Int = 0

    // モデル変換行列
    private val matM = FloatArray(16)
    // ビュー変換行列
    private val matV = FloatArray(16)
    // プロジェクション変換行列
    private val matP = FloatArray(16)
    // モデル・ビュー・プロジェクション行列
    private val matMVP = FloatArray(16)
    // テンポラリ行列
    private val matT = FloatArray(16)
    // カメラの座標
    private val vecEye = floatArrayOf(0f,0f,5f)
    // カメラの上方向を表すベクトル
    private val vecEyeUp = floatArrayOf(0f,1f,0f)
    // 原点のベクトル
    private val vecCenter = floatArrayOf(0f,0f,0f)

    // ビットマップ配列
    val bmpArray = arrayListOf<Bitmap>()

    // テクスチャ配列
    val textures = IntArray(2)

    // 回転スイッチ
    var rotateSwitch = false

    // 回転角度
    private var angle1 = 0

    override fun onDrawFrame(gl: GL10) {

        // canvasを初期化
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        // 回転角度
        angle1 =(angle1+5)%360
        val t1 = angle1.toFloat()

        // ビュー×プロジェクション座標変換行列
        Matrix.multiplyMM(matT,0,matP,0,matV,0)

        // テクスチャ0をバインド
        drawObj.activateTexture(0,textures,bmpArray[0])

        renderMap(1,t1)
        renderMap(2,t1)
        renderMap(3,t1)
        renderMap(4,t1)
        renderMap(5,t1)
        renderMap(6,t1)
        renderMap(7,t1)
        renderMap(8,t1)
        renderMap(9,t1)
    }

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)

        val ratio = width.toFloat()/height.toFloat()

        Matrix.perspectiveM(matP,0,60f,ratio,0.1f,100f)
    }

    override fun onSurfaceCreated(gl: GL10, config: EGLConfig?) {
        // canvasを初期化する色を設定する
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)

        // canvasを初期化する際の深度を設定する
        GLES20.glClearDepthf(1f)

        // 深度テストを有効にする
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)
        GLES20.glDepthFunc(GLES20.GL_LEQUAL)

        // シェーダプログラム登録
        programHandle = W029Shader().loadShader()

        // テクスチャ作成し、idをtexturesに保存
        GLES20.glGenTextures(1,textures,0)
        MyGLFunc.checkGlError("glGenTextures")

        // モデル生成
        drawObj = W029Model()

        // カメラの位置
        Matrix.setLookAtM(matV, 0,
                vecEye[0], vecEye[1], vecEye[2],
                vecCenter[0], vecCenter[1], vecCenter[2],
                vecEyeUp[0], vecEyeUp[1], vecEyeUp[2])
    }

    private fun renderMap(id: Int, t1: Float) {

        when ( id ) {
            1 -> {
                // -----------------------------------------------
                // １つ目
                // -----------------------------------------------
                // 縮小時の補完設定
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST)
                // 拡大時の補完設定
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST)
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,GLES20.GL_REPEAT)
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,GLES20.GL_REPEAT)
                render(t1, floatArrayOf(-3f,3f,0f))
            }
            2 -> {
                // -----------------------------------------------
                // ２つ目
                // -----------------------------------------------
                // 縮小時の補完設定
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR)
                // 拡大時の補完設定
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR)
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,GLES20.GL_REPEAT)
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,GLES20.GL_REPEAT)
                render(t1, floatArrayOf(-3f,0f,0f))
            }
            3 -> {
                // -----------------------------------------------
                // ３つ目
                // -----------------------------------------------
                // 縮小時の補完設定
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST_MIPMAP_NEAREST)
                // 拡大時の補完設定
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR)
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,GLES20.GL_REPEAT)
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,GLES20.GL_REPEAT)
                render(t1, floatArrayOf(-3f,-3f,0f))
            }
            4 -> {
                // -----------------------------------------------
                // ４つ目
                // -----------------------------------------------
                // 縮小時の補完設定
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST_MIPMAP_LINEAR)
                // 拡大時の補完設定
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR)
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,GLES20.GL_REPEAT)
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,GLES20.GL_REPEAT)
                render(t1, floatArrayOf(0f,3f,0f))
            }
            5 -> {
                // -----------------------------------------------
                // ５つ目
                // -----------------------------------------------
                // 縮小時の補完設定
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR_MIPMAP_NEAREST)
                // 拡大時の補完設定
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR)
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,GLES20.GL_REPEAT)
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,GLES20.GL_REPEAT)
                render(t1, floatArrayOf(0f,0f,0f))
            }
            6 -> {
                // -----------------------------------------------
                // ６つ目
                // -----------------------------------------------
                // 縮小時の補完設定
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR_MIPMAP_LINEAR)
                // 拡大時の補完設定
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR)
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,GLES20.GL_REPEAT)
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,GLES20.GL_REPEAT)
                render(t1, floatArrayOf(0f,-3f,0f))
            }
            7 -> {
                // -----------------------------------------------
                // ７つ目
                // -----------------------------------------------
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,GLES20.GL_REPEAT)
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,GLES20.GL_REPEAT)
                render(t1, floatArrayOf(3f,3f,0f))
            }
            8 -> {
                // -----------------------------------------------
                // ８つ目
                // -----------------------------------------------
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,GLES20.GL_MIRRORED_REPEAT)
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,GLES20.GL_MIRRORED_REPEAT)
                render(t1, floatArrayOf(3f,0f,0f))
            }
            9 -> {
                // -----------------------------------------------
                // ９つ目
                // -----------------------------------------------
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,GLES20.GL_CLAMP_TO_EDGE)
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,GLES20.GL_CLAMP_TO_EDGE)
                render(t1, floatArrayOf(3f,-3f,0f))
            }
        }
    }

    private fun render(angleInDegrees: Float, trans: FloatArray) {
        // モデル座標変換行列の生成
        Matrix.setIdentityM(matM,0)
        Matrix.translateM(matM,0,trans[0],trans[1],trans[2])
        if ( rotateSwitch == true ) {
            Matrix.rotateM(matM,0,angleInDegrees,0f,1f,0f)
        }
        Matrix.multiplyMM(matMVP,0,matT,0,matM,0)

        // uniform変数の登録と描画
        drawObj.draw(programHandle, matMVP,0,1)
    }

    fun receiveTouch(ev: MotionEvent, w: Int, h: Int ) {

    }
}
