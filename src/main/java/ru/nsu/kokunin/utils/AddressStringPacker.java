package ru.nsu.kokunin.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

public class AddressStringPacker {
    private final static int IP_ADDRESS_OFFSET = 1;
    private final static Logger log = LoggerFactory.getLogger(AddressStringPacker.class);

    public static String packAddress(InetSocketAddress addressToPack) {
        if (addressToPack.getAddress() == null) {
            log.warn("Trying to pack zero address: [{}]", addressToPack);
            return null;
        }

        String ipAddress = addressToPack.getAddress().getHostAddress();
        int port = addressToPack.getPort();

        return "[" + ipAddress + "]:" + port;
    }

    public static InetSocketAddress unpackAddress(String addressToUnpack) {
        int lastByteOfIpAddress = addressToUnpack.indexOf(']');
        String ipAddress = addressToUnpack.substring(IP_ADDRESS_OFFSET, lastByteOfIpAddress);
        int port = Integer.parseInt(addressToUnpack.substring(lastByteOfIpAddress + 1));

        InetSocketAddress unpackedAddress = null;
        try {
            unpackedAddress = new InetSocketAddress(ipAddress, port);
        } catch (SecurityException exc) {
            log.error("Security error during parsing address: [{}]", addressToUnpack, exc);
        } catch (IllegalArgumentException exc) {
            log.error("Trying to parse incorrect address: [{}]", addressToUnpack, exc);
        }

        return unpackedAddress;
    }
}
