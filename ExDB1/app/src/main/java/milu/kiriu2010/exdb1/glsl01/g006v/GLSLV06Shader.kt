package milu.kiriu2010.exdb1.glsl01.g006v

import android.opengl.GLES20
import milu.kiriu2010.gui.basic.MyGLES20Func
import milu.kiriu2010.gui.model.MgModelAbs
import milu.kiriu2010.gui.shader.es20.ES20MgShader
import milu.kiriu2010.gui.vbo.es20.ES20VBOAbs

// ----------------------------------------------
// ジュリア集合:VBOあり
// OpenGL ES 2.0
// ----------------------------------------------
// https://wgld.org/d/glsl/g006.html
// ----------------------------------------------
class GLSLV06Shader: ES20MgShader() {
    // 頂点シェーダ
    private val scv =
            """
            attribute vec3  a_Position;

            void main() {
                gl_Position = vec4(a_Position,1.0);
            }
            """.trimIndent()

    // フラグメントシェーダ
    private val scf =
            """
            precision mediump   float;

            // 経過時間(ミリ秒を1/1000)
            uniform   float     u_time;
            // 0.0-1.0に正規化
            uniform   vec2      u_mouse;
            // 描画領域の幅・高さ
            uniform   vec2      u_resolution;

            vec3 hsv(float h, float s, float v){
                vec4 t = vec4(1.0, 2.0 / 3.0, 1.0 / 3.0, 3.0);
                vec3 p = abs(fract(vec3(h) + t.xyz) * 6.0 - vec3(t.w));
                return v * mix(vec3(t.x), clamp(p - vec3(t.x), 0.0, 1.0), s);
            }

            void main() {
                // ----------------------------------------------------
                // マウス座標の正規化
                // ----------------------------------------------------
                // 0～1の範囲で入ってくるマウスの位置を
                // -1～1の範囲に正規化している
                // Y座標は上下逆のため、正負を逆転している
                // ----------------------------------------------------
                vec2 m = vec2(u_mouse.x*2.0-1.0,-u_mouse.y*2.0+1.0);
                // ----------------------------------------------------
                // フラグメント座標の正規化
                // ----------------------------------------------------
                // 今から処理しようとしているスクリーン上のピクセル位置を
                // -1～1の範囲に正規化している
                // ----------------------------------------------------
                vec2 p = (gl_FragCoord.xy * 2.0 - u_resolution)/min(u_resolution.x, u_resolution.y);

                int j = 0;
                // -------------------------------------
                // 原点を少しずらす
                // -------------------------------------
                vec2 x = vec2(-0.345, 0.654);
                // -------------------------------------
                // マウス座標を使って拡大度を変更
                // -------------------------------------
                vec2 y = vec2(u_time * 0.005, 0.0);
                // 漸化式Zの初期値(Z0)
                vec2  z = p;
                // 漸化式Zの繰り返し処理
                for(int i = 0; i < 360; i++){
                    j++;
                    // 発散判定
                    if(length(z) > 2.0){break;}
                    // z.x ⇒ Znの実数部
                    // z.y ⇒ Znの虚数部
                    // x+y ⇒ C
                    z = vec2(z.x * z.x - z.y * z.y, 2.0 * z.x * z.y) + x + y;
                }

                // 時間の経過で色HSV出力する
                float h   = abs(mod(u_time*15.0-float(j), 360.0) / 360.0);
                vec3  rgb = hsv(h, 1.0, 1.0);
                // 漸化式で繰り返した回数をもとに輝度を決める
                float t   = float(j) / 360.0;
                gl_FragColor = vec4(rgb * t, 1.0);
            }
            """.trimIndent()

    override fun loadShader(): ES20MgShader {
        // 頂点シェーダを生成
        val svhandle = MyGLES20Func.loadShader(GLES20.GL_VERTEX_SHADER, scv)
        // フラグメントシェーダを生成
        val sfhandle = MyGLES20Func.loadShader(GLES20.GL_FRAGMENT_SHADER, scf)

        // プログラムオブジェクトの生成とリンク
        programHandle = MyGLES20Func.createProgram(svhandle,sfhandle)

        // ----------------------------------------------
        // attributeハンドルに値をセット
        // ----------------------------------------------
        hATTR = IntArray(1)
        // 属性(頂点)
        hATTR[0] = GLES20.glGetAttribLocation(programHandle, "a_Position").also {
            // attribute属性を有効にする
            // ここで呼ばないと描画されない
            GLES20.glEnableVertexAttribArray(it)
            MyGLES20Func.checkGlError("a_Position:glEnableVertexAttribArray")
            // attribute属性を登録
            GLES20.glVertexAttribPointer(it,3,GLES20.GL_FLOAT,false,0,0)
            MyGLES20Func.checkGlError("a_Position:glVertexAttribPointer")
        }
        MyGLES20Func.checkGlError("a_Position:glGetAttribLocation")

        // ----------------------------------------------
        // uniformハンドルに値をセット
        // ----------------------------------------------
        hUNI = IntArray(3)

        // uniform(時間)
        hUNI[0] = GLES20.glGetUniformLocation(programHandle,"u_time")
        MyGLES20Func.checkGlError("u_time:glGetUniformLocation")

        // uniform(タッチ位置)
        hUNI[1] = GLES20.glGetUniformLocation(programHandle,"u_mouse")
        MyGLES20Func.checkGlError("u_mouse:glGetUniformLocation")

        // uniform(解像度)
        hUNI[2] = GLES20.glGetUniformLocation(programHandle,"u_resolution")
        MyGLES20Func.checkGlError("u_resolution:glGetUniformLocation")

        return this
    }

    fun draw(model: MgModelAbs,
             bo: ES20VBOAbs,
             u_time: Float,
             u_mouse: FloatArray,
             u_resolution: FloatArray) {
        GLES20.glUseProgram(programHandle)
        MyGLES20Func.checkGlError2("UseProgram",this,model)

        // attribute(頂点)
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,bo.hVBO[0])
        GLES20.glVertexAttribPointer(hATTR[0],3,GLES20.GL_FLOAT,false,0,0)
        MyGLES20Func.checkGlError2("a_Position",this,model)

        // uniform(時間)
        GLES20.glUniform1f(hUNI[0],u_time)
        MyGLES20Func.checkGlError2("u_time",this,model)

        // uniform(タッチ位置)
        GLES20.glUniform2fv(hUNI[1],1,u_mouse,0)
        MyGLES20Func.checkGlError2("u_mouse",this,model)

        // uniform(解像度)
        GLES20.glUniform2fv(hUNI[2],1,u_resolution,0)
        MyGLES20Func.checkGlError2("u_resolution",this,model)

        // モデル描画
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, model.datIdx.size, GLES20.GL_UNSIGNED_SHORT, model.bufIdx)

        // リソース解放
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,0)
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER,0)
    }
}
