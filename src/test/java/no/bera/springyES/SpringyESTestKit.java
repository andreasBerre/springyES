package no.bera.springyES;

import akka.actor.ActorSystem;
import akka.testkit.TestKit;
import com.typesafe.config.ConfigFactory;

public class SpringyESTestKit extends TestKit {

	protected static final ActorSystem _system = akka.actor.ActorSystem.create("springyES", ConfigFactory.parseResources("classpath:/application_local.conf"));
	protected static final int DURATION = 10000;

	public SpringyESTestKit() {
		super(_system);
	}
}
