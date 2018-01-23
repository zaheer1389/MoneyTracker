package com.ahmadinfotech.moneytracker.fragement;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ahmadinfotech.moneytracker.R;
import com.ahmadinfotech.moneytracker.adaptor.UserAdaptor;
import com.ahmadinfotech.moneytracker.helper.DBHelper;

/**
 * Created by root on 15/1/18.
 */

public class UserFragement extends Fragment{

    DBHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        dbHelper = DBHelper.getInstance();

        RecyclerView rv = new RecyclerView(getContext());
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(new UserAdaptor(dbHelper.getUsers()));
        getActivity().setTitle("Chat");
        return rv;
    }
}
