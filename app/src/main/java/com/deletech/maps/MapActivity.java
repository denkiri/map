package com.deletech.maps;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;

import androidx.databinding.DataBindingUtil;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;

import android.widget.Toast;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.util.MapTileIndex;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polygon;
import org.osmdroid.views.overlay.Polyline;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import com.deletech.maps.databinding.ActivityMapBinding;
import com.deletech.maps.storage.PreferenceManager;

public class MapActivity extends AppCompatActivity {
    ActivityMapBinding binding;
    MapView map;
    double latitudeA=0;
    double longitudeA=0;
    double latitudeB=0;
    double longitudeB=0;
    String latitudePoint;
    String longitudePointB;
    List<String> point;
    double longitudePoint;
    ArrayList<String> lineCoordinate;
    ArrayList<String>  lineLatitude;
    ArrayList<String>  lineLongitude;
    List<List<List<Float>>> lineCoordinates;
     Double coordinates;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_map);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            latitudeA = extras.getDouble("latitudeA");
            longitudeA = extras.getDouble("longitudeA");
            latitudeB = extras.getDouble("latitudeB");
            longitudeB = extras.getDouble("longitudeB");
            lineLatitude= extras.getStringArrayList("lineLatitude");
           lineLongitude= extras.getStringArrayList("lineLongitude");
           // Toast.makeText(this,extras.getStringArrayList("lineCoordinate").toString(),Toast.LENGTH_SHORT).show();
        }
            Context ctx = getApplicationContext();
            map = (MapView) findViewById(R.id.map);
            map.getTileProvider().clearTileCache();
            Configuration.getInstance().setCacheMapTileCount((short) 12);
            Configuration.getInstance().setCacheMapTileOvershoot((short) 12);
            //coordinates=Double.valueOf(new PreferenceManager(this).getType());
        // Create a custom tile source
            map.setTileSource(new OnlineTileSourceBase("", 1, 20, 512, ".png",
                    new String[]{"https://a.tile.openstreetmap.org/"}) {
                @Override
                public String getTileURLString(long pMapTileIndex) {
                    return getBaseUrl()
                            + MapTileIndex.getZoom(pMapTileIndex)
                            + "/" + MapTileIndex.getX(pMapTileIndex)
                            + "/" + MapTileIndex.getY(pMapTileIndex)
                            + mImageFilenameEnding;
                }
            });

            map.setMultiTouchControls(true);
            IMapController mapController = map.getController();
            GeoPoint startPoint;
            startPoint = new GeoPoint(new GeoPoint(latitudeA, longitudeA));
            mapController.setZoom(16.0);
            mapController.setCenter(startPoint);
            final Context context = this;
            map.invalidate();
            createPointA();
            createPointB();
            createPolylineAB();
            //distanceB();
           createPolyline();
           // createPolygon();
        String num =new PreferenceManager(this).getType() ;
        String str[] = num.split(",");
        List<String> al = new ArrayList<String>();
        al = Arrays.asList(str);
        for(String s: al){
           // Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
        }
    }
    public double distance(){
        double lat1 = this.latitudeA;
        double lon1 = this.longitudeA;
        double lat2 = this.latitudeB;
        double lon2 = this.longitudeB;
        double el1 = 0;
        double el2 = 0;
        final int R = 6371; // Radius of the earth
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters
        double height = el1 - el2;
        distance = Math.pow(distance, 2) + Math.pow(height, 2);
        return Math.sqrt(distance);
    }
    public void distanceB(){
        Location startPoint=new Location("locationA");
        startPoint.setLatitude(latitudeA);
        startPoint.setLongitude(longitudeA);
        Location endPoint=new Location("locationB");
        endPoint.setLatitude(latitudeB);
        endPoint.setLongitude(longitudeB);
        double distance=startPoint.distanceTo(endPoint);
        Toast.makeText(getApplicationContext(), Double.toString(distance),Toast.LENGTH_SHORT).show();
    }
    public void createPointA(){
        if(map == null) {
            return;
        }
        Marker my_marker = new Marker(map);
        my_marker.setPosition(new GeoPoint(latitudeA, longitudeA));
        my_marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        my_marker.setTitle("point A");
        my_marker.setPanToView(true);
        map.getOverlays().add(my_marker);
        map.invalidate();
    }
    public void createPointB(){
        if(map == null) {
            return;
        }
        Marker my_marker = new Marker(map);
        my_marker.setPosition(new GeoPoint(latitudeB, longitudeB));
        my_marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        my_marker.setTitle("point B");
        my_marker.setPanToView(true);
        map.getOverlays().add(my_marker);
        map.invalidate();
    }
    public void createPolylineAB(){
        List<GeoPoint> geoPoints = new ArrayList<>();
//add your points here
        geoPoints.add( new GeoPoint(latitudeA, longitudeA));
        geoPoints.add( new GeoPoint(latitudeB, longitudeB));
        Polyline line = new Polyline();   //see note below!
        line.setPoints(geoPoints);
        binding.distanceValue.setText(Double.toString(Math.round((line.getDistance()*100.0)/100.0)));
        line.setOnClickListener((polyline, mapView, eventPos) -> {
         //  Toast.makeText(mapView.getContext(), "polyline with " + polyline.getDistance()+ "pts was tapped", Toast.LENGTH_LONG).show();
            return false;
        });
        map.getOverlayManager().add(line);

    }
    public void createPolyline(){
     //   List<GeoPoint> geoPoints = new ArrayList<>();
        ArrayList<String> points = new ArrayList<String>(lineLatitude);
        for(int i = 0; i < points.size(); i++)
        {
           latitudePoint= points.get(i);
            Toast.makeText(this,latitudePoint.toString(),Toast.LENGTH_SHORT).show();
        }
        ArrayList<String> pointB = new ArrayList<String>(lineLongitude);
        for(int i = 0; i < pointB.size(); i++)
        {
            longitudePointB= pointB.get(i);
           // Toast.makeText(this,longitudePointB.toString(),Toast.LENGTH_SHORT).show();
        }
/*
        geoPoints.add( new GeoPoint(-35.016, 143.321));
        geoPoints.add( new GeoPoint(-34.747, 145.592));
        geoPoints.add( new GeoPoint(-34.364, 147.891));
        geoPoints.add( new GeoPoint(-33.501, 150.217));
        geoPoints.add( new GeoPoint(-32.306, 149.248));
        geoPoints.add( new GeoPoint(-32.491, 147.309));
        Polyline line = new Polyline();   //see note below!
        line.setPoints(geoPoints);
        line.setOnClickListener(new Polyline.OnClickListener() {
            @Override
            public boolean onClick(Polyline polyline, MapView mapView, GeoPoint eventPos) {
                //Toast.makeText(mapView.getContext(), "polyline with " + polyline.getPoints().size() + "pts was tapped", Toast.LENGTH_LONG).show();
                return false;
            }
        });
        map.getOverlayManager().add(line);


 */
    }
    public void createPolygon(){

        List<GeoPoint> geoPoints = new ArrayList<>();
//add your points here
        geoPoints.add( new GeoPoint(-27.457, 153.040));
        geoPoints.add( new GeoPoint(-33.852, 151.211));
        geoPoints.add( new GeoPoint(-37.813, 144.962));
        geoPoints.add( new GeoPoint(-34.928, 138.599));
        Polygon polygon = new Polygon();    //see note below
        polygon.setFillColor(Color.argb(75, 255,0,0));
        geoPoints.add(geoPoints.get(0));    //forces the loop to close
        polygon.setPoints(geoPoints);
        polygon.setTitle("A sample polygon");

//polygons supports holes too, points should be in a counter-clockwise order
        List<List<GeoPoint>> holes = new ArrayList<>();
        holes.add(geoPoints);
        polygon.setHoles(holes);

        map.getOverlayManager().add(polygon);
    }

}