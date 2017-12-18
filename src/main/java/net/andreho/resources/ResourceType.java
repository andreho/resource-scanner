package net.andreho.resources;

import net.andreho.resources.impl.PatternResourceTypeImpl;

/**
 * Created by a.hofmann on 09.05.2016.
 */
public interface ResourceType {

  ResourceType JAVA_TYPE = new PatternResourceTypeImpl("*.java", ".*\\.(?i)java");

  ResourceType CLASS_TYPE = new PatternResourceTypeImpl("*.class", ".*\\.(?i)class");

  ResourceType PROPERTIES_TYPE = new PatternResourceTypeImpl("*.properties", ".*\\.(?i)properties");

  ResourceType XML_TYPE = new PatternResourceTypeImpl("*.xml", ".*\\.(?i)xml");

  ResourceType XSD_TYPE = new PatternResourceTypeImpl("*.xsd", ".*\\.(?i)xsd");

  ResourceType JSON_TYPE = new PatternResourceTypeImpl("*.json", ".*\\.(?i)json");

  ResourceType YAML_TYPE = new PatternResourceTypeImpl("*.yml", ".*\\.(?i)yml");

  ResourceType SERVICE_DECLARATION_TYPE = new PatternResourceTypeImpl("/META-INF/services/*",
                                                                      "\\/META-INF\\/services\\/[\\w].+");

  ResourceType UNKNOWN_TYPE = new PatternResourceTypeImpl("*.*", ".*\\.[^\\.]+");

  /**
   * @return name of this resource type
   */
  String getName();

  /**
   * Tests the given resource name whether it compatible with this type or not.
   *
   * @param resourceName to test
   * @return <b>true</b> if the given resource name is compatible with given type, <b>false</b> otherwise.
   */
  boolean isCompatibleWith(String resourceName);
}
