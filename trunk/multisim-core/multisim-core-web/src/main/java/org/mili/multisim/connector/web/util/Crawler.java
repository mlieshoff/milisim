package org.mili.multisim.connector.web.util;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.mili.multisim.util.Log;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author
 */
public class Crawler {

    private int timeout = 500;

    public String post(String url, Map<String, String> params) throws IOException {
        Log.debug(this, "post", "url=%s, params=%s", url, params.entrySet());

        CookieStore cookieStore = new BasicCookieStore();

        CloseableHttpClient closeableHttpClient = HttpClientBuilder.create()
                .setDefaultCookieStore(cookieStore)
                .build();

        try {
            HttpPost post = new HttpPost(url);

            HttpContext localContext = new BasicHttpContext();
            localContext.setAttribute(HttpClientContext.COOKIE_STORE, cookieStore);

            List<NameValuePair> nameValuePairs = new ArrayList<>();

            for (Map.Entry<String, String> entry : params.entrySet()) {
                nameValuePairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }

            post.setEntity(new UrlEncodedFormEntity(nameValuePairs, Consts.UTF_8));

            HttpResponse response = closeableHttpClient.execute(post, localContext);

            String text = EntityUtils.toString(response.getEntity());

            return text;

        } finally {
            closeableHttpClient.close();
        }
    }

    public HttpResponse get(String uri, Map<String, String> parameters, boolean throwException) throws IOException, URISyntaxException {

        final HttpClient httpClient = new DefaultHttpClient();
        final HttpParams httpParams = httpClient.getParams();
        final URIBuilder uriBuilder = new URIBuilder(uri);

        httpParams.setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY);
        httpParams.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, timeout);

        if (parameters != null && parameters.size() > 0) {
            for (Map.Entry<String, String> entry : parameters.entrySet()) {
                uriBuilder.addParameter(entry.getKey(), entry.getValue());
            }
        }

        HttpGet httpGet = new HttpGet(uriBuilder.build());

        Log.debug(this, "get", "Getting %s", uriBuilder.toString());

        final HttpResponse response = httpClient.execute(httpGet);

        if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK && throwException) {

            HttpEntity entity = response.getEntity();

            String responseBody;
            if (entity != null) {
                responseBody = EntityUtils.toString(entity);
            } else {
                responseBody = "<no response body>";
            }

            httpGet.abort();
            // always attach the response body to simplify debugging

            throw new IllegalStateException(responseBody);

        }

        return response;
    }



}