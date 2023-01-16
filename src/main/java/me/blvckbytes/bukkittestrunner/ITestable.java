package me.blvckbytes.bukkittestrunner;

public interface ITestable {

  /**
   * Lists all classes which contain methods annotated by {@link org.junit.Test}
   * that are to be executed by the testing framework through the bukkit test runner
   */
  Class<?>[] getTestClasses() throws Exception;

}
