package chat.server.command;


import chat.server.ClientHandler;
import chat.server.database.DataBaseHandler;

public class ShortPasswordCommand extends Command{

    public ShortPasswordCommand(DataBaseHandler dataBaseHandler, ClientHandler clientHandler) {
        super(dataBaseHandler, clientHandler);
    }

    @Override
    public void execute() {

    }
}
