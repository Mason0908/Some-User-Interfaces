import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.Label
import javafx.scene.control.ScrollPane
import javafx.scene.control.Spinner
import javafx.scene.layout.*
import javafx.scene.paint.Color

class DataTable(
    private val model: Model,
    private val controller: Main
): VBox(), IView {
    override fun updateView() {
        children.clear()
        var counter = 1
        model.currDataSet?.data?.forEach {
            val label = Label("$counter:")
            val spinner = Spinner<Number>(0,100,it)
            spinner.prefWidth = 80.0
            spinner.id = (counter - 1).toString()
            spinner.valueProperty().addListener{event ->
                model.changeDataSetValue(spinner.id.toInt(), spinner.value.toInt())
            }
            val wrapper = HBox(label,spinner)
            wrapper.spacing = 10.0
            wrapper.padding = Insets(0.0,10.0,0.0,10.0)
            wrapper.alignment = Pos.CENTER_RIGHT
            children.add(wrapper)
            counter++
        }
    }
    init {
        minWidth = 150.0
        maxWidth = 150.0
        padding = Insets(10.0,15.0,10.0,0.0)
        model.addView(this)
    }

}