package com.baomidou.mybatisx.feat.generate.template;

import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;

import java.nio.charset.StandardCharsets;

public class FreeMarkerTemplateEngine {

    Configuration configuration;

    public FreeMarkerTemplateEngine() {
        configuration = new Configuration(Configuration.VERSION_2_3_22);
        configuration.setDefaultEncoding(StandardCharsets.UTF_8.name());
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
    }
}
