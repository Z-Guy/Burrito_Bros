public class Burrito{
    protected static int NoOfCustomers = 20;            //number of Customers in the total runtime
    protected static int amtServers = 3;                //number of servers total
    protected static int Capacity = 15;                 //number of customers in place Max
    protected static int ArrivalWindow = 4000;          //how often customers can come in
    protected static int timeCooking = 1000;            //time to make burritos
    protected static int timeRegister = 1000;           //time at register before leaving

    public static void main(String args[]) throws Exception{
        System.out.println("Welcome to Burrito Brothers!!!!!!!!!");
        System.out.println(" ");

        //Making thread for servers
        for (int i = 0; i < amtServers; ++i){
            Thread Servers = new Thread(new Server(i));
            Servers.start();
        }

        //making threads for customers
        for (int i = 0; i < NoOfCustomers; ++i){
            Thread Customers = new Thread(new Restaurant().place());
            Customers.start();

            try{
                Thread.sleep(1000);
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }
}