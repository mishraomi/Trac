package com.firebolt.trac.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.firebolt.trac.R;
import com.firebolt.trac.adapters.List_Items_Adapter;
import com.firebolt.trac.models.List_Item;
import com.firebolt.trac.utilities.Constants;
import com.firebolt.trac.utilities.SortArraylist;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;


public class ListItemActivity extends AppCompatActivity {

    Activity activity;
    FloatingActionButton fab_add_item;
    Dialog add_list_item_dialog;
    public static String list_name;
    private String list_id;
    RecyclerView list_item_recyclerview;
    List_Items_Adapter list_items_adapter;
    ArrayList<List_Item> list_item_arraylist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_item);

        Intent intent = getIntent();
        list_name = intent.getStringExtra("list_name");
        list_id = intent.getStringExtra("list_id");

        activity = ListItemActivity.this;
        fab_add_item = (FloatingActionButton) findViewById(R.id.fab_add_list_item);
        list_item_recyclerview = (RecyclerView) findViewById(R.id.list_item_recyclerview);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(intent.getStringExtra("list_name"));
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        setSupportActionBar(toolbar);

        Collections.sort(list_item_arraylist, new SortArraylist());
        System.out.println("Sorted Arraylist "+list_item_arraylist);

        list_items_adapter = new List_Items_Adapter(ListItemActivity.this, list_item_arraylist);
        list_item_recyclerview.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        list_item_recyclerview.setLayoutManager(llm);
        System.out.println("list_item_arraylist "+list_item_arraylist);
        list_item_recyclerview.setAdapter(list_items_adapter);

        FirebaseDatabase.getInstance()
                .getReference("all_list")
                .child(list_id)
                .child("items")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        System.out.println("dataSnapshot "+dataSnapshot);
                        list_item_arraylist.clear();
                        for (DataSnapshot list_item : dataSnapshot.getChildren()){
                            list_item_arraylist.add(
                                    new List_Item(list_item.getKey(),
                                            list_item.child("item_quantity").getValue().toString(),
                                            list_item.child("item_measure_type").getValue().toString(),
                                            Integer.parseInt(list_item.child("item_priority").getValue().toString()),
                                            list_item.child("item_added_timestamp").getValue().toString()));
                            list_items_adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        fab_add_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show_add_list_dialog();
            }
        });


        FirebaseDatabase.getInstance().getReference("all_list")
                .child(list_id)
                .child("items")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        FirebaseDatabase.getInstance().getReference("all_list")
                                .child(list_id)
                                .child("info")
                                .child("list_item_count")
                                .setValue(dataSnapshot.getChildrenCount());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }// end of onCreate()



    public void show_add_list_dialog(){
        add_list_item_dialog = new Dialog(activity);
        add_list_item_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        add_list_item_dialog.setContentView(R.layout.dialog_add_list_item);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(add_list_item_dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        add_list_item_dialog.show();
        add_list_item_dialog.getWindow().setAttributes(lp);

        Button dialog_add_item_ok, dialog_add_item_cancel;
        final Spinner dialog_spinner_quantity_type, dialog_spinner_priority;
        final MaterialEditText dialog_edittext_item_name, dialog_edittext_quantity;
        dialog_edittext_item_name = (MaterialEditText) add_list_item_dialog.findViewById(R.id.dialog_edittext_item_name);
        dialog_edittext_quantity = (MaterialEditText) add_list_item_dialog.findViewById(R.id.dialog_edittext_quantity);
        dialog_add_item_ok = (Button) add_list_item_dialog.findViewById(R.id.dialog_add_item_ok);
        dialog_add_item_cancel = (Button) add_list_item_dialog.findViewById(R.id.dialog_add_item_cancel);
        dialog_spinner_quantity_type = (Spinner) add_list_item_dialog.findViewById(R.id.dialog_spinner_quantity_type);
        dialog_spinner_priority = (Spinner) add_list_item_dialog.findViewById(R.id.dialog_spinner_priority);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.quantity_type, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        dialog_spinner_quantity_type.setAdapter(adapter);

        ArrayAdapter<CharSequence> adapter_priority = ArrayAdapter.createFromResource(this,
                R.array.priority_type, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter_priority.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        dialog_spinner_priority.setAdapter(adapter_priority);

        View.OnClickListener dialog_button_listner = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int priority;
                switch (view.getId()){
                    case R.id.dialog_add_item_ok:
                        switch (dialog_spinner_priority.getSelectedItem().toString()){
                            case "High":
                                priority = 1;
                                break;
                            case "Mid":
                                priority = 2;
                                break;
                            case "Low":
                                priority = 3;
                                break;
                            default:
                                priority = 1;
                                break;
                        }
                        FirebaseDatabase.getInstance().getReference("all_list")
                                .child(list_id)
                                .child("items")
                                .child(dialog_edittext_item_name.getText().toString())
                                .setValue(new List_Item(dialog_edittext_item_name.getText().toString(),
                                        dialog_edittext_quantity.getText().toString(),
                                        dialog_spinner_quantity_type.getSelectedItem().toString(),
                                        priority,
                                        String.valueOf(System.currentTimeMillis())));

                        add_list_item_dialog.dismiss();
                        Snackbar.make(activity.findViewById(android.R.id.content),
                                dialog_edittext_item_name.getText().toString() + " added to the list",
                                Snackbar.LENGTH_SHORT)
                                .show();
                        break;

                    case R.id.dialog_add_item_cancel:
                        Snackbar.make(activity.findViewById(android.R.id.content), "Dilog Cancel button clicked", Snackbar.LENGTH_SHORT)
                                .show();
                        add_list_item_dialog.dismiss();
                        break;
                }
            }
        };

        dialog_add_item_ok.setOnClickListener(dialog_button_listner);
        dialog_add_item_cancel.setOnClickListener(dialog_button_listner);
    }
}
