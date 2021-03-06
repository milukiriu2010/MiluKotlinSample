package milu.kiriu2010.exdb1.es30x02.a08

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.opengl.GLES30
import android.opengl.Matrix
import milu.kiriu2010.exdb1.R
import milu.kiriu2010.gui.basic.MyGLES30Func
import milu.kiriu2010.gui.model.d2.Board00Model
import milu.kiriu2010.gui.model.Sphere01Model
import milu.kiriu2010.gui.model.Torus01Model
import milu.kiriu2010.gui.renderer.MgRenderer
import milu.kiriu2010.gui.vbo.es30.ES30VAOIp
import milu.kiriu2010.gui.vbo.es30.ES30VAOIpnto
import java.nio.IntBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

// -----------------------------------------
// gl_VertexIDとgl_InstanceID
// OpenGL ES 3.0
// -----------------------------------------
// https://wgld.org/d/webgl2/w008.html
// -----------------------------------------
class A08Renderer(ctx: Context): MgRenderer(ctx) {
    // 描画オブジェクト(板ポリゴン)
    private var modelBoard = Board00Model()
    // 描画オブジェクト(トーラス)
    private var modelTorus = Torus01Model()
    // 描画オブジェクト(球体)
    private var modelSphere = Sphere01Model()

    // シェーダA
    private lateinit var shaderA: ES30a08ShaderA
    // シェーダB
    private lateinit var shaderB: ES30a08ShaderB

    // VAO(トーラス)
    private lateinit var vaoTorus: ES30VAOIpnto
    // VAO(球体)
    private lateinit var vaoSphere: ES30VAOIpnto
    // VAO(板ポリゴン)
    private lateinit var vaoBoard: ES30VAOIp

    // 画面縦横比
    var ratio: Float = 1f

    // ビットマップ配列
    val bmpArray = arrayListOf<Bitmap>()

    // テクスチャ配列
    val textures = IntArray(2)

    // フレームバッファ
    // 0:マスク
    // 1:ブラー
    val bufFrame = IntBuffer.allocate(2)

    // 深度バッファ用レンダ―バッファ
    val bufDepthRender = IntBuffer.allocate(2)

    // フレームバッファ用のテクスチャ
    val frameTexture = IntBuffer.allocate(2)

    init {
        // ビットマップをロード
        bmpArray.clear()
        val bmp0 = BitmapFactory.decodeResource(ctx.resources, R.drawable.texture_lenna_a03)
        bmpArray.add(bmp0)
    }

    override fun onDrawFrame(gl: GL10?) {
        //Log.d(javaClass.simpleName,"onDrawFrame:start")

        angle[0] =(angle[0]+1)%360
        val t0 = angle[0].toFloat()

        // ビュー×プロジェクション座標変換行列
        vecEye = qtnNow.toVecIII(floatArrayOf(0f,0f,5f))
        vecEyeUp = qtnNow.toVecIII(floatArrayOf(0f,1f,0f))
        Matrix.setLookAtM(matV, 0,
                vecEye[0], vecEye[1], vecEye[2],
                vecCenter[0], vecCenter[1], vecCenter[2],
                vecEyeUp[0], vecEyeUp[1], vecEyeUp[2])
        Matrix.perspectiveM(matP,0,60f,ratio,0.1f,25f)
        Matrix.multiplyMM(matVP,0,matP,0,matV,0)

        // フレームバッファのバインド
        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER,bufFrame[0])

        // フレームバッファを初期化
        GLES30.glClearColor(0.3f,0.3f,0.3f,1f)
        GLES30.glClearDepthf(1f)
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT or GLES30.GL_DEPTH_BUFFER_BIT)

        // テクスチャの適用
        GLES30.glActiveTexture(GLES30.GL_TEXTURE0)
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D,textures[0])

        // 球体をレンダリング
        val matN = FloatArray(16)
        Matrix.setIdentityM(matM,0)
        Matrix.rotateM(matM,0,t0,0f,1f,0f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        Matrix.invertM(matI,0,matM,0)
        Matrix.transposeM(matN,0,matI,0)
        shaderA.draw(vaoSphere,matM,matMVP,matN,vecLight,vecEye,0)

        // トーラスをレンダリング
        shaderA.draw(vaoTorus,matM,matMVP,matN,vecLight,vecEye,0)

        // フレームバッファのバインドを解除
        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER,0)

        // canvasを初期化
        GLES30.glClearColor(0f, 0f, 0f, 1f)
        GLES30.glClearDepthf(1f)
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT or GLES30.GL_DEPTH_BUFFER_BIT)

        // フレームバッファをテクスチャとして適用
        GLES30.glActiveTexture(GLES30.GL_TEXTURE1)
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D,frameTexture[0])

        // 板ポリゴンをレンダリング
        shaderB.draw(vaoBoard,1)

        //Log.d(javaClass.simpleName,"onDrawFrame:end")
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        //Log.d(javaClass.simpleName,"onSurfaceChanged:start")

        GLES30.glViewport(0, 0, width, height)

        ratio = width.toFloat()/height.toFloat()

        renderW = width
        renderH = height

        // テクスチャを作成
        GLES30.glGenTextures(1,textures,0)
        // テクスチャに使う画像をロード
        MyGLES30Func.createTexture(0,textures,bmpArray[0])

        // フレームバッファ生成
        GLES30.glGenFramebuffers(1,bufFrame)
        // レンダ―バッファ生成
        GLES30.glGenRenderbuffers(1,bufDepthRender)
        // フレームバッファを格納するテクスチャ生成
        GLES30.glGenTextures(1,frameTexture)
        MyGLES30Func.createFrameBuffer(renderW,renderH,0,bufFrame,bufDepthRender,frameTexture)

        //Log.d(javaClass.simpleName,"onSurfaceChanged:end")
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        // カリングと深度テストを有効にする
        GLES30.glEnable(GLES30.GL_DEPTH_TEST)
        GLES30.glDepthFunc(GLES30.GL_LEQUAL)
        GLES30.glEnable(GLES30.GL_CULL_FACE)

        // シェーダA
        shaderA = ES30a08ShaderA()
        shaderA.loadShader()

        // シェーダB
        shaderB = ES30a08ShaderB()
        shaderB.loadShader()

        // モデルにオフセットをセット
        (0..8).forEach {
            val off = (it-4).toFloat()
            modelTorus.datOff.addAll(arrayListOf(off,off,off))
            modelSphere.datOff.addAll(arrayListOf(off,off,off))
        }

        // モデル生成(板ポリゴン)
        modelBoard.createPath(mapOf(
                "pattern" to 100f
        ))

        // モデル生成(トーラス)
        modelTorus.createPath(mapOf(
                "row"     to 32f,
                "column"  to 32f,
                "iradius" to 0.1f,
                "oradius" to 0.5f
        ))

        // モデル生成(球体)
        modelSphere.createPath(mapOf(
                "row"    to 16f,
                "column" to 16f,
                "radius" to 0.3f
        ))

        // VAO(トーラス)
        vaoTorus = ES30VAOIpnto()
        vaoTorus.makeVIBO(modelTorus)
        // VAO(球体)
        vaoSphere = ES30VAOIpnto()
        vaoSphere.makeVIBO(modelSphere)
        // VAO(板ポリゴン)
        vaoBoard = ES30VAOIp()
        vaoBoard.makeVIBO(modelBoard)

        // 光源位置
        vecLight[0] = 5f
        vecLight[1] = 2f
        vecLight[2] = 5f
    }

    override fun setMotionParam(motionParam: MutableMap<String, Float>) {
    }

    override fun closeShader() {
        vaoTorus.deleteVIBO()
        vaoSphere.deleteVIBO()
        vaoBoard.deleteVIBO()
        shaderA.deleteShader()
        shaderB.deleteShader()

        GLES30.glDeleteTextures(textures.size,textures,0)
        GLES30.glDeleteTextures(1,frameTexture)
        GLES30.glDeleteRenderbuffers(1,bufDepthRender)
        GLES30.glDeleteFramebuffers(1,bufFrame)
    }
}