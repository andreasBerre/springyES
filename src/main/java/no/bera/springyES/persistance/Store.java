package no.bera.springyES.persistance;


import no.bera.springyES.Event;

import java.util.List;
import java.util.Map;

public interface Store {

    void storeEvent(Event event);
    Map<String, List<Event>> getEvents();
}
