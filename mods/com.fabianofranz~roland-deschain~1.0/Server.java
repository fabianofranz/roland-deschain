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
import org.vertx.java.core.http.RouteMatcher;
import java.lang.Thread;
import java.lang.Runnable;
import java.lang.InterruptedException;
import java.util.Set;

public class Server extends Verticle {
 
  public void start() {

    HttpServer httpServer = vertx.createHttpServer();
    httpServer.setCompressionSupported(true);

    RouteMatcher router = new RouteMatcher();

    router.get("/instagram/event", new Handler<HttpServerRequest>() {
      public void handle(HttpServerRequest req) {
        String mode = req.params().get("hub.mode");
        String challenge = req.params().get("hub.challenge");
        String token = req.params().get("hub.verify_token");
        req.response().headers().set("Content-Type", "text/html; charset=UTF-8");
        req.response().end(challenge);
      }
    });

    router.post("/instagram/event", new Handler<HttpServerRequest>() {
      public void handle(HttpServerRequest req) {
        req.bodyHandler(new Handler<Buffer>() {
          public void handle(Buffer body) {
            for (JsonObject details : Cache.instance().event(body.toString())) {
              vertx.eventBus().publish("MyChannel", details.toString());
              System.out.println("Cache size: " + Cache.instance().cache().size());
            }
          }
        });
        req.response().end();
      }
    });

    router.noMatch(new Handler<HttpServerRequest>() {
        public void handle(HttpServerRequest req) {
          String path = req.path();
          String file = path.equals("/") || path.contains("..") ? "index.html" : req.path();
          req.response().sendFile("./web/" + file);
        }
    });

    httpServer.requestHandler(router);

    JsonObject config = new JsonObject().putString("prefix", "/event");
    JsonArray inbound = new JsonArray(); //nothing
    JsonArray outbound = new JsonArray().add(new JsonObject().putString("address", "MyChannel"));
    vertx.createSockJSServer(httpServer).bridge(config, inbound, outbound);

    httpServer.listen(Config.serverPort(), Config.serverIp());

    setup();

  }

  private void setup() {
    if (!Config.configured()) {
      Instagram.unsubscribeEverything();
      Config.configure();
      Instagram.requestSubscriptionToGeography(35.657872, 139.70232, 1000);
    }
  }

}
