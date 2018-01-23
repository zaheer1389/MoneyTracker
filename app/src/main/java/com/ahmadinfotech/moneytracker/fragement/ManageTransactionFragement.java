package com.ahmadinfotech.moneytracker.fragement;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.ahmadinfotech.moneytracker.R;
import com.ahmadinfotech.moneytracker.enums.Merchante;
import com.ahmadinfotech.moneytracker.enums.PaymentMode;
import com.ahmadinfotech.moneytracker.enums.TransactionCategory;
import com.ahmadinfotech.moneytracker.enums.TransactionType;
import com.ahmadinfotech.moneytracker.helper.DBHelper;
import com.ahmadinfotech.moneytracker.helper.Helper;
import com.ahmadinfotech.moneytracker.model.Lender;
import com.ahmadinfotech.moneytracker.model.Transaction;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.apache.commons.lang3.EnumUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.R.attr.mode;
import static com.ahmadinfotech.moneytracker.R.id.inputLenderAddress;
import static com.ahmadinfotech.moneytracker.R.id.inputLenderMobileNo;
import static com.ahmadinfotech.moneytracker.R.id.inputLenderName;
import static com.ahmadinfotech.moneytracker.R.id.txtLenderAddress;
import static com.ahmadinfotech.moneytracker.R.id.txtLenderMobileNo;
import static com.ahmadinfotech.moneytracker.R.id.txtLenderName;
import static com.ahmadinfotech.moneytracker.R.id.txtTransactionType;

/**
 * Created by root on 15/1/18.
 */

public class ManageTransactionFragement extends Fragment{

    DBHelper dbHelper;

    MaterialBetterSpinner spinnerLender,spinnerType,spinnerPaymentMode,spinnerTransactionCategory,spinnerMerchant;
    EditText txtTransactionDate;
    EditText txtAmount;
    EditText txtDesc;
    EditText txtMerchantAccount;
    EditText txtCashbackAmount;
    EditText txtPaymentAccount;
    CheckBox cbCashbackEarned;

    private Window window;
    private FragmentActivity myContext;

    private Calendar myCalendar;

    Transaction transaction;
    Lender lender;

    @Override
    public void onAttach(Activity activity) {
        myContext = (FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        dbHelper = DBHelper.getInstance();
        getActivity().setTitle("Manage Transaction");
        window = getActivity().getWindow();

        String id = getArguments().getString("id");
        String action = getArguments().getString("action");
        final Transaction transaction = dbHelper.getTransactionById(id);

        View view = inflater.inflate(R.layout.add_edit_delete_transaction, container, false);
        spinnerLender = (MaterialBetterSpinner) view.findViewById(R.id.spinnerLenderId);
        txtAmount = (EditText) view.findViewById(R.id.txtAmount);
        txtDesc = (EditText) view.findViewById(R.id.txtDesc);
        txtTransactionDate = (EditText) view.findViewById(R.id.txtTransactionDate);
        txtPaymentAccount = (EditText) view.findViewById(R.id.txtPaymentAccount);
        txtMerchantAccount = (EditText) view.findViewById(R.id.txtMerchantAccount);
        spinnerType = (MaterialBetterSpinner) view.findViewById(R.id.spinnerTransactionType);
        spinnerPaymentMode = (MaterialBetterSpinner) view.findViewById(R.id.spinnerPaymentMode);
        spinnerTransactionCategory = (MaterialBetterSpinner) view.findViewById(R.id.spinnerTransactionCategory);
        spinnerMerchant = (MaterialBetterSpinner) view.findViewById(R.id.spinnerMerchant);
        txtCashbackAmount = (EditText) view.findViewById(R.id.txtCashbackAmount);
        cbCashbackEarned = (CheckBox) view.findViewById(R.id.cbCashbackEarned);

        Button btnAddEdit = (Button) view.findViewById(R.id.btnSave);

        ArrayAdapter<Lender> adapter = new ArrayAdapter<Lender>(getActivity(), android.R.layout.simple_dropdown_item_1line, dbHelper.getLenders());
        //adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerLender.setAdapter(adapter);

        List<String> transactionTypes = Arrays.asList(TransactionType.CREDIT.toString(), TransactionType.DEBIT.toString());
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, transactionTypes);
        //adapter2.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerType.setAdapter(adapter2);

        List<String> paymentModes = new ArrayList<>();
        for(PaymentMode mode : PaymentMode.values()){
            paymentModes.add(mode.toString());
        }
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, paymentModes);
        //adapter3.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerPaymentMode.setAdapter(adapter3);

        List<String> transactionCategories = new ArrayList<>();
        for(TransactionCategory transactionCategory : TransactionCategory.values()){
            transactionCategories.add(transactionCategory.toString());
        }
        ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, transactionCategories);
        //adapter4.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerTransactionCategory.setAdapter(adapter4);

        List<String> merchants = new ArrayList<>();
        for(Merchante merchante : Merchante.values()){
            merchants.add(merchante.toString());
        }
        ArrayAdapter<String> adapter5 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, merchants);
        //adapter5.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerMerchant.setAdapter(adapter5);

        myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "dd/MM/yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                txtTransactionDate.setText(sdf.format(myCalendar.getTime()));
            }

        };

        txtTransactionDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        spinnerLender.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d(ManageTransactionFragement.class.getSimpleName(), "OnItemClickListener");
                Log.d(ManageTransactionFragement.class.getSimpleName(), "Selected ITem : "+i);
                Log.d(ManageTransactionFragement.class.getSimpleName(), "Selected ITem long : "+l);
                lender = dbHelper.getLenders().get(i);
                Log.d(ManageTransactionFragement.class.getSimpleName(), "Selected ITem : "+lender.toString());
            }
        });

        if(transaction != null){
            btnAddEdit.setText("Update");
        }

        if(action.equals("view")){
            btnAddEdit.setVisibility(View.INVISIBLE);
        }

        if(transaction != null){
            Lender lender = dbHelper.getLenderById(transaction.getLenderId());
            spinnerLender.setText(lender.getName());
            txtAmount.setText(transaction.getAmount()+"");
            txtTransactionDate.setText(new SimpleDateFormat("dd/MM/yyyy").format(transaction.getTransactionDate()));
            txtDesc.setText(transaction.getDescription());
            spinnerType.setText(transaction.getTransactionType());
            spinnerPaymentMode.setText(transaction.getPaymentMode());
            txtPaymentAccount.setText(transaction.getPaymentAccount());
            spinnerTransactionCategory.setText(transaction.getTransactionCategory());
            spinnerMerchant.setText(transaction.getMerchant());
            txtMerchantAccount.setText(transaction.getMerchantAccount());
            cbCashbackEarned.setSelected(transaction.isCashbackEarned());
            txtCashbackAmount.setText(transaction.getCashbackAmount()+"");
        }


        btnAddEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Do you really want to save this record?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // FIRE ZE MISSILES!
                                try{
                                    if(validate()){
                                        if(transaction == null){
                                            Transaction transaction = new Transaction();
                                            transaction.setLenderId(lender.getId());
                                            transaction.setAmount(Double.parseDouble(txtAmount.getText().toString()));
                                            transaction.setTransactionDate(new SimpleDateFormat("dd/MM/yyyy").parse(txtTransactionDate.getText().toString()));
                                            transaction.setDescription(txtDesc.getText().toString());
                                            transaction.setTransactionType(spinnerType.getText().toString());
                                            transaction.setPaymentMode(spinnerPaymentMode.getText().toString());
                                            transaction.setPaymentAccount(txtPaymentAccount.getText().toString());
                                            transaction.setTransactionCategory(spinnerTransactionCategory.getText().toString());
                                            transaction.setMerchant(spinnerMerchant.getText().toString());
                                            transaction.setMerchantAccount(txtMerchantAccount.getText().toString());
                                            transaction.setCashbackEarned(cbCashbackEarned.isSelected());
                                            if(cbCashbackEarned.isSelected()){
                                                transaction.setCashbackAmount(Double.parseDouble(txtCashbackAmount.getText().toString()));
                                            }
                                            dbHelper.saveTransaction(transaction);
                                        }
                                        else{
                                            transaction.setLenderId(lender.getId());
                                            transaction.setAmount(Double.parseDouble(txtAmount.getText().toString()));
                                            transaction.setTransactionDate(new SimpleDateFormat("dd/MM/yyyy").parse(txtTransactionDate.getText().toString()));
                                            transaction.setDescription(txtDesc.getText().toString());
                                            transaction.setTransactionType(spinnerType.getText().toString());
                                            transaction.setPaymentMode(spinnerPaymentMode.getText().toString());
                                            transaction.setPaymentAccount(txtPaymentAccount.getText().toString());
                                            transaction.setTransactionCategory(spinnerTransactionCategory.getText().toString());
                                            transaction.setMerchant(spinnerMerchant.getText().toString());
                                            transaction.setMerchantAccount(txtMerchantAccount.getText().toString());
                                            transaction.setCashbackEarned(cbCashbackEarned.isSelected());
                                            if(cbCashbackEarned.isSelected()){
                                                transaction.setCashbackAmount(Double.parseDouble(txtCashbackAmount.getText().toString()));
                                            }
                                            dbHelper.saveTransaction(transaction);
                                        }

                                        LenderFragement fragement = new LenderFragement();
                                        FragmentTransaction ft = myContext.getSupportFragmentManager().beginTransaction();
                                        ft.replace(R.id.content_frame, fragement);
                                        ft.commit();
                                    }
                                }
                                catch (Exception e){
                                    e.printStackTrace();
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

        return  view;
    }

    public boolean validate(){
        if(spinnerLender.getText().length() == 0){
            Helper.displayMessageToast(getContext(), "Please selec lender");
            return false;
        }
        else if(txtAmount.getText().length() == 0){
            Helper.displayMessageToast(getContext(), "Please enter transaction amount");
            return  false;
        }
        else if(txtTransactionDate.getText().length() == 0){
            Helper.displayMessageToast(getContext(), "Please select transaction date");
            return false;
        }
        else if(txtDesc.getText().length() == 0){
            Helper.displayMessageToast(getContext(), "Please enter transaction description");
            return false;
        }
        else if(spinnerType.getText().length() == 0){
            Helper.displayMessageToast(getContext(), "Please select transaction type");
            return false;
        }
        return  true;
    }
}


