package shai.classwork3;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity implements LocationListener {

    //Location Manager Constants
    private final String PROVIDER = LocationManager.GPS_PROVIDER;
    private final long MIN_TIME = 10;
    private final float MIN_DISTANCE = 5;

    //location manager
    private LocationManager _locationManager;
    private PermissionManager _permissionManager;

    //Text View
    private TextView _my_location_textView;
    private TextView _sinema_city_distance_textView;


    //================================================================
    //                      On Create
    //================================================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Location Manager definition
        _locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        //Text View definition
        _my_location_textView          = (TextView) findViewById(R.id.my_location_textView);
        _sinema_city_distance_textView = (TextView) findViewById(R.id.sinema_city_distance_textView);

        //Set Permission Manager (for android Marshmallow)
        _permissionManager = getPermissionManager();
    }


    //================================================================
    //                      Print Location
    //================================================================
    private void printLocation(Location location)
    {
        String myLocation = "";

        if (location != null)
            location = getLastKnownLocation();


        if (location != null)
        {
            myLocation += "Lat:  " + location.getLatitude() + "\n";
            myLocation += "Long: " + location.getLongitude();
        }
        else
        {
            myLocation = "Loading location...";
        }

        _my_location_textView.setText(myLocation);
    }

    private Location getLastKnownLocation()
    {
        try
        {
            return _locationManager.getLastKnownLocation(PROVIDER);
        }
        catch (SecurityException e)
        {
            printToast("Cant get the last known location");
        }

        return null;
    }


    //================================================================
    //                      Distance
    //================================================================
    private void printDistance(Location location)
    {
        float sinemaCityDistance = distanceFromSinemaCity(location);

        String distance = "You Are " + sinemaCityDistance + " Meters From Cinema City";

        _sinema_city_distance_textView.setText(distance);
    }

    private float distanceFromSinemaCity(Location myLocation)
    {
        Location sinemaCity = new Location("");
        sinemaCity.setLatitude(31.782780);
        sinemaCity.setLongitude(35.203695);

        return myLocation.distanceTo(sinemaCity);   //Distance in meters
    }



    //================================================================
    //                  On Location Changed
    //================================================================
    @Override
    public void onLocationChanged(Location location)
    {
        printLocation(location);

        printDistance(location);
    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras)
    {

    }

    @Override
    public void onProviderEnabled(String provider)
    {

    }

    @Override
    public void onProviderDisabled(String provider)
    {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String [] permissions, int [] grantResaults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResaults);
        _permissionManager.onRequestPermissionsResult(requestCode, permissions, grantResaults);
    }




    //================================================================
    //                      Set Permission Manager
    //================================================================
    private PermissionManager getPermissionManager()
    {
        return new PermissionManager(this, new PermissionManager.OnPermissionListener()
            {
                @Override
                public void OnPermissionChanged(boolean permissionGranted)
                {
                    if(permissionGranted)
                    {
                        onPermissionGranted();
                    }
                    else
                    {
                        printToast("GPS permission not granted!");
                    }

                }
            });
    }


    public void onPermissionGranted()
    {
        updateLocation(PROVIDER, MIN_TIME, MIN_DISTANCE, this);

        printLocation(getLastKnownLocation());

        printToast("Permission Granted");
    }


    //================================================================
    //                      Update Location
    //================================================================
    private void updateLocation(String provider, long minTime, float minDistance, LocationListener listener)
    {
        try
        {
            _locationManager.requestLocationUpdates(provider, minTime, minDistance, listener);
        }
        catch (SecurityException e)
        {

        }
    }


    //================================================================
    //                      Available Check
    //================================================================
    private boolean isGPSAvailable()
    {
        return _locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private boolean isWIFIAvailable()
    {
        return _locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    //================================================================
    //                      Toast
    //================================================================
    private void printToast(String str)
    {
        Toast.makeText(MainActivity.this, str, Toast.LENGTH_SHORT).show();
    }


}
