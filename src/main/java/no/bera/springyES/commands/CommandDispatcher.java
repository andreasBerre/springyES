package no.bera.springyES.commands;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import no.bera.springyES.eventstore.EventStore;

import java.util.List;

public class CommandDispatcher extends UntypedActor{

    private final ActorRef eventStore;

    public static Props mkProps(ActorRef eventStore, List<Object> commandHandlerBeans) {
        return Props.create(CommandDispatcher.class, eventStore, commandHandlerBeans);
    }

    public CommandDispatcher(ActorRef eventStore, List<Object> commandHandlerBeans) {
        this.eventStore = eventStore;

        for (Object commandHandlerBean : commandHandlerBeans){
            initializeCommandHandler(commandHandlerBean);
        }

    }

    private void initializeCommandHandler(Object commandHandlerBean) {
        context().system().actorOf(CommandHandler.mkProps(eventStore, commandHandlerBean));

    }

    @Override
    public void onReceive(Object o) throws Exception {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
