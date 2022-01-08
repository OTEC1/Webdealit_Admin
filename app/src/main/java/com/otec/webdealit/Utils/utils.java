package com.otec.webdealit.Utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.otec.webdealit.R;
import com.otec.webdealit.UI.MainActivity;
import com.otec.webdealit.UI.Music;
import com.otec.webdealit.UI.Notify;
import com.otec.webdealit.UI.SignUp;
import com.otec.webdealit.UI.Video;

public class utils {

    public static void message(String message, Context context){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }


    public  boolean SIGN_IN_USER () {
        if(FirebaseAuth.getInstance().getCurrentUser()!= null)
            return  true;
        else
            return  false;
    }




    //open from  fragment
    public void openFragments(Fragment fragment, FragmentActivity appCompatActivity, Bundle s) {
        FragmentTransaction fragmentTransaction = appCompatActivity.getSupportFragmentManager().beginTransaction();
        fragment.setArguments(s);
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }



    //Open Fragment from  Activity Class
    public void openFragment(Fragment fragment, AppCompatActivity appCompatActivity, Bundle s) {
        FragmentTransaction fragmentTransaction = appCompatActivity.getSupportFragmentManager().beginTransaction();
        fragment.setArguments(s);
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }





    //Open Fragment from  Adapter Class
    public void open_Fragment(Fragment fragments, String tag, Context view, Bundle bundle, int d) {
        AppCompatActivity activity = (AppCompatActivity) view;
        Fragment myfrag = fragments;
        myfrag.setArguments(bundle);
        activity.getSupportFragmentManager().beginTransaction().replace(d, myfrag, tag).addToBackStack(null).commit();

    }


    public void Img_streamer(Context context, String url, ProgressBar progressBar_img, ImageView vendor_img) {
        RequestOptions requestOptions = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true);

        Glide.with(context).load(url)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        progressBar_img.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressBar_img.setVisibility(View.GONE);
                        return false;
                    }
                })
                .apply(requestOptions)
                .into(vendor_img);

    }


    public void bottom_nav(BottomNavigationView bottomNav, AppCompatActivity appCompatActivity, ProgressBar progressBar) {

        bottomNav.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    progressBar.setVisibility(View.VISIBLE);
                    if (SIGN_IN_USER()) {
                        appCompatActivity.startActivity(new Intent(appCompatActivity, MainActivity.class));
                        progressBar.setVisibility(View.INVISIBLE);
                        return true;
                    } else
                        message("Pls sign in",appCompatActivity);

                case R.id.music:
                    progressBar.setVisibility(View.VISIBLE);
                    if (SIGN_IN_USER()) {
                        openFragment(new Music(),appCompatActivity,new Bundle());
                        progressBar.setVisibility(View.INVISIBLE);
                        return true;
                    } else
                        message("Pls sign in",appCompatActivity);

                case R.id.video:
                    progressBar.setVisibility(View.VISIBLE);
                    if (SIGN_IN_USER()) {
                        openFragment(new Video(),appCompatActivity,new Bundle());
                        progressBar.setVisibility(View.INVISIBLE);
                        return true;
                    } else
                        message("Pls sign in",appCompatActivity);

                case R.id.notify:
                    progressBar.setVisibility(View.VISIBLE);
                    if (SIGN_IN_USER()) {
                        appCompatActivity.startActivity(new Intent(appCompatActivity, Notify.class));
                        progressBar.setVisibility(View.INVISIBLE);
                        return true;
                    } else
                        message("Pls sign in",appCompatActivity);

                case R.id.Auth:
                        progressBar.setVisibility(View.VISIBLE);
                        appCompatActivity.startActivity(new Intent(appCompatActivity, SignUp.class));
                        progressBar.setVisibility(View.INVISIBLE);
                        return true;

            }
            return false;
        });

    }

}
