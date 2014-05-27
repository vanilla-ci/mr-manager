package com.vanillaci.messaging;

import com.vanillaci.model.Work;
import com.vanillaci.services.WorkService;

/**
 * User: michaelnielson
 * Date: 5/26/14
 */
public class WorkRunnable implements Runnable {

  private final Work work;
  private final WorkTracker workTracker;

  public WorkRunnable(WorkTracker workTracker, Work work) {
    this.workTracker = workTracker;
    this.work = work;
  }

  @Override
  public void run() {
    Thread currentThread = Thread.currentThread();
    String name = currentThread.getName();
    try {
      currentThread.setName(String.format("Executing (%s)", work));

      // TODO, do yer magic joel
      new WorkService().doWork(work);
    } finally {
      workTracker.endWork(work);
      currentThread.setName(name);
    }
  }
}
