import javafx.application.Application
import javafx.collections.ListChangeListener
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.input.MouseButton
import javafx.scene.layout.*
import javafx.scene.paint.Color
import javafx.stage.Modality
import javafx.stage.Stage
import javafx.stage.StageStyle
import kotlin.random.Random

class HelloFX : Application() {
    private var stage: Stage = Stage()
    private val centralSection = TilePane()
    private var selectedNote = StackPane()
    private var selected = false
    private val deleteButton = Button("Delete")
    private val clearButton = Button("Clear")
    private val currStatus1 = Label("0")
    private val currStatus2 = Label("")
    private val searchBox = TextField()
    private val darkCover = Region()
    private var activeNotesId: MutableList<String> = mutableListOf()
    private val importantNotes = mutableListOf<String>()
    private var showImportantOnly = false
    private var noteIdCounter = 0
    private var totalNotes = 0
    private var visibleNotes = 0

    private val words = listOf("lorem", "ipsum", "dolor", "sit", "amet", "consectetur", "adipiscing", "elit",
        "sed", "molestie", "nisi", "vitae", "semper", "mattis", "in", "convallis", "sem", "nec", "placerat",
        "ac", "rhoncus", "sapien", "blandit", "fusce", "tincidunt", "vehicula", "massa", "vulputate", "gravida",
        "pellentesque", "ante", "dui", "lacinia", "condimentum", "mauris", "faucibus", "neque", "mauris",
        "in", "erat", "venenatis", "donec", "eget", "est", "hendrerit", "varius", "at", "non", "lectus",
        "orci", "natoque", "penatibus", "et", "magnis", "dis", "parturient", "montes", "nascetur", "ridiculus",
        "mus", "praesent", "urna", "mi", "scelerisque", "diam", "iaculis", "velit", "suspendisse", "dapibus",
        "ligula", "fringilla", "nunc", "sed", "euismod", "pulvinar", "quis", "aliquet", "nunc", "potenti",
        "aenean", "ornare", "quam", "elementum", "vestibulum", "orci", "nibh", "facilisis", "sagittis", "a",
        "curabitur", "maximus", "accumsan", "aliquam", "bibendum", "sodales", "justo", "interdum", "viverra",
        "nisl", "volutpat", "duis", "risus", "lobortis", "eleifend", "ut", "turpis", "proin", "libero", "finibus", "tellus")


    /**
     * Function for generating random text for random notes
     *
     * @author Mason ma
     * @return List<String>
     */
    private fun generateRandomNote(): List<String> {
        var title = ""
        var body = ""
        var randomWord = ""
        var randomIndex = 0
        var sentenceWordNum = 0
        var sentence = ""
        val titleWorkNum = Random.nextInt(1,4)
        val bodySentenceNum = Random.nextInt(2,6)

        // Generate Title
        for (i in 1..titleWorkNum){
            randomIndex = Random.nextInt(words.size)
            randomWord = words[randomIndex].replaceFirstChar { it.uppercase() }
            if (i != titleWorkNum){
                randomWord += " "
            }
            title += randomWord
        }

        // Generate Body
        for (x in 1..bodySentenceNum){
            sentenceWordNum = Random.nextInt(3,11)
            for (l in 1..sentenceWordNum){
                randomIndex = Random.nextInt(words.size)

                randomWord = words[randomIndex]
                if (l == 1){
                    randomWord = randomWord.replaceFirstChar { it.uppercase() }
                }
                if (l != sentenceWordNum){
                    randomWord += " "
                }
                if (l == sentenceWordNum){
                    randomWord += "."
                }
                sentence += randomWord
            }
            if (x != bodySentenceNum){
                sentence += " "
            }
            body += sentence
            sentence = ""
        }

        // Generate Importance
        val importance = Random.nextInt(1,6) == 1
        if (importance){
            return listOf(title, body, "important")
        }
        else{
            return listOf(title, body, "unimportant")
        }
    }

    /**
     * Function used to create a new note
     *
     * @author Mason Ma
     * @return void
     * @params
     * info: Information for creating the notes. First value is title, Second value
     *       is body, and the third value is importance
     * isEditing: Indicate if currently is editing the note or not
     * noteId: If currently is editing, indicate which note id is being edited
     */
    private fun createNote(info: List<String>, isEditing: Boolean = false, noteId: String = ""){
        val currNote = StackPane()
        val title = Label(info[0])
        val body = Label(info[1])
        body.isWrapText = true
        val noteContent = VBox(title, body)
        noteContent.spacing = 10.0
        currNote.children.add(noteContent)
        currNote.alignment = Pos.TOP_LEFT
        currNote.padding = Insets(10.0)

        if (info[2] == "important"){
            currNote.background = Background(BackgroundFill(Color.LIGHTYELLOW, CornerRadii.EMPTY, Insets.EMPTY))
        }
        else{
            currNote.background = Background(BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY))
        }
        currNote.setMinSize(150.0,200.0)
        currNote.setMaxSize(150.0,200.0)
        // If not in editing mode, directly add a new node to centralSection
        if (!isEditing){
            noteIdCounter++
            currNote.id = noteIdCounter.toString()

            activeNotesId.add(currNote.id)
            if (info[2] == "important"){
                if (currNote.id !in importantNotes){
                    importantNotes.add(currNote.id);
                }
            }
            else{
                if (showImportantOnly){
                    currNote.isManaged = false
                    currNote.isVisible = false
                }
            }
            if (searchBox.text != ""){
                if ((searchBox.text !in title.text) && (searchBox.text !in body.text)){
                    currNote.isVisible = false
                    currNote.isManaged = false
                }
            }
            currStatus2.text = "Added Note #${currNote.id}"
            centralSection.children.add(currNote)
        }
        // If is in editing mode, replace the previous node with the new node
        else{
            for (i in 0 until centralSection.children.size){
                if (noteId == centralSection.children[i].id){
                    currNote.id = noteId
                    if (searchBox.text != ""){
                        if ((searchBox.text !in title.text) && (searchBox.text !in body.text)){
                            currNote.isVisible = false
                            currNote.isManaged = false
                        }
                    }
                    currStatus2.text = "Edited Note #${currNote.id}"
                    centralSection.children[i] = currNote

                }
            }
            if (info[2] == "important"){
                if (currNote.id !in importantNotes){
                    importantNotes.add(currNote.id);
                }
            }
            else{
                if (showImportantOnly){
                    for (i in centralSection.children){
                        if (i.id == noteId){
                            i.isVisible = false
                            i.isManaged = false
                        }
                    }
                }
                if (currNote.id in importantNotes){
                    importantNotes.remove(currNote.id)
                }
            }
        }
        // Add a mouse click event listener to each note created
        currNote.setOnMouseClicked {
            if (it.button.equals(MouseButton.PRIMARY)){
                // Handle select event
                if (it.clickCount == 1){
                    if (selectedNote != currNote){
                        currNote.border = Border(BorderStroke(Color.BLUEVIOLET, BorderStrokeStyle.SOLID, null,null))
                        val preSelectedNote = selectedNote
                        preSelectedNote.border = null
                        selectedNote = currNote
                        selected = true
                        deleteButton.isDisable = false
                        currStatus2.text = "Note #${selectedNote.id} is selected"
                    }
                    else{
                        currNote.border = null

                        selected = false
                        selectedNote = StackPane()
                        deleteButton.isDisable = true
                        currStatus2.text = ""
                    }
                }
                // Handle edit event
                if (it.clickCount == 2){
                    showAddNoteWindow(isEditing = true, noteId = currNote.id, title = info[0], body = info[1], importance = info[2]=="important")
                }
            }
        }
        // Add event listener to each node's isManaged property to handle visibility of notes
        currNote.managedProperty().addListener { observable, oldValue, newValue ->
            if (oldValue){
                visibleNotes--
            }
            else{
                visibleNotes++
            }
            if (visibleNotes == totalNotes){
                currStatus1.text = totalNotes.toString()
            }
            else{
                currStatus1.text = "$visibleNotes (of $totalNotes)"
            }
            clearButton.isDisable = visibleNotes==0
        }
    }

    /**
     * Function to show the add note window
     *
     * @author Mason Ma
     * @return void
     * @params
     * isEditing: Indicate if currently is editing the note or not
     * noteId: If currently is editing, indicate which note id is being edited
     * title: If currently is editing, indicate the title of note before edited
     * body: If currently is editing, indicate the body of note before edited
     * importance: If currently is editing, indicate the importance of note before edited
     */
    private fun showAddNoteWindow(isEditing: Boolean = false, noteId: String = "",
                                  title: String = "", body: String = "", importance: Boolean = false){
        val addWindow = Stage()
        // Header
        val headerSection = HBox()
        var headerLabel = Label()
        if (isEditing){
            headerLabel.text = "Edit Note $noteId"
        }
        else {
            headerLabel.text = "Add New Note"
        }
        headerSection.children.add(headerLabel)
        headerSection.padding = Insets(10.0,10.0,5.0,10.0)

        // Title
        val titleSection = HBox()
        val titleLabel = Label("Title")
        val titleText = TextField()
        titleLabel.minWidth = 30.0
        titleLabel.maxWidth = 30.0
        titleText.maxWidth = 290.0
        titleText.minWidth = 290.0
        titleSection.children.addAll(titleLabel, titleText)
        titleSection.padding = Insets(0.0, 10.0, 5.0, 10.0)
        titleSection.spacing = 10.0
        titleSection.alignment = Pos.CENTER_LEFT

        // Body
        val bodySection = HBox()
        val bodyLabel = Label("Body")
        val bodyText = TextArea()
        bodyLabel.minWidth = 30.0
        bodyLabel.maxWidth = 30.0
        bodyText.maxHeight = 130.0
        bodyText.maxWidth = 290.0
        bodyText.minWidth = 290.0
        bodySection.children.addAll(bodyLabel, bodyText)
        bodySection.padding = Insets(0.0, 10.0, 5.0, 10.0)
        bodySection.spacing = 10.0
        bodySection.alignment = Pos.CENTER_LEFT

        // Importance
        val importanceSection = HBox()
        val importanceCheckbox = CheckBox()
        val importanceLabel = Label("Important")
        importanceSection.children.addAll(importanceCheckbox, importanceLabel)
        importanceSection.padding = Insets(0.0, 10.0, 5.0, 50.0)
        importanceSection.spacing = 10.0
        importanceSection.alignment = Pos.CENTER_LEFT

        if (isEditing){
            titleText.text = title
            bodyText.text = body
            importanceCheckbox.isSelected = importance
        }

        // Buttons sections
        val saveAndCloseButtons = HBox()
        val saveButton = Button("Save")
        saveButton.setOnAction {
            val title = titleText.text
            val body = bodyText.text
            var isImportant = ""
            if (importanceCheckbox.isSelected){
                isImportant = "important"
            }
            else{
                isImportant = "unimportant"
            }
            val result = listOf<String>(title, body, isImportant)
            if (isEditing){
                createNote(result, isEditing, noteId)
            }
            else{
                createNote(result)
            }
            darkCover.isVisible = false
            addWindow.close()
        }
        val closeAddButton = Button("Cancel")
        closeAddButton.setOnAction {
            darkCover.isVisible = false
            addWindow.close()
        }
        saveButton.minWidth = 100.0
        closeAddButton.minWidth = 100.0
        saveAndCloseButtons.children.addAll(saveButton, closeAddButton)
        saveAndCloseButtons.spacing = 10.0
        saveAndCloseButtons.alignment = Pos.CENTER_RIGHT
        saveAndCloseButtons.padding = Insets(0.0, 10.0, 5.0, 10.0)

        // Wrapper for the whole window contents
        val addWindowContents = VBox()
        addWindowContents.children.addAll(headerSection, titleSection, bodySection, importanceSection, saveAndCloseButtons)
        addWindowContents.alignment = Pos.TOP_LEFT
        addWindowContents.spacing = 10.0
        val addWindowScene = Scene(addWindowContents, 350.0, 300.0)
        addWindow.scene = addWindowScene
        addWindow.initModality(Modality.APPLICATION_MODAL)
        //Centralize the window
        addWindow.x = stage.x + (stage.width-350)/2
        addWindow.y = stage.y + (stage.height-300)/2
        addWindow.initStyle(StageStyle.UNDECORATED)
        darkCover.isVisible = true

        addWindow.showAndWait()
    }

    /**
     * Main function
     *
     * @author Mason Ma
     * @return void
     * @param stage: the stage of main application
     */

    override fun start(stage: Stage) {
        this.stage = stage
        // Create toolbar buttons
        val addButton = Button("Add")
        // Event Listener when add button is clicked
        addButton.setOnAction {
            showAddNoteWindow()
        }
        addButton.setPrefSize(100.0, 30.0)
        addButton.setMinSize(50.0, 30.0)

        val randomButton = Button("Random")
        // Event Listener when random button is clicked
        randomButton.setOnAction {
            val currRandomNotes = generateRandomNote()
            createNote(currRandomNotes)
        }
        randomButton.setPrefSize(100.0, 30.0)
        randomButton.setMinSize(50.0, 30.0)

        deleteButton.isDisable = true
        // Event Listener when delete button is clicked

        deleteButton.setOnAction {
            if (selected){
                centralSection.children.removeIf { it.id == selectedNote.id }
                currStatus2.text = "Deleted Note #${selectedNote.id}"
                selected = false
                deleteButton.isDisable = true
                selectedNote = StackPane()
            }
        }
        deleteButton.setPrefSize(100.0, 30.0)
        deleteButton.setMinSize(50.0, 30.0)

        clearButton.isDisable = visibleNotes==0
        // Event Listener when clear button is clicked
        clearButton.setOnAction {
            val preNum = totalNotes
            centralSection.children.removeIf { it.isVisible && it.isManaged }
            currStatus2.text = "Cleared ${preNum-totalNotes} Notes"
        }
        clearButton.setPrefSize(100.0, 30.0)
        clearButton.setMinSize(50.0, 30.0)

        val importantButton = Button("!")

        importantButton.setPrefSize(20.0, 30.0)

        // Add search text field
        searchBox.prefWidth(150.0)

        // Event Listener when text is changed in search box
        searchBox.setOnKeyTyped {
            for (i in centralSection.children){
                i.isVisible = false
                i.isManaged = false

                if (i is StackPane){
                    for (j in i.children){
                        if (j is VBox){
                            for (p in j.children){
                                var noteContent = p.toString().substringAfter(']')
                                if (searchBox.text in noteContent){
                                    if (showImportantOnly && i.id in importantNotes){
                                        i.isVisible = true
                                        i.isManaged = true
                                    }
                                    else if (!showImportantOnly){
                                        i.isVisible = true
                                        i.isManaged = true
                                    }

                                }
                            }
                        }
                    }
                }
            }
        }
        // Event Listener when ! button is clicked
        importantButton.setOnAction {
            if (showImportantOnly){
                showImportantOnly = false
                for (i in centralSection.children){
                    if (searchBox.text == ""){
                        i.isVisible = true
                        i.isManaged = true
                    }
                    else{
                        if (i is StackPane){
                            for (j in i.children){
                                if (j is VBox){
                                    for (p in j.children){
                                        var noteContent = p.toString().substringAfter(']')
                                        if (searchBox.text in noteContent){
                                            if (showImportantOnly && i.id in importantNotes){
                                                i.isVisible = true
                                                i.isManaged = true
                                            }
                                            else if (!showImportantOnly){
                                                i.isVisible = true
                                                i.isManaged = true
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                }

            }
            else{
                for (i in centralSection.children){
                    if (i.id !in importantNotes){
                        i.isManaged = false
                        i.isVisible = false
                    }
                }
                showImportantOnly = true
            }

            println("Important Button clicked")
        }

        // Create toolbar
        val toolbarContent = HBox(addButton, randomButton, deleteButton, clearButton, importantButton, searchBox)
        toolbarContent.alignment = Pos.CENTER_LEFT
        toolbarContent.padding = Insets(10.0)
        toolbarContent.spacing = 10.0
        val toolbar = VBox(toolbarContent)

        // Setup Notes Section
        centralSection.alignment = Pos.TOP_LEFT
        centralSection.padding = Insets(10.0)
        centralSection.hgap = 10.0
        centralSection.vgap = 10.0
        val noteSection = ScrollPane(centralSection);
        noteSection.isFitToWidth = true


        // Create Status bar

        val statusbarContent = HBox(currStatus1, currStatus2)
        statusbarContent.alignment = Pos.CENTER_LEFT
        statusbarContent.padding = Insets(10.0)
        statusbarContent.spacing = 30.0
        val statusbar = VBox(statusbarContent)

        // setup scene
        val border = BorderPane()
        border.top = toolbar
        border.bottom = statusbar
        border.center = noteSection
        darkCover.background = Background(BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY))
        darkCover.opacity = 0.7
        darkCover.isVisible = false
        val wrapperPane = StackPane()
        wrapperPane.children.addAll(border, darkCover)
        val scene = Scene(wrapperPane)

        stage.title = "A1 Notes (20775201)"
        stage.scene = scene
        stage.width = 800.0
        stage.height = 600.0
        stage.minHeight = 400.0
        stage.minWidth = 400.0
        stage.show()

        // Add change listener for Notes
        centralSection.children.addListener(ListChangeListener {
            if (centralSection.children.size > totalNotes){
                totalNotes++
            }
            else if (centralSection.children.size < totalNotes){
                totalNotes--
            }
            var countVis = 0
            for (i in centralSection.children){
                if (i.isManaged && i.isVisible){
                    countVis++
                }
            }
            visibleNotes = countVis
            if (totalNotes == visibleNotes){
                currStatus1.text = totalNotes.toString()
            }
            else{
                currStatus1.text = "$visibleNotes (of $totalNotes)"
            }
            clearButton.isDisable = visibleNotes==0
        })
    }



}