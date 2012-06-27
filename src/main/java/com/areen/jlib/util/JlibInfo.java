/**
 * Project: jlib 
 * Version: $Id: ApplicationInfo.java 239 2012-03-01 17:22:07Z dejan $ 
 * License: SPL
 *
 * This file is best viewed with 110 columns.
 * 34567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789
 * 
 * Authors (in chronological order): 
 *   Dejan Lekic - http://dejan.lekic.org
 * Contributors (in chronological order): 
 *   -
 */
package com.areen.jlib.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
 
/**
 * Use this class to keep information about the application.
 *
 * @author dejan
 */
public class JlibInfo {
    /************************************************************************
     * Versioning Nomenclature
     *
     * General Format : major.minor[.patch[.build]]
     * 
     *   major  : major version number
     *   minor  : minor version number
     *   patch  : patch number (currently always 0 at present)
     *   build  : SVN Global Branch Revision
     * Versioning Nomenclature
     ************************************************************************/
    private static String prjVersion = "";
    private static String patchNumber = "";
    private static String svnRevision = "";
    private static String verMajor = "";
    private static String verMinor = "";
    private static String jlibVersion = "";

    public JlibInfo() throws IOException {
        Properties versionInfo = new Properties();
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("jlibversion.properties");
        if (in != null) {
            try {
                versionInfo.load(in);

                                    // Initialise parameters from Properties file
                prjVersion = versionInfo.getProperty("prjVersion");
                patchNumber = versionInfo.getProperty("patchNumber");
                svnRevision = versionInfo.getProperty("svnRevision");
                verMajor = prjVersion.split("\\.")[0];
                verMinor = prjVersion.split("\\.")[1];
                jlibVersion = verMajor + "." + verMinor + "." + patchNumber + "." + svnRevision;
            } catch (IOException e) {
                System.err.println("Error loading Jlib Properties file.");
            }

            // release resource (will get tidied up on exit of constructor anyway)
            in.close();
        }
    }

    public static String getMajor() {
        return verMajor;
    }

    public static String getMinor() {
        return verMinor;
    }

    public static String getPatchNumber() {
        return patchNumber;
    }

    public static String getSVNRevision() {
        return svnRevision;
    }

    public static String getVersion() {
        return jlibVersion;
    }

} // JlibInfo class

// $Id: ApplicationInfo.java 239 2012-03-01 17:22:07Z dejan $
