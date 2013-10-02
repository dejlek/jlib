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
package com.areen.jlib.pattern.mvc;

import com.areen.jlib.model.AbstractModel;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Code taken from:
 * http://www.oracle.com/technetwork/articles/javase/index-142890.html
 * 
 * The AbstractController class contains two ArrayList objects, which are used to keep track of the models 
 * and views that are registered. Note that whenever a model is registered, the controller also registers 
 * itself as a property change listener on the model. This way, whenever a model changes its state, the 
 * propertyChange() method is called and the controller will pass this event on to the appropriate views.
 * 
 * The final method, setModelProperty(), employs some magic to get its work done. In order to keep the 
 * models completely decoupled from the controller, the code samples in this article have employed the Java 
 * Reflection API. In this case, when this method is called with the desired property name, you hunt through 
 * the registered models to determine which one contains the appropriate method. Once you find it, you 
 * invoke the method using the new value. If the method is not called, the getMethod() will throw a 
 * NoSuchMethodException, which the exception handler ignores, allowing the for loop to continue.
 */
public abstract class AbstractController implements PropertyChangeListener {

    private ArrayList<AbstractViewPanel> registeredViews;
    private ArrayList<AbstractModel> registeredModels;

    public AbstractController() {
        registeredViews = new ArrayList<AbstractViewPanel>();
        registeredModels = new ArrayList<AbstractModel>();
    }

    public void addModel(AbstractModel model) {
        registeredModels.add(model);
        model.addPropertyChangeListener(this);
    }

    public void removeModel(AbstractModel model) {
        registeredModels.remove(model);
        model.removePropertyChangeListener(this);
    }

    public void addView(AbstractViewPanel view) {
        registeredViews.add(view);
    }

    public void removeView(AbstractViewPanel view) {
        registeredViews.remove(view);
    }

    //  Use this to observe property changes from registered models
    //  and propagate them on to all the views.
    public void propertyChange(PropertyChangeEvent evt) {

        for (AbstractViewPanel view : registeredViews) {
            view.modelPropertyChange(evt);
        }
    }

    /**
     * This is a convenience method that subclasses can call upon to fire property changes back to the models.
     * This method uses reflection to inspect each of the model classes to determine whether it is the owner
     * of the property in question. If it isn't, a NoSuchMethodException is thrown, which the method ignores.
     *
     * @param propertyName = The name of the property.
     * @param newValue = An object that represents the new value of the property.
     */
    protected void setModelProperty(String propertyName, Object newValue) {
        for (AbstractModel model : registeredModels) {
            try {
                Method method = model.getClass().getMethod("set" + propertyName, new Class[] {
                    newValue.getClass()
                });
                method.invoke(model, newValue);
            } catch (Exception ex) {
                // TODO:  Handle exception.
            }
        } // foreach
    } // setModelProperty() method

} // AbstractController class
