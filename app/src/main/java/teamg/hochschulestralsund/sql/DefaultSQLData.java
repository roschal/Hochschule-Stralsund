package teamg.hochschulestralsund.sql;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by ghostgate on 14.05.18.
 */

public class DefaultSQLData {
    public CustomSQL customSQL;

    public static ArrayList<Location> locations;

    public DefaultSQLData(Context context) {
        customSQL = new CustomSQL(context);

        initLocations();
        addDefaultLocations();
    }

    public void initLocations() {
        locations = new ArrayList<>();

        /* house 4 */
        locations.add(new Location("4", "221"));
        locations.add(new Location("4", "222"));
        locations.add(new Location("4", "223"));

        locations.add(new Location("4", "321"));
        locations.add(new Location("4", "322"));
        locations.add(new Location("4", "323"));
    }

    public void addDefaultLectureTimes() {

    }

    public void addDefaultLocations() {
        for(int i = 0; i < locations.size(); i++) {
            customSQL.addLocationIfNotExist(locations.get(i));
        }
    }
}
