package ru.sfedu.myApp.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    public static final User user = new User();


    @Test
    void testAddDelivery() throws Exception {
        user.addDelivery(1,1,1,500);

        assertNotNull(user.getOrder().getDelivery());
        assertEquals(10000,user.getOrder().getFinalCost());
    }
    @Test
    void testAddExistingDelivery() throws Exception {
        user.addDelivery(1,1,1,500);

        Exception exception = assertThrows(Exception.class,()->{
            user.addDelivery(1,1,1,500);
        });
        assertEquals("Delivery has been already added",exception.getMessage());
    }

    @Test
    void testAddStorage() throws Exception {
        user.addStorage(1,1,1,10);

        assertNotNull(user.getOrder().getStorage());
        assertEquals(220,user.getOrder().getFinalCost());
    }
    @Test
    void testAddExistingStorage() throws Exception {
        user.addStorage(1,1,1,10);

        Exception exception = assertThrows(Exception.class,()->{user.addStorage(1,1,1,10);});
        assertEquals("Storage has been already added",exception.getMessage());
    }

    @Test
    void addStorageInsurance() throws Exception {
        user.addStorage(1,1,1,10);
        user.addStorageInsurance();

        assertEquals(true,user.getOrder().isInsuranceForStorageFlag());
        assertEquals(20220,user.getOrder().getFinalCost());
    }
    @Test
    void addExistingStorageInsurance() throws Exception {
        user.addStorage(1,1,1,10);
        user.addStorageInsurance();

        Exception exception = assertThrows(Exception.class,()->{
            user.addStorageInsurance();
        });
        assertEquals("Storage insurance has been already added",exception.getMessage());
    }

    @Test
    void addDeliveryInsurance() throws Exception {
        user.addDelivery(1,1,1,500);
        user.addDeliveryInsurance();

        assertEquals(true,user.getOrder().isInsuranceForDeliveryFlag());
        assertEquals(30000,user.getOrder().getFinalCost());
    }
    @Test
    void addExistingDeliveryInsurance() throws Exception {
        user.addDelivery(1,1,1,10);
        user.addDeliveryInsurance();
        Exception exception = assertThrows(Exception.class,()->{
            user.addDeliveryInsurance();
        });
        assertEquals("Delivery insurance has been already added",exception.getMessage());
    }
}