import org.vertx.java.core.Handler;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.http.HttpServer;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.core.sockjs.SockJSServer;
import org.vertx.java.core.sockjs.SockJSSocket;
import org.vertx.java.platform.Verticle;
 
import java.util.Map;
 
public class Server extends Verticle {
 
  public void start() {

    int port = Integer.parseInt(System.getenv("OPENSHIFT_VERTX_PORT"));
    String ip = System.getenv("OPENSHIFT_VERTX_IP");

    HttpServer server = vertx.createHttpServer();

    server.requestHandler(new Handler<HttpServerRequest>() {
      public void handle(HttpServerRequest req) {

        System.out.println("Got request: " + req.uri());
        System.out.println("Headers are: ");

        for (Map.Entry<String, String> entry : req.headers()) {
          System.out.println(entry.getKey() + ":" + entry.getValue());
        }

        req.response().headers().set("Content-Type", "text/html; charset=UTF-8");

      	if (req.path().equals("/hello")) {
          req.response().end("Hello from Java !!!");

        } else {
          String file = req.path().equals("/") ? "index.html" : req.path();
          req.response().sendFile("webroot/" + file);
        }

      }
    });

    SockJSServer sockServer = vertx.createSockJSServer(server);
    sockServer.installApp(new JsonObject().putString("prefix", "/event"), new Handler<SockJSSocket>() {
      public void handle(final SockJSSocket sock) {
        sock.dataHandler(new Handler<Buffer>() {
          public void handle(Buffer data) {
            sock.write(data); // Echo it back
          }
        });
      }
    });

    server.listen(port, ip);

  }
}
