package conta.servicos.repositorio;

import conta.sistema.dominio.modelo.Conta;
import conta.sistema.dominio.modelo.NegocioException;
import conta.sistema.porta.ContaRepositorio;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;

import static java.util.Objects.isNull;

// Responsável por implementar a porta de saída (driven) de serviços de banco de dados
// usando spring jdbc
@Named
public class ContaRepositorioImp implements ContaRepositorio {
    private static final String ERRO = "Erro inesperado de acesso ao banco. Entre em contato com o administrador" ;

    private JdbcTemplate jdbc; // permite o acesso ao banco de dados

    @Inject
    public ContaRepositorioImp(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public Conta get(Integer numero) {
        if (isNull(numero)) {
            return null;
        }
        var sql = "select * from conta where numero = ?";
        var pm = new Object[]{numero};

        // RowMapper(spring) - faz a conversao do resultset para objeto - Conta
        RowMapper<Conta> orm = (rs, nm) ->
                new Conta(rs.getInt(1), rs.getBigDecimal(2), rs.getString(3));

        try {

            // acessa banco de dados
            var lista = jdbc.query(sql, pm, orm);

            // se não acho conta no bd
            if (lista.isEmpty()) {
                return null;
            }
            return lista.get(0); // achou - retorna o objeto encontrado

        } catch (Exception e) {
            e.printStackTrace();
            throw new NegocioException(ERRO);
        }
    }
    @Transactional
    @Override
    public void alterar(Conta conta) {
        if (isNull(conta)) {
            throw new NegocioException("Conta é obrigatório.");
        }
        var sql = "update conta set saldo=?, correntista=? where numero=?";
        var pm = new Object[]{conta.getSaldo(), conta.getCorrentista(),
                conta.getNumero()};
        try {
            jdbc.update(sql, pm);
        } catch (Exception e) {
            e.printStackTrace();
            throw new NegocioException(ERRO);
        }
    }
}
