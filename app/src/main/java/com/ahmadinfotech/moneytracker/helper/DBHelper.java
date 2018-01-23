package com.ahmadinfotech.moneytracker.helper;

/**
 * Created by Zaheer Khorajiya on 15/1/18.
 */

import android.nfc.Tag;
import android.util.Log;

import com.ahmadinfotech.moneytracker.adaptor.LenderAdaptor;
import com.ahmadinfotech.moneytracker.adaptor.TransactionAdaptor;
import com.ahmadinfotech.moneytracker.enums.TransactionType;
import com.ahmadinfotech.moneytracker.listener.OnGetDataListener;
import com.ahmadinfotech.moneytracker.model.Lender;
import com.ahmadinfotech.moneytracker.model.Transaction;
import com.ahmadinfotech.moneytracker.model.User;
import com.ahmadinfotech.moneytracker.util.AppUtil;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.id;

public class DBHelper {

    private static DBHelper instance = null;
    private static final String TAG = DBHelper.class.getSimpleName();
    private DatabaseReference databaseReference_User;
    private DatabaseReference databaseReference_Lender;
    private DatabaseReference databaseReference_Transaction;
    private List<User> users;
    private List<Lender> lenders;
    private List<Transaction> transactions;
    private boolean dataLoaded = false;
    private boolean userDataLoaded = false;
    private User loggedInUser;
    private LenderAdaptor lenderAdaptor;
    private TransactionAdaptor transactionAdaptor;

    private DBHelper(){

        users = new ArrayList<User>();
        databaseReference_User = FirebaseDatabase.getInstance().getReference("moneytracker/user");
        databaseReference_User.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot childDataSnapshot : dataSnapshot.getChildren()){
                    User user = childDataSnapshot.getValue(User.class);
                    users.add(user);
                }

                dataLoaded = true;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public static DBHelper getInstance(){
        if(instance == null){
            instance = new DBHelper();
        }
        return instance;
    }

    public void init(final OnGetDataListener listener){
        listener.onStart();

        lenders = new ArrayList<Lender>();
        transactions = new ArrayList<Transaction>();

        databaseReference_Lender = FirebaseDatabase.getInstance().getReference("moneytracker/"+loggedInUser.getuId()+"/lender");
        databaseReference_Lender.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                lenders.clear();
                if(lenderAdaptor != null){
                    lenderAdaptor.notifyDataSetChanged();
                }

                for(DataSnapshot childDataSnapshot : dataSnapshot.getChildren()){
                    Lender lender = childDataSnapshot.getValue(Lender.class);
                    lenders.add(lender);
                }

                userDataLoaded = true;
                listener.onSuccess(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onFailed(databaseError);
            }
        });
        databaseReference_Lender.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        databaseReference_Transaction = FirebaseDatabase.getInstance().getReference("moneytracker/"+loggedInUser.getuId()+"/transaction");
        databaseReference_Transaction.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                transactions.clear();
                if(transactionAdaptor != null){
                    transactionAdaptor.notifyDataSetChanged();
                }

                for(DataSnapshot childDataSnapshot : dataSnapshot.getChildren()){
                    Transaction transaction = childDataSnapshot.getValue(Transaction.class);
                    transactions.add(transaction);
                }

                userDataLoaded = true;
                listener.onSuccess(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onFailed(databaseError);
            }
        });
        databaseReference_Lender.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public User saveUser(User user){
        if(user.getuId() == null || user.getuId().length() == 0){
            String id = databaseReference_User.push().getKey();
            user.setuId(id);
            databaseReference_User.child(id).setValue(user);
            return user;
        }
        else{
            databaseReference_User.child(user.getuId()).setValue(user);
            return user;
        }
    }

    public User getUser(String email, String password){
        if(!dataLoaded){
            return getUser(email, password);
        }
        for(User user : users){
            Log.d("===============","===============");
            Log.d("", "User Email : "+user.getEmail()+" , provided : "+email);
            Log.d("", "User Hash passsword : "+user.getPassword()+" , provided hash password: "+password);
            Log.d("XXXXXXXXxxx","XXXXXXXXXXXXxxx");
            if(user.getEmail().equals(email)
                    && user.getPassword().equals(password)){
                return  user;
            }
        }
        return null;
    }

    public boolean isUserExist(String email){
        if(!dataLoaded){
            return isUserExist(email);
        }
        for(User user : users){
            if(user.getEmail().equals(email)){
                return  true;
            }
        }
        return false;
    }

    public List<User> getUsers(){
        return users;
    }

    public void saveLender(Lender lender){
        if(lender.getId() == null || lender.getId().length() == 0){
            String id = databaseReference_Lender.push().getKey();
            lender.setId(id);
            databaseReference_Lender.child(id).setValue(lender);
        }
        else{
            databaseReference_Lender.child(lender.getId()).setValue(lender);
        }
    }

    public List<Lender> getLenders(){
        return lenders;
    }

    public Lender getLenderById(String id){
        for(Lender lender : lenders){
            if(lender.getId().equals(id)){
                return  lender;
            }
        }
        return  null;
    }

    public boolean deleteLender(Lender lender){
        databaseReference_Lender.child(lender.getId()).removeValue();
        return true;
    }

    public void saveTransaction(Transaction transaction){
        if(transaction.getId() == null || transaction.getId().length() == 0){
            String id = databaseReference_Transaction.push().getKey();
            transaction.setId(id);
            databaseReference_Transaction.child(id).setValue(transaction);
        }
        else{
            databaseReference_Transaction.child(transaction.getId()).setValue(transaction);
        }
    }

    public List<Transaction> getTransactions(){
        return transactions;
    }

    public List<Transaction> getTransactions(String lenderId){
        List<Transaction> lenderTransactions = new ArrayList<Transaction>();
        for(Transaction transaction : transactions){
            if(transaction.getLenderId().equals(lenderId)){
                lenderTransactions.add(transaction);
            }

        }
        return lenderTransactions;
    }

    public Transaction getTransactionById(String id){
        for(Transaction transaction : transactions){
            if(transaction.getId().equals(id)){
                return transaction;
            }
        }
        return  null;
    }

    public boolean deleteTransaction(Transaction transaction){
        databaseReference_Transaction.child(transaction.getId()).removeValue();
        return true;
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(User loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    public boolean isUserDataLoaded(){
        return userDataLoaded;
    }

    public void calculateBalances(){
        for(Lender lender : lenders){
            double balance = 0;
            for(Transaction transaction : getTransactions(lender.getId())){
                if(transaction.getTransactionType().equals(TransactionType.DEBIT.toString())){
                    balance = balance - transaction.getAmount();
                }
                if(transaction.getTransactionType().equals(TransactionType.CREDIT.toString())){
                    balance = balance + transaction.getAmount();
                }
            }
            lender.setBalance(balance);
        }
    }

    public LenderAdaptor getLenderAdaptor() {
        return lenderAdaptor;
    }

    public void setLenderAdaptor(LenderAdaptor lenderAdaptor) {
        this.lenderAdaptor = lenderAdaptor;
    }

    public TransactionAdaptor getTransactionAdaptor() {
        return transactionAdaptor;
    }

    public void setTransactionAdaptor(TransactionAdaptor transactionAdaptor) {
        this.transactionAdaptor = transactionAdaptor;
    }
}
