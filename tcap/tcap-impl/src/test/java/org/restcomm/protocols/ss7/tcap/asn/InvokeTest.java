/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
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

package org.restcomm.protocols.ss7.tcap.asn;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.Arrays;

import org.restcomm.protocols.ss7.tcap.asn.ParseException;
import org.restcomm.protocols.ss7.tcap.asn.TcapFactory;
import org.restcomm.protocols.ss7.tcap.asn.comp.ASNInvokeParameterImpl;
import org.restcomm.protocols.ss7.tcap.asn.comp.ComponentImpl;
import org.restcomm.protocols.ss7.tcap.asn.comp.ComponentType;
import org.restcomm.protocols.ss7.tcap.asn.comp.GlobalOperationCodeImpl;
import org.restcomm.protocols.ss7.tcap.asn.comp.LocalOperationCodeImpl;
import org.restcomm.protocols.ss7.tcap.asn.comp.OperationCode;
import org.restcomm.protocols.ss7.tcap.asn.comp.OperationCodeType;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNException;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNGeneric;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

/**
 * The trace is from nad1053.pcap wirehsark trace
 *
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
@Test(groups = { "asn" })
public class InvokeTest {

    private byte[] getData() {
        return new byte[] {
        		0x6c, 0x1f, //Component Tag
                (byte) 0xa1, // Invoke Tag
                0x1d, // Length Dec 29
                0x02, 0x01,
                0x0c, // Invoke ID TAG(2) Length(1) Value(12)
                0x02, 0x01,
                0x3b, // Operation Code TAG(2), Length(1), Value(59)

                // Sequence of parameter
                0x30, 0x15,
                // Parameter 1
                0x04, 0x01, 0x0f,
                // Parameter 2
                0x04, 0x10, (byte) 0xaa, (byte) 0x98, (byte) 0xac, (byte) 0xa6, 0x5a, (byte) 0xcd, 0x62, 0x36, 0x19, 0x0e,
                0x37, (byte) 0xcb, (byte) 0xe5, 0x72, (byte) 0xb9, 0x11 };
    }

    private byte[] getDataFull() {
        return new byte[] { 108, 18, -95, 16, 2, 1, -5, -128, 1, 2, 6, 3, 40, 0, 1, 4, 3, 11, 22, 33 };
    }

    @BeforeClass
	public void setUp()
	{		
    	ASNGeneric.clear(ASNInvokeParameterImpl.class);
    	ASNGeneric.registerAlternative(ASNInvokeParameterImpl.class, InvokeTestASN.class);    	
	}
    
    @Test(groups = { "functional.encode" })
    public void testEncode() throws ASNException {

    	ASNParser parser=new ASNParser();
    	parser.loadClass(ComponentImpl.class);
    	
        byte[] expected = this.getData();

        ComponentImpl invoke = TcapFactory.createComponentInvoke();
        invoke.getInvoke().setInvokeId(12l);

        OperationCode oc = TcapFactory.createLocalOperationCode();

        ((LocalOperationCodeImpl)oc).setLocalOperationCode(59L);
        invoke.getInvoke().setOperationCode(oc);

        InvokeTestASN invokeParameter=new InvokeTestASN();
        
        ASNOctetString o1=new ASNOctetString();
        o1.setValue(Unpooled.wrappedBuffer(new byte[] { 0x0F }));
        
        ASNOctetString o2=new ASNOctetString();
        o2.setValue(Unpooled.wrappedBuffer(new byte[] { (byte) 0xaa, (byte) 0x98, (byte) 0xac, (byte) 0xa6, 0x5a, (byte) 0xcd, 0x62, 0x36, 0x19, 0x0e,
                0x37, (byte) 0xcb, (byte) 0xe5, 0x72, (byte) 0xb9, 0x11 }));
        
        invokeParameter.setO1(Arrays.asList(new ASNOctetString[] { o1 }));
        invokeParameter.setO2(o2);
        invoke.getInvoke().setParameter(invokeParameter);

        ByteBuf buffer=parser.encode(invoke);
        byte[] encodedData = buffer.array();
        assertTrue(Arrays.equals(expected, encodedData));

        expected = this.getDataFull();

        invoke = TcapFactory.createComponentInvoke();
        invoke.getInvoke().setInvokeId(-5L);
        invoke.getInvoke().setLinkedId(2L);
        oc = TcapFactory.createGlobalOperationCode();
        ((GlobalOperationCodeImpl)oc).setGlobalOperationCode(Arrays.asList(new Long[] { 1L, 0L, 0L, 1L }));
        invoke.getInvoke().setOperationCode(oc);

        ASNOctetString pm=new ASNOctetString();
        pm.setValue(Unpooled.wrappedBuffer(new byte[] { 11, 22, 33 }));
        invoke.getInvoke().setParameter(pm);
        
        buffer=parser.encode(invoke);
        encodedData = buffer.array();
        assertTrue(Arrays.equals(expected, encodedData));
    }

    @Test(groups = { "functional.decode" })
    public void testDecodeWithParaSequ() throws ParseException, ASNException {

    	ASNParser parser=new ASNParser();
    	parser.loadClass(ComponentImpl.class);
    	
        byte[] b = this.getData();

        Object output=parser.decode(Unpooled.wrappedBuffer(b)).getResult();
        assertTrue(output instanceof ComponentImpl);        
        ComponentImpl invokeComp = (ComponentImpl)output;
        assertEquals(ComponentType.Invoke, invokeComp.getType());

        assertTrue(12L == invokeComp.getInvoke().getInvokeId());
        OperationCode oc = invokeComp.getInvoke().getOperationCode();
        assertNotNull(oc);
        assertEquals(OperationCodeType.Local, oc.getOperationType());
        assertTrue(59 == ((LocalOperationCodeImpl)oc).getLocalOperationCode());
               
        assertTrue(invokeComp.getInvoke().getParameter() instanceof InvokeTestASN);
        InvokeTestASN parameter=(InvokeTestASN)invokeComp.getInvoke().getParameter();
        assertTrue(byteBufEquals(Unpooled.wrappedBuffer(new byte[] { 0x0f }), parameter.getO1().get(0).getValue()));
        assertTrue(byteBufEquals(Unpooled.wrappedBuffer(new byte[] { (byte) 0xaa, (byte) 0x98, (byte) 0xac, (byte) 0xa6, 0x5a, (byte) 0xcd, 0x62,
                0x36, 0x19, 0x0e, 0x37, (byte) 0xcb, (byte) 0xe5, 0x72, (byte) 0xb9, 0x11 }), parameter.getO2().getValue()));

        b = this.getDataFull();

        output=parser.decode(Unpooled.wrappedBuffer(b)).getResult();
        assertTrue(output instanceof ComponentImpl);        
        invokeComp = (ComponentImpl)output;
        assertEquals(ComponentType.Invoke, invokeComp.getType());

        assertEquals(-5L, (long) invokeComp.getInvoke().getInvokeId());
        assertEquals(2L, (long) invokeComp.getInvoke().getLinkedId());
        oc = invokeComp.getInvoke().getOperationCode();
        assertNotNull(oc);
        assertEquals(OperationCodeType.Global, oc.getOperationType());
        assertEquals(Arrays.asList(new Long[] { 1L, 0L, 0L, 1L }), ((GlobalOperationCodeImpl)oc).getGlobalOperationCode());

        assertTrue(invokeComp.getInvoke().getParameter() instanceof ASNOctetString);
        assertTrue(byteBufEquals(Unpooled.wrappedBuffer(new byte[] { 11, 22, 33 }), ((ASNOctetString)invokeComp.getInvoke().getParameter()).getValue()));
    }

    public static Boolean byteBufEquals(ByteBuf value1,ByteBuf value2) {
    	ByteBuf value1Wrapper=Unpooled.wrappedBuffer(value1);
    	ByteBuf value2Wrapper=Unpooled.wrappedBuffer(value2);
    	byte[] value1Arr=new byte[value1Wrapper.readableBytes()];
    	byte[] value2Arr=new byte[value2Wrapper.readableBytes()];
    	value1Wrapper.readBytes(value1Arr);
    	value2Wrapper.readBytes(value2Arr);
    	return Arrays.equals(value1Arr, value2Arr);
    } 
}
