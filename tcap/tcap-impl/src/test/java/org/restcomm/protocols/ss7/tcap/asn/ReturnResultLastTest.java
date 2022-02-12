/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.tcap.asn.comp.ASNReturnResultParameterImpl;
import org.restcomm.protocols.ss7.tcap.asn.comp.ComponentImpl;
import org.restcomm.protocols.ss7.tcap.asn.comp.ComponentType;
import org.restcomm.protocols.ss7.tcap.asn.comp.OperationCode;
import org.restcomm.protocols.ss7.tcap.asn.comp.ReturnResult;
import org.restcomm.protocols.ss7.tcap.asn.comp.ReturnResultLast;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNParser;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNException;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
@Test(groups = { "asn" })
public class ReturnResultLastTest {

	ASNParser parser=new ASNParser();
	
	@BeforeClass
	public void setUp() {
		parser.loadClass(ComponentImpl.class);
    	
		parser.clearClassMapping(ASNReturnResultParameterImpl.class);
		parser.registerAlternativeClassMapping(ASNReturnResultParameterImpl.class, TCEndTestASN.class);		
	}
	
    private byte[] getLDataEmpty() {
        return new byte[] { 108, 5, (byte) 162, 3, 2, 1, 0 };
    }

    private byte[] getNLDataEmpty() {
        return new byte[] { 108, 5, (byte) 167, 3, 2, 1, 0 };
    }

    private byte[] getLDataCommon() {
        return new byte[] { 108, 21, (byte) 162, 19, 2, 1, 1, 48, 14, 2, 1, 45, 48, 9, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
    }

    private byte[] getNLDataCommon() {
        return new byte[] { 108, 21, (byte) 167, 19, 2, 1, 1, 48, 14, 2, 1, 45, 48, 9, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
    }

    private byte[] getParameterData() {
        return new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
    }

    @Test(groups = { "functional.decode" })
    public void testDecodeWithParaSequ() throws ASNException {
    	byte[] b = this.getLDataEmpty();
        Object output=parser.decode(Unpooled.wrappedBuffer(b)).getResult();
        ComponentImpl comp = (ComponentImpl)output;
        assertEquals(ComponentType.ReturnResultLast, comp.getType());

        ReturnResultLast rrl = comp.getReturnResultLast();
        assertTrue(0L == rrl.getInvokeId());
        OperationCode oc = rrl.getOperationCode();
        assertNull(oc);
        assertNull(rrl.getParameter());

        b = this.getNLDataEmpty();
        output=parser.decode(Unpooled.wrappedBuffer(b)).getResult();
        comp = (ComponentImpl)output;
        assertEquals(ComponentType.ReturnResult, comp.getType());

        ReturnResult rr = comp.getReturnResult();
        assertTrue(0L == rr.getInvokeId());
        oc = rr.getOperationCode();
        assertNull(oc);
        assertNull(rr.getParameter());

        b = this.getLDataCommon();
        output=parser.decode(Unpooled.wrappedBuffer(b)).getResult();
        comp = (ComponentImpl)output;
        assertEquals(ComponentType.ReturnResultLast, comp.getType());

        rrl = comp.getReturnResultLast();
        assertTrue(1L == rrl.getInvokeId());
        oc = rrl.getOperationCode();
        assertNotNull(oc);
        assertTrue(45 == oc.getLocalOperationCode());
        Object p = rrl.getParameter();
        assertNotNull(p);
        assertTrue(p instanceof TCEndTestASN);
        assertTrue(InvokeTest.byteBufEquals(Unpooled.wrappedBuffer(this.getParameterData()), ((TCEndTestASN)p).getValue()));

        b = this.getNLDataCommon();
        output=parser.decode(Unpooled.wrappedBuffer(b)).getResult();
        comp = (ComponentImpl)output;
        assertEquals(ComponentType.ReturnResult, comp.getType());

        rr = comp.getReturnResult();
        assertTrue(1L == rr.getInvokeId());
        oc = rr.getOperationCode();
        assertNotNull(oc);
        assertTrue(45 == oc.getLocalOperationCode());
        p = rr.getParameter();
        assertNotNull(p);
        assertTrue(p instanceof TCEndTestASN);
        assertTrue(InvokeTest.byteBufEquals(Unpooled.wrappedBuffer(this.getParameterData()), ((TCEndTestASN)p).getValue()));        
    }

    @Test(groups = { "functional.decode" })
    public void testEncode() throws ASNException {
    	byte[] expected = this.getLDataEmpty();
        ReturnResultLast rrl = TcapFactory.createComponentReturnResultLast();
        rrl.setInvokeId(0);

        ComponentImpl comp=new ComponentImpl();
        comp.setReturnResultLast(rrl);
        ByteBuf buffer=parser.encode(comp);
        byte[] encodedData = buffer.array();
        assertTrue(Arrays.equals(expected, encodedData));

        expected = this.getNLDataEmpty();
        ReturnResult rr = TcapFactory.createComponentReturnResult();
        rr.setInvokeId(0);
        comp=new ComponentImpl();
        comp.setReturnResult(rr);
        
        buffer=parser.encode(comp);
        encodedData = buffer.array();
        assertTrue(Arrays.equals(expected, encodedData));

        expected = this.getLDataCommon();
        rrl = TcapFactory.createComponentReturnResultLast();
        rrl.setInvokeId(1);
        rrl.setOperationCode(45);
        TCEndTestASN parameter=new TCEndTestASN(Unpooled.wrappedBuffer(getParameterData()));
        rrl.setParameter(parameter);
        comp=new ComponentImpl();
        comp.setReturnResultLast(rrl);
        
        buffer=parser.encode(comp);
        encodedData = buffer.array();
        assertTrue(Arrays.equals(expected, encodedData));

        expected = this.getNLDataCommon();
        rr = TcapFactory.createComponentReturnResult();
        rr.setInvokeId(1);
        rr.setOperationCode(45);
        parameter=new TCEndTestASN(Unpooled.wrappedBuffer(getParameterData()));
        rr.setParameter(parameter);
        comp=new ComponentImpl();
        comp.setReturnResult(rr);
        
        buffer=parser.encode(comp);
        encodedData = buffer.array();
        assertTrue(Arrays.equals(expected, encodedData));
    }

}
