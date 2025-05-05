package pizzashop.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pizzashop.model.Payment;
import pizzashop.model.PaymentType;
import pizzashop.repository.MenuRepository;
import pizzashop.repository.PaymentRepository;
import pizzashop.service.PizzaService;

import java.io.PrintWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Step3 – Teste de integrare completa intre PizzaService (S), PaymentRepository (R) si Payment (E),
 * folosind implementarile reale, fara mock-uri.
 */
public class Step3 {

    private PizzaService service;
    private PaymentRepository realRepo;

    @BeforeEach
    public void setup() {
        // Golim fisierul de plati inainte de fiecare test pentru a evita acumularea datelor
        try (PrintWriter writer = new PrintWriter("data/payments.txt")) {
            writer.print(""); // Sterge tot continutul fisierului
        } catch (IOException e) {
            fail("Nu am putut curata fisierul de plati: " + e.getMessage());
        }

        // Initializam serviciul cu implementari reale
        MenuRepository dummyMenu = new MenuRepository(); // MenuRepository nu e relevant aici
        realRepo = new PaymentRepository();               // Repository-ul real scrie/incarca din fisier
        service = new PizzaService(dummyMenu, realRepo);
    }

    @Test
    public void testAddPaymentAndStoreCorrectly() {
        // Adaugam o plata reala
        service.addPayment(4, PaymentType.Card, 40.0);

        // Verificam ca a fost salvata in repo si in fisier
        assertEquals(1, realRepo.getAll().size());
        assertEquals(40.0, realRepo.getAll().get(0).getAmount());
    }

    @Test
    public void testGetTotalAmountWithRealPayments() {
        // Adaugam mai multe plati reale in repository
        service.addPayment(1, PaymentType.Cash, 15.0);
        service.addPayment(2, PaymentType.Card, 10.0);
        service.addPayment(3, PaymentType.Cash, 5.0);

        // Verificam suma totala a platilor de tip Cash
        double totalCash = service.getTotalAmount(PaymentType.Cash);
        assertEquals(20.0, totalCash);
    }
}
