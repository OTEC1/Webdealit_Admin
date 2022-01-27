package com.otec.webdealit.UI;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.otec.webdealit.R;

public class Notify extends AppCompatActivity {

    private Button webfly,network;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify);


        webfly  = findViewById(R.id.webfly);
        network  = findViewById(R.id.networkingProgramming);



        webfly.setOnClickListener(e->{
                openFragment(new ListOfVisitors(),"listVisitors",1, this);
        });


        network.setOnClickListener(e->{
            openFragment(new ListOfVisitors(),"listVisitors",2, this);
        });
    }



    public void openFragment(Fragment fragment, String my_fragment, int a, AppCompatActivity context) {
        FragmentTransaction fragmentTransaction = context.getSupportFragmentManager().beginTransaction();
        reuse_fragment(fragmentTransaction, fragment, my_fragment, BUNDLE(new Bundle(), a), R.id.frameLayout);
    }

    private Bundle BUNDLE(Bundle bundle, int a) {
        bundle.putInt("table", a);
        return  bundle;
    }

    private void reuse_fragment(FragmentTransaction fragmentTransaction, Fragment fragment, String my_fragment, Bundle b, int frameLayout) {
        fragment.setArguments(b);
        fragmentTransaction.replace(frameLayout, fragment, my_fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}