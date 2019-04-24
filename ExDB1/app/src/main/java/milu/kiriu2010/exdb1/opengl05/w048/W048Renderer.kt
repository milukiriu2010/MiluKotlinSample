package milu.kiriu2010.exdb1.opengl05.w048

import android.graphics.Bitmap
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.GLUtils
import android.opengl.Matrix
import android.view.MotionEvent
import milu.kiriu2010.gui.basic.MyGLFunc
import milu.kiriu2010.gui.basic.MyQuaternion
import java.lang.RuntimeException
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import kotlin.math.sqrt

// トゥーンレンダリング
// http://opengles2learning.blogspot.com/2011/06/texturing-cube-different-textures-on.html
class W048Renderer: GLSurfaceView.Renderer {
    // 描画オブジェクト(球体)
    private lateinit var drawObjSphere: W048ModelSphere
    // 描画オブジェクト(トーラス)
    private lateinit var drawObjTorus: W048ModelTorus

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
    private val vecLight1 = floatArrayOf(-0.5f,0.5f,0.5f)
    private val vecLight2 = floatArrayOf(-1f,0f,0f)
    // 環境光の色
    private val vecAmbientColor = floatArrayOf(0.1f,0.1f,0.1f,1f)
    // カメラの座標
    private var vecEye = floatArrayOf(0f,0f,20f)
    // カメラの上方向を表すベクトル
    private var vecEyeUp = floatArrayOf(0f,1f,0f)
    // 原点のベクトル
    private val vecCenter = floatArrayOf(0f,0f,0f)

    // 回転スイッチ
    var rotateSwitch = false

    // 回転角度
    private var angle1 = 0
    private var angle2 = 0

    // クォータニオン
    var xQuaternion = MyQuaternion().identity()

    // ビットマップ配列
    val bmpArray = arrayListOf<Bitmap>()

    // テクスチャ配列
    val textures = IntArray(2)

    // レンダリングする幅・高さ
    var renderW = 0
    var renderH = 0

    override fun onDrawFrame(gl: GL10?) {
        // 回転角度
        angle1 =(angle1+2)%360
        angle2 =(angle1+180)%360
        val t1 = angle1.toFloat()
        val t2 = angle2.toFloat()

        // canvasを初期化
        GLES20.glClearColor(0.0f, 0.7f, 0.7f, 1.0f)
        GLES20.glClearDepthf(1f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        // ビュー×プロジェクション座標変換行列
        vecEye = xQuaternion.toVecIII(floatArrayOf(0f,0f,10f))
        vecEyeUp = xQuaternion.toVecIII(floatArrayOf(0f,1f,0f))
        Matrix.setLookAtM(matV, 0,
                vecEye[0], vecEye[1], vecEye[2],
                vecCenter[0], vecCenter[1], vecCenter[2],
                vecEyeUp[0], vecEyeUp[1], vecEyeUp[2])
        Matrix.perspectiveM(matP,0,45f,ratio,0.1f,100f)
        Matrix.multiplyMM(matT,0,matP,0,matV,0)

        // テクスチャのバインド
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[0])

        // -------------------------------------------------------
        // トーラス
        // -------------------------------------------------------
        Matrix.setIdentityM(matM,0)
        Matrix.rotateM(matM,0,t1,0f,1f,1f)
        Matrix.multiplyMM(matMVP,0,matT,0,matM,0)
        Matrix.invertM(matI,0,matM,0)

        // モデルをレンダリング
        GLES20.glCullFace(GLES20.GL_BACK)
        drawObjTorus.draw(programHandle,matMVP,matI,vecLight1,0,0, floatArrayOf(0f,0f,0f,0f))

        // エッジ用モデルをレンダリング
        GLES20.glCullFace(GLES20.GL_FRONT)
        drawObjTorus.draw(programHandle,matMVP,matI,vecLight1,0,1, floatArrayOf(0f,0f,0f,1f))


        // -------------------------------------------------------
        // 球体
        // -------------------------------------------------------
        Matrix.setIdentityM(matM,0)
        Matrix.multiplyMM(matMVP,0,matT,0,matM,0)
        Matrix.invertM(matI,0,matM,0)

        // モデルをレンダリング
        GLES20.glCullFace(GLES20.GL_BACK)
        drawObjSphere.draw(programHandle,matMVP,matI,vecLight1,0,0, floatArrayOf(0f,0f,0f,0f))

        // エッジ用モデルをレンダリング
        GLES20.glCullFace(GLES20.GL_FRONT)
        drawObjSphere.draw(programHandle,matMVP,matI,vecLight1,0,1, floatArrayOf(0f,0f,0f,1f))

    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)

        ratio = width.toFloat()/height.toFloat()

        renderW = width
        renderH = height
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

        // シェーダプログラム登録
        programHandle = W048Shader().loadShader()

        // テクスチャ生成
        loadTexture()

        // モデル生成(球体)
        drawObjSphere = W048ModelSphere()

        // モデル生成(トーラス)
        drawObjTorus = W048ModelTorus()

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

    // テクスチャを生成
    private fun loadTexture() {
        // テクスチャ作成し、idをtexturesに保存
        GLES20.glGenTextures(1,textures,0)
        MyGLFunc.checkGlError("glGenTextures")

        // テクスチャをバインド
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,textures[0])

        // ビットマップをテクスチャに設定
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bmpArray[0], 0)

        // ミニマップを生成
        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D)

        // テクスチャパラメータの設定
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_CUBE_MAP, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_CUBE_MAP, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_CUBE_MAP, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_CUBE_MAP, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE)

        bmpArray[0].recycle()

        // テクスチャのバインドを無効化
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,0)

        if (textures[0] == 0) {
            throw RuntimeException("Error loading texture[${0}]")
        }
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