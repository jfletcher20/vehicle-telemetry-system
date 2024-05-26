package slusac;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class AppContextListener implements ServletContextListener {

  private static String appPath;
  private static ServletContext sce;

  @Override
  public void contextInitialized(ServletContextEvent sce) {
    this.sce = sce.getServletContext();
    appPath = this.sce.getRealPath("/WEB-INF") + java.io.File.separator;
    System.out.println("App path: " + appPath);
    // print all files within appPath
    java.io.File folder = new java.io.File(appPath);
    java.io.File[] listOfFiles = folder.listFiles();
    for (int i = 0; i < listOfFiles.length; i++) {
      if (listOfFiles[i].isFile()) {
        System.out.println("   - File " + listOfFiles[i].getName());
      } else if (listOfFiles[i].isDirectory()) {
        System.out.println(" > Directory " + listOfFiles[i].getName());
      }
    }
  }

  @Override
  public void contextDestroyed(ServletContextEvent sce) {}

  public static String getAppPath() {
    return appPath;
  }
}
