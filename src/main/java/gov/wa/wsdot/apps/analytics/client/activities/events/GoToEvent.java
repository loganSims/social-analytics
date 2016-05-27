package gov.wa.wsdot.apps.analytics.client.activities.events;

import com.google.gwt.place.shared.Place;
import com.google.web.bindery.event.shared.binder.GenericEvent;

/**
 * Created by simsl on 5/27/16.
 */
public class GoToEvent extends GenericEvent {
    private final Place place;

    public GoToEvent(Place place) {
        this.place = place;
    }

    public Place getPlace() {
        return this.place;
    }

}
