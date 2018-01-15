package generator;

import java.io.File;
import java.net.URL;

/**
 * @author Quinn
 * @date 2018/1/15
 * @package generator
 */
public class Generator {

    public static void main(String[] args) {
//        ClassLoader.getSystemResource(springmvc_web/TemplateDao.java)

        System.out.println(ClassLoader.getSystemResource("springmvc_web/TemplateDao.java"));
        System.out.println(ClassLoader.getSystemResource("springmvc_web"));



        File file = new File(ClassLoader.getSystemResource("springmvc_web").getPath());
        System.out.println(file.listFiles().length);

//        URL file = ClassLoader.getSystemResource("springmvc_web");
        System.out.println(ClassLoader.getSystemResource("springmvc_web").getPath());

    }

}
