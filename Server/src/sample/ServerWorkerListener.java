package sample;

import java.io.IOException;

public interface ServerWorkerListener {
    void tcpConnectionCreateException(IOException e);

    void serverSocketCreateException(IOException e);
}
