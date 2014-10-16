package de.adornis.de.locationswitcher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;

/**
 * Created by fightcookie on 10/14/2014.
 */
public class GeofenceReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		wifiManager.setWifiEnabled(true);
	}
}
