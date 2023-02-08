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

package org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.primitive;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;

import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.VariablePart;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.MessageIDImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.MessageIDTextImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.MessageIDWrapperImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.VariableMessageImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.VariablePartImpl;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class MessageIDTest {

    public byte[] getData1() {
        return new byte[] { 48, 3, (byte) 128, 1, 123 };
    }

    public byte[] getData2() {
        return new byte[] { 48, 9, (byte) 161, 7, (byte) 128, 5, 84, 111, 100, 97, 121 };
    }

    public byte[] getData3() {
        return new byte[] { 48, 8, (byte) 189, 6, 2, 1, 0, 2, 1, (byte) 255 };
    }

    public byte[] getData4() {
        return new byte[] { 48, 10, (byte) 190, 8, (byte) 128, 1, 99, (byte) 161, 3, (byte) 128, 1, 28 };
    }

    @Test(groups = { "functional.decode", "circuitSwitchedCall.primitive" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(MessageIDWrapperImpl.class);
    	
    	byte[] rawData = this.getData1();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof MessageIDWrapperImpl);
        
        MessageIDWrapperImpl elem = (MessageIDWrapperImpl)result.getResult();                
        assertEquals((int) elem.getMessageID().getElementaryMessageID(), 123);
        assertNull(elem.getMessageID().getText());
        assertNull(elem.getMessageID().getElementaryMessageIDs());
        assertNull(elem.getMessageID().getVariableMessage());

        rawData = this.getData2();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof MessageIDWrapperImpl);
        
        elem = (MessageIDWrapperImpl)result.getResult(); 
        assertNull(elem.getMessageID().getElementaryMessageID());
        assertTrue(elem.getMessageID().getText().getMessageContent().equals("Today"));
        assertNull(elem.getMessageID().getText().getAttributes());
        assertNull(elem.getMessageID().getElementaryMessageIDs());
        assertNull(elem.getMessageID().getVariableMessage());

        rawData = this.getData3();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof MessageIDWrapperImpl);
        
        elem = (MessageIDWrapperImpl)result.getResult(); 
        assertNull(elem.getMessageID().getElementaryMessageID());
        assertNull(elem.getMessageID().getText());
        assertEquals(elem.getMessageID().getElementaryMessageIDs().size(), 2);
        assertEquals((int) elem.getMessageID().getElementaryMessageIDs().get(0), 0);
        assertEquals((int) elem.getMessageID().getElementaryMessageIDs().get(1), -1);
        assertNull(elem.getMessageID().getVariableMessage());

        rawData = this.getData4();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof MessageIDWrapperImpl);
        
        elem = (MessageIDWrapperImpl)result.getResult(); 
        assertNull(elem.getMessageID().getElementaryMessageID());
        assertNull(elem.getMessageID().getText());
        assertNull(elem.getMessageID().getElementaryMessageIDs());
        assertEquals(elem.getMessageID().getVariableMessage().getElementaryMessageID(), 99);
        assertEquals(elem.getMessageID().getVariableMessage().getVariableParts().size(), 1);
        assertEquals((int) elem.getMessageID().getVariableMessage().getVariableParts().get(0).getInteger(), 28);
    }

    @Test(groups = { "functional.encode", "circuitSwitchedCall.primitive" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(MessageIDWrapperImpl.class);
    	
        MessageIDImpl elem = new MessageIDImpl(123);
        MessageIDWrapperImpl wrapper = new MessageIDWrapperImpl(elem);
        byte[] rawData = this.getData1();
        ByteBuf buffer=parser.encode(wrapper);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        MessageIDTextImpl text = new MessageIDTextImpl("Today", null);
        elem = new MessageIDImpl(text);
        wrapper = new MessageIDWrapperImpl(elem);
        rawData = this.getData2();
        buffer=parser.encode(wrapper);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        ArrayList<Integer> elementaryMessageIDs = new ArrayList<Integer>();
        elementaryMessageIDs.add(0);
        elementaryMessageIDs.add(-1);
        elem = new MessageIDImpl(elementaryMessageIDs);
        wrapper = new MessageIDWrapperImpl(elem);
        rawData = this.getData3();
        buffer=parser.encode(wrapper);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        ArrayList<VariablePart> variableParts = new ArrayList<VariablePart>();
        VariablePartImpl vp = new VariablePartImpl(28);
        variableParts.add(vp);
        VariableMessageImpl vm = new VariableMessageImpl(99, variableParts);
        elem = new MessageIDImpl(vm);
        wrapper = new MessageIDWrapperImpl(elem);
        rawData = this.getData4();
        buffer=parser.encode(wrapper);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
        // int elementaryMessageID, ArrayList<VariablePart> variableParts
    }
}
