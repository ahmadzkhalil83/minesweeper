package com.ayikz.minesweeper

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView

class RecyclerViewAdapter(var context: Context,
                          val listener: RecyclerViewItemListener,
                          board: Board) : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    var cells = board.cells.flatten()

    fun updateBoard(board: Board){
        cells = arrayListOf()
        cells = board.cells.flatten()
        notifyDataSetChanged()
    }

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener, View.OnLongClickListener {

        var textView: TextView
        var imageView: ImageView
        var layout: RelativeLayout
        private var cell: Cell? = null

        init {
            v.setOnClickListener(this)
            v.setOnLongClickListener(this)
            textView = v.findViewById(R.id.tv_cell_title)
            imageView = v.findViewById(R.id.img_cell_flag)
            layout = v.findViewById(R.id.lyt_cell)
        }

        fun setCell(cell: Cell){
            this.cell = cell
        }

        override fun onClick(view: View) {
            cell?.let {
                listener.onItemClick(it)
            }
        }

        override fun onLongClick(view: View?): Boolean {
            cell?.let {
                listener.onItemLongClick(it)
            }

            return true
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.grid_cell, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cell = cells[position]
        holder.setCell(cell)

        holder.textView.text = cell.getCellText()
        holder.imageView.visibility = if (cell.isFlagVisible()) View.VISIBLE else View.GONE
        holder.layout.setBackgroundResource(cell.background())
    }

    override fun getItemCount(): Int {
        return cells.count()
    }

    interface RecyclerViewItemListener {
        fun onItemClick(cell: Cell)
        fun onItemLongClick(cell: Cell)
    }
}