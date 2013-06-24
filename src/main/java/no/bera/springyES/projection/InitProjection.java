package no.bera.springyES.projection;

public class InitProjection {

    private final Object projectionBean;
    private final String aggregate;

    public InitProjection(Object projectionBean, String aggregate) {
        this.projectionBean = projectionBean;
        this.aggregate = aggregate;
    }

    public Object getProjectionBean() {
        return projectionBean;
    }

    public String getAggregate() {
        return aggregate;
    }
}
