package chat.server.command;


import chat.server.ClientHandler;
import chat.server.database.DataBaseHandler;

public class ExitChatCommand extends Command{


    public ExitChatCommand(DataBaseHandler dataBaseHandler, ClientHandler clientHandler) {
        super(dataBaseHandler, clientHandler);
    }

    @Override
    public void execute() {

    }
}
