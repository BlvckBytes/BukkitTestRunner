<!-- This file is rendered by https://github.com/BlvckBytes/readme_helper -->

# BukkitTestRunner

An ultra simplistic test runner to be used within the bukkit environment.

## Table of Contents
- [Installation](#installation)
- [Implementation](#implementation)
- [Execution](#execution)

## Installation

Install this project into your local maven repository and put the jar file in your
server's plugin folder. Then, include this project as a *provided* dependency to the
project you're looking to run tests on.

```xml
<dependencies>
    <dependency>
        <groupId>me.blvckbytes</groupId>
        <artifactId>BukkitTestRunner</artifactId>
        <version>0.1</version>
        <scope>provided</scope>
    </dependency>
</dependencies>
```

## Implementation

Implement the class `ITestable` on the class you're extending `JavaPlugin` (the main plugin class).

<details>
<summary>ITestable.java</summary>

```java
package me.blvckbytes.bukkittestrunner;

public interface ITestable {

  /**
   * Lists all classes which contain methods annotated by {@link org.junit.Test}
   * that are to be executed by the testing framework through the bukkit test runner
   */
  Class<?>[] getTestClasses() throws Exception;

}
```
</details>


## Execution

This plugin will wait for all plugins to load. Once loaded, they're iterated and checked for the implementation
of the `ITestable` interface. If the plugin implements it, it's tests get ran and a report is being generated
at `BukkitTestRunner/reports/<plugin-name>.txt`. After this process, the server is shut down immediately, in order
to tell the server orchestrating software (if applicable) that the tests are fully executed and done.

