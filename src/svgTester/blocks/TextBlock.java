package svgTester.blocks;

import java.io.Serializable;
import java.util.ArrayList;

import static svgTester.utils.utils.placeWhitespace;

public class TextBlock extends KoordinatenBlock implements Serializable {


	private ArrayList<TspanBlock> spanList = new ArrayList<>();
	private String clearName;

	public TextBlock(String name) {
		super.setName(name);
	}

	@Override
	public void setEndTag(String endTag) {
	}

	/**
	 * Add new key, value Pair to the Block
	 * @param key map key
	 * @param value map value
	 */
	@Override
	public void setValue(String key, String value) {
		if(key.equals("id")) {
			super.setId(value);
		}else if(key.equals("style")) {
			super.setStyle(value);
		}else{
			super.setValue(key, value);
		}
	}

	/**
	 * Creates a String of this block to print
	 * @return printable String
	 */
	@Override
	public String saveBlock() {
		int depth = getDepth();
		StringBuilder s = new StringBuilder();
		s.append(placeWhitespace(depth)).append("<text\n");
		depth++;
		s.append(super.printId(depth)).append("\n");
		s.append(super.printStyle(depth)).append("\n");
		s.append(placeWhitespace(depth)).append("x=\""+super.getX()+"\"\n");
		s.append(placeWhitespace(depth)).append("y=\""+super.getY()+"\"");
		s.append(">");
		for(TspanBlock tb : spanList) {
			s.append(tb.saveBlock());
		}
		return s.toString();
	}

	/**
	 * Creates a String of the end of this block
	 * @return printable String
	 */
	@Override
	public void setSpanList(ArrayList<TspanBlock> spanList, String clearName) {
		this.spanList.addAll(spanList);
		this.clearName = clearName;
	}

	@Override
	public String getClearName() {
		return this.clearName;
	}

	@Override
	public String saveBlockEnd() {
		return "</"+super.getName()+">\n";
	}
}
