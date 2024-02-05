package svgTester.utils;

public class utils {

    /**
     * Printing Helper return the correct amount of whitespaces
     * @param depth hierarchical depth
     * @return n Whitespaces
     */
	public static String placeWhitespace(int depth) {
		String s = "";
		for(int i=0;i<depth;i++) {
			s = s+"   ";
		}
		return s;
	}

    /**
     * Methode do create a gray variant of the given colour
     * @param colour Colour which should be grayed
     * @param value intensity of grayness
     * @return rgb String of the gray colour. (Form: ffffff)
     */
	public static String makeColourGray(String colour, int value) {
		if((0>value||value>=254)) {
			return colour;
		}
		String cCodesR = colour.substring(0,2);
		String cCodesG = colour.substring(2,4);
		String cCodesB = colour.substring(4,6);

		int codeR = Integer.parseInt(cCodesR, 16);
		int codeG = Integer.parseInt(cCodesG, 16);
		int codeB = Integer.parseInt(cCodesB, 16);

		int rgDiff = codeR - codeG;
		int gbDiff = codeG - codeB;

		rgDiff = (int) (rgDiff*(7.0/10.0));
		gbDiff = (int) (gbDiff*(7.0/10.0));

		codeR = codeR - rgDiff;
		codeB = codeB + gbDiff;

		while(codeR<=value&&codeG<=value&&codeB<=value) {
			codeR++;
			codeG++;
			codeB++;
		}

		return Integer.toHexString(codeR)+""+Integer.toHexString(codeG)+""+Integer.toHexString(codeB);
	}
}
