### easylog [![Build Status](https://travis-ci.org/epyon81/easylog.svg?branch=master)](https://travis-ci.org/epyon81/easylog)
Easy to use aspect oriented logging framework.

### Getting started

First add the repository containing the artifacts to your build script.

### Gradle

```shell
repositories {
    maven { url 'https://dl.bintray.com/epyon81/maven' }
}
```

### Maven

```shell
<repository>
  <id>eastlog-repo</id>
  <name>easylog repository</name>
  <url>https://dl.bintray.com/epyon81/maven</url>
</repository>
```

Add the _easylog-core_ library.

### Gradle

```shell
compile 'com.github.epyon81.easylog:easylog-core:1.2.1'
```

### Maven

```shell
<dependency>
  <groupId>com.github.epyon81.easylog</groupId>
  <artifactId>easylog-core</artifactId>
  <version>1.2.1</version>
</dependency>
```

Add _easylog-slf4j_ binding.

### Gradle

```shell
compile 'com.github.epyon81.easylog:easylog-slf4j:1.2.1'
```

### Maven

```shell
<dependency>
  <groupId>com.github.epyon81.easylog</groupId>
  <artifactId>easylog-slf4j</artifactId>
  <version>1.2.1</version>
</dependency>
```

**(Optional)** Add _easylog-el-messageparser_ to use EL expressions in log messages.

### Gradle

```shell
compile 'com.github.epyon81.easylog:easylog-el-messageparser:1.2.1'
```

### Maven

```shell
<dependency>
  <groupId>com.github.epyon81.easylog</groupId>
  <artifactId>easylog-el-messageparser</artifactId>
  <version>1.2.1</version>
</dependency>
```
 
Choose your framework binding:

## Guice

### Gradle

```shell
compile 'com.github.epyon81.easylog:easylog-guice:1.2.1'
```

### Maven

```shell
<dependency>
  <groupId>com.github.epyon81.easylog</groupId>
  <artifactId>easylog-guice</artifactId>
  <version>1.2.1</version>
</dependency>
```

Include the guice module `easylog.guice.EasylogModule` when creating your interceptor.

## JEE

### Gradle

```shell
compile 'com.github.epyon81.easylog:easylog-jee:1.2.1'
```

### Maven

```shell
<dependency>
  <groupId>com.github.epyon81.easylog</groupId>
  <artifactId>easylog-jee</artifactId>
  <version>1.2.1</version>
</dependency>
```

Setting up JEE is a bit more complex than guice. Register the interceptor class `easylog.jee.EasylogJeeInterceptor` in your _beans.xml_. Next create beans for the interface `easylog.core.LoggerFactory` and optionally `easylog.core.LogMessageParser`. These beans should delegate to an instance of `easylog.slf4j.Slf4jLoggerFactory` and `easylog.el.messageparser.ElMessageParser`.
Now you can annotate your beans with the `@Easylog` interceptor annotation and use the `@Log` annotation on your bean's public methods. 
 
## Spring

### Gradle

```shell
compile 'com.github.epyon81.easylog:easylog-spring:1.2.1'
```

### Maven

```shell
<dependency>
  <groupId>com.github.epyon81.easylog</groupId>
  <artifactId>easylog-spring</artifactId>
  <version>1.2.1</version>
</dependency>
```

To start using it just import the `easylog.spring.EasylogConfiguration` into your application context.