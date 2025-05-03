package pizzashop.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pizzashop.model.Payment;
import pizzashop.model.PaymentType;
import pizzashop.repository.MenuRepository;
import pizzashop.repository.PaymentRepository;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class S_PizzaServiceTest {

    private PizzaService service;
    private PaymentRepository mockPayRepo;

    @BeforeEach
    public void setup() {
        // Se folosesc mock-uri pentru a izola testarea clasei PizzaService
        MenuRepository mockMenuRepo = mock(MenuRepository.class);
        mockPayRepo = mock(PaymentRepository.class);
        service = new PizzaService(mockMenuRepo, mockPayRepo);
    }

    @Test
    public void testAddPaymentValid() {
        // Testeaza adaugarea unui Payment valid
        // Foloseste mock pentru PaymentRepository
        service.addPayment(1, PaymentType.Cash, 100.0);
        verify(mockPayRepo, times(1)).add(any(Payment.class));
    }

    @Test
    public void testAddPaymentInvalidTableLow() {
        // Testeaza cazul cand table number este prea mic (sub 1)
        // Se asteapta o exceptie
        assertThrows(IllegalArgumentException.class, () -> service.addPayment(0, PaymentType.Card, 50.0));
    }

    @Test
    public void testAddPaymentInvalidTableHigh() {
        // Testeaza cazul cand table number este prea mare (peste 8)
        // Se asteapta o exceptie
        assertThrows(IllegalArgumentException.class, () -> service.addPayment(9, PaymentType.Cash, 50.0));
    }

    @Test
    public void testAddPaymentInvalidAmount() {
        // Testeaza cazul cand amount-ul este negativ
        // Se asteapta o exceptie
        assertThrows(IllegalArgumentException.class, () -> service.addPayment(3, PaymentType.Cash, -20.0));
    }

    @Test
    public void testAddPaymentInvalidType() {
        // Testeaza cazul cand PaymentType nu este nici CASH, nici CARD
        // Se asteapta o exceptie
        assertThrows(IllegalArgumentException.class, () -> service.addPayment(3, PaymentType.Test, 20.0));
    }

    @Test
    public void testGetTotalAmount() {
        // Testeaza suma totala pentru un tip de plata (Cash)
        // Returneaza o lista mockuita si verifica calculul
        List<Payment> payments = Arrays.asList(
                new Payment(1, PaymentType.Cash, 10.0),
                new Payment(2, PaymentType.Cash, 15.0),
                new Payment(3, PaymentType.Card, 20.0)
        );
        when(mockPayRepo.getAll()).thenReturn(payments);
        double total = service.getTotalAmount(PaymentType.Cash);
        assertEquals(25.0, total);
    }
}
