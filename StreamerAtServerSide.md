# Using Streamer at server side #

By server side we mean a full JVM hosted environment: standalone java process, servlet container, application server, etc.

By default you do not need any special pass to start using GWT Streamer at server side. If you need to set a [custom configuration](CustomizingGWTStreamer.md) you must do it before start using GWT Streamer.

**Note!** Your client and server must be configured the same way if you want to interchange data between them.

Take a special notice when configuring Streamer at server multithreaded environment as of it's singleton nature. The best way is to put configuration code to servlet static methods or somewhere in the initialization part.
```
public class MyServlet extends HttpServlet {
    static {
        StreamerConfig config = new StreameConfig();
        // set configuration parameters...
        Streamer.applyConfig(config);
    }
    ...
}
```

Or if you use a third-party servlet you may register a servlet listener that performs static initialization during web application startup.
`web.xml`
```
<web-app>
   ...
   <listener>
      <listener-class>com.acme.GWTStreamerInitializer</listenerclass>
   </listener>
   ...
</web-app>
```
`GWTStreamerInitializer.java`
```
public class GWTStreamerInitializer implements ServletContextListener {
    static {
        StreamerConfig config = new StreameConfig();
        // set configuration parameters...
        Streamer.applyConfig(config);
    }
    @Override    
    public void contextInitialized(ServletContextEvent sce) {}
}
```

In IoC container like Spring you may provide a configured singleton Streamer instance:
```
@Configuration
public class MyAppConfig {
   @Bean(scope=DefaultScopes.SINGLETON)
   public Streamer gwtStreamer(){
      StreamerConfig config = new StreameConfig();
      // set configuration parameters...
      Streamer.applyConfig(config);
      return Streamer.get();
   }
}
```
Or using `StreamerFactory` object.
```
@Configuration
public class MyAppConfig {
   @Bean(scope=DefaultScopes.SINGLETON)
   public StreamerFactory streamerFactory(){
        StreamerConfig config = new StreameConfig();
        // set configuration parameters...
        return new StreamerFactory(config);
   }
}
```
In JEE container you may inject configured singleton instance of Streamer using CDI:
```
...
@Produces @ApplicationScoped
public Streamer getStreamer() {
    StreamerConfig config = new StreameConfig();
    // set configuration parameters...
    Streamer.applyConfig(config);
    return Streamer.get();
}
...
@Inject
private Streamer streamer;
...
```

## Classpath and dependencies ##

In JVM environment there is no need to any add GWT jar (gwt-user.jar, gwt-servlet.jar) to you classpath. The only dependency required is gwt-streamer.jar.

**Warning!** Current version of GWT Streamer has an issue
https://code.google.com/p/gwt-streamer/issues/detail?id=3
that throws `ClassCastException` when de-serializing object on runtimes with hierarchical class loaders.
To work around this issue do not add gwt-streamer.jar to your server's classpath. Instead package and deploy it within your application's .war.