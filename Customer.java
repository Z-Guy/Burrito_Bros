import java.util.concurrent.*;

public class Customer{
    private int cID;                    //customer IDs
    private int orderSize;              //number of burritos per customer

    public Customer(){
        this.orderSize = (int) (1 + 20 * Math.random());
    }

    //declaring the number of burritos per customer
    public int orderSize(){
        return orderSize;
    }

    //declaring new number of burritos if there are more after service
    public void newSize(){
        this.orderSize = this.orderSize - 3;
    }

    //display customer ID
    public int show_cID(){
        return cID;
    }

    //make customer ID
    public void make_cID(int cID){
        this.cID = cID;
    }
}
