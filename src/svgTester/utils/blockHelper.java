package svgTester.utils;

import svgTester.blocks.PathBlock;

public class blockHelper {

    static PathBlock ex = new PathBlock("path");

    /**
     * Methode to prepare static PathBlock ex as a red cross
     * @param x Center X pos
     * @param y Center Y pos
     */
    private static void setupEx(double x, double y) {
        ex.setId("EX");
        ex.setValue("style",
                "fill:none;fill-rule:evenodd;stroke:#b7121c;stroke-width:0.30000001;stroke-linecap:butt;stroke-linejoin:miter;stroke-miterlimit:4;stroke-dasharray:none;stroke-opacity:1");
        ex.setValue("inkscape:connector-curvature","0");
        String d = "m x,y 0.45,0.45 -0.9,-0.9 0.45,0.45 0.45,-0.45 -0.9,0.9";
        d = d.replace("x",""+x);
        d = d.replace("y",""+y);
        ex.setValue("d",d);
    }

    /**
     * Returns a clone of ex with other cords
     * @param x Center X pos
     * @param y Center Y pos
     * @return A Red cross PathBlock at pos X,Y
     */
    public static PathBlock getEx(double x, double y) {
        setupEx(x,y);
        return (PathBlock) cloneHelper.deepClone(ex);
    }
}