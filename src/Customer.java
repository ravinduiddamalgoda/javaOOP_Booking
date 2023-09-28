public class Customer {

    private String firstName;
    private String lastName;
    private int pizzasRequired;
    private int CID;

    public Customer(int CID , String firstName, String lastName, int pizzasRequired ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.pizzasRequired = pizzasRequired;
        this.CID = CID;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getPizzasRequired() {
        return pizzasRequired;
    }

    public int getCID() {
        return CID;
    }
}
