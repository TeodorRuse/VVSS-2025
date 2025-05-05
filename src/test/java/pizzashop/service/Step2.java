package pizzashop.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import pizzashop.model.Payment;
import pizzashop.model.PaymentType;
import pizzashop.repository.MenuRepository;
import pizzashop.repository.PaymentRepository;
import pizzashop.service.PizzaService;

import java.util.Arrays;
import java.util.List;

import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Step2 – Teste de integrare intre PizzaService (S) si PaymentRepository (R),
 * folosind obiecte mock pentru Payment (E).
 */
public class Step2 {

    private PizzaService service;
    private PaymentRepository mockRepo;
    private MenuRepository mockMenu;
    @BeforeEach
    public void setup() {
        // Cream mock pentru MenuRepository si PaymentRepository
        mockMenu = Mockito.mock(MenuRepository.class);
        mockRepo = Mockito.mock(PaymentRepository.class);

        // Injectam mock-urile in instanta testata (PizzaService)
        service = new PizzaService(mockMenu, mockRepo);
    }

    @Test
    public void testAddPaymentValid() {
        // Simulam comportamentul metodei add() din repository: nu face nimic
        Mockito.doNothing().when(mockRepo).add(Mockito.any(Payment.class));

        // Apelam metoda testata
        service.addPayment(2, PaymentType.Cash, 30.0);

        // Verificam daca metoda add a fost apelata exact o data cu orice Payment
        Mockito.verify(mockRepo, Mockito.times(1)).add(Mockito.any(Payment.class));
    }

    @Test
    public void testGetTotalAmountFiltersByType() {
        // Simulam lista de plati returnata de repo (getAll)
        List<Payment> payments = Arrays.asList(
                new Payment(1, PaymentType.Cash, 20.0),
                new Payment(2, PaymentType.Card, 10.0),
                new Payment(3, PaymentType.Cash, 5.0)
        );
        Mockito.when(mockRepo.getAll()).thenReturn(payments);

        // Apelam metoda care calculeaza totalul pentru plati Cash
        double total = service.getTotalAmount(PaymentType.Cash);

        // Verificam rezultatul calculului
        assertEquals(25.0, total);
    }
}
