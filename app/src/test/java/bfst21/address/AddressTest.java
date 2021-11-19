package bfst21.address;

import bfst21.vector.addressparser.Address;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AddressTest {

    @BeforeEach
    public void setUP(){

    }

    @AfterEach
    public void tearDown(){
    }

    @Test
    public void testStreetHouse() {
        Address address = Address.parse("Rued Langgaards Vej 7");
        assertEquals("Rued Langgaards Vej", address.street);
        assertEquals("7", address.houseNumber);
    }

    @Test
    public void testStreetHousePostcode() {
        Address address = Address.parse("Rued Langgaards Vej 7 2300");
        assertEquals("Rued Langgaards Vej", address.street);
        assertEquals("7", address.houseNumber);
        assertEquals("2300", address.postcode);
    }

    @Test
    public void testStreetHouseCity() {
        Address address = Address.parse("Rued Langgaards Vej 7 København S");
        assertEquals("Rued Langgaards Vej", address.street);
        assertEquals("7", address.houseNumber);
        assertEquals("København S", address.city);
    }

    @Test
    public void testStreetHousePostcodeCity() {
        Address address = Address.parse("Rued Langgaards Vej 7 2300 København S");
        assertEquals("Rued Langgaards Vej", address.street);
        assertEquals("7", address.houseNumber);
        assertEquals("2300", address.postcode);
        assertEquals("København S", address.city);
    }


    @Test
    public void testAlphabeticHouseNumber() {
        Address address = Address.parse("Rued Langgaards Vej 7D 2300 København S");
        assertEquals("Rued Langgaards Vej", address.street);
        assertEquals("7D", address.houseNumber);
        assertEquals("2300", address.postcode);
        assertEquals("København S", address.city);
    }
}
