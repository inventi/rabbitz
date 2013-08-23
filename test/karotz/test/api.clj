(ns karotz.test.api
  (:use [karotz.api])
  (:use [clojure.test])
  (:require [clojure.xml :as xml])
  (:import java.io.IOException))

(def ok-response {:tag :voosmsg, :attrs nil, :content
                    [{:tag :id, :attrs nil, :content ["28665c15-bd73-4bbd-b2b8-02703cba8ee9"]}
                     {:tag :correlationid, :attrs nil, :content ["73b1200d-2e23-46be-961e-a88a5590f020"]}
                     {:tag :interactiveId, :attrs nil, :content ["INTERACTIVE-ID"]}
                     {:tag :response, :attrs nil, :content
                      [{:tag :code, :attrs nil, :content ["OK"]}]}]})

(def error-response {:tag :voosmsg, :attrs nil, :content
                     [{:tag :id, :attrs nil, :content ["28665c15-bd73-4bbd-b2b8-02703cba8ee9"]}
                      {:tag :correlationid, :attrs nil, :content ["73b1200d-2e23-46be-961e-a88a5590f020"]}
                      {:tag :interactiveId, :attrs nil, :content ["NO-INTERACTIVE-ID"]}
                      {:tag :response, :attrs nil, :content
                       [{:tag :code, :attrs nil, :content ["NOT_CONNECTED"]}]}]})



(deftest test-error
         (testing "Should test error"
                  (is (error? "ERROR some other text"))
                  (is (error? "NOT_CONNECTED some other text"))
                  (is (error? "NOT_CONNECTED"))
                  (is (error? "ERROR"))
                  (is (not (error? nil)))))

(deftest test-karotz-request
  (with-redefs [karotz-api ""]
      (testing "Parses correct response"
        (with-redefs [xml/parse (constantly ok-response)]
          (is (= "INTERACTIVE-ID"
                 (karotz-request "url")))))

  (testing "Parses erroneus response"
    (with-redefs [xml/parse (constantly error-response)]
      (is (= "NOT_CONNECTED"
             (karotz-request "")))))

  (testing "Rethrows io exception if it occurs"
	  (is (= IOException
           (with-redefs [xml/parse (fn [a] (throw (IOException. "problem")))]
             (try
               (karotz-request "no-such-file")
               (catch IOException e (.getClass e)))))))))


(deftest test-sign-in
   (testing "Signin should return valid id"
            (is (= "789"
                   (with-redefs [valid-id? (constantly false)
                                 karotz-request (constantly "789")
                                 login-url (constantly "")]
                     (sign-in ["install1" "123"] {}))))))
