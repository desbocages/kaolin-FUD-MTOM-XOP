# kaolin-FUD-MTOM-XOP
kaolin-FUD-MTOM-XOP is a lightweight java library which the aim is to add file upload/download capability to a JAX-WS based java application without any effort of writing a web service. it relies upon MTOM and XOP. 

There are two ways to benefit from the advantages of the library. It gives two possible  implementations. we can implement the operation in a separate class or in the main class, depending on our taste. The only difference, in fact, is the number of classes to write. 

Let's go straightforward and see how it works:

## First method: Using separate files for service and implementation

The first thing to do here is  to write the class that handles the `Upload/Download` logic. It should extend the `AbstractHandlingManagerImpl` class, have a default constructor and provide an implementation of the abstract methods we need. Secondly, we just have to write the service itself, passing our first class as the parameter of the class.

Let's see an example:

*The following classes should be in the source package of the web application in case we are not using the Endpoint class to publish.*

We will name the handling class `FUDHandlerClass`

```java
public class FUDHandlerClass extends AbstractHandlingManagerImpl{

    @Override
    public FUDFile doDownload(String reference) {
	     //write the download logic here     
    }
    
    @Override
    public FUDImageFile doDownloadForImage(String reference) {
      //write the download logic here     
    }
    
    @Override
    public String doUpload(FUDFile file) {
     //write the upload logic here   
    }
    @Override
    public String doUpload(FUDImageFile file) {
     //write the upload logic here   
    }
}
```
Once done, let's implement the service itself:

We will name it `FUDService`. It should extend the `AbstractFUDServiceImpl` class, passing it the previous class as a parameter. It equally needs a member class that extends the `FUDHandler` class as shown below. The `HandlingConnector` class is the one that does the job we have implemented in the first class.

```java
public class FUDService extends AbstractFUDServiceImpl<FUDHandlerClass>
        implements FUDFileHandler {
        
    private class HandlingConnector extends FUDHandler<FUDHandlerClass> {  }
    
    public FUDService() {
        this.handler = new HandlingConnector();
    }
    
}
```

Everything is done. We can now publish it as we want.

## Second method: Using a single file for service and implementation

This is the simplest method. The only thing to do is to write a concrete java class that extends the `AbstractFUDServiceHandlerImpl` class and implements the overriden methods.

Let's name it `FUDService`. Here is its skeleton:

```java
public class FUDServiceHandler extends AbstractFUDServiceHandlerImpl{
	    
      public String uploadImage(FUDImageFile file){
	     //the logic here
      }
      
    public String uploadFile(FUDFile file) {
	    //the logic here
    }
    
    public FUDImageFile downloadImage(String ref) {
	     //the logic here
    }
    public FUDFile downloadFile(String ref) {
	   //the logic here
    }
}
```

That is all.

*Note: In case we chose to deploy it in a Java SE environment, we will need to add the following annotation on the FUDService class before publishing it with the Endpoint.publish() method: *

```java
@WebService(endpointInterface = "kaolin.mtom.fud.handling.FUDFileHandler")
```

## Deploying

Let's publish our service with `Apache CXF 2.4.1` coupled to `Spring Framework`.

We will consider the following web.xml file.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xmlns="http://java.sun.com/xml/ns/javaee"
         xmlns:web="http://java.sun.com/xml/ns/javaee/web-app_2_5.xsd"
         xsi:schemaLocation="http://java.sun.com/xml/ns/javaee
         http://java.sun.com/xml/ns/javaee/web-app_2_5.xsd"
         id="services" version="2.5">
    <context-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>/WEB-INF/beans.xml</param-value>
    </context-param>
    <listener>
        <listener-class>
            org.springframework.web.context.ContextLoaderListener
        </listener-class>
    </listener>
    <servlet>
        <servlet-name>CXFServlet</servlet-name>
        <servlet-class>
            org.apache.cxf.transport.servlet.CXFServlet
        </servlet-class>
        <load-on-startup>1</load-on-startup>
    </servlet>
    <servlet-mapping>
        <servlet-name>CXFServlet</servlet-name>
        <url-pattern>/*</url-pattern>
    </servlet-mapping>
</web-app>
```

Let's name our CXF config file `beans.xml`, the one defined in the `web.xml`  file above. Its content is the following:

```xml
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:cxf="http://cxf.apache.org/core"
       xmlns:jaxws="http://cxf.apache.org/jaxws"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
     http://www.springframework.org/schema/beans/spring-beans-3.0.xsd
     http://www.springframework.org/schema/context
     http://www.springframework.org/schema/context/spring-context-3.0.xsd
     http://cxf.apache.org/core
     http://cxf.apache.org/schemas/core.xsd
     http://cxf.apache.org/jaxws
     http://cxf.apache.org/schemas/jaxws.xsd"
       default-autowire="byName">
    
    <import resource="classpath:META-INF/cxf/cxf.xml" />
    <!-- <import resource="classpath:META-INF/cxf/cxf-extension-soap.xml" /> -->
    <import resource="classpath:META-INF/cxf/cxf-servlet.xml" />
    <import resource="logic-beans.xml" />
    <context:annotation-config />
    
    <jaxws:endpoint id="fudws"
                    implementorClass=" some.package.name.FUDService"
                    implementor="#FUDService"
                    address="/fud">
        <jaxws:properties>
            <entry key="mtom-enabled" value="true"/>
        </jaxws:properties>
    </jaxws:endpoint>
 
    <!--Message logging using the CXF logging feature? -->
    <cxf:bus bus="cxf">
        <cxf:features>
            <cxf:logging></cxf:logging>
        </cxf:features>
    </cxf:bus>
   
</beans>
```

The `beans.xml` references a file named `logic-beans.xml` that contains our service's bean declaration. Here is its content:

```xml
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:cxf="http://cxf.apache.org/core"
       xmlns:jaxws="http://cxf.apache.org/jaxws"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
   http://www.springframework.org/schema/beans/spring-beans-2.5.xsd
   http://www.springframework.org/schema/context
   http://www.springframework.org/schema/context/spring-context-2.5.xsd
   http://cxf.apache.org/core http://cxf.apache.org/schemas/core.xsd
   http://cxf.apache.org/jaxws http://cxf.apache.org/schemas/jaxws.xsd"
       default-autowire="byName">
    <bean id="FUDService" class="some.package.name.FUDService"/>
</beans>
```
Everything is OK, we can package all this in a war file and deploy in a container such as Apache tomcat.

With all these, a great question remains: How to build the FUDFile/FUDImageFile objects?
Here is an example for image files.

```java
        byte[] data = ...
        FUDFile toUpload = new FUDFile();
        ByteArrayDataSource bads = new ByteArrayDataSource(data, "image/*");
        toUpload.setFileHandler(new DataHandler(bads));
        toUpload.setReference(ref);
        toUpload.setFileLength(data.length);
```

We do the same thing for FUDImageFile that only takes images.


