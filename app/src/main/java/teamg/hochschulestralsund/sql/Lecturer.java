package teamg.hochschulestralsund.sql;

/**
 * Created by Stas Roschal on 11.05.18.
 */

public class Lecturer {

    public String forename;
    public String surname;
    public String academic_title;
    public String telephone;
    public String mail;
    public long id;

    public Lecturer() {

    }

    public Lecturer(String forename, String surname, String academic_title, String telephone, String mail) {
        this.forename = forename;
        this.surname = surname;
        this.academic_title = academic_title;
        this.telephone = telephone;
        this.mail = mail;
    }

    public Lecturer(long id, String forename, String surname, String academic_title, String telephone, String mail) {
        this.id = id;
        this.forename = forename;
        this.surname = surname;
        this.academic_title = academic_title;
        this.telephone = telephone;
        this.mail = mail;
    }

    @Override
    public String toString()
    {
        String text = "";

        if(!academic_title.isEmpty()) {
            text += academic_title + " ";
        }

        if(!forename.isEmpty())
            text += forename + " ";

        if(!surname.isEmpty())
            text += surname;

        return text;
    }
}