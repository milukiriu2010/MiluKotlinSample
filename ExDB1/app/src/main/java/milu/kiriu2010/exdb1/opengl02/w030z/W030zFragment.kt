package milu.kiriu2010.exdb1.opengl02.w030z

import android.content.Intent
import android.opengl.GLES20
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*

import milu.kiriu2010.exdb1.R
import milu.kiriu2010.gui.view.MyGLES20View

class W030zFragment : Fragment() {

    private lateinit var scrollViewW030y: ScrollView

    private lateinit var myGLES20View: MyGLES20View

    private lateinit var renderer: W030zRenderer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_open_gl_w30z, container, false)

        myGLES20View = view.findViewById(R.id.myGLES20ViewW30)
        renderer = W030zRenderer(context!!)
        myGLES20View.setRenderer(renderer)
        myGLES20View.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                    renderer.isRunning = false
                }
                MotionEvent.ACTION_DOWN -> {
                    Log.d(javaClass.simpleName,"ex[${event.x}]ey[${event.y}]")
                    Log.d(javaClass.simpleName,"vw[${myGLES20View.width}]vh[${myGLES20View.height}]")
                    renderer.isRunning = true
                    renderer.receiveTouch(event,myGLES20View.width,myGLES20View.height)
                }
                MotionEvent.ACTION_MOVE -> {
                    renderer.receiveTouch(event,myGLES20View.width,myGLES20View.height)
                }
                else -> {
                }
            }
            true
        }

        // コンテキストの色を変更するダイアログを開く
        val btnW030ContextClearColor = view.findViewById<Button>(R.id.btnW030ContextClearColor)
        btnW030ContextClearColor.setOnClickListener {
            val bundle = Bundle().also {
                it.putFloat("RED"  ,renderer.contextColor[0])
                it.putFloat("GREEN",renderer.contextColor[1])
                it.putFloat("BLUE" ,renderer.contextColor[2])
                it.putFloat("ALPHA",renderer.contextColor[3])
            }

            val dlg = W030zContextDialog.newInstance(bundle)
            dlg.setTargetFragment(this,1)
            dlg.show(fragmentManager,"context clear color")
        }

        // ブレンドの色を変更するダイアログを開く
        val btnW030BlendConstantColor = view.findViewById<Button>(R.id.btnW030BlendConstantColor)
        btnW030BlendConstantColor.setOnClickListener {
            val bundle = Bundle().also {
                it.putFloat("RED"  ,renderer.blendColor[0])
                it.putFloat("GREEN",renderer.blendColor[1])
                it.putFloat("BLUE" ,renderer.blendColor[2])
                it.putFloat("ALPHA",renderer.blendColor[3])
            }

            val dlg = W030zBlendDialog.newInstance(bundle)
            dlg.setTargetFragment(this,2)
            dlg.show(fragmentManager,"blend constant color")
        }

        // パラメータを変更するダイアログを開く(テクスチャ)
        val btnW030Model1 = view.findViewById<Button>(R.id.btnW030Model1)
        btnW030Model1.setOnClickListener {
            val bundle = Bundle().also {
                // リクエストコード
                it.putInt("REQ_CODE", 3)
                // ブレンド
                it.putBoolean("BLEND", renderer.blend[0] )
                // アルファ成分
                it.putFloat("VERTEX_ALPHA", renderer.vertexAplha[0] )
                // 方程式(カラー)
                it.putInt("EQUATION_COLOR", renderer.equationColor[0] )
                // 方程式(アルファ)
                it.putInt("EQUATION_ALPHA", renderer.equationAlpha[0] )
                // ブレンドファクター(カラー元)
                it.putInt("BLEND_FCT_SCBF", renderer.blendFctSCBF[0] )
                // ブレンドファクター(カラー先)
                it.putInt("BLEND_FCT_DCBF", renderer.blendFctDCBF[0] )
                // ブレンドファクター(アルファ元)
                it.putInt("BLEND_FCT_SABF", renderer.blendFctSABF[0] )
                // ブレンドファクター(アルファ先)
                it.putInt("BLEND_FCT_DABF", renderer.blendFctDABF[0] )
            }

            val dlg = W030zModelDialog.newInstance(bundle)
            dlg.setTargetFragment(this,3)
            dlg.show(fragmentManager,"Model1")
        }

        // パラメータを変更するダイアログを開く(板ポリゴン)
        val btnW030Model2 = view.findViewById<Button>(R.id.btnW030Model2)
        btnW030Model2.setOnClickListener {
            val bundle = Bundle().also {
                // リクエストコード
                it.putInt("REQ_CODE", 4)
                // ブレンド
                it.putBoolean("BLEND", renderer.blend[1] )
                // アルファ成分
                it.putFloat("VERTEX_ALPHA", renderer.vertexAplha[1] )
                // 方程式(カラー)
                it.putInt("EQUATION_COLOR", renderer.equationColor[1] )
                // 方程式(アルファ)
                it.putInt("EQUATION_ALPHA", renderer.equationAlpha[1] )
                // ブレンドファクター(カラー元)
                it.putInt("BLEND_FCT_SCBF", renderer.blendFctSCBF[1] )
                // ブレンドファクター(カラー先)
                it.putInt("BLEND_FCT_DCBF", renderer.blendFctDCBF[1] )
                // ブレンドファクター(アルファ元)
                it.putInt("BLEND_FCT_SABF", renderer.blendFctSABF[1] )
                // ブレンドファクター(アルファ先)
                it.putInt("BLEND_FCT_DABF", renderer.blendFctDABF[1] )
            }

            val dlg = W030zModelDialog.newInstance(bundle)
            dlg.setTargetFragment(this,4)
            dlg.show(fragmentManager,"Model2")
        }

        return view
    }

    // ダイアログ上での変更結果をレンダリングに反映
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        when (requestCode) {
            // コンテキストの色
            1 -> {
                Log.d(javaClass.simpleName,"onActivityResult:${requestCode}")

                val red   = data.getFloatExtra("RED",0f)
                val green = data.getFloatExtra("GREEN",0f)
                val blue  = data.getFloatExtra("BLUE",0f)
                val alpha = data.getFloatExtra("ALPHA",0f)

                renderer.contextColor[0] = red
                renderer.contextColor[1] = green
                renderer.contextColor[2] = blue
                renderer.contextColor[3] = alpha
            }
            // ブレンドの色
            2 -> {
                Log.d(javaClass.simpleName,"onActivityResult:${requestCode}")

                val red   = data.getFloatExtra("RED",0f)
                val green = data.getFloatExtra("GREEN",0f)
                val blue  = data.getFloatExtra("BLUE",0f)
                val alpha = data.getFloatExtra("ALPHA",0f)

                renderer.blendColor[0] = red
                renderer.blendColor[1] = green
                renderer.blendColor[2] = blue
                renderer.blendColor[3] = alpha
            }
            // パラメータを変更するダイアログを開く(テクスチャ)
            3 -> {
                Log.d(javaClass.simpleName,"onActivityResult:${requestCode}")
                // ブレンド
                renderer.blend[0] = data.getBooleanExtra("BLEND", false )
                // アルファ成分
                renderer.vertexAplha[0] = data.getFloatExtra("VERTEX_ALPHA", 0f )
                // 方程式(カラー)
                renderer.equationColor[0] = data.getIntExtra("EQUATION_COLOR", GLES20.GL_FUNC_ADD )
                // 方程式(アルファ)
                renderer.equationAlpha[0] = data.getIntExtra("EQUATION_ALPHA", GLES20.GL_FUNC_ADD )
                // ブレンドファクター(カラー元)
                renderer.blendFctSCBF[0] = data.getIntExtra("BLEND_FCT_SCBF", GLES20.GL_ONE )
                // ブレンドファクター(カラー先)
                renderer.blendFctDCBF[0] = data.getIntExtra("BLEND_FCT_DCBF", GLES20.GL_ONE )
                // ブレンドファクター(アルファ元)
                renderer.blendFctSABF[0] = data.getIntExtra("BLEND_FCT_SABF", GLES20.GL_ONE )
                // ブレンドファクター(アルファ先)
                renderer.blendFctDABF[0] = data.getIntExtra("BLEND_FCT_DABF", GLES20.GL_ONE )
            }
            // パラメータを変更するダイアログを開く(板ポリゴン)
            4 -> {
                Log.d(javaClass.simpleName,"onActivityResult:${requestCode}")
                // ブレンド
                renderer.blend[1] = data.getBooleanExtra("BLEND", false )
                // アルファ成分
                renderer.vertexAplha[1] = data.getFloatExtra("VERTEX_ALPHA", 0f )
                // 方程式(カラー)
                renderer.equationColor[1] = data.getIntExtra("EQUATION_COLOR", GLES20.GL_FUNC_ADD )
                // 方程式(アルファ)
                renderer.equationAlpha[1] = data.getIntExtra("EQUATION_ALPHA", GLES20.GL_FUNC_ADD )
                // ブレンドファクター(カラー元)
                renderer.blendFctSCBF[1] = data.getIntExtra("BLEND_FCT_SCBF", GLES20.GL_ONE )
                // ブレンドファクター(カラー先)
                renderer.blendFctDCBF[1] = data.getIntExtra("BLEND_FCT_DCBF", GLES20.GL_ONE )
                // ブレンドファクター(アルファ元)
                renderer.blendFctSABF[1] = data.getIntExtra("BLEND_FCT_SABF", GLES20.GL_ONE )
                // ブレンドファクター(アルファ先)
                renderer.blendFctDABF[1] = data.getIntExtra("BLEND_FCT_DABF", GLES20.GL_ONE )
            }
            //
            else -> super.onActivityResult(requestCode, resultCode, data)
        }

    }

    override fun onResume() {
        super.onResume()
        myGLES20View.onResume()
    }

    override fun onPause() {
        super.onPause()
        myGLES20View.onPause()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
                W030zFragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}
