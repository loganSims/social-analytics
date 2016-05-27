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
package gov.wa.wsdot.apps.analytics.client;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import gov.wa.wsdot.apps.analytics.client.activities.twitter.home.TwitterActivity;
import gov.wa.wsdot.apps.analytics.client.activities.twitter.home.TwitterPlace;
import gov.wa.wsdot.apps.analytics.client.activities.twitter.search.TwitterSearchActivity;
import gov.wa.wsdot.apps.analytics.client.activities.twitter.search.TwitterSearchPlace;

public class AppActivityMapper implements ActivityMapper {
    private ClientFactory clientFactory;

    public AppActivityMapper(ClientFactory clientFactory) {
        super();
        this.clientFactory = clientFactory;
    }

    @Override
    public Activity getActivity(Place place) {
        if (place instanceof TwitterPlace)
            return new TwitterActivity((TwitterPlace) place, clientFactory);
        else if (place instanceof TwitterSearchPlace)
            return new TwitterSearchActivity((TwitterSearchPlace) place, clientFactory);

        return new TwitterActivity((TwitterPlace) place, clientFactory);
    }
}
