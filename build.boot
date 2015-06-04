(set-env!
  :source-paths #{"src"}
  :dependencies '[[org.clojure/clojure         "1.6.0"          :scope "provided"]
                  [boot/core                   "2.0.0"          :scope "provided"]
                  [org.clojure/tools.namespace "0.2.10"         :scope "provided"]
                  [rhizome                     "0.2.5"          :scope "provided"]])

(require '[hendrick.boot-medusa :refer :all])

(def +version+ "0.0.1")

(task-options!
  pom {:project     'hendrick/boot-medusa
       :version     +version+
       :description "Boot task that creates a dependency graph of your namespaces"
       :url         "https://github.com/Hendrick/boot-medusa"
       :scm         {:url "https://github.com/Hendrick/boot-medusa"}
       :license     {"Eclipse Public License"
                     "http://www.eclipse.org/legal/epl-v10.html"}})
