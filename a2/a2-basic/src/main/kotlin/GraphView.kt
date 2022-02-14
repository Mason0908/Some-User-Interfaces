import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Group
import javafx.scene.canvas.Canvas
import javafx.scene.control.Label
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.shape.Line
import javafx.scene.shape.Rectangle
import javafx.scene.text.Text
import javafx.scene.text.TextAlignment
import java.util.Collections.max

class GraphView(
    private val model: Model,
    private val controller: Main
): BorderPane(), IView {
    private val title = Text()
    private val xLabel = Text()
    private val yLabel = Text()
    private val initialValue = Text(0.0,0.0,"0")
    private val maxValue = Text(0.0,0.0,max(model.currDataSet?.data).toString())
    private val graphHolder = Group()
    private val graphRectangle = Rectangle(width - 100.0,height - 100.0)

    private fun draw(){
        val numBars = model.currDataSet?.data?.size ?: 10
        graphHolder.children.removeIf { graphHolder.children.indexOf(it) >= 5 }
        var counter = 0
        model.currDataSet?.data?.forEach {
            val currBar = Rectangle()
            val lenEachBar = (graphRectangle.width-5.0) / numBars
            val maxBar = max(model.currDataSet?.data)
            val currHeightPerc = it.toDouble() / maxBar
            val currHeight = currHeightPerc * graphRectangle.height
            currBar.x = graphRectangle.x + 5.0 + lenEachBar * counter
            currBar.y = graphRectangle.y + graphRectangle.height - currHeight
            currBar.width = lenEachBar - 5.0
            currBar.height = currHeight
            currBar.fill = Color.hsb(360.0*((counter).toDouble()/numBars.toDouble()),1.0,1.0)
            currBar.stroke = Color.BLACK
            graphHolder.children.add(currBar)
            counter ++
        }
    }

    override fun updateView() {
        title.text = model.title
        xLabel.text = model.xAxis
        yLabel.text = model.yAxis
        maxValue.text = max(model.currDataSet?.data).toString()
        // draw actual graphs
        draw()
        graphRectangle.widthProperty().addListener { event ->
            draw()
        }
        graphRectangle.heightProperty().addListener { event ->
            draw()
        }

    }
    init {
        background = simpleFill(Color.WHITE)
        // Add labels
        val topContent = HBox(title)
        topContent.alignment = Pos.CENTER
        topContent.padding = Insets(10.0)
        val leftContent = HBox(yLabel)
        yLabel.rotate = -90.0


        leftContent.alignment = Pos.CENTER
        leftContent.maxWidth = 40.0
        leftContent.minWidth = 40.0

        val rightContent = VBox()


        val bottomContent = HBox(xLabel)
        bottomContent.alignment = Pos.CENTER
        bottomContent.padding = Insets(10.0)


        // Draw Graph
        graphRectangle.fill = Color.TRANSPARENT
        graphRectangle.stroke = Color.LIGHTGREY

        widthProperty().addListener { event ->
            graphRectangle.width = width - 100.0
        }
        heightProperty().addListener { event ->
            graphRectangle.height = height - 100.0
        }
        val xAxis = Line(0.0,0.0,0.0,0.0)
        xAxis.strokeWidth = 2.0
        xAxis.fill = Color.BLACK
        val yAxis = Line(0.0,0.0,0.0,0.0)
        yAxis.strokeWidth = 2.0
        yAxis.fill = Color.BLACK
        initialValue.textAlignment = TextAlignment.RIGHT
        maxValue.textAlignment = TextAlignment.RIGHT
        graphRectangle.widthProperty().addListener { event ->
            initialValue.x = graphRectangle.x - 10.0
            initialValue.y = graphRectangle.y + graphRectangle.height
            maxValue.x = graphRectangle.x - 25.0
            maxValue.y = graphRectangle.y + 10.0
            xAxis.startX = graphRectangle.x
            xAxis.startY = graphRectangle.y + graphRectangle.height
            xAxis.endX = graphRectangle.x + graphRectangle.width
            xAxis.endY = graphRectangle.y + graphRectangle.height
            yAxis.startX = graphRectangle.x
            yAxis.startY = graphRectangle.y + graphRectangle.height
            yAxis.endX = graphRectangle.x
            yAxis.endY = graphRectangle.y
            xLabel.x = graphRectangle.width
        }
        graphRectangle.heightProperty().addListener { event ->
            initialValue.x = graphRectangle.x - 10.0
            initialValue.y = graphRectangle.y + graphRectangle.height
            maxValue.x = graphRectangle.x - 25.0
            maxValue.y = graphRectangle.y + 10.0
            xAxis.startX = graphRectangle.x
            xAxis.startY = graphRectangle.y + graphRectangle.height
            xAxis.endX = graphRectangle.x + graphRectangle.width
            xAxis.endY = graphRectangle.y + graphRectangle.height
            yAxis.startX = graphRectangle.x
            yAxis.startY = graphRectangle.y + graphRectangle.height
            yAxis.endX = graphRectangle.x
            yAxis.endY = graphRectangle.y
            xLabel.x = graphRectangle.width

        }



        graphHolder.children.addAll(graphRectangle, xAxis, yAxis, maxValue, initialValue)

        top = topContent
        left = leftContent
        bottom = bottomContent
        center = graphHolder
        right = rightContent
        model.addView(this)
    }
}