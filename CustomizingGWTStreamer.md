# Introduction #

Custom GWT Streamer configuration may be applied via `StreamerConfig` object:
```
StreamerConfig config = ...
// ...
Streamer.applyConfig(config);
```
Serialized data may only be interchanged between streamers that have the same configuration. It means that if you try to de-serialize data with a Streamer that configured differently that a Streamer you used for serialization, a `StreamerException` will be thrown. A special hash value is passed to output stream to identify a version of configuration of the Streamer used to serialize data. You may obtain a version hash value using `Streamer.getConfigVersion()`:
```
Streamer.applyConfig(config);
int ver = Streamer.getConfigVersion();
assert ver == config.getVersion();
```

## Specifying names of classes/packages ##

To reduce the size of output data Streamer serializes the names of classes and packages only once per object graph. All subsequent occurences of package or class name will be substituted by a short reference. However you may initially tell to Streamer the names of packages that contain your Streamable classes or the whole names of Streamable classes.
```
StreamerConfig config = ...
// you only need to register a largest package, all super packages be registered automatically
// the names of all classes that start with  'com.mycompany.project.client.beans' will be cut by this prefix
config.registerName("com.mycompany.project.client.beans");
// register particular classes: a shortcuts will be used instead of class names
config.registerName("com.mycompany.project.client.dto.SomeClass");
config.registerName("com.mycompany.project.client.dto.DtoClass$NestedClass");
config.registerClass(com.mycompany.project.client.beans.MyClass.class);
```

## Custom streamers ##

To register custom streamers:
```
StreamerConfig config = ...
config.registerStreamer(MyClass.class, new MyClassStreamer());
```
See also: [Creating custom streamers](CustomStreamer.md)

## Change serialization format ##

GWT Streamer supports various output formats. To change format a `StreamFactory` object must be set on the configuration object:
```
StreamerConfig config = ...
config.setStreamFactory(new PrintableStreamFactory());
```
These are some predefined `StreamFactories`:
  * `Base64PackedStreamFactory` (default) - optimized Base64 encoding
  * `Base64StreamFactory` - raw Base64 encoding
  * `UrlEncodedStreamFactory` - string-based format with URL encoding
  * `PrintableStreamFactory` - string-based format (good for debug)
You also may create and register your own `StreamFactory` and implement Reader and Writer interfaces for I/O.

## Restrict a scope of classes ##

To protect your server against attacks that inject classes out of your business scope you may set a regular expression that matches names of allowed classes.
```
StreamerConfig config = ...
// match only classes from package com.mycompany.project.shared
config.setClassRestrictionPolicy("^com\\.mycompany\\.project\\.shared\\..*");
```
You do not need to add a standard classes like `java.lang.String`, `java.util.ArrayList`, arrays, etc. to your filterinf regular expression.
This method only affects a server side and has no effect on client side. It does not change config version number and not needed to be set on client side. Setting null disables restriction policy check.

See also: [SecurityConsiderations](SecurityConsiderations.md)