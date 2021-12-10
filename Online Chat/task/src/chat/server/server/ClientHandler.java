package chat.server;

import chat.data.Data;
import chat.server.command.*;
import chat.server.database.DataBaseHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientHandler extends Thread{
    private Socket socket;
    private String login;
    private String password;
    private Controller controller;
    private String interlocutor;
    private DataBaseHandler dataBaseHandler;
    private DataOutputStream out;
    private DataInputStream in;
    private List<String > clientMessages;
    private int currentRoomHash;
    private String message;
    private Thread inputThread;
    private Thread outputThread;

    public ClientHandler(DataBaseHandler dataBaseHandler, Socket socket) {
        this.dataBaseHandler = dataBaseHandler;
        this.socket = socket;
        this.clientMessages = new ArrayList<>();
        this.controller = new Controller();

    }


    public int getCurrentRoomHash() {
        return currentRoomHash;
    }

    public void setCurrentRoomHash(int currentRoomHash) {
        this.currentRoomHash = currentRoomHash;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getInterlocutor() {
        return interlocutor;
    }

    public void setInterlocutor(String interlocutor) {
        this.interlocutor = interlocutor;
    }


    @Override
    public void run() {
        try {
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }

        startingClientReadingThread();
        startingClientWritingThread();

        try {
            inputThread.start();
            outputThread.start();
            inputThread.join();
            outputThread.join();
        } catch (InterruptedException e) {

            this.interrupt();
            e.printStackTrace();

        }


    }

    //оток принимающий сообщения клиента
    private void startingClientReadingThread(){
        inputThread = new Thread(()-> {
            while (!inputThread.isInterrupted()) {
                try {
                    String clientMessage = in.readUTF();
                    if (!clientMessage.trim().isEmpty()){
                        Command command = createCommand(clientMessage);
                        execute(command);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    inputThread.interrupt();

                }

            }
        });
    }


    //поток отправляющий сообщения клиенту
    private void startingClientWritingThread(){
        try {
            outputThread = new Thread(() -> {
                sendMessageToClient(Data.START_MESSAGE);
                int count = 0;

                while (!outputThread.isInterrupted()){
                    if (!clientMessages.isEmpty() &
                            count < clientMessages.size()){
                        try {
                            out.writeUTF(clientMessages.get(count++));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();


        }
    }


















    /*создает команду из пользовательського ввода*/
    private Command createCommand(String clientMessage) {
        Command command = new ExceptionCommand(dataBaseHandler,this);
        String[] messageComponents = clientMessage.split(" ");

        switch (messageComponents[0]) {
            case "/registration" -> {

                this.login = messageComponents[1];
                this.password = messageComponents[2];
                command = registrationCreate();
            }
            case "/authorize" -> {
                this.login = messageComponents[1];
                this.password = messageComponents[2];
                command = authCreate();
            }
            case "/exit" -> exit();

            case "/chat" -> {
                interlocutor = messageComponents[1];
                command = new CommandStartChatting(dataBaseHandler, this);
            }
            case "/list" -> command = new GetOnlineListCommand(dataBaseHandler, this);

            default -> {
                message = clientMessage;
                command = new CommandToSendMessage(dataBaseHandler,this);



            }

        }
        return command;
    }

    private void exit() {
    }


    //Создание команды регистрации
    private Command registrationCreate(){
        if (password.length() < 8)
           return new ShortPasswordCommand(dataBaseHandler,this);
        else return new RegistrationCommand(dataBaseHandler,this);


    }



    //добавляет новое сообщение с комнаты либо сервера в список
   public synchronized void addMessage(String message){
        clientMessages.add(message);
    }





    //Создание команды авторизации
    private Command authCreate(){
        if (password.length() < 8)
            return new ShortPasswordCommand(dataBaseHandler, this);
        return new AuthorizeCommand(dataBaseHandler, this);


    }

    //Создание команды
    //Создание команды




        //Отправка сообщения клиенту
    private void sendMessageToClient(String message){
        try {
            out.writeUTF(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*вызывает команду*/
    private void execute(Command command){
        if (command != null){
            controller.setCommand(command);
            controller.execute();

        }

    }


    //озвращает текущее сообщение клиента
    public String getMessage() {
        return message;
    }
}
