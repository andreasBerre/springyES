package no.bera.springyES.persistance;

import no.bera.springyES.Event;
import no.bera.springyES.examples.FooEvent;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.util.List;
import java.util.Map;

import static junit.framework.TestCase.*;


public class JournalStoreTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void testJournalStorePersistsEvents() throws Exception {

        JournalStore journalStore = new JournalStore(folder.newFolder());

        FooEvent testerEvent = new FooEvent("2341234124");

        journalStore.storeEvent(testerEvent);
        Map<String, List<Event>> events = journalStore.getEvents();

        assertNotNull(events);
        assertNotNull(events.get("testAggregate"));
        assertEquals(1, events.get("testAggregate").size());

        Event testEvent = events.get("testAggregate").get(0);
        assertTrue(testEvent instanceof FooEvent);

        assertEquals("2341234124", ((FooEvent) testEvent).getId());
    }
}
