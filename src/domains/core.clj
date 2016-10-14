(ns domains.core
  (:gen-class)
  (:use overtone.at-at)
  (:require [domains.fetcher :as fetch]))

(def one-day-in-ms 86400000)
(def at-at-pool (mk-pool))

(defn -main [& args]
  "Fetch the domains and write them as EDN once every n milliseconds
  either from the first command line argument or from the default of once a day."
  (let [interval (if (first args)
                   (Integer/parseInt (first args))
                   one-day-in-ms)
        curr-time (str (java.time.LocalDateTime/now))
        curr-date (first (clojure.string/split curr-time #"T"))]
    (every interval
           #(do (spit (str "domain-data-" curr-date ".edn")
                      (pr-str (fetch/run)) :append false)
                (println
                 (str curr-time
                      ": "
                      "Fetched and saved domain expiry times.")))
           at-at-pool)))
