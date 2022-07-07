package com.example.gmimimanuelinventory.Inventory;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.gmimimanuelinventory.Configuration.InventoryConfig;
import com.example.gmimimanuelinventory.HTTPHandler;
import com.example.gmimimanuelinventory.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class InventoryListFragment extends Fragment {

    private String JSON_STRING;
    private ViewGroup viewGroup;
    private ListView listView;
    ListAdapter listAdapter;
    private FloatingActionButton addButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_inventory_list, container, false);



        return viewGroup;
    }

    private void getJSON() {
        class GetJSON extends AsyncTask<Void, Void, String> {
            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(getContext(), "Getting Data", "Please wait...", false, false);
            }

            @Override
            protected String doInBackground(Void... voids) {
                HTTPHandler handler = new HTTPHandler();
                String result = handler.sendGetResp(InventoryConfig.URL_GET_KDI_INVENTORY);
                Log.d("GetData", result);
                return result;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        progressDialog.dismiss();
                    }
                }, 1000);

                JSON_STRING = s;
                Log.d("Data_JSON", JSON_STRING);

                showAllInventory();
            }
        }
        GetJSON getJSON = new GetJSON();
        getJSON.execute();
    }

    private void showAllInventory() {
        JSONObject jsonObject = null;
        ArrayList<HashMap<String, String>> arrayList = new ArrayList<HashMap<String, String>>();

        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray jsonArray = jsonObject.getJSONArray(InventoryConfig.TAG_JSON_ARRAY);
            Log.d("Data_JSON_LIST: ", String.valueOf(jsonArray));

            for (int i=0;i<jsonArray.length(); i++){
                JSONObject object = jsonArray.getJSONObject(i);
                String id = object.getString(InventoryConfig.KEY_INV_ID);
                String qr = object.getString(InventoryConfig.KEY_INV_QRCODE);
                String name = object.getString(InventoryConfig.KEY_INV_NAME);
                String count = object.getString(InventoryConfig.KEY_INV_COUNT);
                String belongs = object.getString(InventoryConfig.KEY_INV_BELONGS);
                String incharge = object.getString(InventoryConfig.KEY_INV_INCHARGE);

                HashMap<String, String> inventory = new HashMap<>();
                inventory.put(InventoryConfig.KEY_INV_ID, id);
                inventory.put(InventoryConfig.KEY_INV_QRCODE, qr);
                inventory.put(InventoryConfig.KEY_INV_NAME, name);
                inventory.put(InventoryConfig.KEY_INV_COUNT, count);
                inventory.put(InventoryConfig.KEY_INV_BELONGS, belongs);
                inventory.put(InventoryConfig.KEY_INV_INCHARGE, incharge);

                arrayList.add(inventory);
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        listAdapter = new ArrayAdapter(getContext(), R.layout.listview_item_inventory, R.id.lv_item_name, arrayList);
        listView.setAdapter(listAdapter);


//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                DetailCustomerFragment detailCustomerFragment = new DetailCustomerFragment();
//                FragmentManager fragmentManager = getFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.framelayout,detailCustomerFragment);
//
//
////                HashMap<String, String> map = (HashMap) adapter1.getItem(b);
////                String nsbid = map.get(ConfigCustomer.TAG_JSON_CST_ID).toString();
//
//
//                if (a == null) {
//                    a = arrayList1.get(i);
//                }
//
//                b = adapter.getItem(i);
//                c = b.split("\n\n");
//                a = String.valueOf(c[2]);
//
//                args.putString("id", a);
//                args.putString("id_emp_1", id_emp);
//                detailCustomerFragment.setArguments(args);
//                Log.d("post", String.valueOf(a));
//                Log.d("post", String.valueOf(a));
//                Log.d("idddd", String.valueOf(arrayList1));
//
//
//
//
//                Log.d("Par: ", String.valueOf(args));
//                fragmentTransaction.commit();
//            }
//        });
    }
}