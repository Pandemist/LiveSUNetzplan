package svgTester.svg;

import svgTester.struct.Edge;
import svgTester.struct.Node;
import svgTester.struct.Problems;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

public class SvgGraph {

	ArrayList<Node> nodeList;

	public SvgGraph() {
		nodeList = new ArrayList<>();
	}

	private Node getNodeById(String id) {
		for (Node n : nodeList) {
			if (n.getNodeId().equals(id)) {
				return n;
			}
		}
		return null;
	}

	private boolean hasNodeById(String id) {
		for (Node n : this.nodeList) {
			if (n.getNodeId().equals(id)) {
				return true;
			}
		}
		return false;
	}

	private void makeNodeIfNotExist(String id) {
		if (!hasNodeById(id)) {
			Node n = new Node();
			n.setNodeId(id);
			nodeList.add(n);
		}
	}

    /**
     * Methode to find a path between the 2 given node Ids
     *  The algorithem loops over all the edges of the actual node, and select the Edge with a matching Line,
     *  which leads not back to the last Node
     * @param line Line on which the path has to be
     * @param currentNode The node, where the route acutally is
     * @param lastNodeId The last visited node, to avoid infinite circles
     * @param destNodeId The recursive break condition, this node is he end of the path
     * @return Returns a ordered arraylist with all the stations on the route
     */
	private ArrayList<ArrayList<String>> wayTracker(String line, Node currentNode, String lastNodeId, String destNodeId) {
		ArrayList<String> cList = new ArrayList<>();
		ArrayList<ArrayList<String>> outerList = new ArrayList<>();
		if (currentNode.getNodeId().equals(destNodeId)) {
			cList.add(currentNode.getNodeId());
			outerList.add(cList);
			return outerList;
		}
		for (Edge e : currentNode.getEdgeList()) {
			if (e.hasLine(line) && !e.getDest().getNodeId().equals(lastNodeId)) {
				outerList.addAll(wayTracker(line, e.getDest(), currentNode.getNodeId(), destNodeId));
			}
		}
		for (ArrayList<String> a : outerList) {
			a.add(currentNode.getNodeId());
		}
		return outerList;
	}

	//==================================================
	public void addNewNode(String id, String name) {
		makeNodeIfNotExist(id);
		Node n = getNodeById(id);
		n.setNodeName(name);
	}

    /**
     * This Methode adds a new edge and inserts the opposit direction too, to create symetrie
     * @param lines line of the Node
     * @param statA Stationname A
     * @param statB Stationname B
     */
	public void addNewSymEdge(String lines, String statA, String statB) {
		makeNodeIfNotExist(statA);
		makeNodeIfNotExist(statB);
		Node nA = getNodeById(statA);
		Node nB = getNodeById(statB);
		Edge eA = new Edge();
		eA.setDest(nB);
		eA.addLines(lines);
		nA.addEdge(eA);
		Edge eB = new Edge();
		eB.setDest(nA);
		eB.addLines(lines);
		nB.addEdge(eB);
	}

    /**
     * Methode to list all Edges as String (Form: U1-U2-U3_StationA-StationB) for the given parameters
     * @param line affected line
     * @param stations ArrayList of all Stations, to find the edges between
     * @param p Problem of a Traffic report (Depending on this, the line will be disabled)
     * @return ArrayList of Nodenames
     */
	public ArrayList<String> buildEdge(String line, ArrayList<String> stations, Problems p) {
		ArrayList<String> edgeList = new ArrayList<>();
		for (Node n : nodeList) {
			if(stations.contains(n.getNodeId())) {
				for (Edge e : n.getEdgeList()) {
					if (e.hasLine(line) && stations.contains(e.getDest().getNodeId())) {
						if(p != Problems.DELAY) {
							e.disableLine(line);
						}
						edgeList.add(e.printLines() + "_" + n.getNodeId() + "-" + e.getDest().getNodeId());
					}
				}
			}
		}
		return edgeList;
	}

    /**
     * Checks if a Edge has enabled lines
     * @param id Edge id
     * @return Bool value, if a line is enabled or disabled
     */
	public boolean hasEdgeEnabledLines(String id) {
		String[] split = id.split("_");
		if(split.length!=2)
			return false;
		String[] lines  = split[0].split("-");
		String[] stations = split[1].split("-");
		if(stations.length!=2)
			return false;
		Node optOne = getNodeById(stations[0]);
		Node optTwo = getNodeById(stations[1]);
		for(Edge e : optOne.getEdgeList()) {
			for(String line : lines) {
				if (e.getDest() == optTwo && e.hasLine(line) && e.hasEdgeEnabledLines()) {
					return true;
				}
			}
		}
		for(Edge e : optTwo.getEdgeList()) {
			for(String line : lines) {
				if (e.getDest() == optOne && e.hasLine(line) && e.hasEdgeEnabledLines()) {
					return true;
				}
			}
		}
		return false;
	}

    public void enableLine(String line, String id) {
        id = id.replace("_LAYER","");
        String[] split = id.split("_");
        if(split.length!=2)
            return;
        String[] stations = split[1].split("-");
        if(stations.length!=2)
            return;
        Node optOne = getNodeById(stations[0]);
        Node optTwo = getNodeById(stations[1]);

        for(Edge e : optOne.getEdgeList()) {
            if (e.hasLine(line)) {
                e.enableLine(line);
            }
        }

        for(Edge e : optTwo.getEdgeList()) {
            if (e.hasLine(line)) {
                e.enableLine(line);
            }
        }
    }

	public boolean hasNodeEnabledEdges(String id) {
		boolean isEnabled = false;
		Node n = getNodeById(id);
		for (Edge e : n.getEdgeList()) {
			if (e.hasEdgeEnabledLines()) {
				isEnabled = true;
			}
		}
		return isEnabled;
	}

    /**
     * Creates a List of all Stations on a line
     * @param line Linenumber
     * @return List of stations
     */
	private ArrayList<String> getCompleteLine(String line) {
		ArrayList<String> nodes = new ArrayList<>();
		for(Node n : nodeList) {
			for(Edge e : n.getEdgeList()) {
				if(e.hasLine(line)) {
					nodes.add(n.getNodeId());
				}
			}
		}
		Set<String> nodeSet = new LinkedHashSet<String>(nodes);
		nodes.clear();
		nodes.addAll(nodeSet);
		return nodes;
	}

    /**
     * Methode to find a Path on a Line between two stations
     * @param line Line on which the path should be searched
     * @param startNodeId Startpoint of the Node
     * @param viaNodeId Node which has to be on the path.
     * @param destNodeId Endpoint of the Node
     * @return Returns a ordered list of stations on this path
     */
	public ArrayList<String> findWay(String line, String startNodeId, String viaNodeId, String destNodeId) {
		ArrayList<String> completeLine = getCompleteLine(line);
		if(completeLine.size()==0)
			return new ArrayList<>();
		if(startNodeId.equals("") || destNodeId.equals(""))
			return completeLine;

		Node startNode = getNodeById(startNodeId);
		ArrayList<ArrayList<String>> changeList = wayTracker(line, startNode, "", destNodeId);
		if (changeList.size() == 0) {
			return new ArrayList<>();
		}
		if (changeList.size() == 1) {
			return changeList.get(0);
		}
		if (viaNodeId.equals("")) {
			ArrayList<String> rList = changeList.get(0);
			int minSize = rList.size();
			for (ArrayList<String> a : changeList) {
				if (minSize > a.size()) {
					rList = a;
					minSize = a.size();
				}
			}
			return rList;
		} else {
			for (ArrayList<String> a : changeList) {
				for (String s : a) {
					if (s.equals(viaNodeId)) {
						return a;
					}
				}
			}
		}
		return new ArrayList<>();
	}

    /**
     * DEBUG
     * Prints the Graph to the consol window
     */
	public void printGraph() {
		for (Node n : nodeList) {
			System.out.println(n.getNodeId() + " (" + n.getNodeName() + ")");
			for (Edge e : n.getEdgeList()) {
				System.out.println("->" + e.getDest().getNodeId() + " - " + e.printLines());
			}
		}
	}
}
