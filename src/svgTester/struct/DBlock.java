package svgTester.struct;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * DBlock is a Class to handle the parameter "d" of a Pathblock. This parameter describes the Position and form of the Block.
 * The way a "d" segment is designed is listed on the bottom of the class.
 */
public class DBlock implements Serializable {

    private char type;
    private ArrayList<DValue> valueList = new ArrayList<>();

    /**
     * Default Construtor of DBlock.
     * @param pType Type of DBlock. List of possible characters below
     * @param pValueList Value of the DType given in as DValue.
     */
    public DBlock(char pType, ArrayList<DValue> pValueList) {
        this.type = pType;
        this.valueList.addAll(pValueList);
    }

    public char getType() {
        return this.type;
    }

    /**
     * Prepares a String of this DBlock to print.
     * @return DBlock as String
     */
    public String printBlock() {
        String r = type+" ";
        for(DValue d : valueList) {
            r += d.printValue() + " ";
        }
        r = r.substring(0, r.length()-1);
        return r;
    }
}

/*
Command     Description 	Example
M (MoveTo)	Setzt den aktuellen Punkt fest, von dem aus der Pfad starten soll.
	M20 50
Z (ClosePath)	Erstellt eine geschlossene Form.
	Z
L (LineTo)	Zeichnet eine Linie vom aktuellen zum angegebenen Punkt.
	L50 100
H (Horizontal LineTo)	Zeichnet vom aktuellen Punkt aus eine horizontale Linie.
	H75
V (Vertical LineTo)	Zeichnet vom aktuellen Punkt aus eine vertikale Linie.
	V40
C (CurveTo)	Zeichnet vom aktuellen Punkt aus eine Bézier-Kurve.
	C30 0 50 20 60 15
S (Shorthand/Smooth CurveTo)	Ähnlich dem Kommando C, jedoch wird hier als erster Kontrollpunkt der letzte aus der direkt zuvor festgelegten Bézier-Kurve verwendet.
	S40 0 60 0
Q (Quadratic Bézier CurveTo)	Zeichnet eine quadratische Bézier-Kurve.
	Q40 0 60 20
T (Shorthand/Smooth Quadratic Bézier CurveTo)	Ähnlich dem Kommando Q, jedoch wird hier als erster Kontrollpunkt der letzte aus der direkt zuvor festgelegten quadratischen Bézier-Kurve verwendet.
	T1000 300
A (Elliptical Arc)	Zeichnet vom aktuellen Punkt aus einen elliptischen Bogen.
	A25 25 -30 0 1 50 -25
 */