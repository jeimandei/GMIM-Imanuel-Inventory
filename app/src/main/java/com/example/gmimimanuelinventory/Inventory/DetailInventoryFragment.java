package com.example.gmimimanuelinventory.Inventory;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.gmimimanuelinventory.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class DetailInventoryFragment extends Fragment {
    private String JSON_STRING;
    private ViewGroup viewGroup;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_detail_inventory, container, false);

        return viewGroup;
    }
}