package milu.kiriu2010.exdb1.opengl.cube01

import android.opengl.GLSurfaceView
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import android.opengl.GLU

// https://android.keicode.com/basics/opengl-drawing-basic-shapes.php
class MyCube01Renderer: GLSurfaceView.Renderer {
    private val cube = MyCube01()

    override fun onDrawFrame(gl: GL10) {
        // バッファクリア
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT or GL10.GL_DEPTH_BUFFER_BIT)

        gl.glMatrixMode(GL10.GL_MODELVIEW)
        gl.glLoadIdentity()
        // デフォルトの視線は、-z方向で原点にある
        // 物体の座標
        gl.glTranslatef(0f, 0f, -3f)

        // Y軸の回りに30度回転
        gl.glRotatef(30f, 0f, 1f, 0f)
        // X軸の回りに30度回転
        gl.glRotatef(30f, 1f, 0f, 0f)

        cube.draw(gl)
    }

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
        // フラスタム(frustum)とビューポートを表示が歪まないように更新する
        gl.glViewport(0, 0, width, height)

        gl.glMatrixMode(GL10.GL_PROJECTION)
        gl.glLoadIdentity()
        GLU.gluPerspective(gl, 45f, width.toFloat() / height.toFloat(), 1f, 50f)
    }

    override fun onSurfaceCreated(gl: GL10, config: EGLConfig?) {
        gl.glEnable(GL10.GL_DEPTH_TEST)
        gl.glDepthFunc(GL10.GL_LEQUAL)

        // ライティングを有効化する
        // ------------------------------------
        // 光源
        // 環境光(ambient)  (0,0,0,1)
        // 拡散光(diffuse)  (1,1,1,1)
        // 鏡面光(specular) (1,1,1,1)
        // ------------------------------------
        // マテリアル
        // 環境光(ambient)  (0.2,0.2,0.2,1.0)
        // 拡散光(diffuse)  (0.8,0.8,0.8,1.0)
        // 鏡面光(specular) (0.0,0.0,0.0,1.0)
        // ------------------------------------
        // 環境光 0
        // 拡散光のみ有効
        // 鏡面光 0
        // ------------------------------------
        gl.glEnable(GL10.GL_LIGHTING)
        gl.glEnable(GL10.GL_LIGHT0)
    }

}