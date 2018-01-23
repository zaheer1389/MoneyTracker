package com.ahmadinfotech.moneytracker.listener;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

/**
 * Created by root on 18/1/18.
 */

public interface OnGetDataListener {
    public void onStart();
    public void onSuccess(DataSnapshot data);
    public void onFailed(DatabaseError databaseError);
}
