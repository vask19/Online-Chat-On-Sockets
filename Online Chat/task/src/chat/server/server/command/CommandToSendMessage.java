package chat.server.command;


import chat.server.ClientHandler;
import chat.server.database.DataBaseHandler;

public class CommandToSendMessage extends Command{

    public CommandToSendMessage(DataBaseHandler dataBaseHandler, ClientHandler clientHandler) {
        super(dataBaseHandler, clientHandler);
    }

    @Override
    public void execute() {
        dataBaseHandler.sendMessage(clientHandler);

    }
}
