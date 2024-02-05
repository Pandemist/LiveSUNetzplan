package svgTester;

import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.ArrayList;

import svgTester.struct.Report;
import svgTester.svg.SvgLexer;

import static svgTester.twitterHandling.TwitterController.getReports;

public class Main {

	/*
	 *  TODO:
	 *  - Rotate Corsses correct over the Station
	 *  - Better Handling of S-Bahn Junktions. Layers has to on to of both directions.
	 *  - Stroke Texts
	 */

    /**
     * Mainmethode which first gets all reports und later brings the given report information into the svg blocks
     * @param args Inputargs: 0: .svg inputfile 1: path and name for the updated file
     */
	public static void main(String[] args) {
		if(args.length<2) {
			System.exit(-1);
		}
		SvgLexer lex = new SvgLexer(args[0]);

		ArrayList<Report> reportList = new ArrayList<>();
		reportList.addAll(svgTester.webPages.sBahn.pageManager.mainPageAnalyse("http://www.s-bahn-berlin.de/"));
		reportList.addAll(svgTester.webPages.bvg.pageManager.mainPageAnalyse("http://www.bvg.de/de/Fahrinfo/Verkehrsmeldungen"));
		reportList.addAll(getReports());

		Date now = new Date();
		ArrayList<Report> timedReports = new ArrayList<>();
		for(Report r : reportList) {
		    //Function to check if something is now active
			if(r.getStartDate().before(now) && r.getEndDate().after(now)) {
				timedReports.add(r);
			}
		}
		for(Report r : timedReports) {
			r.setStartStationShort(lex.getContraction(r.getStartStation_full()));
			r.setViaStationShort(lex.getContraction(r.getViaStation_full()));
			r.setDestStationShort(lex.getContraction(r.getDestStation_full()));
		}

		for(Report r : timedReports) {
			r.addChanges(lex.findAWay(r.getLine(), r.getStartStation(), r.getViaStation(), r.getDestStation()));
		}

		for(Report r : timedReports) {
			lex.makeChanges(r.getLine(), r.getChanges(), r.getProblem());
		}
		lex.saveToFile(args[1]+".svg");
	}
}
