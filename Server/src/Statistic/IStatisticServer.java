package Statistic;

public interface IStatisticServer {

    void addListener(StatisticListener sl);

    void removeListener(StatisticListener sl);

    void increaseAuthorized();

    void decraeseAuthorized();

    void increaseUnauthorized();


    void clearStatistics();


    int getAuthorizedCount();

    int getUnauthorizedCount();



}
