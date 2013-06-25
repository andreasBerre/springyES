package no.bera.springyES.projection;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import no.bera.springyES.Event;
import no.bera.springyES.eventstore.Subscription;
import no.bera.springyES.projection.annotations.Handler;
import no.bera.springyES.util.HandlerReflectionUtil;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class Projection extends UntypedActor{

    private ActorRef eventStore;
    private String aggregate;
    private Object projectionBean;

    public static Props mkProps(ActorRef eventStore, String aggregate, Object projectionBean) {
        return Props.create(Projection.class, eventStore, aggregate, projectionBean);
    }

    public Projection(ActorRef eventStore, String aggregate, Object projectionBean) {
        this.eventStore = eventStore;
        this.aggregate = aggregate;
        this.projectionBean = projectionBean;
    }

    @Override
    public void preStart() throws Exception {
        sendSubscription();
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof Event){
            delegateEvent((Event) message);
        }
    }

    private void delegateEvent(Event event) {
        Method method = HandlerReflectionUtil.getHandelingMethod(Event.class, event.getClass(), projectionBean.getClass(), true);
        try {
            method.invoke(projectionBean, event);
        } catch (Exception e) {
            throw new RuntimeException("Error calling event handler " + method.getName() + " in projection " + projectionBean.getClass().getName());
        }
    }


    private void sendSubscription() {
        eventStore.tell(new Subscription(aggregate), self());
    }

}
