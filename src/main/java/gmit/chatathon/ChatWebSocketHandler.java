package gmit.chatathon;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by phil on 3/30/2017.
 */

@WebSocket
public class ChatWebSocketHandler {

    private String sender, msg;

    private static final Logger logger = LoggerFactory.getLogger(ChatWebSocketHandler.class);


    @OnWebSocketConnect
    public void onConnect(Session user) throws Exception {
        String username = "User" + Chat.nextUserNumber++;
        Chat.userUsernameMap.put(user, username);
        Chat.broadcastMessage(sender= "Server" , msg = (username + " joined the chat"));
        logger.debug("User {} joined the chat.", username);
    }

    @OnWebSocketClose
    public void onClose(Session user, int statusCode, String reason) {
        String username = Chat.userUsernameMap.get(user);
        Chat.userUsernameMap.remove(user);
        Chat.broadcastMessage(sender = "Server", msg = (username + " left the chat"));

    }

    @OnWebSocketMessage
    public void onMessage(Session user, String message) {
        String username = Chat.userUsernameMap.get(user);
        Chat.broadcastMessage(sender = username, msg = message);
        logger.debug("User {} shouted into the void. (session: {} )", username, user.toString());
    }

}
