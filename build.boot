(set-env!
  :source-paths #{"src"}
  :dependencies '[[org.clojure/clojure         "1.7.0"]
                  [boot/core                   "2.5.5"]
                  [org.clojure/tools.namespace "0.2.11"]
                  [rhizome                     "0.2.5"]
                  [adzerk/bootlaces            "0.1.13" :scope "test"]])

(require
 '[adzerk.bootlaces :refer :all]
 '[hendrick.boot-medusa :refer :all])

(def +version+ "0.1.0")

(bootlaces! +version+)

(task-options!
  pom {:project     'hendrick/boot-medusa
       :version     +version+
       :description "Boot task that creates a dependency graph of your namespaces"
       :url         "https://github.com/Hendrick/boot-medusa"
       :scm         {:url "https://github.com/Hendrick/boot-medusa"}
       :license     {"Eclipse Public License"
                     "http://www.eclipse.org/legal/epl-v10.html"}})
