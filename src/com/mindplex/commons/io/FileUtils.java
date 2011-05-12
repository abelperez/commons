package com.mindplex.commons.io;

import java.io.*;
import java.util.Arrays;
import java.util.List;

import static com.mindplex.commons.base.Check.*;

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
     * Returns a list of files that have names that begin with the
     * specified prefix and are contained in the specified directory.
     *
     * @param directory the directory to search in.
     * @param prefix the file name prefix to search for.
     *
     * @return a list of files found in the given directory that are
     * prefixed withe given file name prefix.
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
     * Retrieve all the file names that end with the given String.
     *
     * @param directoryName
     * @param suffix
     * @return List
     */
    public static List<String> suffixSearch(String directoryName, final String suffix) {

        File dir = new File(directoryName);

        return Arrays.asList(dir.list(new FilenameFilter() {
            public boolean accept(File directory, String name) {
                return (name.endsWith(suffix));
            }
        }));
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
     * Writes the specified data to the specified file. If an error is
     * encountered an exception is raised. There is no attempt to try
     * to recover from an error and the caller is fully responsible for
     * handling any exception that might occur.  
     *
     * @param data the data to write to the specified file.
     * @param filename the absolute filename of the file to write to.
     * @throws Exception can occur if there is an error while trying to
     * write to the file.
     */
    public static void write(String data, String filename) throws Exception {
        BufferedWriter out = new BufferedWriter(new FileWriter(filename));
        out.write(data);
        out.close();
    }
}