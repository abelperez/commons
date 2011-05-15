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

import java.io.*;               
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mindplex.commons.base.Check.*;
import com.mindplex.commons.base.Clock;

/**
 * 
 * @author Abel Perez
 */
public class FileUtils
{    
    /**
     * Returns {@code true} if the specified file/directory is deleted;
     * otherwise {@code false}.
     * 
     * @param path the file or directory to delete.
     *
     * @return {@code true} if the specified file/directory is deleted;
     * otherwise {@code false}.
     */
    public static boolean delete(String path) {
        return new File(notEmpty(path)).delete();
    }

    /**
     * Returns the file associated with the specified path.  If the path
     * does not exist it will be created.
     * 
     * @param path the directory path.
     * @param create a flag that indicates whether to create the specified
     * directory when not found.
     *
     * @return File a file or directory depending on what the given path
     * points to.
     * 
     * @throws IllegalArgumentException can occur is the specified path is
     * null or empty.
     */
    public static File find(String path, boolean create) {
         
        File file = new File(notEmpty(path));

        // if the given path doesn't exist
        // and the create flag is set to true
        // we create the non-existing directory.
        
        if (! file.exists()) {
            if (create) {
                file.mkdirs();
            }
        }
        return file;
    }

    /**
     * Returns a new directory if the specified directory doesn't already exist;
     * otherwise returns the supplied directory.
     * 
     * @param directory the directory to create.
     *
     * @return a new directory if the specified directory doesn't already exist;
     * otherwise returns the supplied directory.
     */
    public static File makeDir(String directory) {

        File dir = new File(notEmpty(directory));
        if (! dir.exists()) {
            dir.mkdir();
        }
        return dir;
    }
    
    /**
     * Returns {@code true} if the specified directory is created or already
     * exists; otherwise {@code false}.
     *  
     * @param directory the directory to create.
     *
     * @return {@code true} if the specified directory is created or already
     * exists; otherwise {@code false}.
     */
    public static boolean makeDirs(String directory) {

        File file = new File(notEmpty(directory));
        if (! file.exists()) {

            // the specified directory does
            // not exist so we create it and
            // its parent directories.
            return file.mkdir();
        }

        // return true if the specified directory
        // is truly a directory otherwise false.
        return file.isDirectory();
    }

    /**
     * Appends the specified path to the supplied directory. If the specified
     * directory is a file then its returned and no further action is taken.
     * 
     * @param directory the directory to append.
     * @param path the path to append to the supplied directory.
     *
     * @return A directory if the specified path is a directory; otherwise a
     * file if the specified path is a file.
     */
    public static File append(File directory, String path) {

        notNull(directory);
        notEmpty(path);
        
        if (! directory.isDirectory()) {
            // no need to continue if the specified
            // directory is a file.
            return directory;
        }
        
        return makeDir(directory.getAbsolutePath() + File.separator + path);
    }

    public static String combine(String file, String otherFile) {
        return file + File.separator + otherFile;
    }

    /**
     * Returns a list of files that have names that begin with the
     * specified prefix and are contained in the specified directory.
     *
     * @param directory the directory to search in.
     * @param prefix the file name prefix to search for.
     *
     * @return a list of files found in the given directory that are
     * prefixed with given file name prefix.
     */
    public static List<String> prefixSearch(String directory, final String prefix) {

        File dir = new File(notEmpty(directory));

        // lists all the files in the
        // specified directory and filters
        // with this filename filter that
        // effectively checks if the filename
        // starts with the given prefix.

        return Arrays.asList(
            dir.list(new FilenameFilter() {
                public boolean accept(File directory, String name) {
                    return (name.startsWith(prefix));
                }
            }
        ));
    }

    /**
     * Returns a list of files that have names that end with the
     * specified suffix and are contained in the specified directory.
     *
     * @param directory the directory to search in.
     * @param suffix the file name suffix to search for.
     *
     * @return a list of files, found in the given directory, that end
     * with the given file name suffix.
     */
    public static List<String> suffixSearch(String directory, final String suffix) {

        File dir = new File(directory);

        // lists all the files in the
        // specified directory and filters
        // with this filename filter that
        // effectively checks if the filename
        // ends with the given suffix.

        return Arrays.asList(
            dir.list(new FilenameFilter() {
                public boolean accept(File directory, String name) {
                    return (name.endsWith(suffix));
                }
            }
        ));
    }

    /**
     * Reads the contents of the specified file into a {@code String}.
     * If an error is encountered an exception is raised. There is no
     * attempt to try to recover from an error and the caller is fully
     * responsible for handling any exception that might occur.
     * 
     * @param filename the name of the file to read.
     *
     * @return the contents of the specified file into a {@code String}.
     *
     * @throws Exception can occur if there is an error while trying to
     * read the file.
     */
    public static String read(String filename) throws Exception {

        StringBuffer buffer = new StringBuffer();
        BufferedReader in = new BufferedReader(new FileReader(filename));

        String line;
        while ((line = in.readLine()) != null) {
            buffer.append(line);
        }

        in.close();
        return buffer.toString();
    }

    /**
     * Returns a list of text lines from the specified file.
     * 
     * @param filename the file to read.
     *
     * @return a list of text lines from the specified file.
     *
     * @throws Exception can occur if there is an error while trying to
     * read the file.     
     */
    public static List<String> readLines(String filename) throws Exception {

        List<String> lines = new ArrayList<String>();
        BufferedReader in = new BufferedReader(new FileReader(filename));

        String line;
        while ((line = in.readLine()) != null) {
            lines.add(line);
        }

        in.close();
        return lines;
    }

    /**
     * Writes the specified data to the specified file. If an error is
     * encountered an exception is raised. There is no attempt to try
     * to recover from an error and the caller is fully responsible for
     * handling any exception that might occur.  
     *
     * @param data the data to write to the specified file.
     * @param filename the absolute filename of the file to write to.
     * @throws IOException can occur if there is an error while trying to
     * write to the file.
     */
    public static void write(String data, String filename) throws IOException {
        BufferedWriter out = new BufferedWriter(new FileWriter(filename));
        out.write(data);
        out.close();
    }
}