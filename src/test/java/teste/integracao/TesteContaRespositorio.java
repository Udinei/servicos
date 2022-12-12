package teste.integracao;

import conta.sistema.dominio.modelo.NegocioException;
import conta.sistema.porta.ContaRepositorio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import javax.inject.Inject;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe Mock de teste que simula o que o frontEnd pode fazer no sistema (requisitos funcionais do sistema)
 * Executa testes com base nas informações do bd mock backservices
 * */
@DisplayName("Serviço de banco de dados - Conta")
@ContextConfiguration(classes = Config.class) // carrega os classes dos pacotes na configuraçõe para testar
@ExtendWith(SpringExtension.class) // integra o junit com o spring
public class TesteContaRespositorio {

    @Inject
    ContaRepositorio rep;

    // negativos get
    @Test
    @DisplayName("pesquisa conta com número nulo")
    void teste1() {
        try {
            var conta = rep.get(null);
            assertTrue(conta == null, "Conta deve ser nula");
        } catch (NegocioException e) {
            fail("Deve carregar uma conta nula.");
        }
    }

    @Test
    @DisplayName("pesquisa conta com número inexistente")
    void teste2() {
        try {
            var conta = rep.get(8547);
            assertTrue(conta == null, "Conta deve ser nula");
        } catch (NegocioException e) {
            fail("Deve carregar uma conta nula.");
        }
    }

    // positivo get
    @Test
    @DisplayName("pesquisa conta com número existente")
    void teste3() {
        try {
            var conta = rep.get(50);
            assertTrue(conta != null, "Conta deve estar preenchida");
            System.out.println(conta);
        } catch (NegocioException e) {
            fail("Deve carregar uma conta.");
        }
    }

    // negativos alterar
    @Test
    @DisplayName("alterar conta como nulo")
    void teste4() {
        try {
            rep.alterar(null);
            fail("Não deve alterar conta nula");
        } catch (NegocioException e) {
            assertEquals(e.getMessage(), "Conta é obrigatório.");
            System.out.println(e.getMessage());
        }
    }

    // positivo alterar
    @Test
    @DisplayName("alterar conta com sucesso")
    void teste5() {
        try {
            var c1 = rep.get(50);
            c1.setSaldo(new BigDecimal("1.00"));
            c1.setCorrentista("Alterado");
            rep.alterar(c1);

            var c2 = rep.get(50);
            assertEquals(c1.getSaldo(), c2.getSaldo(), "Deve bater o saldo");
            assertEquals(c1.getCorrentista(), c2.getCorrentista(), "Deve bater o correntista");
        } catch (NegocioException e) {
            fail("Deve alterar conta ");
        }
    }
}

