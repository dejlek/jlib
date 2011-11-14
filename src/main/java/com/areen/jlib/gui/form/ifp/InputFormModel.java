/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.areen.jlib.gui.form.ifp;

import com.areen.jlib.model.SimpleObject;
import java.util.ArrayList;
import javax.swing.Action;

/**
 *
 * @author mehjabeen
 */
public class InputFormModel <T extends SimpleObject>{
    public T model = null;
    public String title = null;
    public ArrayList<Action> actions = new ArrayList();
}
