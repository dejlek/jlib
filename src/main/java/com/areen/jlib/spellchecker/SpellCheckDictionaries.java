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
    public static final String KEY = "SpellCheckDictionaries";
    private final ArrayList<SpellDictionaryHashMap> spellCheckDictionaries;
    
    public SpellCheckDictionaries() {
        spellCheckDictionaries = new ArrayList<>();
    }
    
    /**
     * Add dictionary - it gets an array of words and creates SpellDictionaryHashMap
     * @param wordArray
     */
    public void addDicrionary(final String[] wordArray) {
        try {
            SpellDictionaryHashMap dictionary = new SpellDictionaryHashMap();
            for (String word : wordArray) {
                dictionary.addWord(word);
            }
            spellCheckDictionaries.add(dictionary);
        } catch (IOException ex) {
            Logger.getLogger(SpellCheckDictionaries.class.getName()).log(Level.SEVERE, null, ex);
        }
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
}
