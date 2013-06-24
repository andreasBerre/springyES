package no.bera.springyES.persistance;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.CompatibleFieldSerializer;
import de.javakaffee.kryoserializers.jodatime.JodaDateTimeSerializer;
import journal.io.api.Journal;
import journal.io.api.Location;
import no.bera.springyES.Event;
import org.apache.commons.io.IOUtils;
import org.joda.time.DateTime;
import org.objenesis.strategy.SerializingInstantiatorStrategy;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.*;

public class JournalStore implements Store{

    private Kryo kryo = new Kryo();

    private Journal journal = new Journal();

    public JournalStore(File journalDir) {
        initiateJournal(journalDir);
        configureKryo();
    }

    private void configureKryo() {
        kryo.setInstantiatorStrategy(new SerializingInstantiatorStrategy());
        kryo.setDefaultSerializer(CompatibleFieldSerializer.class);
        kryo.register(DateTime.class, new JodaDateTimeSerializer());
    }

    private void initiateJournal(File journalDir) {
        try {
            journal.setDirectory(journalDir);
            journal.open();
        } catch (Exception e){
            throw new RuntimeException("Error initializing journal");
        }
    }

    @Override
    public void storeEvent(Event event) {

        event.setCreated(new DateTime());

        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        Output kryodata = new Output(output);
        kryo.writeClassAndObject(kryodata, event);
        kryodata.close();

        try {
            journal.write(output.toByteArray(), Journal.WriteType.SYNC);
        } catch (Exception e) {
            throw new RuntimeException("Error while writing event to journal", e);
        }
    }

    @Override
    public Map<String, List<Event>> getEvents() {
        Input input = null;
        Map<String, List<Event>> events = new HashMap<String, List<Event>>();

        try {
            for (Location location : journal.redo()) {
                input = new Input(journal.read(location, Journal.ReadType.ASYNC));
                Event event = (Event) kryo.readClassAndObject(input);
                if (events.get(event.getAggregate()) == null)
                    events.put(event.getAggregate(), new ArrayList<Event>());

                events.get(event.getAggregate()).add(event);
            }
        } catch (Exception e){
            throw new RuntimeException("Error while reading events from journal", e);
        } finally {
            IOUtils.closeQuietly(input);
        }

        return events;
    }

}
