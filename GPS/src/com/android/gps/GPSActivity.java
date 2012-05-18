package com.android.gps;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;

public class GPSActivity extends MapActivity {
    /** Called when the activity is first created. */
	MapController mapController;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
     // Get a reference to the MapView
        MapView myMapView = (MapView)findViewById(R.id.myMapView);
        // Get the Map View’s controller
        mapController = myMapView.getController();
        // Configure the map display options
        myMapView.setSatellite(true);
        myMapView.setStreetView(true);
        myMapView.displayZoomControls(false);
        // Zoom in
        mapController.setZoom(17);
        LocationManager locationManager;
        String context = Context.LOCATION_SERVICE;
        locationManager = (LocationManager)getSystemService(context);
        //String provider = LocationManager.GPS_PROVIDER;
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        String provider = locationManager.getBestProvider(criteria, true);
        Location location =
        locationManager.getLastKnownLocation(provider);
        
        updateWithNewLocation(location);
        locationManager.requestLocationUpdates(provider, 2000, 10,
        		locationListener);
    }
    
    private void updateWithNewLocation(Location location) {
    	String latLongString;
    	TextView myLocationText;
    	//myLocationText = (TextView)findViewById(R.id.myLocationText);
    	String addressString = "No address found";
    	if (location != null) {
    		// Update the map location.
    		Double geoLat = location.getLatitude()*1E6;
    		Double geoLng = location.getLongitude()*1E6;
    		GeoPoint point = new GeoPoint(geoLat.intValue(),
    		geoLng.intValue());
    		mapController.animateTo(point);
    	double lat = location.getLatitude();
    	double lng = location.getLongitude();
    	
    	Geocoder gc = new Geocoder(this, Locale.getDefault());
    	try {
    	List<Address> addresses = gc.getFromLocation(lat, lng, 1);
    	StringBuilder sb = new StringBuilder();
    	if (addresses.size() > 0) {
    	Address address = addresses.get(0);
    	for (int i = 0; i < address.getMaxAddressLineIndex(); i++)
    	sb.append(address.getAddressLine(i)).append("\n");
    	sb.append(address.getLocality()).append("\n");
    	sb.append(address.getPostalCode()).append("\n");
    	sb.append(address.getCountryName());
    	}
    	addressString = sb.toString();
    	} catch (IOException e) {}
    	
    	latLongString = "Lat:" + lat + "\nLong:" + lng;
    	} else {
    	latLongString = "No location found";
    	}
    	//myLocationText.setText("Your Current Position is:\n" + latLongString);
    	//myLocationText.setText("Your Current Position is:\n" + addressString);
    	
    }
 
    
    private final LocationListener locationListener = new LocationListener() {
    	public void onLocationChanged(Location location) {
    	updateWithNewLocation(location);
    	}
    	public void onProviderDisabled(String provider){
    	updateWithNewLocation(null);
    	}
    	public void onProviderEnabled(String provider){ }
    	public void onStatusChanged(String provider, int status,
    	Bundle extras){ }
    	};
    	
    	@Override
    	protected boolean isRouteDisplayed() {
    	return false;
    	}
}