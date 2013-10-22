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

package com.areen.jlib.gui.renderer;

import com.areen.jlib.gui.GuiTools;
import com.areen.jlib.gui.VoList;
import com.areen.jlib.gui.model.VoListModel;
import com.areen.jlib.model.SimpleObject;
import com.areen.jlib.util.StringUtility;
import java.awt.Color;
import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;

/**
 * Basic list cell renderer of a ValueObject that inherits from JLabel.
 * 
 * TODO: Perhaps we should use JPanel instead of JLabel, in the case we want to make it more flexible...
 * 
 * @author Dejan
 * @param <S> Type of elements in the JList.
 */
public class VoListCellRenderer<S extends SimpleObject> 
        extends JLabel 
        implements ListCellRenderer {

    String htmlTemplate;
    ImageIcon yesIcon;
    Border border;
    private Color pickedBackground;
    private static final Color DISABLED_BG = new Color(250, 250, 250);
    
    /**
     * Constructs a default renderer object for an item
     * in a list.
     */
    public VoListCellRenderer() {
        super();
        setName("List.cellRenderer");
        setOpaque(true);
        htmlTemplate = "$0";
        yesIcon = new ImageIcon(GuiTools.getResource(VoListCellRenderer.class, 
                "/com/areen/jlib/res/icons/yes.png"));
        border = new MatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY);
        pickedBackground = new Color(200, 200, 255);;
    } // VoListCellRenderer constructor (default)
    
    /**
     * Constructor which takes a HTML template.
     */
    public VoListCellRenderer(String argHtmlTemplate) {
        this();
        setName("List.cellRenderer");
        setOpaque(true);
        htmlTemplate = argHtmlTemplate;
    } // VoListCellRenderer constructor
    
    /**
     * Type-safe getListCellRendererComponent implementation...
     * 
     * Note: it should be something like: getListCellRendererComponent(JList<? extends S> list, S value, 
     *       int index, boolean isSelected, boolean cellHasFocus) , but JList from 1.6 does not take type
     *       parameters...
     * 
     * @param list
     * @param value
     * @param index
     * @param isSelected
     * @param cellHasFocus
     * @return 
     */
    public Component getListCellRendererComponent(JList list, 
            S value, int index, boolean isSelected, boolean cellHasFocus) {
        VoList voList = (VoList) list;
        setComponentOrientation(list.getComponentOrientation());
        
        Color sbg = voList.getSelectionBackground();
        Color sfg = voList.getSelectionForeground();
        Color bg = voList.getBackground();
        Color fg = voList.getForeground();

        if (!list.isEnabled()) {
            sfg = Color.LIGHT_GRAY;
            fg = Color.LIGHT_GRAY;
            bg = DISABLED_BG;
        }
        
        VoListModel listModel = (VoListModel) voList.getModel();
        
        if (isSelected) {
            setBackground(sbg);
            setForeground(sfg);
        } else {
            if (listModel.isPicked(index)) {
                setBackground(pickedBackground);
                setForeground(fg);
            } else {
                setBackground(bg);
                setForeground(fg);
            }
        }

        if (yesIcon == null) {
            System.out.println("WHATTTTT");
        }
        
        setIcon(yesIcon);
        setText(getHtml(value));
        setBorder(border);
        
        return this;
    } // getListCellRendererComponent() method
    
    
    /**
     * Generates HTML code.
     * 
     * @param argSO
     * @return String object which contains HTML code.
     */
    private String getHtml(S argSO) {
        String ret = "<html><body>" + htmlTemplate + "</body></html>";
        
        Object[] fields = new Object[argSO.getNumberOfFields()];
        for (int i = 0; i < fields.length; i++) {
            fields[i] = argSO.get(i);
        } // for
        
        ret = StringUtility.replaceVars(ret, fields);
        
        return ret;
    } // getHtml() method

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, 
            boolean cellHasFocus) {
        // For now, we have to call the method above which is aware of the type S... All this because 
        // in Java 1.6 JList does not take type parameter...
        return getListCellRendererComponent(list, (S) value, index, isSelected, cellHasFocus);
    }

    public String getHtmlTemplate() {
        return htmlTemplate;
    }

    public void setHtmlTemplate(String argHtmlTemplate) {
        htmlTemplate = argHtmlTemplate;
    }

} // VoListCellRenderer class

// $Id$
