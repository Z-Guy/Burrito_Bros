# Burrito_Bros
This is a simualtor that I made to resmble a restaurant with customers, servers, and a path for customers while in the restaurant. This was made to test my understanding
of threads and how to make progrmas that mangage resources with semaphores.

![image](https://user-images.githubusercontent.com/49008866/177898033-8723401d-4e7c-4962-9933-17a7f10e0ed6.png)
There are two threads: the Customer and the Server. The restaurant space has a maximum capacity of 15 Customers at a time. There are three Servers that can work only at their repective counters, can only serve one Customer at a time, and fufill three orders at one time. A Customer can have between 1 to 20 orders and they are at random. The Customers, once inside, go to a waiting area before they can go to a counter. The waiting area is an array so the customer with the least number of orders gets served first. Once all of their orders are fufilled, the Customers go to the register to pay for their food and leave the establishment. Both the Server and the register take a set amont of time to complete their tasks.
The flow goes like this:
  1) Customer enters restaurant
  2) Customer goes to the waiting area to stand in a queue
  3) Server arrives to a counter
  4) Customer with least number of orders goes to the available Server
    - if the Customer has more orders to made made, they return to the waiting area
  5) Customer has all of their orders fufilled
  6) Customer goes to the register to pay for the food
    - if there is already someone at the register, Customer waits till they leave
   7) Customer leaves store after paying
   
My first test results are shown below:
![image](https://user-images.githubusercontent.com/49008866/177901852-aa8ca76e-101a-4cc8-a5de-24c9dcaa36a7.png)
![image](https://user-images.githubusercontent.com/49008866/177901862-1c5b2028-805f-4e10-95fe-c679a7d92f8a.png)

My second test results are shown below:
![image](https://user-images.githubusercontent.com/49008866/177901887-906179ee-0bd1-47b6-bcca-31fb22272e3f.png)
![image](https://user-images.githubusercontent.com/49008866/177901911-8cdfcc0d-fc24-4d3a-b631-61a725ce5068.png)

  
