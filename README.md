# domains

A Clojure app that once every 24 hours scrapes a list of recently expired .dk domains and writes a list of the domain and time of expiry to a .edn file.

## Usage

`lein uberjar` will build you a nice complete package out of this.

Run that with `java -jar $YOURNEWJARFILE` and optionally with a number after that command. That number is - in milliseconds - how often to ask for the data. You probably want it to be high; the default is a full 24 hours and the feed we're scraping doesn't change super frequently.

## License

This is licensed under the MPLv2.0. Please see the LICENSE file for the full license text.

