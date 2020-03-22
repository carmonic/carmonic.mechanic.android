package com.camsys.carmonic.mechanic.History;

import android.content.Context;
import android.os.Bundle;

import com.camsys.carmonic.mechanic.Adapters.HistoryFragmentAdapter;
import com.camsys.carmonic.mechanic.Model.HistoryItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.TextView;

import com.camsys.carmonic.mechanic.R;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {


    private OnListFragmentInteractionListener mListener;
    Context mContext =  null;
    private   Toolbar toolbar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_history);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        mContext =  HistoryActivity.this;

        TextView txtTitle  =  (TextView) toolbar.findViewById(R.id.txtTitle);
        txtTitle.setText("History");


        AppCompatImageButton button  =  (AppCompatImageButton) toolbar.findViewById(R.id.appBackButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ArrayList<HistoryItem> list =  new ArrayList<>();

        HistoryItem acc1 =  new HistoryItem();
        acc1.setItemDate("March 14, 2017, 1:20pm");
        acc1.setItemRequester("Requested by Abeeb");
        acc1.setNumberOfStar(4);
        acc1.setAmount("N8765");
        list.add(acc1);

        HistoryItem acc2 =  new HistoryItem();
        acc2.setItemDate("March 14, 2017, 1:20pm");
        acc2.setItemRequester("Requested by Abiola");
        acc2.setNumberOfStar(2);
        acc2.setAmount("N7988");
        list.add(acc2);

        HistoryItem acc3 =  new HistoryItem();
        acc3.setItemDate("March 21, 2017, 1:20pm");
        acc3.setItemRequester("Requested by ``micheal from orile");
        acc3.setNumberOfStar(3);
        acc3.setAmount("N6500");
        list.add(acc3);


        HistoryItem acc4 =  new HistoryItem();
        acc4.setItemDate("March 27, 2017, 1:20pm");
        acc4.setItemRequester("Requested by Abeeb");
        acc4.setNumberOfStar(2);
        acc4.setAmount("N900");
        list.add(acc4);



        HistoryItem acc5 =  new HistoryItem();
        acc5.setItemDate("March 27, 2017, 1:20pm");
        acc5.setItemRequester("Requested by Abeeb");
        acc5.setNumberOfStar(2);
        acc5.setAmount("N900");
        list.add(acc5);



        HistoryItem acc6 =  new HistoryItem();
        acc6.setItemDate("March 27, 2017, 1:20pm");
        acc6.setItemRequester("Requested by Abeeb");
        acc6.setNumberOfStar(2);
        acc6.setAmount("N1900");
        list.add(acc6);

        HistoryItem acc7 =  new HistoryItem();
        acc7.setItemDate("March 27, 2017, 1:20pm");
        acc7.setItemRequester("Requested by Abeeb");
        acc7.setNumberOfStar(2);
        acc7.setAmount("N900");
        list.add(acc7);



        HistoryItem acc8 =  new HistoryItem();
        acc8.setItemDate("March 27, 2017, 1:20pm");
        acc8.setItemRequester("Requested by Abeeb");
        acc8.setNumberOfStar(2);
        acc8.setAmount("N1900");
        list.add(acc8);



        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        recyclerView.setItemViewCacheSize(15);
//        RecyclerView.ItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(),R.drawable.transparent);
//        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(new HistoryFragmentAdapter(list, mListener,mContext));
    }

    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(HistoryItem item);
        void onItemClick(View view, int position);
    }
}
