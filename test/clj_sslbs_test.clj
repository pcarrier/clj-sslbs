(ns clj-sslbs-test
  (:require [clojure.test :refer :all]
            [clj-sslbs :refer :all]))

(defn connect-to-linuxfr []
  (let [ url (java.net.URL. "https://linuxfr.org/") ]
    (with-open [ stream (.openStream url) ]
      (slurp stream))))

; Spot the troll.
(deftest linuxfr-still-broken
  (testing "linuxfr.org still uses a stupid cert
            and we're still broken"
    (is (thrown? javax.net.ssl.SSLHandshakeException
      (connect-to-linuxfr)))))

(deftest linuxfr-repaired
  (testing "linuxfr.org uses a broken cert"
   (is (connect-to-linuxfr))))

(deftest can-create-factory
  (testing "Stupid SSLSocketFactories can be created"
    (is (stupid-sslsocketfactory))))

(deftest can-use-a-stupid-tm
  (testing "We can ask for a stupid TrustManager"
    (is (nil? (use-a-stupid-tm)))))

(deftest can-use-a-stupid-hnv
  (testing "We can ask for a stupid HostnameVerifier"
    (is (nil? (use-a-stupid-hnv)))))

(deftest can-make-https-stupid
  (testing "We can make HTTPS stupid"
    (is (nil? (make-https-stupid)))))

(defn test-ns-hook []
  (can-create-factory)
  (linuxfr-still-broken)
  (can-use-a-stupid-tm)
  (can-use-a-stupid-hnv)
  (can-make-https-stupid)
  (linuxfr-repaired))