package chat.server.command;



import chat.server.ClientHandler;
import chat.server.database.DataBaseHandler;

public class CommandStartChatting extends Command{

    public CommandStartChatting(DataBaseHandler dataBaseHandler, ClientHandler clientHandler) {
        super(dataBaseHandler, clientHandler);
    }

    @Override
    public void execute() {
        dataBaseHandler.startChatting(clientHandler);

    }
}

