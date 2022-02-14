import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.layout.*
import javafx.scene.paint.Color
import javafx.scene.text.*
import javafx.stage.Stage

class Main : Application() {
    private val model = Model()
    private val root = StackPane()
    val scene = Scene(root, 800.0, 600.0)


    override fun start(stage: Stage) {
        val main = BorderPane()
        val topContent = VBox(ToolbarView(model, this), EditView(model, this))
        main.top = topContent
        main.center = GraphView(model, this)
        val leftContent = ScrollPane(DataTable(model, this))
        leftContent.minWidth = 170.0
        leftContent.maxWidth = 170.0

        leftContent.isFitToWidth = true
        main.left = leftContent
        main.right = DataStat(model, this)
        main.bottom = StatusView(model)
        root.children.add(main)

        stage.title = "A2 Grapher (20775201)"
        stage.scene = scene
        stage.minWidth = 600.0
        stage.minHeight = 400.0

        stage.show()
    }
}