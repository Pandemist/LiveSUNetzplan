package svgTester.blocks;

import java.io.Serializable;
import java.util.ArrayList;

import static svgTester.utils.utils.placeWhitespace;

public class DefaultBlock extends Block implements Serializable {
	private String closeTag;
	private String endTag;
	private String content;

	public DefaultBlock() {

	}

    public DefaultBlock(String name) {
		super.setName(name);
    }

	@Override
	public String getContent() {
		return this.content;
	}

	@Override
	public void setContent(String pContent) {
		this.content = pContent;
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
		} else if(key.equals("style")) {
			super.setStyle(value);
		} else {
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
		s.append(placeWhitespace(depth)).append("<"+super.getName());
		depth++;
		if(super.hasId())
			s.append("\n").append(super.printId(depth));
		if(super.hasStyle())
			s.append("\n").append(super.printStyle(depth));
		if(super.hasOther())
			s.append("\n").append(super.printOtherParameter(depth));
		s.append(">");
		if(content!=null) {
			s.append(content);
		}else{
			s.append("\n");
		}
		return s.toString();
	}

	/**
	 * Creates a String of the end of this block
	 * @return printable String
	 */
	@Override
	public String saveBlockEnd() {
		if(content!=null) {
			return "</"+super.getName()+">\n";
		}else{
			return placeWhitespace(getDepth())+"</"+super.getName()+">\n";
		}
	}

	@Override
	public void setEndTag(String endTag) {
		this.endTag = endTag;
	}
}
