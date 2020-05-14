package milu.kiriu2010.exdb1.opengl01.noise01

import android.content.Context
import android.graphics.Bitmap
import android.opengl.GLES20
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import android.opengl.Matrix
import android.os.SystemClock
import milu.kiriu2010.gui.basic.MyGLES20Func
import milu.kiriu2010.gui.basic.MyNoiseX
import milu.kiriu2010.gui.model.d2.Board00Model
import milu.kiriu2010.gui.renderer.MgRenderer

// -------------------------------------------
// noise01_ノイズテクスチャ:VBOなし
// パーリンノイズで生成した画像をテクスチャとして貼る
// OpenGL ES 2.0
// -------------------------------------------
class Noise01Renderer(ctx: Context): MgRenderer(ctx) {

    // 描画オブジェクト
    private lateinit var modelBoard: Board00Model

    // シェーダ
    private lateinit var shader: Noise01Shader

    // ビットマップ配列
    val bmpArray = arrayListOf<Bitmap>()

    // テクスチャ配列
    val textures = IntArray(9)

    init {
        // ノイズを生成するビットマップに描く
        createNoise(5,2,0.6f)
        createNoise(5,2,0.5f)
        createNoise(5,2,0.4f)
        createNoise(6,2,0.6f)
        createNoise(6,2,0.5f)
        createNoise(6,2,0.4f)
        createNoise(6,10,0.6f)
        createNoise(6,10,0.5f)
        createNoise(6,10,0.4f)
    }

    // ノイズを生成するビットマップに描く
    private fun createNoise(oct: Int, ofs: Int, per: Float) {
        val noise = MyNoiseX(oct,ofs,per)
        noise.seed = (SystemClock.uptimeMillis()/1000).toInt()
        val size = 64
        val noiseColor = FloatArray(size*size)
        (0 until size).forEach { i ->
            (0 until size).forEach { j ->
                noiseColor[i*size+j] = noise.snoise(i.toFloat(),j.toFloat(),size.toFloat())
                // 上:黒⇒下：白
                //noiseColor[i*size+j] = i.toFloat()/size.toFloat()
            }
        }
        val bmp = noise.createImageGray(size,noiseColor)
        bmpArray.add(bmp)
    }

    override fun onDrawFrame(gl: GL10) {
        // 回転角度
        if ( isRunning ) {
            angle[0] =(angle[0]+1)%360
        }
        val t0 = angle[0].toFloat()

        // canvasを初期化
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
        GLES20.glClearDepthf(1f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        // ビュー×プロジェクション座標変換行列
        Matrix.multiplyMM(matVP,0,matP,0,matV,0)

        // モデル描画
        draw(0,-3f, 3f,t0)
        draw(1,-3f, 0f,t0)
        draw(2,-3f,-3f,t0)
        draw(3, 0f, 3f,t0)
        draw(4, 0f, 0f,t0)
        draw(5, 0f,-3f,t0)
        draw(6, 3f, 3f,t0)
        draw(7, 3f, 0f,t0)
        draw(8, 3f,-3f,t0)
    }

    private fun draw(id: Int,x: Float,y: Float,t: Float) {
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[id])
        Matrix.setIdentityM(matM,0)
        Matrix.translateM(matM,0,x,y,0f)
        Matrix.rotateM(matM,0,t,0f,1f,0f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shader.draw(modelBoard,matMVP,0)
    }

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)

        val ratio = width.toFloat()/height.toFloat()

        Matrix.perspectiveM(matP,0,45f,ratio,0.1f,100f)

        // テクスチャ作成し、idをtexturesに保存
        GLES20.glGenTextures(9,textures,0)
        (0..8).forEach {
            MyGLES20Func.createTexture(it,textures,bmpArray[it])
        }
        MyGLES20Func.checkGlError("glGenTextures")
    }

    override fun onSurfaceCreated(gl: GL10, config: EGLConfig?) {
        // 回転停止
        isRunning = false

        // 深度テストを有効にする
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)
        GLES20.glDepthFunc(GLES20.GL_LEQUAL)

        // シェーダ
        shader = Noise01Shader()
        shader.loadShader()

        // モデル生成
        modelBoard = Board00Model()
        modelBoard.createPath(mapOf(
                "scale"  to 0.9f,
                "colorR" to 1f,
                "colorG" to 1f,
                "colorB" to 1f,
                "colorA" to 1f
        ))

        // カメラの位置
        vecEye[0] =  0f
        vecEye[1] =  0f
        vecEye[2] = 10f

        // ビュー座標変換
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
