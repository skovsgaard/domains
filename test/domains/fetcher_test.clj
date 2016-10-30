(ns domains.fetcher-test
  (:require [clojure.test :refer :all]
            [domains.fetcher :refer :all]))

(def tds (get-tds))
(def name-tds (take-name-tds tds))
(def time-tds (take-time-tds tds))

(deftest get-tds-test
  (testing "The parsed TDs are in a seq"
    (is (seq? tds)))
  (testing "The parsed TDs have the proper keys"
    (is (= :tag (first (keys (first tds)))))))

(deftest drop-every-test
  (testing "drop-every works for odds"
    (is (= '(1 3 5 7 9) (drop-every '(1 2 3 4 5 6 7 8 9) 2))))
  (testing "drop-every works for evens"
    (is (= [0 2 4 6] (drop-every [0 1 2 3 4 5 6] 2)))))

(deftest take-name-tds-test
  (testing "take-name-tds returns a sequence"
    (is (seq? name-tds)))
  (testing "take-name-tds returns a sequence containing maps"
    (is (map? (first name-tds)))))

(deftest take-time-tds-test
  (testing "take-time-tds returns a sequence"
    (is (seq? time-tds)))
  (testing "take-time-tds returns a sequence containing maps"
    (is (map? (first time-tds)))))

(deftest get-names-from-tds-test
  (testing "get-names-from-tds returns a sequence of url strings"
    (is (seq? (get-names-from-tds name-tds)))
    (is (string? (first (get-names-from-tds name-tds))))))

(deftest get-timestamps-from-tds-test
  (testing "get-timestamps-from-tds returns a sequence of time strings"
    (is (seq? (get-timestamps-from-tds time-tds)))
    (is (string? (first (get-timestamps-from-tds time-tds))))))

(deftest merge-names-and-times-test
  (testing "merge-names-and-times returns a sequence of maps"
    (let [merged (merge-names-and-times (get-names-from-tds name-tds)
                                        (get-timestamps-from-tds time-tds))]
      (is (seq? merged))
      (is (map? (first merged)))
      (is (= merged (run))))))
