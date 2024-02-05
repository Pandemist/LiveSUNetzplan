package svgTester.utils;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class regexHelper {
    /**
     * Methode to match a regex with an String
     * @param input Input text
     * @param pattern Regluar expression to search in input
     * @return ArrayList of all occurs
     */
    public static ArrayList<String> findConstructions(String input, String pattern) {
        ArrayList<String> output = new ArrayList<>();
        Pattern pat = Pattern.compile(pattern);

        Matcher mat = pat.matcher(input);

        while (mat.find()) {
//			System.out.println("Match: " + mat.group());
            output.add(mat.group());
        }
        return output;
    }
}
