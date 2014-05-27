package com.vanillaci.messaging;

import com.vanillaci.model.Work;

/**
* User: michaelnielson
* Date: 5/26/14
*/
class WorkEntry {
  private final int weight;
  private final Work work;
  private final long startTime;

  WorkEntry(Work work, int weight) {
    this.weight = weight;
    this.work = work;
    this.startTime = System.currentTimeMillis();
  }

  public long getRunTime() {
    return System.currentTimeMillis() - startTime;
  }

  public long getStartTime() {
    return startTime;
  }

  public int getWeight() {
    return weight;
  }

  public Work getWork() {
    return work;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    WorkEntry workEntry = (WorkEntry) o;

    return work.equals(workEntry.work);

  }

  @Override
  public int hashCode() {
    return work.hashCode();
  }
}
