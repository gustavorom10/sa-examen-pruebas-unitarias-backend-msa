package mx.gustavo.examen.pruebas.backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BackendApplicationTest {

    @Test
    void mainTest() {
        BackendApplication.main(new String[]{});
    }
}