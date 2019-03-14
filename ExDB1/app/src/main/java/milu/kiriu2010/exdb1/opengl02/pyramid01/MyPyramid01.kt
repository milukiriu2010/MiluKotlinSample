package milu.kiriu2010.exdb1.opengl02.pyramid01

import android.opengl.GLES20
import milu.kiriu2010.exdb1.opengl.MyGLCheck
import java.lang.RuntimeException
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.IntBuffer
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin


// https://wgld.org/d/webgl/w026.html
class MyPyramid01 {
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
    // 描画インデックス
    private lateinit var drawListBuffer: IntBuffer

    val pos = arrayListOf<Float>()
    val nor = arrayListOf<Float>()
    val col = arrayListOf<Float>()
    val idx = arrayListOf<Int>()

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
            uniform   mat4 u_mMatrix;
            varying   vec3 vPosition;
            varying   vec3 vNormal;
            varying   vec4 vColor;
            void main() {
                vPosition   = (u_mMatrix * vec4(a_Position,1.0)).xyz;
                vNormal     = a_Normal;
                vColor      = a_Color;
                gl_Position = u_MVPMatrix * vec4(a_Position,1.0);
            }
            """.trimIndent()

    private val fragmentShaderCode =
            """
            precision mediump float;
            uniform   mat4 u_invMatrix;
            uniform   vec3 u_lightPosition;
            uniform   vec3 u_eyeDirection;
            uniform   vec4 u_ambientColor;
            varying   vec3 vPosition;
            varying   vec3 vNormal;
            varying   vec4 vColor;

            void main() {
                vec3  lightVec  = u_lightPosition - vPosition;
                vec3  invLight  = normalize(u_invMatrix * vec4(lightVec, 0.0)).xyz;
                vec3  invEye    = normalize(u_invMatrix * vec4(u_eyeDirection, 0.0)).xyz;
                vec3  halfLE    = normalize(invLight + invEye);
                float diffuse   = clamp(dot(vNormal, invLight), 0.1, 1.0) + 0.2;
                float specular  = pow(clamp(dot(vNormal,halfLE), 0.0, 1.0), 50.0);
                vec4  destColor = vColor * vec4(vec3(diffuse),1.0) + vec4(vec3(specular),1.0) + u_ambientColor;
                gl_FragColor    = destColor;
            }
            """.trimIndent()

    private var mProgram: Int = 0

    init {
        val c1 = cos(2f*PI/3f).toFloat()
        val c2 = cos(4f*PI/3f).toFloat()
        val s1 = sin(2f*PI/3f).toFloat()
        val s2 = sin(4f*PI/3f).toFloat()

        // 最初の３点は、底面を下からみる⇒トップ
        pos.addAll(arrayListOf(1f,0f,0f))
        pos.addAll(arrayListOf<Float>(c1,0f,s1))
        pos.addAll(arrayListOf<Float>(c2,0f,s2))
        pos.addAll(arrayListOf(0f,1f,0f))

        // 色
        (0 until 4).forEach {
            col.addAll(arrayListOf<Float>(1f,0f,0f,1f))
        }

        // 法線
        nor.addAll(arrayListOf(0f,-1f,0f))
        nor.addAll(arrayListOf(1f,1f,1f))
        nor.addAll(arrayListOf(-1f,1f,0f))
        nor.addAll(arrayListOf(1f,1f,-1f))


        // インデックス
        // 底面
        idx.addAll(arrayListOf<Int>(0,1,2))
        // 横１
        idx.addAll(arrayListOf<Int>(0,3,1))
        // 横２
        idx.addAll(arrayListOf<Int>(1,3,2))
        // 横３
        idx.addAll(arrayListOf<Int>(2,3,1))


        /*
        // 頂点バッファ
        // 最初の３点は、底面を下からみる⇒トップ
        pos.addAll(arrayListOf(1f,0f,0f))
        pos.addAll(arrayListOf<Float>(c1,0f,s1))
        pos.addAll(arrayListOf<Float>(c2,0f,s2))
        // 横１
        pos.addAll(arrayListOf(1f,0f,0f))
        pos.addAll(arrayListOf<Float>(c1,0f,s1))
        pos.addAll(arrayListOf(0f,1f,0f))
        // 横２
        pos.addAll(arrayListOf<Float>(c1,0f,s1))
        pos.addAll(arrayListOf<Float>(c2,0f,s2))
        pos.addAll(arrayListOf(0f,1f,0f))
        // 横３
        pos.addAll(arrayListOf<Float>(c2,0f,s2))
        pos.addAll(arrayListOf(1f,0f,0f))
        pos.addAll(arrayListOf(0f,1f,0f))

        // 色
        (0 until 12).forEach {
            col.addAll(arrayListOf<Float>(1f,0f,0f,1f))
        }
        */





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
            MyGLCheck.printShaderInfoLog(vertexShader)

            // add the fragment shader to program
            GLES20.glAttachShader(it, fragmentShader)
            MyGLCheck.printShaderInfoLog(fragmentShader)

            // シェーダオブジェクトを削除
            GLES20.glDeleteShader(vertexShader)
            GLES20.glDeleteShader(fragmentShader)

            // attributeのindexを設定
            GLES20.glBindAttribLocation(it,0,"a_Position")
            GLES20.glBindAttribLocation(it,1,"a_Color")

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
             modelMatrix: FloatArray,
             invMatrix: FloatArray,
             lightPositionMatrix: FloatArray,
             ambientColorMatrix: FloatArray,
             eyeDirection: FloatArray) {

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
        MyGLCheck.checkGlError("mPositionHandle")

        normalBuffer.position(0)
        GLES20.glGetAttribLocation(mProgram, "a_Normal").also {

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
        MyGLCheck.checkGlError("mNormalHandle")

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
        MyGLCheck.checkGlError("mColorHandle")

        // get handle to shape's transformation matrix
        GLES20.glGetUniformLocation(mProgram, "u_MVPMatrix").also { mvpMatrixHandle ->
            // Apply the projection and view transformation
            GLES20.glUniformMatrix4fv(mvpMatrixHandle, 1, false, mvpMatrix, 0)
        }
        MyGLCheck.checkGlError("mMVPMatrixHandle")

        GLES20.glGetUniformLocation(mProgram, "u_mMatrix").also { modelMatrixHandle ->
            // Apply the projection and view transformation
            GLES20.glUniformMatrix4fv(modelMatrixHandle, 1, false, modelMatrix, 0)

        }
        MyGLCheck.checkGlError("mModelMatrixHandle")

        GLES20.glGetUniformLocation(mProgram,"u_invMatrix").also { invMatrixHandle ->
            GLES20.glUniformMatrix4fv(invMatrixHandle,1,false,invMatrix,0)
        }
        MyGLCheck.checkGlError("mInvMatrixHandle")

        GLES20.glGetUniformLocation(mProgram,"u_lightPosition").also { lightPositionHandle ->
            GLES20.glUniform3fv(lightPositionHandle,1,lightPositionMatrix,0)
        }

        GLES20.glGetUniformLocation(mProgram,"u_eyeDirection").also { eyeDirectionHandle ->
            GLES20.glUniform3fv(eyeDirectionHandle,1,eyeDirection,0)
        }

        GLES20.glGetUniformLocation(mProgram,"u_ambientColor").also { ambientColorHandle ->
            GLES20.glUniform4fv(ambientColorHandle,1,ambientColorMatrix,0)
        }

        // ピラミッド描画
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, idx.toArray().size,
                GLES20.GL_UNSIGNED_INT, drawListBuffer)
    }
}
