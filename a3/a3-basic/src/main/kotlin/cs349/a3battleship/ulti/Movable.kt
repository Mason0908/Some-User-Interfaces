package cs349.a3battleship.ulti

import cs349.a3battleship.model.Cell
import cs349.a3battleship.model.Game
import cs349.a3battleship.model.Orientation
import cs349.a3battleship.model.Player
import cs349.a3battleship.model.ships.ShipType
import cs349.a3battleship.ui.ShipView
import javafx.event.EventHandler
import javafx.scene.Node
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import javafx.scene.transform.Transform

class MovableManager(parent: Node, game: Game) {

    private var movingNode: ShipView? = null
    private val occupiedCell = mutableListOf<Cell>()

    // the offset captured at start of drag
    private var offsetX = 0.0
    private var offsetY = 0.0
    private var startX = 0.0
    private var transformed = false

    init {

        // important that this is in bubble phase, not capture phase
        parent.addEventHandler(MouseEvent.MOUSE_CLICKED) { e ->
            val node = movingNode
            if (e.button == MouseButton.PRIMARY){
                if (node != null) {
                    // check if fleet is inside the board
                    if (node.orientation == Orientation.VERTICAL){
                        if (node.translateX + startX < -329.0 || node.translateX + startX > -59.0){
                            if (node.orientation == Orientation.HORIZONTAL){
                                node.transforms.add(Transform.rotate(-90.0,-offsetX - 360.0 - startX,-offsetY - 33.0))
                                node.orientation = Orientation.VERTICAL
                            }
                            node.translateX = 0.0
                            node.translateY = 0.0
                        }
                        else if (node.translateY < 17.0 || node.translateY + node.height > 317.0){
                            if (node.orientation == Orientation.HORIZONTAL){
                                node.transforms.add(Transform.rotate(-90.0,-offsetX - 360.0 - startX,-offsetY - 33.0))
                                node.orientation = Orientation.VERTICAL
                            }
                            node.translateX = 0.0
                            node.translateY = 0.0
                        }
                        else{
                            var cell = translateToCellVertical(node.translateX + startX, node.translateY)
                            var overlapped = false
                            var tempCell = cell.copy()
                            for (i in 1 .. node.length){
                                if (occupiedCell.contains(tempCell)){
                                    overlapped = true
                                    break
                                }
                                tempCell = Cell(tempCell.x, tempCell.y+1)
                            }
                            if (!overlapped){
                                val snapDis = snapVertical(node.translateX + startX, node.translateY, cell)
                                node.translateX += snapDis.first
                                node.translateY += snapDis.second
                                var tempCell1 = cell.copy()
                                for (i in 1 .. node.length){
                                    occupiedCell.add(tempCell1)
                                    tempCell1 = Cell(tempCell1.x, tempCell1.y+1)
                                }
                                game.placeShip(Player.Human, node.shipType, node.orientation, cell)
                            }
                            else{
                                node.translateX = 0.0
                                node.translateY = 0.0
                            }

                        }
                    }
                    else{
                        if (node.translateX + startX - node.length*30.0 < -329.0 || node.translateX + startX > -29.0){
                            node.transforms!!.add(Transform.rotate(-90.0,0.0,0.0))
                            node.translateX = 0.0
                            node.translateY = 0.0
                            node.orientation = Orientation.VERTICAL
                        }
                        else if (node.translateY < 17.0 || node.translateY + 30.0 > 317.0){
                            node.transforms!!.add(Transform.rotate(-90.0,0.0,0.0))
                            node.translateX = 0.0
                            node.translateY = 0.0
                            node.orientation = Orientation.VERTICAL
                        }
                        else{
                            var cell = translateToCellHorizontal(node.translateX + startX, node.translateY)
                            println(cell)
                            var overlapped = false
                            var tempCell = cell.copy()
                            for (i in 1 .. node.length){
                                if (occupiedCell.contains(tempCell)){
                                    overlapped = true
                                    break
                                }
                                tempCell = Cell(tempCell.x-1, tempCell.y)
                            }
                            if (!overlapped){
                                val snapDis = snapHorizontal(node.translateX + startX, node.translateY, cell)
                                node.translateX += snapDis.first
                                node.translateY += snapDis.second
                                var tempCell1 = cell.copy()
                                for (i in 1 .. node.length){
                                    occupiedCell.add(tempCell1)
                                    tempCell1 = Cell(tempCell1.x-1, tempCell1.y)
                                }
                                game.placeShip(Player.Human, node.shipType, node.orientation, cell)
                            }
                            else{
                                node.translateX = 0.0
                                node.translateY = 0.0
                            }
                        }

                    }

                    movingNode = null
                }
            }
            else if (e.button == MouseButton.SECONDARY){
                val node = movingNode
                if (node != null){
                    with(node){
                        if (this.orientation == Orientation.VERTICAL && !transformed){
                            node.translateX += -offsetX - 360.0 - startX
                            node.translateY += -offsetY - 33.0
                            this.transforms!!.add(Transform.rotate(90.0,0.0,0.0))
                            this.orientation = Orientation.HORIZONTAL
                        }
                        else if (this.orientation == Orientation.HORIZONTAL && transformed){
                            node.translateX -= -offsetX - 360.0 - startX
                            node.translateY -= -offsetY - 33.0
                            this.transforms!!.add(Transform.rotate(-90.0,0.0,0.0))
                            this.orientation = Orientation.VERTICAL
                        }
                        else if (this.orientation == Orientation.HORIZONTAL && !transformed){
                            node.translateX += -offsetX - 360.0 - startX
                            node.translateY += -offsetY - 33.0
                            this.transforms!!.add(Transform.rotate(-90.0,0.0,0.0))
                            this.orientation = Orientation.VERTICAL
                        }
                        else if (this.orientation == Orientation.VERTICAL && transformed){
                            node.translateX -= -offsetX - 360.0 - startX
                            node.translateY -= -offsetY - 33.0
                            this.transforms!!.add(Transform.rotate(90.0,0.0,0.0))
                            this.orientation = Orientation.HORIZONTAL
                        }
                    }
                    transformed = when(transformed){
                        true -> false
                        false -> true
                    }
                }

            }

        }

        parent.addEventFilter(MouseEvent.MOUSE_MOVED) { e ->
            val node = movingNode
            if (node != null) {
                node.translateX = e.sceneX + offsetX
                node.translateY = e.sceneY + offsetY
                if (node.orientation == Orientation.HORIZONTAL && transformed){
                    node.translateX += -offsetX - 360.0 - startX
                    node.translateY += -offsetY - 33.0
                }
                if (node.orientation == Orientation.VERTICAL && transformed){
                    node.translateX += -offsetX - 360.0 - startX
                    node.translateY += -offsetY - 33.0
                }
                // we don't want to drag the background too
                e.consume()
            }
        }
    }

    fun makeMovable(node: ShipView, game: Game) {
        node.onMouseClicked = EventHandler { e ->
            if (game.gameState == Game.GameState.SetupHuman){
                if (movingNode == null) {

                    this.movingNode = node
                    this.transformed = false
                    when(movingNode?.shipType){
                        ShipType.Battleship -> {
                            startX = 111.0
                        }
                        ShipType.Carrier -> {
                            startX = 148.0
                        }
                        ShipType.Cruiser -> {
                            startX = 37.0
                        }
                        ShipType.Destroyer -> {
                            startX = 0.0
                        }
                        ShipType.Submarine -> {
                            startX = 74.0
                        }
                    }
                    if (node.orientation == Orientation.VERTICAL && node.translateX + startX >= -329.0 && node.translateX + startX <= -59.0 &&
                        node.translateY >= 17.0 && node.translateY + node.height <= 317.0){
                        var cell = translateToCellVertical(node.translateX + startX, node.translateY)
                        game.removeShip(Player.Human, cell)
                        for (p in 1 .. node.length){
                            occupiedCell.removeIf{
                                it.x == cell.x && it.y == cell.y
                            }
                            cell = Cell(cell.x, cell.y+1)
                        }
                    }
                    else if (node.orientation == Orientation.HORIZONTAL && node.translateX + startX - node.length*30.0 >= -329.0 &&
                             node.translateX + startX <= -29.0 && node.translateY >= 17.0 && node.translateY + 30.0 <= 317.0){
                        var cell = translateToCellHorizontal(node.translateX + startX, node.translateY)
                        game.removeShip(Player.Human, cell)
                        for (p in 1 .. node.length){
                            occupiedCell.removeIf{
                                it.x == cell.x && it.y == cell.y
                            }
                            cell = Cell(cell.x-1, cell.y)
                        }
                    }

                    offsetX = node.translateX - e.sceneX
                    offsetY = node.translateY - e.sceneY
                    // we don't want to drag the background too
                    e.consume()
                }
            }
        }
    }
}