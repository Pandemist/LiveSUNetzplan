package svgTester.struct;

import java.util.ArrayList;
import java.util.Date;

/**
 * A Report of a Problem at the Trafficnetwork
 */
public class Report{

	private String startStation_full;
	private String viaStation_full;
	private String destStation_full;
	private String startStation;
	private String viaStation;
	private String destStation;
	private String line;
	private ArrayList<String> changes;
	private Date startDate;
	private Date endDate;
	private Problems problem;
	private String bubble;

	//Default Constructor
	public Report() {
		//Do Nothing
	}

    /**
     * Constructor to insert a new Report
     * @param sStart Startstation as fulltext
     * @param sVia Station which is on the Line where the problem is
     * @param sDest Endstation as fulltext
     * @param pLine Line which has this Problem
     * @param pStartDate Date and Time where the Report gets activ
     * @param pEndDate Date and Time where the Report ends
     * @param pProblem Type of Problem
     * @param pBubble Descrition of this Problem. Like a Weblink
     */
	public Report(String sStart, String sVia, String sDest, String pLine, Date pStartDate, Date pEndDate, Problems pProblem, String pBubble) {
		this.startStation_full = sStart;
		this.viaStation_full = sVia;
		this.destStation_full = sDest;
		this.line = pLine;
		this.startDate = pStartDate;
		this.endDate = pEndDate;
		this.problem = pProblem;
		this.bubble = pBubble;
	}

	public void addChanges(ArrayList<String> value) {
		this.changes = value;
	}

	public String getLine() {
		return this.line;
	}

	public void addProblem(Problems problem) {
		this.problem = problem;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public void setLine(String line) {
		this.line = line;
	}

	public void setBubble(String bubble) {
		this.bubble = bubble;
	}

	public void setStartStation_full(String startStation) {
		this.startStation_full = startStation;
	}

	public void setEndStation_full(String endStation) {
		this.destStation_full = endStation;
	}

	public Date getStartDate() {
		return startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setStartStationShort(String startStation) {
		this.startStation = startStation;
	}

	public void setViaStationShort(String viaStation) {
		this.viaStation = viaStation;
	}

	public void setDestStationShort(String destStation) {
		this.destStation = destStation;
	}

	public String getStartStation_full() {
		return startStation_full;
	}

	public String getViaStation_full() {
		return viaStation_full;
	}

	public String getDestStation_full() {
		return destStation_full;
	}

	public ArrayList<String> getChanges() {
		return changes;
	}

	public Problems getProblem() {
		return problem;
	}

	public String getStartStation() {
		return startStation;
	}

	public String getViaStation() {
		return viaStation;
	}

	public String getDestStation() {
		return destStation;
	}

	public void setViaStation_full(String viaStation_full) {
		this.viaStation_full = viaStation_full;
	}
}