package net.minecraft.client.resource.language;

import java.util.IllegalFormatException;
import java.util.Locale;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Language;

@Environment(value=EnvType.CLIENT)
public class I18n {
    private static volatile Language language = Language.getInstance();

    private I18n() {
    }

    static void setLanguage(Language language) {
        I18n.language = language;
    }

    public static String translate(String key, Object ... args) {
        String string2 = language.get(key);
        try {
            return String.format(Locale.ROOT, string2, args);
        } catch (IllegalFormatException illegalFormatException) {
            return "Format error: " + string2;
        }
    }

    public static boolean hasTranslation(String key) {
        return language.hasTranslation(key);
    }
}

