package teamg.hochschulestralsund.connect;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import teamg.hochschulestralsund.exception.ParserException;
import teamg.hochschulestralsund.sql.Lecturer;
import teamg.hochschulestralsund.sql.CustomSQL;

/**
 * Created by Stas Roschal on 12.05.18s.
 */

public class Parser extends AsyncTask<Void, Integer, Boolean> {

    private static final String TAG = "Parser.java";
    private static final String URL_LECTURERS = "https://www.hochschule-stralsund.de/host/im-portrait/mitarbeitende/";

    private Context myContext;
    public CustomSQL customSQL;

    public Parser(Context context)
    {
        myContext = context;
        customSQL = new CustomSQL(myContext);
    }

    @Override
    protected Boolean doInBackground(Void... voids)  {
        try {
            this.updateLecturers();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    // get source code of lecturers website
    public String getURL() throws IOException {
        URL urlLecturers = new URL(URL_LECTURERS);
        URLConnection urlConnLecturers = urlLecturers.openConnection();

        BufferedReader in = new BufferedReader(new InputStreamReader(
                urlConnLecturers.getInputStream(), "UTF-8"));

        String inputLine;
        StringBuilder a = new StringBuilder();

        while ((inputLine = in.readLine()) != null)
            a.append(inputLine);

        in.close();

        return a.toString();
    }

    public Boolean updateLecturers() throws Exception {
        String srcLecturers = "";

        try {
            srcLecturers = this.getURL();
        } catch (IOException e) {
            throw e;
        }

        // delete all tabs
        while (srcLecturers.indexOf("\t") != -1) {
            srcLecturers = srcLecturers.replace("\t", "");
        }

        // while there are still persons to parse
        while(srcLecturers.indexOf("<div class=\"grid__column grid__column--xs-9 grid__column--sm-6 grid__column--md-6 grid__column--lg-6 contact-list__person-informations\">") != -1) {
            String tmpStr = srcLecturers;

            // get first person
            tmpStr = tmpStr.substring(tmpStr.indexOf("<div class=\"grid__column grid__column--xs-9 grid__column--sm-6 grid__column--md-6 grid__column--lg-6 contact-list__person-informations\">"));
            tmpStr = tmpStr.substring(0, tmpStr.indexOf("</div></div></div></div>") + "</div></div></div></div>".length());

            // get Name
            String name = tmpStr.substring(tmpStr.indexOf("<h4 class=\"h4-style\">") + "<h4 class=\"h4-style\">".length());
            name = name.substring(0, name.indexOf("</h4>"));

            // if an academic title was found
            String academic_title = "";

            if(name.indexOf("<span class=\"h4-style h4-style--black\">") >= 0)
            {
                // academic title
                academic_title = name.substring(name.indexOf("<span class=\"h4-style h4-style--black\">") + "<span class=\"h4-style h4-style--black\">".length());
                academic_title = academic_title.substring(0, academic_title.indexOf("</span>"));

                // cut the name
                name = name.substring(0, name.indexOf("<span class=\"h4-style h4-style--black\">"));
            }

            // forename, surname
            String forename = name.substring(0, name.indexOf(","));
            String surname = name.substring(name.indexOf(",") + 2);

            ///////////////////////////////////////////////////////////////////////////////////////////////////////////
            // Mail
            String mail = "";

            try
            {
                // no mail found
                if(tmpStr.indexOf("<a class=") == -1)
                {
                    Exception e = new ParserException("no mail available");
                    throw new ParserException("\"<a class=\" not found", e);
                }

                mail = tmpStr.substring(tmpStr.indexOf("<a class=") + 1);

                if(tmpStr.indexOf(">") == -1 ||  mail.indexOf("</a>") == -1)
                {
                    Exception e = new ParserException("no mail available");
                    throw new ParserException("\">\" or \"</a>\" not found", e);
                }

                mail = mail.substring(mail.indexOf(">") + 1);
                mail = mail.substring(0, mail.indexOf("</a>"));
                mail = mail.replace("(at)", "@");
                mail = mail.replace("(dot)", ".");
            }
            catch (ParserException e)
            {
                mail = "";
            }
            catch (Exception e)
            {
                throw e;
            }

            ///////////////////////////////////////////////////////////////////////////////////////////////////////////
            // Telephone
            String telephone = "";

            try
            {
                // no telephone found
                if(tmpStr.indexOf("<div class=\"contact-list__person-phone\">") == -1)
                {
                    Exception e = new ParserException("no telephone available");
                    throw new ParserException("\"<div class=\\\"contact-list__person-phone\\\">\" not found", e);
                }

                telephone = tmpStr.substring(tmpStr.indexOf("<div class=\"contact-list__person-phone\">") + "<div class=\"contact-list__person-phone\">".length());

                if(telephone.indexOf("</div>") == -1)
                {
                    Exception e = new ParserException("no telephone available");
                    throw new ParserException("</div> not found", e);
                }

                telephone = telephone.substring(0, telephone.indexOf("</div>"));
                telephone = telephone.replace("<p>Tel:</p><p>", "");
                telephone = telephone.replace("</p>", "");
            }
            catch (ParserException e)
            {
                telephone = "";
            }
            catch (Exception e)
            {
                throw e;
            }

            srcLecturers = srcLecturers.substring(srcLecturers.indexOf("<div class=\"grid__column grid__column--xs-9 grid__column--sm-6 grid__column--md-6 grid__column--lg-6 contact-list__person-informations\">"));
            srcLecturers = srcLecturers.substring(srcLecturers.indexOf("</div></div></div></div>") + "</div></div></div></div>".length());

            Lecturer lecturer = new Lecturer(forename, surname, academic_title, mail, telephone);
            customSQL.addLecturerIfNotExist(lecturer);
        }

        customSQL.close();
        return true;
    }
}