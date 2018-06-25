package teamg.hochschulestralsund.sql;

/**
 * @author Stas Roschal
 *
 * Klasse für die DB-Objekte der Personen (Mitarbeiter der HOST)
 */

public class Person {
    public long id = -1;

    public String forename = "";
    public String surname = "";
    public String academic_title = "";
    public String telephone = "";
    public String mail = "";
    public String person_picture_path = "";

    public Person() {

    }

    public Person(String forename, String surname, String academic_title, String telephone, String mail) {
        this.forename = forename;
        this.surname = surname;
        this.academic_title = academic_title;
        this.telephone = telephone;
        this.mail = mail;
    }

    public Person(long id, String forename, String surname, String academic_title, String telephone, String mail) {
        this.id = id;
        this.forename = forename;
        this.surname = surname;
        this.academic_title = academic_title;
        this.telephone = telephone;
        this.mail = mail;
    }

    @Override
    public String toString() {
        String text = "";

        if (academic_title != null && !academic_title.isEmpty()) {
            text += academic_title + " ";
        }

        if (!forename.isEmpty())
            text += forename + " ";

        if (!surname.isEmpty())
            text += surname;

        return text;
    }
}