## Home Automation 

Application, which allows to integrate steering devices and sensors into one system.  
Based on spring boot. Mqtt is a protocol used to integrate devices. 
HTTP interface makes possible to configure the system. Application logic is provided by plugins.

### Domain model
Package *eu.wordpro.ha.server.domain*

#### Space
Describes spatial organisation. The spaces can be organised hierarchic.
#### Device
Describes device. Device is placed in a space
#### SignalProcessor
Signal processor is an instance of a plugin. Describes how to instantiate and configure a plugin.
#### ProcessingChain
Processing chain is attached to device. Allows to process data send from device with a chain of signal processors. 
Result of the processing can be send to other devices.

### Plugin development
Plugin is an implementation of interface *eu.wordpro.ha.api.SignalProcessor*. Plugin defines own configuration format and format of exchanged data.
For implementation details please refer documentation of *eu.wordpro.ha.api.SignalProcessor*.  

#### Plugin installation
Jar containing the plugin has to be added to the classpath of the server application.

### MQTT
The application requires MQTT Broker installation. Access to the broker is configurable as spring boot property *ha.mqtt.brokerUri*. Default configuration is placed in *application.properties* . Refer the [documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/spring-boot-features.html#boot-features-external-config) for details about externalized configuration.  
Topics used to communicate between devices are assembled from spaces hierarchy and device slugs. Signals sent from device are published on topic with suffix *up*, incoming signals are published on topic with prefix *down*, for example */groundfloor/kitchen/switch1/down*


### Server development
#### Dependencies
##### Home automation API
Clone from https://github.com/tomaszbembnista/haapi and install to the local repository with
    
    gradlew install
    
#### Database
Server requires a Postgresql database. Default access to the database is configured in *application.properties*. Database migration is done with liquibase. Migration configuration files are placed in *resources/db.changelog*
#### Starting application during development

    gradlew bootRun
    
#### Building the server

    gradlew bootJar 
    
### Swagger interface
Server exposes a swagger interface, which makes easier client development. API documentation and Swagger UI are available hier:

    http://localhost:8100/v2/api-docs
    http://localhost:8100/swagger-ui.html
    
### Others 
Starting mosquitto on windows

    C:\Program Files\mosquitto>mosquitto -p 11883
    
where 11883 is port number
    

    
