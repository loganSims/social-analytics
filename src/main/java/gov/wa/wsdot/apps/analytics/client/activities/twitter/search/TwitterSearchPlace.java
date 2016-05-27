package gov.wa.wsdot.apps.analytics.client.activities.twitter.search;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

public class TwitterSearchPlace extends Place {
    private String TwitterSearchName;

    public TwitterSearchPlace(String token) {
        this.TwitterSearchName = token;
    }

    public String getTwitterSearchName() {
        return TwitterSearchName;
    }

    @Prefix("TwitterSearch")
    public static class Tokenizer implements PlaceTokenizer<TwitterSearchPlace> {
        @Override
        public String getToken(TwitterSearchPlace place) {
            return place.getTwitterSearchName();
        }

        @Override
        public TwitterSearchPlace getPlace(String token) {
            return new TwitterSearchPlace(token);
        }
    }

}
