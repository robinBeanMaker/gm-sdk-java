package com.trustasia.tagm.sign;

/**
 * Credentials
 *
 * @version 1.0
 * @Author robin.li
 * @Date 2025-04-09 17:03:13
 */
public class Credentials {
    private String clientCert;
    private String priKey;

    private Credentials() {
    }

    public String getClientCert() {
        return clientCert;
    }

    public void setClientCert(String clientCert) {
        this.clientCert = clientCert;
    }

    public String getPriKey() {
        return priKey;
    }

    public void setPriKey(String priKey) {
        this.priKey = priKey;
    }

    public static Credentials getCredentials(String accessKey, String secretKey) {
        Credentials credentials = new Credentials();
        credentials.clientCert = accessKey;
        credentials.priKey = secretKey;
        return credentials;
    }
}