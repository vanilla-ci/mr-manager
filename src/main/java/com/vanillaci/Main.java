package com.vanillaci;

import org.hornetq.jms.server.embedded.EmbeddedJMS;

import javax.jms.ConnectionFactory;

/**
 * User: michaelnielson
 * Date: 5/26/14
 */
public class Main {
  public static void main(String[] args) throws Exception {
    EmbeddedJMS jmsServer = new EmbeddedJMS();
    jmsServer.start();

    ConnectionFactory connectionFactory = (ConnectionFactory) jmsServer.lookup("ConnectionFactory");
  }
}
