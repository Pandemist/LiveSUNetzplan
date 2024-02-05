package svgTester.twitterHandling;

import svgTester.struct.Report;
import svgTester.twitterHandling.SBahn.sBahnController;
import twitter4j.Status;

import java.util.ArrayList;
import java.util.List;

import static svgTester.utils.twitterUtils.getTimeLine;

public class TwitterController {

    /**
     * Methode to gets Twittertimelines and send them to get reports
     * @return ArrayList of reports
     */
    public static ArrayList<Report> getReports() {
        ArrayList<Report> reportList = new ArrayList<>();

        List<Status> sBahnTweets = getTimeLine("SBahnBerlin");
        for(Status s : sBahnTweets) {
            reportList.addAll(sBahnController.getTwitterReports(s));
        }

        List<Status> bvgTweets = getTimeLine("BVG_Ubahn");
        for(Status s : bvgTweets) {
            reportList.addAll(svgTester.twitterHandling.SBahn.sBahnController.getTwitterReports(s));
        }
    //    reportList.removeAll(Collections.singleton(null));
        return reportList;
    }


}
