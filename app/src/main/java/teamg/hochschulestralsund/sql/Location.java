package teamg.hochschulestralsund.sql;

/**
 * Created by Stas Roschal on 12.05.18.
 */

public class Location {
    public long id;
    public String house;
    public String room;

    public Location(String house, String room) {
        this.house = house;
        this.room = room;
    }

    public Location(long id, String house, String room) {
        this.id = id;
        this.house = house;
        this.room = room;
    }


    @Override
    public String toString() {
        String text = "";

        if(!house.isEmpty()) {
            text += house;
        }

        if(!room.isEmpty())
            text += "/" + room;

        return text;
    }

}