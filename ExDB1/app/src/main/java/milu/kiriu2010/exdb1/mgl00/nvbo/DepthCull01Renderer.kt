package milu.kiriu2010.exdb1.mgl00.nvbo

import android.content.Context
import android.graphics.BitmapFactory
import android.opengl.GLES20
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import android.opengl.Matrix
import milu.kiriu2010.exdb1.R
import milu.kiriu2010.gui.basic.MyGLES20Func
import milu.kiriu2010.gui.model.*
import milu.kiriu2010.gui.renderer.MgRenderer
import milu.kiriu2010.gui.renderer.Tetrahedron01Model
import milu.kiriu2010.gui.shader.es20.nvbo.ES20DirectionalLight01Shader
import milu.kiriu2010.gui.shader.es20.nvbo.ES20Simple01Shader
import milu.kiriu2010.gui.shader.es20.nvbo.ES20Texture01Shader
import milu.kiriu2010.math.MyMathUtil


class DepthCull01Renderer(val modelID: Int,ctx: Context): MgRenderer(ctx) {

    // 描画モデル
    private lateinit var model: MgModelAbs
    // 座標軸モデル
    private lateinit var axisModel: MgModelAbs

    // シェーダ(特殊効果なし)
    private lateinit var shaderSimple: ES20Simple01Shader
    // シェーダ(平行光源)
    private lateinit var shaderDirectionalLight: ES20DirectionalLight01Shader
    // シェーダ(テクスチャ)
    private lateinit var shaderTexture: ES20Texture01Shader

    private var ratio = 1f

    // テクスチャ配列
    private val textures = IntArray(1)

    // 前回選択したシェーダ
    private var shaderSwitchPrev = 0

    // 1:Perspective
    // 2:Frustum
    var flgPersFrus = 1

    // fovy
    var fov = 45f
    // near
    var near = 1f
    // far
    var far = 20f

    override fun onDrawFrame(gl: GL10) {

        // 深度テスト
        if ( isDepth ) {
            GLES20.glEnable(GLES20.GL_DEPTH_TEST)
            GLES20.glDepthFunc(GLES20.GL_LEQUAL)
        }
        else {
            GLES20.glDisable(GLES20.GL_DEPTH_TEST)
        }

        // カリング
        // カリングをonにすると、奥が広がってるようにみえる。なぜ？
        if ( isCull ) {
            GLES20.glEnable(GLES20.GL_CULL_FACE)
        }
        else {
            GLES20.glDisable(GLES20.GL_CULL_FACE)
        }


        // canvasを初期化
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        // 回転角度
        if ( isRunning ) {
            angle[0] =(angle[0]+1)%360
        }
        val t1 = angle[0].toFloat()

        val x = MyMathUtil.cosf(t1)
        val y = MyMathUtil.sinf(t1)

        // ビュー×プロジェクション
        vecEye = qtnNow.toVecIII(floatArrayOf(0f,0f,10f))
        vecEyeUp = qtnNow.toVecIII(floatArrayOf(0f,1f,0f))
        Matrix.setLookAtM(matV, 0,
                vecEye[0], vecEye[1], vecEye[2],
                vecCenter[0], vecCenter[1], vecCenter[2],
                vecEyeUp[0], vecEyeUp[1], vecEyeUp[2])
        when ( flgPersFrus ) {
            1 -> Matrix.perspectiveM(matP,0,fov,ratio,near,far)
            2 -> Matrix.frustumM(matP,0,-1f,1f,-1f,1f,near,far)
            3 -> Matrix.orthoM(matP,0,-1f,1f,-1f,1f,near,far)
        }

        Matrix.multiplyMM(matVP,0,matP,0,matV,0)

        // モデルを単位行列にする
        Matrix.setIdentityM(matM,0)
        // モデルをZ軸を中心に公転させる
        //Matrix.translateM(matM,0,x,y,0f)
        // モデルをY軸を中心に自転させる
        Matrix.rotateM(matM,0,t1,0f,1f,0f)
        // モデル×ビュー×プロジェクション
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)

        // モデル座標変換行列から逆行列を生成
        Matrix.invertM(matI,0,matM,0)

        // シェーダの選択が変わったらモデルを読み込みなおす
        if ( shaderSwitchPrev != shaderSwitch ) {
            val colorMap: Map<String,Float> =
                    when (shaderSwitch) {
                        // テクスチャ
                        2 -> mapOf(
                                "colorR" to 1f,
                                "colorG" to 1f,
                                "colorB" to 1f,
                                "colorA" to 1f
                        )
                        else -> mapOf(
                                "colorR" to -1f,
                                "colorG" to -1f,
                                "colorB" to -1f,
                                "colorA" to -1f
                        )
                    }
                createModel(colorMap)
        }

        // モデルを描画
        when (shaderSwitch) {
            0 -> shaderSimple.draw(model,matMVP)
            1 -> shaderDirectionalLight.draw(model,matMVP,matI,vecLight)
            2 -> {
                // テクスチャ0をバインド
                GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,textures[0])
                shaderTexture.draw(model,matMVP,0)
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,0)
            }
            3 -> {
                shaderSimple.draw(model,matMVP,GLES20.GL_LINES)
            }
            else -> shaderSimple.draw(model,matMVP)
        }

        // 座標軸を描画
        shaderSimple.draw(axisModel,matMVP,GLES20.GL_LINES)
    }

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)

        ratio = width.toFloat()/height.toFloat()

        //Matrix.perspectiveM(matP,0,45f,ratio,0.1f,100f)
    }

    override fun onSurfaceCreated(gl: GL10, config: EGLConfig?) {
        // canvasを初期化する色を設定する
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)

        // canvasを初期化する際の深度を設定する
        GLES20.glClearDepthf(1f)

        // カリングと深度テストを有効にする
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)
        GLES20.glDepthFunc(GLES20.GL_LEQUAL)
        GLES20.glEnable(GLES20.GL_CULL_FACE)

        // ビットマップをロード
        // wgld.org
        //val bmp0 = BitmapFactory.decodeResource(context.resources, R.drawable.texture_w026)
        // 地球
        val bmp0 = BitmapFactory.decodeResource(context.resources, R.drawable.texture_w40_0)

        // テクスチャ作成し、idをtexturesに保存
        GLES20.glGenTextures(1,textures,0)
        MyGLES20Func.createTexture(0,textures,bmp0)

        // 回転を止めておく
        isRunning = false

        // カメラの位置
        vecEye[0]  = 0f
        vecEye[1]  = 0f
        vecEye[2]  = 10f
        /*
        Matrix.setLookAtM(matV, 0,
                vecEye[0], vecEye[1], vecEye[2],
                vecCenter[0], vecCenter[1], vecCenter[2],
                vecEyeUp[0], vecEyeUp[1], vecEyeUp[2])
                */

        // シェーダ(特殊効果なし)
        shaderSimple = ES20Simple01Shader()
        shaderSimple.loadShader()

        // シェーダ(平行光源)
        shaderDirectionalLight = ES20DirectionalLight01Shader()
        shaderDirectionalLight.loadShader()

        // シェーダ(テクスチャ)
        shaderTexture = ES20Texture01Shader()
        shaderTexture.loadShader()

        // モデル生成
        createModel()

        // 座標軸モデル
        axisModel = Axis01Model()
        axisModel.createPath( mapOf("scale" to 3f))

        // 座標軸モデルの線の太さを設定
        GLES20.glLineWidth(5f)
    }

    // モデル生成
    private fun createModel( colorMap: Map<String,Float> = mapOf(
            "colorR" to -1f,
            "colorG" to -1f,
            "colorB" to -1f,
            "colorA" to -1f
    )) {
        val modelMap = mutableMapOf<String,Float>()
        colorMap.forEach {
            modelMap[it.key] = it.value
        }

        model = when (modelID) {
            0 -> {
                modelMap["pattern"] = 2f
                Tetrahedron01Model()
            }
            1 -> Cube01Model()
            2 -> Octahedron01Model()
            3 -> Dodecahedron01Model()
            4 -> Icosahedron01Model()
            5 -> Sphere01Model()
            6 -> Torus01Model()
            else -> Tetrahedron01Model()
        }
        model.createPath( modelMap )
    }

    override fun setMotionParam(motionParam: MutableMap<String, Float>) {
    }

    override fun closeShader() {
    }
}
