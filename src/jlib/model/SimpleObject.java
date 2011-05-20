package jlib.model;

/**
 * Every class which has an underlying VO (transfer object) should implement this
 * interface. SimpleObject makes sure some VO-related methods are provided.
 * You can say that SimpleObject is a wrapper for its own transfer object (VO).
 * SimpleObject types are most often going to be used as simple models or as a
 * way to obtain/use underlying transfer object as a model...
 *
 * @author dejan
 */
public interface SimpleObject {
    public String[] getTitles();
    public void setTitles(String[] argTitles);
} // SimpleObject interface

// $id$
