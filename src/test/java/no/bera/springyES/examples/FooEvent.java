package no.bera.springyES.examples;

import no.bera.springyES.Event;

public class FooEvent extends Event {

    private String id;

    public FooEvent(String id) {
        this.id = id;
    }

    @Override
    public String getAggregate() {
        return "testAggregate";
    }

    @Override
    public String getLogMessage() {
        return "testing testing 123";
    }

    public String getId() {
        return id;
    }
}
