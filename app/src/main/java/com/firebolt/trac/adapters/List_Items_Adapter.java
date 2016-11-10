package com.firebolt.trac.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.http.SslCertificate;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.firebolt.trac.R;
import com.firebolt.trac.activities.ListItemActivity;
import com.firebolt.trac.models.List_Item;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Firebolt-Mesh on 9/9/2016.
 */
public class List_Items_Adapter extends RecyclerView.Adapter<List_Items_Adapter.ListItemViewHolder> {

    ArrayList<List_Item> list_item_arraylist = new ArrayList<>();
    Activity activity;
    String list_id;
    int[] color = {
            R.color.highPriority,
            R.color.midPriority,
            R.color.lowPriority
    };

    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();

    public List_Items_Adapter(Activity activity, ArrayList<List_Item> list_item_arraylist, String list_id) {
        this.activity = activity;
        this.list_item_arraylist = list_item_arraylist;
        this.list_id = list_id;
        viewBinderHelper.setOpenOnlyOne(true);
        System.out.println("itemArraylist "+list_item_arraylist);
    }

    @Override
    public ListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_layout, parent, false);
        ListItemViewHolder llv = new ListItemViewHolder(v);
        return llv;
    }

    @Override
    public void onBindViewHolder(final ListItemViewHolder holder, final int position) {
        viewBinderHelper.bind(holder.swipe_reveal_layout, list_item_arraylist.get(position).getItem_name());

        if (list_item_arraylist.get(position).getItem_purchased().equalsIgnoreCase("true")){
            holder.item_purchased.setVisibility(View.VISIBLE);
        }

        TextDrawable item_count_drawable = TextDrawable.builder()
                .buildRect(String.valueOf(list_item_arraylist.get(position).getItem_quantity()),
                        Color.LTGRAY);

        TextDrawable item_unit_drawable = TextDrawable.builder()
                .beginConfig()
                .textColor(Color.WHITE)
                .useFont(Typeface.DEFAULT)
                .fontSize(25) /* size in px */
                .bold()
                .toUpperCase()
                .endConfig()
                .buildRect(list_item_arraylist.get(position).getItem_measure_type(),
                        ContextCompat.getColor(activity, color[list_item_arraylist.get(position).getItem_priority() - 1]));
        System.out.println("Priority : "+list_item_arraylist.get(position).getItem_priority());
        System.out.println("Priority color : "+ color[list_item_arraylist.get(position).getItem_priority() - 1]);

        holder.imageview_item_unit_type.setImageDrawable(item_unit_drawable);
        holder.imageview_item_quantity.setImageDrawable(item_count_drawable);
        holder.textview_item_name.setText(list_item_arraylist.get(position).getItem_name());

        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy hh:mm a", Locale.ENGLISH);
        String date = formatter.format(new Date(Long.parseLong(list_item_arraylist.get(position).getItem_added_timestamp())));

        holder.textview_item_added_on.setText(date);

        holder.cardview_delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance()
                        .getReference("all_list")
                        .child(list_id)
                        .child("items")
                        .child(list_item_arraylist.get(position).getItem_name())
                        .removeValue(new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                Snackbar.make(activity.findViewById(android.R.id.content),
                                        list_item_arraylist.get(position).getItem_name() + " deleted from the list",
                                        Snackbar.LENGTH_SHORT)
                                        .show();
                            }
                        });
            }
        });

        holder.cardview_item.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                FirebaseDatabase.getInstance()
                        .getReference("all_list")
                        .child(list_id)
                        .child("items")
                        .child(list_item_arraylist.get(position).getItem_name())
                        .child("item_purchased")
                        .setValue("true", new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                holder.item_purchased.setVisibility(View.VISIBLE);
                                Snackbar.make(activity.findViewById(android.R.id.content),
                                        list_item_arraylist.get(position).getItem_name() + " purchased",
                                        Snackbar.LENGTH_SHORT)
                                        .show();
                            }
                        });
                return false;
            }
        });

        holder.item_purchased.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog undo_purchase_dialog = new Dialog(activity);
                undo_purchase_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                undo_purchase_dialog.setContentView(R.layout.undo_purchase_dialog);
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(undo_purchase_dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                undo_purchase_dialog.show();
                lp.gravity = Gravity.BOTTOM;
                undo_purchase_dialog.getWindow().setAttributes(lp);

                Button purchase_dialog_cancel, purchase_dialog_undo;
                purchase_dialog_cancel = (Button) undo_purchase_dialog.findViewById(R.id.purchase_dialog_cancel);
                purchase_dialog_undo = (Button) undo_purchase_dialog.findViewById(R.id.purchase_dialog_undo);

                purchase_dialog_undo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FirebaseDatabase.getInstance()
                                .getReference("all_list")
                                .child(list_id)
                                .child("items")
                                .child(list_item_arraylist.get(position).getItem_name())
                                .child("item_purchased")
                                .setValue("false", new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                        undo_purchase_dialog.dismiss();
                                        holder.item_purchased.setVisibility(View.GONE);
                                        Snackbar.make(activity.findViewById(android.R.id.content),
                                                list_item_arraylist.get(position).getItem_name() + " purchase reverted!",
                                                Snackbar.LENGTH_SHORT)
                                                .show();
                                    }
                                });
                    }
                });

                purchase_dialog_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        undo_purchase_dialog.dismiss();
                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return list_item_arraylist.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public class ListItemViewHolder extends RecyclerView.ViewHolder{

        CardView cardview_item;
        TextView textview_item_name;
        TextView textview_item_added_on;
        ImageView imageview_item_quantity, imageview_item_unit_type;
        Button cardview_delete_button;
        SwipeRevealLayout swipe_reveal_layout;
        LinearLayout item_purchased;


        public ListItemViewHolder(View itemView) {
            super(itemView);

            swipe_reveal_layout = (SwipeRevealLayout) itemView.findViewById(R.id.swipe_reveal_layout);
            cardview_item = (CardView) itemView.findViewById(R.id.cardview_item);
            textview_item_name = (TextView) itemView.findViewById(R.id.textview_item_name);
            textview_item_added_on = (TextView) itemView.findViewById(R.id.textview_item_added_on);
            imageview_item_quantity = (ImageView) itemView.findViewById(R.id.imageview_item_quantity);
            imageview_item_unit_type = (ImageView) itemView.findViewById(R.id.imageview_item_unit_type);
            cardview_delete_button = (Button) itemView.findViewById(R.id.cardview_delete_button);
            item_purchased = (LinearLayout) itemView.findViewById(R.id.item_purchased);
        }
    }
}
