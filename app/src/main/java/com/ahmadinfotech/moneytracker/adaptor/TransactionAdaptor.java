package com.ahmadinfotech.moneytracker.adaptor;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ahmadinfotech.moneytracker.R;
import com.ahmadinfotech.moneytracker.model.Lender;
import com.ahmadinfotech.moneytracker.model.Transaction;
import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by root on 16/1/18.
 */

public class TransactionAdaptor extends RecyclerView.Adapter<TransactionAdaptor.UserViewHolder>{

    private List<Transaction> transactions;

    public TransactionAdaptor(List<Transaction> transactions){
        this.transactions = transactions;
        Log.d("UserAdaptor", "Transactions : "+transactions.size());
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_list, parent, false);
        return new UserViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        Transaction transaction = transactions.get(position);

        holder.txtTransactionType.setText(transaction.getTransactionCategory());
        holder.txtDesc.setText(transaction.getDescription());
        holder.txtAmount.setText(transaction.getAmount()+"");
        holder.txtDate.setText(new SimpleDateFormat("dd-MMM-yyyy").format(transaction.getTransactionDate()));
        holder.txtPaymentMode.setText(transaction.getPaymentMode());

        String letter = String.valueOf(transaction.getTransactionCategory().charAt(0));

        ColorGenerator generator = ColorGenerator.MATERIAL;
        //Create a new TextDrawable for our image's background
        TextDrawable drawable = TextDrawable.builder()
                .beginConfig()
                .fontSize(80) /* size in px */
                .bold()
                .toUpperCase()
                .endConfig()
                .buildRound(letter, generator.getRandomColor());
        holder.imageView.setImageDrawable(drawable);
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder{

        public ImageView imageView;
        public TextView txtTransactionType;
        public TextView txtDesc;
        public TextView txtAmount;
        public TextView txtDate;
        public TextView txtPaymentMode;

        public UserViewHolder(View view){
            super(view);
            imageView = (ImageView)view.findViewById(R.id.transactionIcon);
            txtTransactionType = (TextView)view.findViewById(R.id.txtTransactionType);
            txtDesc = (TextView)view.findViewById(R.id.txtDesc);
            txtAmount = (TextView)view.findViewById(R.id.txtTransactionAmount);
            txtDate = (TextView)view.findViewById(R.id.txtDate);
            txtPaymentMode = (TextView)view.findViewById(R.id.txtPaymentMode);
        }
    }
}
