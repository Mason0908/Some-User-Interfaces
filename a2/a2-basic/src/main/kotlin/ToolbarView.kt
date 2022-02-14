import javafx.geometry.*
import javafx.scene.control.*


class ToolbarView(
    private val model: Model,
    private val controller: Main
) : ToolBar(), IView {
    private val dataSelectBox = ChoiceBox<String>()

    override fun updateView() {
        if (dataSelectBox.items.size != model.dataSets.size){
            dataSelectBox.items.add(model.dataSets.keys.last())
            dataSelectBox.selectionModel.selectLast()
        }

    }

    init {
        padding = Insets(10.0)

        val bLabel = Label("Dataset:")
        items.add(bLabel)
        dataSelectBox.minWidth = 100.0

        dataSelectBox.items.addAll(model.dataSets.keys)
        dataSelectBox.selectionModel.selectFirst()

        dataSelectBox.setOnAction {
            val currSelected = dataSelectBox.selectionModel.selectedItem
            model.changeDataSet(currSelected)
        }

        items.add(dataSelectBox)
        val verticalSeparator = Separator(Orientation.VERTICAL)
        verticalSeparator.padding = Insets(0.0,10.0,0.0,10.0)

        items.add(verticalSeparator)

        val newButton = Button("New")
        newButton.prefWidth = 80.0

        items.add(newButton)

        val spinner = Spinner<Number>(1,20,1)
        spinner.prefWidth = 60.0
        items.add(spinner)

        newButton.setOnAction {
            model.addNewDataSet(spinner.value.toInt())
        }


        model.addView(this)
    }
}