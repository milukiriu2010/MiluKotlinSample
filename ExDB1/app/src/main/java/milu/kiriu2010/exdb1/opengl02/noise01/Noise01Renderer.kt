package milu.kiriu2010.exdb1.opengl01.w019

import android.content.Context
import android.graphics.Bitmap
import android.opengl.GLES20
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import android.opengl.Matrix
import android.os.SystemClock
import milu.kiriu2010.exdb1.opengl02.noise01.Noise01Shader
import milu.kiriu2010.gui.basic.MyGLFunc
import milu.kiriu2010.gui.basic.MyNoiseX
import milu.kiriu2010.gui.model.Board01Model
import milu.kiriu2010.gui.renderer.MgRenderer

// ---------------------------------------------------
// パーリンノイズで生成した画像をテクスチャとして貼る
// ---------------------------------------------------
// https://wgld.org/d/webgl/w026.html
// ---------------------------------------------------
class Noise01Renderer(ctx: Context): MgRenderer(ctx) {

    // 描画オブジェクト
    private lateinit var modelBoard: Board01Model

    // シェーダ
    private lateinit var shader: Noise01Shader

    // ビットマップ配列
    val bmpArray = arrayListOf<Bitmap>()

    // テクスチャ配列
    val textures = IntArray(1)

    init {
        // ノイズを生成するビットマップに描く
        val noise = MyNoiseX(5,2,0.6f)
        noise.seed = (SystemClock.uptimeMillis()/1000).toInt()
        val size = 128
        val noiseColor = FloatArray(size*size)
        (0 until size).forEach { i ->
            (0 until size).forEach { j ->
                noiseColor[i*size+j] = noise.snoise(i.toFloat(),j.toFloat(),size.toFloat())
                // 上:黒⇒下：白
                //noiseColor[i*size+j] = i.toFloat()/size.toFloat()
            }
        }
        val bmp = noise.createImage(size,noiseColor)
        bmpArray.add(0,bmp)
    }

    override fun onDrawFrame(gl: GL10) {

        // canvasを初期化
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        // 回転角度
        if ( isRunning ) {
            angle[0] =(angle[0]+1)%360
        }
        val t0 = angle[0].toFloat()

        // テクスチャをバインドする
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[0])

        // ビュー×プロジェクション座標変換行列
        Matrix.multiplyMM(matVP,0,matP,0,matV,0)

        // モデル座標変換行列の生成
        Matrix.setIdentityM(matM,0)
        Matrix.rotateM(matM,0,t0,0f,1f,0f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)

        // モデル描画
        shader.draw(modelBoard,matMVP,0)
    }

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)

        val ratio = width.toFloat()/height.toFloat()

        Matrix.perspectiveM(matP,0,45f,ratio,0.1f,100f)
    }

    override fun onSurfaceCreated(gl: GL10, config: EGLConfig?) {
        // 回転停止
        isRunning = false

        // 深度テストを有効にする
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)
        GLES20.glDepthFunc(GLES20.GL_LEQUAL)

        // シェーダプログラム登録
        shader = Noise01Shader()
        shader.loadShader()

        // モデル生成
        modelBoard = Board01Model()
        modelBoard.createPath(mapOf(
                "colorR" to 1f,
                "colorG" to 1f,
                "colorB" to 1f,
                "colorA" to 1f
        ))

        // テクスチャ作成し、idをtexturesに保存
        GLES20.glGenTextures(1,textures,0)
        MyGLFunc.createTexture(0,textures,bmpArray[0])
        MyGLFunc.checkGlError("glGenTextures")

        // カメラの位置
        Matrix.setLookAtM(matV, 0,
                vecEye[0], vecEye[1], vecEye[2],
                vecCenter[0], vecCenter[1], vecCenter[2],
                vecEyeUp[0], vecEyeUp[1], vecEyeUp[2])
    }

    override fun setMotionParam(motionParam: MutableMap<String, Float>) {
    }

    override fun closeShader() {
    }
}
