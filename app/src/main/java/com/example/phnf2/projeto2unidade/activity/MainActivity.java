package com.example.phnf2.projeto2unidade.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.phnf2.projeto2unidade.R;
import com.example.phnf2.projeto2unidade.adapter.FixedTabsPageAdapter;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private FirebaseDatabase mFirebase;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    TabLayout tabLay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLay = findViewById(R.id.tabLay);
//Autenticação com o Firebase
        mFirebase = FirebaseDatabase.getInstance();

        ViewPager vp = findViewById(R.id.pager);
        PagerAdapter pa = new FixedTabsPageAdapter(getSupportFragmentManager());

        vp.setAdapter(pa);

        tabLay.setupWithViewPager(vp);

        tabLay.getTabAt(0).setIcon(R.drawable.icons8camera24);


        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //return super.onOptionsItemSelected(item);
        switch (item.getItemId()){
            case R.id.sign_out_menu:{
//logout
                AuthUI.getInstance().signOut(this);
                return true;
            }
            default:{
                return super.onOptionsItemSelected(item);
            }
        }

    }

}
