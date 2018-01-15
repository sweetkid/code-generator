package util;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.io.StringWriter;

/**
 * Created by DIY on 2017/5/25.
 */
public class FreemarkerUtil {

    private static Configuration c = new Configuration(Configuration.VERSION_2_3_23);
    private static StringTemplateLoader stringLoader = new StringTemplateLoader();

    static {
        c.setTemplateLoader(stringLoader);
    }

    public static String getString(String templateContent, Object paramMap) {
        stringLoader.putTemplate(templateContent, templateContent);
        try {
            Template template = c.getTemplate(templateContent);
            StringWriter writer = new StringWriter();
            template.process(paramMap, writer);
            return writer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }
        return null;
    }

}
