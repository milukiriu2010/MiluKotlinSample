package milu.kiriu2010.exdb1.opengl01.w019

import android.opengl.GLES20
import milu.kiriu2010.gui.basic.MyGLFunc
import java.lang.RuntimeException
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer


// https://wgld.org/d/webgl/w018.html
class MySquare02 {
    // attribute(頂点)の要素数
    val COORDS_PER_POSITION = 3
    // 頂点の位置情報を格納する配列
    // 反時計回り
    val vertex_position = floatArrayOf(
          0f, 1f, 0f,    // top
          1f, 0f, 0f,    // bottom left
         -1f, 0f, 0f,    // bottom right
          0f,-1f, 0f
    )

    // attribute(色)の要素数
    val COORDS_PER_COLOR = 4
    // 頂点の色情報を格納する配列
    val vertex_color = floatArrayOf(
        1f, 0f, 0f, 1f,
        0f, 1f, 0f, 1f,
        0f, 0f, 1f, 1f,
        1f, 1f, 1f, 1f
    )

    // 頂点のインデックスを格納する配列
    val drawOrder = shortArrayOf(
        0,1,2,
        1,2,3
    )


    private var positionBuffer: FloatBuffer =
            // (number of coordinate values * 4 bytes per float)
            ByteBuffer.allocateDirect(vertex_position.size * 4).run {
                // use the device hardware's native byte order
                order(ByteOrder.nativeOrder())

                // create a floating point buffer from the ByteBuffer
                asFloatBuffer().apply {
                    // add the coordinates to the FloatBuffer
                    put(vertex_position)
                    // set the buffer to read the first coordinate
                    position(0)
                }
            }


    private var colorBuffer: FloatBuffer =
            ByteBuffer.allocateDirect(vertex_color.size * 4).run {
                // use the device hardware's native byte order
                order(ByteOrder.nativeOrder())

                // create a floating point buffer from the ByteBuffer
                asFloatBuffer().apply {
                    // add the coordinates to the FloatBuffer
                    put(vertex_color)
                    // set the buffer to read the first coordinate
                    position(0)
                }
            }


    // initialize byte buffer for the draw list
    private val drawListBuffer: ShortBuffer =
    // (# of coordinate values * 2 bytes per short)
            ByteBuffer.allocateDirect(drawOrder.size * 2).run {
                order(ByteOrder.nativeOrder())
                asShortBuffer().apply {
                    put(drawOrder)
                    position(0)
                }
            }

    private var mPositionHandle: Int = 0
    private var mColorHandle: Int = 0
    private var mMVPMatrixHandle: Int = 0

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
            uniform   mat4 u_MVPMatrix;
            attribute vec3 a_Position;
            attribute vec4 a_Color;
            varying   vec4 vColor;
            void main() {
                vColor = a_Color;
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

    init {
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
            GLES20.glBindAttribLocation(mProgram,1,"a_Color")

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

    fun draw(mvpMatrix: FloatArray) {
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

        // 四角形描画
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, drawOrder.size,
                GLES20.GL_UNSIGNED_SHORT, drawListBuffer)

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle)
    }

    /*
    // VBOを生成する
    private fun createVBO(data: FloatArray) {
        // バッファオブジェクトの生成
        val vbo = GLES20.cre
    }
    */
}
