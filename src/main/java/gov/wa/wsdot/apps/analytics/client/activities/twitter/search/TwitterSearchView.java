package gov.wa.wsdot.apps.analytics.client.activities.twitter.search;

import com.google.gwt.user.client.ui.IsWidget;

/**
 * Created by simsl on 5/26/16.
 */
public interface TwitterSearchView extends IsWidget {

    void setPresenter(Presenter presenter);

    void search(String url);

    interface Presenter {

        void export();
        String getUrl();
        void makeUrl(String searchTerm, int searchType, String account, int media, String startDate, String endDate);

    }
}
