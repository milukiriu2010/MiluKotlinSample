package milu.kiriu2010.exdb1.opengl03.w035

import android.graphics.Bitmap
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import android.view.MotionEvent
import milu.kiriu2010.gui.basic.MyGLFunc
import milu.kiriu2010.gui.basic.MyQuaternion
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import kotlin.math.sqrt

// 平行光源
class W035Renderer: GLSurfaceView.Renderer {
    // 描画オブジェクト
    private lateinit var drawObj: W035Model

    // プログラムハンドル
    private var programHandle: Int = 0

    // 画面縦横比
    var ratio: Float = 0f


    // モデル変換行列
    private val matM = FloatArray(16)
    // モデル変換行列の逆行列
    private val matI = FloatArray(16)
    // ビュー変換行列
    private val matV = FloatArray(16)
    // プロジェクション変換行列
    private val matP = FloatArray(16)
    // モデル・ビュー・プロジェクション行列
    private val matMVP = FloatArray(16)
    // テンポラリ行列
    private val matT = FloatArray(16)
    // 点光源の位置
    private val vecLight = floatArrayOf(15f,10f,15f)
    // 環境光の色
    //private val vecAmbientColor = floatArrayOf(0.1f,0.1f,0.1f,1f)
    // カメラの座標
    private val vecEye = floatArrayOf(0f,5f,10f)
    // カメラの上方向を表すベクトル
    private val vecEyeUp = floatArrayOf(0f,1f,0f)
    // 原点のベクトル
    private val vecCenter = floatArrayOf(0f,0f,0f)

    // ビットマップ配列
    val bmpArray = arrayListOf<Bitmap>()
    // ビットマップリサイクル配列
    val bmpRecycleDoneArray = arrayListOf<Boolean>(false,false)

    // テクスチャ配列
    val textures = IntArray(2)

    // 回転スイッチ
    var rotateSwitch = false

    // クォータニオン
    var xQuaternion = MyQuaternion().identity()

    // ビルボード(有効/無効)
    var isBillBoard = false


    override fun onDrawFrame(gl: GL10?) {
        // canvasを初期化
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        // クォータニオンを行列に適用
        var matQ = xQuaternion.toMatIV()

        // カメラの位置
        // ビュー座標変換行列
        Matrix.setLookAtM(matV, 0,
                vecEye[0], vecEye[1], vecEye[2],
                vecCenter[0], vecCenter[1], vecCenter[2],
                vecEyeUp[0], vecEyeUp[1], vecEyeUp[2])
        // ビルボード用のビュー座標変換行列
        Matrix.setLookAtM(matI, 0,
                vecCenter[0], vecCenter[1], vecCenter[2],
                vecEye[0], vecEye[1], vecEye[2],
                vecEyeUp[0], vecEyeUp[1], vecEyeUp[2])
        // ビュー座標変換行列にクォータニオンの回転を適用
        Matrix.multiplyMM(matV,0,matV,0,matQ,0)
        Matrix.multiplyMM(matI,0,matI,0,matQ,0)

        // ビルボード用ビュー行列の逆行列を取得
        Matrix.invertM(matI,0,matI,0)

        // ビュー×プロジェクション
        Matrix.perspectiveM(matP,0,60f,ratio,0.1f,100f)
        Matrix.multiplyMM(matT,0,matP,0,matV,0)

        // フロア用テクスチャをバインド
        drawObj.activateTexture(1,textures,bmpArray[1],bmpRecycleDoneArray[1])
        bmpRecycleDoneArray[1] = true

        // フロアのレンダリング
        Matrix.setIdentityM(matM,0)
        Matrix.rotateM(matM,0,90f,1f,0f,0f)
        Matrix.scaleM(matM,0,3f,3f,1f)
        Matrix.multiplyMM(matMVP,0,matT,0,matM,0)
        drawObj.draw(programHandle,matMVP,1)

        // ビルボード用テクスチャをバインド
        drawObj.activateTexture(0,textures,bmpArray[0],bmpRecycleDoneArray[0])
        bmpRecycleDoneArray[0] = true

        // ビルボードのレンダリング
        Matrix.setIdentityM(matM,0)
        Matrix.translateM(matM,0,0f,1f,0f)
        if (isBillBoard) {
            Matrix.multiplyMM(matM,0,matM,0,matI,0)
        }
        Matrix.multiplyMM(matMVP,0,matT,0,matM,0)
        drawObj.draw(programHandle,matMVP,0)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)

        ratio = width.toFloat()/height.toFloat()
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        // canvasを初期化する色を設定する
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)

        // canvasを初期化する際の深度を設定する
        GLES20.glClearDepthf(1f)

        // カリングと深度テストを有効にする
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)
        GLES20.glDepthFunc(GLES20.GL_LEQUAL)
        //GLES20.glEnable(GLES20.GL_CULL_FACE)
        GLES20.glEnable(GLES20.GL_BLEND)

        // ブレンドファクター
        GLES20.glBlendFuncSeparate(GLES20.GL_SRC_ALPHA,GLES20.GL_ONE_MINUS_SRC_ALPHA,GLES20.GL_ONE,GLES20.GL_ONE)

        // シェーダプログラム登録
        programHandle = W035Shader().loadShader()

        // テクスチャ作成し、idをtexturesに保存
        GLES20.glGenTextures(2,textures,0)
        MyGLFunc.checkGlError("glGenTextures")

        // モデル生成
        drawObj = W035Model()

        /*
        drawObj.activateTexture(0,textures,bmpArray[0])
        drawObj.activateTexture(1,textures,bmpArray[1])
        */

        // ----------------------------------
        // 単位行列化
        // ----------------------------------
        // モデル変換行列
        Matrix.setIdentityM(matM,0)
        // モデル変換行列の逆行列
        Matrix.setIdentityM(matI,0)
        // ビュー変換行列
        Matrix.setIdentityM(matV,0)
        // プロジェクション変換行列
        Matrix.setIdentityM(matP,0)
        // モデル・ビュー・プロジェクション行列
        Matrix.setIdentityM(matMVP,0)
        // テンポラリ行列
        Matrix.setIdentityM(matT,0)
    }

    fun receiveTouch(ev: MotionEvent, w: Int, h: Int ) {
        var wh = 1f/ sqrt((w*w+h*h).toFloat())
        // canvasの中心点からみたタッチ点の相対位置
        var x = ev.x - w.toFloat()*0.5f
        var y = ev.y - h.toFloat()*0.5f
        var sq = sqrt(x*x+y*y)
        //var r = sq*2f*PI.toFloat()*wh
        // 回転角
        var r = sq*wh*360f
        if (sq != 1f) {
            sq = 1f/sq
            x *= sq
            y *= sq
        }
        xQuaternion = MyQuaternion.rotate(r, floatArrayOf(y,x,0f))
    }
}