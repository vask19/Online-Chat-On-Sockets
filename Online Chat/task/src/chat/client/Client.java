package chat.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private final String SERVER_ADDR = "localhost";
    private final int SERVER_PORT = 8189;
    Scanner scanner = new Scanner(System.in);
    private static boolean debugMode = false;

    private String msgString;

    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    public Client() {
        try {
            System.out.println("Client started!");
            openConnection();
        } catch (IOException e) {
            System.out.println("Нет связи с сервером");
        }
    }

    private void openConnection() throws IOException {
        socket = new Socket(SERVER_ADDR, SERVER_PORT);
        if (debugMode) {
            System.out.println("Connected to Server!");
        }
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());

        //В этом потоке бесконечно отсылаем если есть что слать в сокет и далее на сервер
        Thread thread = new Thread(() -> {
            try {
                while (true) {
                    msgString = scanner.nextLine();
                    if (!msgString.trim().isEmpty()) {
                        try {
                            out.writeUTF(msgString);
                        } catch (IOException e) {
                            System.out.println("Ошибка отправки . Нет связи\n" + e);
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                closeConnection();
                e.printStackTrace();
            }
        });
        thread.start();

        // В этом потоке бесконечно читаем сокет
        Thread thread1 = new Thread(() -> {
            try {
                while (true) {
                    String broadcast = in.readUTF();
                    System.out.println(broadcast);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        thread1.start();

    }


    //Закрываем все соединения
    public void closeConnection() {
        if (debugMode) {
            System.out.println("Connection closing");
        }
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        new Client();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}