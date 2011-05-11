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

package com.mindplex.commons.base;

/**
 * A function that can be applied to a specific input.
 *  
 * @author Abel Perez
 */
public interface Function<T, E extends Exception>
{
    /**
     * Executes this function against the specified {@code input}.
     *  
     * @param input this functions input.
     * 
     * @throws E can occur if the specified input is in a state that is not
     * acceptable by this function.
     */
    public void execute(T input) throws E;
}