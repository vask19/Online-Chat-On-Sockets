package chat.server.command;

import chat.server.ClientHandler;
import chat.server.database.DataBaseHandler;

public abstract class Command {
    protected DataBaseHandler dataBaseHandler;
    protected ClientHandler clientHandler;


    public Command(DataBaseHandler dataBaseHandler, ClientHandler clientHandler) {
        this.dataBaseHandler = dataBaseHandler;
        this.clientHandler = clientHandler;
    }

    public abstract void execute();

}
