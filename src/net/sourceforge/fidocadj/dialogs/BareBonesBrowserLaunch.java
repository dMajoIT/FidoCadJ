package net.sourceforge.fidocadj.dialogs;

import javax.swing.JOptionPane;

/**
    <b>Bare Bones Browser Launch for Java</b><br>
    Utility class to open a web page from a Swing application
    in the user's default browser.<br>
    Supports: Mac OS X, GNU/Linux, Unix, Windows XP/Vista/7<br>
    Example Usage:<code><br> &nbsp; &nbsp;
        String url = "http://www.google.com/";<br> &nbsp; &nbsp;
        BareBonesBrowserLaunch.openURL(url);<br></code>
    Latest Version: <a href="http://www.centerkey.com/java/browser/">
        www.centerkey.com/java/browser</a><br>
    Author: Dem Pilafian<br>
    Public Domain Software -- Free to Use as You Like
    @version 3.1, June 6, 2010 - (mod. 2023)
*/
public final class BareBonesBrowserLaunch
{

    static final String[] browsers = { "google-chrome", "firefox", "opera",
        "epiphany", "konqueror", "conkeror", "midori", "kazehakase",
        "mozilla" };
    static final String errMsg = "Error attempting to launch web browser";


    private BareBonesBrowserLaunch()
    {
        // Does nothing.
    }
    /**
        Opens the specified web page in the user's default browser
        @param url A web address (URL) of a web page (ex:
            "http://www.google.com/")
    */
    public static void openURL(String url)
    {
        try {  //attempt to use Desktop library from JDK 1.6+
            Class<?> d = Class.forName("java.awt.Desktop");
            d.getDeclaredMethod("browse", new Class[]
                {java.net.URI.class}).invoke(
                    d.getDeclaredMethod("getDesktop").invoke(null),
                    new Object[] {java.net.URI.create(url)});
            //above code mimicks:  java.awt.Desktop.getDesktop().browse()
        } catch (Exception ignore) {  //library not available or failed
            String osName = System.getProperty("os.name");
            try {
                if (osName.startsWith("Mac OS")) {
                    Class.forName("com.apple.eio.FileManager")
                        .getDeclaredMethod("openURL",
                        new Class[] {String.class}).invoke(null,
                            new Object[] {url});
                } else if (osName.startsWith("Windows")) {
                    Runtime.getRuntime().exec(
                        "rundll32 url.dll,FileProtocolHandler " + url);
                } else { //assume Unix or Linux
                    String browser = null;
                    for (String b : browsers) {
                        if (browser == null &&
                            Runtime.getRuntime().exec(new String[]
                            {"which", b}).getInputStream().read() != -1)
                        {
                            Runtime.getRuntime().exec(new String[]
                                {browser = b, url});
                        }
                    }
                    if (browser == null) {
                        System.out.println("Browser not found");
                    }
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, errMsg + "\n" +
                    e.toString());
            }
        }
    }

}
