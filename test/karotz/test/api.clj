(ns karotz.test.api
  (:use [karotz.api])
  (:use [clojure.test])
  (:require [clojure.xml :as xml])
  (:import java.io.IOException))

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
             (is (= "INTERACTIVE-ID"
                    (karotz-request "test-resources/ok-response.xml"))))

  (testing "Parses erroneus response"
	  (is (= "NOT_CONNECTED"
	         (karotz-request "test-resources/error-response.xml"))))

  (testing "Rethrows io exception if it occurs"
	  (is (= IOException
           (with-redefs [xml/parse (fn [a] (throw (IOException. "problem")))]
             (try
               (karotz-request "no-such-file")
               (catch IOException e (.getClass e)))))))

  (testing "Returns new interactive id"
           (is (= "INTERACTIVE-ID-WITH-PARAM"
	         (karotz-request "INTERACTIVE-ID" "test-resources/ok-response.xml"))))))

(deftest test-sign-in
   (testing "Signin should return valid id"
            (is (= "789"
                   (with-redefs [valid-id? (constantly false)
                                 karotz-request (constantly "789")
                                 login-url (constantly "")]
                     (sign-in ["install1" "123"] {}))))))

(run-tests)