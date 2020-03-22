package com.camsys.carmonic.mechanic.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Build;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.camsys.carmonic.mechanic.History.HistoryActivity;
import com.camsys.carmonic.mechanic.History.HistoryDetailActivity;
import com.camsys.carmonic.mechanic.Model.HistoryItem;
import com.camsys.carmonic.mechanic.R;

import java.util.ArrayList;

/**
 * {@link RecyclerView.Adapter} that can display a {@link com.camsys.carmonic.mechanic.Model.HistoryItem} and makes a call to the
 * specified {@link HistoryFragment.OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class HistoryFragmentAdapter extends RecyclerView.Adapter<HistoryFragmentAdapter.ViewHolder> {

    private final ArrayList<HistoryItem> accList;
    private final HistoryActivity.OnListFragmentInteractionListener mListener;
    Context context;

    public HistoryFragmentAdapter(ArrayList<HistoryItem> accList, HistoryActivity.OnListFragmentInteractionListener listener, Context context) {
        this.accList = accList;
        mListener = listener;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_item, parent, false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.txtItemDate.setText(accList.get(position).getItemDate());
        holder.txtRequester.setText(accList.get(position).getItemRequester());
        holder.txtAmount.setText(accList.get(position).getAmount());
        setRating(holder,accList.get(position).getNumberOfStar());
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent  =  new  Intent(context, HistoryDetailActivity.class);
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return accList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView txtItemDate;
        public final TextView txtRequester;
        public final TextView txtAmount;
        public final ImageView star1;
        public final ImageView star2;
        public final ImageView star3;
        public final ImageView star4;
        public final ImageView star5;

        public HistoryItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            txtItemDate = (TextView) view.findViewById(R.id.txtItemDate);
            txtRequester = (TextView) view.findViewById(R.id.txtItemRequester);
            txtAmount = (TextView) view.findViewById(R.id.txtAmount);
             star1 =    (ImageView) view.findViewById(R.id.start1);
            star2 =    (ImageView) view.findViewById(R.id.start2);
            star3 =    (ImageView) view.findViewById(R.id.start3);
            star4 =    (ImageView) view.findViewById(R.id.start4);
            star5 =    (ImageView) view.findViewById(R.id.start5);



        }

        @Override
        public String toString() {
            return super.toString() + " '" + txtItemDate.getText() + "'";
        }
    }


    public void setRating(final ViewHolder holder,int  numberOfStart){
        if(numberOfStart == 1){
            holder.star1.setVisibility(View.VISIBLE);
        }
        if(numberOfStart == 2){
            holder.star1.setVisibility(View.VISIBLE);
            holder.star2.setVisibility(View.VISIBLE);
        }
        if(numberOfStart == 3){
            holder.star1.setVisibility(View.VISIBLE);
            holder.star2.setVisibility(View.VISIBLE);
            holder.star3.setVisibility(View.VISIBLE);
        }
        if(numberOfStart == 4){
            holder.star1.setVisibility(View.VISIBLE);
            holder.star2.setVisibility(View.VISIBLE);
            holder.star3.setVisibility(View.VISIBLE);
            holder.star4.setVisibility(View.VISIBLE);
        }
        if(numberOfStart == 5){
            holder.star1.setVisibility(View.VISIBLE);
            holder.star2.setVisibility(View.VISIBLE);
            holder.star3.setVisibility(View.VISIBLE);
            holder.star4.setVisibility(View.VISIBLE);
            holder.star5.setVisibility(View.VISIBLE);

        }
    }
}
