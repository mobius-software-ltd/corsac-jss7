package com.mobius.software.telco.protocols.ss7.asn;

/*
 * Mobius Software LTD
 * Copyright 2019, Mobius Software LTD and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

/**
*
* @author yulian oifa
*
*/

import java.lang.reflect.Field;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ParserClassData {
	Boolean subFieldsFound=false;
	private List<Field> fields;
	private Field wildcardField;
	
	ConcurrentHashMap<ASNHeader,Class<?>> innerMap=new ConcurrentHashMap<ASNHeader,Class<?>>();
	ConcurrentHashMap<ASNHeader,Field> fieldsMap=new ConcurrentHashMap<ASNHeader,Field>();
	
	public ParserClassData(List<Field> fields,Field wildcardField) {
		this.fields=fields;
		if(this.fields!=null && this.fields.size()>0)
			this.subFieldsFound=true;
		else
			this.subFieldsFound=false;
		
		this.wildcardField=wildcardField;
		if(this.wildcardField!=null)
			this.subFieldsFound=true;
	}
	
	public List<Field> getFields() {
		return fields;
	}	
	
	public Boolean getSubFieldsFound() {
		return this.subFieldsFound;
	}
	
	public void addInnerMapElement(ASNHeader header,Class<?> clazz) {
		innerMap.put(header,clazz);
	}
	
	public ConcurrentHashMap<ASNHeader,Class<?>> getInnerMap() {
		return innerMap;
	}
	
	public void addFiledsMapElement(ASNHeader header,Field field) {
		fieldsMap.put(header, field);
	}
	
	public Field getFieldsMapElement(ASNHeader header) {
		return fieldsMap.get(header);
	}
	
	public Field getWildcardField() {
		return wildcardField;
	}
}