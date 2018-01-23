package com.ahmadinfotech.moneytracker.adaptor;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ahmadinfotech.moneytracker.R;
import com.ahmadinfotech.moneytracker.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 16/1/18.
 */

public class UserAdaptor extends RecyclerView.Adapter<UserAdaptor.UserViewHolder>{

    List<User> users;

    public UserAdaptor(List<User> users){
        this.users = users;
        Log.d("UserAdaptor", "Users : "+users.size());
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_item, parent, false);
        return new UserViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        User user= users.get(position);
        holder.txtId.setText(user.getuId());
        holder.txtEmail.setText(user.getEmail());
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder{

        public TextView txtId;
        public TextView txtEmail;

        public UserViewHolder(View view){
            super(view);
            txtId = (TextView)view.findViewById(R.id.txtUserId);
            txtEmail = (TextView)view.findViewById(R.id.txtUserEmail);
        }
    }
}
