package cs349.a3battleship.ulti

import cs349.a3battleship.model.Cell
import kotlin.math.roundToInt

fun translateToCellVertical(translateX: Double, translateY: Double): Cell{
    val distanceX = translateX + 329.0
    val distanceY = translateY - 17.0
    val cellX = (distanceX / 30.0).roundToInt()
    val cellY = (distanceY / 30.0).roundToInt()

    return Cell(cellX, cellY)
}

fun translateToCellHorizontal(translateX: Double, translateY: Double): Cell{
    val distanceX = translateX + 329.0
    val distanceY = translateY - 17.0
    val cellX = (distanceX / 30.0).roundToInt() - 1
    val cellY = (distanceY / 30.0).roundToInt()

    return Cell(cellX, cellY)
}

fun snapVertical(translateX: Double, translateY: Double, cell: Cell): Pair<Double, Double>{
    val distanceX = translateX + 329.0
    val distanceY = translateY - 17.0
    val cellX = cell.x
    val cellY = cell.y
    val cellDistanceX = cellX*30.0
    val cellDistanceY = cellY*30.0
    return Pair(cellDistanceX-distanceX, cellDistanceY-distanceY)
}
fun snapHorizontal(translateX: Double, translateY: Double, cell: Cell): Pair<Double, Double>{
    val distanceX = translateX + 329.0
    val distanceY = translateY - 17.0
    val cellX = cell.x
    val cellY = cell.y
    val cellDistanceX = (cellX+1)*30.0
    val cellDistanceY = cellY*30.0
    return Pair(cellDistanceX-distanceX, cellDistanceY-distanceY)
}