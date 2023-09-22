import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;

public class ApiClient {

    public static void main(String[] args) throws Exception {
        String apiUrl = "https://DT Server Name/e/<server guid>/api/v2/problems?pageSize=500&from=now-1w%2Fd&problemSelector=status%28%22closed%22%29"; // Replace with the actual API URL
        String authToken = "dt0c01.<authToken>"; // Replace with your actual authorization token
        /**
        * Fix for Exception in thread "main" javax.net.ssl.SSLHandshakeException:
        * sun.security.validator.ValidatorException: PKIX path building failed:
        * sun.security.provider.certpath.SunCertPathBuilderException: unable to find
        * valid certification path to requested target
        Start of Fix */
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() { return null; }
            public void checkClientTrusted(X509Certificate[] certs, String authType) { }
            public void checkServerTrusted(X509Certificate[] certs, String authType) { }

        } };

        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        // Create all-trusting host name verifier
        HostnameVerifier allHostsValid = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) { return true; }
        };
        // Install the all-trusting host verifier
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        /* End of the fix*/
        URL url = new URL(apiUrl);
        URLConnection connection = url.openConnection();
        connection.setRequestProperty("Authorization", "Api-Token " + authToken);
        
        // Read the response data
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        
        while ((inputLine = reader.readLine()) != null) {
            response.append(inputLine);
        }
        reader.close();
        
        // Parse the JSON response
        String jsonResponse = response.toString();
        
        System.out.println(jsonResponse);
        
    }
}
