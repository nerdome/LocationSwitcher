package de.adornis.de.locationswitcher;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fightcookie on 10/12/2014.
 */
public class Main extends Activity implements
		GoogleApiClient.ConnectionCallbacks,
		GoogleApiClient.OnConnectionFailedListener,
		LocationListener {


	private final String TAG = "Main";

	private TextView mLocationView;

	private GoogleApiClient mGoogleApiClient;

	private final int GEOFENCE_REQUEST = 1;

	private double testLatitude = 0;

	private double testLongitude = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		mLocationView = (TextView) findViewById(R.id.mainTextBox);

		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addApi(LocationServices.API)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.build();
	}

	@Override
	protected void onStart() {
		super.onStart();
		// Connect the client.
		mGoogleApiClient.connect();
	}

	@Override
	protected void onStop() {
		// Disconnecting the client invalidates it.
		mGoogleApiClient.disconnect();
		super.onStop();
	}

	@Override
	public void onConnected(Bundle bundle) {
		Log.e(TAG, "onConnected");


		LocationRequest mLocationRequest = LocationRequest.create();
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		mLocationRequest.setInterval(30 * 1000); // Update location every second

		LocationServices.FusedLocationApi.requestLocationUpdates(
				mGoogleApiClient, mLocationRequest, this);

		Geofence.Builder geofenceBuilder = new Geofence.Builder();
		Geofence homeGeofence = geofenceBuilder
				.setRequestId("Home") //name for geofence
				.setCircularRegion(testLatitude, testLongitude, 500) //set center and radius of geofence
				.setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
				.setNotificationResponsiveness(5) //time to be notified after geofence event
				.setExpirationDuration(Geofence.NEVER_EXPIRE)
				.build();
//				.setLoiteringDelay(5 * 60 * 1000) //time it must stay in geofence for TRANSITION_DWELL event to be triggered, not used atm!

		ArrayList<Geofence> geofences = new ArrayList<>(1);
		geofences.add(homeGeofence);


		Intent broadcastIntent = new Intent(this, GeofenceReceiver.class);
		PendingIntent geofencePendingIntent = PendingIntent.getBroadcast(this, GEOFENCE_REQUEST, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		LocationServices.GeofencingApi.addGeofences(mGoogleApiClient, geofences, geofencePendingIntent);
	}

	@Override
	public void onConnectionSuspended(int i) {
		Log.i(TAG, "GoogleApiClient connection has been suspend");
	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		Log.i(TAG, "GoogleApiClient connection has failed");
	}

	@Override
	public void onLocationChanged(Location location) {
		Log.e(TAG, location.toString());
//		Log.e(TAG + "last loc", LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient).toString());
		mLocationView.setText("Location received: " + location.toString());
	}
}