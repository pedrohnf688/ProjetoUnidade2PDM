package com.example.phnf2.projeto2unidade.fragment;


import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.phnf2.projeto2unidade.R;
import com.example.phnf2.projeto2unidade.modelo.Conversa;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentMapa extends Fragment {

    private GoogleMap mGoogleMap;
    private static final int MyRequestCode = 10;
    private FusedLocationProviderClient mFusedLocationClient;
    Location l;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMessagesDatabaseReference;
    private FirebaseAuth mFirebaseAuth;
    public double latitude;
    public double longitude;
    private Button EnviarLocal;
    public String localizacaoMaps;




    public FragmentMapa() {


    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_fragment_mapa, container, false);

        EnviarLocal = v.findViewById(R.id.enviarLocal);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);


        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {

                mGoogleMap = googleMap;

                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    mGoogleMap.setMyLocationEnabled(true);


                } else {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, MyRequestCode);

                }

            }
        });

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());

        EnviarLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {

                            if (location != null) {

                                longitude = location.getLongitude();
                                latitude = location.getLatitude();
                                Log.i("PDM", "Latitude:" + latitude + "Longitude:" + longitude);

                                Locale locale = new Locale("pt", "BR");
                                GregorianCalendar calendar = new GregorianCalendar();
                                SimpleDateFormat formatador = new SimpleDateFormat("dd' de 'MMMMM' de 'yyyy' - 'HH':'mm'h'", locale);


                                mFirebaseDatabase = FirebaseDatabase.getInstance();
                                mMessagesDatabaseReference = mFirebaseDatabase.getReference().child("messages");
                                mFirebaseAuth = FirebaseAuth.getInstance();
                                FirebaseUser user = mFirebaseAuth.getCurrentUser();

                                localizacaoMaps = "https://www.google.com/maps/place/" + latitude + "," + longitude;


                                Conversa c = new Conversa(localizacaoMaps,user.getDisplayName(),formatador.format(calendar.getTime()),null);
                                mMessagesDatabaseReference.push().setValue(c);


                            }

                        }

                    });

                }

            }

        });



        return v;
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == MyRequestCode) {
            if (permissions.length == 1 && permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(getContext(),Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(getContext(),
                                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    mGoogleMap.setMyLocationEnabled(true);


                } else {

                    Toast.makeText(getContext(), "Permisões Negadas, não Possível Localizar com Precisão!", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

}