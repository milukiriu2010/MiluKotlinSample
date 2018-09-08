package milu.kiriu2010.gui.common

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.EditText
import milu.kiriu2010.id.BundleID
import milu.kiriu2010.id.FragmentID
import milu.kiriu2010.milurssviewer.R
import milu.kiriu2010.tool.MyTool

// Exceptionを表示するフラグメント
// ------------------------------------------------------
// 2018.09.04 ピンチイン・アウト
// ------------------------------------------------------
// 2018.09.08 "リトライ"ボタン表示・非表示切り替え
//   呼び出し元クラスがOnRetryListenerを実装している場合
//     => "リトライ"ボタン表示
//   呼び出し元クラスがOnRetryListenerを実装してない場合
//     => "リトライ"ボタン非表示
// ------------------------------------------------------
class ExceptionFragment: Fragment()
        , View.OnTouchListener {
    // エラーメッセージ
    private lateinit var strMsg: String
    // エラー詳細
    private lateinit var ex: Exception

    // ピンチイン・アウトを検出するオブジェクト
    private lateinit var detector: ScaleGestureDetector
    // ピンチイン・アウトに使うスケール
    private var scale = 1.0f

    // "リトライ"ボタン
    private lateinit var btnRetry: Button

    // "リトライ"ボタンをクリックしたとき呼び出されるインターフェース
    private var listener: OnRetryListener? = null

    // エラーを表示するフラグメントを生成
    companion object {
        fun newInstance( strMsg: String ) =
                ExceptionFragment().apply {
                    // フラグメントに渡すデータをセット
                    val args = Bundle()
                    args.putString( BundleID.ID_MSG.id, strMsg )
                    arguments = args
                }

        fun newInstance( strMsg: String, ex: Exception ) =
                ExceptionFragment().apply {
                    // フラグメントに渡すデータをセット
                    val args = Bundle()
                    args.putString( BundleID.ID_MSG.id, strMsg )
                    args.putSerializable( BundleID.ID_EXCEPTION.id, ex )
                    arguments = args
                }
    }

    // ----------------------------------------------------------
    // 呼び出し時に渡される引数から指定されたエラー情報を取り出す
    // ----------------------------------------------------------
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d( javaClass.simpleName, "" )
        Log.d( javaClass.simpleName, "onCreateView" )
        Log.d( javaClass.simpleName, "============" )

        val args = this.arguments ?: return
        this.strMsg = args.getString(BundleID.ID_MSG.id)  ?: ""
        this.ex = args.getSerializable(BundleID.ID_EXCEPTION.id) as? Exception ?: return
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //return super.onCreateView(inflater, container, savedInstanceState)
        Log.d(javaClass.simpleName, "")
        Log.d(javaClass.simpleName, "onCreateView")
        Log.d(javaClass.simpleName, "============")

        // XMLからエラー情報を表示するビューを生成
        val view = inflater.inflate( R.layout.fragment_exception, container, false )

        val ctx = context ?: return view

        // エラーメッセージを表示
        val editMsg = view.findViewById<EditText>(R.id.editMsg)
        editMsg.setText( strMsg )

        // エラー詳細を表示
        val editExp = view.findViewById<EditText>(R.id.editExp)
        // 初期化されていればエラー詳細を表示する
        if ( ::ex.isInitialized ) {
            editExp.setText(MyTool.exp2str(ex))
        }

        // ピンチイン・アウトを検出
        detector = ScaleGestureDetector(activity?.applicationContext, object: ScaleGestureDetector.SimpleOnScaleGestureListener() {
            override fun onScale(detector: ScaleGestureDetector?): Boolean {
                Log.d( javaClass.simpleName, "ScaleGestureDetector.onScale")

                detector?.let {
                    scale *= it.scaleFactor
                    editExp.scaleX = scale
                    editExp.scaleY = scale
                }
                return true
            }

        })

        // TouchイベントをScaleListenerに渡す
        view.setOnTouchListener { v, motionEvent ->
            Log.d( javaClass.simpleName, "MotionEvent[${motionEvent.action}]")
            detector.onTouchEvent(motionEvent)
            /*
            when (motionEvent.action) {
                MotionEvent.ACTION_MOVE -> {
                    detector.onTouchEvent(motionEvent)
                }
            }
            */

            true
        }

        // "リトライ"ボタン
        btnRetry = view.findViewById(R.id.btnRetry)
        btnRetry.setOnClickListener {
            listener?.onRetry(FragmentID.ID_EXCEPTION.id)
        }
        // ------------------------------------------------------
        // 2018.09.08 "リトライ"ボタン表示・非表示切り替え
        // ------------------------------------------------------
        //   呼び出し元クラスがOnRetryListenerを実装している場合
        //     => "リトライ"ボタン表示
        //   呼び出し元クラスがOnRetryListenerを実装してない場合
        //     => "リトライ"ボタン非表示
        // ------------------------------------------------------
        if ( listener == null ) {
            btnRetry.visibility = View.GONE
        }

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnRetryListener) {
            listener = context
        }
        /*
        else {
            throw RuntimeException(context.toString() + " must implement OnRetryListener")
        }
        */
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }
    // View.OnTouchListener
    override fun onTouch(v: View?, motionEvent: MotionEvent?): Boolean {
        Log.d( javaClass.simpleName, "MotionEvent[${motionEvent?.action}]")

        when (motionEvent?.action) {
            MotionEvent.ACTION_MOVE -> {
                detector.onTouchEvent(motionEvent)
            }
        }

        return true
    }

}