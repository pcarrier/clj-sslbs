(ns clj-sslbs
  (:import (java.io InputStreamReader Reader)
           (java.net URL
                     URLConnection)
           (javax.net.ssl HostnameVerifier HttpsURLConnection
                          SSLContext SSLSession TrustManager
                          X509TrustManager)
           (java.security cert.X509Certificate SecureRandom)))

(defn stupid-sslsocketfactory
  "A stupid SSLSocketFactory"
  []
  (let [ tm (into-array TrustManager
                        [(proxy [X509TrustManager]
                          []
                          (getAcceptedIssuers [] nil)
                          (checkClientTrusted [certs auth-type])
                          (checkServerTrusted [certs auth-type]))])
         sslctx (SSLContext/getInstance "SSL")
       ]
    (.init sslctx nil tm (SecureRandom.))
    (.getSocketFactory sslctx)))

(defn use-a-stupid-tm
  "HTTPS will use a stupid TrustManager"
  []
  (HttpsURLConnection/setDefaultSSLSocketFactory (stupid-sslsocketfactory)))

(defn use-a-stupid-hnv
  "HTTPS will use a stupid HostnameVerifier"
  []
  (let [ hnv (proxy [HostnameVerifier]
               []
               (verify [urlHostName session] true)) ]
    (HttpsURLConnection/setDefaultHostnameVerifier hnv)))

(defn make-https-stupid
  "Switches to stupid TrustMager and HostnameVerifier for HTTPS"
  []
  (use-a-stupid-hnv)
  (use-a-stupid-tm))
