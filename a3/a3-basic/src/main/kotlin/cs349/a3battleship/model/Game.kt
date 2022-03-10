package cs349.a3battleship.model

import cs349.a3battleship.model.ships.Ship
import cs349.a3battleship.model.ships.ShipType
import cs349.a3battleship.ui.IView

/**
 * Game manages the rules of the game Battleship.
 * @param dimension the number of rows and columns on the board.
 * @param debug if true, debug massages are printed to the command line
 */
class Game (var dimension : Int, private var debug : Boolean) {

    enum class GameState {
        Init,
        SetupHuman,
        SetupAI,
        FireHuman,
        FireAI,
        WonHuman,
        WonAI,
    }

    private val boards = mapOf(
        Player.Human to Board(dimension),
        Player.AI to Board(dimension))

    private var activePlayer = Player.Human
    var gameState = GameState.Init

    /**
     * Invoked if Player can begin setting up
     */
    val onPlayerSetupBegin = ArrayList<(Player) -> Unit>()

    /**
     * Invoked if Player can begin attacking
     */
    val onPlayerAttackBegin = ArrayList<(Player) -> Unit>()

    /**
     * Invoked if an attack has been completed
     */
    val onPlayerAttackComplete = ArrayList<(Unit) -> Unit>()

    /**
     * Invoked if a player has won the game
     */
    val onPlayerWon = ArrayList<(Player) -> Unit>()

    private val views: ArrayList<IView> = ArrayList()

    fun addView(view: IView) {
        views.add(view)
        view.updateView()
    }

    fun notifyObservers(){
        for (i in views){
            i.updateView()
        }
    }

    /**
     * Attacks the cell.
     */
    fun attackCell(cell : Cell)  {
        // perform attack
        var receivingPlayer = when (activePlayer) {
            Player.Human -> Player.AI
            Player.AI -> Player.Human
        }
        boards[receivingPlayer]!!.attackCell(cell)
        onPlayerAttackComplete.forEach { it(Unit) }

        // check win-condition
        if (boards[Player.Human]!!.hasShips().not()) {
            gameState = GameState.WonAI
            onPlayerWon.forEach { it(Player.AI) }
        } else if (boards[Player.AI]!!.hasShips().not()) {
            gameState = GameState.WonHuman
            onPlayerWon.forEach { it(Player.Human) }
        } else {
            if (gameState == GameState.FireHuman) {
                activePlayer = Player.AI
                gameState = GameState.FireAI
            } else if (gameState == GameState.FireAI) {
                activePlayer = Player.Human
                gameState = GameState.FireHuman
            }
            onPlayerAttackBegin.forEach { it(activePlayer) }
        }
        notifyObservers()
    }

    /**
     * Places a ship of shipType for player.
     * @param player the player for which to place the ship for
     * @param shipType the type of the ship
     * @param orientation the orientation of the ship
     * @param bowCell the cell in which the bow of the ship is located
     * @return the newly created ship, or null if the ship was not placed
     */
    fun placeShip(player : Player, shipType : ShipType, orientation : Orientation, bowCell : Cell) : Ship? {
        if (gameState != GameState.SetupAI && gameState != GameState.SetupHuman) {
            return null
        }
        if ((getShipsToPlace() - boards[player]!!.placedShips.map { ship -> ship.shipType}).contains(shipType).not()) {
            return null
        }
        var newShip = boards[player]!!.placeShip(shipType, orientation, bowCell)
        if (debug) {
            debugPrintBoard(player)
        }
        if (getShipsPlacedCount(Player.Human) == 5){
            notifyObservers()
        }
        return newShip
    }

    /**
     * Removes the ship at cell from the board of player.
     */
    fun removeShip(player : Player, cell: Cell) {
        val count = boards[player]!!.placedShips.count()
        if (gameState == GameState.SetupAI || gameState == GameState.SetupHuman) {
            boards[player]!!.placedShips.find { it.getCells().contains(cell) }?.let { boards[player]!!.removeShip(it) }
            println(boards[player]!!.placedShips.find { it.getCells().contains(cell) })
        }
        if (count == 5){
            notifyObservers()
        }
    }

    /**
     * Returns a list of all cells that have been attack so far by player.
     *
     * @return A list of cells
     */
    fun getAttackedCells(player : Player): List<Cell> {
        return boards[player]!!.attackedCells.toList()
    }

    /**
     * Returns a list of all the ship that have to be placed.
     */
    fun getShipsToPlace() : List<ShipType> {
        return ShipType.values().toList()
    }

    /**
     * Returns the number of ships that player has currently placed.
     */
    fun getShipsPlacedCount(player : Player) : Int {
        return boards[player]!!.placedShips.count()
    }

    /**
     * Signals that the player currently setting up has finished their setup
     */
    fun startGame() {
        if (gameState == GameState.Init) {
            gameState = GameState.SetupHuman
            onPlayerSetupBegin.forEach { it(Player.Human) }

        } else if (gameState == GameState.SetupHuman) {
            gameState = GameState.SetupAI
            onPlayerSetupBegin.forEach { it(Player.AI) }
        } else if (gameState == GameState.SetupAI) {

            if (debug) {
                debugPrintBoard(Player.Human)
                debugPrintBoard(Player.AI)
            }

            gameState = GameState.FireHuman
            onPlayerAttackBegin.forEach { it(Player.Human) }
        }
    }

    /**
     * Returns the current state of the board for player. Possible values per cell are
     *   CellState.Ocean
     *   CellState.Attacked
     *   CellState.Hit
     *   CellState.Sunk
     *   The first (outer) dimension of the board represents the y-coordinate,
     *   the second (inner) one the x-coordinate.
     */
    fun getBoard(player : Player) : Array<Array<CellState>> {
        var cells = Array(boards[player]!!.dimension) {
            Array(boards[player]!!.dimension) {
                CellState.Ocean
            }
        }
        getAttackedCells(player).forEach {
            cells[it.y][it.x] = CellState.Attacked
        }
        boards[player]!!.placedShips.forEach { ship ->
            var cellState = when (ship.isSunk()) {
                true -> CellState.ShipSunk
                false -> CellState.ShipHit
            }
            ship.getCells().forEach { cell ->
                if (cell.attacked) {
                    cells[cell.y][cell.x] = cellState
                }
            }
        }
        return cells
    }

    private fun debugPrintBoard(player : Player) {
        println("DEBUG: $player BOARD")
        (0 until dimension).forEach { y ->
            (0 until dimension).forEach { x ->
                with (boards[player]!!.placedShips.find { ship -> ship.getCells().contains(Cell(x, y)) }) {
                    if (this != null) print(" " + this.shipType.name[0] + " ")
                    else print(" _ ")
                }
            }
            println("")
        }
    }
    fun getUnattackedShips(player: Player): List<Ship>{
        val ships = mutableListOf<Ship>()
        boards[player]!!.placedShips.forEach { ship ->
            if (!ship.isSunk()){
                ships.add(ship)
            }
        }
        return ships
    }
}
