package com.vanillaci.messaging;

import org.codehaus.jackson.map.ObjectMapper;
import org.jetbrains.annotations.NotNull;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * User: michaelnielson
 * Date: 5/26/14
 */
public class WorkConsumer implements Runnable {

  private final ObjectMapper objectMapper = new ObjectMapper();
  private final ConnectionFactory connectionFactory;
  private final Destination workDestination;
  private final ExecutorService executorService = Executors.newCachedThreadPool();
  private transient boolean running;

  private Connection connection;

  public WorkConsumer(@NotNull ConnectionFactory connectionFactory, @NotNull Destination workDestination) {
    this.connectionFactory = connectionFactory;
    this.workDestination = workDestination;
  }

  public void start() {
    running = true;
    executorService.submit(this);
  }

  public void stop() {
    running = false;
  }

  private String calculationConsumerExpression() {
    return null;
  }

  @Override
  public void run() {
    while (running) {
      
    }
  }
}
