package svgTester.webPages;

import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

public class getPageContent {

    /**
     * Methode to get the html form a given url
     * @param src Url
     * @return html of the given url
     */
	public static String getContents(String src) {
		String content = "";
		URLConnection connection = null;
		try {
			connection =  new URL(src).openConnection();
			Scanner scanner = new Scanner(connection.getInputStream());
			scanner.useDelimiter("\\Z");
			content = scanner.next();
		}catch ( Exception ex ) {
			ex.printStackTrace();
		}
		return content;
	}
}
