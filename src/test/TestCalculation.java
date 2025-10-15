package test;


import model.Calculation;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class TestCalculation {
    @Test
    public void calculationModelCRUD() {
        Calculation c = new Calculation();
        c.setName("Test");
        c.setInputData("{\"p\":1}");
        c.setResultData("{\"res\":2}");
        assertEquals("Test", c.getName());
        assertTrue(c.getInputData().contains("p"));
    }


    @Test
    public void passwordHashing() {
        String pass = "abc123";
        String hash = util.CryptoUtils.hashPassword(pass);
        assertNotNull(hash);
        assertTrue(util.CryptoUtils.verifyPassword(pass, hash));
    }
}