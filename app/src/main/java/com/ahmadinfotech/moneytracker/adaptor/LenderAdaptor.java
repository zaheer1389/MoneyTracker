package com.ahmadinfotech.moneytracker.adaptor;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ahmadinfotech.moneytracker.R;
import com.ahmadinfotech.moneytracker.model.Lender;
import com.ahmadinfotech.moneytracker.model.User;
import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.util.List;

import static android.R.attr.data;

/**
 * Created by root on 16/1/18.
 */

public class LenderAdaptor extends RecyclerView.Adapter<LenderAdaptor.UserViewHolder>{

    private List<Lender> lenders;

    public LenderAdaptor(List<Lender> lenders){
        this.lenders = lenders;
        Log.d("UserAdaptor", "Lenders : "+lenders.size());
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.lender_list, parent, false);
        return new UserViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        Lender lender = lenders.get(position);

        holder.txtName.setText(lender.getName());
        holder.txtAddress.setText(lender.getAddress());
        holder.txtBalanceAmount.setText(lender.getBalance()+"");

        String letter = String.valueOf(lender.getName().charAt(0));

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
        return lenders.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder{

        public ImageView imageView;
        public TextView txtName;
        public TextView txtAddress;
        public TextView txtBalanceAmount;

        public UserViewHolder(View view){
            super(view);
            imageView = (ImageView)view.findViewById(R.id.lenderIcon);
            txtName = (TextView)view.findViewById(R.id.txtLenderName);
            txtAddress = (TextView)view.findViewById(R.id.txtLenderAddress);
            txtBalanceAmount = (TextView)view.findViewById(R.id.txtBalanceAmount);
        }
    }
}
