package com.example.Note

import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import kotlin.random.Random

class NoteBoard : Fragment() {

    private val viewModel: MyViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // layout is defined in "res/layouts/fragment_1.xml"
        val root = inflater.inflate(R.layout.fragment_note_board, container, false)

        val buttonAdd = root.findViewById<Button>(R.id.add)
        buttonAdd.setOnClickListener {
            findNavController().navigate(R.id.action_f1_to_f2)
        }

        val switchImportant = root.findViewById<Switch>(R.id.filter)
        switchImportant.setOnCheckedChangeListener { compoundButton, b ->
            viewModel.setFiltered(b)
        }

        val buttonClear = root.findViewById<Button>(R.id.clear)
        buttonClear.isEnabled = false
        buttonClear.setOnClickListener {
            viewModel.clearNotes()
        }

        val searchBar = root.findViewById<EditText>(R.id.search)
        searchBar.addTextChangedListener {
            viewModel.setSearch(searchBar.text.toString())
        }
        // add UI handles for navigation here
        val buttonRandom = root.findViewById<Button>(R.id.random)
        buttonRandom.setOnClickListener {
            // (setup navigation actions in "rs/navigation/navigation.xml")
//            findNavController().navigate(R.id.action_f1_to_f2)
            addRandomNewNote()
        }

        // add UI handlers that call your viewmodel here

        // observe viewModel properties here

        val nl = root.findViewById<LinearLayout>(R.id.noteList)

        viewModel.search.observe(this) {search ->
            nl.removeAllViews()
            val notes = viewModel.displayNotes
            for (n in notes){
                val view = inflater.inflate(R.layout.note_card, null)
                val title = view.findViewById<TextView>(R.id.noteTitle)
                val body = view.findViewById<TextView>(R.id.noteBody)
                val card = view.findViewById<CardView>(R.id.noteCard)
                val deleteButton = view.findViewById<Button>(R.id.delete)
                title.text = n.title
                body.text = n.body
                if (n.important){
                    card.setCardBackgroundColor(ContextCompat.getColor(context!!, R.color.important))
                }
                deleteButton.setOnClickListener {
                    viewModel.deleteNoteById(n.id!!)
                }
                card.setOnClickListener {
                    setFragmentResult("viewNote", bundleOf("viewNoteId" to n.id))
                    findNavController().navigate(R.id.action_fragment1_to_viewNote)
                }
                nl.addView(view)
            }
            buttonClear.isEnabled = notes.isNotEmpty()
        }

        viewModel.isFiltered.observe(this) {filter ->
            nl.removeAllViews()
            val notes = viewModel.displayNotes
            for (n in notes){
                val view = inflater.inflate(R.layout.note_card, null)
                val title = view.findViewById<TextView>(R.id.noteTitle)
                val body = view.findViewById<TextView>(R.id.noteBody)
                val card = view.findViewById<CardView>(R.id.noteCard)
                val deleteButton = view.findViewById<Button>(R.id.delete)
                title.text = n.title
                body.text = n.body
                if (n.important){
                    card.setCardBackgroundColor(ContextCompat.getColor(context!!, R.color.important))
                }
                deleteButton.setOnClickListener {
                    viewModel.deleteNoteById(n.id!!)
                }
                card.setOnClickListener {
                    setFragmentResult("viewNote", bundleOf("viewNoteId" to n.id))
                    findNavController().navigate(R.id.action_fragment1_to_viewNote)
                }
                nl.addView(view)
            }
            buttonClear.isEnabled = notes.isNotEmpty()
        }

        viewModel.allNotes.observe(this) {
            nl.removeAllViews()
            val notes = viewModel.displayNotes
            for (n in notes){
                val view = inflater.inflate(R.layout.note_card, null)
                val title = view.findViewById<TextView>(R.id.noteTitle)
                val body = view.findViewById<TextView>(R.id.noteBody)
                val card = view.findViewById<CardView>(R.id.noteCard)
                val deleteButton = view.findViewById<Button>(R.id.delete)
                title.text = n.title
                body.text = n.body
                if (n.important){
                    card.setCardBackgroundColor(ContextCompat.getColor(context!!, R.color.important))
                }
                deleteButton.setOnClickListener {
                    viewModel.deleteNoteById(n.id!!)
                }
                card.setOnClickListener {
                    setFragmentResult("viewNote", bundleOf("viewNoteId" to n.id))
                    findNavController().navigate(R.id.action_fragment1_to_viewNote)
                }
                nl.addView(view)
            }
            buttonClear.isEnabled = notes.isNotEmpty()

        }

        return root
    }

    fun addRandomNewNote(){
        val title = LoremIpsum.getRandomSequence(Random.nextInt(1, 3))
            .split(" ")
            .joinToString(" ") { it.replaceFirstChar { it.uppercase() } }

        // body is 2 to 5 sentences, each 3 to 10 words long
        var body = ""
        for (i in 0..Random.nextInt(2, 5)) {
            val sentence = LoremIpsum.getRandomSequence(Random.nextInt(3, 10))
                .replaceFirstChar { it.uppercase() }
                .plus(". ")
            body = body.plus(sentence)
        }

        // create the new note and pick random importance with prob 20%
        val note = Note(null, title, body, important = (Random.nextDouble() < 0.2))
        viewModel.addNote(note)
    }
}