package br.edu.ifpr2.ifprstore.connection;

import connection.ConnectionFactory;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

public class ConnectionFactoryTest {
    @Test
    public void deveRealizarConexaoSemExcecao() {

        Connection connection = ConnectionFactory.getConnection();

    }
}
