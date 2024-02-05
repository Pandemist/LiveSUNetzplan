package svgTester.blocks;

import org.apache.commons.lang3.math.NumberUtils;

import java.io.Serializable;

public abstract class KoordinatenBlock extends Block implements Serializable {

	private double x;
	private double y;

	public KoordinatenBlock() {

	}

	/**
	 * Adding position information to this block
	 * @param key map key
	 * @param value map value
	 */
	@Override
	public void setValue(String key, String value) {
		if(key.equals("x")) {
			setX(value);
		}else if(key.equals("y")) {
			setY(value);
		}else{
			super.addOtherParameter(key, value);
		}
	}

	public void setX(String x) {
		if(NumberUtils.isParsable(x))
			this.x = Double.parseDouble(x);
	}

	public void setY(String y) {
		if(NumberUtils.isParsable(y))
			this.y = Double.parseDouble(y);
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}
}
