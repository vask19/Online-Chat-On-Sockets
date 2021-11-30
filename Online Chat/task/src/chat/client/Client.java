package chat.client;

import javax.xml.crypto.Data;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try {
            System.out.println("Client started!");

            Socket socket = new Socket("localhost",8189);
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());



            Thread outThread = new Thread(()->{
                Scanner scanner = new Scanner(System.in);
                while (scanner.hasNextLine()){
                    String msg =  scanner.nextLine();
                    if (!msg.trim().isEmpty()){
                        if (msg.equals("/exit")){
                            try {
                                socket.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            System.exit(0);
                        }

                        try {
                            out.writeUTF(msg);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                    }
                }
            });
            outThread.start();

            Thread inThread = new Thread(()->{
                while (true){
                    try {
                        String msg = in.readUTF();
                        System.out.println(msg);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

            });
            inThread.start();






        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
