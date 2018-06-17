package teamg.hochschulestralsund.sql;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by ghostgate on 14.05.18.
 */

public class DefaultSQLData {
    public static ArrayList<Location> locations;
    public CustomSQL customSQL;

    public DefaultSQLData(Context context) {
        customSQL = new CustomSQL(context);

        initLocations();
        addDefaultLocations();

        customSQL.close();
    }

    /**
     * Erzeugt eine Liste mit allen wichtigen Räumen für den Fachbereaich ETI
     */
    public void initLocations() {
        locations = new ArrayList<>();

        /* Haus 4 */
        /* Keller */
        locations.add(new Location("4", "010", ""));
        locations.add(new Location("4", "030", ""));

        /* Erdgeschoss */
        locations.add(new Location("4", "110", ""));
        locations.add(new Location("4", "112", ""));
        locations.add(new Location("4", "113", ""));
        locations.add(new Location("4", "114", ""));
        locations.add(new Location("4", "115", ""));
        locations.add(new Location("4", "116", ""));
        locations.add(new Location("4", "117", ""));
        locations.add(new Location("4", "119", ""));
        locations.add(new Location("4", "136", ""));

        /* 1.Etage */
        locations.add(new Location("4", "210", ""));
        locations.add(new Location("4", "213", ""));
        locations.add(new Location("4", "214", ""));
        locations.add(new Location("4", "215-216", ""));
        locations.add(new Location("4", "217", ""));
        locations.add(new Location("4", "219", ""));
        locations.add(new Location("4", "220", ""));
        locations.add(new Location("4", "221", ""));
        locations.add(new Location("4", "223", ""));
        locations.add(new Location("4", "224", ""));
        locations.add(new Location("4", "225", ""));
        locations.add(new Location("4", "230", ""));
        locations.add(new Location("4", "231", ""));

        /* 2.Etage */
        locations.add(new Location("4", "302", ""));
        locations.add(new Location("4", "307", ""));
        locations.add(new Location("4", "308", ""));
        locations.add(new Location("4", "310", ""));
        locations.add(new Location("4", "312", ""));
        locations.add(new Location("4", "314", "314U"));
        locations.add(new Location("4", "316", ""));
        locations.add(new Location("4", "317", ""));
        locations.add(new Location("4", "317a", ""));
        locations.add(new Location("4", "319", ""));
        locations.add(new Location("4", "320", ""));
        locations.add(new Location("4", "322", ""));
        locations.add(new Location("4", "323", ""));
        locations.add(new Location("4", "324", ""));
        locations.add(new Location("4", "HS3", "Hörsaal 3"));
        locations.add(new Location("4", "HS4", "Hörsaal 4"));
        locations.add(new Location("4", "HS5", "Hörsaal 5"));
        locations.add(new Location("4", "HS6", "Hörsael 6"));
        locations.add(new Location("4", "HS7", "Hörsaal 7"));
        locations.add(new Location("4", "HS8", "Hörsaal 8"));

        /* Haus 5 */
        locations.add(new Location("5", "HS1", "Hörsaal 1"));
        locations.add(new Location("5", "HS2", "Hörsaal 2"));

        /* Haus 7 */
        locations.add(new Location("7", "119", ""));
        locations.add(new Location("7", "205", ""));
        locations.add(new Location("7", "208", ""));
        locations.add(new Location("7", "209", ""));
        locations.add(new Location("7", "210", ""));
        locations.add(new Location("7", "211", ""));
    }

    public void addDefaultLectureTimes() {

    }

    /**
     * Fügt die Räume aus der Raumliste der Datenbank hinzu, falls sie noch nicht existieren
     */
    public void addDefaultLocations() {
        for (int i = 0; i < locations.size(); i++) {
            customSQL.addLocationIfNotExist(locations.get(i));
        }
    }
}
