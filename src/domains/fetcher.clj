(ns domains.fetcher
  (:require [net.cgrand.enlive-html :as html]
            [clj-http.client :as client]))

(def url "https://en.unoeuro.com/services/ninja.php")

(defn fetch-url [url]
  (html/html-snippet (:body (client/get url))))

(defn get-tds []
  (html/select (fetch-url url) [:table.ui-table-grid :td]))

(defn drop-every [col n]
  (lazy-seq
    (if (seq col)
      (concat (take (dec n) col)
              (drop-every (drop n col) n)))))

(defn take-name-tds [tds]
  (take-nth 2 (drop-every tds 3)))

(defn take-time-tds [tds]
  (take-nth 2 (rest (drop-every tds 3))))

(defn get-names-from-tds [name-tds]
  (map (fn [td] (first (get td :content)))
       name-tds))

(defn get-timestamps-from-tds [time-tds]
  (map (fn [td] (-> td
                   (get :attrs)
                   (get :data-sort-value)))
       time-tds))

(defn merge-names-and-times [name-col time-col]
  (map (fn [pair]
         (let [[domain-name timestamp] pair]
           {:domain domain-name :timestamp timestamp}))
       (map vector name-col time-col)))

(defn run []
  (let [tds (get-tds)
        names (get-names-from-tds (take-name-tds tds))
        times (get-timestamps-from-tds (take-time-tds tds))
        merged (merge-names-and-times names times)]
    merged))
