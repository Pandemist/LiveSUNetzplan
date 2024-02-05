package svgTester.webPages.sBahn;

import svgTester.struct.Problems;
import svgTester.struct.Report;
import svgTester.webPages.getPageContent;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static svgTester.utils.regexHelper.findConstructions;

public class pageManager {

    /**
     * Methode to get detail website urls and get Reports for them
     * @param src Overview website
     * @return List of Reports
     */
	public static ArrayList<Report> mainPageAnalyse(String src) {
		ArrayList<Report> reportList = new ArrayList<>();
		try {
			String pageContent = getPageContent.getContents(src+"bauinformationen/uebersicht");
			ArrayList<String> a = new ArrayList<>();
			a.addAll(findConstructions(pageContent, "bauinformationen/details/" + "[-_a-zA-Z]+/[0-9]+"));
			for(String s : a) {
				reportList.addAll(getReportFromPage(src+s));
			}
		}catch(Exception e) {
            System.err.println(e);
		}
		return reportList;
	}

    /**
     * Methode to read a File
     * @param path Path to the File
     * @param encoding Charset of the file
     * @return String with the file
     * @throws IOException
     */
	static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}

    /**
     * Methode to create Reports form the given Detailpage
     * @param s Content of a given Page
     * @return ArrayList with all given Reports
     * @throws ParseException
     */
	private static ArrayList<Report> getReportFromPage(String s) throws ParseException {
		String content = getPageContent.getContents(s);

		ArrayList<String> stations = getStations(content);
		ArrayList<String> lines = getLines(content);
		String time = getTime(content);
		Problems problem = getProblem(content);

		//Make Report
		ArrayList<Report> reportList = new ArrayList<>();
		for(String line : lines) {
			for(String station : stations) {
				Report r = new Report();
				String[] splittedStations = station.split(" - ");
				if(splittedStations.length!=2) {
					if(splittedStations.length==1) {
						r.setStartStation_full(splittedStations[0]);
						r.setViaStation_full("");
						r.setEndStation_full(splittedStations[0]);
					}
				}else{
					r.setStartStation_full(splittedStations[0]);
					r.setViaStation_full("");
					r.setEndStation_full(splittedStations[1]);
				}
				//Add Problem
				r.addProblem(problem);

				//Add Line
				r.setLine(line);

				//Add Start and End Time
				String newtime = time.substring(1, time.length()).substring(0, time.length() - 1);
				String[] startEndTime = newtime.split(", ");

				DateFormat format = new SimpleDateFormat("dd.MM.yyyy-HH:mm", Locale.GERMANY);
				Date startDate = format.parse(startEndTime[0]);
				Date endDate = format.parse(startEndTime[startEndTime.length - 1]);
				r.setStartDate(startDate);
				r.setEndDate(endDate);

				r.setBubble("");
				reportList.add(r);
			}
		}
		return reportList;
	}

    /**
     * Methode to match pattern with a given String
     * @param content Given news to find a Problem in
     * @return A matched Problem, or Problems.NULL.
     */
	private static Problems getProblem(String content) {
		ArrayList<String> infos = findConstructions(content,
				"<strong>Infos[^.|.]*</span>");
		if(infos.size()!=1)
			assert false;
		HashMap<String, Problems> templates = new HashMap<>();
		templates.put("Ersatz fahren Busse", Problems.REPLACED);
		templates.put("verspätet", Problems.DELAY);
		templates.put("Bauarbeiten" ,Problems.BLOCKED);
		templates.put("Zugverkehr verändert",Problems.DELAY);
		templates.put("kein S-Bahnverkehr", Problems.BLOCKED);
		templates.put("Ausfall", Problems.DELAY);
		templates.put("Bahnsteigwechsel", Problems.STATION_WORK);
		templates.put("Minutentakt", Problems.DELAY);

		for(Map.Entry<String, Problems> p : templates.entrySet()) {
			if(infos.get(0).contains(p.getKey())) {
				return p.getValue();
			}
		}
		return Problems.NULL;
	}

    /**
     * Methode to fetch start and end, time and date
     * @param content News with information
     * @return A String which contains all information
     */
	private static String getTime(String content) {
		ArrayList<String> helpRawTime = findConstructions(content, "Wann:</strong></label>([^<])*");
		if(helpRawTime.size()!=1)
			assert false;
		String rawTime = helpRawTime.toString().replace("[Wann:</strong></label>","")
				.replace("]","").replace("\n","")
				.replaceAll("[ ]?\\([a-zA-Z]{2}(/[a-zA-Z]{2})?\\)", "")
				.replace(",","");
		ArrayList<String> rawDate = getDates(rawTime);
		ArrayList<String> rawClockTime = getClockTime(rawTime);
		return makeStartEndTime(rawDate, rawClockTime).toString();
	}

    /**
     * A Methode to correct a time and date format
     * @param rawDate Date which maybe has not the correct form
     * @param rawTime Time which maybe has not the correct form
     * @return ArrayList with time and date in the correct form
     */
	private static ArrayList<String> makeStartEndTime(ArrayList<String> rawDate, ArrayList<String> rawTime) {
		ArrayList<String> timeParts = new ArrayList<>();
		if(rawDate.size()==2 && rawTime.size()==2) {
			timeParts.add(rawDate.get(0)+"-"+rawTime.get(0));
			timeParts.add(rawDate.get(1)+"-"+rawTime.get(1));
			return timeParts;
		}
		if(rawDate.size()==1 && rawTime.size()==2) {
			timeParts.add(rawDate.get(0)+"-"+rawTime.get(0));
			timeParts.add(rawDate.get(0)+"-"+rawTime.get(1));
			return timeParts;
		}
		if(rawDate.size()%2==0 && rawTime.size()%2==0 && rawDate.size()!=0 && rawTime.size()!=0) {
			timeParts.add(rawDate.get(0)+"-"+rawTime.get(0));
			timeParts.add(rawDate.get(1)+"-"+rawTime.get(1));
			return timeParts;
		}
		return new ArrayList<>();
	}

    /**
     * Methode to extract the time form a news string
     * @param content Newsstring with information
     * @return Arraylist with time Strings
     */
	private static ArrayList<String> getClockTime(String content) {
		ArrayList<String> times = findConstructions(content,
				"[0-9]{1,2}(\\.[0-9]{2})? Uhr");
		if(times.size()==0)
			return new ArrayList<>();
		for(String s : times) {
			String k = s;
			k = k.replace(" Uhr", "");
			if(!k.contains("."))
				k = k+".00";
			k = k.replace(".", ":");
			times.set(times.indexOf(s), k);
		}
		return times;
	}

    /**
     * Methode to extract the date form a news string
     * @param content Newsstring with information
     * @return Arraylist with date Strings
     */
	private static ArrayList<String> getDates(String content) {
		ArrayList<String> dates = findConstructions(content,
				"[0-9]{1,2}\\.([0-9]{1,2}\\.)?\\/[0-9]{1,2}\\.[0-9]{1,2}\\.([0-9]{2,4})?");
		if(dates.size()!=0) {
			ArrayList<String> retrunDates = new ArrayList<>();
			for(String s : dates) {
				String[] k = checkDateForm(s).split("~");
				for(String s1 : k) {
					retrunDates.add(s1);
				}
			}
			return retrunDates;
		}
		dates.clear();
		dates = findConstructions(content,
				"[0-9]{1,2}\\.[0-9]{1,2}\\.([0-9]{2,4})?");
		if(dates.size()==0) {
			return new ArrayList<>();
		}
		for(String s : dates) {
			dates.set(dates.indexOf(s), checkDateForm(s));
		}
		return dates;
	}

    /**
     * Checks if the date String has a correct form
     * @param s Date string
     * @return Corrected String
     */
	private static String checkDateForm(String s) {
		if(s.contains("/")) {
			String[] twoDates = s.split("/");
			String year = ""+Calendar.getInstance().get(Calendar.YEAR);
			if(twoDates.length!=2)
				return "";
			if(twoDates[1].split("\\.").length==2) {
				twoDates[1]+=year;
			}else
				return "";
			if(twoDates[0].split("\\.").length==1) {
				twoDates[0]+=twoDates[1].split("\\.")[1]+"."+year;
			}else if(twoDates[0].split("\\.").length==3) {
				twoDates[0]+=year;
			}
			return twoDates[0]+"~"+twoDates[1];
		}else{
			String[] oneDate = s.split("\\.");
			if(oneDate.length==2)
				s += Calendar.getInstance().get(Calendar.YEAR);
		}
		return s;
	}

    /**
     * Methode to get the linenumbers from the News
     * @param content News string
     * @return Arraylist with all matched lines
     */
	private static ArrayList<String> getLines(String content) {
		ArrayList<String> helpLines = findConstructions(content, "Linie S(1|25|26|2|3|41|42|45|46|47|5|75|7|85|8|9)");
		Set<String> hs = new HashSet<>();
		hs.addAll(helpLines);
		helpLines.clear();
		helpLines.addAll(hs);
		for(int i = 0;i<helpLines.size();i++) {
			helpLines.set(i,helpLines.get(i).replace("Linie ", ""));
		}
		Collections.sort(helpLines);
		return helpLines;
	}

    /**
     * Methode to get all stations that could be found
     * @param content News string
     * @return ArrayList with all found stations
     */
	private static ArrayList<String> getStations(String content) {
		ArrayList<String> helpStations = findConstructions(content, "<h1>.+<\\/h1>");
		if(helpStations.size()!=1)
			return new ArrayList<>();
		String stations = helpStations.get(0);
		stations = stations.replace("<h1>","").replace("</h1>","");
		stations = stations.replace("<>", "-");

		return new ArrayList<>();
	}
}
