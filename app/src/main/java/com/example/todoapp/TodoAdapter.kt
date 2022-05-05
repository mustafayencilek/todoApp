package com.example.todoapp

import android.annotation.SuppressLint
import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TodoAdapter(private val todos: MutableList<Todo>) :
    RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

    class TodoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTodoTitle: TextView = view.findViewById(R.id.tvTodoTitle)
        val cbDone: CheckBox = view.findViewById(R.id.cbDone)
    }

    fun addTodo(todo: Todo) {
        todos.add(todo)
        notifyItemInserted(todos.size - 1)
    }

    @SuppressLint("Range")
    fun deleteDoneTodos(database: SQLiteDatabase) {


        for (i in 0..todos.size - 1) {
            if (todos[i].isChecked == true) {
                var tempTitle: String = todos[i].title
                database.delete("todotable", "title=?", arrayOf((tempTitle).toString()))

            }
        }
        todos.removeAll { todo ->
            todo.isChecked
        }
        notifyDataSetChanged()
    }


    private fun toggleStrikeThrough(tvTodoTitle: TextView, isChecked: Boolean) {
        if (isChecked)
            tvTodoTitle.paintFlags = tvTodoTitle.paintFlags or STRIKE_THRU_TEXT_FLAG
        else {
            tvTodoTitle.paintFlags = tvTodoTitle.paintFlags and STRIKE_THRU_TEXT_FLAG.inv()

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_todo, parent, false)
        return TodoViewHolder(v)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val curtodos = todos[position]
        holder.tvTodoTitle.text = curtodos.title
        holder.cbDone.isChecked = curtodos.isChecked
        toggleStrikeThrough(tvTodoTitle = holder.tvTodoTitle, holder.cbDone.isChecked)
        holder.cbDone.setOnCheckedChangeListener { _, isChecked ->

            toggleStrikeThrough(tvTodoTitle = holder.tvTodoTitle, isChecked)
            curtodos.isChecked = !curtodos.isChecked

        }
    }

    override fun getItemCount(): Int {
        return todos.size
    }
}