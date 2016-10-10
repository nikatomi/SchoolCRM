package by.schoolby.component;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Properties;

@Component
public class PropertyLoader {
    public Properties load(String filename) {

        Properties properties = new Properties();

        try {
            Resource resource  = new ClassPathResource(filename);
            properties         = PropertiesLoaderUtils.loadProperties(resource);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return properties;
    }
}
