package pizzashop.service;

import org.junit.jupiter.api.Test;
import pizzashop.model.Payment;
import pizzashop.model.PaymentType;
import pizzashop.repository.PaymentRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class R_PaymentRepositoryTest {

    // Test 1: Verifica daca metoda add() adauga corect o plata in lista
    @Test
    public void testAddAndGetAll() {
        PaymentRepository repo = new PaymentRepository();
        Payment p = new Payment(1, PaymentType.Cash, 20.0);
        int initialSize = repo.getAll().size();
        repo.add(p);
        List<Payment> all = repo.getAll();
        assertEquals(initialSize + 1, all.size());
        assertTrue(all.contains(p));
    }

    // Test 2: Adauga mai multe plati si verifica dimensiunea listei
    @Test
    public void testMultipleAdds() {
        PaymentRepository repo = new PaymentRepository();
        int initialSize = repo.getAll().size();
        repo.add(new Payment(1, PaymentType.Cash, 10.0));
        repo.add(new Payment(2, PaymentType.Card, 15.0));
        assertEquals(initialSize + 2, repo.getAll().size());
    }

    // Test 3: Verifica daca lista returnata nu este null
    @Test
    public void testGetAllNotNull() {
        PaymentRepository repo = new PaymentRepository();
        assertNotNull(repo.getAll());
    }

    // Test 4: Verifica dacă se poate adauga o plata cu valoare 0
    @Test
    public void testAddZeroAmountPayment() {
        PaymentRepository repo = new PaymentRepository();
        Payment zeroPayment = new Payment(3, PaymentType.Card, 0.0);
        repo.add(zeroPayment);
        assertTrue(repo.getAll().contains(zeroPayment));
    }

    // Test 5: Verifica daca metoda add() adauga obiectul in ordine
    @Test
    public void testOrderOfPayments() {
        PaymentRepository repo = new PaymentRepository();
        Payment p1 = new Payment(1, PaymentType.Cash, 5.0);
        Payment p2 = new Payment(2, PaymentType.Card, 10.0);
        repo.add(p1);
        repo.add(p2);
        List<Payment> all = repo.getAll();
        assertEquals(p1, all.get(all.size() - 2));
        assertEquals(p2, all.get(all.size() - 1));
    }

    // Test 6: Verifica dacă plațile sunt scrise corect in fișier
    @Test
    public void testWriteAllDoesNotThrow() {
        PaymentRepository repo = new PaymentRepository();
        repo.add(new Payment(5, PaymentType.Cash, 99.99));
        // daca ajungem aici fara exceptii, testul este considerat trecut
        assertDoesNotThrow(() -> repo.writeAll());
    }
}
