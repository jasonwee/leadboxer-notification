package controllers;

import static play.libs.Json.toJson;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import models.NotificationSpecification;
import play.Logger;
import play.libs.Json;
import play.libs.ws.WSClient;
import play.libs.ws.WSRequest;
import play.libs.ws.WSResponse;
import play.mvc.Controller;
import play.mvc.Result;

public class LogServer extends Controller {

   @Inject private WSClient ws;

   // for logserver to get all the notification
   public Result listAll() {
      List<NotificationSpecification> notificationSpecifications = NotificationSpecification.findAll();
      notificationSpecifications.forEach((ns) -> Logger.info("notificationSpecifications={}", ns));
      return ok(toJson(notificationSpecifications));
   }

   /**
    * For notification server to push to logserver
    * 
    * http://stackoverflow.com/questions/38387780/extract-results-from-wsresponse-playws-java
    * http://stackoverflow.com/questions/38428551/java-lang-illegalstateexception-closed-by-using-wsrequest-play-java?noredirect=1#comment64263909_38428551
    * 
    * @param datasetId
    */
   public void updateDataset(String datasetId) {
      Logger.info("pushing to {}", datasetId);
      // TODO Change url later
      WSRequest request = ws.url("http://log-dev.leadboxer.com/notification")
            .setRequestTimeout(5000)
            .setContentType("application/json");

      JsonNode body = readFromDB(datasetId);

      CompletionStage<WSResponse> responsePromise = request.post(body);

      try {
         WSResponse response = responsePromise.toCompletableFuture().get();
         Logger.info("response from log server {}", response.getBody());
      } catch (InterruptedException | ExecutionException e) {
         Logger.error("unable to push to logserver", e);
      }
   }
   
   /**
    *  read from database for a given datasetId and format the output example of the following into JsonNode format
    *    {
    *     "dataset": {
    *       "datasetId": "a1d90dccc04df83f26553dc753ed41f2",
    *       "enable": false,
    *          "notifications": {
    *            "most_likely_company": [
    *              "google",
    *              "leadboxer"
    *            ],
    *            "li_industry": [
    *                "Defense & Space",
    *              "foo"
    *            ]
    *          }
    *        }
    *      }
    *   
    * @param datasetId
    * @return
    */
   public JsonNode readFromDB(String datasetId) {
      // no need meddle with jdbc, just use orm
      /*
      try (Connection con = DB.getConnection();) {
         try (PreparedStatement pStatment = con.prepareStatement("SELECT * FROM notification_specification where dataset_id = ?");) {
            pStatment.setString(1, dataset);
            try (ResultSet result = pStatment.executeQuery();) {
               result.getString("id");
               result.getString("nKey");
               result.getString("nKey");
            }
         }
         //results.forEach((ns) -> Logger.info("list from db {}", ns));
         
      } catch (SQLException e) {
         e.printStackTrace();
      }
      */
      List<NotificationSpecification> nss = NotificationSpecification.byDataset(datasetId);
      
      boolean enable = false;

      if (nss == null | nss.isEmpty()) {
         enable = false;
      }
      
      List<String> companies = new ArrayList<>();
      List<String> industries = new ArrayList<>();
      List<String> urls = new ArrayList<>();
      
      for (NotificationSpecification ns : nss) {
         switch (ns.getnKey()) {
         case "most_likely_company":
            companies.add(ns.getnValue());
            break;
         case "li_industry":
            industries.add(ns.getnValue());
            break;
         case "original_url":
            urls.add(ns.getnValue());
            break;
         }
      }
      
      // TODO we should really put this into a form where user can specify.
      if (companies.size() > 0 || industries.size() > 0 || urls.size() > 0) {
         enable = true;
      }
      
      ObjectNode root = Json.newObject();
      ObjectNode aDataset = root.putObject("dataset")
            .put("datasetId", datasetId)
            .put("enable", enable);
      ObjectNode notifications = aDataset.putObject("notifications");
      
      if (companies.size() == 1) {
         notifications.put("most_likely_company", companies.get(0));
      } else if (companies.size() > 1) {
         ArrayNode an = notifications.putArray("most_likely_company");
         companies.forEach((c) -> an.add(c));
      }
      
      if (industries.size() == 1) {
         notifications.put("li_industry", industries.get(0));
      } else if (industries.size() > 1) {
         ArrayNode an = notifications.putArray("li_industry");
         industries.forEach( i -> an.add(i));
      }
      
      if (urls.size() == 1) {
         notifications.put("original_url", urls.get(0));
      } else if (urls.size() > 1) {
         ArrayNode an = notifications.putArray("original_url");
         urls.forEach(u -> an.add(u));
      }
      
      if (Logger.isDebugEnabled()) {
         ObjectMapper mapper = new ObjectMapper();
         mapper.enable(SerializationFeature.INDENT_OUTPUT);
         try {
            String ident = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(root);
            Logger.debug("jsonnode {}", ident);
         } catch (JsonProcessingException e) {
            Logger.error("", e);
         }
      }

      return root;
   }
   
   // TODO maybe do a form for writing logservers hostname?
   // log01.leadboxer.com, log02.leadboxer.com
    

}
