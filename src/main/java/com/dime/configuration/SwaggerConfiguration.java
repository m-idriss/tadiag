package com.dime.configuration;

import org.eclipse.microprofile.openapi.annotations.ExternalDocumentation;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

@OpenAPIDefinition(info = @Info(title = "Tadiag", version = "1.0", description = "This is a API Tadiag game Server based on the OpenAPI 3.0 specification. Click [here](https://github.com/m-idriss/wadiag). You can load via the `Code > Clone`, enjoy.", contact = @Contact(name = "Idriss", email = "contact@3dime.com", url = "https://github.com/m-idriss"), license = @License(name = "MIT Licence", url = "https://opensource.org/licenses/mit-license.php")), externalDocs = @ExternalDocumentation(description = "Test Documentation", url = "https://github.com/m-idriss/Tadiag?tab=readme-ov-file#readme"))
@ApplicationPath("/api")
public class SwaggerConfiguration extends Application {
}
