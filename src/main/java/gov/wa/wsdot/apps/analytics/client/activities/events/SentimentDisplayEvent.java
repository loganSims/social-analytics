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
package gov.wa.wsdot.apps.analytics.client.activities.events;
import com.google.web.bindery.event.shared.binder.GenericEvent;

public class SentimentDisplayEvent extends GenericEvent {

    String sentiment;

    public SentimentDisplayEvent(String sentiment){
        this.sentiment = sentiment;
    }

    public String getSentiment(){
        return sentiment;
    }
}
