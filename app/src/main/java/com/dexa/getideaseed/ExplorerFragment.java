package com.dexa.getideaseed;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Dev on 20/01/18.
 */

public class ExplorerFragment extends Fragment {

    private Context context;
    private RecyclerView r1;
    private SeedViewRecycler seedViewRecycler;
    private LinearLayoutManager linearLayoutManager;

    @Override public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;

    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                                                 @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.explorer_tab, container, false);

        r1 = v.findViewById(R.id.rvExplorer);

        linearLayoutManager = new LinearLayoutManager(context);
        seedViewRecycler = new SeedViewRecycler(context,2);
        r1.setLayoutManager(linearLayoutManager);
        r1.setHasFixedSize(true);
        r1.setAdapter(seedViewRecycler);

        return v;
    }
}
