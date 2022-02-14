import javafx.scene.layout.*
import javafx.scene.paint.Color

/*
 * global debug messages
 */
fun debug(msg: String, show: Boolean = true) {
    if (show) println("DBG: $msg")
}

/*
 * useful for quickly assigning a solid fill colour
 */
fun simpleFill(color: Color): Background {
    return Background(BackgroundFill(color, null, null))
}

/*
 * useful for quickly assigning a stroke color and width
 */
fun simpleStroke(color: Color, width: Double = 1.0): Border {
    return Border(BorderStroke(color, BorderStrokeStyle.SOLID, null, BorderWidths(width)))
}