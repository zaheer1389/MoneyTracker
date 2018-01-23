package com.ahmadinfotech.moneytracker.fragement;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ahmadinfotech.moneytracker.R;
import com.ahmadinfotech.moneytracker.activity.MainActivity;
import com.ahmadinfotech.moneytracker.adaptor.LenderAdaptor;
import com.ahmadinfotech.moneytracker.helper.DBHelper;
import com.ahmadinfotech.moneytracker.model.Lender;

import static com.ahmadinfotech.moneytracker.R.id.btnSave;
import static com.ahmadinfotech.moneytracker.helper.Helper.isValidEmail;

/**
 * Created by root on 15/1/18.
 */

public class ManageLenderFragement extends Fragment{

    DBHelper dbHelper;

    EditText txtLenderName;
    EditText txtLenderAddress;
    EditText txtLenderMobileNo;

    TextInputLayout inputLenderName;
    TextInputLayout inputLenderAddress;
    TextInputLayout inputLenderMobileNo;
    Lender lender;
    Window window;
    private FragmentActivity myContext;

    @Override
    public void onAttach(Activity activity) {
        myContext = (FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        dbHelper = DBHelper.getInstance();
        String id = getArguments().getString("id");
        lender  = dbHelper.getLenderById(id);

        window = getActivity().getWindow();

        View view = inflater.inflate(R.layout.add_edit_delete_lender, container, false);
        txtLenderName = (EditText) view.findViewById(R.id.txtLenderName);
        txtLenderAddress = (EditText) view.findViewById(R.id.txtLenderAddress);
        txtLenderMobileNo = (EditText) view.findViewById(R.id.txtLenderMobileNo);

        inputLenderName = (TextInputLayout)  view.findViewById(R.id.inputLenderName);
        inputLenderAddress = (TextInputLayout)  view.findViewById(R.id.inputLenderAddress);
        inputLenderMobileNo = (TextInputLayout)  view.findViewById(R.id.inputLenderMobileNo);

        Button btnAddEdit = (Button) view.findViewById(R.id.btnSave);
        //Button btnDelete = (Button) view.findViewById(R.id.btnDelete);

        if(lender != null){
            btnAddEdit.setText("Update");

            txtLenderName.setText(lender.getName());
            txtLenderAddress.setText(lender.getAddress());
            txtLenderMobileNo.setText(lender.getMobile());
        }
        else{
            //btnDelete.setVisibility(View.INVISIBLE);
        }

        btnAddEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Do you really want to save this record?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // FIRE ZE MISSILES!
                                if(validate()){
                                    if(lender == null){
                                        Lender lender = new Lender();
                                        lender.setId("");
                                        lender.setName(txtLenderName.getText().toString());
                                        lender.setAddress(txtLenderAddress.getText().toString());
                                        lender.setMobile(txtLenderMobileNo.getText().toString());
                                        dbHelper.saveLender(lender);
                                    }
                                    else{
                                        lender.setName(txtLenderName.getText().toString());
                                        lender.setAddress(txtLenderAddress.getText().toString());
                                        lender.setMobile(txtLenderMobileNo.getText().toString());
                                        dbHelper.saveLender(lender);
                                    }

                                    LenderFragement fragement = new LenderFragement();
                                    FragmentTransaction ft = myContext.getSupportFragmentManager().beginTransaction();
                                    ft.replace(R.id.content_frame, fragement);
                                    ft.commit();
                                }
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

            }
        });

        /*btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Do you really want to delete this record?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // FIRE ZE MISSILES!
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
            }
        });*/

        getActivity().setTitle("Manage Lender");
        return  view;
    }

    private boolean validate() {
        if (txtLenderName.getText().toString().trim().isEmpty()) {
            inputLenderName.setError("Enter lender name");
            requestFocus(inputLenderName);
            return false;
        } else {
            inputLenderName.setErrorEnabled(false);
        }

        if (txtLenderAddress.getText().toString().trim().isEmpty()) {
            inputLenderAddress.setError("Enter lender address");
            requestFocus(inputLenderAddress);
            return false;
        } else {
            inputLenderAddress.setErrorEnabled(false);
        }

        if (txtLenderMobileNo.getText().toString().trim().isEmpty()) {
            inputLenderMobileNo.setError("Enter lender mobile no");
            requestFocus(inputLenderMobileNo);
            return false;
        } else {
            inputLenderMobileNo.setErrorEnabled(false);
        }

        return true;
    }


    private void requestFocus(View view) {
        if (view.requestFocus()) {
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private class LenderFormTextWatcher implements TextWatcher{

        private View view;

        private LenderFormTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            validate();
        }
    }
}


