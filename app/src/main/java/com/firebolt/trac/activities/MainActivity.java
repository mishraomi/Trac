package com.firebolt.trac.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
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

import com.amulyakhare.textdrawable.TextDrawable;
import com.firebolt.trac.R;

import com.firebolt.trac.adapters.Landing_List_Adapter;
import com.firebolt.trac.models.List;
import com.firebolt.trac.utilities.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Activity activity;
    RecyclerView landing_recyclerview;
    FloatingActionButton fab_add_list;
    Landing_List_Adapter landing_adapter;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;

    Dialog add_list_dialog;
    String current_user, current_user_uid;
    ArrayList<List> landing_list_arraylist;
    String list_Type;
    String new_list_name;
    final ArrayList<String> list_id_collection = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity = MainActivity.this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        setSupportActionBar(toolbar);

        landing_recyclerview = (RecyclerView) findViewById(R.id.landing_recyclerview);
        landing_list_arraylist = new ArrayList<>();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Intent intent = new Intent(MainActivity.this, SignInActivity.class);
            startActivity(intent);
            finish();
        } else {
            current_user = user.getDisplayName();
            current_user_uid = user.getUid();
            Constants.UID = user.getUid();

            getLanding_List();
        }

        fab_add_list = (FloatingActionButton) findViewById(R.id.fab_add_list);
        landing_adapter = new Landing_List_Adapter(landing_list_arraylist, activity);
        landing_recyclerview.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        landing_recyclerview.setLayoutManager(llm);
        landing_recyclerview.setAdapter(landing_adapter);

        fab_add_list.setOnClickListener(this);

    } //end of onCreate()

    public void getLanding_List(){
        FirebaseDatabase.getInstance().getReference("users")
                .child(current_user_uid)
                .child("my_list")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() == null){
                            list_id_collection.clear();
                            landing_list_arraylist.clear();
                            landing_adapter.notifyDataSetChanged();
                        }
                        else {
                            System.out.println("Duplicate my_list *** "+dataSnapshot);
                            list_id_collection.clear();
                            for (DataSnapshot list_snapshot : dataSnapshot.getChildren()) {
                                list_id_collection.add(list_snapshot.getKey());
                            }
                            if (!list_id_collection.isEmpty()){
                                HashSet<String> id_collection_set = new HashSet<>();
                                id_collection_set.addAll(list_id_collection);
                                list_id_collection.clear();
                                list_id_collection.addAll(id_collection_set);

                                get_Landing_List_Info(list_id_collection);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    public void get_Landing_List_Info(ArrayList<String> list_ids){
        landing_list_arraylist.clear();
        for (int i=0; i<list_ids.size(); i++){
            FirebaseDatabase.getInstance()
                    .getReference("all_list")
                    .child(list_ids.get(i))
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            System.out.println("dataSnapshot *** " + dataSnapshot);
                            if (dataSnapshot.getValue() != null){
                                List list_for_adapter = new List(
                                        dataSnapshot.child("info").child("list_name").getValue().toString(),
                                        Integer.parseInt(dataSnapshot.child("info").child("list_item_count").getValue().toString()),
                                        dataSnapshot.child("info").child("list_created_by").getValue().toString(),
                                        dataSnapshot.child("info").child("list_owner_id").getValue().toString(),
                                        dataSnapshot.child("info").child("list_creation_date").getValue().toString(),
                                        dataSnapshot.child("info").child("list_updated_on").getValue().toString(),
                                        dataSnapshot.child("info").child("list_type").getValue().toString(),
                                        dataSnapshot.child("info").child("list_id").getValue().toString()
                                );
                                landing_list_arraylist.add(list_for_adapter);
                                landing_adapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            current_user = user.getDisplayName();
            current_user_uid = user.getUid();
        }
    }

    @Override
    public void onClick(View view) {
        int view_id = view.getId();
        switch (view_id) {
            case R.id.fab_add_list:
                show_add_list_dialog();
                break;
        }
    }

    public void show_add_list_dialog() {
        list_Type = "private";
        add_list_dialog = new Dialog(MainActivity.this);
        add_list_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        add_list_dialog.setContentView(R.layout.dialog_add_list);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(add_list_dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        add_list_dialog.show();
        add_list_dialog.getWindow().setAttributes(lp);

        final TextDrawable selected_button_drawable = TextDrawable.builder()
                .buildRect("",
                        ContextCompat.getColor(activity, R.color.highPriority));

        final TextDrawable normal_button_drawable = TextDrawable.builder()
                .buildRect("", Color.WHITE);

        Button dialog_add_list_ok, dialog_add_list_cancel;
        final MaterialEditText dialog_edittext_listname;
        final Button button_private_list, button_shared_list;
        dialog_edittext_listname = (MaterialEditText) add_list_dialog.findViewById(R.id.dialog_edittext_listname);
        dialog_add_list_ok = (Button) add_list_dialog.findViewById(R.id.dialog_add_list_ok);
        dialog_add_list_cancel = (Button) add_list_dialog.findViewById(R.id.dialog_add_list_cancel);
        button_private_list = (Button) add_list_dialog.findViewById(R.id.button_private_list);
        button_shared_list = (Button) add_list_dialog.findViewById(R.id.button_shared_list);

        button_private_list.setBackground(selected_button_drawable);
        button_shared_list.setBackground(normal_button_drawable);

        View.OnClickListener private_shared_button_listner = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.button_private_list:
                        button_private_list.setBackground(selected_button_drawable);
                        button_shared_list.setBackground(normal_button_drawable);
                        list_Type = "private";
                        break;

                    case R.id.button_shared_list:
                        button_private_list.setBackground(normal_button_drawable);
                        button_shared_list.setBackground(selected_button_drawable);
                        list_Type = "shared";
                        break;
                }
            }
        };

        button_private_list.setOnClickListener(private_shared_button_listner);
        button_shared_list.setOnClickListener(private_shared_button_listner);

        View.OnClickListener dialog_button_listner = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.dialog_add_list_ok:
                        if (dialog_edittext_listname.getText().length() > 0) {
                            new_list_name = dialog_edittext_listname.getText().toString();
                            FirebaseDatabase.getInstance().getReference("users")
                                    .child(current_user_uid)
                                    .child("my_list")
                                    .push()
                                    .child("list_name")
                                    .setValue(dialog_edittext_listname.getText().toString(), new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                            final List list = new List(dialog_edittext_listname.getText().toString(),
                                                    0,
                                                    current_user,
                                                    Constants.UID,
                                                    String.valueOf(System.currentTimeMillis()),
                                                    "Not yet",
                                                    list_Type,
                                                    databaseReference.getParent().getKey());
                                            FirebaseDatabase.getInstance()
                                                    .getReference("all_list")
                                                    .child(databaseReference.getParent().getKey())
                                                    .child("info")
                                                    .setValue(list);
                                            Snackbar.make(activity.findViewById(android.R.id.content),
                                                    dialog_edittext_listname.getText().toString() + " created",
                                                    Snackbar.LENGTH_SHORT)
                                                    .show();
                                            add_list_dialog.dismiss();
                                        }
                                    });
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
