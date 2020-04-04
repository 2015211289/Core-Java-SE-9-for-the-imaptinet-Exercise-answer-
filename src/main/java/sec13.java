import java.nio.charset.Charset;
import java.text.Collator;
import java.text.Normalizer;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.format.TextStyle;
import java.util.*;
import java.util.prefs.Preferences;

public class sec13 {

    //exercise1
    static void exercise1() {
        FormatStyle style = FormatStyle.FULL;
        DateTimeFormatter dateTimeFormatter =
                DateTimeFormatter.ofLocalizedDateTime(style).withLocale(Locale.FRANCE);
        ZonedDateTime zonedDateTime = ZonedDateTime.now();
        System.out.println(dateTimeFormatter.format(zonedDateTime));
        dateTimeFormatter =
                DateTimeFormatter.ofLocalizedDateTime(style).withLocale(Locale.CHINA);
        System.out.println(dateTimeFormatter.format(zonedDateTime));
        dateTimeFormatter =
                DateTimeFormatter.ofLocalizedDateTime(style).withLocale(
                        new Locale.Builder().setLanguage("th").setRegion("TH")
                                .setExtension('u', "nu-thai").build());
        System.out.println(dateTimeFormatter.format(zonedDateTime));

    }

    //exercise2
    static void exercise2() {
        Locale[] locales = Locale.getAvailableLocales();
        for (Locale locale : locales) {
            NumberFormat format =
                    NumberFormat.getNumberInstance(locale);
            double amt = 123.45678;
            String result = format.format(amt);
            if (result.matches("[^\\d]+"))
                System.out.println(locale.getDisplayName());
        }
    }

    //exercise3
    static void exercise3() {
        Locale[] locales = Locale.getAvailableLocales();
        FormatStyle style = FormatStyle.SHORT;
        for (Locale locale : locales) {
            DateTimeFormatter dateFormatter =
                    DateTimeFormatter.ofLocalizedDate(style).withLocale(
                            locale);
            LocalDate localDate = LocalDate.of(2020, 4, 30);
            String result = dateFormatter.format(localDate);
            String[] s = result.split("/");
            if (s[0].equals("4")) System.out.println(locale.getDisplayName());
        }
    }

    //exercise4
    static void exercise4() {
        Locale[] locales = Locale.getAvailableLocales();
        for (int i = 0; i < locales.length; i++) {
            List<String> words = new ArrayList<>();
            for (int j = 0; j < locales.length; j++) {
                words.add(locales[j].getDisplayName(locales[i]));
                System.out.println(locales[j].getDisplayName(locales[i]));
            }
            Collator coll = Collator.getInstance(locales[i]);
            words.sort(coll);
            for (int k = 0; k < words.size() - 1; k++) {
                if (words.get(i).equals(words.get(i + 1))) words.remove(i);
            }
            System.out.println(words);
        }
    }

    //exercise5
    static void exercise5() {
        Locale[] locales = Locale.getAvailableLocales();
        Set<Currency> currencies = Currency.getAvailableCurrencies();
        for (int i = 0; i < locales.length; i++) {
            List<String> currency = new ArrayList<>();
            for (Currency c : currencies) {
                System.out.println(c.getDisplayName(locales[i]));
                currency.add(c.getDisplayName(locales[i]));
            }
            Collator coll = Collator.getInstance(locales[i]);
            currency.sort(coll);
            for (int k = 0; k < currency.size() - 1; k++) {
                if (currency.get(i).equals(currency.get(i + 1)))
                    currency.remove(i);
            }
            System.out.println(currency);
        }
    }

    //exercise6
    static void exercise6() {
        Locale[] locales = Locale.getAvailableLocales();
        Set<Currency> currencies = Currency.getAvailableCurrencies();
        for (Currency c : currencies) {
            Set<String> currency = new HashSet<>();
            for (int i = 0; i < locales.length; i++) {
                String s = c.getSymbol(locales[i]);
                if (!currency.contains(s) && currency.size() != 0)
                    System.out.println(c.getDisplayName());
                else currency.add(s);
            }
        }
    }

    //exercise7
    static void exercise7() {
        Locale[] locales = Locale.getAvailableLocales();
        for (Month m : Month.values()) {
            for (Locale local : locales) {
                String s = m.getDisplayName(TextStyle.FULL_STANDALONE, local);
                String t = m.getDisplayName(TextStyle.FULL, local);
                if (!s.equals(t) && !s.matches("\\d+")) {
                    System.out.println(m);
                    break;
                }
            }
        }
    }

    //exercise8
    static void exercise8() {
        int[] codepoints = new int[1];
        while (Character.isValidCodePoint(codepoints[0])) {
            String s = new String(codepoints, 0, 1);
            String norm = Normalizer.normalize(s, Normalizer.Form.NFKC);
            String norm2 = Normalizer.normalize(s, Normalizer.Form.NFKD);
            char[] c1 = norm.toCharArray();
            char[] c2 = norm2.toCharArray();
            if (c1.length >= 2 || c2.length >= 2) {
                System.out.println(s);
            }
            codepoints[0]++;
        }
    }
    /*
    不是所有的unicode都能显示，因为扩展unicode的字体在系统不一定存在。
     */

    //exercise9
    static void exercise9() {
        ResourceBundle res = ResourceBundle.getBundle("messages", Locale.GERMANY);
        String hello = res.getString("hello");
        String thank = res.getString("thank");
        System.out.println(hello + " " + thank);
        res = ResourceBundle.getBundle("messages",
                Locale.getDefault(Locale.Category.DISPLAY));
        hello = res.getString("hello");
        thank = res.getString("thank");
        System.out.println(hello + " " + thank);
    }

    //exercise10
    static void exercise10(Locale locale) {
        Map<String, Charset> map = Charset.availableCharsets();
        map.forEach((s, c) -> {
            System.out.println(c.displayName(locale));
        });
    }

    //exercise11
    static class PaperDisplay {
        static public Map<String, Map<String, Double>> map;

        static {
            map = new HashMap<>();
            for (int i = 0; i <= 10; i++) {
                Map<String, Double> A = new HashMap<>();
                A.put("height", Math.pow(2, 0.25 - (double) i / 2));
                A.put("width", Math.pow(2, -0.25 - (double) i / 2));
                map.put("A" + i, A);
            }
            for (int i = 0; i <= 10; i++) {
                Map<String, Double> B = new HashMap<>();
                B.put("height", Math.pow(2, 0.5 - (double) i / 2));
                B.put("width", Math.pow(2, -(double) i / 2));
                map.put("B" + i, B);
            }
            for (int i = 0; i <= 10; i++) {
                Map<String, Double> C = new HashMap<>();
                C.put("height", Math.pow(2, (double) 3 / 8 - (double) i / 2));
                C.put("width", Math.pow(2, -0.125 - (double) i / 2));
                map.put("C" + i, C);
            }
        }

        private double width;
        private double height;

        public PaperDisplay(Locale locale, String type) {
            Preferences root = Preferences.userRoot();
            Preferences node = root.node("/IdeaProjects/test");
            String scale = node.get(locale.toLanguageTag(), "");
            if (scale.equals("mm")) {
                Map<String, Double> m = map.get(type);
                this.width = m.get("width") * 1000;
                this.height = m.get("height") * 1000;
            } else if (scale.equals("in")) {
                Map<String, Double> m = map.get(type);
                this.width = m.get("width") * 1000 / 25.4;
                this.height = m.get("height") * 1000 / 25.4;
            } else {
                Map<String, Double> m = map.get(type);
                this.width = m.get("width");
                this.height = m.get("height");
            }
        }

        public double getWidth() {
            return width;
        }

        public double getHeight() {
            return height;
        }
    }

}
