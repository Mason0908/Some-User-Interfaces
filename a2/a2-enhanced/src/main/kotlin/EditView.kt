import javafx.geometry.*
import javafx.scene.control.*
import javafx.scene.layout.*


class EditView(
    private val model: Model,
    private val controller: Main
) : HBox(), IView {
    private val titleText = TextField()
    private val xAxisText = TextField()
    private val yAxisText = TextField()

    override fun updateView() {
        if (titleText.text != model.title){
            titleText.text = model.title
        }
        if (xAxisText.text != model.xAxis){
            xAxisText.text = model.xAxis
        }
        if (yAxisText.text != model.yAxis){
            yAxisText.text = model.yAxis
        }
    }

    init {
        padding = Insets(10.0)
        spacing = 10.0
        alignment = Pos.CENTER_LEFT

        val titleLabel = Label("Title:")
        children.add(titleLabel)

        titleText.prefWidth = 150.0
        children.add(titleText)

        val xAxisLabel = Label("X-Axis:")
        children.add(xAxisLabel)
        xAxisText.prefWidth = 150.0
        children.add(xAxisText)

        val yAxisLabel = Label("Y-Axis:")
        children.add(yAxisLabel)
        yAxisText.prefWidth = 150.0
        children.add(yAxisText)

        model.addView(this)

        // Add listeners
        titleText.setOnKeyTyped {
            model.title = titleText.text
        }

        xAxisText.setOnKeyTyped {
            model.xAxis = xAxisText.text
        }

        yAxisText.setOnKeyTyped {
            model.yAxis = yAxisText.text
        }
    }
}