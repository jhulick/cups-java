package org.cups4j.operations;

import org.cups4j.ippclient.IppResponse;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.net.URL;
import java.nio.ByteBuffer;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.cups4j.ssl.SSLScheme;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpPoster {

    private static final Logger logger = LoggerFactory.getLogger(HttpPoster.class);

    private static int SOCKET_TIMEOUT = 10000;
    private static int CONNECTION_TIMEOUT = 5000;

    private final static String IPP_MIME_TYPE = "application/ipp";

    static OperationResult sendRequest(URL url, ByteBuffer ippBuf, InputStream documentStream, final AuthInfo auth) throws IOException {

        final OperationResult opResult = new OperationResult();

        if (ippBuf == null) {
            return null;
        }

        if (url == null) {
            return null;
        }

        DefaultHttpClient client = new DefaultHttpClient();

        // will not work with older versions of CUPS!
        client.getParams().setParameter("http.protocol.version", HttpVersion.HTTP_1_1);
        client.getParams().setParameter("http.socket.timeout", SOCKET_TIMEOUT);
        client.getParams().setParameter("http.connection.timeout", CONNECTION_TIMEOUT);
        client.getParams().setParameter("http.protocol.content-charset", "UTF-8");
        client.getParams().setParameter("http.method.response.buffer.warnlimit", new Integer(8092));
        // probabaly not working with older CUPS versions
        client.getParams().setParameter("http.protocol.expect-continue", Boolean.valueOf(true));

        HttpPost httpPost;

        try {
            httpPost = new HttpPost(url.toURI());
        } catch (Exception e) {
            logger.error(e.toString());
            return null;
        }

        httpPost.getParams().setParameter("http.socket.timeout", SOCKET_TIMEOUT);

        byte[] bytes = new byte[ippBuf.limit()];
        ippBuf.get(bytes);

        ByteArrayInputStream headerStream = new ByteArrayInputStream(bytes);
        // If we need to send a document, concatenate InputStreams
        InputStream inputStream = headerStream;
        if (documentStream != null) {
            inputStream = new SequenceInputStream(headerStream, documentStream);
        }

        // set length to -1 to advice the entity to read until EOF
        InputStreamEntity requestEntity = new InputStreamEntity(inputStream, -1);

        requestEntity.setContentType(IPP_MIME_TYPE);
        httpPost.setEntity(requestEntity);

        if (auth.reason == AuthInfo.AUTH_REQUESTED) {
            AuthHeader.makeAuthHeader(httpPost, auth);
            if (auth.reason == AuthInfo.AUTH_OK) {
                httpPost.addHeader(auth.getAuthHeader());
                //httpPost.addHeader("Authorization", "Basic am9uOmpvbmJveQ==");
            }
        }

        ResponseHandler<byte[]> handler = new ResponseHandler<byte[]>() {
            @Override
            public byte[] handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
                if (response.getStatusLine().getStatusCode() == 401) {
                    auth.setHttpHeader(response.getFirstHeader("WWW-Authenticate"));
                } else {
                    auth.reason = AuthInfo.AUTH_OK;
                }
                HttpEntity entity = response.getEntity();
                opResult.setHttResult(response.getStatusLine().toString());
                if (entity != null) {
                    return EntityUtils.toByteArray(entity);
                } else {
                    return null;
                }
            }
        };

        if (url.getProtocol().equals("https")) {
            Scheme scheme = SSLScheme.getScheme();
            if (scheme == null)
                return null;
            client.getConnectionManager().getSchemeRegistry().register(scheme);
        }

        byte[] result = client.execute(httpPost, handler);
        //String test = new String(result);
        IppResponse ippResponse = new IppResponse();

        opResult.setIppResult(ippResponse.getResponse(ByteBuffer.wrap(result)));
        opResult.setAuthInfo(auth);
        client.getConnectionManager().shutdown();
        return opResult;
    }

}
