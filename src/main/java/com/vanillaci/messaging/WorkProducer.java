package com.vanillaci.messaging;

import com.vanillaci.model.Work;
import org.codehaus.jackson.map.ObjectMapper;
import org.jetbrains.annotations.NotNull;

import javax.jms.*;
import java.io.IOException;

/**
 * User: michaelnielson
 * Date: 5/26/14
 */
public class WorkProducer {

  private final ObjectMapper objectMapper = new ObjectMapper();
  private final ConnectionFactory connectionFactory;
  private final Queue workQueue;

  private Connection connection;

  public WorkProducer(@NotNull ConnectionFactory connectionFactory, @NotNull Queue workQueue) {
    this.connectionFactory = connectionFactory;
    this.workQueue = workQueue;

  }

  public String sendWork(Work work) throws JMSException, IOException {
    Connection connection = getConnection();
    Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
    MessageProducer messageProducer = session.createProducer(workQueue);


    String workJson = objectMapper.writeValueAsString(work);

    TextMessage textMessage = session.createTextMessage(workJson);
    populateJmsHeaders(textMessage, work);

    messageProducer.send(workQueue, textMessage);

    return textMessage.getJMSMessageID();
  }

  private void populateJmsHeaders(Message message, Work work) throws JMSException {
    message.setIntProperty(WorkConstants.WEIGHT, 1);

    // todo... what headers...
  }

  private Connection getConnection() throws JMSException {
    if (connection == null) {
      connection = connectionFactory.createConnection();
      connection.start();
    }
    return connection;
  }


}
