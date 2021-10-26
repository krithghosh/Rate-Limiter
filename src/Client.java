public class Client {

    public static void main(String[] args) {
        ClientIdentifier clientIdentifier1 = new ClientIdentifier(1, 3);
        ClientIdentifier clientIdentifier2 = new ClientIdentifier(2, 3);

        for (int i = 0; i < 100; i++) {
            new Thread(clientIdentifier1).start();
            new Thread(clientIdentifier2).start();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
