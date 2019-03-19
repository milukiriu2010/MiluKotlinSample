package milu.kiriu2010.exdb1.opengl01.w019

import android.opengl.GLES20
import milu.kiriu2010.exdb1.opengl.MyGLFunc
import java.lang.RuntimeException
import java.nio.*
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.sin

// ----------------------------------------
// 反射光によるライティング
// ----------------------------------------
// https://wgld.org/d/webgl/w023.html
class MyTorus04 {
    // attribute(頂点)の要素数
    val COORDS_PER_POSITION = 3
    // attribute(法線)の要素数
    val COORDS_PER_NORMAL = 3
    // attribute(色)の要素数
    val COORDS_PER_COLOR = 4

    // 頂点バッファ
    private lateinit var positionBuffer: FloatBuffer

    // 法線バッファ
    private lateinit var normalBuffer: FloatBuffer

    // 色バッファ
    private lateinit var colorBuffer: FloatBuffer


    // initialize byte buffer for the draw list
    private lateinit var drawListBuffer: IntBuffer

    private var mPositionHandle: Int = 0
    private var mNormalHandle: Int = 0
    private var mColorHandle: Int = 0
    private var mMVPMatrixHandle: Int = 0
    private var mInvMatrixHandle: Int = 0
    private var mLightDirectionHandle: Int = 0
    private var mEyeDirectionHandle: Int = 0
    private var mAmbientColorHandle: Int = 0

    // -----------------------------------------------------------------
    // * uMVPMatrix
    // * vPosition
    //
    // This matrix member variable provides a hook to manipulate
    // the coordinates of the objects that use this vertex shader
    // -----------------------------------------------------------------
    // the matrix must be included as a modifier of gl_Position
    // Note that the uMVPMatrix factor *must be first* in order
    // for the matrix multiplication product to be correct.
    // -----------------------------------------------------------------
    private val vertexShaderCode =
            """
            attribute vec3 a_Position;
            attribute vec3 a_Normal;
            attribute vec4 a_Color;
            uniform   mat4 u_MVPMatrix;
            uniform   mat4 u_invMatrix;
            uniform   vec3 u_lightDirection;
            uniform   vec3 u_eyeDirection;
            uniform   vec4 u_ambientColor;
            varying   vec4 vColor;
            void main() {
                vec3  invLight = normalize(u_invMatrix * vec4(u_lightDirection,0.0)).xyz;
                vec3  invEye   = normalize(u_invMatrix * vec4(u_eyeDirection,0.0)).xyz;
                vec3  halfLE   = normalize(invLight + invEye);
                float diffuse  = clamp(dot(a_Normal,invLight),0.0,1.0);
                float specular = pow(clamp(dot(a_Normal,halfLE),0.0,1.0), 50.0);
                vec4  light    = a_Color * vec4(vec3(diffuse),1.0) + vec4(vec3(specular),1.0);
                vColor = light + u_ambientColor;
                gl_Position = u_MVPMatrix * vec4(a_Position,1.0);
            }
            """.trimIndent()

    private val fragmentShaderCode =
            """
            precision mediump float;
            varying   vec4 vColor;
            void main() {
              gl_FragColor = vColor;
            }
            """.trimIndent()

    private var mProgram: Int = 0

    val pos = arrayListOf<Float>()
    val nor = arrayListOf<Float>()
    val col = arrayListOf<Float>()
    val idx = arrayListOf<Int>()

    init {
        // トーラスの頂点データを生成
        createPath(32,32,1f,2f)

        // 頂点バッファ
        positionBuffer =
            ByteBuffer.allocateDirect(pos.toArray().size * 4).run {
                // use the device hardware's native byte order
                order(ByteOrder.nativeOrder())

                // create a floating point buffer from the ByteBuffer
                asFloatBuffer().apply {
                    // add the coordinates to the FloatBuffer
                    put(pos.toFloatArray())
                    // set the buffer to read the first coordinate
                    position(0)
                }
            }

        // 法線バッファ
        normalBuffer =
                ByteBuffer.allocateDirect(nor.toArray().size * 4).run {
                    // use the device hardware's native byte order
                    order(ByteOrder.nativeOrder())

                    // create a floating point buffer from the ByteBuffer
                    asFloatBuffer().apply {
                        // add the coordinates to the FloatBuffer
                        put(nor.toFloatArray())
                        // set the buffer to read the first coordinate
                        position(0)
                    }
                }

        // 色バッファ
        colorBuffer =
            ByteBuffer.allocateDirect(col.toArray().size * 4).run {
                // use the device hardware's native byte order
                order(ByteOrder.nativeOrder())

                // create a floating point buffer from the ByteBuffer
                asFloatBuffer().apply {
                    // add the coordinates to the FloatBuffer
                    put(col.toFloatArray())
                    // set the buffer to read the first coordinate
                    position(0)
                }
            }

        // インデックスバッファ
        drawListBuffer =
            ByteBuffer.allocateDirect(idx.toArray().size * 4).run {
                order(ByteOrder.nativeOrder())
                asIntBuffer().apply {
                    put(idx.toIntArray())
                    position(0)
                }
            }

        // 頂点シェーダを生成
        val vertexShader: Int = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode)
        // フラグメントシェーダを生成
        val fragmentShader: Int = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode)

        // プログラムオブジェクトの生成とリンク
        mProgram = GLES20.glCreateProgram().also {

            // add the vertex shader to program
            GLES20.glAttachShader(it, vertexShader)

            // add the fragment shader to program
            GLES20.glAttachShader(it, fragmentShader)

            // attributeのindexを設定
            GLES20.glBindAttribLocation(mProgram,0,"a_Position")
            GLES20.glBindAttribLocation(mProgram,1,"a_Normal")
            GLES20.glBindAttribLocation(mProgram,2,"a_Color")

            // creates OpenGL ES program executables
            GLES20.glLinkProgram(it)

            /*
            // リンク結果のチェック
            val linkStatus = IntArray(1)
            GLES20.glGetProgramiv(mProgram,GLES20.GL_LINK_STATUS,linkStatus,0)
            if (linkStatus[0] == 0) {
                // リンク失敗
                GLES20.glDeleteProgram(mProgram)
                throw RuntimeException("Error creating program.")
            }
            */
        }
    }

    // ----------------------------------------------------------------------
    // 第一引数はパイプを形成する円をいくつの頂点で表現するのかを指定します。
    // 大きな数値を指定すればパイプの断面が円形に近づきますが、
    // 逆に小さな数値を指定すればパイプの断面はカクカクになっていきます。
    // ----------------------------------------------------------------------
    // 第二引数はパイプをどれくらい分割するのかを指定します。
    // この数値を大きくすると、トーラスは滑らかな輪を形成するようになり、
    // 小さな数値を指定すればカクカクの輪になります。
    // ----------------------------------------------------------------------
    // 第三引数は生成されるパイプそのものの半径です。
    // ----------------------------------------------------------------------
    // 第四引数が原点からパイプの中心までの距離になります。
    // ----------------------------------------------------------------------
    private fun createPath(row: Int, column: Int, irad: Float, orad: Float) {
        pos.clear()
        nor.clear()
        col.clear()
        idx.clear()

        (0..row).forEach { i ->
            var r = PI.toFloat() *2f/row.toFloat()*i.toFloat()
            var rr = cos(r)
            var ry = sin(r)
            (0..column).forEach { ii ->
                val tr = PI.toFloat() *2f/column.toFloat()*ii.toFloat()
                val tx = (rr*irad+orad)*cos(tr)
                val ty = ry*irad
                val tz = (rr*irad+orad)*sin(tr)
                val rx = rr * cos(tr)
                val rz = rr * sin(tr)
                pos.addAll(arrayListOf<Float>(tx,ty,tz))
                nor.addAll(arrayListOf<Float>(rx,ry,rz))
                val tc = hsva(360/column*ii,1f,1f,1f)
                col.addAll(arrayListOf<Float>(tc[0],tc[1],tc[2],tc[3]))
            }

            (0 until row).forEach { i ->
                (0 until column).forEach { ii ->
                    val r = (column+1)*i+ii
                    idx.addAll(arrayListOf<Int>(r,r+column+1,r+1))
                    idx.addAll(arrayListOf<Int>(r+column+1,r+column+2,r+1))
                }
            }
        }
    }

    // ---------------------------------------------------------
    // HSVカラー取得用関数
    // ---------------------------------------------------------
    // HSV では、色相は 0 ～ 360 の範囲に収まっている必要がありますが、
    // それ以上に大きな数値を指定しても計算が破綻しないように関数内で処理しています。
    // また、彩度や明度に不正な値が指定されている場合には正しい値を返しませんので注意しましょう。
    // 彩度・明度・透明度はいずれも 0 ～ 1 の範囲で指定してください
    // ---------------------------------------------------------
    // h: 色相(0-360)
    // s: 彩度(0.0-1.0)
    // v: 明度(0.0-1.0)
    // a: 透明度(0.0-1.0)
    // ---------------------------------------------------------
    private fun hsva(h: Int, s: Float, v: Float, a: Float): ArrayList<Float> {
        val color = arrayListOf<Float>()
        if ( (s > 1f) or (v > 1f) or (a > 1f) ) return color

        val th = h%360
        val i = floor(th.toFloat()/60f)
        val f = th.toFloat()/60f - i
        val m = v*(1f-s)
        val n = v*(1f-s*f)
        val k = v*(1-s*(1-f))
        if ( ((s>0f) === false) and ((s<0f) === false) ) {
            color.addAll(arrayListOf<Float>(v,v,v,a))
        }
        else {
            var r = arrayListOf<Float>(v,n,m,m,k,v)
            var g = arrayListOf<Float>(k,v,v,n,m,m)
            var b = arrayListOf<Float>(m,m,k,v,v,n)
            color.addAll(arrayListOf<Float>(r[i.toInt()],g[i.toInt()],b[i.toInt()],a))
        }
        return color
    }

    fun loadShader(type: Int, shaderCode: String): Int {
        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        return GLES20.glCreateShader(type).also { shader ->

            // add the source code to the shader and compile it
            GLES20.glShaderSource(shader, shaderCode)
            GLES20.glCompileShader(shader)

            // コンパイル結果のチェック
            val compileStatus = IntArray(1)
            GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compileStatus,0)
            if ( compileStatus[0] == 0 ) {
                // コンパイル失敗
                GLES20.glDeleteShader(shader)
                throw RuntimeException("Compile Error:"+shaderCode)
            }
        }
    }

    fun draw(mvpMatrix: FloatArray,
             invMatrix: FloatArray,
             lightDirectionMatrix: FloatArray,
             ambientColorMatrix: FloatArray,
             eyeDirection: FloatArray
    ) {
        // Add program to OpenGL ES environment
        GLES20.glUseProgram(mProgram)

        positionBuffer.position(0)
        // get handle to vertex shader's vPosition member
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "a_Position").also {

            // Prepare the triangle coordinate data
            GLES20.glVertexAttribPointer(
                    it,
                    COORDS_PER_POSITION,
                    GLES20.GL_FLOAT,
                    false,
                    COORDS_PER_POSITION * 4,
                    positionBuffer
            )

            // Enable a handle to the triangle vertices
            GLES20.glEnableVertexAttribArray(it)
        }
        MyGLFunc.checkGlError("mPositionHandle")

        normalBuffer.position(0)
        mNormalHandle = GLES20.glGetAttribLocation(mProgram, "a_Normal").also {

            // Prepare the triangle coordinate data
            GLES20.glVertexAttribPointer(
                    it,
                    COORDS_PER_NORMAL,
                    GLES20.GL_FLOAT,
                    false,
                    COORDS_PER_NORMAL * 4,
                    normalBuffer
            )

            // Enable a handle to the triangle vertices
            GLES20.glEnableVertexAttribArray(it)
        }
        MyGLFunc.checkGlError("mNormalHandle")


        colorBuffer.position(0)
        // get handle to fragment shader's vColor member
        mColorHandle = GLES20.glGetAttribLocation(mProgram, "a_Color").also {
            GLES20.glVertexAttribPointer(
                    it,
                    COORDS_PER_COLOR,
                    GLES20.GL_FLOAT,
                    false,
                    COORDS_PER_COLOR * 4,
                    colorBuffer
            )
            GLES20.glEnableVertexAttribArray(it)
        }
        MyGLFunc.checkGlError("mColorHandle")

        // get handle to shape's transformation matrix
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "u_MVPMatrix").also { mvpMatrixHandle ->
            // Apply the projection and view transformation
            GLES20.glUniformMatrix4fv(mvpMatrixHandle, 1, false, mvpMatrix, 0)

        }
        MyGLFunc.checkGlError("mMVPMatrixHandle")

        mInvMatrixHandle = GLES20.glGetUniformLocation(mProgram,"u_invMatrix").also { invMatrixHandle ->
            GLES20.glUniformMatrix4fv(invMatrixHandle,1,false,invMatrix,0)
        }
        MyGLFunc.checkGlError("mInvMatrixHandle")

        mLightDirectionHandle = GLES20.glGetUniformLocation(mProgram,"u_lightDirection").also { lightDirectionHandle ->
            GLES20.glUniform3fv(lightDirectionHandle,1,lightDirectionMatrix,0)
        }

        mEyeDirectionHandle = GLES20.glGetUniformLocation(mProgram,"u_eyeDirection").also { eyeDirectionHandle ->
            GLES20.glUniform3fv(eyeDirectionHandle,1,eyeDirection,0)
        }

        mAmbientColorHandle = GLES20.glGetUniformLocation(mProgram,"u_ambientColor").also { ambientColorHandle ->
            GLES20.glUniform4fv(ambientColorHandle,1,ambientColorMatrix,0)
        }

        // トーラス描画
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, idx.toArray().size,
                GLES20.GL_UNSIGNED_INT, drawListBuffer)

        // Disable vertex array
        //GLES20.glDisableVertexAttribArray(mPositionHandle)
    }

}
