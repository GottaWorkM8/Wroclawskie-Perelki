package pl.szajsjem;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class SimpleLog {
    private SimpleLog(){
        String logFilePath = "log_latest.txt";
        try {
            logStream = new PrintStream(new FileOutputStream(logFilePath,true));
        } catch (FileNotFoundException e) {
            try{
                logStream = new PrintStream(System.out);
            }
            catch(Exception e2){
                throw new RuntimeException((e.toString()+e2.toString()));
            }
        }
    }


    private void logf(String message){
        logStream.println(dtf.format(LocalDateTime.now()) + ": "+ message);
    }

    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    PrintStream logStream=null;
    private static final SimpleLog simpleLog = new SimpleLog();

    public static void log(String message){
        simpleLog.logf(message);
    }


}
