package milu.kiriu2010.exdb1.opengl03.w033

import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import android.os.SystemClock
import android.util.Log
import android.view.MotionEvent
import milu.kiriu2010.gui.basic.MyQuaternion
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

// 平行光源
class W033Renderer: GLSurfaceView.Renderer {
    // 描画オブジェクト
    private lateinit var drawObj: W033Model

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
    private val vecAmbientColor = floatArrayOf(0.1f,0.1f,0.1f,1f)
    // カメラの座標
    private val vecEye = floatArrayOf(0f,0f,10f)
    // カメラの上方向を表すベクトル
    private val vecEyeUp = floatArrayOf(0f,1f,0f)

    // クォータニオン
    private var xQuaternion = MyQuaternion().identity()

    // X軸
    private var xaxis = floatArrayOf(1f,0f,0f)

    // 回転角度
    private var angle1 = 0
    private var angle2 = 0

    // 回転スイッチ
    var rotateSwitch = false
    // タッチしたとき発生するイベント
    //var event: MotionEvent? = null

    override fun onDrawFrame(gl: GL10?) {
        // canvasを初期化
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        // 回転角度
        angle1 =(angle1+5)%360
        angle2 =(angle2+2)%360
        val t1 = angle1.toFloat()
        val t2 = angle2.toFloat()

        // クォータニオンを行列に適用する
        val matQ = xQuaternion.toMatIV()
        //Log.d(javaClass.simpleName,"0[${matQ[0]}}1[${matQ[1]}}2[${matQ[2]}}3[${matQ[3]}}")

        // ビュー×プロジェクション
        Matrix.multiplyMM(matT,0,matP,0,matV,0)


        // モデルを単位行列にする
        Matrix.setIdentityM(matM,0)
        Matrix.multiplyMM(matM,0,matQ,0,matM,0)
        // モデルを"Y軸"を中心に回転する
        Matrix.rotateM(matM, 0, t1, 0f, 1f, 0f)
        // モデルを"Y軸45度/Z軸45度"を中心に回転する
        //Matrix.rotateM(matM,0,angle,0f,1f,1f)
        // モデルを"X軸45度Y軸45度/Z軸45度"を中心に回転する
        //Matrix.rotateM(matM,0,t1,1f,1f,1f)
        // モデル×ビュー×プロジェクション
        Matrix.multiplyMM(matMVP,0,matT,0,matM,0)

        // モデル座標変換行列から逆行列を生成
        Matrix.invertM(matI,0,matM,0)

        // モデルを描画
        drawObj.draw(matMVP,matM,matI,vecLight,vecEye,vecAmbientColor)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)

        ratio = width.toFloat()/height.toFloat()


        Matrix.perspectiveM(matP,0,60f,ratio,0.1f,100f)
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        // canvasを初期化する色を設定する
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)

        // canvasを初期化する際の深度を設定する
        GLES20.glClearDepthf(1f)

        // カリングと深度テストを有効にする
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)
        GLES20.glDepthFunc(GLES20.GL_LEQUAL)
        GLES20.glEnable(GLES20.GL_CULL_FACE)

        // カメラの位置
        Matrix.setLookAtM(matV, 0,
                vecEye[0], vecEye[1], vecEye[2],
                0f, 0f, 0f,
                vecEyeUp[0], vecEyeUp[1], vecEyeUp[2])

        drawObj = W033Model()
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