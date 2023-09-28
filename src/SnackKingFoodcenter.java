import java.io.*;
import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


class SnackKingFoodcenter {


    public static void main(String[] args) {
// Initialize scanner and queues
        Scanner scanner = new Scanner(System.in);
        FoodQueue[] queues = new FoodQueue[3];
        queues[0] = new FoodQueue(2);
        queues[1] = new FoodQueue(3);
        queues[2] = new FoodQueue(5);


        String choice;
        do {
            // Display the main menu
            displayMenu();
            choice = scanner.nextLine();

            switch (choice.toUpperCase()) {
                case "100", "VFQ" -> viewAllQueues(queues);
                case "101", "VEQ" -> viewAllEmptyQueues(queues);
                case "102", "ACQ" -> addCustomerToQueue(scanner, queues);
                case "103", "RCQ" -> removeCustomerFromQueue(scanner, queues);
                case "104", "PCQ" -> removeServedCustomer(queues);
                case "105", "VCS" -> viewCustomersSorted(queues);
                case "106", "SPD" -> storeProgramData(queues);
                case "107", "LPD" -> loadProgramData(queues);
                case "108", "STK" -> viewRemainingPizzaStock();
                case "109", "AFS" -> addPizzaToStock(scanner);
                case "110", "IFQ" -> printQueueIncome(queues);
                case "999", "EXT" -> System.out.println("Exiting the program.");
                default -> System.out.println("Invalid choice. Please try again.");
            }
        } while (!choice.equals("999"));

        scanner.close();
    }
    // View all queues' status
    private static void displayMenu() {
        System.out.println("********************");
        System.out.println("* Snack King Food Center *");
        System.out.println("********************");
        System.out.println("100 or VFQ: View all Queues.");
        System.out.println("101 or VEQ: View all Empty Queues.");
        System.out.println("102 or ACQ: Add customer to a Queue.");
        System.out.println("103 or RCQ: Remove a customer from a Queue. (From a specific location)");
        System.out.println("104 or PCQ: Remove a served customer.");
        System.out.println("105 or VCS: View Customers Sorted in alphabetical order (Do not use library sort routine)");
        System.out.println("106 or SPD: Store Program Data into file.");
        System.out.println("107 or LPD: Load Program Data from file.");
        System.out.println("108 or STK: View Remaining pizza Stock.");
        System.out.println("109 or AFS: Add pizza to Stock.");
        System.out.println("110 or IFQ: Print income of each queue.");
        System.out.println("999 or EXT: Exit the Program.");
        System.out.print("Enter your choice: ");
    }
    // View all empty queues
    private static void viewAllQueues(FoodQueue[] queues) {
        System.out.println("*****************");
        System.out.println("* Cashiers *");
        System.out.println("*****************");

        int maxQueueSize = getMaxQueueSize(queues);

        for (int row = 0; row < maxQueueSize; row++) {
            for (FoodQueue queue : queues) {
                if (row < queue.getNumberOfCustomers()) {
                    System.out.print("O ");
                } else if (row < queue.getMaxCapacity()) {
                    System.out.print("X ");
                } else {
                    System.out.print("  ");
                }
            }
            System.out.println();
        }
    }

    private static int getMaxQueueSize( FoodQueue[] queues) {
        int maxQueueSize = 0;
        for (FoodQueue queue : queues) {
            if (queue.getMaxCapacity() > maxQueueSize) {
                maxQueueSize = queue.getMaxCapacity();
            }
        }
        return maxQueueSize;
    }

    private static void viewAllEmptyQueues(FoodQueue[] queues) {
        System.out.println("Empty Queues:");
        for (int i = 0; i < queues.length; i++) {
            if (queues[i].isEmpty()) {
                System.out.print("Queue " + (i + 1) + ": ");
               for(int j = 0; j < queues[i].getMaxCapacity(); j++){

                   System.out.print(j+1);
                   System.out.print(" ");
               }
               System.out.println();
            }else{
                System.out.print("Queue " + (i + 1) + ": ");
                int sizeData = queues[i].getSize();
                for(int j = sizeData + 1 ; j <  queues[i].getMaxCapacity() + 1;  j++){

                    System.out.print(j);
                    System.out.print(" ");
                }
                System.out.println();
            }



        }
    }
    // Add customer to a queue
    private static void addCustomerToQueue(Scanner scanner, FoodQueue[] queues) {
        System.out.print("Enter the Customer ID: ");
        int ID = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter customer first name: ");
        String firstName = scanner.nextLine();
        System.out.print("Enter customer last name: ");
        String lastName = scanner.nextLine();
        System.out.print("Enter the number of pizza required: ");
        int pizzaRequired = scanner.nextInt();
        scanner.nextLine(); // Consume the newline left by nextInt()
        // Find the queue with the minimum length
        FoodQueue shortestQueue = queues[0];
        int idT = 0;
        for (int i = 1; i < queues.length; i++) {

                if ( queues[i].getSize() < shortestQueue.getSize() ) {

                    shortestQueue = queues[i];
                    idT = i;

                } else if (queues[i].getSize() == shortestQueue.getSize() && (shortestQueue.getSize() != 0 || queues[i].getSize() != 0 )) {

                    if ((i == 2) && (queues[i].getSize() == queues[i-1].getSize()) && queues[0].isFull()) {
                        // Apply your special logic here
                        shortestQueue = queues[1];
                        idT = 1;
                    }
         } else if (queues[i].getSize() == 3) {
                    shortestQueue= queues[2];
                    idT = 2;
                    break;
                }else if (queues[i].getSize() == 4) {
                    shortestQueue= queues[2];
                    idT = 2;
                    break;
                }


        }



        if (shortestQueue.isFull()) {

            Customer customer = new Customer(ID, firstName, lastName, pizzaRequired);
            if (shortestQueue.isWaitingListFull()) {
                System.out.println("All queues and waiting list are full. Customer not added.");
            } else {
                shortestQueue.addToWaitingList(customer);
                System.out.println("All queues are full. Customer added to the waiting list.");

            }
        } else {
            shortestQueue.addCustomer(new Customer(ID, firstName, lastName, pizzaRequired));
            shortestQueue.addPizzaIncome(pizzaRequired);
            System.out.println(firstName + " " + lastName + " added to Queue " + (Arrays.asList(queues).indexOf(shortestQueue) + 1) + ".");
        }


    }
// Remove a customer from a queue

    private static void removeCustomerFromQueue(Scanner scanner, FoodQueue[] queues) {
        int queueNumber;
        do {
            System.out.print("Enter the queue number (1, 2, or 3): ");
            queueNumber = scanner.nextInt();
            scanner.nextLine(); // Consume the newline left by nextInt()

            if (queueNumber < 1 || queueNumber > 3) {
                System.out.println("Invalid queue number. Please try again.");
            } else {
                FoodQueue queue = queues[queueNumber - 1];
                if (queue.isEmpty()) {
                    System.out.println("Queue " + queueNumber + " is empty. No customers to remove.");
                } else {
//                    queue.displayQueue();
                    System.out.print("Enter the position of the customer to remove: ");
                    int position = scanner.nextInt();
                    scanner.nextLine(); // Consume the newline left by nextInt()

                    if (position < 1 || position > queues[queueNumber - 1].getSize()){
                        System.out.println("Invalid position. Please try again.");
                    } else {
                        String removedCustomer = String.valueOf(queue.removeCustomerAtPosition(position - 1));
                        System.out.println("Removed customer " + removedCustomer + " from Queue " + queueNumber + ".");
                    }
                }
            }
        } while (queueNumber < 1 || queueNumber > 3);
    }

    // Remove a served customer
    private static void removeServedCustomer( FoodQueue[] queues) {
        int queueNumber;
        boolean val = true;
        int cnt = 0;
        while(val) {

            System.out.print("Enter the queue number (1, 2, or 3): ");
            Scanner scanner = new Scanner(System.in);
            queueNumber = scanner.nextInt();
            scanner.nextLine(); // Consume the newline left by nextInt()

            if (queueNumber < 1 || queueNumber > 3) {
                System.out.println("Invalid queue number. Please try again.");
                cnt++;
                if (cnt == 4) {
                    System.out.println("You have Entered Wrong Numbers again and again your exiting from this!!");
                    val = false;
                }
            } else {
                FoodQueue queue = queues[queueNumber - 1];
                if (queue.isEmpty()) {
                    System.out.println("Queue " + queueNumber + " is empty. No customers to serve.");
                } else {
                    queue.removeServedCustomer();
                    System.out.println("Customer served and removed from Queue " + queueNumber + ".");
                    if (!queue.isWaitingListEmpty()) {
                        Customer waitingCustomer = queue.removeFromWaitingList();
                        if (waitingCustomer != null) {
                            queue.addCustomer(waitingCustomer);
                            System.out.println("Customer from waiting list added to Queue " + queueNumber + ".");
                        }
                    }
                }

                val = false;
            }


        }
    }
// View customers sorted in alphabetical order

    private static void viewCustomersSorted(FoodQueue[] queues) {
        Customer[] allCustomers = new Customer[countTotalCustomers(queues)];
        int index = 0;

        for (FoodQueue queue : queues) {
            Customer[] queueCustomers = queue.getCustomers();
            for (Customer customer : queueCustomers) {
                if (customer != null) {
                    allCustomers[index] = customer;
                    index++;
                }
            }
        }

//        sortCustomers(allCustomers);

        int length = allCustomers.length;
        int n = length;
        String[] nameList = new String[length];
        int i =0;

        for(i = 0; i < length; i++){
            nameList[i] = allCustomers[i].getFirstName();
        }
        Arrays.sort(nameList);
        System.out.println("Customers Sorted in Alphabetical Order:");
        for(String name : nameList){
            System.out.println(name);
        }
    }

    private static int countTotalCustomers(FoodQueue[] queues) {
        int count = 0;
        for (FoodQueue queue : queues) {
            count += queue.getNumberOfCustomers();
        }
        return count;
    }


    // Store program data into a file
    private static void storeProgramData(FoodQueue[] queues) {
        try {
            FileWriter fileWriter = new FileWriter("data.txt");
            fileWriter.write("Remaining Stock: "+ FoodQueue.getPizzaInStock() + "\n");
            int i = 1;
            for (FoodQueue queue : queues) {
                fileWriter.write("Queue " + i + "\n");
                for (Customer customer : queue.getCustomers()) {
                    if (customer != null) {
                        fileWriter.write("Customer Id " + customer.getCID() +" customer name :"+customer.getFirstName() +" "+customer.getLastName() + " -ordered pizza's : " + customer.getPizzasRequired() + "\n");
                    }
                }
                fileWriter.write("\n"); // Mark the end of queue data
                i++;
            }

            fileWriter.close();
            System.out.println("Program data stored successfully.");
        } catch (IOException e) {
            System.out.println("Error storing program data: " + e.getMessage());
        }
    }
    // Load program data from a file
    private static void loadProgramData(FoodQueue[] queues) {

        try {

            BufferedReader bufferedReader = new BufferedReader(new FileReader("data.txt"));

            Scanner scan = new Scanner(bufferedReader);
            String stock = scan.nextLine();
            System.out.println(stock);
            while (scan.hasNext()) {                               // Read and display file lines
                String line = scan.nextLine();
                System.out.println(line);
            }
            System.out.println("Program data loaded from file.");
        } catch (IOException e) {
            System.out.println("Error loading program data from file." + e.getMessage());
        }

    }


    // View remaining pizza stock
    private static void viewRemainingPizzaStock() {
        System.out.println("Remaining pizza in Stock: " + FoodQueue.getPizzaInStock());
    }
    // Add pizzas to stock
    private static void addPizzaToStock(Scanner scanner) {
        System.out.print("Enter the number of pizzas to add to stock: ");
        int pizzasToAdd = scanner.nextInt();
        scanner.nextLine(); // Consume the newline left by nextInt()

        FoodQueue.addPizzaToStock(pizzasToAdd);
        System.out.println(pizzasToAdd + " pizzas added to stock. Total stock: " + FoodQueue.getPizzaInStock());

        if (FoodQueue.getPizzaInStock() <= 10) {
            System.out.println("Warning: Low stock! Only " + FoodQueue.getPizzaInStock() + " pizza remaining.");
        }
    }
    // Print income of each queue
    private static void printQueueIncome(FoodQueue[] queues) {
        System.out.println("Queue Incomes:");
        for (int i = 0; i < queues.length; i++) {
            FoodQueue queue = queues[i];
            int income = queue.getIncome();
            System.out.println("Queue " + (i + 1) + ": " + income + " (pizzas sold: " + queue.getPizzaSold() + ")");
        }
    }



}
