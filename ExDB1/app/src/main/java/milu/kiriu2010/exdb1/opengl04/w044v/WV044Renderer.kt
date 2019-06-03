package milu.kiriu2010.exdb1.opengl04.w044v

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.opengl.GLES20
import android.opengl.Matrix
import milu.kiriu2010.exdb1.R
import milu.kiriu2010.gui.basic.MyGLES20Func
import milu.kiriu2010.gui.model.Cube01Model
import milu.kiriu2010.gui.model.Sphere01Model
import milu.kiriu2010.gui.model.Torus01Model
import milu.kiriu2010.gui.renderer.MgRenderer
import milu.kiriu2010.gui.vbo.es20.ES20VBOAbs
import milu.kiriu2010.gui.vbo.es20.ES20VBOIpnc
import java.nio.ByteBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

// -------------------------------------------
// キューブ環境マッピング
// -------------------------------------------
// https://wgld.org/d/webgl/w044.html
// http://opengles2learning.blogspot.com/2011/06/texturing-cube-different-textures-on.html
// -------------------------------------------
class WV044Renderer(ctx: Context): MgRenderer(ctx) {
    // 描画オブジェクト(立方体)
    private lateinit var modelCube: Cube01Model
    // 描画オブジェクト(球体)
    private lateinit var modelSphere: Sphere01Model
    // 描画オブジェクト(トーラス)
    private lateinit var modelTorus: Torus01Model

    // VBO(立方体)
    private lateinit var boCube: ES20VBOAbs
    // VBO(球体)
    private lateinit var boSphere: ES20VBOAbs
    // VBO(トーラス)
    private lateinit var boTorus: ES20VBOAbs

    // シェーダ
    private lateinit var shader: WV044Shader

    // 画面縦横比
    var ratio: Float = 0f

    // ビットマップ配列
    val bmpArray = arrayListOf<Bitmap>()

    // キューブマップ用のターゲットを格納する配列
    val targetArray = arrayListOf<Int>(
            GLES20.GL_TEXTURE_CUBE_MAP_POSITIVE_X,
            GLES20.GL_TEXTURE_CUBE_MAP_POSITIVE_Y,
            GLES20.GL_TEXTURE_CUBE_MAP_POSITIVE_Z,
            GLES20.GL_TEXTURE_CUBE_MAP_NEGATIVE_X,
            GLES20.GL_TEXTURE_CUBE_MAP_NEGATIVE_Y,
            GLES20.GL_TEXTURE_CUBE_MAP_NEGATIVE_Z
    )

    // テクスチャ配列
    val textures = IntArray(2)

    init {
        // ビットマップをロード
        bmpArray.clear()
        val bmp0 = BitmapFactory.decodeResource(ctx.resources,R.drawable.cube_w44_px)
        val bmp1 = BitmapFactory.decodeResource(ctx.resources,R.drawable.cube_w44_py)
        val bmp2 = BitmapFactory.decodeResource(ctx.resources,R.drawable.cube_w44_pz)
        val bmp3 = BitmapFactory.decodeResource(ctx.resources,R.drawable.cube_w44_nx)
        val bmp4 = BitmapFactory.decodeResource(ctx.resources,R.drawable.cube_w44_ny)
        val bmp5 = BitmapFactory.decodeResource(ctx.resources,R.drawable.cube_w44_nz)
        bmpArray.add(bmp0)
        bmpArray.add(bmp1)
        bmpArray.add(bmp2)
        bmpArray.add(bmp3)
        bmpArray.add(bmp4)
        bmpArray.add(bmp5)
    }

    override fun onDrawFrame(gl: GL10?) {
        // 回転角度
        angle[0] =(angle[0]+1)%360
        angle[1] =(angle[0]+180)%360
        val t0 = angle[0].toFloat()
        val t1 = angle[1].toFloat()

        // canvasを初期化
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
        GLES20.glClearDepthf(1f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        // ビュー×プロジェクション座標変換行列
        vecEye = qtnNow.toVecIII(floatArrayOf(0f,0f,20f))
        vecEyeUp = qtnNow.toVecIII(floatArrayOf(0f,1f,0f))
        Matrix.setLookAtM(matV, 0,
                vecEye[0], vecEye[1], vecEye[2],
                vecCenter[0], vecCenter[1], vecCenter[2],
                vecEyeUp[0], vecEyeUp[1], vecEyeUp[2])
        Matrix.perspectiveM(matP,0,45f,ratio,0.1f,200f)
        Matrix.multiplyMM(matVP,0,matP,0,matV,0)

        // 背景用キューブをレンダリング
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_CUBE_MAP,textures[0])
        Matrix.setIdentityM(matM,0)
        Matrix.scaleM(matM,0,100f,100f,100f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shader.draw(modelCube,boCube,matM,matMVP,vecEye,0,0)

        // 球体をレンダリング
        Matrix.setIdentityM(matM,0)
        Matrix.rotateM(matM,0,t0,0f,0f,1f)
        Matrix.translateM(matM,0,5f,0f,0f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shader.draw(modelSphere,boSphere,matM,matMVP,vecEye,-1,1)

        // トーラスをレンダリング
        Matrix.setIdentityM(matM,0)
        Matrix.rotateM(matM,0,t1,0f,0f,1f)
        Matrix.translateM(matM,0,5f,0f,0f)
        Matrix.rotateM(matM,0,t0,1f,0f,1f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shader.draw(modelTorus,boTorus,matM,matMVP,vecEye,-1,1)
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

        // シェーダプログラム登録
        shader = WV044Shader()
        shader.loadShader()

        // モデル生成(立方体)
        modelCube = Cube01Model()
        modelCube.createPath(mapOf(
                "pattern" to 2f,
                "scale"   to 2f,
                "colorR"  to 1f,
                "colorG"  to 1f,
                "colorB"  to 1f,
                "colorA"  to 1f
        ))

        // モデル生成(球体)
        modelSphere = Sphere01Model()
        modelSphere.createPath(mapOf(
                "row"    to 32f,
                "column" to 32f,
                "radius" to 2.5f,
                "colorR" to 1f,
                "colorG" to 1f,
                "colorB" to 1f,
                "colorA" to 1f
        ))

        // モデル生成(トーラス)
        modelTorus = Torus01Model()
        modelTorus.createPath(mapOf(
                "row"     to 32f,
                "column"  to 32f,
                "iradius" to 1f,
                "oradius" to 2f,
                "colorR"  to 1f,
                "colorG"  to 1f,
                "colorB"  to 1f,
                "colorA"  to 1f
        ))

        // VBO(立方体)
        boCube = ES20VBOIpnc()
        boCube.makeVIBO(modelCube)

        // VBO(球体)
        boSphere = ES20VBOIpnc()
        boSphere.makeVIBO(modelSphere)

        // VBO(トーラス)
        boTorus = ES20VBOIpnc()
        boTorus.makeVIBO(modelTorus)

        // キューブマップを生成
        generateCubeMap()
    }

    private fun generateCubeMap() {
        // テクスチャ作成し、idをtexturesに保存
        GLES20.glGenTextures(1,textures,0)
        MyGLES20Func.checkGlError("glGenTextures")

        // テクスチャをキューブマップ
        GLES20.glBindTexture(GLES20.GL_TEXTURE_CUBE_MAP,textures[0])

        // テクスチャへimageを適用
        bmpArray.forEachIndexed { id, bitmap ->
            //val buffer = ByteBuffer.allocate(bitmap.byteCount)
            val bw = bitmap.width
            val bh = bitmap.height
            val buffer = ByteBuffer.allocateDirect(bw*bh*4)
            bitmap.copyPixelsToBuffer(buffer)
            buffer.position(0)

            GLES20.glTexImage2D(targetArray[id],0,GLES20.GL_RGBA,
                    bw,bh,0,GLES20.GL_RGBA,
                    GLES20.GL_UNSIGNED_BYTE,buffer)
            if ( bitmap.isRecycled == false ) {
                bitmap.recycle()
            }
        }

        // ミニマップを生成
        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_CUBE_MAP)

        // テクスチャのパラメータを設定
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_CUBE_MAP, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_CUBE_MAP, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_CUBE_MAP, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_CUBE_MAP, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE)

        // テクスチャのバインド無効化
        GLES20.glBindTexture(GLES20.GL_TEXTURE_CUBE_MAP,0)
    }

    override fun setMotionParam(motionParam: MutableMap<String, Float>) {
    }

    override fun closeShader() {
        boCube.deleteVIBO()
        boSphere.deleteVIBO()
        boTorus.deleteVIBO()
        shader.deleteShader()

        GLES20.glDeleteTextures(textures.size,textures,0)
    }
}