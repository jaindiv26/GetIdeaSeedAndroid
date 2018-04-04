package com.dexa.getideaseed;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Dev on 22/03/18.
 */

public class BottomSheetFragment extends BottomSheetDialogFragment {

    private Context context;
    private Spinner spDifficulty,spOriginality,spProgress,spLightbulbs;
    private TextView tvDifficulty,tvOriginality,tvProgress,tvLightbulbs,tvSearchTitle;
    private Button btClose,btSearchButton,btResetButton;
    private ProgressDialog progressDialog;
    private ApiManagerListener apiManagerListener;
    private int flag = 0,difficultyFlag=0,originalityFlag =0,progressFlag=0,lightBulbsFlag=0;
    private View view;

    public static BottomSheetFragment newInstance(Bundle bundle) {
        BottomSheetFragment instance = new BottomSheetFragment();
        instance.setArguments(bundle);
        return instance;
    }

    @Override public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_search, container, false);
        return view;
    }

    @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initComponents();
        if(getArguments() != null) {
            HashMap<String, String> getDataHashMap = new HashMap<>();
            getDataHashMap = (HashMap<String, String>) getArguments().getSerializable("data");
            if(getDataHashMap !=null && !getDataHashMap.isEmpty()){
                if(Integer.parseInt(getDataHashMap.get("difficulty"))>0){
                    difficultyFlag = 1;
                    tvDifficulty.setTextColor(ContextCompat.getColor(context,R.color.lightGreen));
                    spDifficulty.setSelection(Integer.parseInt(getDataHashMap.get("difficulty")));
                    if(difficultyFlag+originalityFlag+progressFlag+lightBulbsFlag==0){
                        btResetButton.setVisibility(View.GONE);
                        tvSearchTitle.setText("Select Filter");
                    }
                    else {
                        btResetButton.setVisibility(View.VISIBLE);
                        tvSearchTitle.setText(String.valueOf(difficultyFlag+originalityFlag+progressFlag+lightBulbsFlag)+" Filter applied :");
                    }
                }
                else if(Integer.parseInt(getDataHashMap.get("difficulty")) == 0){
                    difficultyFlag=0;
                    tvDifficulty.setTextColor(ContextCompat.getColor(context,R.color.darkGrey));
                    if(difficultyFlag+originalityFlag+progressFlag+lightBulbsFlag==0){
                        btResetButton.setVisibility(View.GONE);
                        tvSearchTitle.setText("Select Filter");
                    }
                    else {
                        btResetButton.setVisibility(View.VISIBLE);
                        tvSearchTitle.setText(String.valueOf(difficultyFlag+originalityFlag+progressFlag+lightBulbsFlag)+" Filter applied :");
                    }
                }

                if(Integer.parseInt(getDataHashMap.get("originality"))>0){
                    originalityFlag = 1;
                    tvOriginality.setTextColor(ContextCompat.getColor(context,R.color.lightGreen));
                    spOriginality.setSelection(Integer.parseInt(getDataHashMap.get("originality")));
                    if(difficultyFlag+originalityFlag+progressFlag+lightBulbsFlag==0){
                        btResetButton.setVisibility(View.GONE);
                        tvSearchTitle.setText("Select Filter");
                    }
                    else {
                        btResetButton.setVisibility(View.VISIBLE);
                        tvSearchTitle.setText(String.valueOf(difficultyFlag+originalityFlag+progressFlag+lightBulbsFlag)+" Filter applied");
                    }
                }
                else if(Integer.parseInt(getDataHashMap.get("originality")) == 0){
                    originalityFlag =0;
                    tvOriginality.setTextColor(ContextCompat.getColor(context,R.color.darkGrey));
                    if(difficultyFlag+originalityFlag+progressFlag+lightBulbsFlag==0){
                        btResetButton.setVisibility(View.GONE);
                        tvSearchTitle.setText("Select Filter");
                    }
                    else {
                        btResetButton.setVisibility(View.VISIBLE);
                        tvSearchTitle.setText(String.valueOf(difficultyFlag+originalityFlag+progressFlag+lightBulbsFlag)+" Filter applied");
                    }
                }

                if(Integer.parseInt(getDataHashMap.get("progress"))>0){
                    progressFlag = 1;
                    tvProgress.setTextColor(ContextCompat.getColor(context,R.color.lightGreen));
                    spProgress.setSelection(Integer.parseInt(getDataHashMap.get("progress")));
                    if(difficultyFlag+originalityFlag+progressFlag+lightBulbsFlag==0){
                        btResetButton.setVisibility(View.GONE);
                        tvSearchTitle.setText("Select Filter");
                    }
                    else {
                        btResetButton.setVisibility(View.VISIBLE);
                        tvSearchTitle.setText(String.valueOf(difficultyFlag+originalityFlag+progressFlag+lightBulbsFlag)+" Filter applied");
                    }
                }
                else if(Integer.parseInt(getDataHashMap.get("progress")) == 0){
                    progressFlag =0;
                    tvProgress.setTextColor(ContextCompat.getColor(context,R.color.darkGrey));
                    if(difficultyFlag+originalityFlag+progressFlag+lightBulbsFlag==0){
                        btResetButton.setVisibility(View.GONE);
                        tvSearchTitle.setText("Select Filter");
                    }
                    else {
                        btResetButton.setVisibility(View.VISIBLE);
                        tvSearchTitle.setText(String.valueOf(difficultyFlag+originalityFlag+progressFlag+lightBulbsFlag)+" Filter applied");
                    }
                }

                if(Integer.parseInt(getDataHashMap.get("lightBulbs"))>0){
                    lightBulbsFlag = 1;
                    tvLightbulbs.setTextColor(ContextCompat.getColor(context,R.color.lightGreen));
                    spLightbulbs.setSelection(Integer.parseInt(getDataHashMap.get("lightBulbs")));
                    if(difficultyFlag+originalityFlag+progressFlag+lightBulbsFlag==0){
                        btResetButton.setVisibility(View.GONE);
                        tvSearchTitle.setText("Select Filter");
                    }
                    else {
                        btResetButton.setVisibility(View.VISIBLE);
                        tvSearchTitle.setText(String.valueOf(difficultyFlag+originalityFlag+progressFlag+lightBulbsFlag)+" Filter applied");
                    }
                }
                else if(Integer.parseInt(getDataHashMap.get("lightBulbs")) == 0){
                    lightBulbsFlag =0;
                    tvLightbulbs.setTextColor(ContextCompat.getColor(context,R.color.darkGrey));
                    if(difficultyFlag+originalityFlag+progressFlag+lightBulbsFlag==0){
                        btResetButton.setVisibility(View.GONE);
                        tvSearchTitle.setText("Select Filter");
                    }
                    else {
                        btResetButton.setVisibility(View.VISIBLE);
                        tvSearchTitle.setText(String.valueOf(difficultyFlag+originalityFlag+progressFlag+lightBulbsFlag)+" Filter applied");
                    }
                }
            }
        }
    }

    private void initComponents() {
        spDifficulty = view.findViewById(R.id.spDifficulty);
        spOriginality = view.findViewById(R.id.spOriginality);
        spProgress = view.findViewById(R.id.spProgress);
        spLightbulbs = view.findViewById(R.id.spLightBulbs);
        btClose = view.findViewById(R.id.btCloseButton);
        btSearchButton = view.findViewById(R.id.btSearchButton);
        tvDifficulty = view.findViewById(R.id.tvDiff);
        tvOriginality = view.findViewById(R.id.tvOrigin);
        tvProgress = view.findViewById(R.id.tvProgress);
        tvLightbulbs = view.findViewById(R.id.tvLightBulbs);
        tvSearchTitle = view.findViewById(R.id.tvSearchTitle);
        btResetButton = view.findViewById(R.id.btResetButton);

        btClose.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                dismiss();
            }
        });

        btSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                HashMap<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("difficulty",String.valueOf(spDifficulty.getSelectedItemPosition()));
                hashMap.put("originality",String.valueOf(spOriginality.getSelectedItemPosition()));
                hashMap.put("progress",String.valueOf(spProgress.getSelectedItemPosition()));
                hashMap.put("lightBulbs",String.valueOf(spLightbulbs.getSelectedItemPosition()));
                MainActivity mainActivity = (MainActivity) context;
                flag=difficultyFlag+originalityFlag+progressFlag+lightBulbsFlag;
                mainActivity.sendData(hashMap,flag);
                dismiss();
            }
        });

        spDifficulty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position>0){
                    difficultyFlag = 1;
                    tvDifficulty.setTextColor(ContextCompat.getColor(context,R.color.lightGreen));
                    if(difficultyFlag+originalityFlag+progressFlag+lightBulbsFlag==0){
                        btResetButton.setVisibility(View.GONE);
                        tvSearchTitle.setText("Select Filter");
                    }
                    else {
                        btResetButton.setVisibility(View.VISIBLE);
                        tvSearchTitle.setText(String.valueOf(difficultyFlag+originalityFlag+progressFlag+lightBulbsFlag)+" Filter applied");
                    }
                }
                else if(position == 0){
                    difficultyFlag=0;
                    tvDifficulty.setTextColor(ContextCompat.getColor(context,R.color.darkGrey));
                    if(difficultyFlag+originalityFlag+progressFlag+lightBulbsFlag==0){
                        btResetButton.setVisibility(View.GONE);
                        tvSearchTitle.setText("Select Filter");
                    }
                    else {
                        btResetButton.setVisibility(View.VISIBLE);
                        tvSearchTitle.setText(String.valueOf(difficultyFlag+originalityFlag+progressFlag+lightBulbsFlag)+" Filter applied");
                    }
                }
            }

            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });
        spOriginality.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position>0){
                    originalityFlag = 1;
                    tvOriginality.setTextColor(Color.parseColor("#2ec97f"));
                    if(difficultyFlag+originalityFlag+progressFlag+lightBulbsFlag==0){
                        btResetButton.setVisibility(View.GONE);
                        tvSearchTitle.setText("Select Filter");
                    }
                    else {
                        btResetButton.setVisibility(View.VISIBLE);
                        tvSearchTitle.setText(String.valueOf(difficultyFlag+originalityFlag+progressFlag+lightBulbsFlag)+" Filter applied");
                    }
                }
                else if(position == 0){
                    originalityFlag =0;
                    tvOriginality.setTextColor(ContextCompat.getColor(context,R.color.darkGrey));
                    if(difficultyFlag+originalityFlag+progressFlag+lightBulbsFlag==0){
                        btResetButton.setVisibility(View.GONE);
                        tvSearchTitle.setText("Select Filter");
                    }
                    else {
                        btResetButton.setVisibility(View.VISIBLE);
                        tvSearchTitle.setText(String.valueOf(difficultyFlag+originalityFlag+progressFlag+lightBulbsFlag)+" Filter applied");
                    }
                }
            }

            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });
        spProgress.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position>0){
                    progressFlag = 1;
                    tvProgress.setTextColor(Color.parseColor("#2ec97f"));
                    if(difficultyFlag+originalityFlag+progressFlag+lightBulbsFlag==0){
                        btResetButton.setVisibility(View.GONE);
                        tvSearchTitle.setText("Select Filter");
                    }
                    else {
                        btResetButton.setVisibility(View.VISIBLE);
                        tvSearchTitle.setText("Filter applied : "+String.valueOf(difficultyFlag+originalityFlag+progressFlag+lightBulbsFlag));
                    }
                }
                else if(position == 0){
                    progressFlag =0;
                    tvProgress.setTextColor(ContextCompat.getColor(context,R.color.darkGrey));
                    if(difficultyFlag+originalityFlag+progressFlag+lightBulbsFlag==0){
                        btResetButton.setVisibility(View.GONE);
                        tvSearchTitle.setText("Select Filter");
                    }
                    else {
                        btResetButton.setVisibility(View.VISIBLE);
                        tvSearchTitle.setText("Filter applied : "+String.valueOf(difficultyFlag+originalityFlag+progressFlag+lightBulbsFlag));
                    }
                }
            }

            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });
        spLightbulbs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position>0){
                    lightBulbsFlag = 1;
                    tvLightbulbs.setTextColor(Color.parseColor("#2ec97f"));
                    if(difficultyFlag+originalityFlag+progressFlag+lightBulbsFlag==0){
                        btResetButton.setVisibility(View.GONE);
                        tvSearchTitle.setText("Select Filter");
                    }
                    else {
                        btResetButton.setVisibility(View.VISIBLE);
                        tvSearchTitle.setText("Filter applied : "+String.valueOf(difficultyFlag+originalityFlag+progressFlag+lightBulbsFlag));
                    }
                }
                else if(position == 0){
                    lightBulbsFlag =0;
                    tvLightbulbs.setTextColor(ContextCompat.getColor(context,R.color.darkGrey));
                    if(difficultyFlag+originalityFlag+progressFlag+lightBulbsFlag==0){
                        btResetButton.setVisibility(View.GONE);
                        tvSearchTitle.setText("Select Filter");
                    }
                    else {
                        btResetButton.setVisibility(View.VISIBLE);
                        tvSearchTitle.setText("Filter applied : "+String.valueOf(difficultyFlag+originalityFlag+progressFlag+lightBulbsFlag));
                    }
                }
            }

            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        btResetButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                tvSearchTitle.setText("Select Filter");
                tvDifficulty.setTextColor(ContextCompat.getColor(context,R.color.darkGrey));
                tvOriginality.setTextColor(ContextCompat.getColor(context,R.color.darkGrey));
                tvProgress.setTextColor(ContextCompat.getColor(context,R.color.darkGrey));
                tvLightbulbs.setTextColor(ContextCompat.getColor(context,R.color.darkGrey));
                spDifficulty.setSelection(0);
                spOriginality.setSelection(0);
                spProgress.setSelection(0);
                spLightbulbs.setSelection(0);
                btResetButton.setVisibility(View.GONE);

                HashMap<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("difficulty",String.valueOf(0));
                hashMap.put("originality",String.valueOf(0));
                hashMap.put("progress",String.valueOf(0));
                hashMap.put("lightBulbs",String.valueOf(0));
                MainActivity mainActivity = (MainActivity) context;
                flag=0;
                mainActivity.sendData(hashMap,flag);
            }
        });

        spinnerSetter();
        tvSearchTitle.setText("Select Filter");
        btResetButton.setVisibility(View.GONE);
    }

    private void spinnerSetter(){
        List<Integer> listDifficulty = new ArrayList<>();
        List<Integer> listOriginality = new ArrayList<>();
        List<String> listProgress = new ArrayList<>();
        List<String> listLightBulbs = new ArrayList<>();

        listDifficulty.add(0);
        listDifficulty.add(1);
        listDifficulty.add(2);
        listDifficulty.add(3);
        listDifficulty.add(4);
        listDifficulty.add(5);
        listDifficulty.add(6);
        listDifficulty.add(7);
        listDifficulty.add(8);
        listDifficulty.add(9);
        listDifficulty.add(10);
        ArrayAdapter<Integer> difficultyDataAdapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_item, listDifficulty);
        difficultyDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDifficulty.setAdapter(difficultyDataAdapter);
        spDifficulty.setSelection(0);

        listOriginality.add(0);
        listOriginality.add(1);
        listOriginality.add(2);
        listOriginality.add(3);
        listOriginality.add(4);
        listOriginality.add(5);
        listOriginality.add(6);
        listOriginality.add(7);
        listOriginality.add(8);
        listOriginality.add(9);
        listOriginality.add(10);
        ArrayAdapter<Integer> originalityDataAdapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_item,listOriginality);
        originalityDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spOriginality.setAdapter(originalityDataAdapter);
        spOriginality.setSelection(0);


        listProgress.add("Select");
        listProgress.add("Not Yet Started");
        listProgress.add("In Progress");
        listProgress.add("Done!");
        ArrayAdapter<String> progressDataAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item,listProgress);
        progressDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spProgress.setAdapter(progressDataAdapter);
        spProgress.setSelection(0);

        listLightBulbs.add("Select");
        listLightBulbs.add("LightBulbs");
        listLightBulbs.add("Most Lightbulbs");
        listLightBulbs.add("Least Lightbulbs");
        ArrayAdapter<String> lightBulbsDataAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item,listLightBulbs);
        lightBulbsDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spLightbulbs.setAdapter(lightBulbsDataAdapter);
        spLightbulbs.setSelection(0);
    }

    private void fetchData(){
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading..."); // Setting Message
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);

        apiManagerListener = new ApiManagerListener() {
            @Override public void onSuccess(String response) {
                progressDialog.dismiss();
            }

            @Override public void onError(VolleyError error) {
                progressDialog.dismiss();

                if(error instanceof NoConnectionError){
                    Toast.makeText(context,
                            "No Internet access", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(context,
                            "Something went bad", Toast.LENGTH_LONG).show();
                }

            }

            @Override public void statusCode(int statusCode) {

            }
        };

        String url = "https://www.getideaseed.com/api/ideas/search/current-user";

        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("difficulty",String.valueOf(spDifficulty.getSelectedItemPosition()));
            if(spLightbulbs.getSelectedItemPosition()==0)
        hashMap.put("lightbulbs","lightbulbs");

            if(spLightbulbs.getSelectedItemPosition()==1)
        hashMap.put("lightbulbs","lightbulbs");

            if(spLightbulbs.getSelectedItemPosition()==2)
        hashMap.put("lightbulbs","lightbulbs");
        hashMap.put("originality",String.valueOf(spOriginality.getSelectedItemPosition()));
        hashMap.put("progress",String.valueOf(spProgress.getSelectedItemPosition()+1));
        hashMap.put("userName",PrefManager.getInstance().getString("username"));

        ApiManager apiManager = new ApiManager(apiManagerListener);
        apiManager.postRequest(context,url,hashMap);
    }
}
