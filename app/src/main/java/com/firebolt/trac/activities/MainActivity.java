package com.firebolt.trac.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.firebolt.trac.R;

import com.firebolt.trac.adapters.Landing_List_Adapter;
import com.firebolt.trac.models.List;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Activity activity;
    RecyclerView landing_recyclerview;
    FloatingActionButton fab_add_list;
    Landing_List_Adapter landing_adapter;
    private FirebaseAuth.AuthStateListener mAuthListener;

    Dialog add_list_dialog;
    String current_user, current_user_uid;
    ArrayList<List> landing_list_arraylist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity = MainActivity.this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        setSupportActionBar(toolbar);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null){
            Intent intent = new Intent(MainActivity.this, SignInActivity.class);
            startActivity(intent);
        }
        else {
            current_user = user.getDisplayName();
            current_user_uid = user.getUid();
        }

        landing_recyclerview = (RecyclerView) findViewById(R.id.landing_recyclerview);
        landing_list_arraylist = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("users")
                .child(current_user_uid)
                .child("my_list")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        System.out.println("ListDatasnapshot "+dataSnapshot);
                        landing_list_arraylist.clear();
                        for (DataSnapshot list_snapshot : dataSnapshot.getChildren()){
                            Log.d("List Name", list_snapshot.getKey());
                            Log.d("List Item count", list_snapshot.child("info").child("list_item_count").getValue().toString());
                            Log.d("List Created by", list_snapshot.child("info").child("list_created_by").getValue().toString());
                            Log.d("List Creation date", list_snapshot.child("info").child("list_creation_date").getValue().toString());
                            Log.d("List Updated on", list_snapshot.child("info").child("list_updated_on").getValue().toString());

                            List list_for_adapter = new List(
                              list_snapshot.getKey(),
                                    Integer.parseInt(list_snapshot.child("info").child("list_item_count").getValue().toString()),
                                    list_snapshot.child("info").child("list_created_by").getValue().toString(),
                                    list_snapshot.child("info").child("list_creation_date").getValue().toString(),
                                    list_snapshot.child("info").child("list_updated_on").getValue().toString()
                            );
                            landing_list_arraylist.add(list_for_adapter);
                            landing_adapter.notifyDataSetChanged();
                            if (landing_list_arraylist.size()<1){
                                landing_recyclerview.removeAllViews();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


        fab_add_list = (FloatingActionButton) findViewById(R.id.fab_add_list);
        landing_adapter = new Landing_List_Adapter(landing_list_arraylist, activity);
        landing_recyclerview.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        landing_recyclerview.setLayoutManager(llm);
        landing_recyclerview.setAdapter(landing_adapter);




        fab_add_list.setOnClickListener(this);

    } //end of onCreate()

    @Override
    public void onClick(View view) {
        int view_id = view.getId();
        switch (view_id){
            case R.id.fab_add_list:
                show_add_list_dialog();
                break;
        }
    }

    public void show_add_list_dialog(){
        add_list_dialog = new Dialog(MainActivity.this);
        add_list_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        add_list_dialog.setContentView(R.layout.dialog_add_list);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(add_list_dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        add_list_dialog.show();
        add_list_dialog.getWindow().setAttributes(lp);

        Button dialog_add_list_ok, dialog_add_list_cancel;
        final MaterialEditText dialog_edittext_listname;
        dialog_edittext_listname = (MaterialEditText) add_list_dialog.findViewById(R.id.dialog_edittext_listname);
        dialog_add_list_ok = (Button) add_list_dialog.findViewById(R.id.dialog_add_list_ok);
        dialog_add_list_cancel = (Button) add_list_dialog.findViewById(R.id.dialog_add_list_cancel);

        View.OnClickListener dialog_button_listner = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.dialog_add_list_ok:
                        if (dialog_edittext_listname.getText().length() > 0){
                            List list = new List(dialog_edittext_listname.getText().toString(),
                                    0, current_user, new Date().toString(), "Not yet");
                            FirebaseDatabase.getInstance().getReference("users")
                                    .child(current_user_uid)
                                    .child("my_list")
                                    .child(dialog_edittext_listname.getText().toString())
                                    .child("info")
                                    .setValue(list);
                            Snackbar.make(activity.findViewById(android.R.id.content),
                                    dialog_edittext_listname.getText().toString() + " created",
                                    Snackbar.LENGTH_SHORT)
                                    .show();
                            add_list_dialog.dismiss();
                        }
                        break;

                    case R.id.dialog_add_list_cancel:
                        Snackbar.make(activity.findViewById(android.R.id.content), "Dilog Cancel button clicked", Snackbar.LENGTH_SHORT)
                                .show();
                        add_list_dialog.dismiss();
                        break;
                }
            }
        };

        dialog_add_list_ok.setOnClickListener(dialog_button_listner);
        dialog_add_list_cancel.setOnClickListener(dialog_button_listner);
    }


}
