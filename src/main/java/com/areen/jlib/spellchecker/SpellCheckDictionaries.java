/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.areen.jlib.spellchecker;

import com.swabunga.spell.engine.SpellDictionaryHashMap;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class to hold many different spelling spellCheckDictionaries,
 * methods to add them either from string[] or a file
 * @author Matthew
 */
public class SpellCheckDictionaries {

    /**
     * Look-up key to be used in the maps to retrieve this object
     */
    public static final String KEY = "SpellCheckDictionaries";
    private final ArrayList<SpellDictionaryHashMap> spellCheckDictionaries;
    private SpellDictionaryHashMap userDictionary;

    public SpellCheckDictionaries() {
        spellCheckDictionaries = new ArrayList<>();
    }
    
    /**
     * Add dictionary - it gets an array of words and creates SpellDictionaryHashMap
     * @param wordArray
     */
    public void addDictionary(final String[] wordArray, boolean argUserDictionary) {
        try {
            SpellDictionaryHashMap dictionary = new SpellDictionaryHashMap();
            for (String word : wordArray) {
                dictionary.addWord(word);
            }
            if (argUserDictionary) {
                userDictionary = dictionary;
            } else {
                spellCheckDictionaries.add(dictionary);
            }
        } catch (IOException ex) {
            Logger.getLogger(SpellCheckDictionaries.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Create SpellDicitonaryHashMap from the array of words
     * @param wordArray 
     */
    public SpellDictionaryHashMap createDictionary(final String[] wordArray) {
        try {
            SpellDictionaryHashMap dictionary = new SpellDictionaryHashMap();
            for (String word : wordArray) {
                dictionary.addWord(word);
            }
        } catch (IOException ex) {
            Logger.getLogger(SpellCheckDictionaries.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }
    
    /**
     * Add dictionary as a file
     * @param f 
     */
    public void addDictionary(File f) {
        try {
            SpellDictionaryHashMap dictionary = new SpellDictionaryHashMap(f);
            spellCheckDictionaries.add(dictionary);
        } catch (IOException ex) {
            Logger.getLogger(SpellCheckDictionaries.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Get spellCheckDictionaries
     * @return 
     */
    public ArrayList<SpellDictionaryHashMap> getDictionaries() {
        return spellCheckDictionaries;
    }
    
    /**
     * Sets user dictionary
     * @param argUserDictionary 
     */
    public void setUserDictionary(SpellDictionaryHashMap argUserDictionary) {
        userDictionary = argUserDictionary;
    }
    
    
    /**
     * Get user dictionary
     * @return 
     */
    public SpellDictionaryHashMap getUserDictionary() {
        return userDictionary;
    }
}
