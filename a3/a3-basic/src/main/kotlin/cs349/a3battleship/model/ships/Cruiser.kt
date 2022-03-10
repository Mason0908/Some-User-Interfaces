package cs349.a3battleship.model.ships

import cs349.a3battleship.model.Cell
import cs349.a3battleship.model.Orientation

class Cruiser(override var orientation: Orientation,
              override var location : Cell
) : Ship() {
    override val length = 3
    override val shipType = ShipType.Cruiser
    override var shipCells = mutableListOf(Cell(0,0))

    init {
        shipCells = MutableList(length) { idx ->
            var delta = when (orientation) {
                Orientation.VERTICAL -> Cell(0, 1)
                Orientation.HORIZONTAL -> Cell(1, 0)
            }
            when (orientation) {
                Orientation.VERTICAL -> location + delta * idx
                Orientation.HORIZONTAL -> location - delta * idx
            }
        }
    }
}