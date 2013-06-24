package no.bera.springyES.init;

import akka.actor.ActorSystem;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SystemInitializer {

    private ActorSystem system;

    public SystemInitializer() {
        initializeActorSystem();
    }

    private void initializeActorSystem() {
        system = ActorSystem.create("springyES");
    }

    @Bean()
    public ActorSystem getActorSystem(){
        return system;
    }
}
