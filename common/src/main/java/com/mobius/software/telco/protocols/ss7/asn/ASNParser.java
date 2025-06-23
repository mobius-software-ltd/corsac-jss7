package com.mobius.software.telco.protocols.ss7.asn;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNChoise;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNDecode;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNEncode;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNExclude;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNLength;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNPostprocess;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNPreprocess;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNWildcard;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNWrappedTag;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNDecodeException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
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

public class ASNParser
{
	protected Logger logger = LogManager.getLogger(ASNParser.class);

	public static Integer MAX_DEPTH = 1000;

	private Class<?> defaultClass;
	private ConcurrentHashMap<ASNHeader, Class<?>> rootClassMapping = new ConcurrentHashMap<ASNHeader, Class<?>>();
	private ConcurrentHashMap<String, ParserClassData> cachedElements = new ConcurrentHashMap<String, ParserClassData>();
	private ConcurrentHashMap<String, ASNParser> innerParser = new ConcurrentHashMap<String, ASNParser>();

	private ConcurrentHashMap<LocalMappingKey, Class<?>> localClassesMapping = new ConcurrentHashMap<LocalMappingKey, Class<?>>();
	private ConcurrentHashMap<String, Class<?>> defaultLocalClassesMapping = new ConcurrentHashMap<String, Class<?>>();

	private Boolean skipErrors = false;
	private ASNParser parentParser;
	private ASNDecodeHandler handler;

	public ASNParser(Class<?> defaultClass, Boolean skipErrors, Boolean loadDefaultPrimitives)
	{
		this.defaultClass = defaultClass;
		clear(loadDefaultPrimitives);
		this.skipErrors = skipErrors;
	}

	public ASNParser(Boolean skipErrors)
	{
		clear(true);
		this.skipErrors = skipErrors;
	}

	public ASNParser()
	{
		clear(true);
	}

	protected ASNParser(ASNParser parentParser)
	{
		this.parentParser = parentParser;
		if (this.parentParser.handler != null)
			this.handler = this.parentParser.handler;

		loadPrimitives();
	}

	public void setDecodeHandler(ASNDecodeHandler handler)
	{
		this.handler = handler;
	}

	public void clearDecodeHandler()
	{
		this.handler = null;
	}

	private void loadPrimitives()
	{
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

	public void clear(Boolean loadDefaultPrimitives)
	{
		rootClassMapping.clear();
		cachedElements.clear();
		if (loadDefaultPrimitives)
			loadPrimitives();
	}

	public ASNParser getParser(Class<?> rootClazz)
	{
		if (parentParser != null)
			return parentParser.getParser(rootClazz);

		ASNParser parser = innerParser.get(rootClazz.getCanonicalName());
		if (parser == null)
		{
			parser = new ASNParser(this);
			ASNParser oldParser = innerParser.putIfAbsent(rootClazz.getCanonicalName(), parser);
			if (oldParser != null)
				parser = oldParser;
		}

		return parser;
	}

	public ASNParser getParser(Class<?> rootClazz, Class<?> defaultClass)
	{
		if (parentParser != null)
			return parentParser.getParser(rootClazz);

		ASNParser parser = innerParser.get(rootClazz.getCanonicalName());
		if (parser == null)
		{
			parser = new ASNParser(this);
			parser.defaultClass = defaultClass;
			ASNParser oldParser = innerParser.putIfAbsent(rootClazz.getCanonicalName(), parser);
			if (oldParser != null)
				parser = oldParser;
		}

		return parser;
	}

	public void clearClassMapping(Class<?> rootClazz)
	{
		getParser(rootClazz).clear(true);
	}

	public void registerAlternativeClassMapping(Class<?> rootClazz, Class<?> clazz)
	{
		getParser(rootClazz).replaceClass(clazz);
	}

	public void registerLocalMapping(Class<?> rootClazz, Object key, Class<?> clazz)
	{
		if (parentParser != null)
		{
			parentParser.registerLocalMapping(rootClazz, key, clazz);
			return;
		}

		LocalMappingKey localKey = new LocalMappingKey(key, rootClazz.getCanonicalName());
		localClassesMapping.put(localKey, clazz);
	}

	public Class<?> getLocalMapping(Class<?> rootClazz, Object key)
	{
		if (parentParser != null)
			return parentParser.getLocalMapping(rootClazz, key);

		LocalMappingKey localKey = new LocalMappingKey(key, rootClazz.getCanonicalName());
		return localClassesMapping.get(localKey);
	}

	public void registerDefaultLocalMapping(Class<?> rootClazz, Class<?> clazz)
	{
		if (parentParser != null)
		{
			parentParser.registerDefaultLocalMapping(rootClazz, clazz);
			return;
		}

		defaultLocalClassesMapping.put(rootClazz.getCanonicalName(), clazz);
	}

	public Class<?> getDefaultLocalMapping(Class<?> rootClazz)
	{
		if (parentParser != null)
			return parentParser.getDefaultLocalMapping(rootClazz);

		return defaultLocalClassesMapping.get(rootClazz.getCanonicalName());
	}

	public void loadClass(Class<?> newClass)
	{
		ASNTag tag = newClass.getAnnotation(ASNTag.class);
		if (tag == null)
		{
			loadWrappedClass(newClass, false);
			return;
		}

		try
		{
			newClass.getConstructor();
		}
		catch (Exception ex)
		{
			throw new RuntimeException("only classes with empty constructor are supported");
		}

		ASNHeader newHeader = new ASNHeader(tag, tag.asnClass(), tag.tag(), tag.constructed(), null);
		if (rootClassMapping.containsKey(newHeader))
			throw new RuntimeException("class with this ASNTag already registered");

		rootClassMapping.put(newHeader, newClass);
	}

	public void replaceClass(Class<?> newClass)
	{
		ASNTag tag = newClass.getAnnotation(ASNTag.class);
		if (tag == null)
		{
			loadWrappedClass(newClass, true);
			return;
		}

		try
		{
			newClass.getConstructor();
		}
		catch (Exception ex)
		{
			throw new RuntimeException("only classes with empty constructor are supported");
		}

		ASNHeader newHeader = new ASNHeader(tag, tag.asnClass(), tag.tag(), tag.constructed(), null);
		rootClassMapping.put(newHeader, newClass);
	}

	private void loadWrappedClass(Class<?> newClass, boolean replace)
	{
		ASNWrappedTag tag = newClass.getAnnotation(ASNWrappedTag.class);
		if (tag == null)
			return;

		try
		{
			newClass.getConstructor();
		}
		catch (Exception ex)
		{
			throw new RuntimeException("only classes with empty constructor are supported");
		}

		ParserClassData cachedData = cachedElements.get(newClass.getCanonicalName());
		if (cachedData == null)
			cachedData = processField(newClass, cachedElements);

		ConcurrentHashMap<ASNHeader, FieldData> fieldsMap = cachedData.getFieldsMap();
		Iterator<Entry<ASNHeader, FieldData>> iterator = fieldsMap.entrySet().iterator();
		// in cassed we have wrapped at this level for this class we should clear all
		// the items first
		// rootClassMapping.clear();
		while (iterator.hasNext())
		{
			ASNHeader newHeader = iterator.next().getKey();
			if (!replace && rootClassMapping.containsKey(newHeader))
				throw new RuntimeException("class with this ASNTag already registered");

			rootClassMapping.put(newHeader, newClass);
		}
	}

	public ASNDecodeResult decode(ByteBuf buffer) throws ASNException
	{
		return decode(buffer, new ConcurrentHashMap<Integer, Object>(), skipErrors, 0);
	}

	public ASNDecodeResult decode(ByteBuf buffer, ConcurrentHashMap<Integer, Object> mappedData, Boolean skipErrors, Integer level) throws ASNException
	{
		try
		{
			return decode(null, buffer, skipErrors, null, null, rootClassMapping, cachedElements, null, mappedData, defaultClass, level + 1);
		}
		catch (ASNException ex)
		{
			throw ex;
		}
		catch (Exception ex)
		{
			throw new ASNException(ex.getMessage());
		}
	}

	public void merge(Object first, Object second)
	{
		Class<?> effectiveClass = first.getClass();
		ParserClassData cachedData = cachedElements.get(effectiveClass.getCanonicalName());
		if (cachedData == null)
			cachedData = processField(effectiveClass, cachedElements);

		for (FieldData currField : cachedData.getFields())
			try
			{
				if (currField.getField().isAccessible() && currField.getField().get(second) != null)
					currField.getField().set(first, currField.getField().get(second));
			}
			catch (Exception ex)
			{

			}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private DecodeResult decode(Object parent, ByteBuf buffer, Boolean skipErrors, Field wildcardField, ConcurrentHashMap<ASNHeader, FieldData> fieldsMap, ConcurrentHashMap<ASNHeader, Class<?>> classMapping, ConcurrentHashMap<String, ParserClassData> cachedElements, Integer index, ConcurrentHashMap<Integer, Object> mappedData, Class<?> defaultClass, Integer level) throws ASNException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, InstantiationException
	{
		int oldIndex = buffer.readerIndex();	
		int oldWriterIndex = buffer.writerIndex();
		buffer.markReaderIndex();
		buffer.markWriterIndex();
		ASNHeaderWithLength header = readHeader(buffer);
		if(header.getIndefiniteLength()!=null && header.getIndefiniteLength())
			buffer.writerIndex(buffer.readerIndex() + header.getLength());
		
		if (level.equals(MAX_DEPTH))
		{
			buffer.readerIndex(0);
			try
			{
				logger.warn("we have reached max level for ASN Parser, the buffer content is " + toHex(buffer));
			}
			catch (Exception ex)
			{
				logger.warn("we have reached max level for ASN Parser, can not print buffer content!!!");
			}

			buffer.resetReaderIndex();
			buffer.markReaderIndex();

			if (skipErrors)
			{
				if (buffer.readableBytes() >= header.getLength())
					buffer.skipBytes(header.getLength());
				else
					buffer.skipBytes(buffer.readableBytes());

				return new DecodeResult(null, parent, header.getAsnClass(), header.getAsnTag(), header.getIsConstructed(), true, true, new ASNDecodeResult(null, parent, true, true, null));
			}
			else
				throw new ASNDecodeException("We have a real problem here , the level of ASN parser got to " + MAX_DEPTH, header.getAsnTag(), header.getAsnClass(), header.getIsConstructed(), parent);
		}

		if (buffer.readableBytes() < header.getLength())
			if (skipErrors)
			{
				if (buffer.readableBytes() >= header.getLength())
					buffer.skipBytes(header.getLength());
				else
					buffer.skipBytes(buffer.readableBytes());

				return new DecodeResult(null, parent, header.getAsnClass(), header.getAsnTag(), header.getIsConstructed(), true, true, new ASNDecodeResult(null, parent, true, true, null));
			}
			else
				throw new ASNDecodeException("Not enough bytes for :" + header.getAsnClass() + "," + header.getAsnTag() + "," + header.getIsConstructed() + "," + header.getIndefiniteLength(), header.getAsnTag(), header.getAsnClass(), header.getIsConstructed(), parent);

		ASNHeader currHeader = new ASNHeader(header.getAsnClass(), header.getIsConstructed(), header.getAsnTag(), header.getIndefiniteLength(), index);
		Class<?> effectiveClass = classMapping.get(currHeader);

		if (effectiveClass == null)
		{
			currHeader = new ASNHeader(header.getAsnClass(), header.getIsConstructed(), header.getAsnTag(), header.getIndefiniteLength(), null);
			effectiveClass = classMapping.get(currHeader);
			if (effectiveClass == null)
				if (wildcardField != null)
				{
					header.setLength(header.getLength() + (buffer.readerIndex() - oldIndex) + (oldWriterIndex - buffer.writerIndex()));					
					buffer.readerIndex(oldIndex);
					buffer.writerIndex(oldWriterIndex);
					if (wildcardField.getType().isAssignableFrom(List.class) && !wildcardField.getType().equals(Object.class))
					{
						Type[] innerTypes = ((ParameterizedType) wildcardField.getGenericType()).getActualTypeArguments();
						effectiveClass = (Class<?>) innerTypes[0];
					}
					else
						effectiveClass = wildcardField.getType();
				} else if (defaultClass != null)
					effectiveClass = defaultClass;
				else if (skipErrors)
				{
					if (buffer.readableBytes() >= header.getLength())
						buffer.skipBytes(header.getLength());
					else
						buffer.skipBytes(buffer.readableBytes());

					return new DecodeResult(null, parent, header.getAsnClass(), header.getAsnTag(), header.getIsConstructed(), true, true, new ASNDecodeResult(null, parent, true, true, null));
				}
				else
					throw new ASNDecodeException("no class found for matching tag:" + currHeader.getAsnClass() + "," + currHeader.getAsnTag() + "," + currHeader.getIsConstructed() + "," + currHeader.getIndefiniteLength(), currHeader.getAsnTag(), currHeader.getAsnClass(), currHeader.getIsConstructed(), parent);
		}

		Boolean isChoise = false;
		FieldData fieldData = null;
		if (fieldsMap != null)
		{
			fieldData = fieldsMap.get(currHeader);

			if (fieldData != null && fieldData.getFieldType() == FieldType.CHOISE)
			{
				isChoise = true;
				header.setLength(header.getLength() + buffer.readerIndex() - oldIndex);
				buffer.resetReaderIndex();

				// if(fieldData.getField().getType().isAssignableFrom(List.class) &&
				// !fieldData.getField().getType().equals(Object.class)) {
				// Type[] innerTypes = ((ParameterizedType)
				// fieldData.getField().getGenericType()).getActualTypeArguments();
				// effectiveClass=(Class<?>)innerTypes[0];
				// }
				// else
				effectiveClass = fieldData.getDefaultClass();
			}
		}

		ParserClassData cachedData = cachedElements.get(effectiveClass.getCanonicalName());
		if (cachedData == null)
			cachedData = processField(effectiveClass, cachedElements);

		Boolean hadErrors = false;
		ASNDecodeResult errorResults = null;

		Constructor<?> ctor = effectiveClass.getConstructor();
		Object currObject = ctor.newInstance(new Object[] {});
		if (cachedData.getHasWrappedTag())
			buffer.resetReaderIndex();

		if (handler != null)
		{
			ASNPreprocess preprocessAnnotation = effectiveClass.getAnnotation(ASNPreprocess.class);
			if (preprocessAnnotation != null)
				handler.preProcessElement(parent, currObject, mappedData);
		}

		if (!cachedData.getSubFieldsFound())
		{
			Method[] methods = effectiveClass.getMethods();
			for (Method method : methods)
			{
				ASNDecode asnDecode = method.getAnnotation(ASNDecode.class);
				if (asnDecode != null)
				{
					if (parent != null && wildcardField != null && wildcardField.isAccessible() && !wildcardField.getType().isAssignableFrom(List.class) && wildcardField.get(parent) != null)
						// patching in case multiple items get through wildcard to same item
						currObject = wildcardField.get(parent);

					try
					{
						Object value = method.invoke(currObject, new Object[] { this, parent, buffer.slice(buffer.readerIndex(), header.getLength()), mappedData, skipErrors, level + 1 });
						if (value instanceof Boolean)
							hadErrors |= (Boolean) value;
						else if (value instanceof ASNDecodeResult)
						{
							if (errorResults == null)
								errorResults = (ASNDecodeResult) value;

							if (value != null)
								hadErrors |= ((ASNDecodeResult) value).getHadErrors();
						}
					}
					catch (Exception ex)
					{
						if (skipErrors)
							return new DecodeResult(null, parent, header.getAsnClass(), header.getAsnTag(), header.getIsConstructed(), true, false, new ASNDecodeResult(null, parent, true, false, null));
						else
							throw ex;
					}

					buffer.skipBytes(header.getLength());
					break;
				}
			}
		}
		else
		{
			int remainingBytes = 0;
			// for parent level wrapped tag may have multiple entries , especially in Huawei
			if (parent == null && cachedData.getHasWrappedTag())
				remainingBytes = buffer.readableBytes();
			else
				remainingBytes = header.getLength();

			int innerIndex = 0;
			if (isChoise)
				innerIndex = index;

			int originalIndex = buffer.readerIndex();
			int originalWriterIndex = buffer.writerIndex();
			while ((buffer.readerIndex() - originalIndex) < (remainingBytes - originalWriterIndex + buffer.writerIndex()))
			{
				int readableBytes = buffer.readableBytes();
				DecodeResult innerValue = decode(currObject, buffer, skipErrors, cachedData.getWildcardField(), cachedData.getFieldsMap(), cachedData.getInnerMap(), cachedElements, innerIndex, mappedData, null, level + 1);
				if(buffer.readableBytes()==readableBytes)
				{
					//oops we have passed a cycle without moving, lets skip internal
					buffer.readerIndex(0);
					try
					{
						logger.warn("The internal parsing is not capable to find next element for ASN Parser, the buffer content is " + toHex(buffer));
					}
					catch (Exception ex)
					{
						logger.warn("The internal parsing is not capable to find next element for ASN Parser, can not print buffer content!!!");
					}

					buffer.resetReaderIndex();
					buffer.markReaderIndex();

					if (skipErrors)
					{
						if((buffer.readerIndex() - originalIndex) < remainingBytes)
							buffer.skipBytes(readableBytes + originalIndex + buffer.readerIndex());

						return new DecodeResult(null, parent, header.getAsnClass(), header.getAsnTag(), header.getIsConstructed(), true, true, new ASNDecodeResult(null, parent, true, true, null));
					}
					else
						throw new ASNDecodeException("We have a real problem here , the level of ASN parser got to " + MAX_DEPTH, header.getAsnTag(), header.getAsnClass(), header.getIsConstructed(), parent);
				}
				
				hadErrors |= innerValue.getHadErrors();
				if (innerValue.getHadErrors() && errorResults == null)
					errorResults = innerValue;

				if (innerValue != null && innerValue.getResult() != null)
				{
					ASNTag innerTag = innerValue.getResult().getClass().getAnnotation(ASNTag.class);
					ASNHeader innerHeader = new ASNHeader(innerTag, innerValue.realClass, innerValue.realTag, innerValue.realConstructed, innerIndex);
					FieldData fd = cachedData.getFieldsMapElement(innerHeader);
					Field f = null;
					if (fd == null)
					{
						innerHeader = new ASNHeader(innerTag, innerValue.realClass, innerValue.realTag, innerValue.realConstructed, null);
						fd = cachedData.getFieldsMapElement(innerHeader);
						if (fd == null)
							f = cachedData.getWildcardField();
					}

					if (fd != null)
						f = fd.getField();

					if (f.getType().isAssignableFrom(List.class) && !f.getType().equals(Object.class))
					{
						f.setAccessible(true);
						List innerList = (List) f.get(currObject);
						if (innerList == null)
						{
							innerList = new ArrayList();
							f.set(currObject, innerList);
						}

						innerList.add(innerValue.getResult());
					}
					else
					{
						f.setAccessible(true);
						f.set(currObject, innerValue.getResult());
					}

					if (!isChoise)
						innerIndex++;
				}
			}
		}

		/*if (header.getIndefiniteLength())
		{
			// lets read end of content
			buffer.readByte();
			buffer.readByte();
		}*/

		if (handler != null)
		{
			ASNPostprocess postprocessAnnotation = effectiveClass.getAnnotation(ASNPostprocess.class);
			if (postprocessAnnotation != null)
				handler.postProcessElement(parent, currObject, mappedData);
		}

		Boolean isTag = false;
		if (errorResults != null && errorResults.getIsTagError() != null && errorResults.getIsTagError())
			isTag = true;

		return new DecodeResult(currObject, parent, header.getAsnClass(), header.getAsnTag(), header.getIsConstructed(), hadErrors, isTag, errorResults);
	}

	private ASNHeaderWithLength readHeader(ByteBuf buffer) throws ASNException
	{
		byte currData = buffer.readByte();
		ASNClass asnClass = ASNClass.fromInt((currData >> 6) & 0x03);
		boolean constructed = (currData & 0x20) != 0;
		int asnTag = currData & 0x1F;
		if (asnTag == 0x1F)
		{
			asnTag = 0;
			Boolean gotEOF = false;

			while (buffer.readableBytes() > 0 && !gotEOF)
			{
				currData = buffer.readByte();
				if ((currData & 0x80) == 0)
					gotEOF = true;

				asnTag = ((asnTag << 7) | (currData & 0x7F));
			}

			if (!gotEOF)
				throw new ASNException("Invalid tag encoding found");
		}

		currData = buffer.readByte();
		int length = 0;
		boolean indefiniteLength = false;
		if ((currData & 0x080) == 0x080 && currData != -128)
		{
			int lengthLength = currData & 0x7F;
			for (int i = 0; i < lengthLength; i++)
				length = (length << 8) | (buffer.readByte() & 0x0FF);
		}
		else if (currData != -128)
			length = currData;
		else
		{
			length = currData & 0x7F;
			if (length == 0 && buffer.readableBytes() > 0)
			{
				indefiniteLength = true;
				buffer.markReaderIndex();
				Boolean previousWasZero = false;
				int tempLength = 0;	
				//lets read till the end indefinite length....
				while (buffer.readableBytes() > 0)
				{
					tempLength++;
					if (buffer.readByte() != 0x00)
						previousWasZero = false;
					else if (previousWasZero)
						length = tempLength - 2;
					else
						previousWasZero = true;
				}

				if (length==0)
				{
					// throw new ASNException("Invalid length encoding found");
					// length = 0;
					indefiniteLength = false;
				}

				buffer.resetReaderIndex();
			}
		}

		return new ASNHeaderWithLength(asnClass, constructed, asnTag, indefiniteLength, null, length);
	}

	public ASNParsingComponentException validateObject(Object value) throws ASNException
	{
		ASNTag tag = value.getClass().getAnnotation(ASNTag.class);
		ASNWrappedTag wrappedTag = value.getClass().getAnnotation(ASNWrappedTag.class);
		if (tag == null && wrappedTag == null)
			throw new ASNException("only entities annotated with ASNTag or ASNWrappedTag annotation are supported");

		try
		{
			ParserClassData cachedData = cachedElements.get(value.getClass().getCanonicalName());
			if (cachedData == null)
				cachedData = processField(value.getClass(), cachedElements);

			return validateObject(value, cachedElements);
		}
		catch (Exception ex)
		{
			throw new ASNException(ex.getMessage());
		}
	}

	@SuppressWarnings("rawtypes")
	private ASNParsingComponentException validateObject(Object value, ConcurrentHashMap<String, ParserClassData> cachedElements) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, ASNException
	{
		ASNParsingComponentException result = null;

		ParserClassData cachedData = cachedElements.get(value.getClass().getCanonicalName());
		if (cachedData == null)
			cachedData = processField(value.getClass(), cachedElements);

		Method[] methods = value.getClass().getMethods();
		for (Method method : methods)
		{
			ASNValidate asnValidate = method.getAnnotation(ASNValidate.class);
			if (asnValidate != null)
			{
				try
				{
					method.invoke(value, new Object[] {});
				}
				catch (InvocationTargetException ex)
				{
					if (ex.getTargetException() != null)
					{
						if (ex.getTargetException() instanceof ASNParsingComponentException)
							return (ASNParsingComponentException) ex.getTargetException();

						throw new ASNException(ex.getTargetException().getMessage());
					}
					else
						throw new ASNException("An error occured while validating the ASN.1 Object format");
				}
				break;
			}
		}

		for (int i = 0; i < cachedData.getFields().size(); i++)
		{
			cachedData.getFields().get(i).getField().setAccessible(true);
			if (cachedData.getFields().get(i).getField().getType().isAssignableFrom(List.class) && !cachedData.getFields().get(i).getField().getType().equals(Object.class))
			{
				List innerList = (List) cachedData.getFields().get(i).getField().get(value);
				for (Object innerObject : innerList)
				{
					result = validateObject(innerObject, cachedElements);
					if (result != null)
						return result;
				}
			}
			else
			{
				Object innerValue = cachedData.getFields().get(i).getField().get(value);
				if (innerValue != null)
				{
					result = validateObject(innerValue, cachedElements);
					if (result != null)
					{

					}
				}
			}
		}

		return result;
	}

	public ByteBuf encode(Object value) throws ASNException
	{
		ASNTag tag = value.getClass().getAnnotation(ASNTag.class);
		ASNWrappedTag wrappedTag = value.getClass().getAnnotation(ASNWrappedTag.class);
		if (tag == null && wrappedTag == null)
			throw new RuntimeException("only entities annotated with ASNTag or ASNWrappedTag annotation are supported");

		try
		{
			ParserClassData cachedData = cachedElements.get(value.getClass().getCanonicalName());
			if (cachedData == null)
				cachedData = processField(value.getClass(), cachedElements);

			int completeLength = getLengthWithHeader(null, value, cachedElements);
			int length = getLength(cachedData, value, cachedElements, false);
			ByteBuf buffer = Unpooled.buffer(completeLength);
			if (tag != null)
				encode(buffer, value, tag, wrappedTag, tag.asnClass(), tag.tag(), tag.constructed(), length, cachedElements);
			else
				encode(buffer, value, tag, wrappedTag, null, null, null, length, cachedElements);
			return buffer;
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			throw new ASNException(ex.getMessage());
		}
	}

	public void encode(ByteBuf buffer, Object value) throws ASNException
	{
		ASNTag tag = value.getClass().getAnnotation(ASNTag.class);
		ASNWrappedTag wrappedTag = value.getClass().getAnnotation(ASNWrappedTag.class);
		if (tag == null && wrappedTag == null)
			throw new RuntimeException("only entities annotated with ASNTag or ASNWrappedTag annotation are supported");

		try
		{
			ParserClassData cachedData = cachedElements.get(value.getClass().getCanonicalName());
			if (cachedData == null)
				cachedData = processField(value.getClass(), cachedElements);

			int length = getLength(cachedData, value, cachedElements, false);
			if (tag != null)
				encode(buffer, value, tag, wrappedTag, tag.asnClass(), tag.tag(), tag.constructed(), length, cachedElements);
			else
				encode(buffer, value, tag, wrappedTag, null, null, null, length, cachedElements);
		}
		catch (Exception ex)
		{
			throw new ASNException(ex.getMessage());
		}
	}

	private void encode(ByteBuf buffer, Object value, ASNTag asnTag, ASNWrappedTag asnWrappedTag, ASNClass realClass, Integer realTag, Boolean realConstructed, Integer length, ConcurrentHashMap<String, ParserClassData> cachedElements) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, ASNException
	{
		if (asnTag != null && asnWrappedTag == null)
		{
			encodeTagAndHeader(buffer, realClass, realTag, realConstructed);
			if (!asnTag.lengthIndefinite())
				encodeLength(buffer, false, length);
			else
				encodeLength(buffer, true, 0);
		}

		encodeWithoutHeader(buffer, value, cachedElements, false);

		if (asnTag != null && asnWrappedTag == null)
			if (asnTag.lengthIndefinite())
			{
				buffer.writeByte(0x00);
				buffer.writeByte(0x00);
			}
	}

	@SuppressWarnings("rawtypes")
	private void encodeWithoutHeader(ByteBuf buffer, Object value, ConcurrentHashMap<String, ParserClassData> cachedElements, Boolean isChoise) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, ASNException
	{
		// encode content
		ParserClassData cachedData = cachedElements.get(value.getClass().getCanonicalName());
		if (cachedData == null)
			cachedData = processField(value.getClass(), cachedElements);

		Boolean hadData = false;
		for (int i = 0; i < cachedData.getFields().size() && !(hadData && isChoise); i++)
 			switch (cachedData.getFields().get(i).getFieldType())
			{
				case STANDARD:
					cachedData.getFields().get(i).getField().setAccessible(true);
					if (cachedData.getFields().get(i).getField().getType().isAssignableFrom(List.class) && !cachedData.getFields().get(i).getField().getType().equals(Object.class))
					{
						List innerList = (List) cachedData.getFields().get(i).getField().get(value);
						for (Object innerObject : innerList)
						{
							ASNTag innerTag = innerObject.getClass().getAnnotation(ASNTag.class);
							ASNWrappedTag innerWrappedTag = innerObject.getClass().getAnnotation(ASNWrappedTag.class);
							if (innerObject != null)
								if (innerTag != null && innerWrappedTag == null)
								{
									int realInnerTag = innerTag.tag();
									ASNClass realAsnClass = innerTag.asnClass();
									Boolean realConstructed = innerTag.constructed();
									ASNProperty property = cachedData.getFields().get(i).getField().getAnnotation(ASNProperty.class);
									if (property != null)
									{
										realInnerTag = property.tag();
										realAsnClass = property.asnClass();
										realConstructed = property.constructed();
									}

									ParserClassData innerCachedData = cachedElements.get(innerObject.getClass().getCanonicalName());
									if (innerCachedData == null)
										innerCachedData = processField(innerObject.getClass(), cachedElements);

									encode(buffer, innerObject, innerTag, innerWrappedTag, realAsnClass, realInnerTag, realConstructed, getLength(innerCachedData, innerObject, cachedElements, false), cachedElements);
									hadData = true;
								}
								else if (innerWrappedTag != null)
								{
									ParserClassData innerCachedData = cachedElements.get(innerObject.getClass().getCanonicalName());
									if (innerCachedData == null)
										innerCachedData = processField(innerObject.getClass(), cachedElements);

									encode(buffer, innerObject, innerTag, innerWrappedTag, null, null, null, getLength(innerCachedData, innerObject, cachedElements, false), cachedElements);
									hadData = true;
								}
						}
					}
					else
					{
						Object innerValue = cachedData.getFields().get(i).getField().get(value);
						ASNTag innerTag = cachedData.getFields().get(i).getField().getType().getAnnotation(ASNTag.class);
						ASNWrappedTag innerWrappedTag = cachedData.getFields().get(i).getField().getType().getAnnotation(ASNWrappedTag.class);
						if (innerValue != null)
							if (innerTag != null && innerWrappedTag == null)
							{
								int realInnerTag = innerTag.tag();
								ASNClass realAsnClass = innerTag.asnClass();
								Boolean realConstructed = innerTag.constructed();
								ASNProperty property = cachedData.getFields().get(i).getField().getAnnotation(ASNProperty.class);
								if (property != null)
								{
									realInnerTag = property.tag();
									realAsnClass = property.asnClass();
									realConstructed = property.constructed();
								}

								ParserClassData innerCachedData = cachedElements.get(innerValue.getClass().getCanonicalName());
								if (innerCachedData == null)
									innerCachedData = processField(innerValue.getClass(), cachedElements);

								encode(buffer, innerValue, innerTag, innerWrappedTag, realAsnClass, realInnerTag, realConstructed, getLength(innerCachedData, innerValue, cachedElements, false), cachedElements);
								hadData = true;
							}
							else
							{
								ParserClassData innerCachedData = cachedElements.get(innerValue.getClass().getCanonicalName());
								if (innerCachedData == null)
									innerCachedData = processField(innerValue.getClass(), cachedElements);

								encode(buffer, innerValue, innerTag, innerWrappedTag, null, null, null, getLength(innerCachedData, innerValue, cachedElements, false), cachedElements);
								hadData = true;
							}
					}
					break;
				case CHOISE:
				case WILDCARD:
				default:
					cachedData.getFields().get(i).getField().setAccessible(true);
					if (cachedData.getFields().get(i).getField().getType().isAssignableFrom(List.class) && !cachedData.getFields().get(i).getField().getType().equals(Object.class))
					{
						List innerList = (List) cachedData.getFields().get(i).getField().get(value);
						for (Object innerObject : innerList)
						{
							encodeWithoutHeader(buffer, innerObject, cachedElements, cachedData.getFields().get(i).getFieldType() == FieldType.CHOISE);
							hadData = true;
						}
					}
					else
					{
						Object innerValue = cachedData.getFields().get(i).getField().get(value);
						if (innerValue != null)
						{
							encodeWithoutHeader(buffer, innerValue, cachedElements, cachedData.getFields().get(i).getFieldType() == FieldType.CHOISE);
							hadData = true;
						}
					}
					break;
			}

		if (!cachedData.getSubFieldsFound())
		{
			Method[] methods = value.getClass().getMethods();
			for (Method method : methods)
			{
				ASNEncode asnEncode = method.getAnnotation(ASNEncode.class);
				if (asnEncode != null)
				{
					method.invoke(value, new Object[] { this, buffer });
					break;
				}
			}
		}
	}

	private Integer getLengthWithHeader(Integer realTag, Object value, ConcurrentHashMap<String, ParserClassData> cachedElements) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, ASNException
	{
		Integer length = 0;
		if (value == null)
			return length;

		ASNTag asnTag = value.getClass().getAnnotation(ASNTag.class);
		ASNWrappedTag asnWrappedTag = value.getClass().getAnnotation(ASNWrappedTag.class);
		// calculating itself header
		if (asnTag != null)
		{
			ParserClassData cachedData = cachedElements.get(value.getClass().getCanonicalName());
			if (cachedData == null)
				cachedData = processField(value.getClass(), cachedElements);

			// get internal content/subtags length
			length = getLength(cachedData, value, cachedElements, false);

			if (asnTag.lengthIndefinite())
				// 2 bytes end of tag and one length
				length += 3;
			else
			{
				Integer lengthLength = getLengthLength(length);
				length += lengthLength + 1;
			}

			if (realTag != null)
				length += getTagLength(realTag) + 1;
			else
				length += getTagLength(asnTag.tag()) + 1;
		}
		else if (asnWrappedTag != null)
		{
			ParserClassData cachedData = cachedElements.get(value.getClass().getCanonicalName());
			if (cachedData == null)
				cachedData = processField(value.getClass(), cachedElements);

			// get internal content/subtags length
			length = getLength(cachedData, value, cachedElements, false);
			// no outer header here
		}

		return length;
	}

	public Integer getLength(Object value) throws ASNException
	{
		ParserClassData cachedData = cachedElements.get(value.getClass().getCanonicalName());
		if (cachedData == null)
			cachedData = processField(value.getClass(), cachedElements);

		try
		{
			return getLengthWithHeader(null, value, cachedElements);
		}
		catch (Exception ex)
		{
			throw new ASNException(ex.getMessage());
		}
	}

	@SuppressWarnings("rawtypes")
	private Integer getLength(ParserClassData parserData, Object value, ConcurrentHashMap<String, ParserClassData> cachedElements, Boolean isChoise) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, ASNException
	{
		Integer length = 0;
		Boolean hadData = false;
		for (int i = 0; i < parserData.getFields().size() && !(hadData && isChoise); i++)
			switch (parserData.getFields().get(i).getFieldType())
			{
				case STANDARD:
					parserData.getFields().get(i).getField().setAccessible(true);

					Integer realTag = null;
					ASNProperty asnProperty = parserData.getFields().get(i).getField().getAnnotation(ASNProperty.class);
					if (asnProperty != null)
						realTag = asnProperty.tag();

					if (parserData.getFields().get(i).getField().getType().isAssignableFrom(List.class) && !parserData.getFields().get(i).getField().getType().equals(Object.class))
					{
						List innerList = (List) parserData.getFields().get(i).getField().get(value);
						for (Object innerValue : innerList)
						{
							length += getLengthWithHeader(realTag, innerValue, cachedElements);
							if (innerValue != null)
								hadData = true;
						}
					}
					else
					{
						length += getLengthWithHeader(realTag, parserData.getFields().get(i).getField().get(value), cachedElements);
						if (parserData.getFields().get(i).getField().get(value) != null)
							hadData = true;
					}
					break;
				case CHOISE:
				case WILDCARD:
				default:
					parserData.getFields().get(i).getField().setAccessible(true);
					if (parserData.getFields().get(i).getField().getType().isAssignableFrom(List.class) && !parserData.getFields().get(i).getField().getType().equals(Object.class))
					{
						List innerList = (List) parserData.getFields().get(i).getField().get(value);
						for (Object listItem : innerList)
						{
							ParserClassData cachedData = cachedElements.get(listItem.getClass().getCanonicalName());
							if (cachedData == null)
								cachedData = processField(listItem.getClass(), cachedElements);

							length += getLength(cachedData, listItem, cachedElements, parserData.getFields().get(i).getFieldType() == FieldType.CHOISE);
							if (listItem != null)
								hadData = true;
						}
					}
					else
					{
						Object innerValue = parserData.getFields().get(i).getField().get(value);
						if (innerValue != null)
						{
							ParserClassData cachedData = cachedElements.get(innerValue.getClass().getCanonicalName());
							if (cachedData == null)
								cachedData = processField(innerValue.getClass(), cachedElements);

							// get internal content/subtags length
							length += getLength(cachedData, innerValue, cachedElements, parserData.getFields().get(i).getFieldType() == FieldType.CHOISE);
							hadData = true;
						}
					}
					break;
			}

		if (!parserData.getSubFieldsFound())
		{
			Method[] methods = value.getClass().getMethods();
			for (Method method : methods)
			{
				ASNLength asnLength = method.getAnnotation(ASNLength.class);
				if (asnLength != null)
					return (Integer) method.invoke(value, this);
			}
		}
		else
			return length;

		throw new ASNException("Length can not be calculated for class:" + value.getClass().getCanonicalName());
	}

	private Integer getTagLength(Integer tag)
	{
		if (tag < 31)
			return 0;

		Integer testValue = tag;
		int result = 0;
		while (testValue > 0)
		{
			result += 1;
			testValue = ((testValue >> 7) & Integer.MAX_VALUE);
		}

		if (result == 0)
			result = 1;

		return result;
	}

	public static Integer getLengthLength(Integer value)
	{
		if (value < 128)
			return 0;

		Integer testValue = value;
		int result = 0;
		while (testValue > 0)
		{
			result += 1;
			testValue = ((testValue >> 8) & Integer.MAX_VALUE);
		}

		if (result == 0)
			result = 1;

		return result;
	}

	private void encodeTagAndHeader(ByteBuf buffer, ASNClass realClass, Integer realTag, Boolean realConstructed)
	{
		byte header;
		if (realTag < 31)
		{
			if (realConstructed)
				header = (byte) (((realClass.getValue() << 6) & 0xC0) | 0x20 | (realTag & 0x1F));
			else
				header = (byte) (((realClass.getValue() << 6) & 0xC0) | (realTag & 0x1F));

			buffer.writeByte(header);
		}
		else
		{
			if (realConstructed)
				header = (byte) (((realClass.getValue() << 6) & 0xC0) | 0x3F);
			else
				header = (byte) (((realClass.getValue() << 6) & 0xC0) | 0x1F);

			buffer.writeByte(header);

			Integer testValue = realTag;
			while (testValue > 0)
			{
				int currByte = testValue & 0x07F;
				testValue = ((testValue >> 7) & Integer.MAX_VALUE);
				if (testValue == 0)
					buffer.writeByte(currByte);
				else
					buffer.writeByte(0x80 | currByte);
			}
		}
	}

	public static void encodeLength(ByteBuf buffer, Boolean lengthIndefinite, Integer value)
	{
		if (lengthIndefinite)
			buffer.writeByte((byte) 0x80);
		else if (value < 128)
			buffer.writeByte(value);
		else
		{
			int totalBytes = getLengthLength(value);
			buffer.writeByte(0x80 | totalBytes);

			for (int i = 0, size = (totalBytes - 1) * 8; i < totalBytes; i++, size -= 8)
				buffer.writeByte((value >> size) & 0x0FF);
		}
	}

	private ParserClassData processField(Class<?> effectiveClass, ConcurrentHashMap<String, ParserClassData> cachedElements)
	{
		ParserClassData cachedData = cachedElements.get(effectiveClass.getCanonicalName());
		if (cachedData == null)
		{
			Class<?> currentPath = effectiveClass;
			List<Class<?>> classesPath = new ArrayList<Class<?>>();
			do
			{
				classesPath.add(0, currentPath);
				currentPath = currentPath.getSuperclass();
			}
			while (currentPath != null);

			List<FieldData> annotatedFields = new ArrayList<FieldData>();
			Boolean hasWrappedTag = false;
			for (Class<?> currentClass : classesPath)
			{
				ASNWrappedTag wrappedTag = currentClass.getAnnotation(ASNWrappedTag.class);
				if (wrappedTag != null)
					hasWrappedTag = true;

				Field[] fields = currentClass.getDeclaredFields();
				for (int i = 0; i < fields.length; i++)
				{
					ASNExclude excludeTag = fields[i].getAnnotation(ASNExclude.class);
					if (excludeTag == null)
					{
						ASNWildcard wildcardTag = fields[i].getAnnotation(ASNWildcard.class);
						ASNChoise choiseTag = fields[i].getAnnotation(ASNChoise.class);
						if (wildcardTag != null)
							annotatedFields.add(new FieldData(FieldType.WILDCARD, fields[i], fields[i].getType()));
						else if (choiseTag != null)
						{
							if (!choiseTag.defaultImplementation().getCanonicalName().equals(Void.class.getCanonicalName()))
								annotatedFields.add(new FieldData(FieldType.CHOISE, fields[i], choiseTag.defaultImplementation()));
							else
								annotatedFields.add(new FieldData(FieldType.CHOISE, fields[i], fields[i].getType()));
						}
						else
						{
							ASNTag innerTag = null;
							Class<?> realType = fields[i].getType();
							if (fields[i].getType().isAssignableFrom(List.class) && !fields[i].getType().equals(Object.class))
							{
								Type[] innerTypes = ((ParameterizedType) fields[i].getGenericType()).getActualTypeArguments();
								if (innerTypes.length == 1)
								{
									realType = (Class<?>) innerTypes[0];
									innerTag = ((Class<?>) innerTypes[0]).getAnnotation(ASNTag.class);
								}
							}
							else if (fields[i].getType().isAssignableFrom(ASNGeneric.class) && !fields[i].getType().equals(Object.class))
								innerTag = ((Class<?>) ((ParameterizedType) effectiveClass.getGenericSuperclass()).getActualTypeArguments()[0]).getAnnotation(ASNTag.class);
							else
								innerTag = fields[i].getType().getAnnotation(ASNTag.class);

							if (innerTag != null)
								annotatedFields.add(new FieldData(FieldType.STANDARD, fields[i], realType));
						}
					}
				}
			}

			cachedData = new ParserClassData(annotatedFields, hasWrappedTag);
			storeFields(effectiveClass, cachedData, annotatedFields, null, null);
			cachedElements.put(effectiveClass.getCanonicalName(), cachedData);
		}

		return cachedData;
	}

	private void processChoiseField(Class<?> effectiveClass, ParserClassData cachedData, Field parentField, Class<?> parentType)
	{
		Class<?> currentPath = effectiveClass;
		List<Class<?>> classesPath = new ArrayList<Class<?>>();
		do
		{
			classesPath.add(0, currentPath);
			currentPath = currentPath.getSuperclass();
		}
		while (currentPath != null);

		List<FieldData> annotatedFields = new ArrayList<FieldData>();
		for (Class<?> currentClass : classesPath)
		{
			Field[] fields = currentClass.getDeclaredFields();
			for (int i = 0; i < fields.length; i++)
			{
				ASNExclude excludeTag = fields[i].getAnnotation(ASNExclude.class);
				if (excludeTag == null)
				{
					ASNWildcard wildcardTag = fields[i].getAnnotation(ASNWildcard.class);
					ASNChoise choiseTag = fields[i].getAnnotation(ASNChoise.class);
					if (wildcardTag != null)
						annotatedFields.add(new FieldData(FieldType.WILDCARD, fields[i], fields[i].getType()));
					else if (choiseTag != null)
					{
						if (!choiseTag.defaultImplementation().getCanonicalName().equals(Void.class.getCanonicalName()))
							annotatedFields.add(new FieldData(FieldType.CHOISE, fields[i], choiseTag.defaultImplementation()));
						else
							annotatedFields.add(new FieldData(FieldType.CHOISE, fields[i], fields[i].getType()));
					}
					else
					{
						ASNTag innerTag = null;
						Class<?> realType = fields[i].getType();
						if (fields[i].getType().isAssignableFrom(List.class) && !fields[i].getType().equals(Object.class))
						{
							Type[] innerTypes = ((ParameterizedType) fields[i].getGenericType()).getActualTypeArguments();
							if (innerTypes.length == 1)
							{
								realType = (Class<?>) innerTypes[0];
								innerTag = ((Class<?>) innerTypes[0]).getAnnotation(ASNTag.class);
							}
						}
						else if (fields[i].getType().isAssignableFrom(ASNGeneric.class) && !fields[i].getType().equals(Object.class))
							innerTag = ((Class<?>) ((ParameterizedType) effectiveClass.getGenericSuperclass()).getActualTypeArguments()[0]).getAnnotation(ASNTag.class);
						else
							innerTag = fields[i].getType().getAnnotation(ASNTag.class);

						if (innerTag != null)
							annotatedFields.add(new FieldData(FieldType.STANDARD, fields[i], realType));
					}
				}
			}
		}

		storeFields(effectiveClass, cachedData, annotatedFields, parentField, parentType);
	}

	private void storeFields(Class<?> effectiveClass, ParserClassData cachedData, List<FieldData> fields, Field parentField, Class<?> parentType)
	{
		for (int i = 0; i < fields.size(); i++)
			switch (fields.get(i).getFieldType())
			{
				case STANDARD:
					ASNTag innerTag = null;
					Class<?> realType = fields.get(i).getField().getType();
					if (fields.get(i).getField().getType().isAssignableFrom(List.class) && !fields.get(i).getField().getType().equals(Object.class))
					{
						Type[] innerTypes = ((ParameterizedType) fields.get(i).getField().getGenericType()).getActualTypeArguments();
						if (innerTypes.length == 1)
						{
							realType = ((Class<?>) innerTypes[0]);
							innerTag = realType.getAnnotation(ASNTag.class);
						}
					}
					else if (fields.get(i).getField().getType().isAssignableFrom(ASNGeneric.class) && !fields.get(i).getField().getType().equals(Object.class))
					{
						realType = ((Class<?>) ((ParameterizedType) effectiveClass.getGenericSuperclass()).getActualTypeArguments()[0]);
						innerTag = ((Class<?>) ((ParameterizedType) effectiveClass.getGenericSuperclass()).getActualTypeArguments()[0]).getAnnotation(ASNTag.class);
					} else
						innerTag = realType.getAnnotation(ASNTag.class);

					int realInnerTag = innerTag.tag();
					ASNClass realClass = innerTag.asnClass();
					Boolean realConstructed = innerTag.constructed();
					Integer index = null;
					ASNProperty property = fields.get(i).getField().getAnnotation(ASNProperty.class);
					if (property != null)
					{
						realInnerTag = property.tag();
						realClass = property.asnClass();
						realConstructed = property.constructed();
						if (property.index() >= 0)
							index = property.index();
					}

					ASNHeader asnHeader = new ASNHeader(innerTag, realClass, realInnerTag, realConstructed, index);
					if (parentField == null || parentType == null)
					{
						Class<?> defaultClass = realType;
						if (fields.get(i).getField().getType().isInterface() && property != null && !property.defaultImplementation().getCanonicalName().equals(Void.class.getCanonicalName()))
							defaultClass = property.defaultImplementation();

						cachedData.addInnerMapElement(asnHeader, defaultClass);
						cachedData.addFieldsMapElement(asnHeader, new FieldData(FieldType.STANDARD, fields.get(i).getField(), defaultClass));
					}
					else
					{
						cachedData.addInnerMapElement(asnHeader, parentType);
						cachedData.addFieldsMapElement(asnHeader, new FieldData(FieldType.CHOISE, parentField, parentType));
					}
					break;
				case CHOISE:
					realType = fields.get(i).getField().getType();
					Class<?> choiceDefaultClass = fields.get(i).getDefaultClass();
					if (!choiceDefaultClass.getCanonicalName().equals(realType.getCanonicalName()))
						realType = choiceDefaultClass;
					else if (fields.get(i).getField().getType().isAssignableFrom(List.class) && !fields.get(i).getField().getType().equals(Object.class))
					{
						Type[] innerTypes = ((ParameterizedType) fields.get(i).getField().getGenericType()).getActualTypeArguments();
						if (innerTypes.length == 1)
							realType = ((Class<?>) innerTypes[0]);
					}
					else if (fields.get(i).getField().getType().isAssignableFrom(ASNGeneric.class) && !fields.get(i).getField().getType().equals(Object.class))
						realType = ((Class<?>) ((ParameterizedType) effectiveClass.getGenericSuperclass()).getActualTypeArguments()[0]);

					if (parentField != null && parentType != null)
						processChoiseField(realType, cachedData, parentField, parentType);
					else
						processChoiseField(realType, cachedData, fields.get(i).getField(), realType);
					break;
				case WILDCARD:
				default:
					break;
			}
	}

	private class DecodeResult extends ASNDecodeResult
	{
		private Integer realTag;
		private ASNClass realClass;
		private Boolean realConstructed;

		private DecodeResult(Object result, Object parent, ASNClass realClass, Integer realTag, Boolean realConstructed, Boolean hadErrors, Boolean isTagError, ASNDecodeResult firstError)
		{
			super(result, parent, hadErrors, isTagError, firstError);
			this.realClass = realClass;
			this.realTag = realTag;
			this.realConstructed = realConstructed;
			this.firstError = firstError;
		}
	}

	public static String toHex(ByteBuf b)
	{
		String out = "";
		for (int index = 0; index < b.readableBytes(); index++)
			out += Integer.toHexString(b.getByte(index));
			
		return out;
	}
}