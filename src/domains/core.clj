(ns domains.core
  (:require [clj-http.client :as client])
  (:use net.cgrand.enlive-html))

(def- url "https://en.unoeuro.com/services/ninja.php")

(defn- take-names [domain-list]
  (map (fn [x] (get x :content)) domain-list))

(defn- take-timestamps [time-list]
  (map (fn [x] (get (get x :attrs) :data-sort-value)) time-list))

(defn- drop-every [n coll]
  (lazy-seq
   (if (seq coll)
     (concat (take (dec n) coll)
             (drop-every n (drop n coll))))))

(defn expired-domains
  "Return a list of 2 entry vectors containing a .dk domain and date of expiry."
  []
  (let [doc (get (client/get url) :body)
        parsed-content (html-content doc)
        target-table (select parsed-content [:table.ui-table-grid])
        cells (drop-every 3 (select target-table [:td]))
        domains (take-names (take-nth 2 cells))
        times (take-timestamps (take-nth 2 (rest cells)))]))
