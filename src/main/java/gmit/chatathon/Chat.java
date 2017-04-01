package gmit.chatathon;


import org.eclipse.jetty.websocket.api.*;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static j2html.TagCreator.*;
import static spark.Spark.*;



/**
 * Created by phil on 3/30/2017.
 */
public class Chat {

    static Map<Session, String> userUsernameMap = new ConcurrentHashMap<>();
    static int nextUserNumber = 1;

    public static void main(String[] args) {
       staticFiles.location("/public");
       staticFiles.expireTime(600);
       webSocket("/chat", ChatWebSocketHandler.class);
       init();
    }


    public static void broadcastMessage(String sender, String message) {
        userUsernameMap.keySet().stream().filter(Session::isOpen).forEach(session -> {
            try {
                session.getRemote().sendString(String.valueOf(new JSONObject()
                    .put("userMessage", createHtmlMessageFromSender(sender, message))
                    .put("userlist", userUsernameMap.values())
                ));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private static String createHtmlMessageFromSender(String sender, String message) {
        return article().with(
                b(sender + " says:"),
                p(message),
                span().withClass("timestamp").withText(new SimpleDateFormat("HH:mm:ss").format(new Date()))
        ).render();
    }
}
