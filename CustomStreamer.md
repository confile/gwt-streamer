# Creating custom streamers #

If you need to add serialization of custom type that does not implement `Streamable` interface you may define your own streamer class.

Create a class that extends `Streamer` and redefine `readObject()` and `writeObject()` methods to serialize object in your own manner.
```
public class MyCustomStreamer extends Streamer {
    @Override
    public void writeObject( Object obj, WriteContext ctx ) {
        // save object identity to avoid reference duplication within the same graph
        ctx.addRef( obj );
        // write object fields
        MyObject mo = (MyObject) obj;
        ctx.writeInt( mo.intField );
        ctx.writeString( mo.stringField );
        ctx.writeObject( mo.objectField );
    }

    @Override
    public Object readObject( ReadContext ctx )
    {
        // create object
        MyObject mo = ...
        // add identity to restore serialized references properly
        ctx.addRef( mo );
        // reading fields
        mo.intField = ctx.readInt();
        mo.stringField = ctx.readString();
        mo.objectField = ctx.readObject();
        return mo;
    }
}
```
To use new streamer you must register it with custom streamer configuration somewhere in the initialization part of your application:
```
StreamerConfig config = ...
config.registerStreamer( MyObject.class, new MyCustomStreamer() );
...
Streamer.applyConfig(config);
```
See also: [CustomizingGWTStreamer](CustomizingGWTStreamer.md)

You may also extend one of predefined implementations of streamers:
  * `ArrayStreamer` for arrays
  * `CollectionStreamer` for collections
  * `EnumStreamer` for custom enums
  * `MapStreamer` for maps
  * `StructStreamer` for field-based objects