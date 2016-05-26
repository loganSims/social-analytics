package gov.wa.wsdot.apps.analytics.client.activities.twitter.search;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import gov.wa.wsdot.apps.analytics.client.ClientFactory;

/**
 * Created by simsl on 5/26/16.
 */
public class TwitterSearchActivity extends AbstractActivity implements TwitterSearchView.Presenter {

    private final ClientFactory clientFactory;
    private String name;

    private TwitterSearchView view;

    public TwitterSearchActivity(TwitterSearchPlace place, ClientFactory clientFactory) {
        this.name = place.getTwitterSearchName();
        this.clientFactory = clientFactory;
    }

    @Override
    public void start(AcceptsOneWidget panel, EventBus eventBus) {
        view = clientFactory.getTwitterSearchView();
        view.setPresenter(this);
        panel.setWidget(view.asWidget());

    }


}
