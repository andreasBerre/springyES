package no.bera.springyES.projection;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;

import java.util.List;

public class ProjectionManager extends UntypedActor {
    private final ActorRef eventStore;

    public static Props mkProps(ActorRef eventStore, List<InitProjection> projections) {
        return Props.create(ProjectionManager.class, eventStore, projections);
    }

    public ProjectionManager(ActorRef eventsStore, List<InitProjection> projections) {
        this.eventStore = eventsStore;
        createProjections(projections);
    }

    private void createProjections(List<InitProjection> projectionsInits) {
        for (InitProjection initProjection : projectionsInits)
            createProjection(initProjection);
    }

    private void createProjection(InitProjection initProjection) {
        context().system().actorOf(Projection.mkProps(eventStore, initProjection.getAggregate(), initProjection.getProjectionBean()));
    }

    @Override
    public void onReceive(Object o) throws Exception {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}

