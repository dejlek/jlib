/**
 * Project: jlib
 * Version: $Id$
 * License: SPL
 * 
 * Authors (in chronological order):
 *   Dejan Lekic - http://dejan.lekic.org
 * Contributors (in chronological order):
 *   -
 */

package com.areen.jlib.net;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The purpose of this class is to be used to generate a secure web URLs that can be used for automatic
 * authentication and authorization.
 * 
 * @author Dejan
 */
public class SecureWebURL {
    // ====================================================================================================
    // ==== Variables =====================================================================================
    // ====================================================================================================
    
    // :::::: PRIVATE/PROTECTED :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    
    private String baseURL;
    private String userID;
    private String userEmail;
    private String password;
    private String sessionID; // clientID + sessionID. Example: "apc" + "/" + "3247" = apc/3247
                              // Not in use at the moment.
    // ====================================================================================================
    // ==== Constructors ==================================================================================
    // ====================================================================================================
    
    public SecureWebURL(String argBaseURL) {
        baseURL = argBaseURL;
    }
    
    // ====================================================================================================
    // ==== Public Methods ================================================================================
    // ====================================================================================================
    
    /**
     * By using this constructor we assume that develop will first use accessors to set userID, password and
     * e-mail address before asking for the URL or SHA-1 hash.
     * 
     * @return
     * @throws MalformedURLException 
     */
    public URL getURL() throws MalformedURLException {
        String what = baseURL.contains("?") ? "&" : "?";
        URL ret = new URL(baseURL + what + "rlo=" + userID + "&code=" + getHexSHA1());
        return ret;
    } // getURL() method
    
    /**
     * Same as above, but here we supply everything.
     * 
     * @param argUserID
     * @param argEmail
     * @param argPassword
     * @return
     * @throws MalformedURLException 
     */
    public URL getURL(String argUserID, String argEmail, String argPassword) throws MalformedURLException {
        setUserID(argUserID);
        setEmail(argEmail);
        setPassword(argPassword);
        
        return getURL();
    } // getURL() method
    
    // ====================================================================================================
    // ==== Accessors =====================================================================================
    // ====================================================================================================
    
    public void setUserID(String argUserID) {
        userID = argUserID;
    }
    
    public String getUserID() {
        return userID;
    }
    
    public void setEmail(String argEmail) {
        userEmail = argEmail;
    }

    public String getEmail() {
        return userEmail;
    }
    
    public String getBaseURL() {
        return baseURL;
    }

    public void setBaseURL(String argBaseURL) {
        baseURL = argBaseURL;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String argPassword) {
        password = argPassword;
    }
    
    
    // ====================================================================================================
    // ==== Private/Protected/Package Methods =============================================================
    // ====================================================================================================
    
    private String getSharedSecret() {
        String ret = userID + password + userEmail;
        //System.out.println("shared secret: " + ret);
        return ret;
    } // getSharedSecret() method
    
    /**
     * Calculates the SHA1 digest and converts it to a String object with hexadecimal digits.
     * 
     * @return String object in hexadecimal format representing the SHA1 digest, or NULL if something goes
     *         wrong.
     */
    private String getHexSHA1() {
        String ret = null;
        String sharedSecret = getSharedSecret();
        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            try {
                crypt.update(sharedSecret.getBytes("UTF-8"));
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(SecureWebURL.class.getName()).log(Level.SEVERE, null, ex);
            }
            ret = new BigInteger(1, crypt.digest()).toString(16);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(SecureWebURL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ret;
    } // getHexSHA1() method
    
    // ====================================================================================================
    // ==== Main function =================================================================================
    // ====================================================================================================
    
	public static void main(String[] args) {
        // #1 - No arguments in the baseURL
		SecureWebURL swu = new SecureWebURL("https://www.areen-online.co.uk/staffnet/hash_test.asp");
        swu.setUserID("LED01");
        swu.setPassword("blahblah");
        swu.setEmail("dejan.lekic@areen.com");
        System.out.println(swu.getHexSHA1());
        System.out.println(swu.getHexSHA1().length());
        
        try {
            System.out.println(swu.getURL());
        } catch (MalformedURLException ex) {
            Logger.getLogger(SecureWebURL.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // #2 - We already have some arguments in the URL
        swu.setBaseURL("https://www.areen-online.co.uk/staffnet/hash_test.asp?something=42");
        try {
            System.out.println(swu.getURL());
        } catch (MalformedURLException ex) {
            Logger.getLogger(SecureWebURL.class.getName()).log(Level.SEVERE, null, ex);
        }
	} // main() function
    
} // SecureWebURL class

// $Id$
