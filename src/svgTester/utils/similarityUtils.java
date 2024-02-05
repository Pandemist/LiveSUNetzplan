package svgTester.utils;

public class similarityUtils {

    /**
     * Algorithem to get the local Alignment of an input and a pattern
     * Algorithem to calculate the global alignment.
     * @param toTest Pattern
     * @param correct Input
     * @return Returns the Alignmentscore of the given Strings
     */
	public static double getAlignmentScore(String toTest, String correct) {

		int gap = 2, substitution = 1, match = 0;
		int[][] opt = new int[toTest.length() + 1][correct.length() + 1];

		for(int i = 1; i <= toTest.length(); i++) {
			opt[i][0] = opt[i - 1][0] + gap;
		}
		for(int j = 1; j <= correct.length(); j++) {
			opt[0][j] = opt[0][j - 1] + gap;
		}
		for (int i = 1; i <= toTest.length(); i++) {
			for (int j = 1; j <= correct.length(); j++) {
				int scoreDiag = opt[i - 1][j - 1] +
						(toTest.charAt(i-1) == correct.charAt(j-1) ? match : substitution);
				int scoreLeft = opt[i][j - 1] + gap;
				int scoreUp = opt[i - 1][j] + gap;

				opt[i][j] = Math.min(Math.min(scoreDiag, scoreLeft), scoreUp);
			}
		}
		double globalAlignment = opt[toTest.length()][correct.length()];
	//	System.out.println(globalAlignment+" / "+correct.length());
		return globalAlignment;
	//	return (globalAlignment/correct.length());
	}
}