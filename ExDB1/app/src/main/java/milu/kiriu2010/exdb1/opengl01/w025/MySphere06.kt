package milu.kiriu2010.exdb1.opengl01.w025

import android.opengl.GLES20
import milu.kiriu2010.exdb1.opengl.MyGLCheck
import milu.kiriu2010.gui.basic.MyColor
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.IntBuffer
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

// 球体
class MySphere06 {
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
    lateinit var drawListBuffer: IntBuffer

    val pos = arrayListOf<Float>()
    val nor = arrayListOf<Float>()
    val col = arrayListOf<Float>()
    val idx = arrayListOf<Int>()

    init {
        // 球体の頂点データを生成
        createPath(32,32,2f, floatArrayOf(0.25f,0.25f,0.75f,1f))

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

    }

    // 球体の頂点データを生成
    private fun createPath( row: Int, column: Int, rad: Float, color: FloatArray? = null ) {
        pos.clear()
        nor.clear()
        col.clear()
        idx.clear()

        (0..row).forEach { i ->
            var r = PI.toFloat() / row.toFloat() * i.toFloat()
            var ry = cos(r)
            var rr = sin(r)
            (0..column).forEach {  ii ->
                var tr = PI.toFloat() * 2f/column.toFloat() * ii.toFloat()
                var tx = rr * rad * cos(tr)
                var ty = ry * rad;
                var tz = rr * rad * sin(tr)
                var rx = rr * cos(tr)
                var rz = rr * sin(tr)
                if ( color != null ) {
                    col.addAll(arrayListOf(color[0],color[1],color[2],color[3]))
                }
                else {
                    var tc = MyColor.hsva(360/row*i,1f,1f,1f)
                    col.addAll(arrayListOf(tc[0],tc[1],tc[2],tc[3]))
                }
                pos.addAll(arrayListOf(tx,ty,tz))
                nor.addAll(arrayListOf(rx,ry,rz))
            }


            (0 until row).forEach { i ->
                (0 until column).forEach { ii ->
                    val r = (column+1)*i+ii
                    idx.addAll(arrayListOf<Int>(r,r+1,r+column+2))
                    idx.addAll(arrayListOf<Int>(r,r+column+2,r+column+1))
                }
            }
        }
    }

    fun draw(mProgram: Int,
             mvpMatrix: FloatArray,
             modelMatrix: FloatArray,
             invMatrix: FloatArray,
             lightPositionMatrix: FloatArray,
             ambientColorMatrix: FloatArray,
             eyeDirection: FloatArray
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

        // 光関係は、２回呼び出さなくてよい？

        /*
        GLES20.glGetUniformLocation(mProgram,"u_lightPosition").also { lightPositionHandle ->
            GLES20.glUniform3fv(lightPositionHandle,1,lightPositionMatrix,0)
        }

        GLES20.glGetUniformLocation(mProgram,"u_eyeDirection").also { eyeDirectionHandle ->
            GLES20.glUniform3fv(eyeDirectionHandle,1,eyeDirection,0)
        }

        GLES20.glGetUniformLocation(mProgram,"u_ambientColor").also { ambientColorHandle ->
            GLES20.glUniform4fv(ambientColorHandle,1,ambientColorMatrix,0)
        }
        */

        // 球体描画
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, idx.toArray().size,
                GLES20.GL_UNSIGNED_INT, drawListBuffer)
    }
}