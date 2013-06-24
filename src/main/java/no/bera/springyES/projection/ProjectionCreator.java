package no.bera.springyES.projection;

import akka.actor.ActorRef;
import akka.japi.Creator;

public abstract class ProjectionCreator<T extends Projection> implements Creator<T> {

    protected ActorRef eventStore;

    protected ProjectionCreator(ActorRef eventStore) {
        this.eventStore = eventStore;
    }
}
