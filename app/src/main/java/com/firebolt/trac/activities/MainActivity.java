package com.firebolt.trac.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.firebolt.trac.R;

import com.firebolt.trac.adapters.Landing_List_Adapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Activity activity;
    RecyclerView landing_recyclerview;
    Landing_List_Adapter landing_adapter;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity = MainActivity.this;
        ArrayList<String> landing_list_arraylist = new ArrayList<>();
        landing_list_arraylist.add("List 1");
        landing_list_arraylist.add("List 2");
        landing_list_arraylist.add("List 3");
        landing_list_arraylist.add("List 4");
        landing_list_arraylist.add("List 5");
        landing_recyclerview = (RecyclerView) findViewById(R.id.landing_recyclerview);
        landing_adapter = new Landing_List_Adapter(landing_list_arraylist, activity);
        landing_recyclerview.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        landing_recyclerview.setLayoutManager(llm);
        landing_recyclerview.setAdapter(landing_adapter);


       /* FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null){
            Intent intent = new Intent(MainActivity.this, SignInActivity.class);
            startActivity(intent);
        }*/

    }
}
