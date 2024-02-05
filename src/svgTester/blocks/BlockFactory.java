package svgTester.blocks;

public class BlockFactory {
	/**
	 * Creates a new Subblock by given Type
	 * @param type type for new Block
	 * @return new Subblock of type 'type'
	 */
	public static Block getNewBlock(String type) {
		if(type.equalsIgnoreCase("PATH")) {
			return new PathBlock(type);
		}else if(type.equalsIgnoreCase("TEXT")) {
			return new TextBlock(type);
		}else if(type.equalsIgnoreCase("RECT")) {
			return new RectBlock(type);
		}else if(type.equalsIgnoreCase("TSPAN")) {
			return new TspanBlock(type);
		}else{
			return new DefaultBlock(type);
		}
	}
}
