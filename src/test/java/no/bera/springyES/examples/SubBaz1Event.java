package no.bera.springyES.examples;

public class SubBaz1Event extends BazEvent {

    public SubBaz1Event(String id) {
        super(id);
    }

    @Override
    public String getAggregate() {
        return "testAggregate";
    }

    @Override
    public String getLogMessage() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

}
