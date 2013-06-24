package no.bera.springyES.examples;

import no.bera.springyES.Event;

public class BarEvent extends Event {
    private final String id;

    public BarEvent(String id) {
        this.id = id;
    }

    @Override
    public String getAggregate() {
        return "testAggregate";
    }

    @Override
    public String getLogMessage() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getId() {
        return id;
    }
}
