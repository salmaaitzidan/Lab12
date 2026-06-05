package com.example.localisation;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        SupportMapFragment fragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map);

        if (fragment != null) {
            fragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        chargerPositionsSurCarte();
    }

    private void chargerPositionsSurCarte() {
        JsonObjectRequest requete = new JsonObjectRequest(
                Request.Method.POST,
                Config.URL_SHOW_ALL,
                null,
                response -> {
                    try {
                        JSONArray positions = response.getJSONArray("positions");
                        placerMarqueurs(positions);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> { /* gérer l'erreur réseau si nécessaire */ }
        );

        requestQueue.add(requete);
    }

    private void placerMarqueurs(JSONArray positions) throws JSONException {
        LatLng dernierePosition = null;

        for (int i = 0; i < positions.length(); i++) {
            JSONObject item = positions.getJSONObject(i);

            double lat = item.getDouble("latitude");
            double lon = item.getDouble("longitude");

            LatLng coordonnees = new LatLng(lat, lon);

            mMap.addMarker(new MarkerOptions()
                    .position(coordonnees)
                    .title(getString(R.string.marker_title))
            );

            dernierePosition = coordonnees;
        }

        if (dernierePosition != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(dernierePosition, 14f));
        }
    }
}
