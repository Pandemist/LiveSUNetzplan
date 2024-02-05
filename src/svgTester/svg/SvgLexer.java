package svgTester.svg;

import java_cup.runtime.Symbol;
import svgTester.blocks.*;
import svgTester.struct.Node;
import svgTester.struct.Problems;
import svgTester.utils.blockHelper;
import svgTester.utils.similarityUtils;
import svgTester.utils.utils;

import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ListIterator;

import static svgTester.blocks.BlockFactory.*;
import static svgTester.utils.cloneHelper.deepClone;

/**
 * The Class SvgLexer recieves a .svg file and extract all the possible Blocks. It also creates a Trafficgraph. (SvgGraph)
 */
public class SvgLexer{

	private ArrayList<Symbol> tokenList = new ArrayList<>();
	private String headBlock = "";
	private ArrayList<Block> blockList = new ArrayList<>();
	private ListIterator<Symbol> it;
	private HashMap<String, Problems> overlayReminder = new HashMap<>();
	private int pos = 1;
	SvgGraph graph = new SvgGraph();

    /**
     * Constructor for the SvgLexer.
     * @param fileName Path to the .svg file
     */
	public SvgLexer(String fileName) {
		Symbol sym;
		try {
			Lexer lexer = new Lexer(new FileReader(fileName));
			for(sym = lexer.next_token();sym.sym != 0;sym = lexer.next_token()) {
	//			System.out.println("#"+sym.sym+": "+sym.value);
				tokenList.add(sym);
			}
		}catch(Exception e) {
			System.out.println(e);
			assert(false);
		}
		it = tokenList.listIterator();
		if(!it.hasNext()) {
			assert(false);
		}

		it = tokenList.listIterator();
		sym = it.next();
		if(it.hasNext()) {
			headBlock = (String) sym.value;
		}
		while(it.hasNext()) {
			if(sym.sym == 12) {
				makeBlocks(sym, 0);
			}
			if(it.hasNext()) {
				sym = it.next();
			}
		}
		completeTexts();
		addToGraph();
	}

    /**
     * Methode searches a route in the trafficgraph
     * @param line Line in which the route has to be
     * @param startNodeId Node where the alogrithem starts to search
     * @param viaNodeId Node which has to be on the route
     * @param destNodeId Node where the route ends
     * @return List of Nodes, which build the route
     */
	public ArrayList<String> findAWay(String line, String startNodeId, String viaNodeId, String destNodeId) {
		return graph.findWay(line, startNodeId, viaNodeId, destNodeId);
	}

    /**
     * Methode to make changes in the graph, sorted by Problem of the Report
     * @param line Line where the changes will be made
     * @param stations Stations where the Problem is
     * @param problem Problem of the Report
     */
	public void makeChanges(String line, ArrayList<String> stations, Problems problem) {
		ArrayList<String> edges = graph.buildEdge(line, stations, problem);
		switch(problem) {
            case ENDED: {
                for(String s : edges) {
                    removeOverlay(line, s+"_LAYER");
                }
                break;
            }
			case STATION_WORK: {
				for(String s : stations) {
					recolour(s+"_NODE", "bdbdbd", "bdbdbd");
				}
				break;
			}
			case DELAY: {
			//	for(String s : stations) {
				//	recolour(s+"_NODE", "bdbdbd");
			//	}
				for(String s : edges) {
					addOverlay(line, s, "ff0000", "0.6", true, problem);
				}
				break;
			}
			case BLOCKED: {
			/*	for(String s : stations) {
					s = s+"_NODE";
					addCross(s);
					recolour(s, "bdbdbd",  getColourOfBlockById(s));
				}*/
				for(String s : edges) {
					addOverlay(line, s, getColourOfBlockById(s), "0.6", false, problem);
				}
				break;
			}
			case REPLACED: {
			/*    for(String s : stations) {
					s = s+"_NODE";
					addCross(s);
					recolour(s, "bdbdbd",  getColourOfBlockById(s));
				}*/
				for(String s : edges) {
					addOverlay(line, s, getColourOfBlockById(s), "0.6", false, problem);
				}
				break;
			}
			default: {

			}
		}
	}
//---Methodes to Change the svg---------------------------------

    /**
     * Methode to recolour a Pathblock
     * @param s Id of the Station
     * @param colour Colour for a Rect block (Big Station)
     * @param colour2 Colour for Path block (Small Station)
     */
	private void recolour(String s, String colour, String colour2) {
		if(graph.hasNodeEnabledEdges(s.replace("_NODE","")) && s.contains("_NODE"))
			return;
		Block b = getBlockById(s);
		if(b == null)
			return;
		if(b instanceof RectBlock) {
			b.changeStyle("fill","#"+colour);
		}
		if(b instanceof PathBlock) {
			b.changeStyle("stroke","#"+colour2);
		}
		if(b instanceof TextBlock) {
			b.changeStyle("fill","#"+colour2);
		}
	}

    /**
     * Places a Cross on a Stationnode (Big Station)
     * @param s Station id
     */
	private void addCross(String s) {
		if(graph.hasNodeEnabledEdges(s.replace("_NODE","")) && s.contains("_NODE"))
			return;
		Block b = getBlockById(s);
		if(b == null)
			return;
		if(!(b instanceof RectBlock))
			return;
		RectBlock rb = (RectBlock) b;
		ArrayList<Double> xy = rb.getCenter();
		if(xy.size()!=2)
			return;
		PathBlock stroke = blockHelper.getEx(xy.get(0), xy.get(1));
		stroke.setDepth(b.getDepth());
		stroke.setId(b.getId()+"_EX");
		blockList.add(stroke);
		insertAfter(stroke, b);
	}

    /**
     * Adding an Overlay over a Path betweet two Stations
     * @param s Path id
     * @param colour Colour of the Overlay
     * @param thickness Thickness of the Overlay
     * @param dotted Boolean, if the Overlay should be dotted/dashed or not
     * @param p Type of Problem
     */
	private void addOverlay(String line, String s, String colour, String thickness, boolean dotted, Problems p) {
	    if(overlayReminder.containsKey(s)) {
            //If Overlay is on a lower level, replace it, if its on the same level, skip
	        if(overlayReminder.get(s).getValue()<p.getValue()) {
                removeOverlay(line, s+"_LAYER");
            }else if(overlayReminder.get(s).getValue()>=p.getValue()) {
                return;
            }

        }
		Block b = getBlockById(s);
		if(b == null)
			return;
		if(!(b instanceof PathBlock))
			return;
		if(p != Problems.DELAY) {
			if(graph.hasEdgeEnabledLines(s) || s.contains("_NODE"))
				return;
		}
		Block c = (Block) deepClone(b);
		overlayReminder.put(s, p);
		c.setPos(-1);
		c.setId(c.getId()+"_LAYER");
		c.changeStyle("stroke","#"+colour);
		c.changeStyle("stroke-width",thickness);
		if(dotted)
			c.changeStyle("stroke-dasharray","1.68600015, 1.68600015");
		blockList.add(c);
		insertAfter(c,b);
	}

	private void removeOverlay(String line, String s) {
        graph.enableLine(line, s);
        overlayReminder.remove(s);
        Block b = getBlockById(s);
        if(b==null) {
            return;
        }
        for(int i=pos-1;i>0;i--) {
            Block c = getNodeByPos(i);
            c.setPos(c.getPos()-1);
            if(i==b.getPos()) {
                c.setPos(c.getPos()+1);
                blockList.remove(b);
                break;
            }
        }
        pos--;
    }
//--------------------------------------------------------------

    /**
     * Returns the colour of a Block, by his id
     * @param id Block id
     * @return Colour. Form: 'ffffff'
     */
	private String getColourOfBlockById(String id) {
		Block b = getBlockById(id);
		if(b == null) {
			return "ffffff";
		}
		if(b instanceof RectBlock)
			return utils.makeColourGray(b.getAStyle("fill").replace("#",""), 190);
		if(b instanceof PathBlock)
			return utils.makeColourGray(b.getAStyle("stroke").replace("#",""), 190);
		return "ffffff";
	}

    /**
     * This Methode inserts a Block A after Block B in the Blocklist.
     * @param insert Block wich will be inserted
     * @param after Block after which will be inserted
     */
	private void insertAfter(Block insert, Block after) {
		int afterPos = after.getPos();
		int max = pos;

		for(int i=max-1;i>0;i--) {
			Block b = getNodeByPos(i);
			b.setPos(b.getPos()+1);
			if(i==afterPos) {
				b.setPos(b.getPos()-1);
				insert.setPos(i+1);
				break;
			}
		}
		pos++;
	}

	private Block getBlockById(String s) {
		for(Block b : blockList) {
			if(b.getId().equals(s)) {
				return b;
			}
		}
		return null;
	}

    /**
     * Methode to collect Nameparts of mulitline Stationnames and merge them together
     */
	private void completeTexts() {
		ArrayList<TspanBlock> spanList = new ArrayList<>();
		ListIterator<Block> it = blockList.listIterator();
		String name = "";
		Block b;
		while(it.hasNext()) {
			b = it.next();
			if(b instanceof TspanBlock) {
				spanList.add((TspanBlock) b);
				String cont = b.getContent();
				if(name.length()>0&&cont.length()>0) {
					if(Character.isUpperCase(cont.charAt(0))&&name.charAt(name.length()-1)!='-') {
						cont = " "+cont;
					}else if(!Character.isUpperCase(cont.charAt(0))&&name.charAt(name.length()-1)=='-') {
						name = name.substring(0, name.length()-1);
					}
				}
				name = name + cont;
				it.remove();
			}else if(b instanceof TextBlock) {
			//	Collections.reverse(spanList);
				b.setSpanList(spanList, name);
				name = "";
				spanList.clear();
			}
		}
	}

    /**
     * Methode which adds the Blocklist to the Graph
     */
	private void addToGraph() {
		for(Block b : blockList) {
			if(b instanceof TextBlock) {
				graph.addNewNode(b.getId(), b.getClearName());
			}
			if(b instanceof PathBlock) {
				String id = b.getId();
				String[] splitedId = id.split("_");
				if(splitedId.length==2) {
					String[] stations = splitedId[1].split("-");
					if(stations.length==2) {
						graph.addNewSymEdge(splitedId[0], stations[0], stations[1]);
					}
				}
			}
		}
	}

    /**
     * Methodes which makes Blocks from lexed Symbols
     * @param pSym First Symbol to start with
     * @param depth Indent of the Block in the file
     * @return -1
     */
	private int makeBlocks(Symbol pSym, int depth) {
		Block b = getNewBlock(((String) pSym.value).replace("<","").replace(">",""));
		b.setDepth(depth);
		if(!(b instanceof TspanBlock)) {
			b.setPos(pos);
			pos++;
		}
		String key = "";
		String value = "";
		boolean isQuote = false;
		while(it.hasNext()) {
			pSym = it.next();
		//	System.out.println("#"+pSym.sym+": "+pSym.value);
			if(isQuote && pSym.sym != 15) {
				value += pSym.value;
			}else{
				if(pSym.sym == 6) {
					if(!key.equals("")) {
						key += " " + pSym.value;
					}else{
						key += pSym.value;
					}
				}else if(pSym.sym == 12) {
					b.addChild(makeBlocks(pSym, depth+1));
				}else if(pSym.sym == 13) {
					key = "";
					value = "";
				}else if(pSym.sym == 14) {
					if(!key.equals("")) {
						b.setContent(key);
					}
					b.setEndTag((String) pSym.value);
					blockList.add(b);
					return b.getPos();
				}else if(pSym.sym == 15) {
					if (isQuote) {
						if (!key.equals("")) {
							b.setValue(key, value);
						}
						key = "";
						value = "";
					}
					isQuote = !isQuote;
				}
			}
		}
		return -1;
	}

    /**
     * Prints the whole Blocklist to a new File
     * @param dest Path to the File
     */
	public void saveToFile(String dest) {
		try{
			File f = new File(dest);
			f.createNewFile();
			PrintWriter p = new PrintWriter(f, "UTF-8");
			p.print(headBlock+"\n");
			int i = 1;
			while(i<pos-1) {
				i = save(p,i);
			}
			p.close();
		}catch(Exception e) {
			System.out.println(e);
		}
	}

    /**
     * Saves the Blocks by position
     * @param w Given Printwriter
     * @param pos Position in the Blocklist
     * @return
     */
	public int save(PrintWriter w, int pos) {
		Block b = getNodeByPos(pos);
		Block c = getNodeByPos(pos+1);

		//System.out.println(b.getId()+" - "+b.getPos());

		if(b==null) {
			return pos;
		}
		w.print(b.saveBlock());
		while(c!=null) {
			if (b.getDepth() < c.getDepth()) {
				pos = save(w,pos+1);
			}else{
				break;
			}
			c = getNodeByPos(pos+1);
		}
		w.print(b.saveBlockEnd());
		return pos;
	}

	private Block getNodeByPos(int aPos) {
		for(Block b : blockList) {
			if(b.getPos() == aPos) {
				return b;
			}
		}
		return null;
	}

    /**
     * Methode findes the Stationname which matches the most with given Name.
     * @param name Given Stationname
     * @return Shortname
     */
	public String getContraction(String name) {
		if(name.equals("")) {
			return "";
		}
		String shorter = "";
		double score = Double.MAX_VALUE;
		for(Node n : graph.nodeList) {
			double i = similarityUtils.getAlignmentScore(n.getNodeName(), name);
			if(i<score) {
				score = i;
				shorter = n.getNodeId();
			}
		}
		return shorter;
	}
}