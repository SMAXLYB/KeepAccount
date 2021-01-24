package life.chenshi.keepaccounts.ui.setting.book

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import life.chenshi.keepaccounts.R
import life.chenshi.keepaccounts.database.entity.Book

class BookAdapter(private var books: List<Book>) : BaseAdapter() {
    private var mSelectedPosition = 1

    override fun getCount(): Int {
        return books.size
    }

    override fun getItem(position: Int): Any {
        return books[position]
    }

    override fun getItemId(position: Int): Long {
        return books[position].id!!.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val book = books[position]
        val viewHolder: BookViewHolder
        val view: View
        if (convertView == null) {
            view = LayoutInflater.from(parent.context).inflate(R.layout.item_book, parent, false)
            viewHolder = BookViewHolder()
            viewHolder.bookName = view.findViewById(R.id.tv_item_book_name)
            viewHolder.bookDescription = view.findViewById(R.id.tv_item_book_description)
            viewHolder.currentBook = view.findViewById(R.id.tv_item_current_book)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as BookViewHolder
        }

        viewHolder.bookName?.text = book.name
        viewHolder.bookDescription?.text = book.description
        if (position == mSelectedPosition) {
            viewHolder.currentBook?.visibility = View.VISIBLE
        } else {
            viewHolder.currentBook?.visibility = View.GONE
        }

        return view
    }

    fun setData(books: List<Book>) {
        this.books = books
        notifyDataSetChanged()
    }

    private class BookViewHolder() {
        var bookName: TextView? = null
        var bookDescription: TextView? = null
        var currentBook: TextView? = null
    }

    fun setItemSelected(currentPosition: Int) {
        mSelectedPosition = currentPosition
    }
}