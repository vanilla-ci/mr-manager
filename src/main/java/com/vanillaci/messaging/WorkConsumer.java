package com.vanillaci.messaging;

import com.vanillaci.model.Work;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.jetbrains.annotations.NotNull;

import javax.jms.*;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * User: michaelnielson
 * Date: 5/26/14
 */
public class WorkConsumer {
  public enum RunState {
    RUNNING, ERROR, STOPPED
  }

  private static final Logger log = LogManager.getLogger();

  private static final int TIMEOUT = 100;

  private final int capacity;

  private final ObjectMapper objectMapper = new ObjectMapper();
  private final ConnectionFactory connectionFactory;
  private final Queue workQueue;

  private final WorkTracker workTracker = new WorkTracker();

  private static final AtomicInteger threadCounter = new AtomicInteger();

  private final ExecutorService executorService = Executors.newCachedThreadPool(runnable ->
      new Thread(runnable, String.format("WorkerThread(%s)", threadCounter.incrementAndGet())));

  private volatile RunState runState;

  private Connection connection;
  private Session session;

  private final JmsExpression coreExpression;
  private final Runner internalRunner = new Runner();

  /**
   * A work consumer that polls for jobs on a JMS Queue
   * @param connectionFactory a JMS Connection Factory
   * @param workQueue the Queue to poll
   * @param coreExpression a first level filter for jobs
   * @param capacity the maximum allowed weight before this consumer should stop consuming messages
   */
  public WorkConsumer(@NotNull ConnectionFactory connectionFactory, @NotNull Queue workQueue, @NotNull JmsExpression coreExpression, int capacity) {
    this.connectionFactory = connectionFactory;
    this.workQueue = workQueue;
    this.coreExpression = coreExpression;
    this.capacity = capacity;
  }

  public void start() {
    runState = RunState.RUNNING;
    new Thread(internalRunner, "ConsumerThread").start();
  }

  public void stop() {
    runState = RunState.STOPPED;
  }

  public RunState getRunState() {
    return runState;
  }

  private String calculateConsumerExpression() {
    int availableCapacity = capacity - workTracker.runningWeight();
    return coreExpression.and(String.format("%s <= %d", WorkConstants.WEIGHT, availableCapacity)).toString();
  }

  private class Runner implements Runnable {
    @Override
    public void run() {
      try {
        while (runState == RunState.RUNNING) {
          if (capacity - workTracker.runningWeight() > 0) {
            pollForWork();
          } else {
            try {
              workTracker.wait();
            } catch (InterruptedException e) {
              // restart loop
            }
          }
        }
      } catch (Throwable throwable) {
        runState = RunState.ERROR;
        log.fatal("WorkConsumer shutdown caused by uncaught exception", throwable);
        throw throwable;
      }
    }
  }

  private void pollForWork() {
    MessageConsumer messageConsumer = null;
    try {
      Session session = getSession();
      String consumerExpression = calculateConsumerExpression();

      messageConsumer = session.createConsumer(workQueue, consumerExpression);
      TextMessage message = (TextMessage) messageConsumer.receive(TIMEOUT);

      if (message != null) {
        Work work = objectMapper.readValue(message.getText(), Work.class);

        // mark the work as started immediately, end work will be called in WorkRunnable
        workTracker.startWork(work, weighMessage(message));
        executorService.submit(new WorkRunnable(workTracker, work));
      }
    } catch (JMSException e) {
      log.warn("JMSException", e);
    } catch (IOException e) {
      log.warn("IOException", e);
    } finally {
      if (messageConsumer != null) {
        try {
          messageConsumer.close();
        } catch (JMSException e) {
          log.warn("JMSException-ConsumerClose", e);
        }
      }
    }
  }

  private int weighMessage(Message message) throws JMSException {
    return message.getIntProperty(WorkConstants.WEIGHT);
  }

  private Session getSession() throws JMSException {
    if (session == null) {
      Connection connection = getConnection();
      session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
    }
    return session;
  }

  private Connection getConnection() throws JMSException {
    if (connection == null) {
      connection = connectionFactory.createConnection();
      connection.start();
    }
    return connection;
  }
}
