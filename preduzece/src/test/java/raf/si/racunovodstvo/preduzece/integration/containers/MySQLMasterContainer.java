package raf.si.racunovodstvo.preduzece.integration.containers;

import org.testcontainers.containers.Network;

public class MySQLMasterContainer extends MySQLContainer {

    public MySQLMasterContainer(Network network, int port) {
        super(network, port, "mysql_db");;

        addEnv("MYSQL_REPLICATION_MODE", "master");
        addEnv("MYSQL_ROOT_PASSWORD", "test");
    }
}
