package life.chenshi.keepaccounts.ui.setting.book

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import life.chenshi.keepaccounts.R
import life.chenshi.keepaccounts.database.entity.Book
import life.chenshi.keepaccounts.module.common.utils.gone
import life.chenshi.keepaccounts.module.common.utils.visible

class BookAdapter(private var books: List<Book>) : BaseAdapter() {
    private var mSelectedPosition = -1

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
        val view: View
        if (book.id == -1) {
            view =
                LayoutInflater.from(parent.context).inflate(R.layout.item_book_empty, parent, false)
            return view
        }
        val viewHolder: BookViewHolder
        // 如果没有view可以复用或者复用的view是最后一个Item,则新建View
        if (convertView == null || convertView.tag == null) {
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

        // 本地没有id
        if (mSelectedPosition == -1) {
            viewHolder.currentBook?.gone()
            return view
        }
        // 本地有id
        if (mSelectedPosition == position) {
            viewHolder.currentBook?.visible()
        } else {
            viewHolder.currentBook?.gone()
        }

        return view
    }

    fun setData(books: List<Book>) {
        this.books = books
        notifyDataSetChanged()
    }

    class BookViewHolder() {
        var bookName: TextView? = null
        var bookDescription: TextView? = null
        var currentBook: TextView? = null
    }

    fun setItemSelected(currentPosition: Int) {
        mSelectedPosition = currentPosition
    }
}