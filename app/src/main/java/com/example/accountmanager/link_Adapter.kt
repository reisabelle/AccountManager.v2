import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.accountmanager.Account_Model
import com.example.accountmanager.R

class link_Adapter(private val context: Context, private var dataSource: List<Account_Model>) : BaseAdapter() {

    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int = dataSource.size

    override fun getItem(position: Int): Any = dataSource[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val holder: ViewHolder

        if (convertView == null) {
            view = inflater.inflate(R.layout.link_item, parent, false)
            holder = ViewHolder(
                view.findViewById(R.id.platformText),
                view.findViewById(R.id.linkText)
            )
            view.tag = holder
        } else {
            view = convertView
            holder = convertView.tag as ViewHolder
        }

        val account = getItem(position) as Account_Model
        holder.platformTextView.text = account.platform
        holder.linkTextView.text = account.link

        return view
    }

    fun updateData(newData: List<Account_Model>) {
        dataSource = newData
        notifyDataSetChanged()
    }

    private class ViewHolder(
        val platformTextView: TextView,
        val linkTextView: TextView
    )
}
