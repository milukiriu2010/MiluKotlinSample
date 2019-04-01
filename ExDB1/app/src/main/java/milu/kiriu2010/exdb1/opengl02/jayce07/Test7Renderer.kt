package milu.kiriu2010.exdb1.opengl02.jayce07

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.IntBuffer

import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import android.os.SystemClock
import milu.kiriu2010.exdb1.R

/**
 * This class implements our custom renderer. Note that the GL10 parameter passed in is unused for OpenGL ES 2.0
 * renderers -- the static class GLES20 is used instead.
 */
class Test7Renderer
/**
 * Initialize the model data.
 */
(private val mActivityContext: Context) : GLSurfaceView.Renderer {

    /**
     * Store the model matrix. This matrix is used to move models from object space (where each model can be thought
     * of being located at the center of the universe) to world space.
     */
    private val mModelMatrix = FloatArray(16)

    /**
     * Store the view matrix. This can be thought of as our camera. This matrix transforms world space to eye space;
     * it positions things relative to our eye.
     */
    private val mViewMatrix = FloatArray(16)

    /** Store the projection matrix. This is used to project the scene onto a 2D viewport.  */
    private val mProjectionMatrix = FloatArray(16)

    /** Allocate storage for the final combined matrix. This will be passed into the shader program.  */
    private val mMVPMatrix = FloatArray(16)

    /** Store our model data in a float buffer.  */
    private val mCubePositions: FloatBuffer
    private val mCubeColors: FloatBuffer
    private val mCubeTextureCoordinates: FloatBuffer

    /** This will be used to pass in the transformation matrix.  */
    private var mMVPMatrixHandle: Int = 0

    /** This will be used to pass in the modelview matrix.  */
    private var mMVMatrixHandle: Int = 0

    /** This will be used to pass in the texture.  */
    private var mTextureUniformHandle: Int = 0

    /** This will be used to pass in model position information.  */
    private var mPositionHandle: Int = 0

    /** This will be used to pass in model color information.  */
    private var mColorHandle: Int = 0

    /** This will be used to pass in model texture coordinate information.  */
    private var mTextureCoordinateHandle: Int = 0

    /** How many bytes per float.  */
    private val mBytesPerFloat = 4

    /** Size of the position data in elements.  */
    private val mPositionDataSize = 3

    /** Size of the color data in elements.  */
    private val mColorDataSize = 4

    /** Size of the texture coordinate data in elements.  */
    private val mTextureCoordinateDataSize = 2

    /** This is a handle to our cube shading program.  */
    private var mProgramHandle: Int = 0

    /** This is a handle to our texture data.  */
    private var mTextureDataHandle: Int = 0

    init {

        // Define points for a cube.

        // X, Y, Z
        val cubePositionData = floatArrayOf(
                // In OpenGL counter-clockwise winding is default. This means that when we look at a triangle,
                // if the points are counter-clockwise we are looking at the "front". If not we are looking at
                // the back. OpenGL has an optimization where all back-facing triangles are culled, since they
                // usually represent the backside of an object and aren't visible anyways.

                // Front face
                -1.0f, 1.0f, 1.0f, -1.0f, -1.0f, 1.0f, 1.0f, 1.0f, 1.0f, -1.0f, -1.0f, 1.0f, 1.0f, -1.0f, 1.0f, 1.0f, 1.0f, 1.0f,

                // Right face
                1.0f, 1.0f, 1.0f, 1.0f, -1.0f, 1.0f, 1.0f, 1.0f, -1.0f, 1.0f, -1.0f, 1.0f, 1.0f, -1.0f, -1.0f, 1.0f, 1.0f, -1.0f,

                // Back face
                1.0f, 1.0f, -1.0f, 1.0f, -1.0f, -1.0f, -1.0f, 1.0f, -1.0f, 1.0f, -1.0f, -1.0f, -1.0f, -1.0f, -1.0f, -1.0f, 1.0f, -1.0f,

                // Left face
                -1.0f, 1.0f, -1.0f, -1.0f, -1.0f, -1.0f, -1.0f, 1.0f, 1.0f, -1.0f, -1.0f, -1.0f, -1.0f, -1.0f, 1.0f, -1.0f, 1.0f, 1.0f,

                // Top face
                -1.0f, 1.0f, -1.0f, -1.0f, 1.0f, 1.0f, 1.0f, 1.0f, -1.0f, -1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, -1.0f,

                // Bottom face
                1.0f, -1.0f, -1.0f, 1.0f, -1.0f, 1.0f, -1.0f, -1.0f, -1.0f, 1.0f, -1.0f, 1.0f, -1.0f, -1.0f, 1.0f, -1.0f, -1.0f, -1.0f)

        // R, G, B, A
        val cubeColorData = floatArrayOf(
                // Front face (red)
                1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f,

                // Right face (green)
                0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 1.0f,

                // Back face (blue)
                0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f,

                // Left face (yellow)
                1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f,

                // Top face (cyan)
                0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 1.0f,

                // Bottom face (magenta)
                1.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f)

        // S, T (or X, Y)
        // Texture coordinate data.
        // Because images have a Y axis pointing downward (values increase as you move down the image) while
        // OpenGL has a Y axis pointing upward, we adjust for that here by flipping the Y axis.
        // What's more is that the texture coordinates are the same for every face.
        val cubeTextureCoordinateData = floatArrayOf(
                // Front face
                0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f,

                // Right face
                0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f,

                // Back face
                0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f,

                // Left face
                0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f,

                // Top face
                0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f,

                // Bottom face
                0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f)

        // Initialize the buffers.
        mCubePositions = ByteBuffer.allocateDirect(cubePositionData.size * mBytesPerFloat)
                .order(ByteOrder.nativeOrder()).asFloatBuffer()
        mCubePositions.put(cubePositionData).position(0)

        mCubeColors = ByteBuffer.allocateDirect(cubeColorData.size * mBytesPerFloat)
                .order(ByteOrder.nativeOrder()).asFloatBuffer()
        mCubeColors.put(cubeColorData).position(0)

        mCubeTextureCoordinates = ByteBuffer.allocateDirect(cubeTextureCoordinateData.size * mBytesPerFloat)
                .order(ByteOrder.nativeOrder()).asFloatBuffer()
        mCubeTextureCoordinates.put(cubeTextureCoordinateData).position(0)
    }

    protected fun getVertexShader(shader: Int): String? {
        return ToolsUtil.readTextFileFromRawResource(mActivityContext, shader)
    }

    protected fun getFragmentShader(shader: Int): String? {
        return ToolsUtil.readTextFileFromRawResource(mActivityContext, shader)
    }

    override fun onSurfaceCreated(glUnused: GL10, config: EGLConfig) {
        // Set the background clear color to black.
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f)

        // Use culling to remove back faces.
        GLES20.glEnable(GLES20.GL_CULL_FACE)

        // Enable depth testing
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)

        // The below glEnable() call is a holdover from OpenGL ES 1, and is not needed in OpenGL ES 2.
        // Enable texture mapping
        GLES20.glEnable(GLES20.GL_TEXTURE_2D)

        // Position the eye in front of the origin.
        val eyeX = 0.0f
        val eyeY = 0.0f
        val eyeZ = -0.5f

        // We are looking toward the distance
        val lookX = 0.0f
        val lookY = 0.0f
        val lookZ = -5.0f

        // Set our up vector. This is where our head would be pointing were we holding the camera.
        val upX = 0.0f
        val upY = 1.0f
        val upZ = 0.0f

        // Set the view matrix. This matrix can be said to represent the camera position.
        // NOTE: In OpenGL 1, a ModelView matrix is used, which is a combination of a model and
        // view matrix. In OpenGL 2, we can keep track of these matrices separately if we choose.
        Matrix.setLookAtM(mViewMatrix, 0, eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ)

        val vertexShader = getVertexShader(R.raw.per_pixel_vertex_shader)
        val fragmentShader = getFragmentShader(R.raw.per_pixel_fragment_shader)

        val vertexShaderHandle = ToolsUtil.compileShader(GLES20.GL_VERTEX_SHADER, vertexShader!!)
        val fragmentShaderHandle = ToolsUtil.compileShader(GLES20.GL_FRAGMENT_SHADER, fragmentShader!!)

        mProgramHandle = ToolsUtil.createAndLinkProgram(vertexShaderHandle, fragmentShaderHandle,
                arrayOf("a_Position", "a_Color", "a_TexCoordinate"))

        // Load the texture
        mTextureDataHandle = ToolsUtil.loadTexture(mActivityContext, R.drawable.texture_w026)
    }

    override fun onSurfaceChanged(glUnused: GL10, width: Int, height: Int) {
        // Set the OpenGL viewport to the same size as the surface.
        GLES20.glViewport(0, 0, width, height)

        // Create a new perspective projection matrix. The height will stay the same
        // while the width will vary as per aspect ratio.
        val ratio = width.toFloat() / height
        val left = -ratio
        val bottom = -1.0f
        val top = 1.0f
        val near = 1.0f
        val far = 10.0f

        Matrix.frustumM(mProjectionMatrix, 0, left, ratio, bottom, top, near, far)
    }

    override fun onDrawFrame(glUnused: GL10) {
        val framebuffer = IntBuffer.allocate(1)
        val depthRenderbuffer = IntBuffer.allocate(1)
        val texture = IntBuffer.allocate(1)
        val texWidth = 480
        val texHeight = 480
        val maxRenderbufferSize = IntBuffer.allocate(1)
        GLES20.glGetIntegerv(GLES20.GL_MAX_RENDERBUFFER_SIZE, maxRenderbufferSize)
        // check if GL_MAX_RENDERBUFFER_SIZE is >= texWidth and texHeight
        if (maxRenderbufferSize.get(0) <= texWidth || maxRenderbufferSize.get(0) <= texHeight) {
            // cannot use framebuffer objects as we need to create
            // a depth buffer as a renderbuffer object
            // return with appropriate error
        }
        // generate the framebuffer, renderbuffer, and texture object names
        GLES20.glGenFramebuffers(1, framebuffer)
        GLES20.glGenRenderbuffers(1, depthRenderbuffer)
        GLES20.glGenTextures(1, texture)
        // bind texture and load the texture mip-level 0
        // texels are RGB565
        // no texels need to be specified as we are going to draw into
        // the texture
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture.get(0))
        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGB, texWidth, texHeight,
                0, GLES20.GL_RGB, GLES20.GL_UNSIGNED_SHORT_5_6_5, null)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR)
        // bind renderbuffer and create a 16-bit depth buffer
        // width and height of renderbuffer = width and height of
        // the texture
        GLES20.glBindRenderbuffer(GLES20.GL_RENDERBUFFER, depthRenderbuffer.get(0))
        GLES20.glRenderbufferStorage(GLES20.GL_RENDERBUFFER, GLES20.GL_DEPTH_COMPONENT16,
                texWidth, texHeight)
        // bind the framebuffer
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, framebuffer.get(0))
        // specify texture as color attachment
        GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0,
                GLES20.GL_TEXTURE_2D, texture.get(0), 0)
        // specify depth_renderbufer as depth attachment
        GLES20.glFramebufferRenderbuffer(GLES20.GL_FRAMEBUFFER, GLES20.GL_DEPTH_ATTACHMENT,
                GLES20.GL_RENDERBUFFER, depthRenderbuffer.get(0))
        // check for framebuffer complete
        val status = GLES20.glCheckFramebufferStatus(GLES20.GL_FRAMEBUFFER)
        if (status == GLES20.GL_FRAMEBUFFER_COMPLETE) {
            // render to texture using FBO
            GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f)
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

            // Do a complete rotation every 10 seconds.
            var time = SystemClock.uptimeMillis() % 10000L
            var angleInDegrees = 360.0f / 10000.0f * (2 * time.toInt())

            GLES20.glUseProgram(mProgramHandle)

            // Set program handles for cube drawing.
            mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgramHandle, "u_MVPMatrix")
            mMVMatrixHandle = GLES20.glGetUniformLocation(mProgramHandle, "u_MVMatrix")
            mTextureUniformHandle = GLES20.glGetUniformLocation(mProgramHandle, "u_Texture")
            mPositionHandle = GLES20.glGetAttribLocation(mProgramHandle, "a_Position")
            mColorHandle = GLES20.glGetAttribLocation(mProgramHandle, "a_Color")
            mTextureCoordinateHandle = GLES20.glGetAttribLocation(mProgramHandle, "a_TexCoordinate")

            // Set the active texture unit to texture unit 0.
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0)

            // Bind the texture to this unit.
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureDataHandle)

            // Tell the texture uniform sampler to use this texture in the shader by binding to texture unit 0.
            GLES20.glUniform1i(mTextureUniformHandle, 0)

            Matrix.setIdentityM(mModelMatrix, 0)
            Matrix.translateM(mModelMatrix, 0, 0.0f, -1.0f, -5.0f)
            Matrix.rotateM(mModelMatrix, 0, angleInDegrees, 1.0f, 1.0f, 0.0f)
            drawCube()

            // render to window system provided framebuffer
            GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0)
            GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

            // Do a complete rotation every 10 seconds.
            time = SystemClock.uptimeMillis() % 10000L
            angleInDegrees = 360.0f / 10000.0f * time.toInt()

            GLES20.glUseProgram(mProgramHandle)

            // Set program handles for cube drawing.
            mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgramHandle, "u_MVPMatrix")
            mMVMatrixHandle = GLES20.glGetUniformLocation(mProgramHandle, "u_MVMatrix")
            mTextureUniformHandle = GLES20.glGetUniformLocation(mProgramHandle, "u_Texture")
            mPositionHandle = GLES20.glGetAttribLocation(mProgramHandle, "a_Position")
            mColorHandle = GLES20.glGetAttribLocation(mProgramHandle, "a_Color")
            mTextureCoordinateHandle = GLES20.glGetAttribLocation(mProgramHandle, "a_TexCoordinate")

            // Set the active texture unit to texture unit 0.
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0)

            // Bind the texture to this unit.
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture.get(0)/*mTextureDataHandle*/)

            // Tell the texture uniform sampler to use this texture in the shader by binding to texture unit 0.
            GLES20.glUniform1i(mTextureUniformHandle, 0)

            Matrix.setIdentityM(mModelMatrix, 0)
            Matrix.translateM(mModelMatrix, 0, 0.0f, 0.0f, -5.0f)
            Matrix.rotateM(mModelMatrix, 0, angleInDegrees, 1.0f, 1.0f, 0.0f)
            drawCube()
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0)
        }

        // cleanup
        GLES20.glDeleteRenderbuffers(1, depthRenderbuffer)
        GLES20.glDeleteFramebuffers(1, framebuffer)
        GLES20.glDeleteTextures(1, texture)
    }

    /**
     * Draws a cube.
     */
    private fun drawCube() {
        // Pass in the position information
        mCubePositions.position(0)
        GLES20.glVertexAttribPointer(mPositionHandle, mPositionDataSize, GLES20.GL_FLOAT, false,
                0, mCubePositions)

        GLES20.glEnableVertexAttribArray(mPositionHandle)

        // Pass in the color information
        mCubeColors.position(0)
        GLES20.glVertexAttribPointer(mColorHandle, mColorDataSize, GLES20.GL_FLOAT, false,
                0, mCubeColors)
        GLES20.glEnableVertexAttribArray(mColorHandle)

        // Pass in the texture coordinate information
        mCubeTextureCoordinates.position(0)
        GLES20.glVertexAttribPointer(mTextureCoordinateHandle, mTextureCoordinateDataSize, GLES20.GL_FLOAT, false,
                0, mCubeTextureCoordinates)

        GLES20.glEnableVertexAttribArray(mTextureCoordinateHandle)

        // This multiplies the view matrix by the model matrix, and stores the result in the MVP matrix
        // (which currently contains model * view).
        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0)

        // Pass in the modelview matrix.
        GLES20.glUniformMatrix4fv(mMVMatrixHandle, 1, false, mMVPMatrix, 0)

        // This multiplies the modelview matrix by the projection matrix, and stores the result in the MVP matrix
        // (which now contains model * view * projection).
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0)

        // Pass in the combined matrix.
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0)

        // Draw the cube.
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 36)
    }

    companion object {
        /** Used for debug logs.  */
        private val TAG = "Test7Renderer"
    }
}
