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

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;
import gov.wa.wsdot.apps.analytics.client.ClientFactory;
import gov.wa.wsdot.apps.analytics.client.activities.coreViews.navigation.NavView;
import gov.wa.wsdot.apps.analytics.client.activities.events.SetDateEvent;
import gov.wa.wsdot.apps.analytics.client.activities.twitter.view.ranking.RankingView;
import gov.wa.wsdot.apps.analytics.client.activities.twitter.view.sentiment.SentimentPieChart;
import gov.wa.wsdot.apps.analytics.client.activities.twitter.view.sources.SourcesPieChart;
import gov.wa.wsdot.apps.analytics.client.activities.twitter.view.summary.SummaryChart;
import gov.wa.wsdot.apps.analytics.client.activities.twitter.view.tweets.TweetsView;
import gwt.material.design.client.ui.*;

import java.util.Date;

public class TwitterViewImpl extends Composite implements TwitterView {

    interface MyEventBinder extends EventBinder<TwitterViewImpl> {}
    private final MyEventBinder eventBinder = GWT.create(MyEventBinder.class);

    private static TwitterViewImplUiBinder uiBinder = GWT
            .create(TwitterViewImplUiBinder.class);

    interface TwitterViewImplUiBinder extends UiBinder<Widget, TwitterViewImpl> {
    }

    @UiField
    MaterialDatePicker dpStart;

    @UiField
    MaterialDatePicker dpEnd;

    @UiField
    MaterialListBox accountPicker;

    @UiField
    MaterialButton submitDateButton;

    @UiField(provided = true)
    NavView nav;

    @UiField(provided = true)
    SummaryChart summaryChart;

    @UiField(provided = true)
    SentimentPieChart sentimentPieChart;

    @UiField(provided = true)
    SourcesPieChart sourcesPieChart;

    @UiField(provided = true)
    TweetsView tweets;

    @UiField(provided = true)
    RankingView ranking;

    private String[] accounts =
                  {"all",
                   "BerthaDigsSR99",
                   "GoodToGoWSDOT",
                   "SnoqualmiePass",
                   "wsdot",
                   "wsdot_520",
                   "WSDOT_East",
                   "wsdot_north",
                   "wsdot_sw",
                   "wsdot_tacoma",
                   "wsdot_traffic",
                   "wsferries"};

    private Presenter presenter;
    private ClientFactory clientFactory;

    public TwitterViewImpl(ClientFactory clientFactory) {

        eventBinder.bindEventHandlers(this, clientFactory.getEventBus());

        nav = new NavView(clientFactory, "home");
        tweets = new TweetsView(clientFactory);
        summaryChart = new SummaryChart(clientFactory);
        sentimentPieChart = new SentimentPieChart(clientFactory);
        sourcesPieChart = new SourcesPieChart(clientFactory);
        ranking = new RankingView(clientFactory);

        this.clientFactory = clientFactory;

        initWidget(uiBinder.createAndBindUi(this));

        accountPicker.setItemSelected(4, true);
    }

    @Override
    public void setPresenter(Presenter p){
        this.presenter = p;
    }

    @EventHandler
    void onSetDate(SetDateEvent event){
        // Only react to this event if we have no dates already (This should only happen when the application starts)
        if(dpStart.getDate() == null || dpEnd.getDate() == null) {
            dpStart.setDate(event.getStartDate());
            dpEnd.setDate(event.getEndDate());
            presenter.onDateSubmit(dpStart.getDate(), dpEnd.getDate(), accounts[accountPicker.getSelectedIndex()]);
        }
    }

    // Setting orientation closes date picker.
    @UiHandler("dpStart")
    protected void onStartDateSelected(ValueChangeEvent<Date> e){
        // Forces date picker to close
        dpStart.setOrientation(dpStart.getOrientation());
    }

    @UiHandler("dpEnd")
    protected void onEndDateSelected(ValueChangeEvent<Date> e){
        // Forces date picker to close
        dpEnd.setOrientation(dpEnd.getOrientation());
    }

    @UiHandler("accountPicker")
    protected void onSelect(ValueChangeEvent<String> e){
        presenter.onDateSubmit(dpStart.getDate(), dpEnd.getDate(), accounts[accountPicker.getSelectedIndex()]);
    }

    @UiHandler("submitDateButton")
    protected void onClick(ClickEvent click){
        presenter.onDateSubmit(dpStart.getDate(), dpEnd.getDate(), accounts[accountPicker.getSelectedIndex()]);
    }
}

