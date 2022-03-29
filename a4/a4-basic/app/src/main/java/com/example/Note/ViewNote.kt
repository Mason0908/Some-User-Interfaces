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

class ViewNote : Fragment() {

    private val viewModel: MyViewModel by activityViewModels()
    private var note: Note? = null

    override fun onSaveInstanceState(outState: Bundle) {
        with(outState){
            println("here1")
            if (note != null && note!!.id != null){
                println("here2")
                putLong("noteId",note!!.id!!)
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
        val root = inflater.inflate(R.layout.fragment_view_note, container, false)
        val noteId = savedInstanceState?.getLong("noteId")
        println("noteid: $noteId")
        if (noteId != null){
            note = viewModel.getNoteById(noteId)!!
            val title = root.findViewById<TextView>(R.id.viewTitle)
            val body = root.findViewById<TextView>(R.id.viewBody)
            val important = root.findViewById<TextView>(R.id.viewImportant)
            val header = root.findViewById<TextView>(R.id.viewHeader)
            title.text = note!!.title
            body.text = note!!.body
            important.isVisible = note!!.important
            header.text = "Note #${note!!.id}"
        }
        else if (note != null){
            val title = root.findViewById<TextView>(R.id.viewTitle)
            val body = root.findViewById<TextView>(R.id.viewBody)
            val important = root.findViewById<TextView>(R.id.viewImportant)
            val header = root.findViewById<TextView>(R.id.viewHeader)
            title.text = note!!.title
            body.text = note!!.body
            important.isVisible = note!!.important
            header.text = "Note #${note!!.id}"
        }
        setFragmentResultListener("viewNote"){requestKey, bundle ->
            val id = bundle.getLong("viewNoteId")
            note = viewModel.getNoteById(id)!!
            val title = root.findViewById<TextView>(R.id.viewTitle)
            val body = root.findViewById<TextView>(R.id.viewBody)
            val important = root.findViewById<TextView>(R.id.viewImportant)
            val header = root.findViewById<TextView>(R.id.viewHeader)
            title.text = note!!.title
            body.text = note!!.body
            important.isVisible = note!!.important
            header.text = "Note #${note!!.id}"
        }


        val buttonEdit = root.findViewById<Button>(R.id.edit)
        buttonEdit.setOnClickListener {
            setFragmentResult("editNote", bundleOf("editNoteId" to note!!.id))
            findNavController().navigate(R.id.action_viewNote_to_fragment2)
        }

        return root
    }
}