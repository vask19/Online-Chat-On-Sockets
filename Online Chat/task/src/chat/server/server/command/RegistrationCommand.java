package chat.server.command;


import chat.server.ClientHandler;
import chat.server.database.DataBaseHandler;

public class RegistrationCommand extends Command{

    public RegistrationCommand(DataBaseHandler dataBaseHandler, ClientHandler clientHandler) {
        super(dataBaseHandler, clientHandler);
    }

    @Override
    public void execute() {
        System.out.println(2);               /////////////22222222222

        dataBaseHandler.registration(clientHandler);

    }
}
