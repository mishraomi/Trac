package com.firebolt.trac.adapters;

import android.app.Activity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.firebolt.trac.R;

import java.util.ArrayList;

/**
 * Created by Firebolt-Mesh on 8/22/2016.
 */
public class Landing_List_Adapter extends RecyclerView.Adapter<Landing_List_Adapter.LandingListViewHolder> {

    ArrayList<String> landing_list_arraylist = new ArrayList<>();
    Activity activity;

    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();

    public Landing_List_Adapter(ArrayList<String> landing_list_arraylist, Activity activity) {
        System.out.println("Adpater Contructor");
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
    public void onBindViewHolder(Landing_List_Adapter.LandingListViewHolder holder, int position) {
        System.out.println("Position : "+position);
        holder.textview_listname.setText(landing_list_arraylist.get(position));
        viewBinderHelper.bind(holder.swipe_reveal_layout, landing_list_arraylist.get(position));
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
