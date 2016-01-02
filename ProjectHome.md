Easy to use serialization library for Google Web Toolkit.

## Motivation ##
Serialization of objects is most painful theme of GWT framework. While GWT does not support reflection on client side serialization may only be done via code generation. GWT RPC provides a serialization utility that is heavily bound with RPC mechanism and is very difficult to use separately.

Our aim is to create a simple easy to use library that performs serialization of java objects to/from strings. Serialized format must be completely symmetric on client and server side and have no dependencies of transport mechanism.

## Features ##
  * Simple, zero-config. Client and server use the same API ([see server notes](StreamerAtServerSide.md)).
  * Symmetric. Serialized data can be exchanged between client and server side.
  * Field-based serialization. No need for getters/setters.
  * Automatic type discovery based on class inheritance.
  * Serialization format is http-ready. Default format uses Base64 encoding which is also url-ready.
  * Default serialization of basic java types, arrays, enums, collections ([full list](SerializableTypes.md)).
  * Customizable. [Custom streamers](CustomStreamer.md) can easily be written for your own types.
  * Fast and [secure](SecurityConsiderations.md).

## Using library ##
Add gwt-streamer.jar library to your classpath. If you are using Maven add this dependency to your pom.xml:
```
<dependency>
  <groupId>com.googlecode.gwt-streamer</groupId>
  <artifactId>gwt-streamer</artifactId>
  <version>2.0.0</version>
</dependency>
```
Add this line to your .gwt.xml:
```
...
<inherits name="com.googlecode.gwtstreamer.GWTStreamer"/>
...
```
All serializable classes must implement Streamable interface directly or indirectly.
```
import com.googlecode.gwtstreamer.client.Streamable;
...
public class Person implements Streamable {
    // fields, visibility doesn't matter
    private String name;
    private int age;

    private Person() {}        // default no-args constructor is required

    public Person( String name, int age ) {
        this.name = name; this.age = age;
    }

    // getters, setters are optional...
}
```
Server and client use the same API for serialization.
```
Person person = new Person( "Anton", 33 );
String buffer = Streamer.get().toString( person );
//...
person = (Person) Streamer.get().fromString( buffer );
//...
Person personCopy = Streamer.get().deepCopy( person );
```

## Serializable types ##
  * All primitive types and their object wrappers.
  * Standard java types: `String, BigInteger, BigDecimal, Date.`
  * Null value.
  * One-dimensional arrays of primitive types, `Object[]` and `String[]`.
  * All classes that implement Streamable interface and all their subclasses.
  * All serializable fields of Streamable class including enums and miltidimensional arrays of any type.
  * `java.util.*` collections.

#### Arrays and enums consideration ####

Currently only primitive, `Object[]` and `String[]` one-dimensional arrays may be serialized at top level. No enums serialization is supported at top level. However if enum or array of any dimension is a field of any Streamable class it will be serialized within enclosing Streamable class.

If you need to serialize your enums or arrays at top level you have two options to do this:
  * Create a DTO wrapper for this type.
  * Write and register [your own streamer](CustomStreamer.md).

Check the [full list](SerializableTypes.md) of serializable types.