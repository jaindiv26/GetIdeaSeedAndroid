package com.dexa.getideaseed;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ImageView homeImage, exploreImage;
    private TextView homeBar, exploreBar;
    private Context context;
    private UnScrollableViewPager viewPager;
    private Toolbar mainToolbar;
    private FrameLayout redCircle;
    private TextView countTextView;
    private int filterCount = 0;
    private MenuItem filterMenuItem, searchItem, menuItemLogOut;
    private SearchView searchView;
    private FrameLayout rootView;
    private HashMap<String, String> hashMap = new HashMap<>();
    private MainActivityPagerAdapter mainActivityPagerAdapter;
    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;

        initComponents();
        onTabsClicked();
    }

    private void onTabsClicked() {
        homeImage.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if (!PrefManager.getInstance().getBoolean("loggedIn")) {
                    showLoginSignUpDialog();
                } else {
                    viewPager.setCurrentItem(0);
                }
            }
        });

        exploreImage.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                viewPager.setCurrentItem(1);
            }
        });
    }

    private void initComponents() {
        homeImage = findViewById(R.id.ivHome);
        exploreImage = findViewById(R.id.ivExplore);
        homeBar = findViewById(R.id.tvHomeHighlight);
        exploreBar = findViewById(R.id.tvExploreHighlight);
        viewPager = findViewById(R.id.pager);
        mainToolbar = findViewById(R.id.maintoolbar);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override public void onPageSelected(int position) {

                switch (position) {
                    case 0: {
                        exploreImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.unselected_explore_botton));
                        homeImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.home_botton));
                        homeBar.setTextColor(ContextCompat.getColor(context, R.color.lightGreen));
                        exploreBar.setTextColor(ContextCompat.getColor(context, R.color.darkGrey));
                        break;
                    }

                    case 1: {
                        homeImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.unselected_home_botton));
                        exploreImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.explore_botton));
                        homeBar.setTextColor(ContextCompat.getColor(context, R.color.darkGrey));
                        exploreBar.setTextColor(ContextCompat.getColor(context, R.color.lightGreen));
                        break;
                    }
                }
            }

            @Override public void onPageScrollStateChanged(int state) {

            }
        });

        setSupportActionBar(mainToolbar);
        getSupportActionBar().setIcon(R.drawable.ic_lightbulb_outline_white_24dp);
    }

    public void postLoginChanges() {
        if (!PrefManager.getInstance().getBoolean("loggedIn")) {
            homeImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.unselected_home_botton));
            exploreImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.explore_botton));
            homeBar.setTextColor(ContextCompat.getColor(context, R.color.darkGrey));
            exploreBar.setTextColor(ContextCompat.getColor(context, R.color.lightGreen));
            menuItemLogOut.setTitle("Log In");
            viewPager.setPagingEnabled(false);

            mainActivityPagerAdapter = new MainActivityPagerAdapter(getSupportFragmentManager());
            viewPager.setAdapter(mainActivityPagerAdapter);
            viewPager.setCurrentItem(1);
        } else {
            exploreImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.unselected_explore_botton));
            homeImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.home_botton));
            homeBar.setTextColor(ContextCompat.getColor(context, R.color.lightGreen));
            exploreBar.setTextColor(ContextCompat.getColor(context, R.color.darkGrey));
            menuItemLogOut.setTitle("Log Out");
            viewPager.setPagingEnabled(true);

            mainActivityPagerAdapter = new MainActivityPagerAdapter(getSupportFragmentManager());
            viewPager.setAdapter(mainActivityPagerAdapter);
            viewPager.setCurrentItem(0);
        }
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        filterMenuItem = menu.findItem(R.id.activity_main_alerts_menu_item);
        rootView = (FrameLayout) filterMenuItem.getActionView();
        redCircle = rootView.findViewById(R.id.view_alert_red_circle);
        countTextView = rootView.findViewById(R.id.view_alert_count_textview);
        searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override public boolean onQueryTextChange(String newText) {
                setFilterAndSearch();
                return false;
            }
        });
        menuItemLogOut = menu.findItem(R.id.menuItemLogOut);
        postLoginChanges();
        return super.onCreateOptionsMenu(menu);
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuItemLogOut: {
                if(!PrefManager.getInstance().getBoolean("loggedIn")){
                    Intent i = new Intent(context, LoginActivity.class);
                    i.putExtra("showLoginView",true);
                    context.startActivity(i);
                    return true;
                }
                else {
                    PrefManager.getInstance().getPrefs().edit().clear().apply();
                    startActivity(new Intent(context, LoginActivity.class));
                    finish();
                    return true;
                }

            }
            case R.id.activity_main_alerts_menu_item: {
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", hashMap);
                BottomSheetFragment bottomSheetFragment = BottomSheetFragment.newInstance(bundle);
                bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
                return true;
            }
            case R.id.action_search: {
                filterMenuItem.setVisible(false);
                menuItemLogOut.setVisible(false);
                MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
                        return true;
                    }

                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
                        filterMenuItem.setVisible(true);
                        menuItemLogOut.setVisible(true);
                        searchView.setQuery("", false);
                        setFilterAndSearch();
                        return true;
                    }
                });
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setFilterAndSearch() {
        List<Fragment> fragments = mainActivityPagerAdapter.getViewPagerFragments();
        for(Fragment fragment : fragments){
            if (fragment instanceof HomeFragment) {
                ((HomeFragment) fragment).homeFragmentSearch(searchView.getQuery().toString(), hashMap);
            }
            if (fragment instanceof ExplorerFragment) {
                ((ExplorerFragment) fragment).explorerFragmentSearch(searchView.getQuery().toString(), hashMap);
            }
        }
    }

    @Override public boolean onPrepareOptionsMenu(Menu menu) {
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(filterMenuItem);
            }
        });
        return super.onPrepareOptionsMenu(menu);
    }

    public Boolean touchListener(int flag) {
        if (flag == 1) {
            searchView.clearFocus();
            return true;
        } else {
            return false;
        }
    }

    public void showLoginSignUpDialog() {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("You need an account to proceed.");
        alertDialogBuilder.setPositiveButton("Log In",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent i = new Intent(context, LoginActivity.class);
                        i.putExtra("showLoginView",true);
                        context.startActivity(i);
                    }
                });

        alertDialogBuilder.setNegativeButton("Register", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(context, LoginActivity.class);
                i.putExtra("showRegisterView",true);
                context.startActivity(i);
            }
        });

        alertDialogBuilder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override protected void onResume() {
        super.onResume();
        if(PrefManager.getInstance().getString("username") == null){
            getSupportActionBar().setTitle(" Welcome ");
            if(menuItemLogOut != null){
                menuItemLogOut.setTitle("Log In");
            }
        }
        else {
            getSupportActionBar().setTitle(" Welcome " + PrefManager.getInstance().getString("username"));
            if(menuItemLogOut != null){
                menuItemLogOut.setTitle("Log Out");
            }
        }
    }

    public void bottomSheetDismissedToast(HashMap<String,String> hashMap,int flag){
        filterCount = flag;
        this.hashMap = hashMap;
        if (filterCount > 0) {
            countTextView.setText(String.valueOf(filterCount));
            redCircle.setVisibility(View.VISIBLE);
        } else if (filterCount == 0) {
            redCircle.setVisibility(View.INVISIBLE);
            countTextView.setText(String.valueOf(0));
        }
        setFilterAndSearch();
    }
}
