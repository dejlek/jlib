/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.areen.jlib.test;

import com.areen.jlib.gui.GuiTools;
import com.areen.jlib.gui.LoggingEventQueue;
import com.areen.jlib.tuple.Pair;
import java.awt.EventQueue;
import java.awt.Toolkit;
import javax.swing.UIManager;
import org.apache.log4j.Logger;

/**
 *
 * @author dejan
 */
public class Main {

    public static class CodeAndValue extends Pair<String, String> {
        public CodeAndValue(String argFirst, String argSecond) {
            super(argFirst, argSecond);
        }
    } // CodeAndValue class

    public static final CodeAndValue[] TLDS = new CodeAndValue[] { 
        new CodeAndValue("aero" , "Air transport")
        , new CodeAndValue("asia" , "Organisations and individuals in the Asia-Pacific region")
        , new CodeAndValue("biz" , "Business")
        , new CodeAndValue("cat" , "Catalan")
        , new CodeAndValue("com" , "Commercial organizations")
        , new CodeAndValue("coop" , "Cooperatives")
        , new CodeAndValue("edu" , "Educational establishments")
        , new CodeAndValue("gov" , "US government")
        , new CodeAndValue("info" , "Informational sites")
        , new CodeAndValue("int" , "international organizations")
        , new CodeAndValue("jobs" , "Employment-related sites")
        , new CodeAndValue("mil" , "US military")
        , new CodeAndValue("mobi" , "Mobile")
        , new CodeAndValue("museum" , "Museums")
        , new CodeAndValue("name" , "Families and individuals")
        , new CodeAndValue("net" , "Network")
        , new CodeAndValue("org" , "Non-profit organizations")
        , new CodeAndValue("pro" , "Profession")
        , new CodeAndValue("tel" , "Telephone")
        , new CodeAndValue("travel" , "Travel")
        , new CodeAndValue("ac" , "Ascension Island")
        , new CodeAndValue("ad" , "Andorra")
        , new CodeAndValue("ae" , "United Arab Emirates")
        , new CodeAndValue("af" , "Afghanistan")
        , new CodeAndValue("ag" , "Antigua and Barbuda")
        , new CodeAndValue("ai" , "Anguilla")
        , new CodeAndValue("al" , "Albania")
        , new CodeAndValue("am" , "Armenia")
        , new CodeAndValue("an" , "Netherlands Antilles")
        , new CodeAndValue("ao" , "Angola")
        , new CodeAndValue("aq" , "Antarctica")
        , new CodeAndValue("ar" , "Argentina")
        , new CodeAndValue("as" , "American Samoa")
        , new CodeAndValue("at" , "Austria")
        , new CodeAndValue("au" , "Australia")
        , new CodeAndValue("aw" , "Aruba")
        , new CodeAndValue("ax" , "Åland Islands")
        , new CodeAndValue("az" , "Azerbaijan")
        , new CodeAndValue("ba" , "Bosnia and Herzegovina")
        , new CodeAndValue("bb" , "Barbados")
        , new CodeAndValue("bd" , "Bangladesh")
        , new CodeAndValue("be" , "Belgium")
        , new CodeAndValue("bf" , "Burkina Faso")
        , new CodeAndValue("bg" , "Bulgaria")
        , new CodeAndValue("bh" , "Bahrain")
        , new CodeAndValue("bi" , "Burundi")
        , new CodeAndValue("bj" , "Benin")
        , new CodeAndValue("bl" , "Saint Barthélemy")
        , new CodeAndValue("bm" , "Bermuda")
        , new CodeAndValue("bn" , "Brunei")
        , new CodeAndValue("bo" , "Bolivia")
        , new CodeAndValue("br" , "Brazil")
        , new CodeAndValue("bs" , "Bahamas")
        , new CodeAndValue("bt" , "Bhutan")
        , new CodeAndValue("bv" , "Bouvet Island")
        , new CodeAndValue("bw" , "Botswana")
        , new CodeAndValue("by" , "Belarus")
        , new CodeAndValue("bz" , "Belize")
        , new CodeAndValue("ca" , "Canada")
        , new CodeAndValue("cc" , "Cocos (Keeling) Islands")
        , new CodeAndValue("cd" , "Democratic Republic of the Congo")
        , new CodeAndValue("cf" , "Central African Republic")
        , new CodeAndValue("cg" , "Republic of the Congo")
        , new CodeAndValue("ch" , "Switzerland")
        , new CodeAndValue("ci" , "Côte d'Ivoire")
        , new CodeAndValue("ck" , "Cook Islands")
        , new CodeAndValue("cl" , "Chile")
        , new CodeAndValue("cm" , "Cameroon")
        , new CodeAndValue("cn" , "People's Republic of China")
        , new CodeAndValue("co" , "Colombia")
        , new CodeAndValue("cr" , "Costa Rica")
        , new CodeAndValue("cu" , "Cuba")
        , new CodeAndValue("cv" , "Cape Verde")
        , new CodeAndValue("cx" , "Christmas Island")
        , new CodeAndValue("cy" , "Cyprus")
        , new CodeAndValue("cz" , "Czech Republic")
        , new CodeAndValue("de" , "Germany")
        , new CodeAndValue("dj" , "Djibouti")
        , new CodeAndValue("dk" , "Denmark")
        , new CodeAndValue("dm" , "Dominica")
        , new CodeAndValue("do" , "Dominican Republic")
        , new CodeAndValue("dz" , "Algeria")
        , new CodeAndValue("ec" , "Ecuador")
        , new CodeAndValue("ee" , "Estonia")
        , new CodeAndValue("eg" , "Egypt")
        , new CodeAndValue("eh" , "Western Sahara")
        , new CodeAndValue("er" , "Eritrea")
        , new CodeAndValue("es" , "Spain")
        , new CodeAndValue("et" , "Ethiopia")
        , new CodeAndValue("eu" , "European Union")
        , new CodeAndValue("fi" , "Finland")
        , new CodeAndValue("fj" , "Fiji")
        , new CodeAndValue("fk" , "Falkland Islands")
        , new CodeAndValue("fm" , "Federated States of Micronesia")
        , new CodeAndValue("fo" , "Faroe Islands")
        , new CodeAndValue("fr" , "France")
        , new CodeAndValue("ga" , "Gabon")
        , new CodeAndValue("gb" , "United Kingdom")
        , new CodeAndValue("gd" , "Grenada")
        , new CodeAndValue("ge" , "Georgia")
        , new CodeAndValue("gf" , "French Guiana")
        , new CodeAndValue("gg" , "Guernsey")
        , new CodeAndValue("gh" , "Ghana")
        , new CodeAndValue("gi" , "Gibraltar")
        , new CodeAndValue("gl" , "Greenland")
        , new CodeAndValue("gm" , "Gambia")
        , new CodeAndValue("gn" , "Guinea")
        , new CodeAndValue("gp" , "Guadeloupe")
        , new CodeAndValue("gq" , "Equatorial Guinea")
        , new CodeAndValue("gr" , "Greece")
        , new CodeAndValue("gs" , "South Georgia and the South Sandwich Islands")
        , new CodeAndValue("gt" , "Guatemala")
        , new CodeAndValue("gu" , "Guam")
        , new CodeAndValue("gw" , "Guinea-Bissau")
        , new CodeAndValue("gy" , "Guyana")
        , new CodeAndValue("hk" , "Hong Kong")
        , new CodeAndValue("hm" , "Heard Island and McDonald Islands")
        , new CodeAndValue("hn" , "Honduras")
        , new CodeAndValue("hr" , "Croatia")
        , new CodeAndValue("ht" , "Haiti")
        , new CodeAndValue("hu" , "Hungary")
        , new CodeAndValue("id" , "Indonesia")
        , new CodeAndValue("ie" , "Ireland")
        , new CodeAndValue("il" , "Israel")
        , new CodeAndValue("im" , "Isle of Man")
        , new CodeAndValue("in" , "India")
        , new CodeAndValue("io" , "British Indian Ocean Territory")
        , new CodeAndValue("iq" , "Iraq")
        , new CodeAndValue("ir" , "Iran")
        , new CodeAndValue("is" , "Iceland")
        , new CodeAndValue("it" , "Italy")
        , new CodeAndValue("je" , "Jersey")
        , new CodeAndValue("jm" , "Jamaica")
        , new CodeAndValue("jo" , "Jordan")
        , new CodeAndValue("jp" , "Japan")
        , new CodeAndValue("ke" , "Kenya")
        , new CodeAndValue("kg" , "Kyrgyzstan")
        , new CodeAndValue("kh" , "Cambodia")
        , new CodeAndValue("ki" , "Kiribati")
        , new CodeAndValue("km" , "Comoros")
        , new CodeAndValue("kn" , "Saint Kitts and Nevis")
        , new CodeAndValue("kp" , "North Korea")
        , new CodeAndValue("kr" , "South Korea")
        , new CodeAndValue("kw" , "Kuwait")
        , new CodeAndValue("ky" , "Cayman Islands")
        , new CodeAndValue("kz" , "Kazakhstan")
        , new CodeAndValue("la" , "Laos")
        , new CodeAndValue("lb" , "Lebanon")
        , new CodeAndValue("lc" , "Saint Lucia")
        , new CodeAndValue("li" , "Liechtenstein")
        , new CodeAndValue("lk" , "Sri Lanka")
        , new CodeAndValue("lr" , "Liberia")
        , new CodeAndValue("ls" , "Lesotho")
        , new CodeAndValue("lt" , "Lithuania")
        , new CodeAndValue("lu" , "Luxembourg")
        , new CodeAndValue("lv" , "Latvia")
        , new CodeAndValue("ly" , "Libya")
        , new CodeAndValue("ma" , "Morocco")
        , new CodeAndValue("mc" , "Monaco")
        , new CodeAndValue("md" , "Moldova")
        , new CodeAndValue("me" , "Montenegro")
        , new CodeAndValue("mg" , "Madagascar")
        , new CodeAndValue("mh" , "Marshall Islands")
        , new CodeAndValue("mk" , "Republic of Macedonia")
        , new CodeAndValue("ml" , "Mali")
        , new CodeAndValue("mm" , "Myanmar")
        , new CodeAndValue("mn" , "Mongolia")
        , new CodeAndValue("mo" , "Macau")
        , new CodeAndValue("mp" , "Northern Mariana Islands")
        , new CodeAndValue("mq" , "Martinique")
        , new CodeAndValue("mr" , "Mauritania")
        , new CodeAndValue("ms" , "Montserrat")
        , new CodeAndValue("mt" , "Malta")
        , new CodeAndValue("mu" , "Mauritius")
        , new CodeAndValue("mv" , "Maldives")
        , new CodeAndValue("mw" , "Malawi")
        , new CodeAndValue("mx" , "Mexico")
        , new CodeAndValue("my" , "Malaysia")
        , new CodeAndValue("mz" , "Mozambique")
        , new CodeAndValue("na" , "Namibia")
        , new CodeAndValue("nc" , "New Caledonia")
        , new CodeAndValue("ne" , "Niger")
        , new CodeAndValue("nf" , "Norfolk Island")
        , new CodeAndValue("ng" , "Nigeria")
        , new CodeAndValue("ni" , "Nicaragua")
        , new CodeAndValue("nl" , "Netherlands")
        , new CodeAndValue("no" , "Norway")
        , new CodeAndValue("np" , "Nepal")
        , new CodeAndValue("nr" , "Nauru")
        , new CodeAndValue("nu" , "Niue")
        , new CodeAndValue("nz" , "New Zealand")
        , new CodeAndValue("om" , "Oman")
        , new CodeAndValue("pa" , "Panama")
        , new CodeAndValue("pe" , "Peru")
        , new CodeAndValue("pf" , "French Polynesia")
        , new CodeAndValue("pg" , "Papua New Guinea")
        , new CodeAndValue("ph" , "Philippines")
        , new CodeAndValue("pk" , "Pakistan")
        , new CodeAndValue("pl" , "Poland")
        , new CodeAndValue("pm" , "Saint Pierre and Miquelon")
        , new CodeAndValue("pn" , "Pitcairn Islands")
        , new CodeAndValue("pr" , "Puerto Rico")
        , new CodeAndValue("ps" , "Palestine")
        , new CodeAndValue("pt" , "Portugal")
        , new CodeAndValue("pw" , "Palau")
        , new CodeAndValue("py" , "Paraguay")
        , new CodeAndValue("qa" , "Qatar")
        , new CodeAndValue("re" , "Réunion")
        , new CodeAndValue("ro" , "Romania")
        , new CodeAndValue("rs" , "Serbia")
        , new CodeAndValue("ru" , "Russia")
        , new CodeAndValue("rw" , "Rwanda")
        , new CodeAndValue("sa" , "Saudi Arabia")
        , new CodeAndValue("sb" , "Solomon Islands")
        , new CodeAndValue("sc" , "Seychelles")
        , new CodeAndValue("sd" , "Sudan")
        , new CodeAndValue("se" , "Sweden")
        , new CodeAndValue("sg" , "Singapore")
        , new CodeAndValue("sh" , "Saint Helena")
        , new CodeAndValue("si" , "Slovenia")
        , new CodeAndValue("sj" , "Svalbard and Jan Mayen")
        , new CodeAndValue("sk" , "Slovakia")
        , new CodeAndValue("sl" , "Sierra Leone")
        , new CodeAndValue("sm" , "San Marino")
        , new CodeAndValue("sn" , "Senegal")
        , new CodeAndValue("so" , "Somalia")
        , new CodeAndValue("sr" , "Suriname")
        , new CodeAndValue("st" , "São Tomé and Príncipe")
        , new CodeAndValue("su" , "Soviet Union")
        , new CodeAndValue("sv" , "El Salvador")
        , new CodeAndValue("sy" , "Syria")
        , new CodeAndValue("sz" , "Swaziland")
        , new CodeAndValue("tc" , "Turks and Caicos Islands")
        , new CodeAndValue("td" , "Chad")
        , new CodeAndValue("tf" , "French Southern Territories")
        , new CodeAndValue("tg" , "Togo")
        , new CodeAndValue("th" , "Thailand")
        , new CodeAndValue("tj" , "Tajikistan")
        , new CodeAndValue("tk" , "Tokelau")
        , new CodeAndValue("tl" , "East Timor")
        , new CodeAndValue("tm" , "Turkmenistan")
        , new CodeAndValue("tn" , "Tunisia")
        , new CodeAndValue("to" , "Tonga")
        , new CodeAndValue("tp" , "East Timor")
        , new CodeAndValue("tr" , "Turkey")
        , new CodeAndValue("tt" , "Trinidad and Tobago")
        , new CodeAndValue("tv" , "Tuvalu")
        , new CodeAndValue("tw" , "Taiwan")
        , new CodeAndValue("tz" , "Tanzania")
        , new CodeAndValue("ua" , "Ukraine")
        , new CodeAndValue("ug" , "Uganda")
        , new CodeAndValue("uk" , "United Kingdom")
        , new CodeAndValue("um" , "US Minor Outlying Islands")
        , new CodeAndValue("us" , "United States")
        , new CodeAndValue("uy" , "Uruguay")
        , new CodeAndValue("uz" , "Uzbekistan")
        , new CodeAndValue("va" , "Vatican City")
        , new CodeAndValue("vc" , "Saint Vincent and the Grenadines")
        , new CodeAndValue("ve" , "Venezuela")
        , new CodeAndValue("vg" , "British Virgin Islands")
        , new CodeAndValue("vi" , "United States Virgin Islands")
        , new CodeAndValue("vn" , "Vietnam")
        , new CodeAndValue("vu" , "Vanuatu")
        , new CodeAndValue("wf" , "Wallis and Futuna")
        , new CodeAndValue("ws" , "Samoa")
        , new CodeAndValue("ye" , "Yemen")
        , new CodeAndValue("yt" , "Mayotte")
        , new CodeAndValue("yu" , "Yugoslavia")
        , new CodeAndValue("za" , "South Africa")
        , new CodeAndValue("zm" , "Zambia")
        , new CodeAndValue("zw" , "Zimbabwe")
        , new CodeAndValue("zz" , "A wise man is superior to any insults which can be put upon him, and " 
            + "the best reply to unseemly behavior is patience and moderation")
    };

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            //Tell the UIManager to use the platform look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            //UIManager.setLookAndFeel(new WindowsLookAndFeel());

            /*
            com.jgoodies.looks.windows.WindowsLookAndFeel
            com.jgoodies.looks.plastic.PlasticLookAndFeel
            com.jgoodies.looks.plastic.Plastic3DLookAndFeel
            com.jgoodies.looks.plastic.PlasticXPLookAndFeel
            */
            //UIManager.setLookAndFeel("com.jgoodies.looks.plastic.Plastic3DLookAndFeel");
            
        } catch (Exception e) {
            //Do nothing
        }

        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                FcbTestFrame mainFrame = new FcbTestFrame(Main.TLDS);
                GuiTools.setParentComponent(mainFrame);
                mainFrame.setLocationRelativeTo(null);
                mainFrame.setVisible(true);
                
                Logger.getLogger("com.areen.table").info("BLA");
                //if (Logger.getLogger("com.areen.table").isDebugEnabled()) {
                    System.out.println("DEBUG ENABLED");
                    EventQueue queue = Toolkit.getDefaultToolkit().getSystemEventQueue();
                    queue.push(new LoggingEventQueue("com.areen.table", mainFrame));
                //}
            }
        }); // invokeLater()
    } // main() method
} // Main class
