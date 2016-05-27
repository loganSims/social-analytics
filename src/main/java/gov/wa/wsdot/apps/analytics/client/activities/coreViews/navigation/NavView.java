package gov.wa.wsdot.apps.analytics.client.activities.coreViews.navigation;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.place.shared.Place;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.binder.EventBinder;
import gov.wa.wsdot.apps.analytics.client.ClientFactory;
import gov.wa.wsdot.apps.analytics.client.activities.events.GoToEvent;
import gov.wa.wsdot.apps.analytics.client.activities.twitter.home.TwitterPlace;
import gov.wa.wsdot.apps.analytics.client.activities.twitter.search.TwitterSearchPlace;
import gov.wa.wsdot.apps.analytics.client.resources.Resources;
import gwt.material.design.client.ui.MaterialImage;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialToast;


public class NavView extends Composite{


    interface MyEventBinder extends EventBinder<NavView> {}
    private final MyEventBinder eventBinder = GWT.create(MyEventBinder.class);

    private static NavViewUiBinder uiBinder = GWT
            .create(NavViewUiBinder.class);

    interface NavViewUiBinder extends
            UiBinder<Widget, NavView> {
    }

    @UiField
    MaterialImage logo;

    @UiField
    MaterialLink home;

    @UiField
    MaterialLink search;

    ClientFactory clientFactory;

    public NavView(ClientFactory clientFactory, String here) {

        eventBinder.bindEventHandlers(this, clientFactory.getEventBus());

        initWidget(uiBinder.createAndBindUi(this));

        this.clientFactory = clientFactory;

        if (here.equalsIgnoreCase("home")){
            home.setTextColor("blue");
            home.setIconColor("blue");
        }else if (here.equalsIgnoreCase("search")){
            search.setTextColor("blue");
            search.setIconColor("blue");
        }

        logo.setResource(Resources.INSTANCE.tacronymWhiteLogoPNG());
        logo.addStyleName(Resources.INSTANCE.css().logo());
    }

    @UiHandler("home")
    public void onHomeClick(ClickEvent e){
        clientFactory.getEventBus().fireEvent(new GoToEvent(new TwitterPlace("main")));
    }

    @UiHandler("search")
    public void onSearchClick(ClickEvent e){
        clientFactory.getEventBus().fireEvent(new GoToEvent(new TwitterSearchPlace("main")));
    }
}
