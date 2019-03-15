package forms;

import java.util.ArrayList;

public class Util {
    public static String formatId(int id) {
        return String.format("%04d", id);
    }

    public static ArrayList<String> getProductStatusList() {
        ArrayList<String> sts = new ArrayList<>();
        sts.add("Any");
        sts.add("Active");
        sts.add("Disabled");

        return sts;
    }

    public static String statusToString(int status) {
        return getProductStatusList().get(status);
    }
}
