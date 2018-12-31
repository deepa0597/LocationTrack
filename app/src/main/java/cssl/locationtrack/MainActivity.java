package cssl.locationtrack;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
{
    public String getLoc(String lat, String lon)
    {
        Geocoder g = new Geocoder(MainActivity.this, Locale.ENGLISH);

        List<Address> addressList = null;
        try {
            addressList = g.getFromLocation(Double.parseDouble(lat) , Double.parseDouble(lon) ,1);
            Address address = addressList.get(0);
            String add =
                    address.getCountryName()+" "+
                            address.getAdminArea()+" "+
                            address.getSubAdminArea()+" "+
                            address.getLocality()+" "+
                            address.getSubLocality();
            return add;

        } catch (IOException e) {
            return "Some error while getting address";
        }

    }


    //TextView tvLatitude, tvLongitude, tvTime,tvAddress;
    static String lon;
    static String lat;
    static String add;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getLocationUpdates();

        final SharedPreferences.Editor editor= getSharedPreferences("Mypref",MODE_PRIVATE).edit();

        //lon=tvLongitude.getText().toString();
        if(lon ==null ||lon.equals(""))
            lon="73.093948";
        //tvLongitude.setText(lon);

        //lat =tvLatitude.getText().toString();
        if(lat==null||lat.equals(""))
            lat="19.209401";
        //tvLatitude.setText(lat)
    }

    private void getLocationUpdates() {

        LocationRequest request = new LocationRequest();
        request.setInterval(100);
        request.setFastestInterval(50);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(this);
        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission == PackageManager.PERMISSION_GRANTED) {
            client.requestLocationUpdates(request, new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    Location location = locationResult.getLastLocation();
                    if (location != null) {
                        Log.d("Deepa", "location update " + location.getLatitude() + " " + location.getLatitude());
                        lat=String.valueOf(location.getLatitude());
                        lon=String.valueOf(location.getLongitude());
                        add = getLoc(lat,lon);


                        Log.d("Location",""+ lon +" "+lat +" "+add);
                        Toast.makeText(MainActivity.this, ""+ lon +" "+lat +" "+add, Toast.LENGTH_LONG).show();

                    }
                }
            }, null);

        }
        if(permission != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},105);


        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==105 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
        {
            Toast.makeText(this, "Permission granted.", Toast.LENGTH_SHORT).show();
            getLocationUpdates();

        }
        else
        {
            finish();
        }
    }

}
