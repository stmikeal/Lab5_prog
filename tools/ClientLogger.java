package tools;

import java.io.FileInputStream;
import java.util.logging.LogManager;

public class ClientLogger {
    public static java.util.logging.Logger logger;
    static {
        try(FileInputStream ins = new FileInputStream("log.config")){ 
            LogManager.getLogManager().readConfiguration(ins);
            logger = java.util.logging.Logger.getLogger(ServerLogger.class.getName());
        }catch (Exception ignore){
            ignore.printStackTrace();
        }
    }
}
