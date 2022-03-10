package cs349.a3battleship.ui

import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.scene.text.Font
import javafx.scene.text.FontWeight
import javafx.scene.text.Text

class RightPartView(borderView: OppBoardView): IView, VBox() {
    override fun updateView() {
        TODO("Not yet implemented")
    }
    init {
        minWidth = 350.0

        val title = Text("Opponent's Formation")
        title.font = Font.font("verdana", FontWeight.BOLD, 16.0)
        val topContainer = HBox(title)
        topContainer.minHeight = 25.0
        topContainer.alignment = Pos.TOP_CENTER
        topContainer.padding = Insets(0.0,10.0,10.0,10.0)
        children.add(topContainer)

        val boardContainer = BorderPane()

        boardContainer.left = RowLabelView()
        boardContainer.center = borderView
        boardContainer.right = RowLabelView()
        boardContainer.top = ColLabelView()
        boardContainer.bottom = ColLabelView()
        children.add(boardContainer)
    }
}