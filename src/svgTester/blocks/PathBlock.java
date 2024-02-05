package svgTester.blocks;

import svgTester.struct.PositionsHelper;

import java.io.Serializable;
import java.util.ArrayList;

import static svgTester.utils.utils.placeWhitespace;

public class PathBlock extends Block implements Serializable {

	private PositionsHelper pHelper = new PositionsHelper();

	public PathBlock(String name) {
		super.setName(name);
	}

	@Override
	public void setName(String name) {
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
		}else if(key.equals("d")) {
			pHelper.addD(value);
		}else{
			super.addOtherParameter(key, value);
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
		s.append(placeWhitespace(depth)).append("<path\n");
		depth++;
		s.append(super.printId(depth)).append("\n");
		s.append(super.printStyle(depth)).append("\n");
		s.append(placeWhitespace(depth)).append("d=\""+pHelper.printPosition(depth)+"\"").append("\n");
		s.append(super.printOtherParameter(depth));
		s.append(" />");
		return s.toString();
	}

	/**
	 * Creates a String of the end of this block
	 * @return printable String
	 */
	@Override
	public String saveBlockEnd() {
		return "\n";
	}
}
