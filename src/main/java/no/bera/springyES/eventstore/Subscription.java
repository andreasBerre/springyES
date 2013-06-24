package no.bera.springyES.eventstore;

public class Subscription {
    private String aggregate;

    public Subscription(String aggregate) {
        this.aggregate = aggregate;
    }

    public String getAggregate() {
        return aggregate;
    }

    public void setAggregate(String aggregate) {
        this.aggregate = aggregate;
    }
}
