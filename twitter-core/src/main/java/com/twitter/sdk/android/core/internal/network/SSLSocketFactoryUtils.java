package com.twitter.sdk.android.core.internal.network;

import android.annotation.SuppressLint;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * @author liangzhen
 * @date 2021/10/13 2:24 下午
 */
public class SSLSocketFactoryUtils {


    /**
     * https信任所有验证配置
     */
    public static SSLSocketFactory createTrustAllSSLSocketFactory() {
        SSLSocketFactory mSSLSocketFactory;
        synchronized (SSLSocketFactoryUtils.class) {
            SSLContext sslContext;
            try {
                sslContext = SSLContext.getInstance("TLS");
            } catch (NoSuchAlgorithmException e) {
                return null;
            }
            //获得服务器端证书
            TrustManager[] turstManager = getTurstAllManager();

            //初始化ssl证书库
            try {
                sslContext.init(null, turstManager, new SecureRandom());
            } catch (KeyManagementException e) {
                return null;
            }
            //获得sslSocketFactory
            mSSLSocketFactory = sslContext.getSocketFactory();
        }
        return mSSLSocketFactory;
    }


    /**
     * 获得信任所有服务器端证书库
     */
    public static TrustManager[] getTurstAllManager() {
        return new X509TrustManager[]{new MyX509TrustManager()};
    }


    @SuppressLint("CustomX509TrustManager")
    public static class MyX509TrustManager implements X509TrustManager {

        @SuppressLint("TrustAllX509TrustManager")
        public void checkClientTrusted(X509Certificate[] chain, String authType) {

        }

        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateNotYetValidException, CertificateExpiredException {
            chain[0].checkValidity();
        }

        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }


}
