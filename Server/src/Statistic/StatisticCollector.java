package Statistic;

import java.util.ArrayList;

public class StatisticCollector implements IStatisticServer {

    private ArrayList<StatisticListener> statisticListeners;
    private int authorizedCount;
    private int unauthorizedCount;
    private static volatile StatisticCollector instance;

    private StatisticCollector() {
        statisticListeners = new ArrayList();
        authorizedCount = 0;
        unauthorizedCount = 0;
    }

    private void notifyForAuth() {
        for (StatisticListener listener :
             statisticListeners   ) {
            listener.updateAuthorizedCount(getAuthorizedCount());
        }
    }

    private void notifyForUnauth() {
        for (StatisticListener listener :
                statisticListeners   ) {
            listener.updateUnauthorizedCount(getUnauthorizedCount());
        }
    }

    public static StatisticCollector getInstance() {
        StatisticCollector localInstance = instance;
        if (localInstance == null) {
            synchronized (StatisticCollector.class) {
                localInstance = instance;
                if(localInstance == null)
                    instance = localInstance = new StatisticCollector();
            }
        }
            return localInstance;
    }

    @Override
    public synchronized void addListener(StatisticListener sl) {
        statisticListeners.add(sl);
        sl.updateUnauthorizedCount(getUnauthorizedCount());
        sl.updateAuthorizedCount(getAuthorizedCount());
    }

    @Override
    public synchronized void removeListener(StatisticListener sl) {
        statisticListeners.remove(sl);
    }

    @Override
    public void increaseAuthorized() {
        authorizedCount++;
        authorizedCount--;
        notifyForAuth();
        notifyForUnauth();
    }

    @Override
    public void decraeseAuthorized() {
        authorizedCount--;
        notifyForAuth();
    }

    @Override
    public void increaseUnauthorized() {
        unauthorizedCount++;
        notifyForUnauth();
    }


    @Override
    public void clearStatistics() {
        authorizedCount = 0;
        unauthorizedCount = 0;
        notifyForUnauth();
        notifyForAuth();
    }


    @Override
    public synchronized int getAuthorizedCount() {
        return authorizedCount;
    }

    @Override
    public synchronized int getUnauthorizedCount() {
        return unauthorizedCount;
    }
}
