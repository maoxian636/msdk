package com.msdk.xsdk.utils;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class XMapLanguage {
    private XMapLanguage() {
    }

    private static final Map<String, String> languageMap = new HashMap<>();

    static {
        languageMap.put("en", "1");
        languageMap.put("pt", "2");
        languageMap.put("zh", "3");
        languageMap.put("zh-TW", "4");
        languageMap.put("vi", "5");
        languageMap.put("id", "6");
        languageMap.put("th", "7");
    }
    public static String mapLanguageToCode() {
        Locale currentLocale = Locale.getDefault();
        String languageCode = languageMap.get(currentLocale.getLanguage());
        if (languageCode != null) {
            return languageCode;
        } else {
            return "0";
        }
    }
}
