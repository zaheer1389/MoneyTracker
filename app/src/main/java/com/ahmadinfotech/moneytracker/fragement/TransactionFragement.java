package com.ahmadinfotech.moneytracker.fragement;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.ahmadinfotech.moneytracker.R;
import com.ahmadinfotech.moneytracker.adaptor.LenderAdaptor;
import com.ahmadinfotech.moneytracker.adaptor.TransactionAdaptor;
import com.ahmadinfotech.moneytracker.helper.DBHelper;
import com.ahmadinfotech.moneytracker.helper.Helper;
import com.ahmadinfotech.moneytracker.listener.RecyclerItemClickListener;
import com.ahmadinfotech.moneytracker.model.Lender;
import com.ahmadinfotech.moneytracker.model.Transaction;

import java.util.List;

/**
 * Created by root on 15/1/18.
 */

public class TransactionFragement extends Fragment{

    DBHelper dbHelper;
    List<Transaction> transactions;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        dbHelper = DBHelper.getInstance();
        String id = getArguments().getString("id");
        transactions = dbHelper.getTransactions(id);

        RecyclerView rv = new RecyclerView(getContext());
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(new TransactionAdaptor(transactions));
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        getActivity().setTitle("Lenders");

        rv.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), rv, new RecyclerItemClickListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                final Transaction transaction = transactions.get(position);
                Bundle bundle = new Bundle();
                bundle.putString("id", transaction.getId());
                bundle.putString("action","view");
                ManageTransactionFragement fragement = new ManageTransactionFragement();
                fragement.setArguments(bundle);
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, fragement).addToBackStack("fragBack");
                ft.commit();
            }

            @Override
            public void onLongClick(View view, int position) {
                final Transaction transaction = transactions.get(position);
                PopupMenu popup = new PopupMenu(getContext(), view);
                //inflating menu from xml resource
                popup.inflate(R.menu.options_menu);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menuDelete:
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setMessage("Do you really want to delete this record?")
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                // FIRE ZE MISSILES!
                                                dbHelper.deleteTransaction(transaction);
                                                TransactionFragement fragement = new TransactionFragement();
                                                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                                                ft.replace(R.id.content_frame, fragement);
                                                ft.commit();
                                                Helper.displayMessageToast(getContext(), "Lender deleted successfully");
                                            }
                                        })
                                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                // User cancelled the dialog
                                            }
                                        });
                                // Create the AlertDialog object and return it
                                builder.create();
                                builder.show();
                                break;
                            case R.id.menuEdit:
                                //Lender lender = lenders.get(position);
                                Bundle bundle = new Bundle();
                                bundle.putString("id", transaction.getId());
                                bundle.putString("action","edit");
                                ManageTransactionFragement fragement = new ManageTransactionFragement();
                                fragement.setArguments(bundle);
                                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                                ft.replace(R.id.content_frame, fragement).addToBackStack("fragBack");
                                ft.commit();
                                break;
                        }
                        return false;
                    }
                });
                //displaying the popup
                popup.show();
            }
        }));

        return rv;
    }
}
