package com.vanillaci.messaging;

import com.vanillaci.model.Work;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * User: michaelnielson
 * Date: 5/26/14
 */
public class WorkTracker {
  private final List<WorkEntry> runningWork = new ArrayList<>();

  public synchronized void startWork(Work work, int weight) {
    WorkEntry workEntry = newWorkEntry(work, weight);
    runningWork.add(workEntry);
  }

  public synchronized void endWork(Work work) {
    runningWork.remove(findWorkEntry(work));

    // signal waiting classes that a change has occurred
    notifyAll();
  }

  public synchronized int runningWeight() {
    int totalWeight = 0;
    for (WorkEntry workEntry : runningWork) {
      totalWeight += workEntry.getWeight();
    }
    return totalWeight;
  }

  private WorkEntry newWorkEntry(@NotNull Work work, int weight) {
    return new WorkEntry(work, weight);
  }

  private WorkEntry findWorkEntry(@NotNull Work work) {
    for (WorkEntry workEntry : runningWork) {
      if (workEntry.getWork().equals(work)) {
        return workEntry;
      }
    }
    return null;
  }

}
