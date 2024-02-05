package svgTester.blocks;

import org.apache.commons.lang3.math.NumberUtils;

import java.io.Serializable;
import java.util.ArrayList;

import static svgTester.utils.utils.placeWhitespace;

public class TspanBlock extends KoordinatenBlock implements Serializable {
	private String content = "";

	public TspanBlock(String name) {
		super.setName(name);
	}

	@Override
	public String getContent() {
		return this.content;
	}

	@Override
	public void setContent(String s) {
		this.content = s;
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
		}else if(key.equals("x")) {
			if(NumberUtils.isParsable(value)) {
				super.setX(value);
			}
		}else if(key.equals("y")) {
			if(NumberUtils.isParsable(value)) {
				super.setY(value);
			}
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
		s.append("<tspan\n");
		depth++;
		s.append(super.printId(depth)).append("\n");
		s.append(super.printStyle(depth)).append("\n");
		s.append(super.printOtherParameter(depth)).append("\n");
		s.append(placeWhitespace(depth)).append("x=\""+super.getX()+"\"\n");
		s.append(placeWhitespace(depth)).append("y=\""+super.getY()+"\"");
		s.append(">");
		s.append(content);
		s.append("</"+super.getName()+">");
		return s.toString();
	}

	/**
	 * Creates a String of the end of this block
	 * @return printable String
	 */
	@Override
	public String saveBlockEnd() {
		return "";
	}
}
