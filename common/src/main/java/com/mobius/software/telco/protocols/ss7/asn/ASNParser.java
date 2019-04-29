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
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNDecode;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNEncode;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNExclude;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNLength;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNWildcard;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNBitString;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNBoolean;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNGeneric;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNIA5String;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNNull;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNObjectIdentifier;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNUTF8String;

public class ASNParser 
{
	private ConcurrentHashMap<ASNHeader,Class<?>> classMapping=new ConcurrentHashMap<ASNHeader,Class<?>>();
	private ConcurrentHashMap<String,ParserClassData> cachedElements=new ConcurrentHashMap<String,ParserClassData>();
	private Boolean skipErrors=false;
	
	public ASNParser(Boolean skipErrors) {
		clear();
		this.skipErrors=skipErrors;		
	}
	public ASNParser() {
		clear();
	}
	
	private void loadPrimitives() {
		this.loadClass(ASNBitString.class);
		this.loadClass(ASNBoolean.class);
		this.loadClass(ASNEnumerated.class);
		this.loadClass(ASNIA5String.class);
		this.loadClass(ASNInteger.class);
		this.loadClass(ASNNull.class);
		this.loadClass(ASNObjectIdentifier.class);
		this.loadClass(ASNOctetString.class);
		this.loadClass(ASNUTF8String.class);	
	}
	
	public void clear() {
		classMapping.clear();
		cachedElements.clear();
		loadPrimitives();
	}
	
	public void loadClass(Class<?> newClass) {
		ASNTag tag=newClass.getAnnotation(ASNTag.class);
		if(tag==null)
			throw new RuntimeException("only classes annotated with ASNTag annotation are supported");
		
		try
		{
			newClass.getConstructor();
		}
		catch(Exception ex) {
			throw new RuntimeException("only classes with empty constructor are supported");			
		}
		
		ASNHeader newHeader=new ASNHeader(tag,tag.asnClass(),tag.tag(),tag.constructed(),null);
		if(classMapping.containsKey(newHeader))
			throw new RuntimeException("class with this ASNTag already registered");
		
		classMapping.put(newHeader, newClass);
	}
	
	public ASNDecodeResult decode(ByteBuf buffer)  throws ASNException {
		return decode(buffer,skipErrors);
	}
	
	public ASNDecodeResult decode(ByteBuf buffer, Boolean skipErrors) throws ASNException {
		try {
			return decode(buffer,skipErrors, null, classMapping,cachedElements,null);
		}
		catch(Exception ex) {
			ex.printStackTrace();
			throw new ASNException(ex.getMessage());
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static DecodeResult decode(ByteBuf buffer,Boolean skipErrors,Field wildcardField, ConcurrentHashMap<ASNHeader,Class<?>> classMapping,ConcurrentHashMap<String,ParserClassData> cachedElements,Integer index) throws ASNException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, InstantiationException {
		int oldIndex=buffer.readerIndex();
		buffer.markReaderIndex();
		ASNHeaderWithLength header=readHeader(buffer);
		ASNHeader currHeader=new ASNHeader(header.getAsnClass(), header.getIsConstructed(), header.getAsnTag(), header.getIndefiniteLength(),index);
		Class<?> effectiveClass=classMapping.get(currHeader);
		if(effectiveClass==null) {
			currHeader=new ASNHeader(header.getAsnClass(), header.getIsConstructed(), header.getAsnTag(), header.getIndefiniteLength(),null);
			effectiveClass=classMapping.get(currHeader);
			if(effectiveClass==null) {
				if(wildcardField!=null) {
					header.setLength(header.getLength() + buffer.readerIndex()-oldIndex);
					buffer.resetReaderIndex();
					if(wildcardField.getType().isAssignableFrom(List.class) && !wildcardField.getType().equals(Object.class)) {
						Type[] innerTypes = ((ParameterizedType) wildcardField.getGenericType()).getActualTypeArguments();
						effectiveClass=(Class<?>)innerTypes[0];
					}
					else
						effectiveClass=wildcardField.getType();
				} else {
					if(skipErrors) {
						if(buffer.readableBytes()>=header.getLength())
							buffer.skipBytes(header.getLength());
						else
							buffer.skipBytes(buffer.readableBytes());
						
						return new DecodeResult(null,header.getAsnClass(), header.getAsnTag(), header.getIsConstructed(),true);
					}
					else
						throw new ASNException("no class found for matching tag:" + currHeader.getAsnClass() + "," + currHeader.getAsnTag() + "," + currHeader.getIsConstructed() + "," + currHeader.getIndefiniteLength());
				}
			}
		}
		
		ParserClassData cachedData=cachedElements.get(effectiveClass.getCanonicalName());
		if(cachedData==null) {
			cachedData=processField(effectiveClass,cachedElements);			
		}
		
		Constructor<?> ctor = effectiveClass.getConstructor();
		Object currObject = ctor.newInstance(new Object[] {  });
		Boolean hadErrors=false;
		if(!cachedData.getSubFieldsFound()) {
			Method[] methods=effectiveClass.getMethods();
			for(Method method:methods) {
				ASNDecode asnDecode=method.getAnnotation(ASNDecode.class);
				if(asnDecode!=null) {
					hadErrors|=(Boolean)method.invoke(currObject,new Object[] { buffer.slice(buffer.readerIndex(), header.getLength()), new Boolean(skipErrors) });
					buffer.skipBytes(header.getLength());
				}
			}
		}
		else {
			int remainingBytes=buffer.readableBytes();
			remainingBytes-=header.getLength();
			int innerIndex=0;
			while(buffer.readableBytes()>remainingBytes) {
				DecodeResult innerValue=decode(buffer,skipErrors, cachedData.getWildcardField(), cachedData.getInnerMap(),cachedElements,innerIndex);
				hadErrors|=innerValue.getHadErrors();
				
				if(innerValue!=null && innerValue.getResult()!=null) {
					ASNTag innerTag=innerValue.getResult().getClass().getAnnotation(ASNTag.class);
					ASNHeader innerHeader=new ASNHeader(innerTag, innerValue.realClass, innerValue.realTag,innerValue.realConstructed,innerIndex);
					Field f=cachedData.getFieldsMapElement(innerHeader);
					if(f==null) {
						innerHeader=new ASNHeader(innerTag, innerValue.realClass, innerValue.realTag,innerValue.realConstructed,null);
						f=cachedData.getFieldsMapElement(innerHeader);
						if(f==null)
							f=cachedData.getWildcardField();
					}
					
					if(f.getType().isAssignableFrom(List.class) && !f.getType().equals(Object.class)) {
						f.setAccessible(true);
						List innerList=(List)f.get(currObject);					
						if(innerList==null) {
							innerList=new ArrayList();
							f.set(currObject, innerList);
						}
						
						innerList.add(innerValue.getResult());
					}
					else {
						f.setAccessible(true);
						f.set(currObject, innerValue.getResult());
					}
					
					innerIndex++;
				}
			}
		}
		
		if(header.getIndefiniteLength()) {
			//lets read end of content
			buffer.readByte();
			buffer.readByte();
		}
		
		return new DecodeResult(currObject,header.getAsnClass(), header.getAsnTag(), header.getIsConstructed(), hadErrors);
	}
	
	private static ASNHeaderWithLength readHeader(ByteBuf buffer) throws ASNException {
		byte currData=buffer.readByte();
		ASNClass asnClass=ASNClass.fromInt((currData>>6) & 0x03);
		boolean constructed=(currData & 0x20)!=0;
		int asnTag=currData & 0x1F;
		if(asnTag==0x1F) {
			asnTag=0;
			Boolean gotEOF=false;
			
			while(buffer.readableBytes()>0 && !gotEOF)
			{
				currData=buffer.readByte();
				if((currData & 0x80)==0)
					gotEOF=true;
				
				asnTag=((asnTag<<7) | (currData & 0x7F));				
			}
			
			if(!gotEOF)
				throw new ASNException("Invalid tag encoding found");
		}
				
		currData=buffer.readByte();
		int length=0;
		boolean indefiniteLength=false;
		if((currData & 0x080)==0x080 && currData !=-128) {
			int lengthLength=currData & 0x7F;
			for(int i=0;i<lengthLength;i++)
				length=(length<<8) | (buffer.readByte() & 0x0FF);						
		}
		else if(currData!=-128)
			length=currData;
		else
		{
			length=currData & 0x7F;
			if(length==0 && buffer.readableBytes()>0) {
				indefiniteLength=true;
				buffer.markReaderIndex();
				Boolean previousWasZero=false;
				Boolean gotEOF=false;
				while(!gotEOF && buffer.readableBytes()>0) {
					length++;
					if(buffer.readByte()!=0x00) {
						previousWasZero=false;
					} else {
						if(previousWasZero) {
							length-=2;
							gotEOF=true;
						}
						else
							previousWasZero=true;
					}
				}
				
				if(!gotEOF) {					
					//throw new ASNException("Invalid length encoding found");
					length=0;
					indefiniteLength=false;
				}
				
				buffer.resetReaderIndex();				
			}
		}				
		
		return new ASNHeaderWithLength(asnClass, constructed, asnTag, indefiniteLength, null,length);
	}
	
	public ByteBuf encode(Object value) throws ASNException {
		ASNTag tag=value.getClass().getAnnotation(ASNTag.class);
		if(tag==null)
			throw new RuntimeException("only entities annotated with ASNTag annotation are supported");

		try
		{
			ParserClassData cachedData=cachedElements.get(value.getClass().getCanonicalName());
			if(cachedData==null) {
				cachedData=processField(value.getClass(),cachedElements);			
			}
			
			int completeLength=getLengthWithHeader(null, value, cachedElements);
			int length=getLength(cachedData,value,cachedElements);		
			ByteBuf buffer=Unpooled.buffer(completeLength);
			encode(buffer,value,tag,tag.asnClass(), tag.tag(),tag.constructed(), length, cachedElements);
			return buffer;
		}
		catch(Exception ex) {	
			throw new ASNException(ex.getMessage());
		}
	}
	
	public void encode(ByteBuf buffer, Object value) throws ASNException {
		ASNTag tag=value.getClass().getAnnotation(ASNTag.class);
		if(tag==null)
			throw new RuntimeException("only entities annotated with ASNTag annotation are supported");

		try
		{
			ParserClassData cachedData=cachedElements.get(value.getClass().getCanonicalName());
			if(cachedData==null) {
				cachedData=processField(value.getClass(),cachedElements);			
			}
			
			int length=getLength(cachedData,value,cachedElements);		
			encode(buffer,value,tag,tag.asnClass(), tag.tag(),tag.constructed(), length, cachedElements);			
		}
		catch(Exception ex) {			
			throw new ASNException(ex.getMessage());
		}
	}
	
	private void encode(ByteBuf buffer,Object value,ASNTag asnTag, ASNClass realClass, Integer realTag,Boolean realConstructed,Integer length,ConcurrentHashMap<String,ParserClassData> cachedElements) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, ASNException {
		encodeTagAndHeader(buffer,asnTag,realClass,realTag,realConstructed);
		if(!asnTag.lengthIndefinite())
			encodeLength(buffer,false,length);
		else
			encodeLength(buffer,true,0);
		
		encodeWithoutHeader(buffer, value, cachedElements);
		
		if(asnTag.lengthIndefinite()) {
			buffer.writeByte(0x00);
			buffer.writeByte(0x00);
		}
	}
	
	@SuppressWarnings("rawtypes")
	private void encodeWithoutHeader(ByteBuf buffer,Object value,ConcurrentHashMap<String,ParserClassData> cachedElements) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, ASNException {
		//encode content
		ParserClassData cachedData=cachedElements.get(value.getClass().getCanonicalName());
		if(cachedData==null) {
			cachedData=processField(value.getClass(),cachedElements);			
		}
		
		for(int i=0;i<cachedData.getFields().size();i++) {
			cachedData.getFields().get(i).setAccessible(true);
			if(cachedData.getFields().get(i).getType().isAssignableFrom(List.class) && !cachedData.getFields().get(i).getType().equals(Object.class)) {
				List innerList=(List)cachedData.getFields().get(i).get(value);
				for(Object innerObject:innerList) {
					ASNTag innerTag=innerObject.getClass().getAnnotation(ASNTag.class);
					if(innerObject!=null) {
						int realInnerTag=innerTag.tag();
						ASNClass realAsnClass=innerTag.asnClass();
						Boolean realConstructed=innerTag.constructed();
						ASNProperty property=cachedData.getFields().get(i).getAnnotation(ASNProperty.class);
						if(property!=null) {
							realInnerTag=property.tag();
							realAsnClass=property.asnClass();
							realConstructed=property.constructed();
						}
						
						ParserClassData innerCachedData=cachedElements.get(innerObject.getClass().getCanonicalName());
						if(innerCachedData==null) {
							innerCachedData=processField(innerObject.getClass(),cachedElements);			
						}
						
						encode(buffer, innerObject, innerTag, realAsnClass, realInnerTag, realConstructed, getLength(innerCachedData, innerObject, cachedElements),cachedElements);
					}
				}
			}
			else {
				Object innerValue=cachedData.getFields().get(i).get(value);
				ASNTag innerTag=cachedData.getFields().get(i).getType().getAnnotation(ASNTag.class);
				if(innerValue!=null) {
					int realInnerTag=innerTag.tag();
					ASNClass realAsnClass=innerTag.asnClass();
					Boolean realConstructed=innerTag.constructed();
					ASNProperty property=cachedData.getFields().get(i).getAnnotation(ASNProperty.class);
					if(property!=null) {
						realInnerTag=property.tag();
						realAsnClass=property.asnClass();
						realConstructed=property.constructed();
					}
					
					ParserClassData innerCachedData=cachedElements.get(innerValue.getClass().getCanonicalName());
					if(innerCachedData==null) {
						innerCachedData=processField(innerValue.getClass(),cachedElements);			
					}
					
					encode(buffer, innerValue, innerTag, realAsnClass, realInnerTag, realConstructed, getLength(innerCachedData, innerValue, cachedElements),cachedElements);
				}
			}
		}
		
		if(cachedData.getWildcardField()!=null) {
			cachedData.getWildcardField().setAccessible(true);
			if(cachedData.getWildcardField().getType().isAssignableFrom(List.class) && !cachedData.getWildcardField().getType().equals(Object.class)) {
				List innerList=(List)cachedData.getWildcardField().get(value);
				for(Object innerObject:innerList) {
					encodeWithoutHeader(buffer, innerObject, cachedElements);
				}
			}
			else {
				Object innerValue=cachedData.getWildcardField().get(value);
				if(innerValue!=null)
					encodeWithoutHeader(buffer, innerValue, cachedElements);
			}
		}
		
		if(!cachedData.getSubFieldsFound()) {
			Method[] methods=value.getClass().getMethods();
			for(Method method:methods) {
				ASNEncode asnEncode=method.getAnnotation(ASNEncode.class);
				if(asnEncode!=null) {
					method.invoke(value,new Object[] { buffer});
				}
			}
		}		
	}
	
	private static Integer getLengthWithHeader(Integer realTag,Object value,ConcurrentHashMap<String,ParserClassData> cachedElements) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, ASNException {
		Integer length=0;
		if(value==null)
			return length;
		
		ASNTag asnTag=value.getClass().getAnnotation(ASNTag.class);
		//calculating itself header
		if(asnTag!=null) {
			ParserClassData cachedData=cachedElements.get(value.getClass().getCanonicalName());
			if(cachedData==null) {
				cachedData=processField(value.getClass(),cachedElements);			
			}
			
			//get internal content/subtags length
			length=getLength(cachedData, value, cachedElements);
			
			if(asnTag.lengthIndefinite())
				//2 bytes end of tag and one length
				length+=3;
			else
				length+=getLengthLength(length);
			
			if(realTag!=null)
				length+=getTagLength(realTag)+1;
			else
				length+=getTagLength(asnTag.tag())+1;
		}
		
		return length;
	}
	
	public Integer getLength(Object value) throws ASNException {
		ParserClassData cachedData=cachedElements.get(value.getClass().getCanonicalName());
		if(cachedData==null) {
			cachedData=processField(value.getClass(),cachedElements);			
		}
		
		try {
			return getLengthWithHeader(null, value, cachedElements);			
		}
		catch(Exception ex) {
			throw new ASNException(ex.getMessage());
		}
	}
	
	@SuppressWarnings("rawtypes")
	private static Integer getLength(ParserClassData parserData,Object value,ConcurrentHashMap<String,ParserClassData> cachedElements) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, ASNException {
		Integer length=0;
		for(int i=0;i<parserData.getFields().size();i++) {
			parserData.getFields().get(i).setAccessible(true);
			
			Integer realTag=null;
			ASNProperty asnProperty=parserData.getFields().get(i).getAnnotation(ASNProperty.class);
			if(asnProperty!=null)
				realTag=asnProperty.tag();
			
			if(parserData.getFields().get(i).getType().isAssignableFrom(List.class) && !parserData.getFields().get(i).getType().equals(Object.class)) {
				List innerList= (List)parserData.getFields().get(i).get(value);
				for(Object innerValue:innerList) {
					length+=getLengthWithHeader(realTag, innerValue,cachedElements);
				}
			}
			else {
				length+=getLengthWithHeader(realTag, parserData.getFields().get(i).get(value),cachedElements);
			}
		}
		
		if(parserData.getWildcardField()!=null) {
			parserData.getWildcardField().setAccessible(true);
			if(parserData.getWildcardField().getType().isAssignableFrom(List.class) && !parserData.getWildcardField().getType().equals(Object.class)) {
				List innerList= (List)parserData.getWildcardField().get(value);
				for(Object listItem:innerList) {
					ParserClassData cachedData=cachedElements.get(listItem.getClass().getCanonicalName());
					if(cachedData==null) {
						cachedData=processField(listItem.getClass(),cachedElements);			
					}
					
					length+=getLength(cachedData, listItem,cachedElements);
				}
			}
			else {
				Object innerValue=parserData.getWildcardField().get(value);
				if(innerValue!=null) {
					ParserClassData cachedData=cachedElements.get(innerValue.getClass().getCanonicalName());
					if(cachedData==null) {
						cachedData=processField(innerValue.getClass(),cachedElements);			
					}
					
					//get internal content/subtags length
					length+=getLength(cachedData, innerValue, cachedElements);
				}			
			}		
		}
		
		if(!parserData.getSubFieldsFound()) {
			Method[] methods=value.getClass().getMethods();
			for(Method method:methods) {
				ASNLength asnLength=method.getAnnotation(ASNLength.class);
				if(asnLength!=null) {
					return (Integer)method.invoke(value);
				}
			}
		}
		else
			return length;
		
		throw new ASNException("Length can not be calculated for class:" + value.getClass().getCanonicalName());
	}
	
	private static Integer getTagLength(Integer tag) {
		if(tag<31)
			return 0;
			
		Integer testValue=new Integer(tag);
		int result=0;
		while(testValue>0)
		{
			result+=1;
			testValue=((testValue>>7) & Integer.MAX_VALUE);			
		}
		
		if(result==0)
			result=1;
		
		return result+1;
	}
	
	public static Integer getLengthLength(Integer value) {
		if(value<128)
			return 1;
		
		Integer testValue=new Integer(value);
		int result=0;
		while(testValue>0)
		{
			result+=1;
			testValue=((testValue>>8) & 0x0FFFFFFF);			
		}
		
		if(result==0)
			result=1;
		
		return result;
	}
	
	private static void encodeTagAndHeader(ByteBuf buffer, ASNTag asnTag, ASNClass realClass,Integer realTag,Boolean realConstructed) {
		byte header;
		if(realTag<31)
		{
			if(realConstructed)
				header=(byte)(((realClass.getValue()<<6) & 0xC0) | 0x20 | (realTag & 0x1F));
			else
				header=(byte)(((realClass.getValue()<<6) & 0xC0) | (realTag & 0x1F));
			
			buffer.writeByte(header);
		}
		else
		{
			if(realConstructed)
				header=(byte)(((realClass.getValue()<<6) & 0xC0) | 0x3F);
			else
				header=(byte)(((realClass.getValue()<<6) & 0xC0) | 0x1F);
			
			buffer.writeByte(header);
			
			Integer testValue=new Integer(realTag);
			while(testValue>0)
			{
				int currByte=testValue& 0x7F;
				testValue=((testValue>>7) & Integer.MAX_VALUE);
				if(testValue==0)
					buffer.writeByte(currByte);
				else
					buffer.writeByte(0x80 | currByte);
			}
		}
	}
	
	public static void encodeLength(ByteBuf buffer, Boolean lengthIndefinite,Integer value) {
		if(lengthIndefinite)
			buffer.writeByte((byte)0x80);
		else if(value<128)
			buffer.writeByte(value);
		else
		{
			int totalBytes=getLengthLength(value);
			buffer.writeByte(0x80 | totalBytes);
			
			byte[] data=new byte[totalBytes];
			for(int i=0,size=(totalBytes-1)*8;i<data.length;i++,size-=8)
				buffer.writeByte((value>>size)&0xFF);
		}		
	}
	
	private static ParserClassData processField(Class<?> effectiveClass,ConcurrentHashMap<String,ParserClassData> cachedElements) {
		ParserClassData cachedData=cachedElements.get(effectiveClass.getCanonicalName());
		if(cachedData==null) {
			Class<?> currentPath=effectiveClass;
			List<Class<?>> classesPath=new ArrayList<Class<?>>();
			do {
				classesPath.add(0,currentPath);
				currentPath=currentPath.getSuperclass();				
			}
			while(currentPath!=null);
			
			List<Field> annotatedFields=new ArrayList<Field>();
			Field wildcardField=null;
			for(Class<?> currentClass:classesPath)
			{
				Field[] fields=currentClass.getDeclaredFields();
				for(int i=0;i<fields.length;i++) {
					ASNExclude excludeTag=fields[i].getAnnotation(ASNExclude.class);
					if(excludeTag==null) {
						ASNWildcard wildcardTag=fields[i].getAnnotation(ASNWildcard.class);
						if(wildcardTag!=null) {
							wildcardField=fields[i];
						} else {
							ASNTag innerTag = null;

							if(fields[i].getType().isAssignableFrom(List.class) && !fields[i].getType().equals(Object.class)) {
								Type[] innerTypes = ((ParameterizedType) fields[i].getGenericType()).getActualTypeArguments();
								if(innerTypes.length==1) {
									innerTag=((Class<?>)innerTypes[0]).getAnnotation(ASNTag.class);
								}
							}
							else if(fields[i].getType().isAssignableFrom(ASNGeneric.class) && !fields[i].getType().equals(Object.class)) {
								innerTag=((Class<?>)((ParameterizedType)effectiveClass.getGenericSuperclass()).getActualTypeArguments()[0]).getAnnotation(ASNTag.class);
							}	
							else {
								innerTag=fields[i].getType().getAnnotation(ASNTag.class);							
							}
							
							if(innerTag!=null) {
								annotatedFields.add(fields[i]);
							}
						}
					}
				}
			}
			
			cachedData=new ParserClassData(annotatedFields,wildcardField);
			storeFields(effectiveClass,cachedData);
			cachedElements.put(effectiveClass.getCanonicalName(), cachedData);			
		}
		
		return cachedData;
	}
	
	private static void storeFields(Class<?> effectiveClass,ParserClassData cachedData) {
		for(int i=0;i<cachedData.getFields().size();i++) {
			ASNTag innerTag=null;
			Class<?> realType=cachedData.getFields().get(i).getType();
			if(cachedData.getFields().get(i).getType().isAssignableFrom(List.class) && !cachedData.getFields().get(i).getType().equals(Object.class)) {
				Type[] innerTypes = ((ParameterizedType) cachedData.getFields().get(i).getGenericType()).getActualTypeArguments();
				if(innerTypes.length==1) {
					realType=((Class<?>)innerTypes[0]);
					innerTag=realType.getAnnotation(ASNTag.class);
				}
			}
			else if(cachedData.getFields().get(i).getType().isAssignableFrom(ASNGeneric.class) && !cachedData.getFields().get(i).getType().equals(Object.class)) {
				realType=((Class<?>)((ParameterizedType)effectiveClass.getGenericSuperclass()).getActualTypeArguments()[0]);
				innerTag=((Class<?>)((ParameterizedType)effectiveClass.getGenericSuperclass()).getActualTypeArguments()[0]).getAnnotation(ASNTag.class);
			}	
			else {
				innerTag=realType.getAnnotation(ASNTag.class);					
			}
			
			int realInnerTag=innerTag.tag();
			ASNClass realClass=innerTag.asnClass();
			Boolean realConstructed=innerTag.constructed();
			Integer index=null;
			ASNProperty property=cachedData.getFields().get(i).getAnnotation(ASNProperty.class);
			if(property!=null)
			{
				realInnerTag=property.tag();
				realClass=property.asnClass();
				realConstructed=property.constructed();
				if(property.index()>=0)
					index=property.index();
			}
				
			ASNHeader asnHeader=new ASNHeader(innerTag, realClass, realInnerTag,realConstructed,index);
			cachedData.addInnerMapElement(asnHeader, realType);
			cachedData.addFiledsMapElement(asnHeader, cachedData.getFields().get(i));
		}
	}
	
	private static class DecodeResult extends ASNDecodeResult
	{
		private Integer realTag;
		private ASNClass realClass;
		private Boolean realConstructed;
		
		private DecodeResult(Object result,ASNClass realClass,Integer realTag,Boolean realConstructed,Boolean hadErrors) {
			super(result,hadErrors);
			this.realClass=realClass;
			this.realTag=realTag;
			this.realConstructed=realConstructed;
		}
	}
}