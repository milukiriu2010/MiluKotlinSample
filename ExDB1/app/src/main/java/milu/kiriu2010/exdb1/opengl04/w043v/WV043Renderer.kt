package milu.kiriu2010.exdb1.opengl04.w043v

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.opengl.GLES20
import android.opengl.Matrix
import milu.kiriu2010.exdb1.R
import milu.kiriu2010.gui.basic.MyGLES20Func
import milu.kiriu2010.gui.model.Sphere01Model
import milu.kiriu2010.gui.renderer.MgRenderer
import milu.kiriu2010.gui.vbo.es20.ES20VBOAbs
import milu.kiriu2010.gui.vbo.es20.ES20VBOIpnct
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

// -----------------------------------------------------
// 視差マッピング
// -----------------------------------------------------
// 視線や高さを考慮したバンプマッピング
// -----------------------------------------------------
// 視差マッピングを行うためには、
// 法線マップと高さマップの２つのテクスチャが必要
// 高さマップは、画像データに高さデータを格納したもので
// 通常モノクロで扱う
// 黒⇒0(最も低い),白⇒1(最も高い)
// -----------------------------------------------------
// https://wgld.org/d/webgl/w043.html
// -----------------------------------------------------
class WV043Renderer(ctx: Context): MgRenderer(ctx) {

    // 描画オブジェクト(球体)
    private lateinit var modelSphere: Sphere01Model

    // VBO(球体)
    private lateinit var boSphere: ES20VBOAbs
    // シェーダ
    private lateinit var shader: WV043Shader

    // 画面縦横比
    var ratio: Float = 0f

    // ビットマップ配列
    val bmpArray = arrayListOf<Bitmap>()

    // テクスチャ配列
    val textures = IntArray(2)

    // 高さ情報
    var hScale = 0.5f

    init {
        // ビットマップをロード
        bmpArray.clear()
        val bmp0 = BitmapFactory.decodeResource(ctx.resources, R.drawable.texture_w43_0)
        val bmp1 = BitmapFactory.decodeResource(ctx.resources, R.drawable.texture_w43_1)
        bmpArray.add(bmp0)
        bmpArray.add(bmp1)
    }

    override fun onDrawFrame(gl: GL10?) {
        // 回転角度
        angle[0] =(angle[0]+1)%360
        val t0 = angle[0].toFloat()

        // canvasを初期化
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
        GLES20.glClearDepthf(1f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        // ビュー×プロジェクション座標変換行列
        vecEye = qtnNow.toVecIII(floatArrayOf(0f,0f,5f))
        vecEyeUp = qtnNow.toVecIII(floatArrayOf(0f,1f,0f))
        Matrix.setLookAtM(matV, 0,
                vecEye[0], vecEye[1], vecEye[2],
                vecCenter[0], vecCenter[1], vecCenter[2],
                vecEyeUp[0], vecEyeUp[1], vecEyeUp[2])
        Matrix.perspectiveM(matP,0,45f,ratio,0.1f,100f)
        Matrix.multiplyMM(matVP,0,matP,0,matV,0)

        // 球体をレンダリング
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,textures[0])
        GLES20.glActiveTexture(GLES20.GL_TEXTURE1)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,textures[1])
        Matrix.setIdentityM(matM,0)
        Matrix.rotateM(matM,0,-t0,0f,1f,0f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        Matrix.invertM(matI,0,matM,0)
        shader.draw(modelSphere,boSphere,matM,matMVP,matI,vecLight,vecEye,0,1,hScale)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)

        ratio = width.toFloat()/height.toFloat()

        renderW = width
        renderH = height
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        // 深度テストを有効にする
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)
        GLES20.glDepthFunc(GLES20.GL_LEQUAL)

        // シェーダ
        shader = WV043Shader()
        shader.loadShader()

        // モデル生成(球体)
        modelSphere = Sphere01Model()
        modelSphere.createPath(mapOf(
                "row" to 32f,
                "column" to 32f,
                "radius" to 1f
        ))

        // VBO
        boSphere = ES20VBOIpnct()
        boSphere.makeVIBO(modelSphere)

        // テクスチャ作成し、idをtexturesに保存
        GLES20.glGenTextures(2,textures,0)
        MyGLES20Func.checkGlError("glGenTextures")
        // 法線マップテクスチャ
        MyGLES20Func.createTexture(0,textures,bmpArray[0])
        // 高さマップテクスチャ
        MyGLES20Func.createTexture(1,textures,bmpArray[1])

        // 光源位置
        vecLight[0] = -10f
        vecLight[1] = 10f
        vecLight[2] = 10f
        // 視点位置
        vecEye[0] = 0f
        vecEye[1] = 0f
        vecEye[2] = 5f
    }

    override fun setMotionParam(motionParam: MutableMap<String, Float>) {
    }

    override fun closeShader() {
        boSphere.deleteVIBO()
        shader.deleteShader()

        GLES20.glDeleteTextures(textures.size,textures,0)
    }
}