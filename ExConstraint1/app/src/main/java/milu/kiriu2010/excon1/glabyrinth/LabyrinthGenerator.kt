package milu.kiriu2010.excon1.glabyrinth

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

object LabyrinthGenerator {

    val FLOOR = 0

    val WALL = 1

    val START = 2

    val GOAL = 3

    val HOLE = 4

    val POLL = -1

    enum class Direction {
        TOP,
        LEFT,
        RIGHT,
        BOTTOM
    }

    class MapResult internal constructor(internal val result: Array<IntArray>, internal val startY: Int, internal val startX: Int)

    fun getMap(seed: Int, horizontalBlockNum: Int, verticalBlockNum: Int): MapResult {

        var result = Array(verticalBlockNum) { IntArray(horizontalBlockNum) }

        // 配列の初期化
        for (y in 0 until verticalBlockNum) {

            for (x in 0 until horizontalBlockNum) {

                if (y == 0 || y == verticalBlockNum - 1) {
                    // １行目と最終行は壁に設定
                    result[y][x] = WALL

                } else if (x == 0 || x == horizontalBlockNum - 1) {
                    // １列目と最終列は壁に設定
                    result[y][x] = WALL

                } else if (x > 1 && x % 2 == 0 && y > 1 && y % 2 == 0) {

                    // ２つ毎に壁
                    result[y][x] = POLL
                } else {
                    // その他は床に設定
                    result[y][x] = FLOOR
                }
            }
        }

        // 迷路を生成
        result = generateLabyrinth(seed, result)

        var startY = -1
        var startX = -1

        // スタート地点を下端の右端から最初の床に設定
        for (y in verticalBlockNum - 1 downTo 0) {
            for (x in horizontalBlockNum - 1 downTo 0) {
                if (result[y][x] == FLOOR) {
                    startY = y
                    startX = x
                    result[startY][startX] = START
                    break
                }
            }
            if (startY != -1 && startX != -1) {
                break
            }
        }

        val exam = Array(verticalBlockNum) { IntArray(horizontalBlockNum) }

        setGoalPosition(result, startY, startX, exam, 0)

        var maxScore = 0
        var maxScoreY = 0
        var maxScoreX = 0
        for (y in 0 until verticalBlockNum) {
            for (x in 0 until horizontalBlockNum) {
                if (exam[y][x] > maxScore) {
                    maxScore = exam[y][x]
                    maxScoreY = y
                    maxScoreX = x
                }
            }
        }

        // ゴールの設定
        result[maxScoreY][maxScoreX] = GOAL

        return MapResult(result, startY, startX)

    }

    private fun setGoalPosition(map: Array<IntArray>, y: Int, x: Int, exam: Array<IntArray>, scoreA: Int): Array<IntArray> {
        var score = scoreA
        score++

        if (y < 0 || x < 0 || y >= map.size || x >= map[0].size) {
            return exam
        }

        if (map[y][x] == WALL) {
            exam[y][x] = -1
            return exam
        }

        if (exam[y][x] == 0 || exam[y][x] > score) {
            exam[y][x] = score

            setGoalPosition(map, y, x + 1, exam, score)
            setGoalPosition(map, y + 1, x, exam, score)
            setGoalPosition(map, y, x - 1, exam, score)
            setGoalPosition(map, y - 1, x, exam, score)
        }
        return exam
    }

    private fun generateLabyrinth(seed: Int, map: Array<IntArray>): Array<IntArray> {
        val rand = Random(seed.toLong())

        val horizontal = map[0].size
        val vertical = map.size

        for (y in 0 until vertical) {
            for (x in 0 until horizontal) {
                if (map[y][x] == POLL) {

                    // 壁を作る方向
                    var directionList: MutableList<Direction>

                    // 閉鎖路を防ぐため、２段目以降は上向きに壁を設定しない
                    if (y == 1) {
                        directionList = ArrayList(
                                Arrays.asList(Direction.TOP, Direction.LEFT, Direction.RIGHT, Direction.BOTTOM))
                    } else {
                        directionList = ArrayList(
                                Arrays.asList(Direction.LEFT, Direction.RIGHT, Direction.BOTTOM))
                    }

                    do {
                        val direction = directionList[rand.nextInt(directionList.size)]
                        if (setDirection(y, x, direction, map)) {
                            break
                        } else {
                            directionList.remove(direction)
                        }
                    } while (directionList.size > 0)
                }
            }
        }

        // 設定する穴の個数
        var holeCount = seed + 1
        if (holeCount > vertical + horizontal) {
            holeCount = vertical + horizontal
        }

        setHoles(holeCount, rand, vertical, horizontal, map)

        return map
    }

    private fun setHoles(holeCount: Int, rand: Random, vertical: Int, horizontal: Int, map: Array<IntArray>) {

        do {
            // 外壁は穴にしない
            val y = rand.nextInt(vertical - 2) + 1
            val x = rand.nextInt(horizontal - 2) + 1

            if (map[y][x] == WALL) {
                map[y][x] = HOLE
            }

        } while (rand.nextInt(holeCount) != 0)
    }

    private fun setDirection(yA: Int, xA: Int, direction: Direction, map: Array<IntArray>): Boolean {
        var y = yA
        var x = xA
        map[y][x] = WALL

        when (direction) {
            LabyrinthGenerator.Direction.LEFT -> x -= 1
            LabyrinthGenerator.Direction.RIGHT -> x += 1
            LabyrinthGenerator.Direction.BOTTOM -> y -= 1
            LabyrinthGenerator.Direction.TOP -> y += 1
        }

        if (x < 0 || y < 0 || x >= map[0].size || y >= map.size) {
            return false
        }

        if (map[y][x] == WALL) {
            return false
        }

        map[y][x] = WALL

        return true
    }
}
