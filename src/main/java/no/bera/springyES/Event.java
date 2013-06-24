package no.bera.springyES;

import org.joda.time.DateTime;

import java.io.Serializable;

abstract public class Event implements Serializable{
    private DateTime created;

    public void setCreated(DateTime created) {
        this.created = created;
    }

    public DateTime getCreated() {
        return created;
    }

    abstract public String getAggregate();

    abstract public String getLogMessage();
}
