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

        System.out.println("Handling " + req.method() + ": " + req.path());

      	if (req.path().equals("/instagram/event")) {

          if ("GET".equals(req.method())) {
            String mode = req.params().get("hub.mode");
            String challenge = req.params().get("hub.challenge");
            String token = req.params().get("hub.verify_token");

            //Instagram.verifySubscriptionToGeography(challenge);
            
            req.response().headers().set("Content-Type", "text/html; charset=UTF-8");
            req.response().end(challenge);

          } else if ("POST".equals(req.method())) {
            req.bodyHandler(new Handler<Buffer>() {
              public void handle(Buffer body) {
                eb.publish("MyChannel", body);
              }
            });
            req.response().end();
          }

        } else {
          req.response().headers().set("Content-Type", "text/html; charset=UTF-8");
          String file = req.path().equals("/") ? "index.html" : req.path();
          req.response().sendFile("web/" + file);
        }

      }
    });

    JsonObject config = new JsonObject().putString("prefix", "/event");
    JsonArray inbound = new JsonArray(); //nothing
    JsonArray outbound = new JsonArray();
    //inbound.add(new JsonObject()); //everything
    //inbound.add(new JsonObject().putString("address", "MyChannel"));
    outbound.add(new JsonObject().putString("address", "MyChannel"));
    vertx.createSockJSServer(httpServer).bridge(config, inbound, outbound);

    httpServer.listen(Config.serverPort(), Config.serverIp());

    setup();

  }

  private void setup() {
    if (!Config.configured()) {
      Config.configure();
      Instagram.requestSubscriptionToGeography(35.657872, 139.70232, 1000);
    }
  }

}
