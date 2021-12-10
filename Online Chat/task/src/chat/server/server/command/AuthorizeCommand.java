package chat.server.command;


import chat.server.ClientHandler;
import chat.server.database.DataBaseHandler;

public class AuthorizeCommand extends  Command{


    public AuthorizeCommand(DataBaseHandler dataBaseHandler, ClientHandler clientHandler) {
        super(dataBaseHandler, clientHandler);
    }

    @Override
    public void execute() {

    }
}
