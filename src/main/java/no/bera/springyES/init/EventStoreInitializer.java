package no.bera.springyES.init;


import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import no.bera.springyES.eventstore.EventStore;
import no.bera.springyES.persistance.JournalStore;
import no.bera.springyES.persistance.Store;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;

@Configuration
public class EventStoreInitializer {

    @Autowired
    private ActorSystem actorSystem;

    @Bean(name="eventStore")
    public ActorRef createEventStore(){
        Store journalStore = new JournalStore(new File(System.getProperty("java.io.tmpdir")));
        return actorSystem.actorOf(Props.create(EventStore.class, journalStore));
    }

}
