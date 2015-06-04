medusa
======

A Boot task that generates a graph of the dependency hierarchy of a set of clojure files. This uses the [clojure.tools.namespace](https://github.com/clojure/tools.namespace) library for namespace parsing and [Rhizome](https://github.com/ztellman/rhizome) for graph generation using [Graphviz](http://www.graphviz.org/).

This plugin is inspired by [lein-ns-dep-graph](https://github.com/hilverd/lein-ns-dep-graph) and [lein-hiera](https:/github.com/greglook/lein-hiera).

## Installation

Add `boot-reload` to your `build.boot` dependencies and `require` the namespace:

```clj
(set-env! :dependencies '[[hendrick/boot-medusa "X.Y.Z" :scope "test"]])
(require '[hendrick.boot-medusa :refer :all])
```

You'll also need Graphviz installed, in order to generate the graph images. Check your local package manager:

```
# Debian/Ubuntu:
$ sudo apt-get install graphviz

# OS X:
$ brew install graphviz
```

## Usage

You can see the options available on the command line:

```bash
boot medusa -h
```

or in the REPL:

```clj
boot.user=> (doc medusa)
```

A namespace graph can be generated by running:

```bash
boot medusa
```

This will generate a dependency graph at `ns-hierarchy.png`, showing the interdependency of the project's source namespaces for `.clj` files located in the `src` directory.

## License

Eclipse Public License.
