package milu.kiriu2010.exdb1.opengl03.w039

import android.content.Context
import android.graphics.Bitmap
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import android.view.MotionEvent
import milu.kiriu2010.gui.basic.MyGLFunc
import milu.kiriu2010.gui.basic.MyQuaternion
import milu.kiriu2010.gui.renderer.MgRenderer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import kotlin.math.sqrt

// 平行光源
class W039Renderer(ctx: Context): MgRenderer(ctx) {

    // 描画オブジェクト(トーラス)
    private lateinit var drawObjTorus: W039ModelTorus
    // 描画オブジェクト(球体)
    private lateinit var drawObjSphere: W039ModelSphere

    // プログラムハンドル
    private var programHandle: Int = 0

    // 画面縦横比
    var ratio: Float = 0f

    // ビットマップ配列
    val bmpArray = arrayListOf<Bitmap>()

    // テクスチャ配列
    val textures = IntArray(2)

    override fun onDrawFrame(gl: GL10?) {
        // canvasを初期化
        GLES20.glClearColor(0f, 0f, 0f, 1.0f)
        GLES20.glClearDepthf(1f)
        GLES20.glClearStencil(0)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT or GLES20.GL_STENCIL_BUFFER_BIT)

        // 回転角度
        angle[0] =(angle[0]+1)%360
        val t1 = angle[0].toFloat()

        // クォータニオンを行列に適用
        var matQ = qtnNow.toMatIV()

        // カメラの位置
        // ビュー座標変換行列
        Matrix.setLookAtM(matV, 0,
                vecEye[0], vecEye[1], vecEye[2],
                vecCenter[0], vecCenter[1], vecCenter[2],
                vecEyeUp[0], vecEyeUp[1], vecEyeUp[2])
        // ビュー×プロジェクション
        Matrix.perspectiveM(matP,0,45f,ratio,0.1f,100f)
        // ビュー座標変換行列にクォータニオンの回転を適用
        Matrix.multiplyMM(matV,0,matV,0,matQ,0)
        Matrix.multiplyMM(matVP,0,matP,0,matV,0)

        // テクスチャ0をバインド
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,textures[0])

        // ステンシルテストを有効にする
        GLES20.glEnable(GLES20.GL_STENCIL_TEST)

        // カラーバッファと深度バッファへ描画されないようにする
        GLES20.glColorMask(false,false,false,false)
        GLES20.glDepthMask(false)

        // -----------------------------------------------
        // トーラス(シルエット)用ステンシル設定
        // -----------------------------------------------
        // トーラス(シルエット)が描画されたところの
        // 基準値が１に設定される
        // -----------------------------------------------
        GLES20.glStencilFunc(GLES20.GL_ALWAYS,1, 0.inv())
        GLES20.glStencilOp(GLES20.GL_KEEP,GLES20.GL_REPLACE,GLES20.GL_REPLACE)

        // トーラス(シルエット)をレンダリング
        //   ライティング:OFF
        //   アウトライン:ON
        //   テクスチャ  :OFF
        Matrix.setIdentityM(matM,0)
        Matrix.rotateM(matM,0,t1,0f,1f,1f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        drawObjTorus.draw(programHandle,matMVP,matI,vecLight,0,1,0,0)

        // カラーバッファと深度バッファへ描画されるようにする
        GLES20.glColorMask(true,true,true,true)
        GLES20.glDepthMask(true)

        // -----------------------------------------------
        // 球体モデル用ステンシル設定
        // -----------------------------------------------
        // ステンシルテストで基準値が０のところだけ
        // レンダリングが行われる
        // -----------------------------------------------
        GLES20.glStencilFunc(GLES20.GL_EQUAL,0, 0.inv())
        GLES20.glStencilOp(GLES20.GL_KEEP,GLES20.GL_KEEP,GLES20.GL_KEEP)

        // 球体(背景)をレンダリング
        //   ライティング:OFF
        //   アウトライン:OFF
        //   テクスチャ  :ON
        Matrix.setIdentityM(matM,0)
        Matrix.scaleM(matM,0,50f,50f,50f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        drawObjSphere.draw(programHandle,matMVP,matI,vecLight,0,0,0,1)

        // ステンシルテストを無効にする
        GLES20.glDisable(GLES20.GL_STENCIL_TEST)

        // トーラスをレンダリング
        //   ライティング:ON
        //   アウトライン:OFF
        //   テクスチャ  :OFF
        Matrix.setIdentityM(matM,0)
        Matrix.rotateM(matM,0,t1,0f,1f,1f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        drawObjTorus.draw(programHandle,matMVP,matI,vecLight,1,0,0,0)
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

        GLES20.glClearStencil(0)

        // 深度テストを有効にする
        // 球体(背景)を内側から見るようにしているため、カリングをOFFにしている
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)
        GLES20.glDepthFunc(GLES20.GL_LEQUAL)

        // シェーダプログラム登録
        programHandle = W039Shader().loadShader()

        // テクスチャ作成し、idをtexturesに保存
        GLES20.glGenTextures(1,textures,0)
        MyGLFunc.checkGlError("glGenTextures")

        // モデル生成(球体)(背景)
        drawObjSphere = W039ModelSphere()

        // モデル生成(トーラス)(本体とアウトライン)
        drawObjTorus = W039ModelTorus()

        // テクスチャ0をバインド
        drawObjSphere.activateTexture(0,textures,bmpArray[0])

        // 光源位置
        vecLight[0] = 1f
        vecLight[1] = 1f
        vecLight[2] = 1f
        // 視点位置
        vecEye[0] = 0f
        vecEye[1] = 0f
        vecEye[2] = 10f

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
        Matrix.setIdentityM(matVP,0)
    }

    override fun setMotionParam(motionParam: MutableMap<String, Float>) {
    }

    override fun closeShader() {
    }
}