[![Build Status](https://drone.io/github.com/figurate/figurate-core/status.png)](https://drone.io/github.com/figurate/figurate-core/latest)

figurate-core
=============

A JVM plugin environment built on OSGi


Modules
-------

Whilst OSGi allows for modules that are loosely coupled to dependent modules, implementations often result in fragile,
tightly-coupled modules that will not run without all of the required dependencies.

A key driver for figurate is to design modules around services that are not dependent on others, or are able to function
partially without loading any other modules.
