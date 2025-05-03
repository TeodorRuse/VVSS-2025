package pizzashop.service;

import org.junit.jupiter.api.Test;
import pizzashop.model.Payment;
import pizzashop.model.PaymentType;

import static org.junit.jupiter.api.Assertions.*;

public class E_PaymentTest {

    // Test #1: Constructorul seteaza corect toate campurile (unit test clasic)
    @Test
    public void testConstructorInitializesFieldsCorrectly() {
        // Arrange & Act
        Payment payment = new Payment(1, PaymentType.Cash, 10.5);

        // Assert
        assertEquals(1, payment.getTableNumber());
        assertEquals(PaymentType.Cash, payment.getType());
        assertEquals(10.5, payment.getAmount());
    }

    // Test #2: Setter-ul pentru tableNumber functioneaza corect (unit test)
    @Test
    public void testSetTableNumber() {
        Payment payment = new Payment(0, PaymentType.Cash, 0); // default
        payment.setTableNumber(5); // schimbăm valoarea

        assertEquals(5, payment.getTableNumber()); // verificăm noua valoare
    }

    // Test #3: Setter-ul pentru tipul de plata (PaymentType) functioneaza corect (unit test)
    @Test
    public void testSetPaymentType() {
        Payment payment = new Payment(0, PaymentType.Cash, 0);
        payment.setType(PaymentType.Card); // schimbăm tipul

        assertEquals(PaymentType.Card, payment.getType());
    }

    // Test #4: Setter-ul pentru amount seteaza corect suma (unit test)
    @Test
    public void testSetAmount() {
        Payment payment = new Payment(0, PaymentType.Cash, 0);
        payment.setAmount(99.99); // modificăm suma

        assertEquals(99.99, payment.getAmount());
    }

    // Test #5: Testam comportamentul pentru o suma negativa (edge case - dacă nu exista validare, ar trebui sa o accepte)
    @Test
    public void testNegativeAmount() {
        Payment payment = new Payment(1, PaymentType.Cash, -10.0);

        // Nu avem validare în constructor, deci ar trebui să accepte -10.0
        assertEquals(-10.0, payment.getAmount(), "Suma negativă este acceptată în lipsa unei validări explicite.");
    }

    // Test #6: Test complet – toate campurile se pot schimba si raman consistente (test de consistenta)
    @Test
    public void testChangeAllFields() {
        Payment payment = new Payment(1, PaymentType.Cash, 10.0);

        // Schimbam toate atributele
        payment.setTableNumber(3);
        payment.setType(PaymentType.Test);
        payment.setAmount(45.0);

        // Le verificam impreuna intr-un assertAll
        assertAll(
                () -> assertEquals(3, payment.getTableNumber()),
                () -> assertEquals(PaymentType.Test, payment.getType()),
                () -> assertEquals(45.0, payment.getAmount())
        );
    }
}
