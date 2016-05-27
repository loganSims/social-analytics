package gov.wa.wsdot.apps.analytics.client.activities.twitter.search;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.jsonp.client.JsonpRequestBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.binder.EventBinder;
import gov.wa.wsdot.apps.analytics.client.ClientFactory;
import gov.wa.wsdot.apps.analytics.client.activities.coreViews.navigation.NavView;
import gov.wa.wsdot.apps.analytics.client.activities.twitter.view.tweet.TweetView;
import gov.wa.wsdot.apps.analytics.client.resources.Resources;
import gov.wa.wsdot.apps.analytics.shared.Mention;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.ui.*;

import java.util.Date;

public class TwitterSearchViewImpl extends Composite implements TwitterSearchView {

    interface MyEventBinder extends EventBinder<TwitterSearchViewImpl> {}
    private final MyEventBinder eventBinder = GWT.create(MyEventBinder.class);

    private static TwitterSearchViewImplUiBinder uiBinder = GWT
            .create(TwitterSearchViewImplUiBinder.class);

    interface TwitterSearchViewImplUiBinder extends UiBinder<Widget, TwitterSearchViewImpl> {
    }

    @UiField(provided = true)
    NavView nav;

    @UiField
    static
    MaterialPreLoader searchLoader;

    @UiField
    static
    HTMLPanel searchList;

    @UiField
    static
    MaterialButton moreSearchBtn;

    @UiField
    static
    MaterialButton backToSearchTopBtn;

    @UiField
    static
    MaterialLink exportLink;

    @UiField
    static
    MaterialButton clearSearchBtn;

    @UiField
    static
    MaterialButton searchBtn;

    @UiField
    static
    MaterialListBox searchAccountPicker;

    @UiField
    static
    MaterialListBox searchTypePicker;

    @UiField
    static
    MaterialDatePicker searchdpStart;

    @UiField
    static
    MaterialDatePicker searchdpEnd;

    @UiField
    static
    MaterialTextBox searchTextBox;

    @UiField
    static
    MaterialCheckBox mediaOnly;

    final Resources res;

    private static int pageNum = 1;
    private static ClientFactory clientFactory;

    Presenter presenter;

    public TwitterSearchViewImpl(ClientFactory clientFactory) {
        eventBinder.bindEventHandlers(this, clientFactory.getEventBus());
        nav = new NavView(clientFactory, "search");

        res = GWT.create(Resources.class);
        res.css().ensureInjected();
        eventBinder.bindEventHandlers(this, clientFactory.getEventBus());
        initWidget(uiBinder.createAndBindUi(this));
        this.clientFactory = clientFactory;

    }

    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }


    @UiHandler("searchBtn")
    void onSearch(ClickEvent e){

        if (searchTextBox.getText().equalsIgnoreCase("")){
            searchTextBox.setText(" ");
        }

        DateTimeFormat fmt = DateTimeFormat.getFormat("/yyyy/M/d");

        String endDate = null;
        String startDate = null;

        if (searchdpStart.getDate() != null) {
            startDate = fmt.format(searchdpStart.getDate());
        }

        if (searchdpEnd.getDate() != null) {
            endDate = fmt.format(searchdpEnd.getDate());
        }

        String account = searchAccountPicker.getSelectedItemText();
        int searchType = Integer.valueOf(searchTypePicker.getSelectedValue());
        String searchTerm = searchTextBox.getText();

        int media = (mediaOnly.getValue() ? 1 : 0);

        presenter.makeUrl(searchTerm, searchType, account, media, startDate, endDate);
    }


    public void search(String url) {
        pageNum = 1;
        backToSearchTopBtn.setVisible(false);
        searchList.clear();
        moreSearchBtn.setVisible(false);
        exportLink.setVisible(false);

        searchLoader.setVisible(true);

        JsonpRequestBuilder jsonp = new JsonpRequestBuilder();
        // Set timeout for 30 seconds (30000 milliseconds)
        jsonp.setTimeout(30000);
        jsonp.requestObject(url + pageNum, new AsyncCallback<Mention>() {

            @Override
            public void onFailure(Throwable caught) {
                Window.alert("Failure: " + caught.getMessage());
                searchLoader.setVisible(false);
            }

            @Override
            public void onSuccess(Mention mention) {
                if (mention.getMentions() != null) {
                    updateSearch(mention.getMentions());
                    searchLoader.setVisible(false);
                }
            }
        });
    }

    // Setting orientation closes date picker.
    @UiHandler("searchdpStart")
    protected void onStartDateSelected(ValueChangeEvent<Date> e){
        // Forces date picker to close
        searchdpStart.setOrientation(searchdpStart.getOrientation());
    }

    @UiHandler("searchdpEnd")
    protected void onEndDateSelected(ValueChangeEvent<Date> e){
        // Forces date picker to close
        searchdpEnd.setOrientation(searchdpEnd.getOrientation());
    }

    @UiHandler("clearSearchBtn")
    protected void clearSearch(ClickEvent e) {
        searchTextBox.clear();
        searchdpEnd.clear();
        searchdpStart.clear();
        searchAccountPicker.setSelectedIndex(0);
        mediaOnly.setValue(false);
    }

    @UiHandler("exportLink")
    protected void onExport(ClickEvent e){
        presenter.export();
    }



    @UiHandler("moreSearchBtn")
    public void onMore(ClickEvent e){

        int nextPage = pageNum + 1;

        searchLoader.setVisible(true);

        JsonpRequestBuilder jsonp = new JsonpRequestBuilder();
        // Set timeout for 30 seconds (30000 milliseconds)
        jsonp.setTimeout(30000);
        jsonp.requestObject(presenter.getUrl() + nextPage, new AsyncCallback<Mention>() {

            @Override
            public void onFailure(Throwable caught) {
                Window.alert("Failure: " + caught.getMessage());
                searchLoader.setVisible(false);
            }

            @Override
            public void onSuccess(Mention mention) {
                if (mention.getMentions() != null) {
                    pageNum++;
                    updateSearch(mention.getMentions());
                    searchLoader.setVisible(false);
                }
            }
        });
    }

    public static void updateSearch(JsArray<Mention> asArrayOfMentionData) {

        int j = asArrayOfMentionData.length();

        if (j > 24){
            backToSearchTopBtn.setVisible(true);
        }

        DateTimeFormat dateTimeFormat = DateTimeFormat.getFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        DateTimeFormat dateTimeFormat2 = DateTimeFormat.getFormat("MMMM dd, yyyy h:mm:ss a");

        String urlPattern = "(https?:\\/\\/[-a-zA-Z0-9._~:\\/?#@!$&\'()*+,;=%]+)";
        String atPattern = "@+([_a-zA-Z0-9-]+)";
        String hashPattern = "#+([_a-zA-Z0-9-]+)";
        String text;
        String updatedText;
        String screenName;
        String mediaUrl;

        for (int i = 0; i < j; i++) {

            screenName = (asArrayOfMentionData.get(i).getFromUser() != null) ?
                    asArrayOfMentionData.get(i).getFromUser() :
                    asArrayOfMentionData.get(i).getUser().getScreenName();

            TweetView tweet;

            text = asArrayOfMentionData.get(i).getText();
            updatedText = text.replaceAll(urlPattern, "<a href=\"$1\" target=\"_blank\">$1</a>");
            updatedText = updatedText.replaceAll(atPattern, "<a href=\"http://twitter.com/#!/$1\" target=\"_blank\">@$1</a>");
            updatedText = updatedText.replaceAll(hashPattern, "<a href=\"http://twitter.com/#!/search?q=%23$1\" target=\"_blank\">#$1</a>");

            String createdAt = dateTimeFormat2.format(dateTimeFormat.parse(asArrayOfMentionData.get(i).getCreatedAt()));

            String link = "http://twitter.com/#!/" + screenName + "/status/" + asArrayOfMentionData.get(i).getIdStr();

            mediaUrl = null;
            try {
                for (int k = 0; k < asArrayOfMentionData.get(i).getEntities().getMedia().length(); k++) {
                    mediaUrl =  asArrayOfMentionData.get(i).getEntities().getMedia().get(k).getMediaUrl();
                }
            } catch (Exception e) {} // Image preview is nice, but if it fails...oh well.

            String id = asArrayOfMentionData.get(i).getIdStr();

            if (asArrayOfMentionData.get(i).getSentiment().equals("positive")) {
                tweet = new TweetView(id, screenName, updatedText, createdAt, link, mediaUrl, IconType.SENTIMENT_SATISFIED);
            } else if (asArrayOfMentionData.get(i).getSentiment().equals("negative")) {
                tweet = new TweetView(id, screenName, updatedText, createdAt, link, mediaUrl, IconType.SENTIMENT_DISSATISFIED);
            } else {
                tweet = new TweetView(id, screenName, updatedText, createdAt, link, mediaUrl, IconType.SENTIMENT_NEUTRAL);
            }
            searchList.add(tweet);
        }

        if (j > 0){
            exportLink.setVisible(true);
        }

        if (j < 25){
            moreSearchBtn.setVisible(false);
        } else {
            moreSearchBtn.setVisible(true);
        }
    }

    @UiHandler("backToSearchTopBtn")
    public void onBackToTop(ClickEvent e){
        Window.scrollTo(0,0);
    }

}
