(ns domains.core
  (:gen-class)
  (:use overtone.at-at)
  (:require [domains.fetcher :as fetch]
            [clojure.java.io :refer [as-file]]
            [clojure.tools.cli :refer [parse-opts]]))

(def one-day-in-ms 86400000)
(def at-at-pool (mk-pool))

(def cli-options
  [["-i" "--interval MILLISECONDS" "Interval to poll in milliseconds"
    :default one-day-in-ms
    :parse-fn #(Integer/parseInt %)
    :validate [#(< 0 %)]]
   ["-h" "--help" "Show this help text"]])

(defn read-existing-feed []
  (if (.exists (as-file "domain-data.edn"))
    (read-string (slurp "domain-data.edn"))
    '()))

(defn do-interval [interval]
  (every interval
         #(do
            (spit "domain-data.edn"
                  (pr-str
                   (distinct
                    (concat (read-existing-feed) (fetch/run))))
                  :append false)
            (println
             (str (str (java.time.LocalDateTime/now))
                  ": "
                  "Fetched and saved domain expiry times.")))
         at-at-pool))

(defn -main [& args]
  "Fetch the domains and write them as EDN once every n milliseconds
  either from the first command line argument or from the default of once a day."
  (let [opts (parse-opts args cli-options)]
    (if (:help (:options opts))
      (println (str "Options:\n"
                    (:summary opts)))
      (do-interval (-> opts
                       (get :options)
                       (get :interval))))))
