package org.cups4j.operations;

import org.apache.http.HttpRequest;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.auth.DigestScheme;

public class AuthHeader {

    static void makeAuthHeader(HttpRequest request, AuthInfo auth) {

        if (auth.reason == AuthInfo.AUTH_NOT_SUPPORTED) {
            return;
        }

        if (auth.username.equals("") || (auth.password.equals(""))) {
            auth.reason = AuthInfo.AUTH_REQUIRED;
            return;
        }

        UsernamePasswordCredentials creds = new UsernamePasswordCredentials(auth.username, auth.password);
        if (auth.getType().equals("Basic")) {
            BasicScheme basicScheme = new BasicScheme();
            try {
                auth.setAuthHeader(basicScheme.authenticate(creds, request));
            } catch (Exception e) {
                System.err.println(e.toString());
                auth.reason = AuthInfo.AUTH_BAD;
            }
        } else if (auth.getType().equals("Digest")) {
            try {
                DigestScheme digestScheme = new DigestScheme();
                digestScheme.processChallenge(auth.getHttpHeader());
                auth.setAuthHeader(digestScheme.authenticate(creds, request));
                String test0 = auth.getHttpHeader().getValue();
                String test1 = auth.getAuthHeader().getValue();
                System.out.println();
            } catch (Exception e) {
                System.err.println(e.toString());
                auth.reason = AuthInfo.AUTH_BAD;
            }
        } else {
            auth.reason = AuthInfo.AUTH_NOT_SUPPORTED;
        }
    }
}
