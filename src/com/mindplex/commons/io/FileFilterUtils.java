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
import java.io.FileFilter;

import static com.mindplex.commons.base.Check.*;

/**
 *
 * @author Abel Perez
 */
public class FileFilterUtils
{
    /**
     * A {@code FileFilter} that filters for Jar files.
     *
     * @return {@code FileFilter} that filters for Jar files.
     */
    public static FileFilter jarFilter() {
        return new SourceFileFilter(".jar");
    }

    /**
     * A {@code FileFilter} that filters for Java source files.
     *
     * @return {@code FileFilter} that filters for Java source files.
     */
    public static FileFilter javaFilter() {
        return new SourceFileFilter(".java");
    }

    /**
     * A {@code FileFilter} that filters for XML source files.
     *
     * @return {@code FileFilter} that filters for XML source files.
     */
    public static FileFilter xmlFilter() {
        return new SourceFileFilter(".xml");
    }

    /**
     * A {@code FileFilter} that filters for text files.
     *
     * @return {@code FileFilter} that filters for text files.
     */
    public static FileFilter txtFilter() {
        return new SourceFileFilter(".txt");
    }

    /**
     * A general purpose {@code FileFilter} that filters files with the
     * specified file extension.
     *
     * @param extension the file extension to filter for.
     * 
     * @return a general purpose {@code FileFilter}.
     */
    public static FileFilter fileFilter(String extension) {
        return new SourceFileFilter(extension);
    }

    /**
     * A general purpose file filter.
     */
    private static class SourceFileFilter implements FileFilter
    {
        /**
         * The file extension to filter on.
         */
        private String extension;

        /**
         * Constructs this file filter for the specified file extension.
         *
         * @param extension the file extension to filter on.
         */
        SourceFileFilter(String extension) {
            this.extension = extension;
        }

        /**
         * Returns {@code true} if the specified file ends with the file
         * extension this file filter was initialized with.
         * 
         * @param file the file to filter.
         *
         * @return {@code true} if the specified file ends with the target
         * extension; otherwise {@code false}.
         */
        public boolean accept(File file) {
            return notNull(file).getAbsolutePath().endsWith(extension);
        }
    }    
}
