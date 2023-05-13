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

package org.restcomm.protocols.ss7.map.smstpdu;

import java.util.HashMap;
import java.util.Map;

import org.restcomm.protocols.ss7.map.api.smstpdu.UserDataHeader;
import org.restcomm.protocols.ss7.map.api.smstpdu.UserDataHeaderElement;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class UserDataHeaderImpl implements UserDataHeader {
    private Map<Integer, ByteBuf> data = new HashMap<Integer, ByteBuf>();

    public UserDataHeaderImpl() {
    }

   public UserDataHeaderImpl(ByteBuf encodedData) {
        if (encodedData == null || encodedData.readableBytes() < 1)
            return;
        //we just read it
        int udhl = encodedData.readByte();
        if (udhl > encodedData.readableBytes())
            udhl = encodedData.readableBytes();
        
        while (udhl>0) {
            int id = encodedData.readByte();
            int len = encodedData.readByte();
            udhl-=2;
            udhl-=len;
            if (len <= encodedData.readableBytes())
                data.put(id, encodedData.readSlice(len));             
        }
    }
   
   public UserDataHeaderImpl(ByteBuf encodedData, int length) {
       if (encodedData == null || encodedData.readableBytes() < 1)
           return;
       //we just read it
       int udhl = length;
       if (udhl > encodedData.readableBytes())
           udhl = encodedData.readableBytes();
       
       while (udhl>0) {
           int id = encodedData.readByte();
           int len = encodedData.readByte();
           udhl-=2;
           udhl-=len;
           if (len <= encodedData.readableBytes())
               data.put(id, encodedData.readSlice(len));             
       }
   }

   	public int getLength() {
   		if (data.size() == 0)
           return 0;
       
   		//length of the udh
   		int length=1;
   		for (int id : data.keySet()) {
           ByteBuf innerData = data.get(id);
           //1 byte id and 1 length + the data itself if any
           if(innerData==null)
        	   length+=2;
           else
        	   length+=2+innerData.readableBytes();           
       }
   		
   	   return length;
   	}
  
   	public void getEncodedData(ByteBuf buf) {

        if (data.size() == 0)
            return;
        
        int index=buf.writerIndex();
        buf.writeByte(0);
        for (int id : data.keySet()) {
            ByteBuf innerData = data.get(id);

            buf.writeByte(id);
            if (innerData == null)
            	buf.writeByte(0);
            else {
            	buf.writeByte(innerData.readableBytes());
            	buf.writeBytes(innerData);
            }
        }

        int newIndex=buf.writerIndex();
        buf.setByte(index, newIndex-index-1);        
    }
   	
   	public void getEncodedDataWithoutLength(ByteBuf buf) {

        if (data.size() == 0)
            return;
        
        int index=buf.writerIndex();
        buf.writeByte(0);
        for (int id : data.keySet()) {
            ByteBuf innerData = data.get(id);

            buf.writeByte(id);
            if (innerData == null)
            	buf.writeByte(0);
            else {
            	buf.writeByte(innerData.readableBytes());
            	buf.writeBytes(innerData);
            }
        }

        int newIndex=buf.writerIndex();
        buf.setByte(index, newIndex-index-1);        
    }

    public Map<Integer, ByteBuf> getAllData() {
        return data;
    }

    public void addInformationElement(int informationElementIdentifier, ByteBuf encodedData) {
        this.data.put(informationElementIdentifier, encodedData);
    }

    public void addInformationElement(UserDataHeaderElement informationElement) {
        this.data.put(informationElement.getEncodedInformationElementIdentifier(),
                informationElement.getEncodedInformationElementData());
    }

    public ByteBuf getInformationElementData(int informationElementIdentifier) {
        return this.data.get(informationElementIdentifier);
    }

    public NationalLanguageLockingShiftIdentifierImpl getNationalLanguageLockingShift() {
    	ByteBuf buf = this.data.get(_InformationElementIdentifier_NationalLanguageLockingShift);
        if (buf != null && buf.readableBytes() == 1)
            return new NationalLanguageLockingShiftIdentifierImpl(buf);
        else
            return null;
    }

    public NationalLanguageSingleShiftIdentifierImpl getNationalLanguageSingleShift() {
    	ByteBuf buf = this.data.get(_InformationElementIdentifier_NationalLanguageSingleShift);
        if (buf != null && buf.readableBytes() == 1)
            return new NationalLanguageSingleShiftIdentifierImpl(buf);
        else
            return null;
    }

    public ConcatenatedShortMessagesIdentifierImpl getConcatenatedShortMessagesIdentifier() {
    	ByteBuf buf = this.data.get(_InformationElementIdentifier_ConcatenatedShortMessages16bit);
        if (buf != null && buf.readableBytes() == 4)
            return new ConcatenatedShortMessagesIdentifierImpl(buf);
        else {
            buf = this.data.get(_InformationElementIdentifier_ConcatenatedShortMessages8bit);
            if (buf != null && buf.readableBytes() == 3)
                return new ConcatenatedShortMessagesIdentifierImpl(buf);
            else
                return null;
        }
    }

    public ApplicationPortAddressing16BitAddressImpl getApplicationPortAddressing16BitAddress() {
    	ByteBuf buf = this.data.get(_InformationElementIdentifier_ApplicationPortAddressingScheme16BitAddress);
        if (buf != null && buf.readableBytes() == 4)
            return new ApplicationPortAddressing16BitAddressImpl(buf);
        else
            return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("UserDataHeader [");
        boolean isFirst = true;
        for (int id : data.keySet()) {
            ByteBuf buf = Unpooled.wrappedBuffer(data.get(id));

            if (isFirst)
                isFirst = false;
            else
                sb.append("\n\t");
            sb.append(id);
            sb.append(" = ");
            sb.append(ASNOctetString.printDataArr(buf));
        }

        NationalLanguageLockingShiftIdentifierImpl nllsi = this.getNationalLanguageLockingShift();
        NationalLanguageSingleShiftIdentifierImpl nlssi = this.getNationalLanguageSingleShift();
        ConcatenatedShortMessagesIdentifierImpl csmi = this.getConcatenatedShortMessagesIdentifier();
        ApplicationPortAddressing16BitAddressImpl apa16 = this.getApplicationPortAddressing16BitAddress();
        if (nllsi != null) {
            sb.append(", NationalLanguageLockingShiftIdentifier = [");
            sb.append(nllsi);
            sb.append("]");
        }
        if (nlssi != null) {
            sb.append(", NationalLanguageSingleShiftIdentifier = [");
            sb.append(nlssi);
            sb.append("]");
        }
        if (csmi != null) {
            sb.append(", ConcatenatedShortMessagesIdentifier = [");
            sb.append(csmi);
            sb.append("]");
        }
        if (apa16 != null) {
            sb.append(", ApplicationPortAddressing16BitAddress = [");
            sb.append(apa16);
            sb.append("]");
        }

        sb.append("]");

        return sb.toString();
    }
}
