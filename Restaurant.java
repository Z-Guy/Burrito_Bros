import java.util.concurrent.*;
import java.util.LinkedList;

public class Restaurant implements Runnable {
    private static Restaurant place = new Restaurant();
    public int inside = 0; // number of customers inside
    public int inLine = 0; // number of customer waiting in line
    public int cID = 0;
    Customer line[] = new Customer[Burrito.Capacity];
    LinkedList<Customer> RegisterLine = new LinkedList<Customer>();

    Semaphore Entrance = new Semaphore(1); // determines if a customer can enter the store
    Semaphore waitArea = new Semaphore(1); // determines when a customer gets in line for service
    Semaphore Counters = new Semaphore(1); // determines when a server gets access to a counter
    Semaphore Servicing = new Semaphore(0); // determines when a server helps a customer
    Semaphore waitRegister = new Semaphore(1); // determines when a customer can wait for the register
    Semaphore Register = new Semaphore(1); // determines when the customer can use the register

    public static Restaurant place() {
        return place;
    }

    // method for customer entering the place
    public void Enter() {
        try {
            Entrance.acquire(); // customers enter one at a time
            cID++; // so that a customer ID can be made
            // detrmine if the place is full
            if (inside < Burrito.Capacity) {
                ++inside; // customer can enter
                Customer CC = new Customer();
                CC.make_cID(cID); // making customer ID
                System.out.println("Customer " + CC.show_cID() + " with " + CC.orderSize()
                        + " order(s) has entered Burritos Bros.");
                findInLine(CC, true); // customer enters line

                Servicing.release(); // server is signaled of a customer ready
                Entrance.release(); // another customer can enter
            } else {
                System.out.println("At Capacity so Customer " + cID + " cannot enter.");
                Entrance.release(); // another customer can enter
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // method to sort line based on shortest number of orders
    public void findInLine(Customer CC, boolean just) {
        try {
            waitArea.acquire(); // one customer is where to go in line
            // loop for finding where to put customer in line
            if (inLine == 0) {
                EnterLine(1, 0, CC);
            } else {
                if (inLine == 1 && !just) {
                    line[1] = CC;
                } else {
                    if (inLine == 1 && just) {
                        if (CC.orderSize() > line[0].orderSize()) {
                            line[1] = CC;
                        } else {
                            line[1] = line[0];
                            line[0] = CC;
                        }
                    } else {
                        for (int i = 0; i < inLine - 1; ++i) {
                            if (line[i].orderSize() > line[i + 1].orderSize()) {
                                EnterLine(inLine + 1, i + 1, CC);
                                break;
                            }
                            if (i == inLine - 2) {
                                line[i + 2] = CC;
                            }
                        }
                    }
                }
            }
            inLine++;
            waitArea.release();
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
    }

    // method for inserting customer into line
    public void EnterLine(int CCinLine, int Sort, Customer CC) {
        // loop for seeing the postion of every customer in line
        if (CCinLine == 1) {
            line[0] = CC;
        } else {
            for (int i = CCinLine - 1; i > Sort; --i) {
                if (CC.orderSize() < line[i - 1].orderSize()) {
                    line[i] = line[i - 1];
                    if (i == Sort + 1) {
                        line[i - 1] = CC;
                    }
                } else {
                    line[i] = CC;
                }
            }
        }
        System.out.println("Customer " + CC.show_cID() + " with " + CC.orderSize() + " order(s) goes in line.");
    }

    // method for customer to leave line and go to server
    public Customer approachCounter(int sID) {
        Customer watching = line[0]; // customer first in line goes to server
        System.out.println("Server " + sID + " is serving Customer " + watching.show_cID() + " at a counter.");
        for (int i = 0; i < inLine; ++i) { // line is moved up to fill the space
            line[i] = line[i + 1];
        }
        --inLine; // line is shorter
        line[inLine] = null;
        return watching;
    }

    // method for server preparing burritos for specific customer
    public void Making(int orders, int sID) {
        System.out.println("Server " + sID + " making burrito(s).");
        try {
            Thread.sleep(orders * Burrito.timeCooking);
        } // time for server to make orders
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // method for customer to line up for the register
    public void Register(Customer waiting) {
        try {
            waitRegister.acquire(); // one customer gets in line
            RegisterLine.addLast(waiting); // using linkedlist to add customer in line for payment
            waitRegister.release(); // another customer can get in line
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
    }

    // method for customer to pay then leave the place
    public void Payment() {
        Customer CC; // making variable for customer
        while (!RegisterLine.isEmpty()) {
            CC = RegisterLine.pollFirst(); // using linkedlist to get customer first in line
            System.out.println("Customer " + CC.show_cID() + " hass paid for order(s).");
            try {
                Thread.sleep(Burrito.timeRegister); // time at register
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Customer " + CC.show_cID() + " has left Burrito Bros.");
            System.out.println(" ");
            inside--; // customer is leaving the place
        }
        Register.release(); // new customer can use the register
    }

    public void run() {
        Enter();
    }
}