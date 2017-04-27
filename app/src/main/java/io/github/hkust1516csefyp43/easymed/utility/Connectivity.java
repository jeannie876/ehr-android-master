package io.github.hkust1516csefyp43.easymed.utility;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * Check device's network connectivity and speed (kind of)
 * @author emil http://stackoverflow.com/users/220710/emil
 *         P.S. ALWAYS give getApplicationContext()
 */
public class Connectivity {

  /**
   * Get the network info
   *
   * @param context
   * @return
   */
  public static NetworkInfo getNetworkInfo(Context context) {
    ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    return cm.getActiveNetworkInfo();
  }

  /**
   * Check if there is any connectivity
   *
   * @param context
   * @return true or false
   */
  public static boolean isConnected(Context context) {
    NetworkInfo info = Connectivity.getNetworkInfo(context);
    return (info != null && info.isConnected());
  }

  /**
   * Check if there is any connectivity to a Wifi network
   *
   * @param context
   * @return true or false
   */
  public static boolean isConnectedWifi(Context context) {
    NetworkInfo info = Connectivity.getNetworkInfo(context);
    return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_WIFI);
  }

  /**
   * Get the SSID of Wi-Fi connection
   *
   * @param context
   * @return
   */
  public static String ConnectedWifiSSID(Context context) {
    if (isConnectedWifi(context)) {
      WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
      WifiInfo wifiInfo = wifiManager.getConnectionInfo();
      return wifiInfo.getSSID();
    } else {
      return null;
    }
  }

  /**
   * Check if there is any connectivity to a mobile network
   *
   * @param context
   * @return
   */
  public static boolean isConnectedMobile(Context context) {
    NetworkInfo info = Connectivity.getNetworkInfo(context);
    return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_MOBILE);
  }

  /**
   * Check if there is fast connectivity
   *
   * @param context
   * @return
   */
  public static boolean isConnectedFast(Context context) {
    NetworkInfo info = Connectivity.getNetworkInfo(context);
    return (info != null && info.isConnected() && Connectivity.isConnectionFast(info.getType(), info.getSubtype()));
  }

  /**
   * Check if the connection is fast
   *
   * @param type
   * @param subType
   * @return
   */
  public static boolean isConnectionFast(int type, int subType) {
    if (type == ConnectivityManager.TYPE_WIFI) {
      return true;                                        //Is it?
    } else if (type == ConnectivityManager.TYPE_MOBILE) {
      switch (subType) {
        case TelephonyManager.NETWORK_TYPE_LTE:         // ~ 10+ Mbps, API level 11
        case TelephonyManager.NETWORK_TYPE_HSPAP:       // ~ 10-20 Mbps, API level 13
        case TelephonyManager.NETWORK_TYPE_EVDO_B:      // ~ 5 Mbps, API level 9
        case TelephonyManager.NETWORK_TYPE_HSDPA:       // ~ 2-14 Mbps
        case TelephonyManager.NETWORK_TYPE_HSUPA:       // ~ 1-23 Mbps
        case TelephonyManager.NETWORK_TYPE_EHRPD:       // ~ 1-2 Mbps, API level 11
        case TelephonyManager.NETWORK_TYPE_HSPA:        // ~ 700-1700 kbps
          return true;
        case TelephonyManager.NETWORK_TYPE_EVDO_A:      // ~ 600-1400 kbps
        case TelephonyManager.NETWORK_TYPE_UMTS:        // ~ 400-7000 kbps
        case TelephonyManager.NETWORK_TYPE_EVDO_0:      // ~ 400-1000 kbps
        case TelephonyManager.NETWORK_TYPE_GPRS:        // ~ 100 kbps
        case TelephonyManager.NETWORK_TYPE_EDGE:        // ~ 50-100 kbps
        case TelephonyManager.NETWORK_TYPE_1xRTT:       // ~ 50-100 kbps
        case TelephonyManager.NETWORK_TYPE_IDEN:        // ~ 25 kbps, API level 8
        case TelephonyManager.NETWORK_TYPE_CDMA:        // ~ 14-64 kbps
        case TelephonyManager.NETWORK_TYPE_UNKNOWN:     // Unknown
        default:
          return false;
      }
    } else {
      return false;
    }
  }

  public static boolean isReachableByTcp(String host, int port, int timeout) {
    try {
      Socket socket = new Socket();
      SocketAddress socketAddress = new InetSocketAddress(host, port);
      socket.connect(socketAddress, timeout);
      socket.close();
      return true;
    } catch (IOException e) {
      return false;
    }
  }

}