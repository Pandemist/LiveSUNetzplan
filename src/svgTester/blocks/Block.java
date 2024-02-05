package svgTester.blocks;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static svgTester.utils.utils.placeWhitespace;

public class Block implements Serializable {

	private String id = "";
	private HashMap<String, String> style = new HashMap<>();
	private HashMap<String, String> otherParameter = new HashMap<>();
	private ArrayList<Integer> childList = new ArrayList<>();
	private String content;
	private int depth = 0;
	private int pos = -1;
	private String name;
	private ArrayList<Block> spanList;

	/**
	 *	Default Constructor of Block
	 */
	public Block(){}

	/**
	 * Special Constructor to initialize a normal Block
	 * @param pId Id of Block
	 * @param key Keys for key value Mapping of Blockstyle
	 * @param value Values for key value Mapping of Blockstype
	 */
	public Block(String pId, String key, String value) {
		this.id = pId;
		this.style.put(key, value);
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	/**
	 * Method to add a Child idnumber to the Childlist
	 * @param i Identification Number of a Block, that is a Child of this Block
	 */
	public void addChild(int i) {
		childList.add(i);
	}

	public void setContent(String s) {}

	public void setEndTag(String endTag) {}

	public boolean hasId() {
		if(id!=null) {
			if(!id.equals("")) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Method to prepare a String of not Style Parameters
	 * @param depth of identation
	 * @return Prepared String for printing
	 */
	String printOtherParameter(int depth) {
		StringBuilder b = new StringBuilder();
		for(Map.Entry<String, String> entry : otherParameter.entrySet()) {
			b.append(placeWhitespace(depth)).append(entry.getKey()+"=\""+entry.getValue()+"\"\n");
		}
	//	b.append(placeWhitespace(depth)).append("edit:depth=\""+depth+"\"\n");
	//	b.append(placeWhitespace(depth)).append("edit:pos=\""+pos+"\"\n");
		b.deleteCharAt(b.lastIndexOf("\n"));
		return b.toString();
	}

	/**
	 * Test if Block has other Parameters
	 * @return Boolean return value
	 */
	boolean hasOther() {
		if(otherParameter!=null) {
			if(otherParameter.size()>0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Method to prepare a String for the Styleparameters
	 * @param depth of identation
	 * @return Prepared String for printing
	 */
	String printStyle(int depth) {
		StringBuilder b = new StringBuilder();
		b.append(placeWhitespace(depth));
		b.append("style=\"");
		for(Map.Entry<String, String> entry : style.entrySet()) {
			b.append(entry.getKey()+":"+entry.getValue()+";");
		}
		b.append("\"");
		return b.toString();
	}

	/**
	 * Test of Block has Styleparameters
	 * @return Boolean return value
	 */
	boolean hasStyle() {
		if(style!=null) {
			if(style.size()>0) {
				return true;
			}
		}
		return false;
	}

	public void setValue(String key, String value) {}

	String printId(int depth) {
		return placeWhitespace(depth)+"id=\""+id+"\"";
	}

	public String saveBlock() {return null;}

	void addOtherParameter(String key, String value) {
		otherParameter.put(key, value);
	}

	/**
	 * Ckecks if Block has a Parameter
	 * @param key test if it exists
	 * @return boolean return value
	 */
	public boolean hasOtherParam(String key) {
		return otherParameter.containsKey(key);
	}

	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Adding a Styleparameter to the List
	 * @param style Parameterstring in form: key:value i.e: "fill:none"
	 */
	void setStyle(String style) {
		String[] styles = style.split(";");
		for(String s : styles) {
			String[] sp = s.split(":");
			if(sp.length!=2) {
				return;
			}
			this.style.put(sp[0], sp[1]);
		}
	}

	public String getContent() {
		return content;
	}

	public String getId() {
		return id;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public HashMap<String, String> getOtherParams() {
		return otherParameter;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

	public int getDepth() {
		return depth;
	}

	public int getPos() {
		return pos;
	}

	public String saveBlockEnd() {
		return "";
	}

	public void setSpanList(ArrayList<TspanBlock> spanList, String name) {}

    public String getClearName() {
        return "";
    }

	/**
	 * Changes a Style by key
	 * @param key key to change value
	 * @param value new value
	 */
	public void changeStyle(String key, String value) {
		style.replace(key, value);
	}

	public String getAStyle(String key) {
		return style.get(key);
	}

}
