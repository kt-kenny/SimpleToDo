package com.example.simpletodo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {
    var listOfTasks = mutableListOf<String>()
    lateinit var adapter: TaskItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadItems()
        //look up recycler view in layout
        val longClickListener = object : TaskItemAdapter.OnLongClickListener{
            override fun onItemLongClicked(position: Int) {
                //Remove item from list
                listOfTasks.removeAt(position)
                //Notify adapter that our data set has changed
                adapter.notifyDataSetChanged()
                saveItems()
            }

        }
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        adapter = TaskItemAdapter(listOfTasks, longClickListener )

        recyclerView.adapter = adapter

        recyclerView.layoutManager = LinearLayoutManager(this)

        val inputTextField = findViewById<EditText>(R.id.addTaskView)
        //Set up the button and input field so that the user can enter a task
        findViewById<Button>(R.id.button).setOnClickListener{
            //1) Grab text the user inputed int @id.addTaskField
            val userInputtedTask = inputTextField.text.toString()
            //2) add the string to our list of tasks
            listOfTasks.add(userInputtedTask)
            adapter.notifyItemInserted(listOfTasks.size -1)
            //3) reset text field
            inputTextField.setText("")
            saveItems()
        }
    }

    //Save the data that the user has inputted
    //Save by writing and reading from a file
    //Create a method to get the file we need
    fun getDataFile(): File {

        return File(filesDir, "data.txt")
    }

    //Load the items by reading every line in data file
    fun loadItems(){
        try {
            listOfTasks = FileUtils.readLines(getDataFile(), Charset.defaultCharset())
        } catch (ioException:IOException){
            ioException.printStackTrace()
        }
    }
    //Save items by writing into our data file
    fun saveItems(){
        try{
            FileUtils.writeLines(getDataFile(),listOfTasks)
        } catch(ioException: IOException){
            ioException.printStackTrace()
        }

    }
}