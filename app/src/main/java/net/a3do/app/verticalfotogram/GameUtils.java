package net.a3do.app.verticalfotogram;

import org.json.JSONArray;
import org.json.JSONException;

public class GameUtils {
    public static boolean checkTitle(JSONArray titleArray, String titleToCheck) {
        boolean out = false;

        try {
            String realTitle = titleArray.getJSONObject(0).getString("value").toLowerCase();
            titleToCheck = titleToCheck.toLowerCase();
            if (realTitle.equals(titleToCheck)) {
                out = true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return out;
    }
}
