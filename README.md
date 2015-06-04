medusa
==========

A Boot task that generates a graph of the dependency hierarchy of a set of clojure files. This uses the [clojure.tools.namespace](https://github.com/clojure/tools.namespace) library for namespace parsing and [Rhizome](https://github.com/ztellman/rhizome) for graph generation using [Graphviz](http://www.graphviz.org/).

This plugin is inspired by [lein-ns-dep-graph](https://github.com/hilverd/lein-ns-dep-graph) and [lein-hiera](https:/github.com/greglook/lein-hiera).

## Installation

Add `boot-reload` to your `build.boot` dependencies and `require` the namespace:

```clj
(set-env! :dependencies '[[hendrick/boot-medusa "X.Y.Z" :scope "test"]])
(require '[adzerk.boot-reload :refer :all])
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

This will generate a dependency graph at `ns-hierarchy.png`, showing the interdependency of the project's source namespaces for `.clj` files located in the `src` directory. Additional directories to include may be given as command-line arguments:

    $ boot medusa --path ../foo-lib/src --path ../bar-lib/src

## Example

This image shows the dependency hierarchy from a moderately complex project. The namespaces are clustered by two levels, and it shows a dependency on the external `puget` library.

![Example dependency hierarchy](doc/example.png)

## Options

Graph generation may be controlled with additional options under the `:hiera` key in the project map. The available options, and their default values are:

```clojure
:hiera
{:path "target/ns-hierarchy.png"
 :vertical true
 :show-external false
 :cluster-depth 0
 :trim-ns-prefix true
 :ignore-ns #{}}
```

| name | description |
|------|-------------|
| `:path` | Gives the location to output the graph image to. |
| `:vertical` | Specifies whether to lay out the graph vertically or horizontally. |
| `show-external` | When set, the graph will include nodes for namespaces which are not defined in the source files, marked by a dashed border. |
| `:cluster-depth` | Sets the number of namespace segments to cluster nodes by. Clusters must contain at least one fewer segment than the nodes themselves. |
| `:trim-ns-prefix` | When set, clustered namespaces will have the cluster prefix removed from the node labels. |
| `:ignore-ns` | A set of namespace prefixes to exclude from the graph. For example, `#{clojure}` would exclude `clojure.string`, `clojure.java.io`, etc. |

## License

Dunno yet.
