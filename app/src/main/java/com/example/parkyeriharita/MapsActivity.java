package com.example.parkyeriharita;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.common.primitives.Ints;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    android.widget.SearchView search_bar;
    MyAdapter myAdapter;
    ListView listView;
    List<MyItems> listItems = new ArrayList<>();
    MyItems myItems1, myItems2;
    String park_adi, park_saat;
    int park_bos_sayi, park_fiyat;
    LinkedHashMap<String[], LinkedHashMap<String, Integer>> park_list = new LinkedHashMap<>();
    LinkedHashMap<String, LatLng> list_latlng;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        search_bar = findViewById(R.id.search_bar);
        search_bar.setFocusable(false);
        search_bar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                myAdapter.getFilter().filter(newText);
                return true;
            }

        });

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("park")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                for (String yer : document.getData().keySet()) {
                                    System.out.println("çıktı yer " + yer);

                                    Object o1 = document.getData().get(yer);
                                    ObjectMapper om1 = new ObjectMapper();
                                    LinkedHashMap<String, Object> mappedObject1 = om1.convertValue(o1, LinkedHashMap.class);
                                    park_adi = (String) mappedObject1.get(" adı");
                                    park_saat = (String) mappedObject1.get("aktif saatler");
                                    park_bos_sayi = ((Long) mappedObject1.get("boş yer sayısı")).intValue();
                                    park_fiyat = ((Long) mappedObject1.get("0-1 saat")).intValue();
                                    String[] info = new String[]{park_adi, park_saat, String.valueOf(park_bos_sayi), String.valueOf(park_fiyat)};
                                    System.out.println("çıktı info " + Arrays.toString(info));

                                    Object o2 = mappedObject1.get("koordinat");
                                    ObjectMapper om2 = new ObjectMapper();
                                    LinkedHashMap<String, Integer> mappedObject2 = om2.convertValue(o2, LinkedHashMap.class);
                                    System.out.println("çıktı koordinat " + mappedObject2);
                                    park_list.put(info, mappedObject2);

                                }
                            }
                            after_database();

                        } else {
                            Log.w("çıktıı", "Error getting documents.", task.getException());
                        }
                    }
                });

    }

    public void after_database() {
        listView = findViewById(R.id.listView);

        myItems1 = new MyItems(null, null, 0, 0, 0, park_list);

        list_latlng = new LinkedHashMap<>();
        listItems.clear();
        for (Map.Entry<String[], LinkedHashMap<String, Integer>> entry : myItems1.getAll_info().entrySet()) {
            int[] latlng = Ints.toArray(entry.getValue().values());
            list_latlng.put(entry.getKey()[0], new LatLng(latlng[0], latlng[1]));
            double distance = SphericalUtil.computeDistanceBetween(new LatLng(latlng[0], latlng[1]), new LatLng(latlng[0], latlng[1]));

            myItems2 = new MyItems(entry.getKey()[0], entry.getKey()[1], Integer.parseInt(entry.getKey()[2]), Integer.parseInt(entry.getKey()[3]), distance, null);
            listItems.add(myItems2);
        }

        myAdapter = new MyAdapter(listItems, MapsActivity.this);
        listView.setAdapter(myAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(context, SecondActivity.class);
                startActivity(intent);
            }
        });
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
        LatLng samandıra, uskudar;
        samandıra = new LatLng(41, 29.25);
        mMap.addMarker(new MarkerOptions().position(samandıra).title("samandıra"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(samandıra, 15));

        uskudar = new LatLng(41.03, 29.03);
        mMap.addMarker(new MarkerOptions().position(uskudar).title("üsküdar"));

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        UiSettings uis = googleMap.getUiSettings();
        uis.setCompassEnabled(true);
        uis.setMyLocationButtonEnabled(true);
        uis.setZoomControlsEnabled(true);

        final LatLng[] guncel_koord = new LatLng[1];
        final PolylineOptions[] options = {new PolylineOptions().add(samandıra).add(samandıra).width(5).color(Color.BLUE).visible(true).geodesic(true)};
        final Polyline[] polylineFinal = {mMap.addPolyline(options[0])};
        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                guncel_koord[0] = new LatLng(location.getLatitude(), location.getLongitude());

                polylineFinal[0].remove();
                options[0] = new PolylineOptions().add(samandıra).add(guncel_koord[0]).width(5).color(Color.BLUE).visible(true).geodesic(true);
                polylineFinal[0] = mMap.addPolyline(options[0]);

                int count=0;
                for (LinkedHashMap<String, Integer> i : myItems1.getAll_info().values()) {
                    int[] latlng = Ints.toArray(i.values());
                    double distance = SphericalUtil.computeDistanceBetween(guncel_koord[0], new LatLng(latlng[0], latlng[1]));

                    listItems.get(count).setKm(distance);
                    count++;
                    myAdapter.setMyItemsList(listItems);
                    listView.setAdapter(myAdapter);
                }
            }
        });

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

        public void setMyItemsList(List<MyItems> myItemsList) {
            this.myItemsList = myItemsList;
            notifyDataSetChanged();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @SuppressLint({"SetTextI18n", "DefaultLocale"})
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            @SuppressLint({"ViewHolder", "InflateParams"})
            View view1 = getLayoutInflater().inflate(R.layout.row_items, null);

            TextView tx_yer_ismi = view1.findViewById(R.id.tx_yer_ismi);
            tx_yer_ismi.setText(myItemsListFiltered.get(position).getName());

            TextView tx_bos_yer_sayisi = view1.findViewById(R.id.tx_bos_yer_sayisi);
            tx_bos_yer_sayisi.setText(myItemsListFiltered.get(position).getBos_sayisi()+"");

            TextView tx_km = view1.findViewById(R.id.tx_km);
            tx_km.setText(String.format("%.2f", myItemsListFiltered.get(position).getKm()/1000) + " km");

            TextView tx_fee = view1.findViewById(R.id.tx_fee);
            tx_fee.setText(myItemsListFiltered.get(position).getFiyat()+"₺");

            TextView tx_duration = view1.findViewById(R.id.tx_duration);

            TextView tx_time = view1.findViewById(R.id.tx_time);
            tx_time.setText(myItemsListFiltered.get(position).getAktif_saat());

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
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();

                    if (constraint == null && constraint.length() == 0) {
                        filterResults.count = myItemsList.size();
                        filterResults.values = myItemsList;
                    } else {
                        String searchStr = constraint.toString().toLowerCase();
                        List<MyItems> resultData = new ArrayList<>();

                        for (MyItems i : myItemsList) {
                            if (i.getName().toLowerCase().contains(searchStr))
                                resultData.add(i);

                            filterResults.count = resultData.size();
                            filterResults.values = resultData;
                        }

                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    myItemsListFiltered = (List<MyItems>) results.values;
                    notifyDataSetChanged();
                }
            };
        }
    }

    public void hideKeyboard(View view) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}