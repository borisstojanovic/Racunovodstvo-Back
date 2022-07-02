package raf.si.racunovodstvo.knjizenje.integration.network;

import org.testcontainers.containers.Network;

public final class NetworkHolder {

    private NetworkHolder() {
    }

    private static Network network;

    public static Network network() {
        if (network == null) {
            network = Network.newNetwork();
        }
        return network;
    }
}
