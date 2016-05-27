package gov.wa.wsdot.apps.analytics.client.activities.twitter.search;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.binder.EventBinder;
import gov.wa.wsdot.apps.analytics.client.ClientFactory;
import gov.wa.wsdot.apps.analytics.client.activities.coreViews.navigation.NavView;

public class TwitterSearchViewImpl extends Composite implements TwitterSearchView {

    interface MyEventBinder extends EventBinder<TwitterSearchViewImpl> {}
    private final MyEventBinder eventBinder = GWT.create(MyEventBinder.class);

    private static TwitterSearchViewImplUiBinder uiBinder = GWT
            .create(TwitterSearchViewImplUiBinder.class);

    interface TwitterSearchViewImplUiBinder extends UiBinder<Widget, TwitterSearchViewImpl> {
    }

    @UiField(provided = true)
    NavView nav;

    Presenter presenter;

    public TwitterSearchViewImpl(ClientFactory clientFactory) {
        eventBinder.bindEventHandlers(this, clientFactory.getEventBus());
        nav = new NavView(clientFactory, "search");
        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }
}
