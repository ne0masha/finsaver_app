package com.example.betaversionapp.ui.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.betaversionapp.R
import com.example.betaversionapp.data.utils.DateConverter
import com.example.betaversionapp.data.utils.ResourcesUtil
import com.example.betaversionapp.data.db.entities.Transaction
import com.example.betaversionapp.ui.TransactionUpsertDialog
import com.example.betaversionapp.ui.TransactionsListViewModel
import com.example.betaversionapp.ui.listeners.UpsertDialogListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TransactionsListAdapter(
    private val activity: AppCompatActivity, // Updated to receive AppCompatActivity
    var items: List<Transaction>,
    private val viewModel: TransactionsListViewModel
): RecyclerView.Adapter<TransactionsListAdapter.TransactionsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        return TransactionsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: TransactionsViewHolder, position: Int) {
        val curTransactionItem = items[position]

        holder.itemView.setOnClickListener {
            TransactionUpsertDialog(
                activity, // Updated to use activity context
                viewModel,
                object : UpsertDialogListener {
                    override fun onAddButtonClicked(item: Transaction) {
                        viewModel.upsert(curTransactionItem)
                    }
                },
                curTransactionItem,
                curTransactionItem.isIncome
            ).show()
        }

        val textViewDate = holder.itemView.findViewById<TextView>(R.id.textViewDate)
        val textViewPlusMinus = holder.itemView.findViewById<TextView>(R.id.textViewIsIncome)
        val textViewAmount = holder.itemView.findViewById<TextView>(R.id.textViewAmount)
        val categoryIcon = holder.itemView.findViewById<ImageView>(R.id.categoryIcon)
        val categoryText = holder.itemView.findViewById<TextView>(R.id.categoryText)

        val deleteButton = holder.itemView.findViewById<ImageButton>(R.id.buttonDelete)
        deleteButton.setOnClickListener {
            viewModel.delete(curTransactionItem)
        }

        textViewDate.text = DateConverter.longToDate(curTransactionItem.date)
        textViewAmount.text = String.format("%.2f", curTransactionItem.amount / 100.0)

        val textColor: Int
        val plusMinus: String
        if (curTransactionItem.isIncome) {
            plusMinus = "+ "
            textColor = R.color.green_text
        } else {
            plusMinus = "- "
            textColor = R.color.red_text
        }
        textViewPlusMinus.text = plusMinus
        textViewPlusMinus.setTextColor(ContextCompat.getColor(holder.itemView.context, textColor))
        textViewAmount.setTextColor(ContextCompat.getColor(holder.itemView.context, textColor))

        CoroutineScope(Dispatchers.IO).launch {
            val category = viewModel.getCategoryById(curTransactionItem.categoryId)
            val iconId = ResourcesUtil.getResourceIdByName(holder.itemView.context, category!!.icon)
            val drawable = ContextCompat.getDrawable(holder.itemView.context, iconId)
            withContext(Dispatchers.Main) {
                categoryText.text = category.name
                categoryIcon.setImageDrawable(drawable)
            }
        }
    }

    inner class TransactionsViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView)
}
