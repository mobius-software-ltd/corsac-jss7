/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.restcomm.protocols.ss7.map.primitives;

import java.util.ArrayList;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class ArrayListSerializingBase<T> {

    private ArrayList<T> data = new ArrayList<T>();
    
    public ArrayListSerializingBase(String elementName, Class<? extends T> classDef, ArrayList<T> data) {
        this.data = data;
        //this.classDef = classDef;
        //this.elementName = elementName;
    }

    public ArrayListSerializingBase(String elementName, Class<? extends T> classDef) {
    	//this.elementName = elementName;
    	//this.classDef = classDef;
    }

    public ArrayList<T> getData() {
        return this.data;
    }
}
