package chat.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Server {
    private static final int PORT = 8189;
    private volatile static ServerSocket server;
    private HashMap<ServerHandlerClass,String > clients = new HashMap<>();
    private volatile List<String > chat = new ArrayList();


    public static void main(String[] args) throws IOException {
        chat.server.Server server  = new chat.server.Server();
        server.startServer();
        server.run();



    }

    private  void run(){
        while (!server.isClosed()){
            try {
                Socket socket = server.accept();
                new Thread(()->{
                    try {
                        ServerHandlerClass client = registrationNewClient(socket);
                        clients.put(client,client.getClientName());
                        sendStartMessagesForClient(client);
                        new Thread(()->{
                            while (!socket.isClosed()){
                                try {
                                    String message = client.getInputStream().readUTF();
                                    chat.add(client.getClientName() + ": " + message);
                                } catch (IOException e) {
                                    break;

                                }
                            }
                        }).start();
                        new Thread(()->{
                            int messageCounter = chat.size();

                            while (!socket.isClosed()){

                                if (messageCounter<chat.size()){
                                    try {
                                        client.getOutputStream().writeUTF(chat.get(messageCounter));
                                        messageCounter++;
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                        }).start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();


            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
    /*Запрашивает имя клиента создает обработчик для клиента и возвращает его*/
    private ServerHandlerClass registrationNewClient(Socket socket) throws IOException {

        ServerHandlerClass client = null;
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        DataInputStream in = new DataInputStream(socket.getInputStream());
        String clientName = "";
        out.writeUTF("Server: write your name");
        while (true){
            clientName = in.readUTF().trim();


            if (clients.containsValue(clientName))
                out.writeUTF("Server: this name is already taken! Choose another one.");
            else break;
        }
        if (!clientName.equals(""))
            client = new ServerHandlerClass(socket,in,out,clientName);



        return client;

    }
    private void sendStartMessagesForClient(ServerHandlerClass client){
        if (chat.size() < 10){
            for (String message : chat){
                try {
                    client.getOutputStream().writeUTF(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        else {
            int firstIndex = chat.size() - 10;
            for (int i = firstIndex;i<chat.size();i++) {
                try {
                    client.getOutputStream().writeUTF(chat.get(i));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }







    public  void startServer(){
        try {
            server = new ServerSocket(PORT);
            server.setSoTimeout(7000);
            System.out.println("Server started!");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}