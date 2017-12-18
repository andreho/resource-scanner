package net.andreho.resources;

import net.andreho.resources.impl.DelegatingResourceTypeImpl;
import net.andreho.resources.impl.PatternResourceTypeImpl;

import java.util.function.Function;

/**
 * Created by a.hofmann on 09.05.2016.
 */
public interface ResourceType {

  ResourceType JAVA_TYPE = fromPatterns("*.java", ".*\\.(?i)java");

  ResourceType CLASS_TYPE = fromPatterns("*.class", ".*\\.(?i)class");

  ResourceType PROPERTIES_TYPE = fromPatterns("*.properties", ".*\\.(?i)properties");

  ResourceType XML_TYPE = fromPatterns("*.xml", ".*\\.(?i)xml");

  ResourceType XSD_TYPE = fromPatterns("*.xsd", ".*\\.(?i)xsd");

  ResourceType JSON_TYPE = fromPatterns("*.json", ".*\\.(?i)json");

  ResourceType YAML_TYPE = fromPatterns("*.yml", ".*\\.(?i)yml");

  ResourceType SQL_TYPE = fromPatterns("*.sql", ".*\\.(?i)sql");

  ResourceType SERVICE_DECLARATION_TYPE = fromPatterns("/META-INF/services/*",
                                                                      "\\/META-INF\\/services\\/[\\w].+");

  ResourceType UNKNOWN_TYPE = fromDelegate("*", (name) -> true);

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
  static ResourceType fromPatterns(String name, String ... patterns) {
    return new PatternResourceTypeImpl(name, patterns);
  }

  /**
   * @param name
   * @param delegate
   * @return
   */
  static ResourceType fromDelegate(String name, Function<String, Boolean> delegate) {
    return new DelegatingResourceTypeImpl(name, delegate);
  }
}
