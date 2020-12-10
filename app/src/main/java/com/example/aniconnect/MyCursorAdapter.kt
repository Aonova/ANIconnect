package com.example.aniconnect

import android.database.Cursor
import android.database.sqlite.SQLiteCursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyCursorAdapter(private val cursor: Cursor): RecyclerView.Adapter<MyCursorAdapter.ViewHolder>(){
    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title = view.findViewById<TextView>(R.id.titleText_rowItem)
        val subtitle = view.findViewById<TextView>(R.id.titleSubText_rowItem)
        val eps = view.findViewById<TextView>(R.id.epText_rowItem)
        val date = view.findViewById<TextView>(R.id.dateText_rowItem)
        val delBut = view.findViewById<ImageButton>(R.id.delButton_rowItem)
        val launchBut = view.findViewById<ImageButton>(R.id.launchButton_rowItem)
        val webBut = view.findViewById<ImageButton>(R.id.webButton_rowItem)
        val linkBut = view.findViewById<ImageButton>(R.id.linkButton_rowItem)

    }
    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.row_item, viewGroup, false)
        return ViewHolder(view)
    }
    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        cursor.moveToPosition(position)
        // TODO: fill all views and add onclick handlers to all buttons
    }
    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = cursor.count

}