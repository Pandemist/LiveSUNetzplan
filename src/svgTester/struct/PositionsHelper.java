package svgTester.struct;

import org.apache.commons.lang3.math.NumberUtils;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * The PositionHelper is a Class which converts
 */
public class PositionsHelper implements Serializable {

    private String d;
    private double x;
    private double y;
    private ArrayList<DBlock> dblockList = new ArrayList<>();

    public PositionsHelper() {

    }

    /**
     * Default Constructor to convert the 'd' parameter to DBlocks and DValues
     * @param pD 'd' parameter from Block
     */
    public PositionsHelper(String pD) {
        this.d = pD;
        converD();
    }

    /**
     * Convert method to transform a 'd' parameter to DBlocks which are listed in the dblocklist
     */
	private void converD() {
        char[] c = d.toCharArray();

        /*
        * Stages:
        * 0 = Block has not begun
        * 1 = type has been inserted
        * 2 = x or r value
        * 3 = y value
         */

        char helpType = ' ';
        ArrayList<DValue> helpValue = new ArrayList<>();
        String stageTwoHelper = "";
        String stageThreeHelper = "";
        int stage = 0;
        /**
         * Loop which loops over the 'd' chars. Depending of the position state.
         */
        for(int i=0; i<c.length;i++) {
            if(stage == 2 && c[i] == ',') {
                stage = 3;
            }else if(stage == 3 && c[i] == ' ') {
                stage = 1;
                if(NumberUtils.isParsable(stageTwoHelper)&&NumberUtils.isParsable(stageThreeHelper)) {
                    helpValue.add(new DValue(
                            Double.parseDouble(stageTwoHelper),
                            Double.parseDouble(stageThreeHelper)));
                    stageTwoHelper = "";
                    stageThreeHelper = "";
                }else{
                    assert(false);
                }
            }else if(stage == 2 && c[i] == ' ') {
                stage = 1;
                if(NumberUtils.isParsable(stageTwoHelper)) {
                    helpValue.add(new DValue(Double.parseDouble(stageTwoHelper)));
                    stageTwoHelper="";
                }else{
                    assert(false);
                }
            }else if(stage == 1 && c[i] == ' ') {
            }else if((stage == 1 || stage == 2) && (Character.isDigit(c[i]) || c[i] == '.') || c[i] == '-') {
                stageTwoHelper += c[i];
                stage = 2;
            }else if(stage == 3 && (Character.isDigit(c[i]) || c[i] == '.') || c[i] == '-') {
                stageThreeHelper += c[i];
            }else if(stage == 1 && Character.isLetter(c[i])) {
                dblockList.add(new DBlock(helpType, helpValue));
                helpValue.clear();
                stageTwoHelper = "";
                stageThreeHelper = "";
                stage = 1;
                helpType = c[i];
            }else if(stage == 0 && Character.isLetter(c[i])) {
                stage = 1;
                helpType = c[i];
            }
        }
        if(stageThreeHelper.equals("")) {
            if(NumberUtils.isParsable(stageTwoHelper)) {
                helpValue.add(new DValue(Double.parseDouble(stageTwoHelper)));
            }else{
                assert(false);
            }
        }else{
            if(NumberUtils.isParsable(stageTwoHelper)&&NumberUtils.isParsable(stageThreeHelper)) {
                helpValue.add(new DValue(
                        Double.parseDouble(stageTwoHelper),
                        Double.parseDouble(stageThreeHelper)));
            }else{
                assert(false);
            }
        }
        dblockList.add(new DBlock(helpType, helpValue));
    }

    public boolean isEmpty() {
        return d == null;
    }

    /**
     * Creates a String of the DBlock from this Block
     * @param depth Depth of this parameter
     * @return String of this dblockList
     */
	public String printPosition(int depth) {
        StringBuilder s = new StringBuilder();
        for(DBlock db : dblockList) {
            s.append(db.printBlock()).append(" ");
        }
        s.deleteCharAt(s.lastIndexOf(" "));
        return d;
    //    return s.toString();
	}

    public void addD(String value) {
        this.d=value;
        converD();
    }
}
















