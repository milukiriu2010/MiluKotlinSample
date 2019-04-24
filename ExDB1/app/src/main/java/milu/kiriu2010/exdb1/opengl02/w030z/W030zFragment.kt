package milu.kiriu2010.exdb1.opengl02.w030z

import android.content.Intent
import android.graphics.BitmapFactory
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
import milu.kiriu2010.exdb1.opengl.TextureView
import milu.kiriu2010.exdb1.opengl01.w019.W030zRenderer

class W030zFragment : Fragment() {

    private lateinit var scrollViewW030y: ScrollView

    private lateinit var textureView: TextureView

    private lateinit var renderer: W030zRenderer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_open_gl02_w030z, container, false)

        /*
        scrollViewW030y = view.findViewById(R.id.scrollViewW030y)
        val marginLayoutParams = scrollViewW030y.layoutParams
        marginLayoutParams.height = 3000;
        scrollViewW030y.layoutParams = marginLayoutParams
        */

        textureView = view.findViewById<TextureView>(R.id.textureView)
        val bmp0 = BitmapFactory.decodeResource(resources,R.drawable.texture_w029)
        renderer = W030zRenderer()
        renderer.bmpArray.add(bmp0)
        textureView.setRenderer(renderer)
        textureView.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                    renderer.rotateSwitch = false
                }
                MotionEvent.ACTION_DOWN -> {
                    Log.d(javaClass.simpleName,"ex[${event.x}]ey[${event.y}]")
                    Log.d(javaClass.simpleName,"vw[${textureView.width}]vh[${textureView.height}]")
                    renderer.rotateSwitch = true
                    renderer.receiveTouch(event,textureView.width,textureView.height)
                }
                MotionEvent.ACTION_MOVE -> {
                    renderer.receiveTouch(event,textureView.width,textureView.height)
                }
                else -> {
                }
            }
            true
        }

        /*
        // ブレンド有効
        val checkBoxW030 = view.findViewById<CheckBox>(R.id.checkBoxW030)
        checkBoxW030.setOnCheckedChangeListener { buttonView, isChecked ->
            render.blend = isChecked
        }

        // アルファ成分
        val seekBarW030 = view.findViewById<SeekBar>(R.id.seekBarW030)
        seekBarW030.setOnSeekBarChangeListener( object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                render.vertexAplha = seekBarW030.progress.toFloat()/seekBarW030.max.toFloat()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                render.vertexAplha = seekBarW030.progress.toFloat()/seekBarW030.max.toFloat()
            }
        })

        // Color Equation
        val spinnerW30CE = view.findViewById<Spinner>(R.id.spinnerW30CE)
        spinnerW30CE.onItemSelectedListener = object: OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                render.equationColor = when (spinnerW30CE.selectedItem) {
                    "FUNC_ADD" -> GLES20.GL_FUNC_ADD
                    "FUNC_SUBTRACT" -> GLES20.GL_FUNC_SUBTRACT
                    "FUNC_REVERSE_SUBTRACT" -> GLES20.GL_FUNC_REVERSE_SUBTRACT
                    else -> GLES20.GL_FUNC_ADD
                }
            }
        }

        // Alpha Equation
        val spinnerW30AE = view.findViewById<Spinner>(R.id.spinnerW30AE)
        spinnerW30AE.onItemSelectedListener = object: OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                render.equationAlpha = when (spinnerW30CE.selectedItem) {
                    "FUNC_ADD" -> GLES20.GL_FUNC_ADD
                    "FUNC_SUBTRACT" -> GLES20.GL_FUNC_SUBTRACT
                    "FUNC_REVERSE_SUBTRACT" -> GLES20.GL_FUNC_REVERSE_SUBTRACT
                    else -> GLES20.GL_FUNC_ADD
                }
            }
        }

        // ブレンドファクター(カラー元)
        val spinnerW30SCBF = view.findViewById<Spinner>(R.id.spinnerW30SCBF)
        spinnerW30SCBF.onItemSelectedListener = object: OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                render.blendFctSCBF = when (spinnerW30SCBF.selectedItem) {
                    "ZERO" -> GLES20.GL_ZERO
                    "ONE" -> GLES20.GL_ONE
                    "SRC_COLOR" -> GLES20.GL_SRC_COLOR
                    "DST_COLOR" -> GLES20.GL_DST_COLOR
                    "ONE_MINUS_SRC_COLOR" -> GLES20.GL_ONE_MINUS_SRC_COLOR
                    "ONE_MINUS_DST_COLOR" -> GLES20.GL_ONE_MINUS_DST_COLOR
                    "SRC_ALPHA" -> GLES20.GL_SRC_ALPHA
                    "DST_ALPHA" -> GLES20.GL_DST_ALPHA
                    "ONE_MINUS_SRC_ALPHA" -> GLES20.GL_ONE_MINUS_SRC_ALPHA
                    "ONE_MINUS_DST_ALPHA" -> GLES20.GL_ONE_MINUS_DST_ALPHA
                    "CONSTANT_COLOR" -> GLES20.GL_CONSTANT_COLOR
                    "ONE_MINUS_CONSTANT_COLOR" -> GLES20.GL_ONE_MINUS_CONSTANT_COLOR
                    "CONSTANT_ALPHA" -> GLES20.GL_CONSTANT_ALPHA
                    "ONE_MINUS_CONSTANT_ALPHA" -> GLES20.GL_ONE_MINUS_CONSTANT_ALPHA
                    "SRC_ALPHA_SATURATE" -> GLES20.GL_SRC_ALPHA_SATURATE
                    else -> GLES20.GL_ONE
                }
            }
        }
        */

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

        // パラメータを変更するダイアログを開く(ポリゴン)
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
            dlg.setTargetFragment(this,3)
            dlg.show(fragmentManager,"Model2")
        }

        return view
    }

    // ダイアログ上での変更結果をレンダリングに反映
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        when (requestCode) {
            // コンテキストの色
            1 -> {
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
            // パラメータを変更するダイアログを開く(ポリゴン)
            4 -> {
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
        textureView.onResume()
    }

    override fun onPause() {
        super.onPause()
        textureView.onPause()
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