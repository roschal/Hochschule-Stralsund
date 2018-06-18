package teamg.hochschulestralsund.connect;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import teamg.hochschulestralsund.sql.Meal;

/**
 * Created by Stas Roschal on 12.05.18s.
 */

public class ParserMensa extends AsyncTask<Void, Void, ArrayList<Meal>> {
    private String URL_MENSA = "http://studwerk.fh-stralsund.de/essen/speiseplaene/mensa-stralsund/";
    private static String URL_MENSA_DEFAULT = "http://studwerk.fh-stralsund.de/essen/speiseplaene/mensa-stralsund/";

    private Calendar myCalendar = Calendar.getInstance();
    private Context myContext;

    public ParserMensa(Context context, Calendar calendar) {
        Log.e("calendar", Long.toString(calendar.getTimeInMillis()));
        myContext = context;
        myCalendar.setTimeInMillis(calendar.getTimeInMillis());
    }

    @Override
    protected ArrayList<Meal> doInBackground(Void... voids) {
        ArrayList<Meal> meals = new ArrayList<>();

        Log.e("h", "hi");

        try {
            setURL();
            Log.e("URl", URL_MENSA);
            meals = updateMensa();

            for (int i = 0; i < meals.size(); i++ )
                Log.e("sdfdsf", meals.get(i).meal_title);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return meals;
    }

    //* get source code
    public String getURL(String url) {
        try {
            URL urlLecturers = new URL(url);
            URLConnection urlConnLecturers = urlLecturers.openConnection();

            BufferedReader in = new BufferedReader(new InputStreamReader(
                    urlConnLecturers.getInputStream(), "UTF-8"));

            String inputLine;
            StringBuilder a = new StringBuilder();

            while ((inputLine = in.readLine()) != null)
                a.append(inputLine);

            in.close();

            return a.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    public ArrayList<Meal> updateMensa() {
        ArrayList<Meal> meals= new ArrayList<>();

        String srcMensa = getURL(URL_MENSA);
        srcMensa = srcMensa.trim();

        while (srcMensa.contains("<table class=\"table module-food-table\">")) {
            String tmpStr = srcMensa;
            tmpStr = tmpStr.substring(tmpStr.indexOf("<table class=\"table module-food-table\">") + "<table class=\"table module-food-table\">".length());

            String meal_category = "";

            //* get category
            if (srcMensa.contains("<th>")) {
                tmpStr = tmpStr.substring(tmpStr.indexOf("<th>") + "<th>".length());
                meal_category = tmpStr.substring(0, tmpStr.indexOf("<img src="));
            }

            while (tmpStr.contains("<td style=\"width:70%\">")) {
                Meal meal = new Meal();
                meal.meal_category = meal_category;

                //* meal title
                tmpStr = tmpStr.substring(tmpStr.indexOf("<td style=\"width:70%\">") + "<td style=\"width:70%\">".length());

                if (tmpStr.contains("<div class=\"price hidden-sm hidden-md hidden-lg\">")) {
                    meal.meal_title = tmpStr.substring(0, tmpStr.indexOf("<div class=\"price hidden-sm hidden-md hidden-lg\">"));
                }
                else
                {
                    return meals;
                }

                //* meal ingredients
                while (meal.meal_title.contains("<sup>")) {
                    meal.meal_title += meal.meal_title.substring(meal.meal_title.indexOf("<sup>")+"<sup>".length(), meal.meal_title.indexOf("</sup"));
                    meal.meal_title = meal.meal_title.replace("<sup>.*</sup>;", "");
                }

                Log.e("meal_title", meal.meal_title);
                //* meal price
                tmpStr = tmpStr.substring(tmpStr.indexOf("<td class=\"hidden-xs\" style=\"text-align:center\">") + "<td class=\"hidden-xs\" style=\"text-align:center\">".length());
                String price_student = tmpStr.substring(0, tmpStr.indexOf("&nbsp;&euro;</td>"));
                price_student = price_student.replaceAll(",", ".");
                meal.meal_price_student = Double.valueOf(price_student);

                tmpStr = tmpStr.substring(tmpStr.indexOf("<td class=\"hidden-xs\" style=\"text-align:center\">") + "<td class=\"hidden-xs\" style=\"text-align:center\">".length());
                String price_worker = tmpStr.substring(0, tmpStr.indexOf("&nbsp;&euro;</td>"));
                price_worker = price_worker.replaceAll(",", ".");
                meal.meal_price_worker = Double.valueOf(price_worker);

                tmpStr = tmpStr.substring(tmpStr.indexOf("<td class=\"hidden-xs\" style=\"text-align:center\">") + "<td class=\"hidden-xs\" style=\"text-align:center\">".length());
                String price_guest = tmpStr.substring(0, tmpStr.indexOf("&nbsp;&euro;</td>"));
                price_guest = price_guest.replaceAll(",", ".");
                meal.meal_price_guest = Double.valueOf(price_guest);

                meals.add(meal);
            }

            srcMensa = tmpStr;
        }

        for (int i = 0; i < meals.size(); i++ )
            Log.e("sdfdsf", meals.get(i).meal_title);

        return meals;
    }

    private void setURL() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formatted = simpleDateFormat.format(myCalendar.getTime());

        URL_MENSA = URL_MENSA_DEFAULT + "?datum=" + formatted;
    }
}