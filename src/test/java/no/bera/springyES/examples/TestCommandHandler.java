package no.bera.springyES.examples;

import no.bera.springyES.commands.Command;
import no.bera.springyES.commands.annotations.CommandHandler;
import no.bera.springyES.projection.annotations.Handler;

@CommandHandler
public class TestCommandHandler {

    @Handler
    public void handleTestCommand(Command command){

    }
}
