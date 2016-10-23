(defproject domains "0.2.0"
  :description "An app to fetch a list of recently expired .dk domains at intervals."
  :url "http://example.com/TBD"
  :license {:name "Mozilla Public License"
            :url "https://www.mozilla.org/media/MPL/2.0/index.815ca599c9df.txt"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/tools.cli "0.3.5"]
                 [clj-http "2.3.0"]
                 [enlive "1.1.6"]
                 [overtone/at-at "1.2.0"]]
  :main domains.core
  :aot [domains.core])
