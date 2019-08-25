package com.camsys.carmonic.mechanic.History;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.camsys.carmonic.mechanic.Adapters.HistoryFragmentAdapter;
import com.camsys.carmonic.mechanic.Model.HistoryItem;
import com.camsys.carmonic.mechanic.R;

import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link HistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistoryFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnListFragmentInteractionListener mListener;

    public HistoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment HistoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HistoryFragment newInstance() {
        return new HistoryFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view  = inflater.inflate(R.layout.fragment_history, container, false);

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


        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        recyclerView.setItemViewCacheSize(15);
//        RecyclerView.ItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(),R.drawable.transparent);
//        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(new HistoryFragmentAdapter(list, mListener,getActivity()));

        return view;
    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getActivity().getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */


    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(HistoryItem item);
        void onItemClick(View view, int position);
    }
}
