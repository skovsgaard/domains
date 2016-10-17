(ns domains.core
  (:gen-class)
  (:use overtone.at-at)
  (:require [domains.fetcher :as fetch]
            [clojure.java.io :refer [as-file]]))

(def one-day-in-ms 86400000)
(def at-at-pool (mk-pool))

(defn read-existing-feed []
  (if (.exists (as-file "domain-data.edn"))
    (read-string (slurp "domain-data.edn"))
    '()))

(defn -main [& args]
  "Fetch the domains and write them as EDN once every n milliseconds
  either from the first command line argument or from the default of once a day."
  (let [interval (if (first args)
                   (Integer/parseInt (first args))
                   one-day-in-ms)
        curr-time (str (java.time.LocalDateTime/now))
        curr-date (first (clojure.string/split curr-time #"T"))]
    (every interval
           #(do (spit "domain-data.edn"
                      (pr-str
                       (distinct
                        (concat (read-existing-feed) (fetch/run))))
                      :append false)
                (println
                 (str curr-time
                      ": "
                      "Fetched and saved domain expiry times.")))
           at-at-pool)))
