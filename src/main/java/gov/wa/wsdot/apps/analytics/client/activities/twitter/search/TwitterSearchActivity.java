package gov.wa.wsdot.apps.analytics.client.activities.twitter.search;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;
import gov.wa.wsdot.apps.analytics.client.ClientFactory;
import gov.wa.wsdot.apps.analytics.client.activities.events.GoToEvent;
import gov.wa.wsdot.apps.analytics.util.Consts;

/**
 * Created by simsl on 5/26/16.
 */
public class TwitterSearchActivity extends AbstractActivity implements TwitterSearchView.Presenter {

    interface MyEventBinder extends EventBinder<TwitterSearchActivity> {
    }

    private final MyEventBinder eventBinder = GWT.create(MyEventBinder.class);

    private final ClientFactory clientFactory;
    private String name;

    private String url = "";
    private String searchText = "";

    private TwitterSearchView view;

    public TwitterSearchActivity(TwitterSearchPlace place, ClientFactory clientFactory) {
        this.name = place.getTwitterSearchName();
        this.clientFactory = clientFactory;
        eventBinder.bindEventHandlers(this, clientFactory.getEventBus());
    }

    @Override
    public void start(AcceptsOneWidget panel, EventBus eventBus) {
        view = clientFactory.getTwitterSearchView();
        view.setPresenter(this);
        panel.setWidget(view.asWidget());

    }

    @EventHandler
    void goTo(GoToEvent event) {
        clientFactory.getPlaceController().goTo(event.getPlace());
    }

    /**
     * Constructs a url for searching tweets
     *
     * the url has the form:
     *
     *  HOST_URL/search/:text/:screenName/:collection/:fromYear/:fromMonth/:fromDay/:toYear/:toMonth/:toDay/:page
     *
     */
    @Override
    public void makeUrl(String searchTerm, int searchType, String account, int media, String startDate, String endDate) {

        searchTerm = URL.encodePathSegment(searchTerm);

        url = Consts.HOST_URL + "/search/" + searchTerm + "/";

        url = url + account + "/" + ((searchType == 2) ? "mentions" : "statuses");

        url = url + "/" + media;

        if (startDate != null && endDate != null){
            url = url + startDate + endDate + "/";
        }else if (endDate != null){
            url = url + "/0/0/0" + endDate + "/";
        }else if (startDate != null){
            url = url + startDate + "/0/0/0/";
        }else{
            url = url + "/0/0/0/0/0/0/";
        }
        view.search(url);
    }

    @Override
    public String getUrl(){
        return this.url;
    }

    @Override
    public void export() {
        Window.open(url,"_blank","");
    }
}
