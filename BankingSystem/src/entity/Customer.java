
package entity;

public class Customer {
    private int customer_id;
    private String first_name;
    private String last_name;
    private String email;
    private long phone_number;
    private String address;

    // Default constructor
    public Customer() {
    }

    // Overloaded constructor with Account attributes
    public Customer(int customer_id, String first_name, String last_name, String email, long phone_number, String address) {
        this.customer_id = customer_id;
        this.first_name = first_name;
        this.last_name = last_name;
        setEmail(email); // Validate email in the constructor
        setphone_number(phone_number); // Validate phone number in the constructor
        this.address = address;
    }

    // Getter methods
    public int getCustomer_id() {
        return customer_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getEmail() {
        return email;
    }

    public long getPhone_number() {
        return phone_number;
    }

    public String getAddress() {
        return address;
    }

    // Setter methods
    public void setcustomer_id(int customer_id) {
        this.customer_id = Customer.this.customer_id;
    }

    public void setfirst_name(String first_name) {
        this.first_name = Customer.this.first_name;
    }

    public void setlast_name(String last_name) {
        this.last_name = last_name;
    }

    public void setEmail(String email) {
        // Validate email without regex
        if (email.contains("@") && email.contains(".") && email.indexOf("@") < email.lastIndexOf(".")) {
            this.email = email;
        } else {
            System.out.println("Invalid email address");
        }
    }

    public void setphone_number(long phone_number) {
        // Validate 10-digit phone number without regex
        if (String.valueOf(phone_number).length() == 10) {
            this.phone_number = phone_number;
        } else {
            System.out.println("Invalid phone number");
        }
    }

    public void setAddress(String address) {
        this.address = address;
    }

    // Method to print all information
    public void printInfo() {
        System.out.println("Customer ID: " + customer_id);
        System.out.println("First Name: " + first_name);
        System.out.println("Last Name: " + last_name);
        System.out.println("Email Address: " + email);
        System.out.println("Phone Number: " + phone_number);
        System.out.println("Address: " + address);
    }

    
}
