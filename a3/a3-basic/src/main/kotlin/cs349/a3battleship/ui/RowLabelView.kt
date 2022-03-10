package cs349.a3battleship.ui

import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.layout.VBox
import javafx.scene.text.Font
import javafx.scene.text.Text

class RowLabelView() : VBox() {
    init{
        minWidth = 25.0
        height = 300.0
        padding = Insets(10.0)
        spacing = 15.0
        alignment = Pos.CENTER
        var c = 'A'

        while (c <= 'J'){
            val currText = Text(c.toString())
            currText.font = Font.font("verdana", 12.0)
            children.add(currText)
            c++
        }
    }
}