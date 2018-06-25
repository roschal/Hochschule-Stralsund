package teamg.hochschulestralsund;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;

import java.util.Locale;

/**
 * Klasse für das setzen der Sprachresourcen.
 */
public class CustomLocale {
    public static void setNewLocale(Context c, String language) {
        updateResources(c, language);
    }

    private static void updateResources(Context context, String language) {
        Locale locale;

        switch (language) {
            case "Deutsch":
                locale = new Locale("de");

                break;

            case "English":
                locale = new Locale("en");
                break;

            default:
                locale = new Locale("de");

                break;
        }

        Locale.setDefault(locale);

        Configuration config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
    }
}