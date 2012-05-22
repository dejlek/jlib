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
    private static final String prjVersion = "1.0_Beta5-SNAPSHOT";
    private static final String patchNumber = "0";
    private static final String svnRevision = "394M";
    private static final String verMajor = prjVersion.split("\\.")[0];
    private static final String verMinor = prjVersion.split("\\.")[1];
    private static final String jlibVersion = verMajor + "." + verMinor + "." + patchNumber + "." + svnRevision;

    public JlibInfo() {
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
