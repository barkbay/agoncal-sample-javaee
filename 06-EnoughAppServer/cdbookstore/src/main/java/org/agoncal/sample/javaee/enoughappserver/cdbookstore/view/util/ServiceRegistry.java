package org.agoncal.sample.javaee.enoughappserver.cdbookstore.view.util;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;

public class ServiceRegistry
{

   private static Logger logger = Logger.getLogger(ServiceRegistry.class.getName());

   private static String[] defaultTopBooksServiceURLs = {
            "http://localhost:8080/topbooks",
            "http://localhost:8082/topbooks",
            "http://docker.local:8082/topbooks"};

   private static String[] topBooksServiceURLs = System.getenv("TOPBOOKS_URL") != null
                                                 ? new String[] { System.getenv("TOPBOOKS_URL") }
                                                 : defaultTopBooksServiceURLs;

   private static String[] defaultTopCDsServiceURLs = {
            "http://localhost:8080/topcds",
            "http://localhost:8081/topcds",
            "http://docker.local:8081/topcds"};

   private static String[] topCDsServiceURLs = System.getenv("TOPCDS_URL") != null
                                               ? new String[] { System.getenv("TOPCDS_URL") }
                                               : defaultTopCDsServiceURLs;

   public static String getTopBooksServiceURL()
   {
      return pingWorkingURL(topBooksServiceURLs);
   }

   public static String getTopCDsServiceURL()
   {
      return pingWorkingURL(topCDsServiceURLs);
   }

   private static String pingWorkingURL(String[] urls)
   {
      for (String url : urls)
      {
         try
         {
            Response response = ClientBuilder.newClient().target(url).request(MediaType.APPLICATION_JSON).get();
            if (response.getStatus() == Response.Status.OK.getStatusCode())
            {
               logger.info("OK URL : " + url);
               return url;
            }
            else
            {
               logger.warning("KO URL : " + url + "  -  Status code : " + response.getStatus());
            }
         }
         catch (Exception e)
         {
            logger.info("KO URL : " + url);
         }
      }
      return null;
   }
}
