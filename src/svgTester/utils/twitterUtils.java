package svgTester.utils;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import java.util.List;

public class twitterUtils {

    /**
     * Get Timeline Methode for twitter4j API, includes conf. Builder.
     * Template by Twitter4j src. examples
     * @param profileName Profilename of the Twitteraccount
     * @return List of Tweets sended by the given Profile
     */
	public static List<Status> getTimeLine(String profileName) {
		// gets Twitter instance with default credentials
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
				.setDebugEnabled(false)
				.setTweetModeExtended(true)
				.setOAuthConsumerKey("0GA8cO6IgoLQ7n9iWXmjUuIpz")
				.setOAuthConsumerSecret("EiHlitDthTGNgtKJMZu93lI2Jrc0BfhsRXfnxhvp7ST6xWRrPx")
				.setOAuthAccessToken("1266284432-htiSWbTl4F1ZYiwbc10OVgHKpG1dmWOTU6kNHP0")
				.setOAuthAccessTokenSecret("VX45J68DuEhP2vt3c3tAAmiQg9h3FNIDw1hXOXHAYJqlF");
		TwitterFactory tf = new TwitterFactory(cb.build());
		Twitter twitter = tf.getInstance();
		try {
			List<Status> statuses;
			String user;
			user = profileName;
			statuses = twitter.getUserTimeline(user);
			System.out.println("Showing @" + user + "'s user timeline.");
			for (Status status : statuses) {
				System.out.println("@" + status.getUser().getScreenName() + " - " + status.getText());
			}
			return statuses;
		} catch (TwitterException te) {
			te.printStackTrace();
			System.out.println("Failed to get timeline: " + te.getMessage());
			System.exit(-1);
			return null;
		}
	}
}
