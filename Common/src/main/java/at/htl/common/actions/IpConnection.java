package at.htl.common.actions;

import at.htl.common.fx.FxUtils;
import at.htl.common.io.FileUtils;
import org.apache.logging.log4j.Level;

import java.io.IOException;
import java.net.*;

/**
 * executes system commands.
 *
 * @timeline IpConnection
 * 22.05.2016: PHI 015  created class and improved the isIpReachable-method.
 * 22.05.2016: PHI 002  created method to check internet-connection.
 */
public class IpConnection {

    /**
     * runs a system command and analyses the result of the command.
     *
     * @param ip           the ip to ping.
     * @param errorPopUp   only creates a popUp-Window if the ping failed.
     * @param successPopUp only creates a popUp-Window if the ping was successful.
     * @return boolean if the IP was successfully pinged or not.
     */
    public static boolean isIpReachable(String ip, int port, boolean errorPopUp, boolean successPopUp) {
        boolean connected = true;
        if (ip.isEmpty()) {
            throw new IllegalArgumentException("Please enter an IP address");
//            FxUtils.showPopUp("Please enter an IP address!", false);
        } else {
            try (Socket s = new Socket()){
                SocketAddress sa = new InetSocketAddress(ip, port);
                s.connect(sa, 2000);
            } catch (Exception e) {
                FileUtils.log("isReachable", Level.ERROR,e.getMessage());
                connected = false;
            }

//            if ((errorPopUp && !connected) || (successPopUp && connected)) {
//                FxUtils.showPopUp(msg, connected);
//            }
        }
        return connected;
    }

    /**
     * checks if the application has internet connection.
     *
     * @return internet-connectivity.
     */
    public static boolean checkInternetConnection() {
        //return isIpReachable("www.google.com", false, false);
        try {
            final URL url = new URL("http://www.google.com");
            final URLConnection conn = url.openConnection();
            conn.connect();
            return true;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            return false;
        }
    }
}
