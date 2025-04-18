package sd.mini2.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;

@Configuration
public class SwaggerConfig {
    
    @Bean
    public OpenAPI zooOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Зоопарк API")
                        .description("API для управления зоопарком")
                        .version("v1.0")
                        .contact(new Contact()
                                .name("Zoo Manager")
                                .email("zoo@example.com")));
    }
}