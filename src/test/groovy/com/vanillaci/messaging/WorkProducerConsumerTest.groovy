package com.vanillaci.messaging

import com.vanillaci.model.Work
import org.hornetq.jms.server.embedded.EmbeddedJMS
import org.junit.Test

import javax.jms.ConnectionFactory
import javax.jms.Queue

/**
 * User: michaelnielson
 * Date: 5/26/14
 */
class WorkProducerConsumerTest {

  @Test
  public void testProduceAndConsume() throws Exception {
    EmbeddedJMS jmsServer = new EmbeddedJMS()
    jmsServer.start()

    ConnectionFactory connectionFactory = (ConnectionFactory) jmsServer.lookup("ConnectionFactory")

    Queue workerQueue = jmsServer.lookup("/queue/workerQueue") as Queue
    WorkProducer workProducer = new WorkProducer(connectionFactory, workerQueue)

    Work work = new Work("someid", [:], [])
    workProducer.sendWork(work)

    WorkConsumer workConsumer = new WorkConsumer(connectionFactory, workerQueue, JmsExpression.EMPTY, 1)
    workConsumer.start()

    // hard to assert anything until any work is actually done, this is just a POC
    Thread.sleep(100)

    jmsServer.stop()
  }
}
