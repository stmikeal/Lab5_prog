package tools;

import java.io.FileInputStream;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class ServerLogger {
   
    public static Logger logger;
    static {
        try(FileInputStream ins = new FileInputStream("log.config")){ 
            LogManager.getLogManager().readConfiguration(ins);
            logger = Logger.getLogger(ServerLogger.class.getName());
        }catch (Exception ignore){
            ignore.printStackTrace();
        }
    }
}
