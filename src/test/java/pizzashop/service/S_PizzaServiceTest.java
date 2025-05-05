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
import org.mockito.Mockito;
public class S_PizzaServiceTest {

    private PizzaService service;
    public PaymentRepository mockPayRepo;
    public MenuRepository mockMenuRepo;
    @BeforeEach
    public void setup() {

        mockPayRepo = Mockito.mock(PaymentRepository.class);
        mockMenuRepo = Mockito.mock(MenuRepository.class);
        service = new PizzaService(mockMenuRepo, mockPayRepo);
    }

    @Test
    public void testAddPaymentValid() {
        service.addPayment(1, PaymentType.Cash, 100.0);
        Mockito.verify(mockPayRepo, Mockito.times(1)).add(Mockito.any(Payment.class));
    }

    @Test
    public void testAddPaymentInvalidTableLow() {
        assertThrows(IllegalArgumentException.class, () -> service.addPayment(0, PaymentType.Card, 50.0));
    }

    @Test
    public void testAddPaymentInvalidTableHigh() {
        assertThrows(IllegalArgumentException.class, () -> service.addPayment(9, PaymentType.Cash, 50.0));
    }

    @Test
    public void testAddPaymentInvalidAmount() {
        assertThrows(IllegalArgumentException.class, () -> service.addPayment(3, PaymentType.Cash, -20.0));
    }

    @Test
    public void testGetTotalAmount() {
        List<Payment> payments = Arrays.asList(
                new Payment(1, PaymentType.Cash, 10.0),
                new Payment(2, PaymentType.Cash, 15.0),
                new Payment(3, PaymentType.Card, 20.0)
        );
        Mockito.when(mockPayRepo.getAll()).thenReturn(payments);
        double total = service.getTotalAmount(PaymentType.Cash);
        assertEquals(25.0, total);
    }
}
