package no.bera.springyES.examples;


import no.bera.springyES.Event;
import no.bera.springyES.projection.annotations.Callable;
import no.bera.springyES.projection.annotations.EventHandler;
import no.bera.springyES.projection.annotations.Projection;

import java.util.ArrayList;
import java.util.List;

@Projection("testAggregate")
public class TestProjection {

    private List<Event> fooEvents = new ArrayList<Event>();
    private List<Event> barEvents = new ArrayList<Event>();
    private List<Event> bazEvents = new ArrayList<Event>();

    @EventHandler
    public void handleEvent(FooEvent event){
        fooEvents.add(event);
    }

    @EventHandler
    public void handleEvent(BarEvent event){
        barEvents.add(event);
    }

    @EventHandler
    public void handleEvent(BazEvent event){
        bazEvents.add(event);
    }

    @Callable
    public List<Event> getFooEvents() {
        return fooEvents;
    }

    @Callable
    public List<Event> getBarEvents() {
        return barEvents;
    }

    @Callable
    public List<Event> getBazEvents() {
        return bazEvents;
    }


}
