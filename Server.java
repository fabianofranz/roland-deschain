import org.vertx.java.core.Handler;
import org.vertx.java.core.AsyncResult;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.http.HttpServer;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.sockjs.SockJSServer;
import org.vertx.java.core.sockjs.SockJSSocket;
import org.vertx.java.platform.Verticle;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.core.sockjs.EventBusBridgeHook;
import org.vertx.java.core.sockjs.SockJSSocket;
import org.vertx.java.core.eventbus.EventBus;
import java.lang.Thread;
import java.lang.Runnable;
import java.lang.InterruptedException;
 
public class Server extends Verticle {
 
  public void start() {

    final EventBus eb = vertx.eventBus();

    HttpServer httpServer = vertx.createHttpServer();
    httpServer.setCompressionSupported(true);

    // TODO RouteMatcher routeMatcher = new RouteMatcher();

    httpServer.requestHandler(new Handler<HttpServerRequest>() {
      public void handle(HttpServerRequest req) {

      	if (req.path().equals("/instagram")) {
          req.response().headers().set("Content-Type", "text/html; charset=UTF-8");
          req.response().end("Hello from Java !!!");

        } else {
          req.response().headers().set("Content-Type", "text/html; charset=UTF-8");
          String file = req.path().equals("/") ? "index.html" : req.path();
          req.response().sendFile("webroot/" + file);
        }

      }
    });

    JsonObject config = new JsonObject().putString("prefix", "/event");
    JsonArray inbound = new JsonArray();
    JsonArray outbound = new JsonArray();
    inbound.add(new JsonObject());
    //inbound.add(new JsonObject().putString("address", "MyChannel"));
    outbound.add(new JsonObject().putString("address", "MyChannel"));
    vertx.createSockJSServer(httpServer).bridge(config, inbound, outbound);

    new Thread(new Runnable() {          
        public void run() {
          while (true) {
            try { Thread.sleep(15 * 1000); } catch (InterruptedException e) { }
            eb.publish("MyChannel", "Hello World");
          }
        }
    }).start();

    int port = Integer.parseInt(System.getenv("OPENSHIFT_VERTX_PORT"));
    String ip = System.getenv("OPENSHIFT_VERTX_IP");
    httpServer.listen(port, ip);

  }
}
