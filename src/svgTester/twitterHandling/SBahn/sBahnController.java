package svgTester.twitterHandling.SBahn;

import java_cup.runtime.Symbol;
import org.apache.commons.lang3.time.DateUtils;
import svgTester.struct.Problems;
import svgTester.struct.Report;
import twitter4j.Status;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

public class sBahnController {

    /**
     * Methode to get Reports from a given Tweet
     * @param tweet Tweet from twitter4j api
     * @return ArrayList of reports or empty ArrayList
     */
    public static ArrayList<Report> getTwitterReports(Status tweet) {
        Symbol sym;
        ArrayList<Symbol> tokenList = new ArrayList<>();
        try {
            SLexer slexer = new SLexer(new ByteArrayInputStream(tweet.getText().getBytes(StandardCharsets.UTF_8)));
            for (sym = slexer.next_token(); sym.sym != 0; sym = slexer.next_token()) {
                //    System.out.println("#"+sym.sym+": "+sym.value);
                tokenList.add(sym);
            }
        } catch (Exception e) {
            System.out.println(e);
            assert (false);
        }

        ArrayList<String> lineList = new ArrayList<>();
        Problems problem = getProblem(tweet.getText());

        for (Symbol s : tokenList) {
            if (s.sym == 4) {
                lineList.add(((String) s.value).replace("#", ""));
            }
        }
        Set<String> hs = new HashSet<>();
        hs.addAll(lineList);
        lineList.clear();
        lineList.addAll(hs);

        if (lineList.isEmpty() || problem == Problems.NULL) {
            return new ArrayList<>();
        }

        Date sended = tweet.getCreatedAt();
        Date ended;

        if (problem == Problems.DELAY) {
            ended = DateUtils.addHours(sended, 2);
        } else if (problem == Problems.BLOCKED) {
            ended = DateUtils.addHours(sended, 2);
        } else {
            ended = DateUtils.addHours(sended, 2);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy-HH:mm", Locale.GERMANY);
        String startDate = sdf.format(sended);
        String endDate = sdf.format(ended);

        ArrayList<Report> reportList = new ArrayList<>();
        for (String l : lineList) {
            Report r = new Report();

            r.setLine(l);
            r.setStartStation_full("");
            r.setViaStation_full("");
            r.setEndStation_full("");

            r.addProblem(problem);
            try {
                r.setStartDate(sdf.parse(startDate));
                r.setEndDate(sdf.parse(endDate));
            } catch (Exception e) {
                System.err.println(e);
            }

            r.setBubble("https://twitter.com/SBahnBerlin/status/" + tweet.getId());

            reportList.add(r);
        }

        return reportList;
    }

    /**
     * Methode to match pattern to get the correct Problem
     * @param content String with information
     * @return Matched problem, or Problem.NULL.
     */
    private static Problems getProblem(String content) {
        HashMap<String, Problems> templates = new HashMap<>();
        templates.put("verspätet", Problems.DELAY);
        templates.put("Verspätungen", Problems.DELAY);
        templates.put("zurzeit unterbrochen", Problems.BLOCKED);
        templates.put("unterbrochen", Problems.BLOCKED);
        templates.put("halt in .* entfällt", Problems.STATION_WORK);
        templates.put("hält wieder in", Problems.ENDED);
        templates.put("ist beendet", Problems.ENDED);
        templates.put("zuende", Problems.ENDED);
   //     templates.put("", Problems.);

        for(Map.Entry<String, Problems> p : templates.entrySet()) {
            if(content.contains(p.getKey())) {
                return p.getValue();
            }
        }
        return Problems.NULL;
    }
}
