package svgTester.webPages.bvg;

import svgTester.struct.Problems;
import svgTester.struct.Report;
import svgTester.webPages.getPageContent;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static svgTester.utils.regexHelper.findConstructions;

public class pageManager {

    /**
     * Mainpart of the Pageanalysis.
     * Calls Methodes to extract Time and Date, problemtype, line and Stations and merge this informations to Reports
     * @param src Website url
     * @return List of all created Reports
     */
	public static ArrayList<Report> mainPageAnalyse(String src) {
		ArrayList<Report> reportList = new ArrayList<>();
		String pageContent = getPageContent.getContents(src);
		pageContent = pageContent.
				substring(pageContent.indexOf("\"traffic-overview\""),
						pageContent.lastIndexOf("</table>"));
		ArrayList<String> a = new ArrayList<>();
		a.addAll(findConstructions(pageContent,
				"<tr([^<]|<[^/]|</[^t]|</t[^r]|</tr[^>]|)*</tr>"));
		if(a.size() < 2) return new ArrayList<>();
		a.remove(0);
		for(String s : a) {
			String line = getLine(s);
			if(line == "") continue;
			ArrayList<String> station = getStations(s);
			Problems problem = getProblem(s);
			String time = getTime(s);

			Report r = new Report();

			r.addProblem(problem);
			r.setLine(line);

			if(station.size()!=2) continue;
			r.setStartStation_full(station.get(0).replace("S+U ","").replace("U ", ""));
			r.setViaStation_full("");
			r.setEndStation_full(station.get(1).replace("S+U ","").replace("U ", ""));

			String[] startEndTime = time.split("-");
			if(startEndTime.length!=2) continue;

			try {
				DateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.GERMANY);
				Date startDate = format.parse(startEndTime[0]);
				Date endDate = format.parse(startEndTime[1]);
				r.setStartDate(startDate);
				r.setEndDate(endDate);
			}catch(Exception e) {
				continue;
			}
			r.setBubble("");
			reportList.add(r);
		}

		return reportList;
	}

    /**
     * Methode to extract start and end time and date
     * @param s News with information
     * @return String which contains time and date information
     */
	private static String getTime(String s) {
		ArrayList<String> times = findConstructions(s,
				"von[^/]*</td>");
		if(times.size() < 1) return "";
		String time = times.get(0).replace("von ", "").
				replace("bis ", "").replace("<br>", "-").
				replace("</td>", "").replace("\n", "").
				replaceAll("  ","");
		return time;
	}

    /**
     * Methode which try to match the linenumbers
     * @param s News with information
     * @return Matched lines
     */
	private static String getLine(String s) {
		ArrayList<String> lines = findConstructions(s,
				"U-Bahn U(1|2|3|4|5|55|6|7|8|9)<");
		if(lines.size() < 1) return "";
		String line = lines.get(0).replace("<", "")
				.replace("U-Bahn ", "");
		return line;
	}

    /**
     * Methode which extract the station names
     * @param s News with information
     * @return Arraylist with stationnames
     */
	public static ArrayList<String> getStations(String s) {
		ArrayList<String> stations = findConstructions(s, ">[^<]*</a>");
		if(stations.size() < 1) return new ArrayList<>();
		String station = stations.get(0).replace("</a>", "")
				.replace(">", "");
		return new ArrayList<>(Arrays.asList(station.split("  &#x21c4; ")));
	}

    /**
     * Methode which matches with a couple of Problems
     * @param s News with information
     * @return A matched Problem, or Problem.NULL.
     */
	public static Problems getProblem(String s) {
		HashMap<String, Problems> templates = new HashMap<>();
		templates.put("Sperrung wegen Bauarbeiten", Problems.BLOCKED);
		templates.put("Sperrung wegen Aufzugseinbau", Problems.STATION_WORK);
		templates.put("eines Ausgangs", Problems.STATION_WORK);

		for(Map.Entry<String, Problems> p : templates.entrySet()) {
			if(s.contains(p.getKey())) {
				return p.getValue();
			}
		}
		return Problems.NULL;
	}
}
