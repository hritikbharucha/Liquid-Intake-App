package edu.sdccd.cisc191.template;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CustomerRequestTest {
    private CustomerRequest customerRequest;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        customerRequest = new CustomerRequest(1);
    }

    @org.junit.jupiter.api.Test
    void getCustomer() {
        assertEquals(customerRequest.toString(), "Customer[id=1]");
    }

    @org.junit.jupiter.api.Test
    void setCustomer() {
        assertEquals(customerRequest.toString(), "Customer[id=1]");
    }
}