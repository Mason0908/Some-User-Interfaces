package cs349.a3battleship

import cs349.a3battleship.model.Game
import cs349.a3battleship.ui.*
import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.layout.*
import javafx.stage.Stage

class Battleship : Application() {
    val root = StackPane()
    private val scene = Scene(root, 900.0, 375.0)
    private val container = HBox()

    override fun start(stage: Stage) {

        var game = Game(10, true)
        var computer = AI(game)

        // var player = ...
        game.startGame()


        root.children.add(container)

        val myBoardView = MyBoardView(game, this)
        val left = LeftPartView(myBoardView)
        container.children.add(left)

        val mid = CenterPartView(this, game)
        container.children.add(mid)

        val oppBoardView = OppBoardView(game, this)
        val right = RightPartView(oppBoardView)
        container.children.add(right)


        stage.title = "A3 Battleship (20775201)"
        stage.scene = scene
        stage.minWidth = 875.0
        stage.minHeight = 375.0


        stage.show()
    }
}