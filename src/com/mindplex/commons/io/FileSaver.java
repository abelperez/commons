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
import java.io.IOException;

import static com.mindplex.commons.base.Check.*;
import com.mindplex.commons.base.Clock;
import com.mindplex.commons.base.Saver;

/**
 * A {@code FileSaver} that saves data to dynamically generated file.
 * 
 * @author Abel Perez
 */
public class FileSaver implements Saver<String, IOException>
{
    /**
     * The base directory this {@code FileSaver} saves files to.
     */
    private String directory;

    /**
     * The canonical filename part this {@code FileSaver} uses in dynamically
     * generated file names. 
     */
    private String filename;

    /**
     * Constructs this {@code FileSaver} based on the specified directory and
     * filename.
     * 
     * @param directory the base directory this saver saves to.
     * @param filename the canonical filename part fo dynamically generated file
     * names.
     */
    private FileSaver(String directory, String filename) {
        this.directory = directory;
        this.filename = filename;
    }

    /**
     * Constructs this {@code FileSaver} based on the specified directory and
     * filename.
     *
     * @param directory the base directory this saver saves to.
     * @param filename the canonical filename part fo dynamically generated file
     * names.
     *
     * @return a new instance of the {@code FileSaver}.
     */
    public static FileSaver of(String directory, String filename) {
        return new FileSaver(notEmpty(directory), notEmpty(filename));    
    }

    /**
     * Returns a dynamic directory name composed of the directory this save was
     * initialized with and the current date. For example, calling this method
     * on January 2nd of 2010, assuming the directory this saver was initialized
     * with is {@code store} yields the value: <em>store/2010-01-02</em>.
     * The result directory will be the absolute directory from the point where
     * this code is executing from.
     *
     * <p>This directory convention results in a clean way of categorizing files.
     * The end result is that all files for a given day are grouped in the same
     * directory.
     * 
     * @see #filename()
     * 
     * @return a dynamic directory name composed of the directory this save was
     * initialized with and the current date.
     */
    public String directory() {
        File base = FileUtils.makeDir(directory);
        File curr = FileUtils.append(base, Clock.today());
        return curr.getAbsolutePath();
    }

    /**
     * Returns a dynamic <em>filename</em> composed of the current time (unixtime)
     * and the filename this file saver was initialized with. For instance,
     * assuming this {@code FileSaver} was initialized with the filename event.xml,
     * the result of this function would look something like this:
     * <tt>1305447712364-event.xml</tt>.
     *
     * <p>This filename convention allows this saver to write multiple files
     * without the need to worry about filename conflicts. As files are written
     * under this convention, you also get a nice chronological sorting of files.
     *
     * @see #directory()
     * 
     * @return a dynamic filename composed of the current time (unix time) and
     * the filename this file saver was initialized with.
     */
    public String filename() {
        return Clock.unixTime() + "-" + filename;
    }

    /**
     * Gets the absolute filename this saver will write to when the {@code save}
     * method is invoked. The directory and filename are dynamically generated
     * at runtime based on the directory and filename this saver was initialized
     * with.  The directory and filename are enhanced with the current date and
     * time in order to generate unique file names.
     * 
     * @see #directory()
     * @see #filename()
     * 
     * @return the absolute filename this saver will write to when the {@code save}
     * method is invoked.
     */
    public String getAbsoluteFilename() {
        return FileUtils.combine(directory(), filename());
    }

    /**
     * Saves the specified data to the persistence store this saver is configured
     * with. Data will be stored in a file with the following convention:
     *
     * <pre>
     * base_directory/YYYY-MM-DD/1305447712364-type.xml
     * </pre>
     *
     * <p>Base directory and type is the directory and filename this saver was
     * initialized with. The year, month, and date combination is the current
     * date and the long sequence of numbers is the current time in unix time.
     * 
     * @param data the data to store.
     *
     * @return {@code true} if the specified data is stored; otherwise {@code false}.
     *
     * @throws IOException can occur while writing the specified data to disk.
     */
    public boolean save(String data) throws IOException {
        FileUtils.write(data, getAbsoluteFilename());
        return true;
    }
}
