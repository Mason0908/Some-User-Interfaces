package com.example.Note

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.Button

import androidx.activity.viewModels
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    // create view model using delegation
    private val viewModel: MyViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // layout is defined in "res/layouts/activity_main.xml"
        // (it must have a NavHostFragment element)
        setContentView(R.layout.activity_main)

        // observe viewModel properties here

        supportActionBar!!.subtitle = "(0 notes)"



        viewModel.message.observe(this){
            if (it != ""){
                val ori = resources.configuration.orientation
                val snackbar = Snackbar.make(findViewById(R.id.snackbar), it, Snackbar.LENGTH_LONG)
                val buttonMain = findViewById<Button>(R.id.clear)
                val buttonView = findViewById<Button>(R.id.save)
                if (ori == 1){
                    if (buttonMain == null){
                        snackbar.setAnchorView(buttonView)
                    }
                    else{
                        snackbar.setAnchorView(buttonMain)

                    }
                }
                snackbar.show()
            }

        }

        viewModel.allNotes.observe(this){
            if (viewModel.isFiltered.value!! || viewModel.search.value!! != ""){
                val matchCount = viewModel.displayNotes.size
                supportActionBar!!.subtitle = "(matches ${matchCount} of ${it.size} notes)"
            }
            else{
                supportActionBar!!.subtitle = "(${it.size} notes)"
            }
//

        }
//
        viewModel.isFiltered.observe(this){
            val matchCount = viewModel.displayNotes.size
            val totalCount = viewModel.noteNum
            if (viewModel.isFiltered.value!! || viewModel.search.value!! != ""){
                supportActionBar!!.subtitle = "(matches ${matchCount} of ${totalCount} notes)"
            }
            else{
                supportActionBar!!.subtitle = "(${totalCount} notes)"
            }
        }

        viewModel.search.observe(this){
            val matchCount = viewModel.displayNotes.size
            val totalCount = viewModel.noteNum
            if (viewModel.isFiltered.value!! || viewModel.search.value!! != ""){
                supportActionBar!!.subtitle = "(matches ${matchCount} of ${totalCount} notes)"
            }
            else{
                supportActionBar!!.subtitle = "(${totalCount} notes)"
            }
        }


        // if you need the navController in activity (for bottom nav, etc.)
//        val navController = findNavController(findViewById(R.id.navhostfragment))
    }

    // region Material ActionBar options menu setup and events

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // uncomment line below to add ActionBar options menu
//        menuInflater.inflate(R.menu.action_menu, menu)
        // (menu is defined in "res/menus/action_menu.xml")
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.actionName -> {
                // update viewModel for this action
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    //endregion
}