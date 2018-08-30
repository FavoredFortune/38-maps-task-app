package com.android.sooz.errandrunner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;

public class ErrandListActivity extends AppCompatActivity
        implements ValueEventListener {

    @BindView(R.id.errands)
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    ErrandAdapter errandAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_errand_list);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
