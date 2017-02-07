package org.cups4j.ssl;

//This class taken from http://stackoverflow.com/questions/2642777/trusting-all-certificates-using-httpclient-over-https

import org.apache.http.conn.ssl.SSLSocketFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import java.io.IOException;
import java.net.Socket;
import java.security.*;
import java.security.cert.X509Certificate;

/**
 * Allows you to trust certificates from additional KeyStores in addition to
 * the default KeyStore
 */
public class AdditionalKeyStoresSSLSocketFactory extends SSLSocketFactory {

    private AdditionalKeyStoresTrustManager trustManager;

    protected SSLContext sslContext = SSLContext.getInstance("TLS");

    public AdditionalKeyStoresSSLSocketFactory(KeyStore keyStore) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
        super(null, null, null);
        trustManager = new AdditionalKeyStoresTrustManager(keyStore);
        sslContext.init(null, new TrustManager[]{trustManager}, null);
    }

    @Override
    public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException {
        return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
    }

    @Override
    public Socket createSocket() throws IOException {
        return sslContext.getSocketFactory().createSocket();
    }

    public X509Certificate[] getCertChain() {
        if (trustManager == null) {
            return null;
        }
        return trustManager.getCertChain();
    }
}
