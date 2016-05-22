package at.htl.common.actions;

import at.htl.common.fx.FxUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * @timeline SystemCommands
 * 22.05.2016: PHI 015  created class and improved the runSystemCommand-method.
 */

/**
 * executes system commands.
 *
 * @author Philipp Hermüller
 */
public class SystemCommands {

    /**
     * runs a system command and analyses the result of the command.
     *
     * @param command        is always "ping" command.
     * @param ip             the ip to ping.
     * @param errorPopUp     only creates a popUp-Window if the ping failed.
     * @param successPopUp   only creates a popUp-Window if the ping was successful.
     * @return               boolean if the IP was successfully pinged or not.
     */
    public static boolean runSystemCommand(String command, String ip, boolean errorPopUp, boolean successPopUp) {
        command += ip;
        boolean connected = false;
        try {
            Process p = Runtime.getRuntime().exec(command);
            BufferedReader inputStream = new BufferedReader(
                    new InputStreamReader(p.getInputStream()));

            String s = "";
            double lossPercentage = -1;
            String msg = "";

            while ((s = inputStream.readLine()) != null) {
                if (s.startsWith("---")) {
                    break;
                }
            }
            s = inputStream.readLine();
            if (s != null) {
                String loss = s.split(",")[2];
                lossPercentage = Double.parseDouble(loss.split("%")[0].trim());
            }
            if (lossPercentage > 0 && lossPercentage != 100) {
                msg = lossPercentage + "% received";
            } else if (lossPercentage == 100 || s == null) {
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
        return runSystemCommand("ping -c 2 ", "www.google.com", false, false);
    }
}
