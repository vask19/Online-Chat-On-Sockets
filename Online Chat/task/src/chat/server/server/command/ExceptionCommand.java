package chat.server.command;

import chat.server.ClientHandler;
import chat.server.database.DataBaseHandler;

public class ExceptionCommand extends Command{
    public ExceptionCommand(DataBaseHandler dataBaseHandler, ClientHandler clientHandler) {
        super(dataBaseHandler, clientHandler);
    }

    @Override
    public void execute() {

    }
}
