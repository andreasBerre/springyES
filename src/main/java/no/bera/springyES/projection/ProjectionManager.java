package no.bera.springyES.projection;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;

import java.util.List;

public class ProjectionManager extends UntypedActor {
    private final ActorRef eventStore;
    private final ActorSystem system;

    public ProjectionManager(ActorSystem system, ActorRef eventsStore, List<InitProjection> projections) {
        this.eventStore = eventsStore;
        this.system = system;
        createProjections(projections);
    }

    private void createProjections(List<InitProjection> projectionsInits) {
        for (InitProjection initProjection : projectionsInits)
            createProjection(initProjection);
    }

    private void createProjection(InitProjection initProjection) {
        system.actorOf(Props.create(Projection.class, eventStore, initProjection.getAggregate(), initProjection.getProjectionBean()));
    }

    @Override
    public void onReceive(Object o) throws Exception {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
