package com.fabianofranz.deschain;

import io.vertx.core.Vertx;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.web.Router;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.sockjs.BridgeOptions;
import io.vertx.ext.web.handler.sockjs.PermittedOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;

import java.io.File;
import java.io.IOException;

public class Server extends AbstractVerticle {

  public static void main(String[] args) {
    Vertx.vertx().deployVerticle(Server.class.getCanonicalName());
  }

  @Override
  public void start() {
    Router router = Router.router(vertx);

    router.route(HttpMethod.GET, "/instagram/event").handler(ctx -> {
      String mode = ctx.request().getParam("hub.mode");
      String challenge = ctx.request().getParam("hub.challenge");
      String token = ctx.request().getParam("hub.verify_token");
      ctx.response().putHeader("content-type", "text/html");
      ctx.response().end(challenge);
    });

    router.route(HttpMethod.POST, "/instagram/event").handler(BodyHandler.create()).handler(ctx -> {
      String body = ctx.getBodyAsString();

      for (String geography : Instagram.parseEventGeographies(body)) {
        JsonObject envelope = Instagram.fetchGeographyDetails(geography);
        JsonArray items = envelope.getJsonArray("data");
        for (int i = 0; i < items.size(); i++) {
          JsonObject item = items.getJsonObject(i);
          String id = item.getString("id");
          if (Cache.instance().insert(id, item)) {
            vertx.eventBus().send("MyChannel", item);
          }
        }
      }

      ctx.response().end();
    });

    BridgeOptions opts = new BridgeOptions().addOutboundPermitted(new PermittedOptions().setAddress("MyChannel"));
    SockJSHandler ebHandler = SockJSHandler.create(vertx).bridge(opts);
    router.route("/event/*").handler(ebHandler);

    router.route("/static/*").handler(StaticHandler.create());

    vertx.createHttpServer().requestHandler(router::accept).listen(Config.serverPort());
  }

  // public void start() {
  //
  //
  //   JsonObject config = new JsonObject().putString("prefix", "/event");
  //   JsonArray inbound = new JsonArray(); //nothing
  //   JsonArray outbound = new JsonArray().add(new JsonObject().putString("address", "MyChannel"));
  //   vertx.createSockJSServer(httpServer).bridge(config, inbound, outbound);
  //
  //   httpServer.listen(Config.serverPort(), Config.serverIp());
  //
  //   setup();
  //
  // }
  //
  // private void setup() {
  //   if (!Config.configured()) {
  //     Instagram.unsubscribeEverything();
  //     Config.configure();
  //     //Instagram.requestSubscriptionToGeography(35.657872, 139.70232, 1000);
  //   }
  // }

}
