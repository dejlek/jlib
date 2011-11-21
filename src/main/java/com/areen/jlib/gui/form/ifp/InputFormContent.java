/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.areen.jlib.gui.form.ifp;

import com.areen.jlib.model.SimpleObject;
import javax.swing.JPanel;

/**
 * Every class which has an underlying VO (transfer object) should implement this
 * interface. SimpleObject makes sure some VO-related methods are provided.
 * You can say that SimpleObject is a wrapper for its own transfer object (VO).
 * SimpleObject types are most often going to be used as simple models or as a
 * way to obtain/use underlying transfer object as a model...
 *
 * @author dejan
 */
public class InputFormContent<T extends SimpleObject> extends JPanel {
    public InputFormContent(T argSo) { }
    public T getModel() {
        return null;
    }
    public void setModel(T argSo) { }
} // SimpleObject interface

// $Id: SimpleObject.java 65 2011-07-12 09:47:52Z dejan $
