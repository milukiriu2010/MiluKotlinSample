package milu.kiriu2010.exdb1.es30x02.a09

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
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.IntBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

// -----------------------------------------
// gl_VertexIDとgl_InstanceID
// -----------------------------------------
// https://wgld.org/d/webgl2/w008.html
// -----------------------------------------
class A09Renderer(ctx: Context): MgRenderer(ctx) {
    // 描画オブジェクト(板ポリゴン)
    private var modelBoard = Board00Model()
    // 描画オブジェクト(トーラス)
    private var modelTorus = Torus01Model()
    // 描画オブジェクト(球体)
    private var modelSphere = Sphere01Model()

    // シェーダA
    private lateinit var shaderA: ES30a09ShaderA
    // シェーダB
    private lateinit var shaderB: ES30a09ShaderB
    // シェーダC
    private lateinit var shaderC: ES30a09ShaderC

    // VAO(トーラス)
    private lateinit var vaoTorus: ES30VAOIp
    // VAO(球体)
    private lateinit var vaoSphere: ES30VAOIp
    // VAO(板ポリゴン)
    private lateinit var vaoBoard: ES30VAOIp

    // 画面縦横比
    var ratio: Float = 1f

    // フレームバッファ
    // 0:マスク
    // 1:ブラー
    val bufFrame = IntBuffer.allocate(2)

    // 深度バッファ用レンダ―バッファ
    val bufDepthRender = IntBuffer.allocate(2)

    // フレームバッファ用のテクスチャ
    val frameTexture = IntBuffer.allocate(2)

    // UBOハンドル
    val bufUBO = IntBuffer.allocate(2)

    init {
    }

    override fun onDrawFrame(gl: GL10?) {
        //Log.d(javaClass.simpleName,"onDrawFrame:start")

        // フレームバッファのバインド
        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER,bufFrame[0])

        // フレームバッファを初期化
        GLES30.glClearColor(0.3f,0.3f,0.3f,1f)
        GLES30.glClearDepthf(1f)
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT or GLES30.GL_DEPTH_BUFFER_BIT)

        // ---------------------------
        // 球体を描画(左側)
        // ---------------------------
        GLES30.glViewport(0,0,renderW/2,renderH)
        shaderA.draw(vaoSphere,0.9f)

        // ---------------------------
        // トーラスを描画(左側)
        // ---------------------------
        GLES30.glViewport(renderW/2,0,renderW/2,renderH)
        shaderB.draw(vaoTorus,1.1f)

        // フレームバッファのバインドを解除
        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER,0)

        // canvasを初期化
        GLES30.glClearColor(0f, 0f, 0f, 1f)
        GLES30.glClearDepthf(1f)
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT or GLES30.GL_DEPTH_BUFFER_BIT)

        // フレームバッファをテクスチャとして適用
        GLES30.glActiveTexture(GLES30.GL_TEXTURE0)
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D,frameTexture[0])

        // 板ポリゴンをレンダリング
        shaderC.draw(vaoBoard,0)

        //Log.d(javaClass.simpleName,"onDrawFrame:end")
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        //Log.d(javaClass.simpleName,"onSurfaceChanged:start")

        GLES30.glViewport(0, 0, width, height)

        ratio = width.toFloat()/height.toFloat()

        renderW = width
        renderH = height

        // モデル座標変換行列
        Matrix.rotateM(matM,0,90f,1f,0f,0f)

        // ビュー・プロジェクション座標変換行列
        Matrix.setLookAtM(matV,0,
                vecEye[0],vecEye[1],vecEye[2],
                vecCenter[0],vecCenter[1],vecCenter[2],
                vecEyeUp[0],vecEyeUp[1],vecEyeUp[2])
        Matrix.perspectiveM(matP,0,60f,0.5f,0.1f,15f)
        Matrix.multiplyMM(matVP,0,matP,0,matV,0)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)


        // UBOを生成
        GLES30.glGenBuffers(2,bufUBO)

        // UBO(u_mat)
        val bufMatMVP = ByteBuffer.allocateDirect(matMVP.size*4).run {
            order(ByteOrder.nativeOrder())

            asFloatBuffer().apply {
                put(matMVP)
                position(0)
            }
        }
        GLES30.glBindBuffer(GLES30.GL_UNIFORM_BUFFER,bufUBO[0])
        GLES30.glBufferData(GLES30.GL_UNIFORM_BUFFER,bufMatMVP.capacity()*4,bufMatMVP,GLES30.GL_DYNAMIC_DRAW)
        GLES30.glBindBuffer(GLES30.GL_UNIFORM_BUFFER,0)

        // UBO(u_color)
        val baseColor = floatArrayOf(1f,0.6f,0.1f,1f)
        val bufBaseColor = ByteBuffer.allocateDirect(baseColor.size*4).run {
            order(ByteOrder.nativeOrder())

            asFloatBuffer().apply {
                put(baseColor)
                position(0)
            }
        }
        GLES30.glBindBuffer(GLES30.GL_UNIFORM_BUFFER,bufUBO[1])
        GLES30.glBufferData(GLES30.GL_UNIFORM_BUFFER,bufBaseColor.capacity()*4,bufBaseColor,GLES30.GL_DYNAMIC_DRAW)
        GLES30.glBindBuffer(GLES30.GL_UNIFORM_BUFFER,0)

        // UBOをバインド
        GLES30.glBindBufferBase(GLES30.GL_UNIFORM_BUFFER,0,bufUBO[0])
        GLES30.glBindBufferBase(GLES30.GL_UNIFORM_BUFFER,1,bufUBO[1])

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
        shaderA = ES30a09ShaderA()
        shaderA.loadShader()

        // シェーダB
        shaderB = ES30a09ShaderB()
        shaderB.loadShader()

        // シェーダC
        shaderC = ES30a09ShaderC()
        shaderC.loadShader()

        // モデル生成(板ポリゴン)
        modelBoard.createPath(mapOf(
                "pattern" to 100f
        ))

        // モデル生成(トーラス)
        modelTorus.createPath(mapOf(
                "row"     to 32f,
                "column"  to 32f,
                "iradius" to 0.25f,
                "oradius" to 0.75f
        ))

        // モデル生成(球体)
        modelSphere.createPath(mapOf(
                "row"    to 16f,
                "column" to 16f,
                "radius" to 1f
        ))

        // VAO(トーラス)
        vaoTorus = ES30VAOIp()
        vaoTorus.makeVIBO(modelTorus)
        // VAO(球体)
        vaoSphere = ES30VAOIp()
        vaoSphere.makeVIBO(modelSphere)
        // VAO(板ポリゴン)
        vaoBoard = ES30VAOIp()
        vaoBoard.makeVIBO(modelBoard)


        /*
        // 光源位置
        vecLight[0] = 5f
        vecLight[1] = 2f
        vecLight[2] = 5f
        */

        // 視点座標
        vecEye[0] = 0f
        vecEye[1] = 0f
        vecEye[2] = 3f
    }

    override fun setMotionParam(motionParam: MutableMap<String, Float>) {
    }

    override fun closeShader() {
        vaoTorus.deleteVIBO()
        vaoSphere.deleteVIBO()
        vaoBoard.deleteVIBO()
        shaderA.deleteShader()
        shaderB.deleteShader()

        GLES30.glDeleteTextures(1,frameTexture)
        GLES30.glDeleteRenderbuffers(1,bufDepthRender)
        GLES30.glDeleteFramebuffers(1,bufFrame)
    }
}