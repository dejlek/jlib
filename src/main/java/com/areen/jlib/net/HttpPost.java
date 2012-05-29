/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.areen.jlib.net;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A class that uses HttpURLConnection to do a HTTP post. 
 * 
 * The main reason for this class is to have a simple solution for uploading files using the PHP file below:
 * 
 * Example:
 * 
 * <pre>
 *  <?php
 *  // In PHP versions earlier than 4.1.0, $HTTP_POST_FILES should be used instead
 *  // of $_FILES.
 *
 *  $uploaddir = '/srv/www/lighttpd/dejan/files/';
 *  $uploadfile = $uploaddir . basename($_FILES['userfile']['name']);
 *
 *  echo '<pre>';
 *  if (move_uploaded_file($_FILES['userfile']['tmp_name'], $uploadfile)) {
 *      echo "File is valid, and was successfully uploaded.\n";
 *      } else {
 *          echo "Possible file upload attack!\n";
 *      }
 *
 *      echo 'Here is some more debugging info:';
 *      print_r($_FILES);
 *
 *      print "</pre>";
 *  }        
 *  ?>
 *
 * </pre>
 * 
 * TODO:
 *   - Add support for arbitrary form fields.
 *   - Add support for more than just one file.
 * 
 * @author dejan
 */
public class HttpPost {
    private final String crlf = "\r\n";
    private URL url;
    private URLConnection urlConnection;
    private OutputStream outputStream;
    private InputStream inputStream;
    private String[] fileNames;
    private String output;
    private String boundary;
    
    public HttpPost(URL argUrl) {
        url = argUrl;
        boundary = "---------------------------4664151417711";
    }
    
    public void setFileNames(String[] argFiles) {
        fileNames = argFiles;
    }
    
    public void post2() {
        URLConnection conn = null;
        OutputStream os = null;
        InputStream is = null;

        try {
            System.out.println("url:" + url);
            conn = url.openConnection();
            conn.setDoOutput(true);

            String postData = "";

            //InputStream imgIs = getClass().getResourceAsStream("/home/dejan/work/ddn-100x46.png");
             
             InputStream imgIs = new FileInputStream("/home/dejan/work/ddn-100x46.png");
             
            byte[] imgData = new byte[imgIs.available()];
            imgIs.read(imgData);

            String message1 = "";
            message1 += "--" + boundary + crlf;
            message1 += "Content-Disposition: form-data; name=\"userfile\"; filename=\"ddn-100x46.png\""
                    + crlf;
            message1 += "Content-Type: image/jpeg" + crlf;
            message1 += crlf;

            // the image is sent between the messages in the multipart message.
            String message2 = "";
            message2 += crlf + "--" + boundary + "--" + crlf;

            conn.setRequestProperty("Content-Type",("multipart/form-data; boundary=" + boundary));
            System.out.println("multipart/form-data; boundary=---------------------------4664151417711");
            // might not need to specify the content-length when sending chunked
            // data.
            System.out.println("Content-Length" + 
                    String.valueOf(message1.length() + message2.length() + imgData.length));
            conn.setRequestProperty("Content-Length", 
                    String.valueOf(message1.length() + message2.length() + imgData.length));

            System.out.println("open os");
            os = conn.getOutputStream();

            System.out.println(message1);
            os.write(message1.getBytes());

            // SEND THE IMAGE
            int index = 0;
            int size = 1024;
            do {
                System.out.println("write:" + index);
                if ((index + size) > imgData.length) {
                    size = imgData.length - index;
                }
                os.write(imgData, index, size);
                index += size;
            } while (index < imgData.length);
            System.out.println("written:" + index);

            System.out.println(message2);
            os.write(message2.getBytes());
            os.flush();

            System.out.println("open is");
            is = conn.getInputStream();

            char buff = 512;
            int len;
            byte[] data = new byte[buff];
            do {
                System.out.println("READ");
                len = is.read(data);

                if (len > 0) {
                    System.out.println(new String(data, 0, len));
                }
            } while (len > 0);

            System.out.println("DONE");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("Close connection");
            try {
                os.close();
            } catch (Exception e) {
                System.out.println(e);
            }
            try {
                is.close();
            } catch (Exception e) {
                System.out.println(e);
            }
            try {

            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }
    public void post() {
        try {
            System.out.println("url:" + url);
            urlConnection = url.openConnection();
            urlConnection.setDoOutput(true);
            //urlConnection.setDoInput(true);
            //urlConnection.setUseCaches(false);
            //urlConnection.setChunkedStreamingMode(1024);
            urlConnection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

            String postData = "";
            String fileName = fileNames[0];
            InputStream fileInputStream = new FileInputStream(fileName);
             
            byte[] fileData = new byte[fileInputStream.available()];
            fileInputStream.read(fileData);

            // ::::: PART 1 :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
            String part1 = "";
            part1 += "--" + boundary + crlf;
            File f = new File(fileNames[0]);
            fileName = f.getName(); // we do not want the whole path, just the name
            part1 += "Content-Disposition: form-data; name=\"userfile\"; filename=\"ddn-100x46.png\"" + crlf;
            
            // CONTENT-TYPE
            // TODO: add proper MIME support here
            if (fileName.endsWith("png")) {
                part1 += "Content-Type: image/png" + crlf;
            } else {
                part1 += "Content-Type: image/jpeg" + crlf;
            }
            
            part1 += crlf;
            System.out.println(part1);
            // File's binary data will be sent after this part

            // ::::: PART 2 :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
            String part2 = crlf + "--" +boundary + "--" + crlf;

            

            System.out.println("Content-Length" 
                    +  String.valueOf(part1.length() + part2.length() + fileData.length));
            urlConnection.setRequestProperty("Content-Length", 
                    String.valueOf(part1.length() + part2.length() + fileData.length));


            // ::::: File send ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
            outputStream = urlConnection.getOutputStream();
            outputStream.write(part1.getBytes());

            int index = 0;
            int size = 1024;
            do {
                System.out.println("write:" + index);
                if ((index + size) > fileData.length) {
                    size = fileData.length - index;
                }
                outputStream.write(fileData, index, size);
                index += size;
            } while (index < fileData.length);
            System.out.println("written:" + index);

            System.out.println(part2);
            outputStream.write(part2.getBytes());
            outputStream.flush();

            // ::::: Download result into the 'output' String :::::::::::::::::::::::::::::::::::::::::::::::
            inputStream = urlConnection.getInputStream();
            
            StringBuilder sb = new StringBuilder();
            char buff = 512;
            int len;
            byte[] data = new byte[buff];
            do {

                len = inputStream.read(data);

                if (len > 0) {
                    sb.append(new String(data, 0, len));
                }
            } while (len > 0);
            output = sb.toString();

            System.out.println("DONE");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("Close connection");
            try {
                outputStream.close();
            } catch (Exception e) {
                System.out.println(e);
            }
            try {
                inputStream.close();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    } // post() method
    
    public String getOutput() {
        return output;
    }
    
    public static void main(String[] args) {

        try {
            HttpPost httpPost = new HttpPost(new URL("http://srl009.adg.internal/dejan/file.php"));
            httpPost.setFileNames(new String[]{ "/home/dejan/work/ddn-100x46.png" });
            httpPost.post();
            System.out.println("=======");
            System.out.println(httpPost.getOutput());
        } catch (MalformedURLException ex) {
            Logger.getLogger(HttpPost.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
} // HttpPost class

// $Id$
