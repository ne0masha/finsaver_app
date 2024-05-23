package com.example.betaversionapp.other


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.betaversionapp.R
import com.example.betaversionapp.data.db.DateConverter
import com.example.betaversionapp.data.db.entities.Transaction
import com.example.betaversionapp.ui.TransactionUpsertDialog
import com.example.betaversionapp.ui.TransactionsListViewModel
import com.example.betaversionapp.ui.UpsertDialogListener

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

        val deleteButton = holder.itemView.findViewById<ImageButton>(R.id.buttonDelete)
        deleteButton.setOnClickListener {
            viewModel.delete(curTransactionItem)
        }

        textViewDate.text = DateConverter.longToDate(curTransactionItem.date)
        textViewAmount.text = String.format("%.2f", curTransactionItem.amount / 100.0)

        if (curTransactionItem.isIncome) {
            textViewPlusMinus.text = "+ "
            textViewPlusMinus.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.green_text))
            textViewAmount.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.green_text))
        } else {
            textViewPlusMinus.text = "- "
            textViewPlusMinus.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.red_text))
            textViewAmount.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.red_text))
        }
    }

    inner class TransactionsViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView)
}
