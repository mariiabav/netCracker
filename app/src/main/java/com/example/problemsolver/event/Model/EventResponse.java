
package com.example.problemsolver.event.Model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EventResponse {

    @SerializedName("events")
    @Expose
    private List<Event> events = null;
    @SerializedName("pagesLimit")
    @Expose
    private Integer pagesLimit;

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public Integer getPagesLimit() {
        return pagesLimit;
    }

    public void setPagesLimit(Integer pagesLimit) {
        this.pagesLimit = pagesLimit;
    }

}
