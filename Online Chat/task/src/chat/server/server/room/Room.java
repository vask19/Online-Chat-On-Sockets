package chat.server.room;

import chat.server.ClientHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Room{

    private int hash;
    private final String firstClient;
    private final String secondClient;
    private volatile List<String> firstClientMessages;
    private volatile List<String> secondClientMessages;
    private ClientHandler firstClientHandler;
    private ClientHandler secondClientHandler;
    private Thread firstThread;
    private Thread secondThread;


    public Room(ClientHandler client, int hash) {
        this.firstClientHandler = client;
        this.firstClient = client.getLogin();
        this.secondClient = client.getInterlocutor();
        this.hash = hash;
        firstClientMessages = new ArrayList<>();
        secondClientMessages = new ArrayList<>();
        this.firstThread = createOutThread(firstClientHandler,firstClientMessages);
        firstThread.start();


    }



    //создает потоки и ждет. основной поток заверштся когда оба клиента покинут комнату
    public void start(){

           /* if (firstClientHandler != null) {
                first = createOutThread(firstClientHandler, firstClientMessages);
                first.start();
            }



            if (secondClientHandler != null){
                second = createOutThread(secondClientHandler,secondClientMessages);
                second.start();
            }

        }
        Objects.requireNonNull(first).interrupt();
        Objects.requireNonNull(second).interrupt();
     */


    }


    //добавляет второго участника в комнату
    public boolean addClientInRoom(ClientHandler secondClientHandler) {
        if (this.secondClientHandler == null) {
            this.secondClientHandler = secondClientHandler;
            this.secondThread = createOutThread(secondClientHandler,secondClientMessages);
            secondThread.start();
            return true;
        }
        return false;
    }


    //создает поток который отправляет сообщения с комнаты на обработчик клиента;
    private Thread createOutThread(ClientHandler client, List<String> clientMessages) {
        return new Thread(() -> {
            int messageCounter = clientMessages.size();
            sendStartMessagesForClient(client,clientMessages);
            while (true) {
                if (messageCounter < clientMessages.size()) {
                    client.addMessage(clientMessages.get(messageCounter++));
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    //добавляет сообщение в список
    public void sendMessage(ClientHandler client) {
        System.out.println(secondClientHandler);
        System.out.println(firstClientHandler);
        System.out.println(secondClientHandler == firstClientHandler);

        if (client.equals(firstClientHandler)) {
            System.out.println("cl 1");
            secondClientMessages.add("(new) " + client.getMessage() + "\n");
            System.out.println("cl 1 ");
        }

        else if (client.equals(secondClientHandler)){
            System.out.println("cl 2 ");

            firstClientMessages.add("(new) " + client.getMessage() + "\n");
            System.out.println("cl 2 ");

        }
    }


    //отправляет первые 10 сообщений
    private void sendStartMessagesForClient(ClientHandler client, List<String> clientMessages) {
        if (clientMessages.size() < 10) {
            for (String message : clientMessages)
                client.addMessage(message);
        } else {
            int firstIndex = clientMessages.size() - 10;
            for (int i = firstIndex; i < clientMessages.size(); i++)
                client.addMessage(clientMessages.get(i));


        }


    }

}
