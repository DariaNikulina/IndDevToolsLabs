package org.example.Lab4;

import org.apache.catalina.Context;
import org.apache.catalina.Wrapper;
import org.apache.catalina.startup.Tomcat;

import java.io.File;

public class Main {
    public static void main(String[] args) throws Exception {
        int port = 8080;
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(port);

        String webappDir = new File("src/main/resources").getAbsolutePath();
        Context ctx = tomcat.addWebapp("", webappDir);
        ctx.setAddWebinfClassesResources(true);
        ctx.addWelcomeFile("index.html");

        Wrapper wrapper = ctx.createWrapper();
        MyServlet myServlet = new MyServlet();
        wrapper.setName(myServlet.getClass().getSimpleName());
        wrapper.setServletClass(myServlet.getClass().getName());
        wrapper.setLoadOnStartup(1);

        ctx.addChild(wrapper);

        ctx.addServletMappingDecoded("/search", myServlet.getClass().getSimpleName());

        tomcat.getConnector();
        tomcat.start();
        System.out.println("Embedded Tomcat started on port " + port);
        tomcat.getServer().await();
    }
}
