package cs349.a3battleship.ui

import cs349.a3battleship.model.Orientation
import cs349.a3battleship.model.ships.ShipType
import javafx.scene.image.Image
import javafx.scene.paint.Color
import javafx.scene.paint.ImagePattern
import javafx.scene.shape.Rectangle

class ShipView(shipType: ShipType) : Rectangle(){
    val shipType = shipType
    var length: Int = 0
    var orientation = Orientation.VERTICAL

    init {
        width = 30.0
        when(shipType){
            ShipType.Battleship -> {
                val image = Image("battleship.png")
                fill = ImagePattern(image)
                stroke = Color.TRANSPARENT
                strokeWidth = 2.0
                length = 4
            }
            ShipType.Carrier -> {
                val image = Image("carrier.png")
                fill = ImagePattern(image)
                stroke = Color.TRANSPARENT
                strokeWidth = 2.0
                length = 5
            }
            ShipType.Cruiser -> {
                val image = Image("cruiser.png")
                fill = ImagePattern(image)
                stroke = Color.TRANSPARENT
                strokeWidth = 2.0
                length = 3
            }
            ShipType.Destroyer -> {
                val image = Image("destroyer.png")
                fill = ImagePattern(image)
                stroke = Color.TRANSPARENT
                strokeWidth = 2.0
                length = 2
            }
            ShipType.Submarine -> {
                val image = Image("submarine.png")
                fill = ImagePattern(image)
                stroke = Color.TRANSPARENT
                strokeWidth = 2.0
                length = 3
            }
        }
        height = 30.0*length

//        setOnMouseClicked {
//            println("123")
//            if (it.button == MouseButton.SECONDARY){
//                println(1)
//                transforms.add(Transform.rotate(-90.0,0.0,0.0))
//            }
//        }

    }
}