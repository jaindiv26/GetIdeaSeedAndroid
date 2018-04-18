package com.dexa.getideaseed;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Build;

import java.util.Arrays;

/**
 * Created by Dev on 17/04/18.
 */

public class AppShortcutUtil {

    public void changeShortcutOnLoggedIn(Context context){
        if (PrefManager.getInstance().getBoolean("loggedIn")){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1){
                ShortcutManager shortcutManager = context.getSystemService(ShortcutManager.class);
                Intent addIdeaIntent = new Intent(Intent.ACTION_MAIN, Uri.EMPTY, context, NewIdeaActivity.class);
                addIdeaIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                ShortcutInfo addIdea = new ShortcutInfo.Builder(context, "addIdea")
                        .setShortLabel("Add Idea")
                        .setLongLabel("Add Idea")
                        .setIcon(Icon.createWithResource(context, R.mipmap.ic_launcher))
                        .setIntent(addIdeaIntent)
                        .setRank(2)
                        .build();

                Intent homeIntent = new Intent(Intent.ACTION_MAIN, Uri.EMPTY, context, MainActivity.class);
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                homeIntent.putExtra("viewPagerPosition",0);
                ShortcutInfo home = new ShortcutInfo.Builder(context, "home")
                        .setShortLabel("Home")
                        .setLongLabel("Home")
                        .setIcon(Icon.createWithResource(context, R.mipmap.ic_launcher))
                        .setIntent(homeIntent)
                        .setRank(1)
                        .build();

                Intent explorerIntent = new Intent(Intent.ACTION_MAIN, Uri.EMPTY, context, MainActivity.class);
                explorerIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                explorerIntent.putExtra("viewPagerPosition",1);
                ShortcutInfo explorer = new ShortcutInfo.Builder(context, "explorer")
                        .setShortLabel("Explore")
                        .setLongLabel("Explore")
                        .setIcon(Icon.createWithResource(context, R.mipmap.ic_launcher))
                        .setIntent(explorerIntent)
                        .setRank(0)
                        .build();

                if(shortcutManager != null){
                    shortcutManager.setDynamicShortcuts(Arrays.asList(explorer,home,addIdea));
                }
            }
        }
    }

    public void changeShortcutOnSignedOut(Context context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1){
            ShortcutManager shortcutManager = context.getSystemService(ShortcutManager.class);
            Intent loginIntent = new Intent(Intent.ACTION_MAIN, Uri.EMPTY, context, LoginActivity.class);
            loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            ShortcutInfo login = new ShortcutInfo.Builder(context, "login")
                    .setShortLabel("Login")
                    .setLongLabel("Login")
                    .setIcon(Icon.createWithResource(context, R.mipmap.ic_launcher))
                    .setIntent(loginIntent)
                    .setRank(2)
                    .build();

            Intent signUpIntent = new Intent(Intent.ACTION_MAIN, Uri.EMPTY, context, LoginActivity.class);
            signUpIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            ShortcutInfo signUp = new ShortcutInfo.Builder(context, "signUp")
                    .setShortLabel("Sign Up")
                    .setLongLabel("Sign Up")
                    .setIcon(Icon.createWithResource(context, R.mipmap.ic_launcher))
                    .setIntent(signUpIntent)
                    .setRank(1)
                    .build();

            Intent GuestExplorerIntent = new Intent(Intent.ACTION_MAIN, Uri.EMPTY, context, MainActivity.class);
            GuestExplorerIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            GuestExplorerIntent.putExtra("viewPagerPosition",1);
            ShortcutInfo GuestExplorer = new ShortcutInfo.Builder(context, "explorer1")
                    .setShortLabel("Explore")
                    .setLongLabel("Explore")
                    .setIcon(Icon.createWithResource(context, R.mipmap.ic_launcher))
                    .setIntent(GuestExplorerIntent)
                    .setRank(0)
                    .build();

            if(shortcutManager != null){
                shortcutManager.setDynamicShortcuts(Arrays.asList(GuestExplorer,signUp,login));
            }
        }
    }
}
