package milu.kiriu2010.exdb1.opengl.triangle02

import android.opengl.GLES20
import milu.kiriu2010.exdb1.opengl.MyGLCheck
import java.lang.RuntimeException
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer



// https://developer.android.com/training/graphics/opengl/shapes
class MyTriangle02 {
    // attribute(頂点)の要素数
    val COORDS_PER_POSITION = 3
    // 頂点の位置情報を格納する配列
    // 反時計回り
    val vertex_position = floatArrayOf(
         0f, 1f, 0f,    // top
         1f, 0f, 0f,    // bottom left
        -1f, 0f, 0f     // bottom right
    )
    // attribute(色)の要素数
    val COORDS_PER_COLOR = 4
    // 頂点の色情報を格納する配列
    val vertex_color = floatArrayOf(
        1f, 0f, 0f, 1f,
        0f, 1f, 0f, 1f,
        0f, 0f, 1f, 1f
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
    private var mPositionHandle: Int = 0
    private var mColorHandle: Int = 0
    private var mMVPMatrixHandle: Int = 0

    /*
    private val vertexCount: Int = vertex_position.size / COORDS_PER_POSITION
    private val vertexStride: Int = COORDS_PER_POSITION * 4 // 4 bytes per vertex
    */

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
            attribute vec3 position;
            attribute vec4 color;
            uniform   mat4 mvpMatrix;
            varying   vec4 vColor;
            void main() {
                vColor = color;
                gl_Position = mvpMatrix * vec4(position, 1.0);
            }
            """.trimIndent()

    private val fragmentShaderCode =
            """
            precision mediump float;
            uniform   vec4 vColor;
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

            // creates OpenGL ES program executables
            GLES20.glLinkProgram(it)
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

        // get handle to vertex shader's vPosition member
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "position").also {

            // Enable a handle to the triangle vertices
            GLES20.glEnableVertexAttribArray(it)

            // Prepare the triangle coordinate data
            GLES20.glVertexAttribPointer(
                    it,
                    COORDS_PER_POSITION,
                    GLES20.GL_FLOAT,
                    false,
                    COORDS_PER_POSITION * 4,
                    positionBuffer
            )
        }

        // get handle to fragment shader's vColor member
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "color").also {
            /*
            // Set color for drawing the triangle
            GLES20.glUniform4fv(colorHandle, 1, color, 0)
            */
            GLES20.glEnableVertexAttribArray(it)
            GLES20.glVertexAttribPointer(
                    it,
                    COORDS_PER_COLOR,
                    GLES20.GL_FLOAT,
                    false,
                    COORDS_PER_COLOR * 4,
                    colorBuffer
            )
        }
        MyGLCheck.checkGlError("mColorHandle")

        // get handle to shape's transformation matrix
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "mvpMatrix").also { mvpMatrixHandle ->
            // Apply the projection and view transformation
            GLES20.glUniformMatrix4fv(mvpMatrixHandle, 1, false, mvpMatrix, 0)
        }
        MyGLCheck.checkGlError("mMVPMatrixHandle")

        // 三角形描画
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3)

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
