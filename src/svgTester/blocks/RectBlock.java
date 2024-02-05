package svgTester.blocks;

import org.apache.commons.lang3.math.NumberUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.IntStream;

import static svgTester.utils.utils.placeWhitespace;

public class RectBlock extends KoordinatenBlock implements Serializable {

	private double width;
	private double height;

	public RectBlock(String name) {
		super.setName(name);
	}

	/**
	 * Calculates the center of the Block (used as Stations). Rotation correction included.
	 * @return Retuns a Arraylist with x and y.
	 */
	public ArrayList<Double> getCenter() {
		ArrayList<Double> cords = new ArrayList<>();
		double x = super.getX();
		double y = super.getY();

		x += (width/2);
		y += (height/2);

		if(hasOtherParam("transform")) {
			String tranformation = getOtherParams().get("transform");
			if(tranformation.contains("matrix")) {
				double[] tranformed = transform(x,y,tranformation);
				x = tranformed[0];
				y = tranformed[1];
			}
		}

		cords.add(x);
		cords.add(y);
		return cords;
	}

	/**
	 * Calculates the correction for the block rotationmatrix.
	 * @param x X position of the Block
	 * @param y Y position of the Block
	 * @param matrix Transforationmatrix as String. Form: "(x1, x2, x3, x4, x5, x6)"
	 * @return Transformated coordinates
	 */
	private double[] transform(double x, double y, String matrix) {
		double[] a = new double[] {x,y, 1};
		matrix = matrix.substring(matrix.lastIndexOf('(')+1,matrix.lastIndexOf(')'));
		matrix = matrix.replace(" ","");
		String helpB[] = matrix.split(",");
		if(helpB.length!=6)
			assert(false);
		double[][] b = {{Double.parseDouble(helpB[0]), Double.parseDouble(helpB[2]), Double.parseDouble(helpB[4])},
				{Double.parseDouble(helpB[1]), Double.parseDouble(helpB[3]), Double.parseDouble(helpB[5])},
				{0.0,0.0,1.0}};
		double[] c = new double[2];
		c[0] = (b[0][0]*a[0])+(b[0][1]*a[1])+b[0][2];
		c[1] = (b[1][0]*a[0])+(b[1][1]*a[1])+b[1][2];

		return new double[] {c[0],c[1]};
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
		}else if(key.equals("width")) {
			if(NumberUtils.isParsable(value)) {
				this.width = Double.parseDouble(value);
			}
		}else if(key.equals("height")) {
			if(NumberUtils.isParsable(value)) {
				this.height = Double.parseDouble(value);
			}
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
		s.append(placeWhitespace(depth)).append("<rect\n");
		depth++;
		s.append(super.printId(depth)).append("\n");
		s.append(super.printStyle(depth)).append("\n");
		s.append(placeWhitespace(depth)).append("width=\""+width+"\"\n");
		s.append(placeWhitespace(depth)).append("height=\""+height+"\"\n");
		s.append(placeWhitespace(depth)).append("x=\""+super.getX()+"\"\n");
		s.append(placeWhitespace(depth)).append("y=\""+super.getY()+"\"\n");
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
