package com.example.todoapp

import android.annotation.SuppressLint
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerview: RecyclerView
    private lateinit var edittexttitle: EditText
    private lateinit var todoAddbutton: Button
    private lateinit var todoDeletebutton: Button
    private lateinit var todoadapter: TodoAdapter
    lateinit var database: SQLiteDatabase

    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerview = findViewById(R.id.recyclerview)
        edittexttitle = findViewById(R.id.edittexttitle)
        todoAddbutton = findViewById(R.id.todoAddbutton)
        todoDeletebutton = findViewById(R.id.todoDeletebutton)
        todoadapter = TodoAdapter(mutableListOf())
        recyclerview.adapter = todoadapter
        recyclerview.layoutManager = LinearLayoutManager(this@MainActivity)
        database = this.openOrCreateDatabase("TodoDB", MODE_PRIVATE, null)
        database.execSQL("CREATE TABLE IF NOT EXISTS todotable(id INTEGER PRIMARY KEY,title VARCHAR,isChecked BOOLEAN)")
        val cursor: Cursor = database.rawQuery("select title, isChecked from todotable ", null)

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast) {
                val name: String = cursor.getString(cursor.getColumnIndex("title"))

                val isChecked = cursor.getString(cursor.getColumnIndex("isChecked"))
                val todo: Todo = Todo(name, isChecked.toBoolean())
                todoadapter.addTodo(todo)
                cursor.moveToNext()
            }
        }

        todoAddbutton.setOnClickListener {
            val title = edittexttitle.text.toString()

            try {

                val sqlString = "INSERT INTO todotable (title,isChecked) VALUES(?,?)"
                val statement = database.compileStatement(sqlString)
                statement.bindString(1, title)
                statement.bindString(2, "false")
                statement.execute()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            if (title.isNotEmpty()) {
                val todo: Todo = Todo(title, false)
                todoadapter.addTodo(todo)
                edittexttitle.text.clear()
            }
        }
        todoDeletebutton.setOnClickListener {
            todoadapter.deleteDoneTodos(database)

        }
    }

}