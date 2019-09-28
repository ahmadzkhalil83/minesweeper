package com.ayikz.minesweeper

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class RecyclerViewAdapter(var context: Context,
                          val listener: RecyclerViewItemListener,
                          var board: Board) : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    var cells = board.cells.flatten()

    fun updateBoard(board: Board){
        this.cells = arrayListOf()
        this.cells = board.cells.flatten()
        notifyDataSetChanged()
    }

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {

        var textView: TextView
        private var cell: Cell? = null

        init {

            v.setOnClickListener(this)
            textView = v.findViewById(R.id.tv_cell_title)

        }

        fun setCell(cell: Cell){
            this.cell = cell
        }

        override fun onClick(view: View) {
            cell?.let {
                listener.onItemClick(it)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.grid_cell, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cell = cells[position]
        holder.setCell(cell)

        holder.textView.text = if (cell.isOpen) cell.neighboringMines.toString() else ""
    }

    override fun getItemCount(): Int {
        return cells.count()
    }

    interface RecyclerViewItemListener {
        fun onItemClick(cell: Cell)
    }
}