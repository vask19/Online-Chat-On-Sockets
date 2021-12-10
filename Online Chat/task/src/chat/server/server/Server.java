package chat.server;
import chat.data.Data;
import chat.server.database.DataBaseHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {
    private  static ServerSocket server;
    private  static DataBaseHandler dataBaseHandler;
    public static void main(String[] args) {
        new Server().startServer();

    }

    public void startServer(){
        try {
            server = new ServerSocket(Data.SERVER_PORT);
            dataBaseHandler = new DataBaseHandler();
            System.out.println("Server started");
            run();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private void run(){
        try {
            while (!server.isClosed()){
                Socket socket = server.accept();
                try {
                    new ClientHandler(dataBaseHandler,socket).start();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
