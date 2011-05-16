/**
 * Copyright (C) 2011 Mindplex Media, LLC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package com.mindplex.commons.io;

import java.io.File;
import java.util.Observable;

/**
 * A {@code FAileScanner} searched a specified directory for files. Registered
 * observers are notified each time new file is found while scanning.
 * This scanner can also recursively search sub-directories.
 * 
 * @author Abel Perez
 */
public class FileScanner extends Observable
{
    /**
     * Scans a specified directory for files of any type and notifies any
     * registered observers each time a new file is encountered.
     * 
     * @param file the directory or file to scan.
     * @param includeDirectories whether or not to scan recursively.
     */
    public void scan(File file, boolean includeDirectories) {

        if (file.isDirectory()) {
            if (includeDirectories) {

                // found directory, change state and notify
                // observers.
                setChanged();
                notifyObservers(file);
            }

            // recursively scan sub directories.
            String[] files = file.list();
            if (files != null) {
                for (String child : files) {
                    scan(new File(file, child), includeDirectories);
                }
            }
            
        } else {
            // found file, change state and notify
            // observers.
            setChanged();
            notifyObservers(file);
        }
    }
}
