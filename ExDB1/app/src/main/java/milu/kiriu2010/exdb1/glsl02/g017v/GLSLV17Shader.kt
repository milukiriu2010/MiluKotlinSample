package milu.kiriu2010.exdb1.glsl02.g017v

import android.opengl.GLES20
import milu.kiriu2010.gui.basic.MyGLES20Func
import milu.kiriu2010.gui.model.MgModelAbs
import milu.kiriu2010.gui.shader.es20.ES20MgShader
import milu.kiriu2010.gui.vbo.es20.ES20VBOAbs

// -------------------------------------------
// オブジェクトを行列で回転
// -------------------------------------------
// https://wgld.org/d/glsl/g017.html
// -------------------------------------------
class GLSLV17Shader: ES20MgShader() {
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
            // 法線の出力をするかどうか
            //   0: ライティング
            //   1: 法線
            uniform   int       u_showNormal;

            const vec3 cPos = vec3(0.0, 0.0, 3.0);
            const vec3 cDir = vec3(0.0, 0.0, -1.0);
            const vec3 cUp  = vec3(0.0, 1.0, 0.0);

            // 光源位置
            const vec3  lightDir = vec3(0.577, 0.577, 0.577);

            // rotate
            vec3 rotate(vec3 p, float angle, vec3 axis){
                vec3  a = normalize(axis);
                float s = sin(angle);
                float c = cos(angle);
                float r = 1.0 - c;
                mat3  m = mat3(
                    a.x * a.x * r + c,
                    a.y * a.x * r + a.z * s,
                    a.z * a.x * r - a.y * s,
                    a.x * a.y * r - a.z * s,
                    a.y * a.y * r + c,
                    a.z * a.y * r + a.x * s,
                    a.x * a.z * r + a.y * s,
                    a.y * a.z * r - a.x * s,
                    a.z * a.z * r + c
                );
                return m * p;
            }

            // smoothing min
            float smoothMin(float d1, float d2, float k){
                float h = exp(-k * d1) + exp(-k * d2);
                return -log(h) / k;
            }

            // torus
            float distFuncTorus(vec3 p, vec2 r){
                vec2 d = vec2(length(p.xy) - r.x, p.z);
                return length(d) - r.y;
            }

            // box
            float distFuncBox(vec3 p){
                return length(max(abs(p) - vec3(2.0, 0.1, 0.5), 0.0)) - 0.1;
            }

            // cylinder
            //  r.x: 円柱の太さ
            //  r.y: 円柱の長さ
            float distFuncCylinder(vec3 p, vec2 r){
                vec2 d = abs(vec2(length(p.xy), p.z)) - r;
                return min(max(d.x, d.y), 0.0) + length(max(d, 0.0)) - 0.1;
            }

            // distance function
            float distFunc(vec3 p){
                vec3  q  = rotate(p, radians(u_time * 10.0), vec3(1.0, 0.5, 0.0));
                float d1 = distFuncTorus(q, vec2(1.5, 0.25));
                float d2 = distFuncBox(q);
                float d3 = distFuncCylinder(q, vec2(0.75, 0.25));
                return smoothMin(smoothMin(d1, d2, 16.0), d3, 16.0);
            }

            // ---------------------------------------
            // 法線を算出する
            // ---------------------------------------
            //   p: レイとオブジェクトの交点の座標位置
            // ---------------------------------------
            vec3 genNormal(vec3 p){
                float d = 0.0001;
                // -----------------------------------------
                // 正負それぞれにほんの少しだけずらした座標を
                // distanceFuncに渡すことによって、
                // その戻り値から勾配を計算している
                // これにより、各軸に対して
                // どの程度の傾きになっているかわかる
                // -----------------------------------------
                return normalize(vec3(
                    distFunc(p + vec3(  d, 0.0, 0.0)) - distFunc(p + vec3( -d, 0.0, 0.0)),
                    distFunc(p + vec3(0.0,   d, 0.0)) - distFunc(p + vec3(0.0,  -d, 0.0)),
                    distFunc(p + vec3(0.0, 0.0,   d)) - distFunc(p + vec3(0.0, 0.0,  -d))
                ));
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

                // camera and ray
                vec3 cSide = cross(cDir, cUp);
                float targetDepth = 1.0;
                vec3 ray = normalize(cSide * p.x + cUp * p.y + cDir * targetDepth);

                // marching loop
                float tmp, dist;
                tmp = 0.0;
                vec3 dPos = cPos;
                for(int i = 0; i < 256; i++){
                    dist = distFunc(dPos);
                    tmp += dist;
                    dPos = cPos + tmp * ray;
                }

                // hit check
                vec3 color;
                if (abs(dist) < 0.001) {
                    vec3  normal = genNormal(dPos);
                    // 法線の出力
                    if ( bool(u_showNormal) ) {
                        gl_FragColor = vec4(normal,1.0);
                    }
                    // ライティング
                    else {
                        float diff   = clamp(dot(lightDir,normal), 0.1, 1.0);
                        gl_FragColor = vec4(vec3(diff), 1.0);
                    }
                }
                else {
                    gl_FragColor = vec4(vec3(0.0), 1.0);
                }
            }
            """.trimIndent()

    override fun loadShader(): ES20MgShader {
        // 頂点シェーダを生成
        svhandle = MyGLES20Func.loadShader(GLES20.GL_VERTEX_SHADER, scv)
        // フラグメントシェーダを生成
        sfhandle = MyGLES20Func.loadShader(GLES20.GL_FRAGMENT_SHADER, scf)

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
        hUNI = IntArray(4)

        // uniform(時間)
        hUNI[0] = GLES20.glGetUniformLocation(programHandle,"u_time")
        MyGLES20Func.checkGlError("u_time:glGetUniformLocation")

        // uniform(タッチ位置)
        hUNI[1] = GLES20.glGetUniformLocation(programHandle,"u_mouse")
        MyGLES20Func.checkGlError("u_mouse:glGetUniformLocation")

        // uniform(解像度)
        hUNI[2] = GLES20.glGetUniformLocation(programHandle,"u_resolution")
        MyGLES20Func.checkGlError("u_resolution:glGetUniformLocation")

        // uniform(法線の出力をするかどうか)
        hUNI[3] = GLES20.glGetUniformLocation(programHandle,"u_showNormal")
        MyGLES20Func.checkGlError("u_showNormal:glGetUniformLocation")

        return this
    }

    fun draw(model: MgModelAbs,
             bo: ES20VBOAbs,
             u_time: Float,
             u_mouse: FloatArray,
             u_resolution: FloatArray,
             u_showNormal: Int) {
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

        // uniform(法線の出力をするかどうか)
        GLES20.glUniform1i(hUNI[3],u_showNormal)
        MyGLES20Func.checkGlError2("u_showNormal",this,model)

        // モデル描画
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, model.datIdx.size, GLES20.GL_UNSIGNED_SHORT, model.bufIdx)

        // リソース解放
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,0)
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER,0)
    }
}
