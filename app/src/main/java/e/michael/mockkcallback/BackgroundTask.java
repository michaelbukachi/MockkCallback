package e.michael.mockkcallback;

public class BackgroundTask {

    public void doBackground(Callback callback) {
        new Thread(() -> {
            try {
                Thread.sleep(5000);
                callback.onDone(true, "Success");
            } catch (InterruptedException e) {
                e.printStackTrace();
                callback.onDone(false, "Failed");
            }
        }).start();
    }
}
