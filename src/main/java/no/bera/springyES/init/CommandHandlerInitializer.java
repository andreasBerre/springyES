package no.bera.springyES.init;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import no.bera.springyES.commands.CommandDispatcher;
import no.bera.springyES.commands.annotations.CommandHandler;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CommandHandlerInitializer implements ApplicationContextAware, InitializingBean {
    private ApplicationContext applicationContext;

    @Resource
    ActorSystem system;

    @Resource
    ActorRef eventStore;

    @Override
    public void afterPropertiesSet() throws Exception {
        createCommandHandlerManager(new ArrayList<Object>(applicationContext.getBeansWithAnnotation(CommandHandler.class).values()));
    }

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    private void createCommandHandlerManager(List<Object> commandHandlers) {
        system.actorOf(CommandDispatcher.mkProps(eventStore, commandHandlers));
    }
}