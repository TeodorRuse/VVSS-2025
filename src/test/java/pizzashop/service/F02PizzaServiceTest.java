package pizzashop.service;

import org.junit.jupiter.api.*;
import pizzashop.model.Payment;
import pizzashop.model.PaymentType;
import pizzashop.repository.MenuRepository;
import pizzashop.repository.PaymentRepository;
import pizzashop.service.PizzaService;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Tag("F02PizzaServiceTests")
class F02PizzaServiceTest {

    private PizzaService service;

    @BeforeEach
    void setUp() throws IOException {
        // Resetăm fișierul payments.txt
        FileWriter fw = new FileWriter("data/payments.txt", false);
        fw.write("");
        fw.close();

        service = new PizzaService(new MenuRepository(), new PaymentRepository());
    }

    @DisplayName("SC: Acoperire completă a instrucțiunilor")
    @Test
    @Order(1)
    void testSC_StatementCoverageFullFlow() {
        service.addPayment(1, PaymentType.Cash, 10.0);  // se va adăuga la total
        service.addPayment(2, PaymentType.Card, 20.0);  // nu se va adăuga
        double total = service.getTotalAmount(PaymentType.Cash);
        assertEquals(10.0, total, 0.001);
    }

    @DisplayName("MCC: Lista e null")
    @Test
    @Order(2)
    void testMCC_ListIsNull() {
        PizzaService customService = new PizzaService(new MenuRepository(), new PaymentRepository()) {
            @Override
            public List<Payment> getPayments() {
                return null;
            }
        };
        double total = customService.getTotalAmount(PaymentType.Cash);
        assertEquals(0.0, total);
    }

    @DisplayName("MCC: Lista este goala")
    @Test
    @Order(3)
    void testMCC_ListIsEmpty() {
        double total = service.getTotalAmount(PaymentType.Cash);
        assertEquals(0.0, total);
    }

    @DisplayName("MCC: Lista este corecta")
    @Test
    @Order(4)
    void testMCC_ListHasElements() {
        service.addPayment(1, PaymentType.Cash, 20.0);
        assertEquals(20.0, service.getTotalAmount(PaymentType.Cash), 0.001);
    }

    @DisplayName("DC: plata cu tip diferit, nu se vede")
    @Test
    @Order(5)
    void testDC_PaymentTypeMismatch() {
        service.addPayment(1, PaymentType.Card, 100.0);
        assertEquals(0.0, service.getTotalAmount(PaymentType.Cash));
    }

    @DisplayName("DC: plata cu tip corect, se vede")
    @Test
    @Order(6)
    void testDC_PaymentTypeMatch() {
        service.addPayment(2, PaymentType.Cash, 42.5);
        assertEquals(42.5, service.getTotalAmount(PaymentType.Cash), 0.001);
    }

    @DisplayName("APC: doua plati, una potrivita, total partial")
    @Test
    @Order(7)
    void testAPC_PartialMatch() {
        service.addPayment(1, PaymentType.Cash, 30.0);
        service.addPayment(2, PaymentType.Card, 50.0);
        assertEquals(30.0, service.getTotalAmount(PaymentType.Cash), 0.001);
    }

    @DisplayName("LC: trei plati, doua potrivite, test bucla multiplu")
    @Test
    @Order(8)
    void testLC_LoopWithMultipleMatches() {
        service.addPayment(1, PaymentType.Cash, 10.0);
        service.addPayment(2, PaymentType.Cash, 20.0);
        service.addPayment(3, PaymentType.Card, 50.0);
        assertEquals(30.0, service.getTotalAmount(PaymentType.Cash), 0.001);
    }
}
