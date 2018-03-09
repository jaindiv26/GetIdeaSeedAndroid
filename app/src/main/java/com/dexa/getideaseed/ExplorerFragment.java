package com.dexa.getideaseed;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by Dev on 20/01/18.
 */

public class ExplorerFragment extends Fragment{

    private Context context;
    private RecyclerView recyclerView;
    private ExplorerAdapter explorerAdapter;
    private ArrayList<ModelExplorer> modelExplorerArrayList;
    private ClickListener clickListener;

    @Override public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;

        Bundle bundle=getArguments();

        modelExplorerArrayList = bundle.getParcelableArrayList("arr");
    }

    @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                                                 @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_explorer, container, false);
        initComponents(view);
        return view;
    }

    private void initComponents(View view) {
        recyclerView = view.findViewById(R.id.rvExplorer);

        ClickListener clickListener = new ClickListener() {
            @Override public void onClick(ModelExplorer modelExplorer) {
                Intent i = new Intent(context, FeedbackActivity.class);
                i.putExtra("feedbackUserData",modelExplorer);
                context.startActivity(i);
            }
        };
        explorerAdapter = new ExplorerAdapter(context, modelExplorerArrayList,clickListener);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(explorerAdapter);

        ModelExplorer modelExplorer;
    }

    public static ExplorerFragment newInstance(Bundle bundle) {
        ExplorerFragment instance = new ExplorerFragment();
        instance.setArguments(bundle);
        return instance;
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
