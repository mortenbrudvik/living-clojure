(defproject living-clojure "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [camel-snake-kebab "0.4.2"]
                 [org.clojure/core.async "1.3.618"]]
  :repl-options {:init-ns living-clojure.5-how-to-use-Clojure-project-and-libraries}
  :main living-clojure.5-how-to-use-Clojure-project-and-libraries
  :aot [living-clojure.5-how-to-use-Clojure-project-and-libraries])
