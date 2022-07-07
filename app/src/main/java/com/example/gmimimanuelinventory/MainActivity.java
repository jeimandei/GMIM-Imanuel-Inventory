package com.example.gmimimanuelinventory;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.gmimimanuelinventory.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private ActionBarDrawerToggle toggle;
    Toolbar toolbar;
    String id, name, komisi;
    EditText id_login, pass_login;
    TextView user_komisi, user_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private void initView(){
        View headerlayout = binding.navbarView.getHeaderView(0);

        user_name = headerlayout.findViewById(R.id.nb_username);
        user_komisi = headerlayout.findViewById(R.id.nb_komisi);

        user_name.setText(name);
        user_komisi.setText(komisi);

        setSupportActionBar(binding.toolbar);
    }

    public void callFragment(Fragment fragment) {
        FragmentManager man = getSupportFragmentManager();
        FragmentTransaction trans = man.beginTransaction();
        trans.setCustomAnimations(
                android.R.anim.slide_in_left,
                android.R.anim.slide_out_right
        );
        trans.replace(R.id.framelayout, fragment);
        trans.addToBackStack(null);
        trans.commit();
    }
}