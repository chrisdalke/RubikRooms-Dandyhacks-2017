////////////////////////////////////////////////
// The Pyramid's Curse
// A game by Chris Dalke
////////////////////////////////////////////////
// Module: Logger
////////////////////////////////////////////////

package Engine.System.Logging;

import Engine.Display.Display;
import Engine.System.Commands.Commands;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.Timestamp;
import java.util.ArrayList;

//saves
public class Logger {

    public static boolean isInited = false;
    public static BufferedWriter bw;
    public static final String filename = "Resources/Logs/Log-Latest.htm";

    public static ArrayList<String> buffer;
    public static final int bufferMax = 20;

    public static void init(){

        buffer = new ArrayList<String>();

        try {

            File file = new File(filename);
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            bw = new BufferedWriter(fw);

            isInited = true;
            writeHeader();
            log("Logger subsystem started successfully.");
        } catch (Exception e){
            isInited = false;
            bw = null;
        }

    }

    private static void writeHeader(){
        write("<!DOCTYPE html>");
        write("<html><head>");
        write("<meta charset=\"UTF-8\">");
        write("<title>The Pyramid's Curse Log File</title>");
        //<link rel="stylesheet" href="stylesheet.css" type="text/css" media="screen">
        write("<style>.infoTable {\n" +
                "\tborder-spacing: 0px;\n" +
                "\tmargin:0px;padding:0px;\n" +
                "\twidth:100%;\n" +
                "\tborder:1px solid #000000;\n" +
                "\t\n" +
                "\t-moz-border-radius-bottomleft:0px;\n" +
                "\t-webkit-border-bottom-left-radius:0px;\n" +
                "\tborder-bottom-left-radius:0px;\n" +
                "\t\n" +
                "\t-moz-border-radius-bottomright:0px;\n" +
                "\t-webkit-border-bottom-right-radius:0px;\n" +
                "\tborder-bottom-right-radius:0px;\n" +
                "\t\n" +
                "\t-moz-border-radius-topright:0px;\n" +
                "\t-webkit-border-top-right-radius:0px;\n" +
                "\tborder-top-right-radius:0px;\n" +
                "\t\n" +
                "\t-moz-border-radius-topleft:0px;\n" +
                "\t-webkit-border-top-left-radius:0px;\n" +
                "\tborder-top-left-radius:0px;\n" +
                "}.infoTable table{\n" +
                "    border-collapse: collapse;\n" +
                "        border-spacing: 0;\n" +
                "\twidth:100%;\n" +
                "\theight:100%;\n" +
                "\tmargin:0px;padding:0px;\n" +
                "}.infoTable tr:last-child td:last-child {\n" +
                "\t-moz-border-radius-bottomright:0px;\n" +
                "\t-webkit-border-bottom-right-radius:0px;\n" +
                "\tborder-bottom-right-radius:0px;\n" +
                "}\n" +
                ".infoTable table tr:first-child td:first-child {\n" +
                "\t-moz-border-radius-topleft:0px;\n" +
                "\t-webkit-border-top-left-radius:0px;\n" +
                "\tborder-top-left-radius:0px;\n" +
                "}\n" +
                ".infoTable table tr:first-child td:last-child {\n" +
                "\t-moz-border-radius-topright:0px;\n" +
                "\t-webkit-border-top-right-radius:0px;\n" +
                "\tborder-top-right-radius:0px;\n" +
                "}.infoTable tr:last-child td:first-child{\n" +
                "\t-moz-border-radius-bottomleft:0px;\n" +
                "\t-webkit-border-bottom-left-radius:0px;\n" +
                "\tborder-bottom-left-radius:0px;\n" +
                "}.infoTable tr:hover td{\n" +
                "\t\n" +
                "}\n" +
                ".infoTable tr:nth-child(odd){ background-color:#ffffff; }\n" +
                ".infoTable tr:nth-child(even)    { background-color:#ffffff; }.infoTable td{\n" +
                "\tvertical-align:middle;\n" +
                "\t\n" +
                "\t\n" +
                "\tborder:1px solid #000000;\n" +
                "\tborder-width:0px 1px 1px 0px;\n" +
                "\ttext-align:left;\n" +
                "\tpadding:7px;\n" +
                "\tfont-size:10px;\n" +
                "\tfont-family:Arial;\n" +
                "\tfont-weight:normal;\n" +
                "\tcolor:#000000;\n" +
                "}.infoTable tr:last-child td{\n" +
                "\tborder-width:0px 1px 0px 0px;\n" +
                "}.infoTable tr td:last-child{\n" +
                "\tborder-width:0px 0px 1px 0px;\n" +
                "}.infoTable tr:last-child td:last-child{\n" +
                "\tborder-width:0px 0px 0px 0px;\n" +
                "}\n" +
                ".infoTable tr:first-child td{\n" +
                "\t\tbackground:-o-linear-gradient(bottom, #005fbf 5%, #003f7f 100%);\tbackground:-webkit-gradient( linear, left top, left bottom, color-stop(0.05, #005fbf), color-stop(1, #003f7f) );\n" +
                "\tbackground:-moz-linear-gradient( center top, #005fbf 5%, #003f7f 100% );\n" +
                "\tfilter:progid:DXImageTransform.Microsoft.gradient(startColorstr=\"#005fbf\", endColorstr=\"#003f7f\");\tbackground: -o-linear-gradient(top,#005fbf,003f7f);\n" +
                "\n" +
                "\tbackground-color:#005fbf;\n" +
                "\tborder:0px solid #000000;\n" +
                "\ttext-align:center;\n" +
                "\tborder-width:0px 0px 1px 1px;\n" +
                "\tfont-size:14px;\n" +
                "\tfont-family:Arial;\n" +
                "\tfont-weight:bold;\n" +
                "\tcolor:#ffffff;\n" +
                "}\n" +
                ".infoTable tr:first-child:hover td{\n" +
                "\tbackground:-o-linear-gradient(bottom, #005fbf 5%, #003f7f 100%);\tbackground:-webkit-gradient( linear, left top, left bottom, color-stop(0.05, #005fbf), color-stop(1, #003f7f) );\n" +
                "\tbackground:-moz-linear-gradient( center top, #005fbf 5%, #003f7f 100% );\n" +
                "\tfilter:progid:DXImageTransform.Microsoft.gradient(startColorstr=\"#005fbf\", endColorstr=\"#003f7f\");\tbackground: -o-linear-gradient(top,#005fbf,003f7f);\n" +
                "\n" +
                "\tbackground-color:#005fbf;\n" +
                "}\n" +
                ".infoTable tr:first-child td:first-child{\n" +
                "\tborder-width:0px 0px 1px 0px;\n" +
                "}\n" +
                ".infoTable tr:first-child td:last-child{\n" +
                "\tborder-width:0px 0px 1px 1px;\n" +
                "}</style>");
        write("</head>");
        write("<body>");
        write("<h1>The Pyramid's Curse Log File</h1></hr>");
        write("<table class=\"infoTable\">\n" +
                "    <colgroup>\n" +
                "       <col span=\"1\" style=\"width: 15%;\">\n" +
                "       <col span=\"1\" style=\"width: 15%;\">\n" +
                "       <col span=\"1\" style=\"width: 70%;\">\n" +
                "    </colgroup>");
        write("<tr>");
        write("<td>System Time</td>");
        write("<td>Frame Number</td>");
        write("<td>Message</td>");
        write("</tr>");
    }

    private static void writeFooter(){
        write("</table>");
        write("</body>");
        write("</html>");
    }

    public static void log(String message){
        java.util.Date date= new java.util.Date();
        write("<tr>");
        write("<td>" + new Timestamp(date.getTime()).toString() + "</td>");
        write("<td>" +Display.frameNumber + "</td>");
        write("<td>" + message + "</td>");
        write("</tr>");
        String messageFormatted = "[Log "+ new Timestamp(date.getTime()).toString() +"] "+message;
        Commands.log(messageFormatted);
        System.out.println(messageFormatted);
        buffer.add(message);
        bufferUpdate();
    }

    public static void logError(String message){
        java.util.Date date= new java.util.Date();
        write("<tr>");
        write("<td>" + new Timestamp(date.getTime()).toString() + "</td>");
        write("<td>" +Display.frameNumber + "</td>");
        write("<td style='color:red'>" + message + "</td>");
        write("</tr>");
        System.out.println("[Error "+ new Timestamp(date.getTime()).toString() +"] "+message);
        buffer.add(message);
        bufferUpdate();
    }

    public static void bufferUpdate(){
        while (buffer.size() > bufferMax){
            buffer.remove(0);
        }
    }

    public static ArrayList<String> getBuffer(){
        return buffer;
    }

    public static void write(String content){
        if (isInited){
            try {
                bw.write(content);
                bw.flush();
            } catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    public static void dispose(){
        log("Disposing of Logging System...");
        try {

            writeFooter();

            bw.close();
        } catch (Exception e){

        }
    }

}

////////////////////////////////////////////////
// End of code
////////////////////////////////////////////////