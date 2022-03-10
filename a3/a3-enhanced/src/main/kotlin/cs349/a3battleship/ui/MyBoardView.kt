package cs349.a3battleship.ui

import cs349.a3battleship.Battleship
import cs349.a3battleship.model.*
import javafx.scene.shape.Rectangle
import javafx.scene.Group
import javafx.scene.paint.Color

class MyBoardView(private val game: Game, controller: Battleship): IView, Group(){
    private val boardRectangle = Rectangle(300.0,300.0)
    private val cells = mutableListOf<Array<Rectangle>>()
    override fun updateView() {
        for (i in 0 until cells.size){
            for (j in 0 until cells.size){
                val currCell = cells[j][i]
                val cellState = game.getBoard(Player.Human)[j][i]
                if (game.gameState == Game.GameState.WonHuman){
                    if (cellState != CellState.ShipSunk){
                        currCell.fill = Color.CADETBLUE
                    }
                }
                else{
                    if (cellState == CellState.Attacked){
                        currCell.fill = Color.LIGHTGRAY
                    }
                    if (cellState == CellState.ShipHit){
                        currCell.fill = Color.CORAL
                    }
                    if (cellState == CellState.ShipSunk){
                        currCell.fill = Color.DARKGRAY
                    }
                }

            }
        }

    }
    init {
        boardRectangle.fill = Color.TRANSPARENT
        boardRectangle.stroke = Color.BLACK
        children.add(boardRectangle)

        val lenPerCell = 300.0/game.dimension

        for (i in 0 until game.dimension){
            val row = mutableListOf<Rectangle>()
            for (j in 0 until game.dimension){
                val currCell = Rectangle(boardRectangle.x+j*lenPerCell, boardRectangle.y+i*lenPerCell, lenPerCell, lenPerCell)
                currCell.stroke = Color.BLACK
                currCell.fill = Color.CADETBLUE
                children.add(currCell)
                row.add(currCell)
            }
            cells.add(row.toTypedArray())
        }
        game.addView(this)
    }
}