# Security considerations #

When using our application at public web environment we have a security problem that someone could craft a serialized message and provoke malfunctions at server side. How does data manipulation at client side can affect GWT Streamer at server-side?

### Corrupted data ###

GWT Streamer guarantees that during de-serialization an object graph will be re-created or `StreamerException` will be thrown. However it does not guarantee that the data in object graph is correct in the sense of your business application. Your business logic must perform additional checks of the data received from the client side.

### Maximum message size ###

GWT Streamer does not control a size of message. An attack with a huge amount of serialized data can provoke `OutOfMemoryError` at server side if the message size is not controlled by underlying platform.

### Array dimensions ###

Attacker could request array of big number of dimensions. To prevent JVM `OutOfMemoryError` faults on creating such arrays the number of dimensions is limited to 256.

### Length of strings and arrays ###

Attacker could request string or array of huge length to provoke `OutOfMemory` on creating buffer. GWT Streamer controls that requested size does not exceed the length of the whole data in buffer.

### Injecting of classes that are out of business scope ###

GWT Streamer controls that de-serialized classes must be one of predefined classes (`java.lang.String`, `java.util.ArrayList`, etc...) or an instance of Streamable interface. Thus it is difficult for attacker to acquire an instance of undesired class at server side (while you are not using Streamable interface somewhere outside of your business scope). However before this checks will be done a class resolution must be made. It means that an attacker theoretically could force your server to load every class from your classpath provoking various undesirable effects including `OutOfMemoryError` when there is no more space in JVM's `PermGen`.

To protect server from these attacks you can configure your GWT Streamer with a class restriction policy specifying a regular expression that matches the classes of your business scope using `StreamerConfig.setClassRestrictionPolicy()`

See [Customizing GWT Streamer](CustomizingGWTStreamer.md)