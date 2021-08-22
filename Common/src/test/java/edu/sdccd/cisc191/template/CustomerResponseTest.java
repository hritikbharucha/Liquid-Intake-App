package edu.sdccd.cisc191.template;

import static org.junit.jupiter.api.Assertions.*;

class CustomerResponseTest {
    private CustomerResponse customerResponse;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        customerResponse = new CustomerResponse(1, "Test", "User");
    }

    @org.junit.jupiter.api.Test
    void getCustomer() {
        assertEquals(customerResponse.toString(), "Customer[id=1, firstName='Test', lastName='User']");
    }

    @org.junit.jupiter.api.Test
    void setCustomer() {
        customerResponse.setFirstName("User");
        customerResponse.setLastName("Test");
        assertEquals(customerResponse.toString(), "Customer[id=1, firstName='User', lastName='Test']");
    }
}