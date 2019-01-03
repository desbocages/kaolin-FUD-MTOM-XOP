# kaolin-FUD-MTOM-XOP
kaolin-FUD-MTOM-XOP is a lightweight java library which the aim is helping developers to add file upload/download capability to a JAX-WS based java application without any effort of writing a web service. it relies upon MTOM and XOP to transfer files as attachments between client and server parts of a distributed application. `FUD` just means `File Upload/Download`, while `XOP` means `XML-Binary Optimized Packaging` and `MTOM` means `Message Transmission Optimization Mechanism`. The laters are known technologies.

There are two ways to benefit from the advantages of the library. It gives two possible  implementations. we can implement the operations in a separate class or in the main class, depending on our taste. The only difference, in fact, is the number of classes to write. 

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
public class FUDService extends AbstractFUDServiceHandlerImpl{
	  
      @Override	  
      public String uploadImage(FUDImageFile file){
	     //the logic here
      }
      
    @Override
    public String uploadFile(FUDFile file) {
	    //the logic here
    }
    
    @Override
    public FUDImageFile downloadImage(String ref) {
	     //the logic here
    }
    @Override
    public FUDFile downloadFile(String ref) {
	   //the logic here
    }
}
```

That is all.

*Note: In case we chose to deploy it in a Java SE environment, we will need to add the following annotation on the FUDService class before publishing it with the Endpoint.publish() method:*

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

## Writing a client

To access this webservice, let's write a small client app. 
As we exposed our services with Apache CXF, let's configure the client relying upon CXF too.

The configuration file is the one below. Let's name it `cxf-client.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:sec="http://cxf.apache.org/configuration/security"
       xmlns:http="http://cxf.apache.org/transports/http/configuration"
       xmlns:cxf="http://cxf.apache.org/core"
       xmlns:jaxws="http://java.sun.com/xml/ns/jaxws"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:http-conf="http://cxf.apache.org/transports/http/configuration"
       xsi:schemaLocation="
       http://cxf.apache.org/configuration/security
      http://cxf.apache.org/schemas/configuration/security.xsd
      http://cxf.apache.org/transports/http/configuration
      http://cxf.apache.org/schemas/configuration/http-conf.xsd
      http://www.springframework.org/schema/beans
      http://www.springframework.org/schema/beans/spring-beans-3.0.xsd
      http://www.springframework.org/schema/context
      http://www.springframework.org/schema/context/spring-context-3.0.xsd
      http://cxf.apache.org/core
      http://cxf.apache.org/schemas/core.xsd
      http://cxf.apache.org/jaxws
      http://cxf.apache.org/schemas/jaxws.xsd">

    <bean id="jaxwsServiceFactory"
          class="org.apache.cxf.jaxws.support.JaxWsServiceFactoryBean"
          scope="prototype">
        <property name="serviceConfigurations"> 
            <list>
                <bean class="org.apache.cxf.jaxws.support.JaxWsServiceConfiguration"/>
                <bean class="org.apache.cxf.service.factory.DefaultServiceConfiguration"/>
            </list>
        </property>
    </bean>
    <!-- Factory to create the dynamic proxy -->
    <bean id="fudServiceFactory" class="org.apache.cxf.jaxws.JaxWsProxyFactoryBean">
        <property name="serviceClass" value="kaolin.mtom.fud.handling.FUDFileHandler"/>
        <property name="address"
                  value="http://localhost:8080/TestKaolinFUD-MTOM-XOP/fud"/>
        <property name="serviceFactory" ref="jaxwsServiceFactory"/>
    </bean>
    
    <cxf:bus bus="cxf">
        <cxf:features>
            <cxf:logging></cxf:logging>
        </cxf:features>
    </cxf:bus>
</beans>
```
Let's write a small app that frist downloads a specified file on the server, then takes a jpg file and sends it to the server. We will consider the configuration files presented above and thus, just write the java codes of the client and the server to let things be more clear.

### The server with the second method

This is the service:

```java
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Calendar;
import javax.activation.DataHandler;
import javax.imageio.ImageIO;
import kaloin.mtom.client.ds.ByteArrayDataSource;
import kaolin.mtom.fud.FUDFile;
import kaolin.mtom.fud.FUDImageFile;
import kaolin.mtom.fud.service.AbstractFUDServiceHandlerImpl;
import org.apache.cxf.helpers.IOUtils;

/**
 *
 * @author Desbocages
 */
public class FUDService extends AbstractFUDServiceHandlerImpl{

    @Override
    public String uploadImage(FUDImageFile file) {
        try {
            InputStream in = file.getFileHandler().getInputStream();
            String id = file.getReference();
            byte[] data = IOUtils.readBytesFromStream(in);
            saveByteArrayAsJPEGFile(data, id + Calendar.getInstance().getTimeInMillis());
            return "OK";
        } catch (IOException | RuntimeException e) {
            return "KO - " + e.getMessage();
        }
    }

    @Override
    public String uploadFile(FUDFile file) {
         try {
            InputStream in = file.getFileHandler().getInputStream();
            String id = file.getReference();
            byte[] data = IOUtils.readBytesFromStream(in);
            saveByteArrayAsFile(data, id + Calendar.getInstance().getTimeInMillis());
            return "OK";
        } catch (IOException | RuntimeException e) {
            return "KO - " + e.getMessage();
        }
    }

    @Override
    public FUDImageFile downloadImage(String ref1) {
        String ref = ref1;
        FUDImageFile toReturn;
        ByteArrayDataSource bads;
        toReturn = new FUDImageFile();
        try {
            bads = new ByteArrayDataSource(
	               getByteArrayFromJPEGImageFile(new File(ref)), "image/jpeg");
        } catch (IOException ex) {
            return null;
        }
        toReturn.setFileHandler(new DataHandler(bads));
        toReturn.setReference(ref);
        return toReturn;
    }

    @Override
    public FUDFile downloadFile(String ref1) {
        String ref = ref1;
        FUDFile toReturn;
        ByteArrayDataSource bads;
        toReturn = new FUDFile();
        try {
            //make sure the file exists (this is just an example)
            bads = new ByteArrayDataSource(
	                     Files.readAllBytes(
			            new File(ref).toPath()), "application/octet-stream");
        } catch (IOException ex) {
            return null;
        }
        toReturn.setFileHandler(new DataHandler(bads));
        toReturn.setReference(ref);
        return toReturn;
    }
    
    private void saveByteArrayAsFile(byte[] tb, String absoluteOutputFilePath) 
                                               throws FileNotFoundException, IOException {
        FileOutputStream fos;
        fos = new FileOutputStream(absoluteOutputFilePath);
        fos.write(tb);
        fos.flush();
        fos.close();
    }

    private byte[] getByteArrayFromJPEGImageFile(File f) throws IOException {
        byte[] toReturn;
        BufferedImage bimg = ImageIO.read(f);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bimg, "jpg", baos);
        baos.flush();
        toReturn = baos.toByteArray();
        baos.close();
        return toReturn;
    }

    private void saveByteArrayAsJPEGFile(byte[] tb, String absoluteOutputFilePath)
    throws IOException {
        InputStream in = new ByteArrayInputStream(tb);
        BufferedImage bImageFromConvert = ImageIO.read(in);
        ImageIO.write(bImageFromConvert, "jpg", new File(
                absoluteOutputFilePath));
    }
    
}

```

Now it is the turn of the client. Here is its implementation. It gets an instance of the proxy and frist downloads a specified file on the server, then takes a jpg file and sends it to the server.

```java
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.activation.DataHandler;
import kaloin.mtom.client.ds.ByteArrayDataSource;
import kaolin.mtom.fud.FUDFile;
import kaolin.mtom.fud.FUDImageFile;
import kaolin.mtom.fud.handling.FUDFileHandler;
import org.apache.commons.io.IOUtils;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

/**
 *
 * @author Desbocages
 */
public class MainClass {

    private static void saveByteArrayAsFile(byte[] tb,
            String absoluteOutputFilePath)
            throws FileNotFoundException, IOException {
        FileOutputStream fos;
        fos = new FileOutputStream(absoluteOutputFilePath);
        fos.write(tb);
        fos.flush();
        fos.close();
    }

    public static <T> T get(String factoryBeanName,
            Class interfaceClass) {
        JaxWsProxyFactoryBean proxyFactory;
        proxyFactory = (JaxWsProxyFactoryBean) (new XmlBeanFactory(
                new ClassPathResource(
                        "other/package/name/cxf-client.xml")))
                .getBean(factoryBeanName);
        T proxy = null;
        proxy = (T) proxyFactory.create(interfaceClass);
        return proxy;
    }

    public static void main(String[] args) throws IOException {
    
        // getting the proxy to access the remote server
        FUDFileHandler server = get("fudServiceFactory", FUDFileHandler.class);
	
	//downloading a file from the server knowing its path
        FUDFile file = server.downloadImage("F:\\img_prototype_6.jpg");
	
	//saving the gotten data on the local disk.
        saveByteArrayAsFile(
                IOUtils.toByteArray(file.getFileHandler().getInputStream()),
                "G:/jpg-dl-FUD.jpg");
		
	//collecting an image on the local disk
        String ref = "F:\\img_prototype_6.jpg";
        FUDImageFile toReturn;
        ByteArrayDataSource bads;
        toReturn = new FUDImageFile();
        InputStream inStream;
        inStream = new FileInputStream(new File(ref));
        bads = new ByteArrayDataSource(
                IOUtils.toByteArray(inStream), "images/*");
        toReturn.setFileHandler(new DataHandler(bads));
        toReturn.setReference(ref);
	
	//sending the collected file to the server
        server.uploadImage(toReturn);
    }
}
```

Hope this will be helpful.

