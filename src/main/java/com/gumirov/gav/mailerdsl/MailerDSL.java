/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package com.gumirov.gav.mailerdsl;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.d0sl.domain.DomainFunction;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import org.d0sl.domain.DomainModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.stream.Collectors;

@DomainModel(name = "MailerDSL")
public class MailerDSL {
    private static final int JITTER = 700; // jitter in milliseconds

    private String secret;
    private String uri;

    public MailerDSL(){}

    @DomainFunction(name = "init")
    public boolean init(String host, String secret){
        uri = host;
        if (uri.endsWith("/")) uri = uri.substring(0,uri.length()-2); // cut '/' from the end
        this.secret = secret;
        return true;
    }

    @DomainFunction(name = "send")
    public String send(String to, String subject, String body) {
        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            URL conn = new URL(uri+"/send?secret="+secret+"&to="+URLEncoder.encode(to,"utf-8")+"&subject="+ URLEncoder.encode(subject,"utf-8")+"&body="+new String(Base64.getEncoder().encode(body.getBytes())));
            javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(
                    new javax.net.ssl.HostnameVerifier(){

                        public boolean verify(String hostname,
                                              javax.net.ssl.SSLSession sslSession) {
                            return hostname.equals(hostname.replace("https://",""));
                        }
                    });
            URLConnection yc = conn.openConnection();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            yc.getInputStream()));
//            char []resp = new char[2048];
            String json = in.lines().collect(Collectors.joining());
//            in.close();
//            String json = new String(resp);
            System.out.println("Json: "+json);
            try {
                JSONParser parser = new JSONParser();
                JSONObject resultObject = (JSONObject) parser.parse(json);
                System.out.println("Object: "+resultObject);
                if ((long)resultObject.get("code") == 1L){
                    System.out.println("Error: "+resultObject.get("error"));
                    return "error";
                }
                return (String) resultObject.get("uid");
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return "error";
    }

    @DomainFunction(name = "isOK")
    public boolean isOK(String uid) throws InterruptedException {
        System.out.println("Checking is ok on uid "+uid);
        boolean done = false;
        while (!done) {
            try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
                URL conn = new URL(uri + "/status?secret=" + secret + "&uid=" + uid);
                javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(
                        new javax.net.ssl.HostnameVerifier() {
                            public boolean verify(String hostname,
                                                  javax.net.ssl.SSLSession sslSession) {
                                return hostname.equals(hostname.replace("https://", ""));
                            }
                        });
                URLConnection yc = conn.openConnection();
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(
                                yc.getInputStream()));
//            char []resp = new char[2048];
                String json = in.lines().collect(Collectors.joining());
//            in.close();
//            String json = new String(resp);
                System.out.println("Json: " + json);
                try {
                    JSONParser parser = new JSONParser();
                    JSONObject resultObject = (JSONObject) parser.parse(json);
                    System.out.println("Object: " + resultObject);
                    if ((resultObject.get("error")).equals("Mail not yet sent or no such task")) {
                        Thread.sleep(JITTER);
                        continue;
                    }
                    done = true;
                    if ((long) resultObject.get("code") == 1L) {
                        System.out.println("Error: " + resultObject.get("error"));
                        return false;
                    }
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                    done = true;
                }

            } catch (IOException ex) {
                ex.printStackTrace();
                done = true;
            }
        }
        return false;
    }


    public boolean someLibraryMethod() {
        return true;
    }
}
