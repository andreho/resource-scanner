package net.andreho.resources;

import net.andreho.resources.impl.DelegatingResourceTypeImpl;
import net.andreho.resources.impl.PatternResourceTypeImpl;

import java.util.function.Function;

/**
 * Created by a.hofmann on 09.05.2016.
 */
public interface ResourceType {

  ResourceType JAVA_TYPE = newTypeByPatterns("*.java", ".*\\.(?i)java");

  ResourceType CLASS_TYPE = newTypeByPatterns("*.class", ".*\\.(?i)class");

  ResourceType PROPERTIES_TYPE = newTypeByPatterns("*.properties", ".*\\.(?i)properties");

  ResourceType XML_TYPE = newTypeByPatterns("*.xml", ".*\\.(?i)xml");

  ResourceType XSD_TYPE = newTypeByPatterns("*.xsd", ".*\\.(?i)xsd");

  ResourceType JSON_TYPE = newTypeByPatterns("*.json", ".*\\.(?i)json");

  ResourceType YAML_TYPE = newTypeByPatterns("*.yml", ".*\\.(?i)yml");

  ResourceType SQL_TYPE = newTypeByPatterns("*.sql", ".*\\.(?i)sql");

  ResourceType SERVICE_DECLARATION_TYPE = newTypeByPatterns("/META-INF/services/*",
                                                            "\\/META-INF\\/services\\/[\\w].+");

  ResourceType UNKNOWN_TYPE = newTypeByDelegate("*", (name) -> true);

  /**
   * @return name of this resource type
   */
  String getName();

  /**
   * Tests the given resource's name whether it compatible with this type or not.
   *
   * @param resourceName to test for compatibility
   * @return <b>true</b> if the given resource name is compatible with given type, <b>false</b> otherwise.
   */
  boolean isCompatibleWith(String resourceName);

  /**
   * @param name
   * @param patterns
   * @return
   */
  static ResourceType newTypeByPatterns(String name, String ... patterns) {
    return new PatternResourceTypeImpl(name, patterns);
  }

  /**
   * @param name
   * @param delegate
   * @return
   */
  static ResourceType newTypeByDelegate(String name, Function<String, Boolean> delegate) {
    return new DelegatingResourceTypeImpl(name, delegate);
  }
}
