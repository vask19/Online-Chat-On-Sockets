package chat.server.database;

import chat.server.ClientHandler;
import chat.server.room.Room;

import java.util.HashMap;
import java.util.Map;

public class DataBase {
    private volatile HashMap<String, ClientHandler> onlineClients;
    private volatile Map<String, Integer> clientHashes;
    private volatile Map<Integer, Room> rooms;

    public DataBase() {
        this.onlineClients = new HashMap<>();
        this.clientHashes = new HashMap<>();
        this.rooms = new HashMap<>();
    }

    //возвращает комнату по хэш-коду
    public Room getRoom(Integer hash) {
        return rooms.get(hash);
    }


    //Добавляет комнату в мапу;
    public synchronized void addRoom(int hash, Room room) {
        rooms.put(hash, room);
    }


    //проверяет заригистрирован ли данный логин  в чате
    public boolean isRegistration(String login){
        return clientHashes.containsKey(login);
   }


   //добавляет нового клиентра в базу данных
    public void addNewClient(ClientHandler client, int hash) {
        clientHashes.put(client.getLogin(),hash);
        onlineClients.put(client.getLogin(),client);
    }
}





