package gov.wa.wsdot.apps.analytics.client.activities.twitter.search;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;
import gov.wa.wsdot.apps.analytics.client.ClientFactory;
import gov.wa.wsdot.apps.analytics.client.activities.events.GoToEvent;

/**
 * Created by simsl on 5/26/16.
 */
public class TwitterSearchActivity extends AbstractActivity implements TwitterSearchView.Presenter {

    interface MyEventBinder extends EventBinder<TwitterSearchActivity> {}
    private final MyEventBinder eventBinder = GWT.create(MyEventBinder.class);

    private final ClientFactory clientFactory;
    private String name;

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
    void goTo(GoToEvent event){
        clientFactory.getPlaceController().goTo(event.getPlace());
    }
}
