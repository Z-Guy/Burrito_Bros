import java.util.concurrent.*;

public class Server implements Runnable{
    private int sID;                    //server IDs
    private Customer watchFood;         //Customer who is being served

    public Server(int sID){
        this.sID = sID;
    }

    public void Service(){
        try{
            //server and customer meeting at counter
            Restaurant.place().Counters.acquire();
            watchFood = Restaurant.place().approachCounter(sID);
            Restaurant.place().Counters.release();

            //server making orders then sendng customer either back in line or to the register
            if(watchFood.orderSize() > 3){
                Restaurant.place().Making(3, sID);                                                              // made three burritos
                watchFood.newSize();                                                                            // results
                System.out.println("Customer " + watchFood.show_cID() + " reenters the line.");                 //send customer back to line
                Restaurant.place().findInLine(watchFood, false);                                                //server can serve another customer
                Restaurant.place().Servicing.release();
            } else{
                Restaurant.place().Making(watchFood.orderSize(), sID);                                          //complete full orders
                Restaurant.place().Register(watchFood);                                                         // try to get the register
                if(!Restaurant.place().RegisterLine.isEmpty() && Restaurant.place().Register.tryAcquire()){     
                    Restaurant.place().Payment();                                                               //customer pays for oders
                }
            }
        } catch(InterruptedException e){e.printStackTrace();}
    }

    public void run(){
        try{
            if (Restaurant.place().Servicing.tryAcquire(Burrito.ArrivalWindow,TimeUnit.MILLISECONDS)){
                Service();
            }
        }catch (InterruptedException e){e.printStackTrace();}
    }
}