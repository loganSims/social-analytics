/*
 * Copyright (c) 2016 Washington State Department of Transportation
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 *
 */

package gov.wa.wsdot.apps.analytics.client.activities.twitter.home;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.jsonp.client.JsonpRequestBuilder;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;
import gov.wa.wsdot.apps.analytics.client.ClientFactory;
import gov.wa.wsdot.apps.analytics.client.activities.events.DateSubmitEvent;
import gov.wa.wsdot.apps.analytics.client.activities.events.GoToEvent;
import gov.wa.wsdot.apps.analytics.client.activities.events.SetDateEvent;
import gov.wa.wsdot.apps.analytics.shared.TweetTimes;
import gov.wa.wsdot.apps.analytics.util.Consts;
import gwt.material.design.client.ui.MaterialToast;

import java.util.Date;

/**
 *  Main twitter analytics activity
 */
public class TwitterActivity extends AbstractActivity implements TwitterView.Presenter {

    interface MyEventBinder extends EventBinder<TwitterActivity> {}
    private final MyEventBinder eventBinder = GWT.create(MyEventBinder.class);

    private final ClientFactory clientFactory;
    private String name;

    private TwitterView view;

    public TwitterActivity(TwitterPlace place, ClientFactory clientFactory) {
        this.name = place.getTwitterName();
        this.clientFactory = clientFactory;
        eventBinder.bindEventHandlers(this, clientFactory.getEventBus());
    }

    @Override
    public void start(AcceptsOneWidget panel, EventBus eventBus) {
        view = clientFactory.getTwitterView();
        view.setPresenter(this);
        panel.setWidget(view.asWidget());
        getStartDate();
    }

    @Override
    public void onDateSubmit(Date startDate, Date endDate, String account) {

        DateTimeFormat fmt = DateTimeFormat.getFormat("/yyyy/M/d");

        if (startDate.getTime() > endDate.getTime()) {
            MaterialToast.fireToast("Whoops! Invalid date range.", 1000);
        } else {
            String fromDate = fmt.format(startDate);
            String toDate = fmt.format(endDate);
            String dateRange = fromDate + toDate;

            clientFactory.getEventBus().fireEvent(new DateSubmitEvent(dateRange, startDate, endDate, account));
        }
    }

    @EventHandler
    void goTo(GoToEvent event){
        clientFactory.getPlaceController().goTo(event.getPlace());
    }

    public void getStartDate(){

        String url = Consts.HOST_URL + "/summary/startTime";

        JsonpRequestBuilder jsonp = new JsonpRequestBuilder();
        // Set timeout for 30 seconds (30000 milliseconds)
        jsonp.setTimeout(30000);
        jsonp.requestObject(url, new AsyncCallback<TweetTimes>() {

            @Override
            public void onFailure(Throwable caught) {
                Window.alert("Failure: " + caught.getMessage());
            }

            @Override
            public void onSuccess(TweetTimes result) {
                // Fire SetDateEvent to change date picker to default date from server
                DateTimeFormat dateTimeFormat = DateTimeFormat.getFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
                Date startDate = dateTimeFormat.parse(result.getStartDate());
                Date endDate = dateTimeFormat.parse(result.getEndDate());

                clientFactory.getEventBus().fireEvent(new SetDateEvent(startDate, endDate));
            }
        });
    }

    @Override
    public EventBus getEventBus() {
        return clientFactory.getEventBus();
    }
}