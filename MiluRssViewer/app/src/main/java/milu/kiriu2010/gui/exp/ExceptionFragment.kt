package milu.kiriu2010.gui.exp

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import milu.kiriu2010.id.BundleID
import milu.kiriu2010.milurssviewer.R
import milu.kiriu2010.tool.MyTool

class ExceptionFragment: Fragment() {
    // エラーメッセージ
    private lateinit var strMsg: String
    // エラー詳細
    private lateinit var ex: Exception

    // エラーを表示するフラグメントを生成
    companion object {
        fun newInstance( strMsg: String ): Fragment {
            val fragmentException = ExceptionFragment()

            // フラグメントに渡すデータをセット
            val args = Bundle()
            args.putString( BundleID.ID_MSG.id, strMsg )
            fragmentException.arguments = args

            return fragmentException
        }

        fun newInstance( strMsg: String, ex: Exception ): Fragment {
            val fragmentException = ExceptionFragment()

            // フラグメントに渡すデータをセット
            val args = Bundle()
            args.putString( BundleID.ID_MSG.id, strMsg )
            args.putSerializable( BundleID.ID_EXCEPTION.id, ex )
            fragmentException.arguments = args

            return fragmentException
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
        this.strMsg = args.getString(BundleID.ID_MSG.id)
        this.ex = args.getSerializable(BundleID.ID_EXCEPTION.id) as? Exception ?: return
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //return super.onCreateView(inflater, container, savedInstanceState)
        Log.d(javaClass.simpleName, "")
        Log.d(javaClass.simpleName, "onCreateView")
        Log.d(javaClass.simpleName, "============")

        // XMLからエラー情報を表示するビューを生成
        val view = inflater.inflate( R.layout.fragment_exception, container, false )

        // エラーメッセージを表示
        val editMsg = view.findViewById<EditText>(R.id.editMsg)
        editMsg.setText( strMsg )

        // エラー詳細を表示
        val editExp = view.findViewById<EditText>(R.id.editExp)
        // 初期化されていればエラー詳細を表示する
        if ( ::ex.isInitialized ) {
            editExp.setText(MyTool.exp2str(ex))
        }

        return view
    }
}