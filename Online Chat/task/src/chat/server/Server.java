package chat.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
    private static final int PORT = 8189;
    private static boolean debugMode = false;

    public static void main(String[] args) {
        try (ServerSocket server = new ServerSocket(PORT)) {
            System.out.println("Server started!");
            Socket socket = server.accept();
            if (debugMode) {
                System.out.println(socket.getInetAddress().getHostAddress() + " Client connected!");
            }
            DataInputStream input = new DataInputStream(socket.getInputStream());
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());

            //В этом потоке бесконечно слушаем сокет.
            Thread thread = new Thread(() -> {
                while (true) {

                    String msg = "";
                    try {
                        msg = input.readUTF();
                    } catch (IOException e) {
                        // Как только клиент отключился - сервер завершит работу
                        System.exit(0);
                    }

                    // Печатаем что прочитали от клиента сообщение
                    System.out.println(msg);
                }
            } );
            thread.start();

            // В этом потоке реализуем рассылку сообщений с сервера
            Thread thread1 = new Thread(() -> {
                while (true) {
                    Scanner scanner = new Scanner(System.in);
                    String broadcast = scanner.nextLine();
                    try {
                        // Отсылаем сообщение клиенту
                        output.writeUTF(broadcast);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread1.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}