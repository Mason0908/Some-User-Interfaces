package cs349.a3battleship.ui

import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.layout.HBox
import javafx.scene.text.Font
import javafx.scene.text.Text

class ColLabelView : HBox() {
    init{
        minWidth = 300.0
        height = 25.0
        padding = Insets(0.0,50.0,0.0,42.0)
        spacing = 22.0
        alignment = Pos.CENTER_LEFT
        var c = 1

        while (c <= 10){
            val currText = Text(c.toString())
            currText.font = Font.font("verdana", 12.0)
            children.add(currText)
            c++
        }
    }
}