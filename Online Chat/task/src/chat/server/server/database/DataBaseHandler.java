package chat.server.database;

import chat.server.ClientHandler;
import chat.server.room.Room;
import chat.server.room.RoomHandler;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class DataBaseHandler {
    private static volatile DataBase dataBase;

    public DataBaseHandler(){
        if (dataBase == null)
            createDataBase();
    }

    private void createDataBase(){
        dataBase = new DataBase();
    }


    //регистрация нового пользователя
    public void registration(ClientHandler client){
        if (dataBase.isRegistration(client.getLogin())){
            return;
        }
        int hash = creatingClientHash(client.getLogin(),client.getPassword());
        dataBase.addNewClient(client,hash);
        client.addMessage("Server: you are registered successfully!");

        }



    //передает сообщение в комнату
    public void sendMessage(ClientHandler client){
        System.out.println("send " + client);
        if (client.getCurrentRoomHash() != 0){
            Room room = dataBase.getRoom(client.getCurrentRoomHash());
            room.sendMessage(client);
        }
      }


    // Ищет в базе данных комнату, если не находит - создаёт новую;
    public void startChatting(ClientHandler client){
        Room room = null;
        int hash = RoomHandler
                .createRoomHash(client.getLogin(),client.getInterlocutor());

        if ((room = dataBase.getRoom(hash)) == null ){
            room = RoomHandler.createNewRoom(client,hash);
            client.setCurrentRoomHash(hash);
            room.start();
            dataBase.addRoom(hash,room);

        }

        else {
            room.addClientInRoom(client);
            client.setCurrentRoomHash(hash);
        }






    }




    /*создание хэша*/
    private int creatingClientHash(String login,String password){
        File file = new File("hash");
        try(DataOutputStream out =
                    new DataOutputStream(
                            new FileOutputStream(file))){

            out.writeUTF(login + " " + password);
            return file.hashCode();

        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }







}
