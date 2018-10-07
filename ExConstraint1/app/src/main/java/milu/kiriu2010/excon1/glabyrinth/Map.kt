package milu.kiriu2010.excon1.glabyrinth

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.R.attr.startX
import android.R.attr.startY


class Map(w: Int, h: Int, private val blockSize: Int, private val callback: LabyrinthView.Callback, private val stageSeed: Int) : Ball.OnMoveListener {

    private var horizontalBlockNum: Int = 0
    private var verticalBlockNum: Int = 0

    private var block: Array<Array<Block?>>? = null

    private val targetBlock = Array<Array<Block?>>(3) { arrayOfNulls(3) }
    var startBlock: Block? = null
        private set

    init {
        horizontalBlockNum = w / blockSize
        verticalBlockNum = h / blockSize

        if (horizontalBlockNum % 2 == 0) {
            horizontalBlockNum--
        }
        if (verticalBlockNum % 2 == 0) {
            verticalBlockNum--
        }

        createMap()
    }

    private fun createMap() {

        val map = LabyrinthGenerator.getMap(stageSeed, horizontalBlockNum, verticalBlockNum)

        //block = Array(verticalBlockNum) { arrayOfNulls(horizontalBlockNum) }
        //block = Array<Array<Block?>>(verticalBlockNum, { arrayOfNulls<Block?>(horizontalBlockNum) } )
        block = Array(verticalBlockNum, { arrayOfNulls<Block?>(horizontalBlockNum) } )
        for (y in 0 until verticalBlockNum) {
            for (x in 0 until horizontalBlockNum) {
                val type = map.result[y][x]
                val left = x * blockSize + 1
                val top = y * blockSize + 1
                val right = left + blockSize - 2
                val bottom = top + blockSize - 2
                block!![y][x] = Block(type, left, top, right, bottom)
            }
        }
        startBlock = block!![map.startY][map.startX]
    }

    internal fun drawMap(canvas: Canvas) {
        for (y in 0 until verticalBlockNum) {
            for (x in 0 until horizontalBlockNum) {
                block!![y][x]!!.draw(canvas)
            }
        }
    }

    override fun canMove(left: Int, top: Int, right: Int, bottom: Int): Boolean {
        val verticalBlock = top / blockSize
        val horizontalBlock = left / blockSize

        // 検索対象のブロックを設定
        seTargetBlock(verticalBlock, horizontalBlock)

        val yLen = targetBlock.size
        val xLen = targetBlock[0].size

        for (y in 0 until yLen) {
            for (x in 0 until xLen) {
                if (targetBlock[y][x] == null) {
                    continue
                }
                if (targetBlock[y][x]?.type == Block.TYPE_WALL && targetBlock[y][x]?.rect!!.intersects(left, top, right, bottom)) {
                    return false
                } else if (targetBlock[y][x]?.type == Block.TYPE_GOAL && targetBlock[y][x]?.rect!!.contains(left, top, right, bottom)) {
                    callback.onGoal()
                    return true
                } else if (targetBlock[y][x]?.type == Block.TYPE_HOLE) {

                    val ballCenterX = left + (right - left) / 2
                    val ballCenterY = top + (bottom - top) / 2

                    val distanceX = targetBlock[y][x]?.rect?.centerX()!! - ballCenterX
                    val distanceY = targetBlock[y][x]?.rect?.centerY()!! - ballCenterY

                    val distance = Math.sqrt(Math.pow(distanceX.toDouble(), 2.0) + Math.pow(distanceY.toDouble(), 2.0))

                    // 穴に落ちる判定
                    if (distance < blockSize / 2) {
                        callback.onHole()
                    }
                }
            }
        }
        return true
    }

    private fun seTargetBlock(verticalBlock: Int, horizontalBlock: Int) {
        targetBlock[1][1] = getBlock(verticalBlock, horizontalBlock)

        targetBlock[0][0] = getBlock(verticalBlock - 1, horizontalBlock - 1)
        targetBlock[0][1] = getBlock(verticalBlock - 1, horizontalBlock)
        targetBlock[0][2] = getBlock(verticalBlock - 1, horizontalBlock + 1)

        targetBlock[1][0] = getBlock(verticalBlock, horizontalBlock - 1)
        targetBlock[1][2] = getBlock(verticalBlock, horizontalBlock + 1)

        targetBlock[2][0] = getBlock(verticalBlock + 1, horizontalBlock - 1)
        targetBlock[2][1] = getBlock(verticalBlock + 1, horizontalBlock)
        targetBlock[2][2] = getBlock(verticalBlock + 1, horizontalBlock + 1)
    }

    private fun getBlock(y: Int, x: Int): Block? {
        return if (y < 0 || x < 0 || y >= verticalBlockNum || x >= horizontalBlockNum) {
            null
        } else block!![y][x]
    }

    class Block (val type: Int, left: Int, top: Int, right: Int, bottom: Int) {

        val rect: Rect

        private val paint: Paint?
            get() {
                when (type) {
                    TYPE_FLOOR -> return PAINT_FLOOR
                    TYPE_START -> return PAINT_START
                    TYPE_GOAL -> return PAINT_GOAL
                    TYPE_HOLE -> return PAINT_HOLE
                    TYPE_WALL -> return PAINT_WALL
                }
                return null
            }

        init {
            rect = Rect(left, top, right, bottom)
        }

        fun draw(canvas: Canvas) {
            canvas.drawRect(rect, paint!!)

        }

        companion object {

            val TYPE_FLOOR = 0
            val TYPE_WALL = 1
            val TYPE_START = 2
            val TYPE_GOAL = 3
            val TYPE_HOLE = 4

            val PAINT_FLOOR = Paint()
            val PAINT_WALL = Paint()
            val PAINT_START = Paint()
            val PAINT_GOAL = Paint()
            val PAINT_HOLE = Paint()

            init {
                PAINT_FLOOR.color = Color.GRAY
                PAINT_WALL.color = Color.BLACK
                PAINT_START.color = Color.DKGRAY
                PAINT_GOAL.color = Color.YELLOW
                PAINT_HOLE.color = Color.rgb(32, 32, 32)
            }
        }
    }
}
