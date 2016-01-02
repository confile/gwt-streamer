# Full list of serializable types #

| **Types** | **Classes** |
|:----------|:------------|
|Primitive types|`int, byte, short, long, double, float, char, boolean`|
|Wrapper types|`java.lang.Integer, java.lang.Byte, java.lang.Short, java.lang.Long, java.lang.Double, java.lang.Float, java.lang.Character, java.lang.Boolean`|
|Java basic types|`java.lang.Object, java.lang.String, java.util.Date, java.math.BigInteger, java.math.BigDecimal`|
|Arrays of primitives|`int[], byte[], short[], long[], double[], float[], char[], boolean[]`|
|Arrays of objects|`java.lang.Object[], java.lang.String[]`|
|Streamable classes|instances of `com.googlecode.gwtstreamer.client.Streamable` and their subclasses|
|Static nested classes|the same as "Streamable classes", outer class does not need to be Streamable|
|Non-static inner classes|currently not supported|
|Collections|`java.util.ArrayList, java.util.LinkedList, java.util.HashSet, java.util.LinkedHashSet, java.util.TreeSet, java.util.Vector`|
|Maps       |`java.util.HashMap, java.util.IdentityHashMap, java.util.LinkedHashMap, java.util.TreeMap`|
|Multidimensional arrays|only as a field of `Streamable` class|
|Enums      |only as a field of `Streamable` class|
|Other classes|creating and registering [custom streamer](CustomStreamer.md)|