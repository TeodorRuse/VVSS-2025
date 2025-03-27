package pizzashop.service;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import pizzashop.model.PaymentType;
import pizzashop.repository.MenuRepository;
import pizzashop.repository.PaymentRepository;
import pizzashop.service.PizzaService;

import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Tag("PizzaServiceTests")
class PizzaServiceTest {

    private PizzaService service;

    @BeforeEach
    void setUp() throws IOException {
        // Curățăm fișierul payments.txt pentru a porni mereu de la 0
        FileWriter fw = new FileWriter("data/payments.txt", false); // false = overwrite
        fw.write("");
        fw.close();

        // Cream instanța service-ului pe curat
        service = new PizzaService(new MenuRepository(), new PaymentRepository());
    }

    @DisplayName("ECP Valid Payment 1")
    @Test
    @Order(1)
    void testAddPayment_ECP_Valid() {
        service.addPayment(3, PaymentType.Cash, 50.0);
        assertEquals(50.0, service.getTotalAmount(PaymentType.Cash), 0.01);
    }

    @DisplayName("ECP Valid Payment 2")
    @Test
    @Order(5)
    void testAddPayment_ECP_Valid2() {
        service.addPayment(5, PaymentType.Card, 70.0);
        assertEquals(70.0, service.getTotalAmount(PaymentType.Card), 0.01);
    }


    @DisplayName("ECP Invalid Table Number")
    @Test
    @Order(2)
    void testAddPayment_ECP_InvalidTable() {
        assertThrows(IllegalArgumentException.class, () -> {
            service.addPayment(-1, PaymentType.Cash, 50.0);
        });
    }

    @DisplayName("ECP Invalid Amount")
    @Test
    @Order(3)
    void testAddPayment_ECP_InvalidAmount() {
        assertThrows(IllegalArgumentException.class, () -> {
            service.addPayment(2, PaymentType.Cash, -20.0);
        });
    }

    @DisplayName("BVA Test Table Values")
    @ParameterizedTest
    @CsvSource(value = {
            "1, 20.0", // Min valid
            "8, 30.0", // Max valid
            "0, 40.0", // Invalid below min
            "9, 40.0"  // Invalid above max
    })
    @Order(4)
    void testAddPayment_BVA_Tables(int table, double amount) {
        if (table >= 1 && table <= 8) {
            service.addPayment(table, PaymentType.Card, amount);
            assertTrue(service.getPayments().stream().anyMatch(p -> p.getAmount() == amount && p.getTableNumber() == table));
        } else {
            assertThrows(IllegalArgumentException.class, () -> {
                service.addPayment(table, PaymentType.Card, amount);
            });
        }
    }

    @DisplayName("BVA Test Amount Values")
    @ParameterizedTest
    @CsvSource({
            "2, 0.01",   // Min valid
            "2, 100.0",  // Max valid
            "2, 0.0",    // Invalid below min
            "2, -5.0"    // Invalid above max
    })
    @Order(6)
    void testAddPayment_BVA_Amounts(int table, double amount) {
        if (amount > 0) {
            service.addPayment(table, PaymentType.Cash, amount);
            assertTrue(service.getPayments().stream().anyMatch(p -> p.getAmount() == amount && p.getTableNumber() == table));
        } else {
            assertThrows(IllegalArgumentException.class, () -> {
                service.addPayment(table, PaymentType.Cash, amount);
            });
        }
    }

}
