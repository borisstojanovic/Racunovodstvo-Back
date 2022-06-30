package raf.si.racunovodstvo.preduzece.integration.extensions;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.wait.strategy.HostPortWaitStrategy;
import raf.si.racunovodstvo.preduzece.integration.containers.MySQLMasterContainer;
import raf.si.racunovodstvo.preduzece.integration.containers.MySQLSlaveContainer;
import raf.si.racunovodstvo.preduzece.integration.network.NetworkHolder;

public class MySQLExtension implements BeforeAllCallback, AfterAllCallback {

    private final MySQLMasterContainer mySQLMasterContainer = new MySQLMasterContainer(NetworkHolder.network(), 3306);
    private final MySQLSlaveContainer mySQLSlaveContainer = new MySQLSlaveContainer(NetworkHolder.network(), 3306, "mysql_slave");
    private final MySQLSlaveContainer mySQLSlaveContainer1 = new MySQLSlaveContainer(NetworkHolder.network(), 3306, "mysql_slave_1");

    @Override
    public void afterAll(ExtensionContext extensionContext) throws Exception {
        mySQLMasterContainer.stop();
        mySQLSlaveContainer.stop();
        mySQLSlaveContainer1.stop();
    }

    @Override
    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        mySQLMasterContainer.start();
        mySQLSlaveContainer.start();
        mySQLSlaveContainer1.start();
    }
}

