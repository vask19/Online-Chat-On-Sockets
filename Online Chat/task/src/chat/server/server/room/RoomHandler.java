package chat.server.room;

import chat.server.ClientHandler;

public class RoomHandler {










    //создает хэш комнаты
    public static int createRoomHash(String firstClientLogin, String secondClientLogin){
       return firstClientLogin.hashCode() + secondClientLogin.hashCode();

    }


    //создает комнату первый раз
    public static Room createNewRoom(ClientHandler client,int hash) {
        return new Room(client,hash);
    }
}
