import org.vertx.java.core.Handler;
import org.vertx.java.core.AsyncResult;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.http.HttpServer;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.core.sockjs.SockJSServer;
import org.vertx.java.core.sockjs.SockJSSocket;
import org.vertx.java.platform.Verticle;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.core.sockjs.EventBusBridgeHook;
import org.vertx.java.core.sockjs.SockJSSocket;
 
public class Server extends Verticle {
 
  public void start() {

    HttpServer server = vertx.createHttpServer();

    server.requestHandler(new Handler<HttpServerRequest>() {
      public void handle(HttpServerRequest req) {

      	if (req.path().equals("/hello")) {
          req.response().headers().set("Content-Type", "text/html; charset=UTF-8");
          req.response().end("Hello from Java !!!");

        } else {
          req.response().headers().set("Content-Type", "text/html; charset=UTF-8");
          String file = req.path().equals("/") ? "index.html" : req.path();
          req.response().sendFile("webroot/" + file);
        }

      }
    });

    JsonArray permitted = new JsonArray();
    permitted.add(new JsonObject()); // Let everything through
    SockJSServer sockJSServer = vertx.createSockJSServer(server);
    sockJSServer.setHook(new EventBusBridgeHook() { 
      public boolean handleSocketCreated(SockJSSocket sock) { return true; }
      public void handleSocketClosed(SockJSSocket sock) { }
      public boolean handleSendOrPub(SockJSSocket sock, boolean send, JsonObject msg, String address) { return true; }
      public boolean handlePreRegister(SockJSSocket sock, String address) { return true; }
      public void handlePostRegister(SockJSSocket sock, String address) { }
      public boolean handleUnregister(SockJSSocket sock, String address) { return true; }
      public boolean handleAuthorise(JsonObject message, String sessionID, Handler<AsyncResult<Boolean>> handler) { return false; }
    });
    sockJSServer.bridge(new JsonObject().putString("prefix", "/event"), permitted, permitted);

    int port = Integer.parseInt(System.getenv("OPENSHIFT_VERTX_PORT"));
    String ip = System.getenv("OPENSHIFT_VERTX_IP");
    server.listen(port, ip);

  }
}
