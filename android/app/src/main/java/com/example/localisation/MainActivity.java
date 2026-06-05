package com.example.localisation;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private TextView tvLat;
    private TextView tvLon;
    private RequestQueue requestQueue;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvLat = findViewById(R.id.tvLat);
        tvLon = findViewById(R.id.tvLon);
        Button btnMap = findViewById(R.id.btnMap);

        requestQueue   = Volley.newRequestQueue(getApplicationContext());
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        btnMap.setOnClickListener(v ->
                startActivity(new Intent(this, MapsActivity.class))
        );

        verifierEtDemanderPermission();
    }

    private void verifierEtDemanderPermission() {
        boolean permissionAccordee = ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED;

        if (!permissionAccordee) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{ Manifest.permission.ACCESS_FINE_LOCATION },
                    Config.REQUEST_PERMISSION_LOCATION
            );
        } else {
            demarrerEcouteGPS();
        }
    }

    @SuppressLint("MissingPermission")
    private void demarrerEcouteGPS() {
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                Config.GPS_MIN_TIME_MS,
                Config.GPS_MIN_DISTANCE_M,
                new LocationListener() {

                    @Override
                    public void onLocationChanged(@NonNull Location location) {
                        double lat = location.getLatitude();
                        double lon = location.getLongitude();
                        double alt = location.getAltitude();
                        float  acc = location.getAccuracy();

                        tvLat.setText("Latitude : " + lat);
                        tvLon.setText("Longitude : " + lon);

                        String message = String.format(
                                getResources().getString(R.string.new_location),
                                lat, lon, alt, acc
                        );

                        envoyerPosition(lat, lon);
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {
                        String etat;
                        switch (status) {
                            case LocationProvider.AVAILABLE:             etat = "DISPONIBLE";         break;
                            case LocationProvider.TEMPORARILY_UNAVAILABLE: etat = "INDISPONIBLE TEMP"; break;
                            case LocationProvider.OUT_OF_SERVICE:        etat = "HORS SERVICE";       break;
                            default:                                     etat = "INCONNU";
                        }
                        Toast.makeText(
                                getApplicationContext(),
                                String.format(getString(R.string.provider_new_status), provider, etat),
                                Toast.LENGTH_SHORT
                        ).show();
                    }

                    @Override
                    public void onProviderEnabled(@NonNull String provider) {
                        Toast.makeText(
                                getApplicationContext(),
                                String.format(getString(R.string.provider_enabled), provider),
                                Toast.LENGTH_SHORT
                        ).show();
                    }

                    @Override
                    public void onProviderDisabled(@NonNull String provider) {
                        Toast.makeText(
                                getApplicationContext(),
                                String.format(getString(R.string.provider_disabled), provider),
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }
        );
    }

    private void envoyerPosition(final double lat, final double lon) {
        StringRequest requete = new StringRequest(
                Request.Method.POST,
                Config.URL_CREATE,
                response -> { /* réponse reçue — log possible ici */ },
                error -> Toast.makeText(
                        getApplicationContext(),
                        String.format(getString(R.string.network_error), error.getMessage()),
                        Toast.LENGTH_SHORT
                ).show()
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                SimpleDateFormat sdf = new SimpleDateFormat(Config.DATE_FORMAT, Locale.getDefault());

                Map<String, String> params = new HashMap<>();
                params.put("latitude",  String.valueOf(lat));
                params.put("longitude", String.valueOf(lon));
                params.put("date",      sdf.format(new Date()));
                params.put("imei",      recupererIdentifiantAppareil());
                return params;
            }
        };

        requestQueue.add(requete);
    }

    private String recupererIdentifiantAppareil() {
        String androidId = Settings.Secure.getString(
                getContentResolver(), Settings.Secure.ANDROID_ID
        );
        if (androidId != null && !androidId.trim().isEmpty()) {
            return androidId;
        }

        try {
            boolean permissionTel = ActivityCompat.checkSelfPermission(
                    this, Manifest.permission.READ_PHONE_STATE
            ) == PackageManager.PERMISSION_GRANTED;

            if (permissionTel) {
                TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                if (tm != null) {
                    String imei = tm.getDeviceId();
                    if (imei != null && !imei.trim().isEmpty()) return imei;
                }
            }
        } catch (Exception ignored) {}

        return "APPAREIL_INCONNU";
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        boolean accorde = grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED;

        if (requestCode == Config.REQUEST_PERMISSION_LOCATION) {
            if (accorde) {
                demarrerEcouteGPS();
            } else {
                Toast.makeText(this, getString(R.string.permission_denied), Toast.LENGTH_LONG).show();
            }
        }
    }
}
