module conta.servicos {
    // usar conta sistema core
    requires conta.sistema; // linkando com outro modulo java core

    // usar spring
    requires javax.inject;
    requires spring.tx;
    requires spring.core;
    requires spring.beans;
    requires spring.context;
    requires java.sql;
    requires spring.jdbc;

    // repositorio
    opens conta.servicos.repositorio;

}