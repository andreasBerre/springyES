package no.bera.springyES.projection;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import no.bera.springyES.Event;
import no.bera.springyES.eventstore.Subscription;
import no.bera.springyES.projection.annotations.EventHandler;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class Projection extends UntypedActor{

    private ActorRef eventStore;
    private String aggregate;
    private Object projectionBean;

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
        Method method = getHandelingMethod(event);
        try {
            method.invoke(projectionBean, event);
        } catch (Exception e) {
            throw new RuntimeException("Error calling event handler " + method.getName() + " in projection " + projectionBean.getClass().getName());
        }
    }

    private Method getHandelingMethod(Event event) {
        List<Method> methods = Arrays.asList( projectionBean.getClass().getMethods());

        System.out.println("looking for handler");

        Method handelingMethod = null;
        Class<?> eventClass = event.getClass();

        while (handelingMethod == null || !eventClass.isAssignableFrom(Event.class)){
            handelingMethod = findAssignableMethod(methods, eventClass);
            eventClass = eventClass.getSuperclass();
        }

        if (handelingMethod == null)
            throw new NoEventHandlerFoundException("No handler of event " + event.getClass().getName() + " could be found in projection " + this.getClass().getName());
        else
            return handelingMethod;
    }

    private Method findAssignableMethod(List<Method> methods, Class<?> event) {
        for (Method m : methods) {
            if (m.isAnnotationPresent(EventHandler.class) && event.isAssignableFrom(getParam(m)))
                    return m;
        }
        return null;
    }

    private Class<?> getParam(Method candidateMethod) {
        List<Class<?>> paramClass = Arrays.asList(candidateMethod.getParameterTypes());

        if (paramClass.size() > 1)
            throw new RuntimeException("Handler " + candidateMethod.getName() + " has more than one parameter. A handler should have exactly one parameter.");
        else if (paramClass.isEmpty())
            throw new RuntimeException("Handler " + candidateMethod.getName() + " has no parameters. A handler should have exactly one parameter.");
        return paramClass.get(0);
    }

    private void sendSubscription() {
        eventStore.tell(new Subscription(aggregate), self());
    }

}
