package milu.kiriu2010.exdb1.opengl

import android.opengl.GLSurfaceView
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import android.opengl.GLES10.glTranslatef
import android.opengl.GLES10.glLoadIdentity
import android.opengl.GLES10.glMatrixMode
import android.opengl.GLU
import android.opengl.GLES10.glLoadIdentity
import android.opengl.GLES10.glMatrixMode





class MyRenderer: GLSurfaceView.Renderer {
    private val cube = MyCube()

    override fun onDrawFrame(gl: GL10) {
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT or GL10.GL_DEPTH_BUFFER_BIT)

        gl.glMatrixMode(GL10.GL_MODELVIEW)
        gl.glLoadIdentity()
        gl.glTranslatef(0f, 0f, -3f)

        gl.glRotatef(30f, 0f, 1f, 0f)
        gl.glRotatef(30f, 1f, 0f, 0f)

        cube.draw(gl)
    }

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
        gl.glViewport(0, 0, width, height)

        gl.glMatrixMode(GL10.GL_PROJECTION)
        gl.glLoadIdentity()
        GLU.gluPerspective(gl, 45f, width.toFloat() / height.toFloat(), 1f, 50f)
    }

    override fun onSurfaceCreated(gl: GL10, config: EGLConfig?) {
        gl.glEnable(GL10.GL_DEPTH_TEST)
        gl.glDepthFunc(GL10.GL_LEQUAL)

        gl.glEnable(GL10.GL_LIGHTING)
        gl.glEnable(GL10.GL_LIGHT0)
    }

}