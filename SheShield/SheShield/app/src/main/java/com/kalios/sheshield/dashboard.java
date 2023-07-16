package com.kalios.sheshield;
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

public class dashboard extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 123;
    private static  String SMS_DESTINATION_NUMBER = "8511845528";
    private EditText phoneNumberEditText;
    private Button triggerButton;

    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        phoneNumberEditText = findViewById(R.id.editText_PhoneNumber);
        triggerButton = findViewById(R.id.mic_active);

        triggerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocationAndSendData();
            }
        });

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        createLocationRequest();
        createLocationCallback();
    }

    private void createLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(10000); // 10 seconds
        locationRequest.setFastestInterval(5000); // 5 seconds
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void createLocationCallback() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }

                for (Location location : locationResult.getLocations()) {
                    // Get the latitude and longitude coordinates
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();

                    // Send the coordinates via SMS and email
                    sendSmsWithCoordinates(latitude, longitude);

                    // Remove location updates after obtaining the coordinates
                    fusedLocationProviderClient.removeLocationUpdates(locationCallback);
                }
            }
        };
    }

    private void getLocationAndSendData() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    private void sendSmsWithCoordinates(double latitude, double longitude) {
        SMS_DESTINATION_NUMBER=phoneNumberEditText.getText().toString();
        String message = "Seema\n"+"Need Help!!\n"+"\nCurrent Location: Latitude = " + latitude + ", Longitude = " + longitude;
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(SMS_DESTINATION_NUMBER, null, message, null, null);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocationAndSendData();
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}