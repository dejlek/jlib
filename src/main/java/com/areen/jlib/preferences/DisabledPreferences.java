/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.areen.jlib.preferences;

import java.util.prefs.AbstractPreferences;
import java.util.prefs.BackingStoreException;

/**
 * Copyright 2003-2011 Robert Slifka
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * A do-nothing Preferences implementation so that we can avoid the hassles
 * of the JVM Preference implementations.
 *
 * Usage:
 *   java -Djava.util.prefs.PreferencesFactory=DisabledPreferencesFactory ...
 * 
 * @author Robert Slifka (robert.slifka@gmail.com)
 */
public class DisabledPreferences extends AbstractPreferences {

    public DisabledPreferences() {
        super(null, "");
    }

    @Override
    protected void putSpi(String key, String value) {

    }

    @Override
    protected String getSpi(String key) {
        return null;
    }

    @Override
    protected void removeSpi(String key) {

    }

    @Override
    protected void removeNodeSpi() throws BackingStoreException {

    }

    @Override
    protected String[] keysSpi() throws BackingStoreException {
        return new String[0];
    }

    @Override
    protected String[] childrenNamesSpi()
            throws BackingStoreException {
        return new String[0];
    }

    @Override
    protected AbstractPreferences childSpi(String name) {
        return null;
    }

    @Override
    protected void syncSpi() throws BackingStoreException {

    }

    @Override
    protected void flushSpi() throws BackingStoreException {

    }
}

// $Id$ 
