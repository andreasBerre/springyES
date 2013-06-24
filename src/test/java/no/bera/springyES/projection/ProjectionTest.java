package no.bera.springyES.projection;

import akka.actor.Props;
import akka.testkit.TestActorRef;
import no.bera.springyES.Event;
import no.bera.springyES.SpringyESTestKit;
import no.bera.springyES.examples.*;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.UUID;

import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

public class ProjectionTest extends SpringyESTestKit {

    private TestActorRef<Projection> projection;
    private TestProjection testProjection;

    @Before
    public void setup(){
        testProjection = new TestProjection();
        projection = TestActorRef.create(_system, Props.create(Projection.class, super.testActor(), "testAggregate", testProjection), UUID.randomUUID().toString());
    }

    @Test
    public void testThatAnEventIsDelegatedToCorrectHandler() throws Exception{
        projection.tell(new FooEvent("a foo event"), super.testActor());
        projection.tell(new BarEvent("a bar event"), super.testActor());

        List<Event> fooEvents = testProjection.getFooEvents();
        assertNotNull(fooEvents);
        assertEquals(1, fooEvents.size());
        Event fooEvent = fooEvents.get(0);
        assertTrue(fooEvent instanceof FooEvent);
        assertEquals("a foo event", ((FooEvent) fooEvent).getId());

        List<Event> barEvents = testProjection.getBarEvents();
        assertNotNull(barEvents);
        assertEquals(1, barEvents.size());
        Event barEvent = barEvents.get(0);
        assertTrue(barEvent instanceof BarEvent);
        assertEquals("a bar event", ((BarEvent) barEvent).getId());
    }

    @Test
    public void testThatSubclassesOfHandledEventClassAreDelegatedToCorrectHandler() throws Exception{
        projection.tell(new SubBaz1Event("a subBaz1 event"), super.testActor());
        projection.tell(new SubBaz2Event("a subBaz2 event"), super.testActor());

        List<Event> bazEvents = testProjection.getBazEvents();
        assertNotNull(bazEvents);
        assertEquals(2, bazEvents.size());
        assertTrue(bazEvents.get(0) instanceof SubBaz1Event);
        assertTrue(bazEvents.get(1) instanceof SubBaz2Event);
    }
}
