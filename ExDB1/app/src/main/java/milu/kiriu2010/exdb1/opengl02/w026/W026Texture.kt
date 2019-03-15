package milu.kiriu2010.exdb1.opengl01.w019

import android.opengl.GLES20
import milu.kiriu2010.exdb1.opengl.MyGLCheck
import java.lang.RuntimeException
import java.nio.*
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.sin

// ----------------------------------------
// テクスチャ
// ----------------------------------------
// https://wgld.org/d/webgl/w026.html
class W026Texture {
    // attribute(頂点)の要素数
    val COORDS_PER_POSITION = 3
    // attribute(色)の要素数
    val COORDS_PER_COLOR = 4
    // attribute(テクスチャ)の要素数
    val COORDS_PER_TEXTURE = 2

    // 頂点バッファ
    private lateinit var positionBuffer: FloatBuffer
    // 色バッファ
    private lateinit var colorBuffer: FloatBuffer
    // テクスチャバッファ
    private lateinit var textureBuffer: FloatBuffer


    // initialize byte buffer for the draw list
    private lateinit var drawListBuffer: IntBuffer


    // -----------------------------------------------------------------
    // the matrix must be included as a modifier of gl_Position
    // Note that the uMVPMatrix factor *must be first* in order
    // for the matrix multiplication product to be correct.
    // -----------------------------------------------------------------
    private val vertexShaderCode =
            """
            attribute vec3 a_Position;
            attribute vec4 a_Color;
            attribute vec2 a_TextureCoord;
            uniform   mat4 u_mvpMatrix;
            varying   vec4 vColor;
            varying   vec2 vTextureCoord;

            void main() {
                vColor        = a_Color;
                vTextureCoord = a_TextureCoord;
                gl_Position   = u_mvpMatrix * vec4(a_Position,1.0);
            }
            """.trimIndent()

    private val fragmentShaderCode =
            """
            precision mediump float;

            uniform   sampler2D  texture;
            varying   vec4       vColor;
            varying   vec2       vTextureCoord;

            void main() {
                vec4 smpColor = texture2D(texture, vTextureCoord);
                gl_FragColor  = vColor * smpColor;
            }
            """.trimIndent()

    var mProgram: Int = 0

    val pos = arrayListOf<Float>()
    val col = arrayListOf<Float>()
    val txc = arrayListOf<Float>()
    val idx = arrayListOf<Int>()

    init {
        // 頂点の位置
        pos.addAll(arrayListOf(-1f,1f,0f))
        pos.addAll(arrayListOf(1f,1f,0f))
        pos.addAll(arrayListOf(-1f,-1f,0f))
        pos.addAll(arrayListOf(1f,-1f,0f))

        // 頂点色
        col.addAll(arrayListOf(1f,1f,1f,1f))
        col.addAll(arrayListOf(1f,1f,1f,1f))
        col.addAll(arrayListOf(1f,1f,1f,1f))
        col.addAll(arrayListOf(1f,1f,1f,1f))

        // テクスチャ座標
        txc.addAll(arrayListOf(0f,0f))
        txc.addAll(arrayListOf(1f,0f))
        txc.addAll(arrayListOf(0f,1f))
        txc.addAll(arrayListOf(1f,1f))

        // 頂点インデックス
        idx.addAll(arrayListOf(0,1,2))
        idx.addAll(arrayListOf(3,2,1))

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

        // テクスチャバッファ
        textureBuffer =
                ByteBuffer.allocateDirect(txc.toArray().size * 4).run {
                    // use the device hardware's native byte order
                    order(ByteOrder.nativeOrder())

                    // create a floating point buffer from the ByteBuffer
                    asFloatBuffer().apply {
                        // add the coordinates to the FloatBuffer
                        put(txc.toFloatArray())
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
        MyGLCheck.printShaderInfoLog(vertexShader)
        // フラグメントシェーダを生成
        val fragmentShader: Int = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode)
        MyGLCheck.printShaderInfoLog(fragmentShader)

        // プログラムオブジェクトの生成とリンク
        mProgram = GLES20.glCreateProgram().also {

            // add the vertex shader to program
            GLES20.glAttachShader(it, vertexShader)

            // add the fragment shader to program
            GLES20.glAttachShader(it, fragmentShader)

            // attributeのindexを設定
            GLES20.glBindAttribLocation(it,0,"a_Position")
            GLES20.glBindAttribLocation(it,1,"a_Color")
            GLES20.glBindAttribLocation(it,2,"a_TextureCoord")

            // creates OpenGL ES program executables
            GLES20.glLinkProgram(it)

            // リンク結果のチェック
            val linkStatus = IntArray(1)
            GLES20.glGetProgramiv(it,GLES20.GL_LINK_STATUS,linkStatus,0)
            if (linkStatus[0] == 0) {
                MyGLCheck.printProgramInfoLog(it)
                // リンク失敗
                GLES20.glDeleteProgram(it)
                throw RuntimeException("Error creating program.")
            }

            // Add program to OpenGL ES environment
            // シェーダプログラムを適用する
            GLES20.glUseProgram(it)
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

    fun draw(mvpMatrix: FloatArray,
             texture: Int
    ) {
        positionBuffer.position(0)
        // get handle to vertex shader's vPosition member
        GLES20.glGetAttribLocation(mProgram, "a_Position").also {

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
        MyGLCheck.checkGlError("a_Position")

        colorBuffer.position(0)
        // get handle to fragment shader's vColor member
        GLES20.glGetAttribLocation(mProgram, "a_Color").also {
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
        MyGLCheck.checkGlError("a_Color")

        textureBuffer.position(0)
        GLES20.glGetAttribLocation(mProgram, "a_TextureCoord").also {

            // Prepare the triangle coordinate data
            GLES20.glVertexAttribPointer(
                    it,
                    COORDS_PER_TEXTURE,
                    GLES20.GL_FLOAT,
                    false,
                    COORDS_PER_TEXTURE * 4,
                    textureBuffer
            )

            // Enable a handle to the triangle vertices
            GLES20.glEnableVertexAttribArray(it)
        }
        MyGLCheck.checkGlError("a_TextureCoord")


        // get handle to shape's transformation matrix
        GLES20.glGetUniformLocation(mProgram, "u_mvpMatrix").also { mvpMatrixHandle ->
            // Apply the projection and view transformation
            GLES20.glUniformMatrix4fv(mvpMatrixHandle, 1, false, mvpMatrix, 0)

        }
        MyGLCheck.checkGlError("u_mvpMatrix")

        GLES20.glGetUniformLocation(mProgram, "texture").also { textureMatrixHandle ->
            // Apply the projection and view transformation
            GLES20.glUniform1i(textureMatrixHandle, texture)
        }
        MyGLCheck.checkGlError("texture")

        // テクスチャ
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, idx.toArray().size,
                GLES20.GL_UNSIGNED_INT, drawListBuffer)
    }


}
