package chat.server.command;


import chat.server.ClientHandler;
import chat.server.database.DataBaseHandler;

public class GetOnlineListCommand extends Command{

    public GetOnlineListCommand(DataBaseHandler dataBaseHandler, ClientHandler clientHandler) {
        super(dataBaseHandler, clientHandler);
    }

    @Override
    public void execute() {

    }
}
