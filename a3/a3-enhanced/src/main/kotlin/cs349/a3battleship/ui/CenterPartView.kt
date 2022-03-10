package cs349.a3battleship.ui

import cs349.a3battleship.Battleship
import cs349.a3battleship.model.Game
import cs349.a3battleship.model.Orientation
import cs349.a3battleship.model.Player
import cs349.a3battleship.model.ships.ShipType
import cs349.a3battleship.ulti.MovableManager
import javafx.application.Platform
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.scene.text.Font
import javafx.scene.text.FontWeight
import javafx.scene.text.Text
import javafx.scene.transform.Transform

class CenterPartView(controller: Battleship, private val game: Game) : IView, VBox() {
    private val startButton = Button("Start Game")
    private val title = Text("My Fleet")
    private val destroyer = ShipView(ShipType.Destroyer)
    private val cruiser = ShipView(ShipType.Cruiser)
    private val submarine = ShipView(ShipType.Submarine)
    private val battleship = ShipView(ShipType.Battleship)
    private val carrier =  ShipView(ShipType.Carrier)
    private val shipsContainer = HBox()


    override fun updateView() {
        if (game.gameState == Game.GameState.SetupHuman){
            if (game.getShipsPlacedCount(Player.Human) != 5){
                startButton.isDisable = true
                shipsContainer.viewOrder = -1.0
            }
            else{
                startButton.isDisable = false
                shipsContainer.viewOrder = 0.0

            }
        }
        if (game.gameState == Game.GameState.WonHuman){
            title.text = "You Won!"
            val unAttackedShips = game.getUnattackedShips(Player.Human)
            for (i in unAttackedShips){
                if (i.shipType == ShipType.Destroyer){
                    if (destroyer.orientation == Orientation.HORIZONTAL){
                        destroyer.transforms!!.add(Transform.rotate(-90.0,0.0,0.0))
                        destroyer.orientation = Orientation.VERTICAL
                    }
                    destroyer.translateX = 0.0
                    destroyer.translateY = 0.0

                }
                if (i.shipType == ShipType.Carrier){
                    if (carrier.orientation == Orientation.HORIZONTAL){
                        carrier.transforms!!.add(Transform.rotate(-90.0,0.0,0.0))
                        carrier.orientation = Orientation.VERTICAL
                    }
                    carrier.translateX = 0.0
                    carrier.translateY = 0.0
                }
                if (i.shipType == ShipType.Cruiser){
                    if (cruiser.orientation == Orientation.HORIZONTAL){
                        cruiser.transforms!!.add(Transform.rotate(-90.0,0.0,0.0))
                        cruiser.orientation = Orientation.VERTICAL
                    }
                    cruiser.translateX = 0.0
                    cruiser.translateY = 0.0
                }
                if (i.shipType == ShipType.Battleship){
                    if (battleship.orientation == Orientation.HORIZONTAL){
                        battleship.transforms!!.add(Transform.rotate(-90.0,0.0,0.0))
                        battleship.orientation = Orientation.VERTICAL
                    }
                    battleship.translateX = 0.0
                    battleship.translateY = 0.0
                }
                if (i.shipType == ShipType.Submarine){
                    if (submarine.orientation == Orientation.HORIZONTAL){
                        submarine.transforms!!.add(Transform.rotate(-90.0,0.0,0.0))
                        submarine.orientation = Orientation.VERTICAL
                    }
                    submarine.translateX = 0.0
                    submarine.translateY = 0.0
                }
            }
        }
        else if (game.gameState == Game.GameState.WonAI){
            title.text = "You were defeated!"
        }
    }

    init {

        minWidth = 175.0
        alignment = Pos.TOP_CENTER
        title.font = Font.font("verdana", FontWeight.BOLD, 16.0)
        val topContainer = HBox(title)
        topContainer.minHeight = 25.0
        topContainer.alignment = Pos.TOP_CENTER
        topContainer.padding = Insets(0.0,10.0,10.0,10.0)
        children.add(topContainer)

        shipsContainer.spacing = 5.0
        shipsContainer.minHeight = 260.0
        shipsContainer.viewOrder = -1.0

        // Set up fleets
        val moveManager = MovableManager(controller.root, game)

        moveManager.makeMovable(destroyer, game)
        moveManager.makeMovable(cruiser, game)
        moveManager.makeMovable(submarine, game)
        moveManager.makeMovable(battleship, game)
        moveManager.makeMovable(carrier, game)
        shipsContainer.children.addAll(destroyer,cruiser,submarine,battleship,carrier)
        children.add(shipsContainer)
        startButton.minWidth = 175.0
        startButton.isDisable = true
        startButton.setOnAction {
            if (game.gameState == Game.GameState.SetupHuman){
                startButton.isDisable = true
                game.startGame()
            }
        }
        val exitButton = Button("Exit Game")
        exitButton.minWidth = 175.0
        exitButton.setOnAction {
            Platform.exit()
        }
        children.addAll(startButton, exitButton)
        viewOrder = -1.0
        game.addView(this)
    }
}