package be.lias;

import be.lias.server.ProducerWS;
import be.lias.server.RosettaServer;
import be.lias.server.RosettaTestServer;

import java.net.MalformedURLException;

/**
* Created with IntelliJ IDEA.
* User: kris
* Date: 10/9/12
* Time: 5:47 PM
* To change this template use File | Settings | File Templates.
*/

public class SdkTest {

    public static void main(String[] args) {

        RosettaServer server = null;
        // Get the test server configuration
        try {
            server = new RosettaTestServer();
            // Login into the server
            server.login();
            // get The User's UserID
            String user_id = server.producer.getUserId(server.getUserName());
            // print a list of producers for the producer agent
            for (ProducerWS.Producer p : server.producer.getProducers(user_id)) {
                System.out.println("Producer: id=" + p.id + " description=" + p.description);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } finally {
            if (server != null) {
                server.logout();
            }
        }
    }
}
