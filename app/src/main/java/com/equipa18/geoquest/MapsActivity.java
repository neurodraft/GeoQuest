package com.equipa18.geoquest;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import io.github.jdiemke.triangulation.DelaunayTriangulator;
import io.github.jdiemke.triangulation.NotEnoughPointsException;
import io.github.jdiemke.triangulation.Triangle2D;
import io.github.jdiemke.triangulation.Vector2D;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = MapsActivity.class.getSimpleName();

    private GoogleMap mMap;
    private List<InterestPoint> interestPoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //Read interest points from JSON in assets folder (database replacement)
        InputStream is = null;
        try {
            is = getAssets().open("interestPoints.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
        InputStreamReader isr = new InputStreamReader(is);

        // use GSON library to construct a list of InterestPoint instances from the json file
        Gson gson = new Gson();
        Type interestPointListType = new TypeToken<ArrayList<InterestPoint>>(){}.getType();
        interestPoints = gson.fromJson(isr, interestPointListType);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.style_json));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }

        mMap = googleMap;

/*        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/

        // Get the absolute sum of latitude and longitude of all points to calculate avrg
        double latSum = 0;
        double lngSum = 0;

        // cycle through all interest points
        for(InterestPoint interestPoint : interestPoints){
            // create LatLng point from values stored in InterestPoint
            // TODO: Replace GeoCoordinates with LatLng inm InterestPoint
            LatLng point = new LatLng(interestPoint.geoCoordinates.latitude,
                    interestPoint.geoCoordinates.longitude);

            //polygonOptions.add(point);

            // Create marker for InterestPoint with name and image
            MarkerOptions markerOptions = new MarkerOptions().position(point)
                    .title(interestPoint.name);

            // Trye to load image from assets folder and scale/crop it
            try {
                InputStream is = getAssets().open("images/" + interestPoint.imageFile);
                Bitmap bmp = BitmapFactory.decodeStream(is);

                int width = bmp.getWidth();
                int height = bmp.getHeight();
                float scale;
                int xOffset = 0;
                int yOffset = 0;

                if(height > width){
                    scale = ((float) 150) / width;
                    yOffset = (height -width)/2;
                    height = width;
                } else if(width > height){
                    scale = ((float) 150) / height;
                    xOffset = (width -height)/2;
                    width = height;
                } else {
                    scale = ((float) 150) / width;
                }

                Matrix matrix = new Matrix();
                matrix.postScale(scale, scale);
                Bitmap resizedBmp = Bitmap.createBitmap(
                        bmp, xOffset, yOffset, width, height, matrix, true);
                bmp.recycle();

                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(resizedBmp));
            } catch (IOException e) {
                e.printStackTrace();
            }

            mMap.addMarker(markerOptions);

            latSum += interestPoint.geoCoordinates.latitude;
            lngSum += interestPoint.geoCoordinates.longitude;
        }

        // Create triangle mesh from points
        // TODO: Replace with graph data structure and single line rendering
        delaunayTriangulation();

        // calculates center point for camera with avrg of all points' coordinates
        // TODO: Replace with user's location
        LatLng avg = new LatLng(latSum / interestPoints.size(),
                lngSum / interestPoints.size());

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(avg)
                .zoom(12)
                .bearing(0)
                .tilt(25)
                .build();

        // Moves camera to position
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    // Only rendering of triangles (should be lines), no logic yet (should be a graph)
    private void delaunayTriangulation(){
        try {
            Vector<Vector2D> pointSet = new Vector<>();
            for(InterestPoint interestPoint : interestPoints){
                pointSet.add(new Vector2D(interestPoint.geoCoordinates.longitude,
                        interestPoint.geoCoordinates.latitude));
            }

            DelaunayTriangulator delaunayTriangulator = new DelaunayTriangulator(pointSet);
            delaunayTriangulator.triangulate();

            List<Triangle2D> triangleSoup = delaunayTriangulator.getTriangles();

            for(Triangle2D triangle : triangleSoup){
                LatLng p1 = new LatLng(triangle.a.y, triangle.a.x);
                LatLng p2 = new LatLng(triangle.b.y, triangle.b.x);
                LatLng p3 = new LatLng(triangle.c.y, triangle.c.x);

                mMap.addPolygon(new PolygonOptions().add(p1, p2, p3).strokeWidth(3));
            }
        } catch (NotEnoughPointsException e) {
        }
    }

}