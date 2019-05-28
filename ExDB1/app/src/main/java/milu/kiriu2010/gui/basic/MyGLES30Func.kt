package milu.kiriu2010.gui.basic

import android.graphics.Bitmap
import android.opengl.GLES30
import android.util.Log
import milu.kiriu2010.gui.model.MgModelAbs
import milu.kiriu2010.gui.shader.MgShader
import java.nio.ByteBuffer
import java.nio.IntBuffer
import kotlin.math.exp

// ----------------------------------------------------------------------
// GLSL ES3.0用
// ----------------------------------------------------------------------
// 2019.05.28 初回
// ----------------------------------------------------------------------
// from MyGLES20Func
//   2019.04.27 ビットマップをロードしテクスチャを生成
//   2019.05.11 OpenGLのエラー状態を出力2
//   2019.05.18 テクスチャパラメータの設定をしないパラメータ追加
//   2019.05.19 フレームバッファを生成
//   2019.05.24 フレームバッファ生成の引数に"浮動小数点数テクスチャ"用を追加
// ----------------------------------------------------------------------
class MyGLES30Func {

    companion object {
        private const val TAG = "MyGLFunc"

        // -------------------------------------
        // シェーダをロードする
        // -------------------------------------
        // type
        //   GLES30.GL_VERTEX_SHADER
        //   GLES30.GL_FRAGMENT_SHADER
        // -------------------------------------
        fun loadShader(type: Int, shaderCode: String): Int {
            return GLES30.glCreateShader(type).also { shader ->

                // add the source code to the shader and compile it
                GLES30.glShaderSource(shader, shaderCode)
                GLES30.glCompileShader(shader)

                // コンパイル結果のチェック
                val compileStatus = IntArray(1)
                GLES30.glGetShaderiv(shader, GLES30.GL_COMPILE_STATUS, compileStatus,0)
                if ( compileStatus[0] == 0 ) {
                    // 何行目に誤りがあるか出力
                    Log.d(TAG,GLES30.glGetShaderInfoLog(shader))
                    // コンパイル失敗
                    GLES30.glDeleteShader(shader)
                    throw RuntimeException("Compile Error:"+shaderCode)
                }
            }
        }

        // -------------------------------------
        // プログラムオブジェクトの生成とリンク
        // -------------------------------------
        // svhandle
        //  頂点シェーダのハンドル
        // sfhandle
        //  フラグメントシェーダのハンドル
        fun createProgram(svhandle: Int, sfhandle: Int, attrStrArray: Array<String>): Int {
            val programHandle = GLES30.glCreateProgram().also {
                // 頂点シェーダをプログラムに追加
                GLES30.glAttachShader(it,svhandle)
                MyGLFunc.printShaderInfoLog(svhandle,"vertex shader")

                // フラグメントシェーダをプログラムに追加
                GLES30.glAttachShader(it,sfhandle)
                MyGLFunc.printShaderInfoLog(sfhandle,"fragment shader")

                // シェーダオブジェクトを削除
                GLES30.glDeleteShader(svhandle)
                GLES30.glDeleteShader(sfhandle)

                // attributeのindexを設定
                attrStrArray.forEachIndexed { id, attr ->
                    GLES30.glBindAttribLocation(it,id,attr)
                }

                // リンクする
                GLES30.glLinkProgram(it)

                // リンク結果のチェック
                val linkStatus = IntArray(1)
                GLES30.glGetProgramiv(it,GLES30.GL_LINK_STATUS,linkStatus,0)
                // リンク失敗
                if (linkStatus[0] == 0) {
                    MyGLFunc.printProgramInfoLog(it)
                    GLES30.glDeleteProgram(it)
                    throw RuntimeException("Error creating program.")
                }

                // シェーダプログラムを適用する
                GLES30.glUseProgram(it)
            }
            return programHandle
        }

        // -------------------------------------
        // OpenGLのエラー状態を出力
        // -------------------------------------
        fun checkGlError( str: String ) {
            var error = GLES30.glGetError()
            while ( error != GLES30.GL_NO_ERROR ) {
                Log.d(TAG, "${str}:${error}")
                error = GLES30.glGetError()
            }
        }

        // -------------------------------------
        // OpenGLのエラー状態を出力2
        // -------------------------------------
        fun checkGlError2( str: String, shader: MgShader, model: MgModelAbs ) {
            var error = GLES30.glGetError()
            while ( error != GLES30.GL_NO_ERROR ) {
                Log.d(TAG, "${shader.javaClass.simpleName}:${str}:${model.javaClass.simpleName}:${error}")
                error = GLES30.glGetError()
            }
        }

        // -------------------------------------
        // シェーダの情報を表示する
        // -------------------------------------
        fun printShaderInfoLog(shaderHandle: Int, tag: String) {
            Log.d(TAG,"=== shader compile[${tag}] ===================")
            // シェーダのコンパイル時のログの内容を取得する
            Log.d(TAG,GLES30.glGetShaderInfoLog(shaderHandle))
            /*
            val len = IntArray(1)
            GLES30.glGetProgramiv(shaderHandle,GLES30.GL_INFO_LOG_LENGTH,len,0)
            // シェーダのコンパイル時のログの内容を取得する
            if ( len[0] > 0 ) {
                Log.d(TAG,GLES30.glGetShaderInfoLog(shaderHandle))
            }
            else {
                Log.d(TAG,"cannot allocate log for shader.")
            }
            */
        }

        // -------------------------------------
        // プログラムの情報を表示する
        // -------------------------------------
        fun printProgramInfoLog(programHandle: Int) {
            Log.d(TAG,"=== shader link ============================")
            // シェーダのリンク時のログの内容を取得する
            Log.d(TAG, GLES30.glGetProgramInfoLog(programHandle))
            /*
            val len = IntArray(1)
            GLES30.glGetProgramiv(programHandle,GLES30.GL_INFO_LOG_LENGTH,len,0)
            // シェーダのリンク時のログの内容を取得する
            if ( len[0] > 0 ) {
                Log.d(TAG,GLES30.glGetProgramInfoLog(programHandle))
            }
            else {
                Log.d(TAG,"cannot allocate log for program.")
            }
            */
        }

        // ----------------------------------------
        // ビットマップをロードしテクスチャを生成する
        // ----------------------------------------
        fun createTexture(id: Int, textures: IntArray, bmp: Bitmap, size: Int = -1, wrapParam: Int = GLES30.GL_REPEAT) {

            // テクスチャをバインドする
            GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textures[id])
            MyGLFunc.checkGlError("glBindTexture")

            if ( size > 0 ) {
                val resizedBmp = Bitmap.createScaledBitmap(bmp,size,size,false)

                // ビットマップ⇒バッファへ変換
                val buffer = ByteBuffer.allocate(resizedBmp.byteCount)
                resizedBmp.copyPixelsToBuffer(buffer)
                buffer.rewind()

                // テクスチャへイメージを適用
                GLES30.glTexImage2D(GLES30.GL_TEXTURE_2D,0,GLES30.GL_RGBA,resizedBmp.width,resizedBmp.height,0,
                        GLES30.GL_RGBA,GLES30.GL_UNSIGNED_BYTE,buffer)
            }
            else {
                // ビットマップ⇒バッファへ変換
                val buffer = ByteBuffer.allocate(bmp.byteCount)
                bmp.copyPixelsToBuffer(buffer)
                buffer.rewind()

                // テクスチャへイメージを適用
                GLES30.glTexImage2D(GLES30.GL_TEXTURE_2D,0,GLES30.GL_RGBA,bmp.width,bmp.height,0,
                        GLES30.GL_RGBA,GLES30.GL_UNSIGNED_BYTE,buffer)
            }

            /*
            // GLES30.glTexImage2Dを使わないやり方
            // ビットマップをテクスチャに設定
            GLUtils.texImage2D(GLES30.GL_TEXTURE_2D, 0, bmp, 0)
            MyGLFunc.checkGlError("texImage2D")
            */

            // ミップマップを生成
            GLES30.glGenerateMipmap(GLES30.GL_TEXTURE_2D)

            // テクスチャパラメータを設定
            if ( wrapParam != -1 ) {
                GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_LINEAR)
                GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_LINEAR)
                GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_S, wrapParam)
                GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_T, wrapParam)
            }

            // テクスチャのバインドを無効化
            GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, 0)

            if ( bmp.isRecycled == false ) {
                bmp.recycle()
            }

            if (textures[id] == 0) {
                throw java.lang.RuntimeException("Error loading texture[${id}]")
            }
        }

        // --------------------------------------------------
        // フレームバッファを生成する
        // --------------------------------------------------
        fun createFrameBuffer(width: Int, height: Int, id: Int, bufFrame: IntBuffer, bufDepthRender: IntBuffer, frameTexture: IntBuffer, type: Int = GLES30.GL_UNSIGNED_BYTE) {
            // フレームバッファのバインド
            GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER,bufFrame[id])

            // 深度バッファ用レンダ―バッファのバインド
            GLES30.glBindRenderbuffer(GLES30.GL_RENDERBUFFER,bufDepthRender[id])

            // レンダ―バッファを深度バッファとして設定
            GLES30.glRenderbufferStorage(GLES30.GL_RENDERBUFFER, GLES30.GL_DEPTH_COMPONENT16, width, height)

            // フレームバッファにレンダ―バッファを関連付ける
            GLES30.glFramebufferRenderbuffer(GLES30.GL_FRAMEBUFFER, GLES30.GL_DEPTH_ATTACHMENT, GLES30.GL_RENDERBUFFER,bufDepthRender[id])

            // フレームバッファ用のテクスチャをバインド
            GLES30.glBindTexture(GLES30.GL_TEXTURE_2D,frameTexture[id])

            // フレームバッファ用のテクスチャにカラー用のメモリ領域を確保
            //   type:
            //     浮動小数点数テクスチャ⇒GLES30.GL_FLOATを指定
            GLES30.glTexImage2D(GLES30.GL_TEXTURE_2D,0,GLES30.GL_RGBA,width,height,0,
                    GLES30.GL_RGBA,type,null)

            // テクスチャパラメータ
            GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D,GLES30.GL_TEXTURE_MAG_FILTER,GLES30.GL_LINEAR)
            GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D,GLES30.GL_TEXTURE_MIN_FILTER,GLES30.GL_LINEAR)

            // フレームバッファにテクスチャを関連付ける
            GLES30.glFramebufferTexture2D(GLES30.GL_FRAMEBUFFER, GLES30.GL_COLOR_ATTACHMENT0,GLES30.GL_TEXTURE_2D,frameTexture[id],0)

            // 各種オブジェクトのバインドを解除
            GLES30.glBindTexture(GLES30.GL_TEXTURE_2D,0)
            GLES30.glBindRenderbuffer(GLES30.GL_RENDERBUFFER,0)
            GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER,0)
        }
    }


}
