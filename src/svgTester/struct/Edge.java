package svgTester.struct;

import java.util.*;

public class Edge{

	private Node dest;
	private TreeMap<String, Boolean> lineList = new TreeMap<String, Boolean>();

	public Edge() {}

	public void setDest(Node s) {
		this.dest = s;
	}

	public boolean hasDest() {
        return (this.dest == null)? false: true;
    }

    public Node getDest() {
    	return this.dest;
    }

	public void addLine(String s) {
		this.lineList.put(s, true);
	}

    /**
     * Adding lines to this edge
     * @param s A lines in form: U1-U2-U3
     */
	public void addLines(String s) {
		String[] k = s.split("-");
		for(int i=0;i<k.length;i++) {
			this.lineList.put(k[i], true);
		}
	}

    /**
     * Returning a String, that contains all the lines in th form: U1-U2-U3
     * @return Returns a String
     */
	public String printLines() {
		String r = "";
		for(Map.Entry<String, Boolean> s : lineList.entrySet()) {
			r = r+"-"+s.getKey();
		}
		r = r.substring(1, r.length());
		return r;
	}

    /**
     * Checks if a line is on this edge is enabled. (Disabled lines are blocked)
     * @return Boolean value
     */
	public boolean hasEdgeEnabledLines() {
		if(lineList.containsValue(true)) {
			return true;
		}
		return false;
	}

    /**
     * Disable a line of this edge.
     * @param line Line to disable
     */
	public void disableLine(String line) {
		lineList.replace(line, true, false);
	}

	public void enableLine(String line) {
	    lineList.replace(line, false, true);
    }

    /**
     * Checks if the edge a line in the linelist
     * @param s Line to check
     * @return Boolean value to check
     */
	public boolean hasLine(String s) {
		return this.lineList.containsKey(s);
	}

    /**
     * Returns a string which contains all the lines. Form: U1-U2-U3
     * @return String with lines
     */
	public String getLists() {
		String s = "";
		for(Map.Entry<String, Boolean> k : lineList.entrySet()) {
			s = s+"-"+k.getKey();
		}
		if(s.length()>0) {
			s = s.substring(1, s.length());
		}
		return s;
	}
}