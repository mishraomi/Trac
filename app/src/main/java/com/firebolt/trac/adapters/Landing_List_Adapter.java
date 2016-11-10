package com.firebolt.trac.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.provider.ContactsContract;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.firebolt.trac.R;
import com.firebolt.trac.activities.ListItemActivity;
import com.firebolt.trac.models.List;
import com.firebolt.trac.models.User;
import com.firebolt.trac.utilities.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Firebolt-Mesh on 8/22/2016.
 */
public class Landing_List_Adapter extends RecyclerView.Adapter<Landing_List_Adapter.LandingListViewHolder> {

    ArrayList<List> landing_list_arraylist = new ArrayList<>();
    Activity activity;
    Dialog add_contributor_dialog;

    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();

    public Landing_List_Adapter(ArrayList<List> landing_list_arraylist, Activity activity) {
        this.landing_list_arraylist = landing_list_arraylist;
        this.activity = activity;
        viewBinderHelper.setOpenOnlyOne(true);
    }

    @Override
    public Landing_List_Adapter.LandingListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_list, parent, false);
        LandingListViewHolder llv = new LandingListViewHolder(v);
        return llv;
    }

    @Override
    public void onBindViewHolder(final Landing_List_Adapter.LandingListViewHolder holder, final int position) {
        System.out.println("Position : "+position);

        TextDrawable item_count_drawable = TextDrawable.builder()
                .buildRect(String.valueOf(landing_list_arraylist.get(position).getList_item_count()),
                        ContextCompat.getColor(activity, R.color.highPriority));
        holder.imageview_list_item_count.setImageDrawable(item_count_drawable);
        holder.textview_listname.setText(landing_list_arraylist.get(position).getList_name());
        holder.textview_list_created_by.setText(landing_list_arraylist.get(position).getList_created_by());

        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy hh:mm a", Locale.ENGLISH);
        String date = formatter.format(new Date(Long.parseLong(landing_list_arraylist.get(position).getList_creation_date())));
        holder.textview_listdate.setText(date);

        holder.cardview_delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String list_id = landing_list_arraylist.get(position).getList_id();

                FirebaseDatabase.getInstance()
                        .getReference("users")
                        .child(Constants.UID)
                        .child("my_list")
                        .child(list_id)
                        .removeValue(new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                FirebaseDatabase.getInstance()
                                        .getReference("all_list")
                                        .child(list_id)
                                        .removeValue();
                            }
                        });
            }
        });

        holder.cardview_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, ListItemActivity.class);
                intent.putExtra("list_name", landing_list_arraylist.get(position).getList_name());
                intent.putExtra("list_id", landing_list_arraylist.get(position).getList_id());
                activity.startActivity(intent);
                activity.finish();
            }
        });

        TextDrawable add_contributor_background = TextDrawable.builder()
                .buildRound("",
                        ContextCompat.getColor(activity, R.color.highPriority));
        holder.add_contributor_layout.setBackground(add_contributor_background);
        holder.imagebutton_add_contributor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show_add_contributor_dialog(position);
            }
        });

        viewBinderHelper.bind(holder.swipe_reveal_layout, landing_list_arraylist.get(position).getList_name());

        holder.more_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.contributors_hidden_layout.getVisibility() == View.GONE){
                    holder.contributors_hidden_layout.setVisibility(View.VISIBLE);
                    holder.imagebutton_more_less.setBackgroundResource(R.drawable.ic_keyboard_arrow_up_white_24dp);
                    holder.textview_more_less.setText("less");
                }
                else {
                    holder.contributors_hidden_layout.setVisibility(View.GONE);
                    holder.imagebutton_more_less.setBackgroundResource(R.drawable.ic_keyboard_arrow_down_white_24dp);
                    holder.textview_more_less.setText("more");
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return landing_list_arraylist.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class LandingListViewHolder extends RecyclerView.ViewHolder{

        CardView cardview_list;
        Button cardview_delete_button;
        TextView textview_listname, textview_list_created_by, textview_listdate, textview_more_less;
        SwipeRevealLayout swipe_reveal_layout;
        ImageView imageview_list_item_count;
        ImageButton imagebutton_add_contributor, imagebutton_more_less;
        LinearLayout more_layout, add_contributor_layout, contributors_hidden_layout;



        public LandingListViewHolder(View itemView) {
            super(itemView);
            swipe_reveal_layout = (SwipeRevealLayout) itemView.findViewById(R.id.swipe_reveal_layout);
            cardview_list = (CardView) itemView.findViewById(R.id.cardview_list);
            cardview_delete_button = (Button) itemView.findViewById(R.id.cardview_delete_button);
            textview_listname = (TextView) itemView.findViewById(R.id.textview_listname);
            textview_list_created_by = (TextView) itemView.findViewById(R.id.textview_list_created_by);
            textview_listdate = (TextView) itemView.findViewById(R.id.textview_listdate);
            imageview_list_item_count = (ImageView) itemView.findViewById(R.id.imageview_list_item_count);
            imagebutton_add_contributor = (ImageButton) itemView.findViewById(R.id.imagebutton_add_contributor);
            more_layout = (LinearLayout) itemView.findViewById(R.id.more_layout);
            add_contributor_layout = (LinearLayout) itemView.findViewById(R.id.add_contributor_layout);
            contributors_hidden_layout = (LinearLayout) itemView.findViewById(R.id.contributors_hidden_layout);
            imagebutton_more_less = (ImageButton) itemView.findViewById(R.id.imagebutton_more_less);
            textview_more_less = (TextView) itemView.findViewById(R.id.textview_more_less);
        }
    }

    public void show_add_contributor_dialog(final int position){

        add_contributor_dialog = new Dialog(activity);
        add_contributor_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        add_contributor_dialog.setContentView(R.layout.dialog_add_contributor);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(add_contributor_dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        add_contributor_dialog.show();
        add_contributor_dialog.getWindow().setAttributes(lp);

        Button dialog_add_contributor_ok, dialog_add_contributor_cancel;
        final MaterialEditText dialog_edittext_contributor_email;
        dialog_edittext_contributor_email = (MaterialEditText) add_contributor_dialog.findViewById(R.id.dialog_edittext_contributor_email);
        dialog_add_contributor_ok = (Button) add_contributor_dialog.findViewById(R.id.dialog_add_contributor_ok);
        dialog_add_contributor_cancel = (Button) add_contributor_dialog.findViewById(R.id.dialog_add_contributor_cancel);

        View.OnClickListener dialog_button_listner = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.dialog_add_contributor_ok:

                        FirebaseDatabase.getInstance()
                                .getReference("users")
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for (DataSnapshot user_info : dataSnapshot.getChildren()){
                                            if (user_info.child("info").child("email").getValue().toString()
                                                    .equalsIgnoreCase(dialog_edittext_contributor_email
                                                    .getText().toString())){
                                                FirebaseDatabase.getInstance()
                                                        .getReference("users")
                                                        .child(user_info.getKey())
                                                        .child("my_list")
                                                        .child(landing_list_arraylist.get(position).getList_id())
                                                        .setValue(landing_list_arraylist.get(position).getList_name(),
                                                                new DatabaseReference.CompletionListener() {
                                                                    @Override
                                                                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                                                        add_contributor_dialog.dismiss();
                                                                    }
                                                                });

                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                        break;

                    case R.id.dialog_add_contributor_cancel:
                        Snackbar.make(activity.findViewById(android.R.id.content), "Dialog Cancel button clicked", Snackbar.LENGTH_SHORT)
                                .show();
                        add_contributor_dialog.dismiss();
                        break;
                }
            }
        };

        dialog_add_contributor_ok.setOnClickListener(dialog_button_listner);
        dialog_add_contributor_cancel.setOnClickListener(dialog_button_listner);
    }

    public Map<String, Object> getUserDetails(final String user_email){
       final Map<String, Object> user_detail = new HashMap<>();
        FirebaseDatabase.getInstance()
                .getReference("users")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot user_info : dataSnapshot.getChildren()){
                            if (user_info.child("info").child("email").getValue().toString().equals(user_email)){
                                User user = new User(user_info.child("info").child("displayName").getValue().toString(),
                                        user_info.child("info").child("email").getValue().toString(),
                                        user_info.child("info").child("imageUrl").getValue().toString());
                                user_detail.put(user_info.getKey(), user);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        System.out.println("user_detail "+user_detail);
        return user_detail;
    }

}
