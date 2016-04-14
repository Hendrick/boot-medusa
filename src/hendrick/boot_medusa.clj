(ns hendrick.boot-medusa
  {:boot/export-tasks true}
  (:require [boot.core :refer [deftask]]
            [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure.set :as set]
            (clojure.tools.namespace
             [dependency :as ns-dep]
             [file :as ns-file]
             [find :as ns-find]
             [track :as ns-track])
            [rhizome.viz :as rhizome])
  (:import java.io.File))

(def default-options
  {:path "ns-hierarchy.png"
   :vertical true
   :show-external false
   :cluster-depth 0
   :trim-ns-prefix true
   :ignore-ns #{}})

(def source-directory "src")

;; Add two local functions until they are added to `clojure.tools.namespace`.
;; See: http://dev.clojure.org/jira/browse/TNS-29?focusedCommentId=36741#comment-36741
(defn- clojurescript-file?
  "Returns true if the file represents a normal ClojureScript source file."
  [^File file]
  (and (.isFile file)
       (.endsWith (.getName file) ".cljs")))

(defn- find-sources-in-dir
  "Searches recursively under dir for source files (.clj and .cljs).
  Returns a sequence of File objects, in breadth-first sort order."
  [dir]
  (->>
    (io/file dir)
    file-seq
    (filter #(or (clojurescript-file? %)
                 (ns-file/clojure-file? %)))
    (sort-by #(.getAbsolutePath ^File %))))

(defn- file-deps
  "Calculates the dependency graph of the namespaces in the given files."
  [files]
  (->>
    files
    (ns-file/add-files {})
    ::ns-track/deps))

(defn- file-namespaces
  "Calculates the namespaces defined by the given files."
  [files]
  (map (comp second ns-file/read-file-ns-decl) files))

(defn- ignored-ns?
  [context n]
  (not (some #(.startsWith (str n) (str %))
             (:ignore-ns context))))

(defn- filter-ns
  "Filters namespaces based on the context options."
  [context namespaces]
  (cond->> namespaces
    (not (:show-external? context (:show-external context)))
    (filter (:internal-ns context))
    (:ignore-ns context)
    (filter (partial ignored-ns? context))))

(defn- graph-nodes
  [context]
  (->>
    (:graph context)
    ns-dep/nodes
    (filter-ns context)))

(defn- adjacent-to
  [context node]
  (->>
    node
    (ns-dep/immediate-dependencies (:graph context))
    (filter-ns context)))

(defn- node-cluster
  [context node]
  (let [depth (:cluster-depth context)]
    (when (< 0 depth)
      (->
        (str node)
        (str/split #"\.")
        (as-> parts
          (take (min depth (dec (count parts))) parts)
          (str/join \. parts)
          (when-not (empty? parts) parts))))))

(defn- render-node
  [context node]
  (let [internal? (contains? (:internal-ns context) node)
        cluster (node-cluster context node)]
    {:label (if (and (:trim-ns-prefix context)
                     (not (empty? cluster)))
              (subs (str node) (inc (count cluster)))
              (str node))
     :style (if internal? :solid :dashed)}))

(deftask medusa
  "Generate a dependency graph"
  [d dir PATH str "The source directory. Defaults to :src"]
  (let [source-files (find-sources-in-dir (or dir source-directory))
        context (merge default-options
                       {:internal-ns (set (file-namespaces source-files))
                        :graph (file-deps source-files)})]
    (rhizome/save-graph
      (graph-nodes context)
      (partial adjacent-to context)
      :vertical? (:vertical? context (:vertical context))
      :node->descriptor (partial render-node context)
      :node->cluster (partial node-cluster context)
      :cluster->descriptor (fn [c] {:label c})
      :filename (:path context))))
