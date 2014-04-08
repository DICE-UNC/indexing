indexing
========

An indexing framework for iRODS based middleware

Overview
--------
The indexing framework comes in an OSGI bundle that provides two services

    databook.listener.service.MessagingService
    
and

    databook.listener.service.IndexingService
    
The former is used to interface with messaging services such as AMQP 0.9, AMQP 1.0, etc.
This can be done by defining a Camel route.

The latter is used to interface with indexers. IndexingService provides methods for an indexer to register and unregister.
When an indexer is registered to the IndexingService, the IndexingService will relay all messages in the form of POJO that it receive to the indexer. The indexer also acquires a reference to a scheduler automatically which allows the indexer to submit time-consuming tasks to be scheduled. Currently, the supported tasks are those which require access to the underlying data grid. Each task is composed of an object indicating which data object it needs to access and a continuation object which will be called by the scheduler when the data object and system resources becomes available. A simple scheduler based on a thread pool model is included. More complex schedulers can be implemented by implementing the 

    databook.listener.Scheduler
    
interface.

Installation
--------

* Install Apache ServiceMix 5.0.0

* Checkout indexing framework source from github

* Run

        mvn install
        
  This will generate an OSGI bundle in the maven project target directory
  
* Deploy generated target to ServiceMix 5.0.0


Message Format
--------

The message format is defined in JSON Schema. Look for the schema directory in the source tree for the schemas. The jsonschema2pojo plugin is used in the build process to generate Java classes corresponding to the schemas. Jackson is used to translate between JSON messages and Java objects.

Composing JSON Message using the Java DSL
--------
The Java DSL for composing JSON Message is generated from an EriLex specification. This DSL allows very flexible composition of JSON Messages against the schemas. Some examples:

    .object()
        .key("key1").value("value1")
        .key("key2").value("value2")
        .key("key3").value("value3")
    .end()

and

    .object()
        .key("key1").array()
            .elem("elem1")
            .elem("elem2")
            .elem("elem3")
        .end()
    .end()

and

    .object()
        .key("key1").object()
            .key("key11").array()
                .object("elem1")
                    .key("key1").value("value1")
                .end()
                .object("elem2")
                    .key("key2").value("value2")
                .end()
                .object("elem3")
                    .key("key3").value("value3")
                .end()
            .end()
        .end()
        .key("key2").value("value2")
        .key("key3").value("value3")
    .end()

Objects and arrays can be nested arbitrarily.

Configure ServiceMix with AMQP routing
--------

