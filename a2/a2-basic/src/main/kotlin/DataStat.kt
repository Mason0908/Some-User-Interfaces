import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.Label
import javafx.scene.control.Spinner
import javafx.scene.layout.*
import javafx.scene.paint.Color
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.Collections.max
import java.util.Collections.min

class DataStat(
    private val model: Model,
    private val controller: Main
): HBox(), IView {
    var currDataSet = model.currDataSet
    private val number = Label()
    private val min = Label()
    private val max = Label()
    private val ave = Label()
    private val sum = Label()
    override fun updateView() {
        currDataSet = model.currDataSet
        number.text = currDataSet?.data?.size.toString()
        min.text = min(currDataSet?.data).toString()
        max.text = max(currDataSet?.data).toString()
        val df = DecimalFormat(".#")
        df.roundingMode = RoundingMode.CEILING
        ave.text = df.format(currDataSet?.data?.average()).toString()
        sum.text = currDataSet?.data?.sum().toString()
    }
    init {
        minWidth = 125.0
        val labels = VBox()
        val numberLabel = Label("Number")
        val minLabel = Label("Minimum")
        val maxLabel = Label("Maximum")
        val aveLabel = Label("Average")
        val sumLabel = Label("Sum")
        labels.children.addAll(numberLabel, minLabel, maxLabel, aveLabel, sumLabel)
        labels.padding = Insets(10.0)
        labels.spacing = 10.0
        val values = VBox()
        values.children.addAll(number, min, max, ave, sum)
        values.padding = Insets(10.0)
        values.spacing = 10.0

        children.addAll(labels, values)
        model.addView(this)

    }

}