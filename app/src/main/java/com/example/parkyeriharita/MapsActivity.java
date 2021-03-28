package com.example.parkyeriharita;

import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    ListView listView;
    List<MyItems> listItems = new ArrayList<>();

    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        listView = findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, SecondActivity.class);
                startActivity(intent);
            }
        });

        MyItems myItems1, myItems2;
        LinkedHashMap<String, Integer> all_info = new LinkedHashMap<>();
        all_info.put("Park A", 10);
        all_info.put("Park B", 24);
        all_info.put("Park C", 50);
        all_info.put("Park D", 90);
        all_info.put("Park E", 135);
        all_info.put("Park F", 477);
        all_info.put("Park G", 0);
        all_info.put("Park H", 5);
        myItems1 = new MyItems(null, 0, all_info);



        for (String yer : myItems1.getAll_info().keySet()) {
            //myItems2 = new MyItems(yer, null, null);
            //listItems.add(myItems2);
        }

        for (Map.Entry<String, Integer> entry : all_info.entrySet()) {
            System.out.println("key: "+entry.getKey());
            System.out.println("value: "+entry.getValue());
            myItems2 = new MyItems(entry.getKey(), entry.getValue(), null);
            listItems.add(myItems2);
        }


        MyAdapter myAdapter = new MyAdapter(listItems, this);
        listView.setAdapter(myAdapter);


    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng istanbul = new LatLng(41, 29);
        mMap.addMarker(new MarkerOptions().position(istanbul).title("İstanbul"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(istanbul));
    }

    public class MyAdapter extends BaseAdapter implements Filterable {
        List<MyItems> myItemsList;
        List<MyItems> myItemsListFiltered;

        public MyAdapter(List<MyItems> myItemsList, MapsActivity mapsActivity) {
            this.myItemsList = myItemsList;
            this.myItemsListFiltered = myItemsList;
        }

        @Override
        public int getCount() {
            return myItemsListFiltered.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @SuppressLint("SetTextI18n")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            @SuppressLint("ViewHolder")
            View view1 = getLayoutInflater().inflate(R.layout.row_items, null);

            TextView tx_yer_ismi = view1.findViewById(R.id.tx_yer_ismi);
            tx_yer_ismi.setText(myItemsListFiltered.get(position).name);

            TextView tx_bos_yer_sayisi = view1.findViewById(R.id.tx_bos_yer_sayisi);
            tx_bos_yer_sayisi.setText(myItemsListFiltered.get(position).bos_sayisi+"");

            TextView tx_km = view1.findViewById(R.id.tx_km);
            //tx_km.setText(myItemsListFiltered.get(position));

            TextView tx_fee = view1.findViewById(R.id.tx_fee);
            TextView tx_duration = view1.findViewById(R.id.tx_duration);
            TextView tx_time = view1.findViewById(R.id.tx_time);

            ImageView imageView = view1.findViewById(R.id.imageView);
            imageView.setImageResource(R.drawable.park_entry);
            ImageView imageView2 = view1.findViewById(R.id.imageView2);
            imageView2.setImageResource(R.drawable.car_32);
            ImageView imageView3 = view1.findViewById(R.id.imageView3);
            imageView3.setImageResource(R.drawable.car_ph_32);


            return view1;
        }

        @Override
        public Filter getFilter() {
            return null;
        }
    }

    public class MyItems implements Serializable {
        private String name;
        private int bos_sayisi;
        private LinkedHashMap<String, Integer> all_info;

        public MyItems(String name, int bos_sayisi, LinkedHashMap<String, Integer> all_info) {
            this.name = name;
            this.bos_sayisi = bos_sayisi;
            this.all_info = all_info;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getBos_sayisi() {
            return bos_sayisi;
        }

        public void setBos_sayisi(int desc) {
            this.bos_sayisi = desc;
        }

        public LinkedHashMap<String, Integer> getAll_info() {
            return all_info;
        }

        public void setAll_info(LinkedHashMap<String, Integer> all_info) {
            this.all_info = all_info;
        }
    }
}