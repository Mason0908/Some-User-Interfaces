package com.example.Note

import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import kotlin.random.Random

class AddNote : Fragment() {

    private val viewModel: MyViewModel by activityViewModels()
    private var note: Note? = null
    private var isEditing = false

    override fun onSaveInstanceState(outState: Bundle) {
        with(outState){
            if (note != null && note!!.id != null){
                putLong("noteId", note!!.id!!)
            }
        }
        super.onSaveInstanceState(outState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // layout is defined in "res/layouts/fragment_2.xml"
        val root = inflater.inflate(R.layout.fragment_add_note, container, false)


        val noteId = savedInstanceState?.getLong("noteId")
        if (noteId != null){
            note = viewModel.getNoteById(noteId)!!
            val title = root.findViewById<EditText>(R.id.editTitle)
            val body = root.findViewById<EditText>(R.id.editBody)
            val important = root.findViewById<Switch>(R.id.important)
            val header = root.findViewById<TextView>(R.id.editHeader)
            title.setText(note!!.title)
            body.setText(note!!.body)
            important.isChecked = note!!.important
            header.text = "Edit #${note!!.id}"
            isEditing = true
        }

        setFragmentResultListener("editNote"){requestKey, bundle ->
            val id = bundle.getLong("editNoteId")
            note = viewModel.getNoteById(id)!!
            val title = root.findViewById<EditText>(R.id.editTitle)
            val body = root.findViewById<EditText>(R.id.editBody)
            val important = root.findViewById<Switch>(R.id.important)
            val header = root.findViewById<TextView>(R.id.editHeader)
            title.setText(note!!.title)
            body.setText(note!!.body)
            important.isChecked = note!!.important
            header.text = "Edit #${note!!.id}"
            isEditing = true
        }

        val buttonSave = root.findViewById<Button>(R.id.save)
        buttonSave.setOnClickListener {

            val important = root.findViewById<Switch>(R.id.important).isChecked
            val title = root.findViewById<EditText>(R.id.editTitle).text.toString()
            val body = root.findViewById<EditText>(R.id.editBody).text.toString()
            if (isEditing){
                val note = Note(note!!.id, title, body, important)
                viewModel.updateNote(note)
                setFragmentResult("viewNote", bundleOf("viewNoteId" to note.id))
                findNavController().navigate(R.id.action_fragment2_to_viewNote)
            }
            else{
                val note = Note(null, title, body, important)
                viewModel.addNote(note)
                findNavController().navigate(R.id.action_fragment2_to_fragment1)

            }

        }

//        viewModel.property.observe(this) {
//            println("observe property $it")
//            // update UI here
//
//        }

        return root
    }
}