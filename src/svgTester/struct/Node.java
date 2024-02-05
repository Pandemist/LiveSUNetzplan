package svgTester.struct;

import java.util.ArrayList;

public class Node {

	private String nodeId;
	private String nodeName;
	private ArrayList<Edge> edgeList = new ArrayList<>();

	public Node() {}

	public void setNodeId(String pId) {
        this.nodeId = pId;
    }

    public boolean hasNodeId() {
        return (this.nodeId == null)? false: true;
    }

    public String getNodeId() {
        if(this.hasNodeId()) {
            return this.nodeId;
        }else{
            return "";
        }
    }

	public void setNodeName(String pName) {
        this.nodeName = pName;
    }

    public boolean hasNodeName() {
        return (this.nodeName == null)? false: true;
    }

    public String getNodeName() {
        if(this.hasNodeName()) {
            return this.nodeName;
        }else{
            return "";
        }
    }

    public void addEdge(Edge pEdge) {
        this.edgeList.add(pEdge);
    }

    public boolean hasEdge(Edge e) {
        return this.edgeList.contains(e);
    }

    public ArrayList<Edge> getEdgeList() {
    	return this.edgeList;
    }

}