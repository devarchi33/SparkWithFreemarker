package com.skyfly33.spark;

import freemarker.template.Configuration;
import freemarker.template.Template;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jeoos43 on 15. 8. 11..
 */
public class HelloWorldSparkWithFreemarker {

    public static void main(String[] args) {

        final Configuration configuration = new Configuration();
        configuration.setClassForTemplateLoading(HelloWorldSparkWithFreemarker.class, "/");

        Spark.get(new Route("/") {
            StringWriter writer = new StringWriter();

            @Override
            public Object handle(Request request, Response response) {
                Map<String, Object> fruitsMap = new HashMap<String, Object>();
                fruitsMap.put("fruits", Arrays.asList("apple", "orange", "banana", "peach"));

                try {
                    Template fruitPickerTemplate = configuration.getTemplate("fruit_form.html");
                    fruitPickerTemplate.process(fruitsMap, writer);
                    return writer;
                } catch (Exception e) {
                    halt(500);
                    e.printStackTrace();
                    return null;
                }
            }
        });

        Spark.post(new Route("/favorite_fruit") {
            @Override
            public Object handle(Request request, Response response) {
                final String fruit = request.queryParams("fruit");
                if (fruit == null) {
                    return "why don't you pick up fruit?";
                } else {
                    return "Your favorite fruit is " + fruit;
                }
            }
        });

        Spark.get(new Route("/test") {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                return "This is test page\n";
            }
        });

        Spark.get(new Route("/echo/:thing") {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                return request.params(":thing");
            }
        });

    }
}
