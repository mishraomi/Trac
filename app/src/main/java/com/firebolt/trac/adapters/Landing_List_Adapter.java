package com.firebolt.trac.adapters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
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
import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.firebolt.trac.R;
import com.firebolt.trac.activities.ListItemActivity;
import com.firebolt.trac.models.List;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by Firebolt-Mesh on 8/22/2016.
 */
public class Landing_List_Adapter extends RecyclerView.Adapter<Landing_List_Adapter.LandingListViewHolder> {

    ArrayList<List> landing_list_arraylist = new ArrayList<>();
    Activity activity;

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
        holder.textview_listdate.setText(landing_list_arraylist.get(position).getList_creation_date());

        holder.cardview_delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference("all_list")
                        .child(holder.textview_listname.getText().toString())
                        .child("info")
                        .removeValue();
                notifyDataSetChanged();
            }
        });

        holder.cardview_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, ListItemActivity.class);
                intent.putExtra("list_name", landing_list_arraylist.get(position).getList_name());
                activity.startActivity(intent);
            }
        });

        viewBinderHelper.bind(holder.swipe_reveal_layout, landing_list_arraylist.get(position).getList_name());

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
        TextView textview_listname, textview_list_created_by, textview_listdate;
        SwipeRevealLayout swipe_reveal_layout;
        ImageView imageview_list_item_count;


        public LandingListViewHolder(View itemView) {
            super(itemView);
            swipe_reveal_layout = (SwipeRevealLayout) itemView.findViewById(R.id.swipe_reveal_layout);
            cardview_list = (CardView) itemView.findViewById(R.id.cardview_list);
            cardview_delete_button = (Button) itemView.findViewById(R.id.cardview_delete_button);
            textview_listname = (TextView) itemView.findViewById(R.id.textview_listname);
            textview_list_created_by = (TextView) itemView.findViewById(R.id.textview_list_created_by);
            textview_listdate = (TextView) itemView.findViewById(R.id.textview_listdate);
            imageview_list_item_count = (ImageView) itemView.findViewById(R.id.imageview_list_item_count);
        }
    }

}
