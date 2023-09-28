public class FoodQueue {
    private final int maxCapacity;
    private final Customer[] customers;
    private static int pizzaInStock = 100;
    private int currentSize;
//
    private int totalCnt =0;

    private int waitingListSize = 0;
    private int waitingListFront = 0;
    private int waitingListRear = 0;
    private static final int WAITING_LIST_CAPACITY = 5;
    private Customer[] waitingList = new Customer[WAITING_LIST_CAPACITY];

    public FoodQueue(int maxCapacity) {
        this.maxCapacity = maxCapacity;
        this.customers = new Customer[maxCapacity];
        this.currentSize = 0;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public int getSize() {
        return currentSize;
    }

    public boolean isEmpty() {
        return currentSize == 0;
    }

    public Customer[] getCustomers() {
        Customer[] customersArray = new Customer[currentSize];
        int index = 0;
        for (Customer customer : customers) {
            if (customer != null) {
                customersArray[index] = customer;
                index++;
            }
        }
        return customersArray;
    }


    public boolean removeCustomerAtPosition(int position) {
        if (position < 0 || position >= currentSize) {
            System.out.println("Invalid position.");
            return false;
        }

        Customer removedCustomer = customers[position];
        pizzaInStock += removedCustomer.getPizzasRequired();

        // Shift customers to fill the gap
        for (int i = position; i < currentSize - 1; i++) {
            customers[i] = customers[i + 1];
        }
        customers[currentSize - 1] = null;
        currentSize--;

        return true;
    }

    public void addCustomer(Customer customer) {
        if (currentSize < maxCapacity) {
            customers[currentSize] = customer;
            currentSize++;

            pizzaInStock -= customer.getPizzasRequired();
        } else {
            System.out.println("Queue is full. Cannot add more customers.");
        }
    }
    public void addPizzaIncome( int pizza){totalCnt += pizza; }

    public void removeServedCustomer() {
        if (currentSize > 0) {
            Customer servedCustomer = customers[0];
            System.arraycopy(customers, 1, customers, 0, currentSize - 1);
            customers[currentSize - 1] = null;
            currentSize--;
        }
    }

    public static int getPizzaInStock() {
        return pizzaInStock;
    }

    public static void addPizzaToStock(int pizzaToAdd) {
        pizzaInStock += pizzaToAdd;
    }

    public int getPizzaSold() {
        return totalCnt;
    }

    public int getIncome() {
        return getPizzaSold() * 1350;
    }


    public int getNumberOfCustomers() {
        return currentSize;
    }

    public boolean isFull() {
        return currentSize >= maxCapacity;
    }



    public boolean isWaitingListFull() {
        return waitingListSize == WAITING_LIST_CAPACITY;
    }

    public void addToWaitingList(Customer customer) {
        if (!isWaitingListFull()) {
            waitingList[waitingListRear] = customer;
            waitingListRear = (waitingListRear + 1) % WAITING_LIST_CAPACITY;
            waitingListSize++;
        } else {
            System.out.println("Waiting list is full. Customer not added.");
        }
    }


    public Customer removeFromWaitingList() {
        if (!isWaitingListEmpty()) {
            Customer customer = waitingList[waitingListFront];
            waitingList[waitingListFront] = null;
            waitingListFront = (waitingListFront + 1) % WAITING_LIST_CAPACITY;
            waitingListSize--;
            return customer;
        } else {
            return null;
        }
    }

    public boolean isWaitingListEmpty() {
        return waitingListSize == 0;
    }






}