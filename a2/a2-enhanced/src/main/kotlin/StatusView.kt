import javafx.geometry.*
import javafx.scene.control.*
import javafx.scene.layout.*
import javafx.scene.paint.Color

class StatusView(
    private val model: Model
): HBox(), IView {
    private val count = Label()

    // status bar is all about composing the right count and message
    override fun updateView() {
        count.text = "${model.dataSets.size} datasets"
    }

    val message = Label("")

    init {
        // layout
        alignment = Pos.CENTER_LEFT
        padding = Insets(10.0)
        spacing = 32.0
        background = simpleFill(Color.LIGHTGREY)

        children.addAll(count, message)

        // register with the model when we're ready to start receiving data
        model.addView(this)
    }
}