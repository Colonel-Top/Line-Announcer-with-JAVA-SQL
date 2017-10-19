/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author proms
 */
import java.awt.Color;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.Proxy;
import java.net.InetSocketAddress;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import static java.nio.charset.StandardCharsets.UTF_8;
import java.sql.*;
import javax.net.ssl.HttpsURLConnection;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class CurlLauncher
{

    public static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    public static final String DB_URL = "jdbc:mysql://colonel-tech.com/DELC_DB";
    static final String USER = "delcsql";
    static final String PASS = "x";

    public static void postCurl(String messagein, String urlin) throws MalformedURLException, IOException
    {
        System.out.println(messagein);
        GUI.jLabel8.setText("0");
        int counter = 0;
        Connection connection = null;
        Statement stmt = null;
        Statement stmt2 = null;
        GUI.jTextArea2.setText("");
        try
        {
            //SQL Section
            Class.forName("com.mysql.jdbc.Driver");
            GUI.jTextArea2.append("Connecting to database...\n");
            GUI.thisgui.repaint(); GUI.thisgui.validate();
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
            GUI.jTextArea2.append("Creating statement...\n");
            GUI.thisgui.repaint(); GUI.thisgui.validate();
            stmt = connection.createStatement();
            
            String sql;
            sql = "SELECT userId FROM LineUserId";
            
            ResultSet rs = stmt.executeQuery(sql);
            
            //SQL Serction
            int tick = 0,allcount = 0,loopwalker = 0;
            while (rs.next())
            {
                if(GUI.release==false)
                    if(loopwalker >= 2)
                    {
                        JOptionPane.showMessageDialog(null, "Dev Mode Breaking Query\nDev mode Send message Succesful");
                        break;
                    }
                String url = "https://api.line.me/v2/bot/message/push";
                //url = URLEncoder.encode(url,"UTF-8");
               

                URL obj = new URL(url);
                HttpsURLConnection conn = (HttpsURLConnection) obj.openConnection();
                GUI.jTextArea2.append("Content-Type:application/json\n");
                GUI.thisgui.repaint(); GUI.thisgui.validate();
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);
                GUI.jTextArea2.append("Method : POST\n");
                GUI.thisgui.repaint(); GUI.thisgui.validate();
                conn.setRequestMethod("POST");

                String basicAuth = "Bearer {Q8BjDP5iRdj+FprSJXxyDk95z358K0355D7rTRTBuubd0z+0XFTanFHD+yPyR7uAIOKtdpbtv99HxxSgm49QEOxmYI/PrA5Hlt3l4TYVVoxPYPYRS4G/7eVMr8YldmlybqUabhXknXephfGwzB1GXgdB04t89/1O/w1cDnyilFU=}";
                GUI.jTextArea2.append("\"Bearer {Q8BjDP5iRdj+********************}\";\n");
                GUI.thisgui.repaint(); GUI.thisgui.validate();
                conn.setRequestProperty("Authorization", basicAuth);

                String line = rs.getString("userId");
                if (line == null)
                {
                    break;
                }
                GUI.jTextArea2.append(line + "\n");
                GUI.thisgui.repaint(); GUI.thisgui.validate();
                String data = null;
                if (messagein.equals(""))
                {
                    messagein = "Announcement Today:";
                    tick++;
                }
                else
                    tick++;
                data = "{\"to\":\"" + line + "\",\"messages\":[{\"type\":\"text\",\"text\":\"" + messagein + "\"}";
                //GUI.jTextArea2.append(data);
                
                if (!(urlin.equals("")))
                {
                    tick++;
                    GUI.jTextArea2.append("Image insertion\n");
                    GUI.thisgui.repaint(); GUI.thisgui.validate();
                    if (!messagein.equals(""))
                    {
                        data = data + ",";
                    }
                    data = data + "{\"type\":\"image\",\"originalContentUrl\":\"";

                    data = data + urlin + "\",";
                    data = data + "\"previewImageUrl\":\"" + urlin + "\"}";
                    //GUI.jTextArea2.append(data);
                } else
                {
                    GUI.jTextArea2.append("Empty Image\n");
                    GUI.thisgui.repaint(); GUI.thisgui.validate();
                }
                data = data + "]}";
                GUI.jTextArea2.append(data.substring(0, 42) + "\n");
                GUI.jTextArea2.append(data.substring(41) + "\n");
                GUI.thisgui.repaint(); GUI.thisgui.validate();
                OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
                GUI.jTextArea2.append("Writing data\n");
                GUI.thisgui.repaint(); GUI.thisgui.validate();
                out.write(data);
                GUI.jTextArea2.append("Close Connection\n");
                GUI.thisgui.repaint(); GUI.thisgui.validate();
                out.close();
                counter++;
                allcount += tick;
                GUI.jLabel8.setText(Integer.toString(allcount));
                loopwalker++;
                new InputStreamReader(conn.getInputStream());
                if (counter % 5 == 0)
                {
                    GUI.jTextArea2.setText("");
                }
                if (tick % 1001 == 0 || tick % 1000 == 0 || tick% 999 == 0 || tick % 998 == 0 || tick % 997 == 0)
                {

                    GUI.jLabel9.setForeground(Color.red);
                    GUI.jLabel9.setText("Reloading");
                    String tmptick = new String(Integer.toString(tick));
                    tick = 0;
                    GUI.jTextArea2.setText("Message route Defect limit at " + tmptick+" message\n Waiting Reload magazine" );
                    final long SEC = 61;
                    GUI.jProgressBar1.setValue(0);
                    for(int i = 0 ; i < SEC ; i++)
                    {
                        GUI.jProgressBar1.setValue((i/61)*100);
                        Thread.sleep(1000);
                        GUI.thisgui.repaint(); GUI.thisgui.validate();
                    }
                    GUI.jProgressBar1.setValue(0);
                    GUI.jLabel9.setForeground(Color.green);
                    GUI.jLabel9.setText("Reloaded");
                    GUI.thisgui.repaint(); GUI.thisgui.validate();
                }
            }

            rs.close();
            stmt.close();
            connection.close();
            GUI.jTextArea2.append("Close SQL DATABASE\n");
            GUI.jTextArea2.setForeground(Color.green);
            GUI.thisgui.repaint(); GUI.thisgui.validate();
            JOptionPane.showMessageDialog(null, "Announcement Successful to send \n");
            allcount = 0;
        } catch (SQLException se)
        {
            GUI.jTextArea2.setForeground(Color.red);
            GUI.jTextArea2.append(se.getMessage());
            
            se.printStackTrace();
            //JOptionPane.showMessageDialog(null, "Error to send \n"+se.getMessage());
        } catch (Exception e)
        {
            GUI.jTextArea2.setForeground(Color.red);
            GUI.jTextArea2.append(e.getMessage());
            e.printStackTrace();
        }
    }

}
