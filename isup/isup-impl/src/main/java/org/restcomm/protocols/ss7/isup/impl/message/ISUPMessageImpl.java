/*
 * TeleStax, Open Source Cloud Communications
 * Mobius Software LTD
 * Copyright 2012, Telestax Inc and individual contributors
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

package org.restcomm.protocols.ss7.isup.impl.message;

import io.netty.buffer.ByteBuf;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.restcomm.protocols.ss7.isup.ISUPMessageFactory;
import org.restcomm.protocols.ss7.isup.ISUPParameterFactory;
import org.restcomm.protocols.ss7.isup.ParameterException;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.AbstractISUPParameter;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.CircuitIdentificationCodeImpl;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.EndOfOptionalParametersImpl;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.MessageTypeImpl;
import org.restcomm.protocols.ss7.isup.message.parameter.CircuitIdentificationCode;
import org.restcomm.protocols.ss7.isup.message.parameter.ISUPParameter;
import org.restcomm.protocols.ss7.isup.message.parameter.MessageType;

/**
 * Start time:14:09:04 2009-04-20<br>
 * Project: restcomm-isup-stack<br>
 * This is super message class for all messages that we have. It defines some methods that need to be implemented
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author yulianoifa
 */
public abstract class ISUPMessageImpl extends AbstractISUPMessage {
	/**
     * To use one when encoding, created, possibly when decoding
     */
    protected static final EndOfOptionalParametersImpl _END_OF_OPTIONAL_PARAMETERS = new EndOfOptionalParametersImpl();

    // protected static final Logger logger = LogManager.getLogger(ISUPMessageImpl.class);

    // TODO: change everything below into [], for such small size of arrays, its faster to even search through them.
    /**
     * F = mandatory fixed length parameter;<br>
     * for type F parameters: the length, in octets, of the parameter content;
     */
    protected Map<Integer, ISUPParameter> f_Parameters;
    /**
     * V = mandatory variable length parameter;<br>
     * for type V parameters: the length, in octets, of the length indicator and of the parameter content. The minimum and the
     * maximum length are indicated;
     */
    protected Map<Integer, ISUPParameter> v_Parameters;
    /**
     * O = optional parameter of fixed or variable length; for type O parameters: the length, in octets, of the parameter name,
     * length indicator and parameter content. For variable length parameters the minimum and maximum length is indicated.
     */
    protected Map<Integer, ISUPParameter> o_Parameters;

    // magic
    protected Set<Integer> mandatoryCodes;
    protected Set<Integer> mandatoryVariableCodes;
    protected Set<Integer> optionalCodes;

    protected Map<Integer, Integer> mandatoryCodeToIndex;
    protected Map<Integer, Integer> mandatoryVariableCodeToIndex;
    protected Map<Integer, Integer> optionalCodeToIndex;

    protected CircuitIdentificationCode cic;
    protected int sls;

    public ISUPMessageImpl(Set<Integer> mandatoryCodes, Set<Integer> mandatoryVariableCodes, Set<Integer> optionalCodes,
            Map<Integer, Integer> mandatoryCode2Index, Map<Integer, Integer> mandatoryVariableCode2Index,
            Map<Integer, Integer> optionalCode2Index) {
        super();

        this.f_Parameters = new TreeMap<Integer, ISUPParameter>();
        this.v_Parameters = new TreeMap<Integer, ISUPParameter>();
        this.o_Parameters = new TreeMap<Integer, ISUPParameter>();

        this.mandatoryCodes = mandatoryCodes;
        this.mandatoryVariableCodes = mandatoryVariableCodes;
        this.optionalCodes = optionalCodes;

        this.mandatoryCodeToIndex = mandatoryCode2Index;
        this.mandatoryVariableCodeToIndex = mandatoryVariableCode2Index;
        this.optionalCodeToIndex = optionalCode2Index;

    }

    /**
     *
     */
    public ISUPMessageImpl() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public void setSls(int sls) {
        // if(sls>=16 || sls<0)
        // {
        // throw new IllegalArgumentException("SLS must be in range of one byte, it is: "+sls+"!");
        // }
        this.sls = (sls & 0x0F);
    }

    @Override
    public int getSls() {
        return this.sls;
    }

    /**
     * @return <ul>
     *         <li><b>true</b> - if all requried parameters are set</li>
     *         <li><b>false</b> - otherwise</li>
     *         </ul>
     */
    public abstract boolean hasAllMandatoryParameters();

    /**
     * Returns message code. See Q.763 Table 4. It simply return value of static constant - _MESSAGE_TYPE, where value of
     * parameter is value _MESSAGE_CODE
     *
     * @return
     */
    public abstract MessageType getMessageType();

    // ////////////////
    // CODE SECTION //
    // ////////////////    
    public void encode(ByteBuf buffer) throws ParameterException {
        final boolean optionalPresent = this.o_Parameters.size() > 1;
        this.encodeMandatoryParameters(f_Parameters, buffer);
        this.encodeMandatoryVariableParameters(v_Parameters, buffer, optionalPresent);
        if (optionalPresent) {
            this.encodeOptionalParameters(o_Parameters, buffer);
        }
    }

    // NOTE: those methods are more or less generic.
    protected void encodeMandatoryParameters(Map<Integer, ISUPParameter> parameters, ByteBuf buffer)
            throws ParameterException {
        // 1.5 Mandatory fixed part
        // Those parameters that are mandatory and of fixed length for a
        // particular message type will be
        // contained in the mandatory fixed part. The position, length and order
        // of the parameters is uniquely
        // defined by the message type; thus, the names of the parameters and
        // the length indicators are not
        // included in the message.
        if (this.cic == null) {
            // this will be changed to different exception
            throw new ParameterException("CIC is not set!");
        }
        ((AbstractISUPParameter) this.cic).encode(buffer);
        for (ISUPParameter p : parameters.values()) {
            ((AbstractISUPParameter) p).encode(buffer);
        }
    }

    /**
     * takes care of endoding parameters - poniters and actual parameters.
     *
     * @param parameters - list of parameters
     * @param bos - output
     * @param isOptionalPartPresent - if <b>true</b> this will encode pointer to point for start of optional part, otherwise it
     *        will encode this octet as zeros
     * @throws ParameterException
     */
    protected void encodeMandatoryVariableParameters(Map<Integer, ISUPParameter> parameters, ByteBuf buffer,
            boolean isOptionalPartPresent) throws ParameterException {
        try {
            // complicated
            if (!mandatoryVariablePartPossible()) {
                // we ommit pointer to this part, go straight for optional pointer.
                if (optionalPartIsPossible()) {
                    if (isOptionalPartPresent) {
                    	buffer.writeByte(0x01);
                    } else {
                        // zeros
                    	buffer.writeByte(0x00);
                    }
                } else {
                    // do nothing?
                }

            } else {
            	int lastParameterLength = 0;
                int currentParameterLength = 0;     
                int totalParameterLength=0;
                
                for (int index = 0; index < parameters.size(); index++) {
                    AbstractISUPParameter p = (AbstractISUPParameter) parameters.get(index);
                    
                    if (index == 0)
                        totalParameterLength=(parameters.size() + (optionalPartIsPossible() ? 1 : 0));
                    else 
                    	totalParameterLength+=lastParameterLength;

                    buffer.writeByte(totalParameterLength);                
                    buffer.markWriterIndex();
                    
                    int originalIndex=buffer.writerIndex();                    
                    buffer.writerIndex(originalIndex+totalParameterLength);
                    int currWriteIndex=buffer.writerIndex();
                    p.encode(buffer);                    
                    currentParameterLength = buffer.writerIndex()-currWriteIndex;
                    if (currentParameterLength > 255) {
                        throw new ParameterException("Length of body must not be greater than one octet - 255 ");
                    }                    

                    buffer.writerIndex(originalIndex+totalParameterLength-1);
                    buffer.writeByte(currentParameterLength);
                    lastParameterLength = currentParameterLength;   
                    buffer.resetWriterIndex();
                }

                // we ommit pointer to this part, go straight for optional pointer.
                totalParameterLength+=lastParameterLength;
                if (optionalPartIsPossible()) {
                	if (isOptionalPartPresent) {
                    	buffer.writeByte(totalParameterLength);                        
                    } else {
                    	buffer.writeByte(0x00);
                    }
                	totalParameterLength--;                	
                }
                
                buffer.writerIndex(buffer.writerIndex() + totalParameterLength);
            }
        } catch (ParameterException pe) {
            throw pe;
        } catch (Exception e) {
            throw new ParameterException(e);
        }
    }

    /**
     * This method must be called ONLY in case there are optional params. This implies ISUPMessage.o_Parameters.size()>1 !!!
     *
     * @param parameters
     * @param bos
     * @throws ParameterException
     */
    protected void encodeOptionalParameters(Map<Integer, ISUPParameter> parameters, ByteBuf buffer)
            throws ParameterException {

        // NOTE: parameters MUST have as last endOfOptionalParametersParameter+1
        // param
        for (ISUPParameter p : parameters.values()) {

            if (p == null)
                continue;

            buffer.markWriterIndex();
            buffer.writerIndex(2+buffer.writerIndex());
            int currIndex=buffer.writerIndex();
            ((AbstractISUPParameter) p).encode(buffer);
            int length=buffer.writerIndex()-currIndex;
            buffer.resetWriterIndex();
            if (length > 255) {
                throw new ParameterException("Parameter length is over 255: " + p);
            }
            if (!(p instanceof EndOfOptionalParametersImpl)) {
                buffer.writeByte(p.getCode());
                buffer.writeByte(length);
            }
            
            buffer.writerIndex(buffer.writerIndex() + length);
        }
    }

    public void decode(ByteBuf b, ISUPMessageFactory messageFactory,ISUPParameterFactory parameterFactory) throws ParameterException {
        this.decodeMandatoryParameters(parameterFactory, b);

        if (mandatoryVariablePartPossible())
            this.decodeMandatoryVariableParameters(parameterFactory, b);

        if (!this.optionalPartIsPossible() || b.readableBytes()==0) {
            return;
        }

        // moving pointer to possible location
        // index++;

        // +1 for pointer location :)
        //index += b[index];
        if (!mandatoryVariablePartPossible())
            b.skipBytes(1);
        //we are already at required location
        this.decodeOptionalParameters(parameterFactory, b);        
    }

    // Unfortunelty this cant be generic, can it?
    protected void decodeMandatoryParameters(ISUPParameterFactory parameterFactory, ByteBuf buffer) throws ParameterException {
    	this.cic=new CircuitIdentificationCodeImpl();
        ((CircuitIdentificationCodeImpl)cic).decode(buffer.slice(buffer.readerIndex(), 2));        
        //skip command
        buffer.skipBytes(3);
    }

    /**
     * decodes ptrs and returns offset from passed index value to first optional parameter parameter
     *
     * @param b
     * @param index
     * @return
     * @throws ParameterException
     */
    protected void decodeMandatoryVariableParameters(ISUPParameterFactory parameterFactory, ByteBuf buffer)
            throws ParameterException {
        if (buffer.readableBytes() > 0) {
        	int parameterLength = 0;
            try {
                int parametersCount = getNumberOfMandatoryVariableLengthParameters();
                for (int parameterIndex = 0; parameterIndex < parametersCount; parameterIndex++) {
                	buffer.markReaderIndex();
                    int parameterLengthIndex = buffer.readByte();
                    buffer.skipBytes(parameterLengthIndex-1);
                    parameterLength = buffer.readByte();
                    ByteBuf parameterBody = buffer.slice(buffer.readerIndex(), parameterLength);
                    decodeMandatoryVariableBody(parameterFactory, parameterBody, parameterIndex);
                    buffer.resetReaderIndex();
                    buffer.skipBytes(1);
                }                
            } catch (ArrayIndexOutOfBoundsException aioobe) {
                throw new ParameterException("Failed to read parameter, to few octets in buffer", aioobe);
            } catch (IllegalArgumentException e) {
                throw new ParameterException("Failed to parse", e);
            }
            
            //skipping to optional part
            int optionalSkip=buffer.readByte();
            if(optionalSkip>0)
            	buffer.skipBytes(optionalSkip-1);
            else if(parameterLength>0)
            	buffer.skipBytes(parameterLength+1);
        } else {
            throw new ParameterException(
                    "To few bytes to decode mandatory variable part. There should be atleast on byte to indicate optional part.");
        }       
    }

    protected void decodeOptionalParameters(ISUPParameterFactory parameterFactory, ByteBuf buffer)
            throws ParameterException {

        // if not, there are no params.
        if (buffer.readableBytes() > 0) {
            // let it rip :)
            boolean readParameter = true;
            while (readParameter) {
                if (buffer.readableBytes() > 0) {
                    readParameter = true;
                } else {
                    readParameter = false;
                    continue;
                }
                byte extPCode = -1;
                byte assumedParameterLength = -1;
                try {                	
                    byte parameterCode = buffer.readByte();
                    if(parameterCode==0x00) {
                    	readParameter = false;
                        continue;
                    }
                    
                    extPCode = parameterCode;
                    byte parameterLength = buffer.readByte();
                    assumedParameterLength = parameterLength;
                    ByteBuf parameterBody=buffer.slice(buffer.readerIndex(),parameterLength);
                    buffer.skipBytes(parameterLength);
                    // This is bad, we will change this
                    decodeOptionalBody(parameterFactory, parameterBody, parameterCode);
                } catch (ArrayIndexOutOfBoundsException aioobe) {
                    throw new ParameterException("Failed to read parameter, to few octets in buffer, parameter code: "
                            + extPCode + ", assumed length: " + assumedParameterLength, aioobe);
                } catch (IllegalArgumentException e) {
                    throw new ParameterException("Failed to parse parameter: " + extPCode, e);
                }
            }
        }
    }

    // TODO: add general method to handle decode and "addParam" so we can remove "copy/paste" code to create param and set it in
    // msg.
    /**
     * @param parameterBody
     * @param parameterIndex
     */
    protected abstract void decodeMandatoryVariableBody(ISUPParameterFactory parameterFactory, ByteBuf parameterBody,int parameterIndex) throws ParameterException;

    protected abstract void decodeOptionalBody(ISUPParameterFactory parameterFactory, ByteBuf parameterBody, byte parameterCode)
            throws ParameterException;

    protected abstract int getNumberOfMandatoryVariableLengthParameters();

    protected abstract boolean optionalPartIsPossible();

    protected boolean mandatoryVariablePartPossible() {

        return getNumberOfMandatoryVariableLengthParameters() != 0;
    }

    // ////////////////////////
    // PARAM HANDLE SECTION //
    // ////////////////////////
    public void addParameter(ISUPParameter param) throws ParameterException {
        if (param == null) {
            throw new IllegalArgumentException("Argument must not be null");
        }
        int paramCode = param.getCode();
        if (this.mandatoryCodes.contains(paramCode)) {
            int index = this.mandatoryCodeToIndex.get(paramCode);
            this.f_Parameters.put(index, (AbstractISUPParameter) param);
            return;
        }

        if (this.mandatoryVariableCodes.contains(paramCode)) {
            int index = this.mandatoryVariableCodeToIndex.get(paramCode);
            this.v_Parameters.put(index, (AbstractISUPParameter) param);
            return;
        }
        if (this.optionalCodes.contains(paramCode)) {
            int index = this.optionalCodeToIndex.get(paramCode);
            this.o_Parameters.put(index, (AbstractISUPParameter) param);
            return;
        }

        throw new ParameterException("Parameter with code: " + paramCode
                + " is not defined in any type: mandatory, mandatory variable or optional");
    }

    public ISUPParameter getParameter(int parameterCode) throws ParameterException {

        if (this.mandatoryCodes.contains(parameterCode)) {
            int index = this.mandatoryCodeToIndex.get(parameterCode);
            return this.f_Parameters.get(index);
        }

        if (this.mandatoryVariableCodes.contains(parameterCode)) {
            int index = this.mandatoryVariableCodeToIndex.get(parameterCode);
            return this.v_Parameters.get(index);
        }
        if (this.optionalCodes.contains(parameterCode)) {
            int index = this.optionalCodeToIndex.get(parameterCode);
            return this.o_Parameters.get(index);
        }

        throw new ParameterException("Parameter with code: " + parameterCode
                + " is not defined in any type: mandatory, mandatory variable or optional");
    }

    public void removeParameter(int parameterCode) throws ParameterException {
        if (this.mandatoryCodes.contains(parameterCode)) {
            int index = this.mandatoryCodeToIndex.get(parameterCode);
            this.f_Parameters.remove(index);
        }

        if (this.mandatoryVariableCodes.contains(parameterCode)) {
            int index = this.mandatoryVariableCodeToIndex.get(parameterCode);
            this.v_Parameters.remove(index);
        }
        if (this.optionalCodes.contains(parameterCode)) {
            int index = this.optionalCodeToIndex.get(parameterCode);
            this.o_Parameters.remove(index);
        }
        throw new ParameterException("Parameter with code: " + parameterCode
                + " is not defined in any type: mandatory, mandatory variable or optional");
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ISUPMessage [\n===");
        sb.append(this.getMessageType().getMessageName().toString());
        sb.append(", code=");
        sb.append(this.getMessageType().getCode());
        sb.append("\nF: [");
        int i1 = 0;
        for (ISUPParameter p : this.f_Parameters.values()) {
            if (!(p instanceof MessageTypeImpl)) {
                if (i1 == 0)
                    i1 = 1;
                else
                    sb.append("\n  ");
                sb.append("==");
                sb.append(p);
            }
        }
        sb.append("]\nV: [");
        i1 = 0;
        for (ISUPParameter p : this.v_Parameters.values()) {
            if (i1 == 0)
                i1 = 1;
            else
                sb.append("\n  ");
            sb.append("==");
            sb.append(p);
        }
        sb.append("]\nO: [");
        i1 = 0;
        for (ISUPParameter p : this.o_Parameters.values()) {
            if (!(p instanceof EndOfOptionalParametersImpl)) {
                if (i1 == 0)
                    i1 = 1;
                else
                    sb.append("\n  ");
                sb.append("==");
                sb.append(p);
            }
        }
        sb.append("]]");
        return sb.toString();
    }

    public CircuitIdentificationCode getCircuitIdentificationCode() {
        return this.cic;
    }

    public void setCircuitIdentificationCode(CircuitIdentificationCode cic) {
        this.cic = cic;

    }

}
