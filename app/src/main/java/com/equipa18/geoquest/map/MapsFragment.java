package com.equipa18.geoquest.map;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.equipa18.geoquest.world.InterestPoint;
import com.equipa18.geoquest.R;
import com.equipa18.geoquest.player.Player;
import com.equipa18.geoquest.player.PlayerManager;
import com.equipa18.geoquest.world.WorldManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import io.github.jdiemke.triangulation.DelaunayTriangulator;
import io.github.jdiemke.triangulation.NotEnoughPointsException;
import io.github.jdiemke.triangulation.Triangle2D;
import io.github.jdiemke.triangulation.Vector2D;

public class MapsFragment extends Fragment {

    private static final String TAG = MapsFragment.class.getSimpleName();

    private GoogleMap mMap;
    private Map<Marker, InterestPoint> markerInterestPointMap;

    List<InterestPoint> interestPoints;
    private Map<InterestPoint, List<GameEdge>> edgeMap;
    private List<GameEdge> edgeList;

    private InterestPoint currentPoint;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;

            customizeStyle();

            conqueredBitmap = vectorToBitmap(R.drawable.ic_baseline_flag_48, Color.GREEN, 60, 20);
            lockedBitmap = vectorToBitmap(R.drawable.ic_baseline_location_on_48,
                    Color.GRAY, 0 ,10);
            unlockedBitmap = vectorToBitmap(R.drawable.ic_baseline_location_on_48,
                    Color.RED, 0 ,10);


            prepareMapContent();

            setActions();

            setUpPlayer();
        }
    };

    private boolean showingCard = false;
    private BitmapDescriptor conqueredBitmap;
    private BitmapDescriptor lockedBitmap;
    private BitmapDescriptor unlockedBitmap;

    private void customizeStyle() {
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = mMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this.getContext(), R.raw.style_json));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        interestPoints = WorldManager.getInterestPoints();

        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    private BitmapDescriptor vectorToBitmap(@DrawableRes int id, @ColorInt int color, int offsetX, int offsetY) {
        Drawable vectorDrawable = ResourcesCompat.getDrawable(getResources(), id, null);
        assert vectorDrawable != null;
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth()+offsetX,
                vectorDrawable.getIntrinsicHeight() + offsetY, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0 + offsetX, 0 + offsetY, canvas.getWidth(), canvas.getHeight());
        DrawableCompat.setTint(vectorDrawable, color);
        vectorDrawable.draw(canvas);

        Bitmap resizedBitmap =Bitmap.createBitmap(bitmap, 0,0,bitmap.getWidth(), bitmap.getHeight() - offsetY);
        return BitmapDescriptorFactory.fromBitmap(resizedBitmap);
    }

    private void prepareMapContent(){
        markerInterestPointMap = new HashMap<>();
        edgeMap = new HashMap<>();
        edgeList = new ArrayList<>();

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
                    .title(interestPoint.name)
                    .icon(lockedBitmap);



            /*
            // Try to load image from assets folder and scale/crop it
            try {
                InputStream is = getContext().getAssets().open("images/" + interestPoint.imageFile);
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

             */

            markerInterestPointMap.put(mMap.addMarker(markerOptions), interestPoint);
            edgeMap.put(interestPoint, new ArrayList<>());

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


    private void delaunayTriangulation(){
        try {
            HashMap<Vector2D, InterestPoint> pointMap = new HashMap<>();
            Vector<Vector2D> pointVector = new Vector<>();

            for(InterestPoint interestPoint : interestPoints){
                Vector2D point = new Vector2D(interestPoint.geoCoordinates.longitude,
                        interestPoint.geoCoordinates.latitude);

                pointMap.put(point, interestPoint);
                pointVector.add(point);
            }

            DelaunayTriangulator delaunayTriangulator = new DelaunayTriangulator(pointVector);
            delaunayTriangulator.triangulate();

            List<Triangle2D> triangleSoup = delaunayTriangulator.getTriangles();

            for(Triangle2D triangle : triangleSoup){

                setUpEdgeBetween(pointMap.get(triangle.a), pointMap.get(triangle.b));
                setUpEdgeBetween(pointMap.get(triangle.a), pointMap.get(triangle.c));
                setUpEdgeBetween(pointMap.get(triangle.b), pointMap.get(triangle.c));



            }
        } catch (NotEnoughPointsException e) {
        }
    }



    private void setUpEdgeBetween(InterestPoint a, InterestPoint b){
        Polyline line = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(a.geoCoordinates.latitude, a.geoCoordinates.longitude),
                        new LatLng(b.geoCoordinates.latitude, b.geoCoordinates.longitude))
                .width(4)
                .color(Color.GRAY));


        GameEdge edge = new GameEdge(a, b, line);

        edgeMap.get(a).add(edge);
        edgeMap.get(b).add(edge);

        edgeList.add(edge);
    }


    private void setUpPlayer(){
        Player player = PlayerManager.getCurrentPlayer();
        if(player.isUnitialized()){
            InterestPoint startPoint = interestPoints.get(10);
            player.unlockPoint(startPoint.id);

            for(GameEdge edge : edgeMap.get(startPoint)){
                InterestPoint other;
                if(edge.a == startPoint){
                    other = edge.b;
                } else {
                    other = edge.a;
                }
                player.unlockPoint(other.id);

                edge.line.setColor(Color.RED);
            }

            player.initialized();

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(startPoint.geoCoordinates.latitude,
                            startPoint.geoCoordinates.longitude))
                    .zoom(16)
                    .bearing(0)
                    .tilt(25)
                    .build();

            // Moves camera to position
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        } else{
            for(GameEdge edge : edgeList){
                boolean conqueredA = player.hasConquered(edge.a.id);
                boolean conqueredB = player.hasConquered(edge.b.id);

                if(conqueredA && conqueredB){
                    edge.line.setColor(Color.GREEN);
                } else{
                    boolean unlockedA = player.hasUnlocked(edge.a.id);
                    boolean unlockedB = player.hasUnlocked(edge.b.id);
                    if(unlockedA && unlockedB){
                        edge.line.setColor(Color.RED);
                    }
                }
            }
        }

        for(Marker marker : markerInterestPointMap.keySet()){
            if(player.hasConquered(markerInterestPointMap.get(marker).id)){
                marker.setIcon(conqueredBitmap);
            } else if(player.hasUnlocked(markerInterestPointMap.get(marker).id)){
                marker.setIcon(unlockedBitmap);
            }
        }




    }

    private void setActions(){
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                // on marker click we are getting the title of our marker
                // which is clicked and displaying it in a toast message.
                //String markerName = marker.getTitle();
                //Toast.makeText(getContext(), "Clicked location is " + markerName, Toast.LENGTH_SHORT).show();
                InterestPoint interestPoint = markerInterestPointMap.get(marker);

                /*
                Bundle bundle = new Bundle();
                bundle.putString("title", interestPoint.name);
                bundle.putString("imageFile", interestPoint.imageFile);
                bundle.putBoolean("isUnlocked", PlayerManager.getCurrentPlayer().hasUnlocked(interestPoint));
                bundle.putBoolean("isConquered", PlayerManager.getCurrentPlayer().hasConquered(interestPoint));

                 */

                getParentFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .add(R.id.fragment_container_view, new InterestPointFragment(interestPoint))
                        .addToBackStack(null)
                        .commit();

                showingCard = true;
                currentPoint = interestPoint;


                return false;
            }
        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if(showingCard){

                    /*getParentFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .remove(getParentFragmentManager().findFragmentById(R.id.fragment_container_view))
                            .commitAllowingStateLoss();

                     */
                    int backStackEntry = getParentFragmentManager().getBackStackEntryCount();
                    if (backStackEntry > 0) {
                        for (int i = 0; i < backStackEntry; i++) {
                            getParentFragmentManager().popBackStackImmediate();
                        }
                    }
                    showingCard = false;
                    currentPoint = null;

                }
            }
        });
    }

    public void conqueredCurrentPoint() {
        if(currentPoint != null){
            System.out.println("Conquered " + currentPoint.name);
            PlayerManager.getCurrentPlayer().conquerPoint(currentPoint.id);

            List<InterestPoint> unlockedPoints = new ArrayList<>();

            for(GameEdge edge : edgeMap.get(currentPoint)){
                InterestPoint other;
                if(edge.a == currentPoint){
                    other = edge.b;
                } else {
                    other = edge.a;
                }
                System.out.println("\nConsidering " + other.name + "...");
                if(!PlayerManager.getCurrentPlayer().hasConquered(other.id)){
                    if(!PlayerManager.getCurrentPlayer().hasUnlocked(other.id)){
                        System.out.println("Unlocked " + other.name);
                        unlockedPoints.add(other);
                        PlayerManager.getCurrentPlayer().unlockPoint(other.id);
                    }
                    edge.line.setColor(Color.RED);
                } else{
                    System.out.println("Connected " + currentPoint.name + " to " + other.name);
                    edge.line.setColor(Color.GREEN);
                }

            }
            for(Marker marker : markerInterestPointMap.keySet()){
                InterestPoint element = markerInterestPointMap.get(marker);
                if(element == currentPoint){
                    marker.setIcon(conqueredBitmap);
                } else if(unlockedPoints.contains(element)){
                    marker.setIcon(unlockedBitmap);
                }
            }

            ((InterestPointFragment)getParentFragmentManager().findFragmentById(R.id.fragment_container_view)).wasConquered(unlockedPoints);
        }
    }


    private class GameEdge{
        public InterestPoint a;
        public InterestPoint b;
        public Polyline line;

        public GameEdge(InterestPoint a, InterestPoint b, Polyline line) {
            this.a = a;
            this.b = b;
            this.line = line;
        }
    }
}