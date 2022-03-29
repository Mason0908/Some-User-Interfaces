package com.example.Note

import androidx.lifecycle.*

class MyViewModel() : ViewModel() {

    // add all observable properties here
    val property: MutableLiveData<Int> = MutableLiveData<Int>(0)

    init {
    }

    // add modelview update functions here

    //endregion

    // the list of notes, keep it private so views can only
    // get the filtered lit
    val allNotes: MutableLiveData<MutableList<Note>> =
        MutableLiveData<MutableList<Note>>(mutableListOf<Note>())

    // public way to get number of notes in the list
    val noteNum: Int
        get() = allNotes.value?.size ?: 0

    // importance filtering
    val isFiltered: MutableLiveData<Boolean> = MutableLiveData(false)

    fun setFiltered(value: Boolean){
        isFiltered.value = value
    }

    // search string to filter
    var search: MutableLiveData<String> = MutableLiveData("")

    fun setSearch(value: String){
        if (search.value != value){
            search.value = value.lowercase()
        }
    }

    // model's last message describing operation

    var message: MutableLiveData<String> = MutableLiveData("")

    // list of notes to display that considers importance and search filers
    val displayNotes: List<Note>
        get() {
            val filtered = isFiltered.value ?: false
            val list =
                if (filtered)
                    allNotes.value?.filter { x -> x.important }
                else
                    allNotes.value

            // include any search filtering
            if (search.value != null) {
                return list!!.filter {
                        x -> x.title.lowercase().contains(search.value!!) ||
                        x.body.lowercase().contains(search.value!!) }
            } else {
                return list!!
            }
        }


    // simple unique note ID, just count up from 0 each run
    private var _counter: Long = 0
    private fun generateID(): Long {
        return _counter++
    }

    fun addNote(newNote: Note) {
        val note = newNote.copy(id = generateID())
        val currNotes = allNotes.value
        currNotes?.add(note)
        allNotes.value = currNotes!!
        message.value = "Added Note #${note.id}"
    }

    // clear only notes that are displayed
    fun clearNotes() {
        message.value = "Cleared ${displayNotes.size} notes"
        val currNotes = allNotes.value!!
        currNotes.removeAll(displayNotes)
        allNotes.value = currNotes
    }

    // the model handles note selection
    // state = null mean toggle
    // (arguably should be in View or Controller)
    fun selectNote(id: Long, state: Boolean? = null) {
        // get current select state
        val currNotes = allNotes.value!!
        var note = currNotes.find { x -> x.id == id }

        // check if note is null (shouldn't be)
        if (note == null) {
            println("selectNote with null note")
            return
        }

        // set new state
        val newState =
            if (state == null) {
                !note.isSelected
            } else {
                state
            }

        // deselect all notes (should only be 0 or 1 selected already)
        for (n in currNotes) {
            n.isSelected = false
        }

        // get the new selection
        note.isSelected = newState
        allNotes.value = currNotes
    }

    fun getSelectedNote(): Note? {
        val currNotes = allNotes.value!!
        return currNotes.find { x -> x.isSelected }
    }

    fun getNoteById(id: Long): Note? {
        val currNotes = allNotes.value!!
        return currNotes.find { x -> x.id == id }
    }

    fun deleteNoteById(id: Long) {
        // make sure we have a note selected first
        var note = getNoteById(id)
        val currNotes = allNotes.value!!
        if (note != null) {
            currNotes.remove(note)
            message.value = "Deleted Note #${note.id}"
            allNotes.value = currNotes
        }
    }

    fun updateNote(updatedNote: Note) {
        val currNotes = allNotes.value!!
        var note = currNotes.find { x -> x.id == updatedNote.id }
        // must be a better way to do this part?
        if (note != null) {
            note.title = updatedNote.title
            note.body = updatedNote.body
            note.important = updatedNote.important
            message.value = "Edited Note #${note.id}"
        }
        allNotes.value = currNotes
    }
}