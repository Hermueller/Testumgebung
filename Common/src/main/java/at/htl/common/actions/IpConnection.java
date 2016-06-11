package at.htl.common.actions;

import at.htl.common.fx.FxUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;

/**
 * @timeline IpConnection
 * 22.05.2016: PHI 015  created class and improved the isIpReachable-method.
 * 22.05.2016: PHI 002  created method to check internet-connection.
 */

/**
 * executes system commands.
 *
 * @author Philipp Hermüller
 */
public class IpConnection {

    /**
     * runs a system command and analyses the result of the command.
     *
     * @param ip             the ip to ping.
     * @param errorPopUp     only creates a popUp-Window if the ping failed.
     * @param successPopUp   only creates a popUp-Window if the ping was successful.
     * @return               boolean if the IP was successfully pinged or not.
     */
    public static boolean isIpReachable(String ip, boolean errorPopUp, boolean successPopUp) {
        boolean connected = false;

        try {
            String msg;

            InetAddress address = InetAddress.getByName(ip);
            boolean reachable = address.isReachable(2000);

            if (!reachable) {
                msg = "can't ping the following IP: " + ip;
            } else {
                msg = "IP pinged successfully!!";
                connected = true;
            }

            if ((errorPopUp && !connected) || (successPopUp && connected)) {
                FxUtils.showPopUp(msg, connected);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return connected;
    }

    /**
     * checks if the application has internet connection.
     *
     * @return  internet-connectivity.
     */
    public static boolean checkInternetConnection() {
        return isIpReachable("www.google.com", false, false);
    }
}
