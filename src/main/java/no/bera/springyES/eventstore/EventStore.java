package no.bera.springyES.eventstore;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import no.bera.springyES.Event;
import no.bera.springyES.persistance.Store;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventStore extends UntypedActor {

    private final Store store;
    private Map<String, List<ActorRef>> subscribers = new HashMap<String, List<ActorRef>>();

    public EventStore(Store store) {
        this.store = store;
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof Event)
            storeEvent((Event) message);
        else if (message instanceof Subscription)
            addSubscriber((Subscription) message);
    }

    private void addSubscriber(Subscription subscription) {
        if (subscribers.get(subscription.getAggregate()) == null)
            subscribers.put(subscription.getAggregate(), new ArrayList<ActorRef>());

        subscribers.get(subscription.getAggregate()).add(sender());
        publishEventsToNewSubscriber(subscription.getAggregate());
    }

    private void publishEventsToNewSubscriber(String aggregate) {
        List<Event> events = store.getEvents().get(aggregate);
        if (events != null && !events.isEmpty()) {
            for (Event event : events){
                publishEvent(event);
            }
        }
    }

    private void storeEvent(Event event) {
        store.storeEvent(event);
        publishEvent(event);
    }

    private void publishEvent(Event event) {
        for (ActorRef subscriber : subscribers.get(event.getAggregate()))
            subscriber.tell(event, self());
    }

}
