package cs349.a3battleship.ui

import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Group
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.scene.shape.Rectangle
import javafx.scene.text.Font
import javafx.scene.text.FontWeight
import javafx.scene.text.Text

class LeftPartView(borderView: MyBoardView): IView, VBox() {
    override fun updateView() {
    }
    init {
        minWidth = 350.0
        alignment = Pos.TOP_CENTER

        val title = Text("My Formation")
        title.font = Font.font("verdana", FontWeight.BOLD, 16.0)
        val topContainer = HBox(title)
        topContainer.minHeight = 25.0
        topContainer.alignment = Pos.TOP_CENTER
        topContainer.padding = Insets(0.0,10.0,10.0,10.0)
        children.add(topContainer)

        val boardContainer = BorderPane()
        boardContainer.padding = Insets(0.0)

        val rightGroup = Group()
        val rightContainerRect = Rectangle(25.0, 300.0)
        rightGroup.children.add(rightContainerRect)

        boardContainer.left = RowLabelView()
        boardContainer.center = borderView
        boardContainer.right = RowLabelView()
        boardContainer.top = ColLabelView()
        boardContainer.bottom = ColLabelView()
        children.add(boardContainer)
    }
}