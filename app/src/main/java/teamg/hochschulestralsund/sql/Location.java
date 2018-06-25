package teamg.hochschulestralsund.sql;

/**
 * @author Stas Roschal
 *
 * Klasse für die DB-Objekte der Veranstaltungsorte
 *
 */

public class Location {
    public long id = -1;
    public String house = "";
    public String room = "";
    public String name = "";

    public Location() {

    }

    public Location(String house, String room, String name) {
        this.house = house;
        this.room = room;
        this.name = name;
    }

    public Location(long id, String house, String room, String name) {
        this.id = id;
        this.house = house;
        this.room = room;
        this.name = name;
    }


    @Override
    public String toString() {
        String text = "";

        if (!house.isEmpty()) {
            text += house;
        }

        if (!room.isEmpty()) {
            text += "/" + room;
        }

        if (!name.isEmpty()) {
            text += " - " + name;
        }

        return text;
    }
}