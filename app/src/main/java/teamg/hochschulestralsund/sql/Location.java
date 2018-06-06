package teamg.hochschulestralsund.sql;

/**
 * Created by Stas Roschal on 12.05.18.
 */

public class Location {
    public long id;
    public String house = "";
    public String room = "";
    public String name = "";

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

        if(!house.isEmpty()) {
            text += house;
        }

        if(!room.isEmpty()){
            text += "/" + room;
        }

        if(!name.isEmpty()){
            text += " - " + name;
        }


        return text;
    }

}