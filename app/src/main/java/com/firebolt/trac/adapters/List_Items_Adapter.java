package com.firebolt.trac.adapters;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.http.SslCertificate;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.firebolt.trac.R;
import com.firebolt.trac.activities.ListItemActivity;
import com.firebolt.trac.models.List_Item;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by Firebolt-Mesh on 9/9/2016.
 */
public class List_Items_Adapter extends RecyclerView.Adapter<List_Items_Adapter.ListItemViewHolder> {

    ArrayList<List_Item> list_item_arraylist = new ArrayList<>();
    Activity activity;
    int[] color = {
            R.color.highPriority,
            R.color.midPriority,
            R.color.lowPriority
    };

    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();

    public List_Items_Adapter(Activity activity, ArrayList<List_Item> list_item_arraylist) {
        this.activity = activity;
        this.list_item_arraylist = list_item_arraylist;
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
    public void onBindViewHolder(ListItemViewHolder holder, final int position) {
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
        holder.textview_item_added_on.setText(list_item_arraylist.get(position).getItem_added_timestamp());
        viewBinderHelper.bind(holder.swipe_reveal_layout, list_item_arraylist.get(position).getItem_name());
        holder.cardview_delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance()
                        .getReference("all_list")
                        .child(ListItemActivity.list_name)
                        .child("items")
                        .child(list_item_arraylist.get(position).getItem_name())
                        .removeValue();
                Snackbar.make(activity.findViewById(android.R.id.content),
                        list_item_arraylist.get(position).getItem_name() + " deleted from the list",
                        Snackbar.LENGTH_SHORT)
                        .show();
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


        public ListItemViewHolder(View itemView) {
            super(itemView);

            swipe_reveal_layout = (SwipeRevealLayout) itemView.findViewById(R.id.swipe_reveal_layout);
            cardview_item = (CardView) itemView.findViewById(R.id.cardview_item);
            textview_item_name = (TextView) itemView.findViewById(R.id.textview_item_name);
            textview_item_added_on = (TextView) itemView.findViewById(R.id.textview_item_added_on);
            imageview_item_quantity = (ImageView) itemView.findViewById(R.id.imageview_item_quantity);
            imageview_item_unit_type = (ImageView) itemView.findViewById(R.id.imageview_item_unit_type);
            cardview_delete_button = (Button) itemView.findViewById(R.id.cardview_delete_button);
        }
    }
}
