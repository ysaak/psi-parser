package com.phpsysinfo.parser.utils;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class HttpUtils {

  private static final TrustManager[] TRUST_ALL_CERTS = new TrustManager[] { new X509TrustManager() {
      @Override
      public java.security.cert.X509Certificate[] getAcceptedIssuers() {
        return null;
      }

      @Override
      public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) { /**/}

      @Override
      public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) { /**/}
    }
  };

  private static boolean sslTrustInstalled = false;

  public static void installSSLTrust() {
    if (!sslTrustInstalled) {
      //Install the all-trusting trust manager
      try {
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, TRUST_ALL_CERTS, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
      }
      catch (Exception e) {
        e.printStackTrace();
      }
      sslTrustInstalled = true;
    }
  }

  public static Authenticator getBasicAuthenticator(String user, String pass) {
    return new BasicAuthenticator(user, pass);
  }

  private static class BasicAuthenticator extends Authenticator {
    private String username, password;

    public BasicAuthenticator(String user, String pass) {
      username = user;
      password = pass;
    }

    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
      System.out.println("Requesting Host  : " + getRequestingHost());
      System.out.println("Requesting Port  : " + getRequestingPort());
      System.out.println("Requesting Prompt : " + getRequestingPrompt());
      System.out.println("Requesting Protocol: " + getRequestingProtocol());
      System.out.println("Requesting Scheme : " + getRequestingScheme());
      System.out.println("Requesting Site  : " + getRequestingSite());
      return new PasswordAuthentication(username, password.toCharArray());
    }
  }
}
