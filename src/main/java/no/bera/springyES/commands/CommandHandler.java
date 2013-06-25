package no.bera.springyES.commands;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import no.bera.springyES.util.HandlerReflectionUtil;

import java.lang.reflect.Method;

public class CommandHandler extends UntypedActor {

    private final ActorRef eventStore;
    private final Object commandHandlerBean;

    public static Props mkProps(ActorRef eventStore, Object commandHandlerBean) {
        return Props.create(CommandHandler.class, eventStore, commandHandlerBean);
    }

    public CommandHandler(ActorRef eventStore, Object commandHandlerBean) {
        this.eventStore = eventStore;
        this.commandHandlerBean = commandHandlerBean;
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof Command){
            delegateCommand((Command) message);
        }
    }

    private void delegateCommand(Command command) {
        Method method = HandlerReflectionUtil.getHandelingMethod(Command.class, command.getClass(), commandHandlerBean.getClass(), false);
        try {
            method.invoke(commandHandlerBean, command);
        } catch (Exception e) {
            throw new RuntimeException("Error calling handler " + method.getName() + " in command handler " + commandHandlerBean.getClass().getName());
        }
    }

}
