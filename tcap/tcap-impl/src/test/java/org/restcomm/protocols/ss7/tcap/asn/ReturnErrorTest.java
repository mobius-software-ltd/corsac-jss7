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
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.Arrays;
import java.util.List;

import org.restcomm.protocols.ss7.tcap.asn.TcapFactory;
import org.restcomm.protocols.ss7.tcap.asn.comp.ASNReturnErrorParameterImpl;
import org.restcomm.protocols.ss7.tcap.asn.comp.ComponentImpl;
import org.restcomm.protocols.ss7.tcap.asn.comp.ComponentType;
import org.restcomm.protocols.ss7.tcap.asn.comp.ErrorCode;
import org.restcomm.protocols.ss7.tcap.asn.comp.ErrorCodeType;
import org.restcomm.protocols.ss7.tcap.asn.comp.GlobalErrorCodeImpl;
import org.restcomm.protocols.ss7.tcap.asn.comp.LocalErrorCodeImpl;
import org.restcomm.protocols.ss7.tcap.asn.comp.ReturnErrorImpl;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNException;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNGeneric;

/**
 *
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
@Test(groups = { "asn" })
public class ReturnErrorTest {

	@BeforeClass
	public void setUp() {
		ASNGeneric.clear(ASNReturnErrorParameterImpl.class);
		ASNGeneric.registerAlternative(ASNReturnErrorParameterImpl.class, TCBeginTestASN3.class);		
	}
	
    private byte[] getDataWithoutParameter() {
        return new byte[] {
        		108, 8, //Component Impl
        		// 0xA3 - Return ReturnError TAG
                (byte) 0xA3,
                // 0x06 - Len
                0x06,
                // 0x02 - InvokeID Tag
                0x02,
                // 0x01 - Len
                0x01,
                // 0x05
                0x05,
                // 0x02 - ReturnError Code Tag
                0x02,
                // 0x01 - Len
                0x01,
                // 0x0F
                0x0F };
    }

    private byte[] getDataWithParameter() {
        return new byte[] {
        		108, 0x1B, //Component Tag
        		// 0xA3 - Return ReturnError TAG
                (byte) 0xA3,
                // 0x06 - Len
                0x19,
                // 0x02 - InvokeID Tag
                0x02,
                // 0x01 - Len
                0x01,
                // 0x05
                0x05,
                // 0x02 - ReturnError Code Tag
                0x02,
                // 0x01 - Len
                0x01,
                // 0x0F
                0x0F,
                // parameter
                (byte) 0xA0,// some tag.1
                17, (byte) 0x80,// some tag.1.1
                2, 0x11, 0x11, (byte) 0xA1,// some tag.1.2
                04, (byte) 0x82, // some tag.1.3 ?
                2, 0x00, 0x00, (byte) 0x82, 1,// some tag.1.4
                12, (byte) 0x83, // some tag.1.5
                2, 0x33, 0x33, // some trash here

        };
    }

    private byte[] getDataLongErrorCode() {
        return new byte[] { 108, 10,-93, 8, 2, 1, -1, 6, 3, 40, 22, 33 };
    }

    private byte[] getParameterData() {
        return new byte[] { -128, 2, 17, 17, -95, 4, -126, 2, 0, 0, -126, 1, 12, -125, 2, 51, 51 };
    }

    @Test(groups = { "functional.decode" })
    public void testDecode() throws ASNException {
    	ASNParser parser=new ASNParser();
    	parser.loadClass(ComponentImpl.class);
    	
        byte[] b = getDataWithoutParameter();
        Object output=parser.decode(Unpooled.wrappedBuffer(b)).getResult();
        assertTrue(output instanceof ComponentImpl);
        ComponentImpl comp = (ComponentImpl)output;

        assertEquals(ComponentType.ReturnError, comp.getType(), "Wrong component Type");
        ReturnErrorImpl re = comp.getReturnError();
        assertEquals(new Long(5), re.getInvokeId(), "Wrong invoke ID");
        assertNotNull(re.getErrorCode(), "No error code.");
        ErrorCode ec = re.getErrorCode();
        assertEquals(ErrorCodeType.Local, ec.getErrorType(), "Wrong error code type.");
        long lec = ((LocalErrorCodeImpl)ec).getLocalErrorCode();
        assertEquals(lec, 15, "wrong data content.");
        assertNull(re.getParameter());

        b = getDataWithParameter();
        output=parser.decode(Unpooled.wrappedBuffer(b)).getResult();
        assertTrue(output instanceof ComponentImpl);
        comp = (ComponentImpl)output;

        assertEquals(ComponentType.ReturnError, comp.getType(), "Wrong component Type");
        re = comp.getReturnError();
        assertEquals(new Long(5), re.getInvokeId(), "Wrong invoke ID");
        assertNotNull(re.getErrorCode(), "No error code.");
        ec = re.getErrorCode();
        assertEquals(ErrorCodeType.Local, ec.getErrorType(), "Wrong error code type.");
        lec = ((LocalErrorCodeImpl)ec).getLocalErrorCode();
        assertEquals(lec, 15, "wrong data content.");

        assertNotNull(re.getParameter(), "Parameter should not be null");
        Object p = re.getParameter();
        assertTrue(p instanceof TCBeginTestASN3);
        assertTrue(Arrays.equals(this.getParameterData(), ((TCBeginTestASN3)p).getValue()));

        b = getDataLongErrorCode();
        output=parser.decode(Unpooled.wrappedBuffer(b)).getResult();
        assertTrue(output instanceof ComponentImpl);
        comp = (ComponentImpl)output;

        assertEquals(ComponentType.ReturnError, comp.getType(), "Wrong component Type");
        re = comp.getReturnError();
        assertEquals(new Long(-1L), re.getInvokeId(), "Wrong invoke ID");
        assertNotNull(re.getErrorCode(), "No error code.");
        ec = re.getErrorCode();
        assertEquals(ErrorCodeType.Global, ec.getErrorType(), "Wrong error code type.");
        List<Long> gec = ((GlobalErrorCodeImpl)ec).getGlobalErrorCode();
        assertEquals(Arrays.asList(new Long[] { 1L, 0L, 22L, 33L }), gec, "wrong data content.");
        assertNull(re.getParameter());
    }

    @Test(groups = { "functional.encode" })
    public void testEncode() throws ASNException {
    	ASNParser parser=new ASNParser();
    	parser.loadClass(ComponentImpl.class);
    	
        byte[] expected = this.getDataWithoutParameter();
        ComponentImpl re = TcapFactory.createComponentReturnError();
        re.getReturnError().setInvokeId(5l);
        ErrorCode ec = TcapFactory.createLocalErrorCode();
        ((LocalErrorCodeImpl)ec).setLocalErrorCode(15L);
        re.getReturnError().setErrorCode(ec);

        ByteBuf buffer=parser.encode(re);
        byte[] encodedData = buffer.array();
        assertTrue(Arrays.equals(expected, encodedData));

        expected = this.getDataWithParameter();
        re = TcapFactory.createComponentReturnError();
        re.getReturnError().setInvokeId(5l);
        ec = TcapFactory.createLocalErrorCode();
        ((LocalErrorCodeImpl)ec).setLocalErrorCode(15L);
        re.getReturnError().setErrorCode(ec);
        
        TCBeginTestASN3 pm=new TCBeginTestASN3();
        pm.setValue(getParameterData());
        re.getReturnError().setParameter(pm);

        buffer=parser.encode(re);
        encodedData = buffer.array();
        assertTrue(Arrays.equals(expected, encodedData));

        expected = this.getDataLongErrorCode();
        re = TcapFactory.createComponentReturnError();
        re.getReturnError().setInvokeId(-1L);
        ec = TcapFactory.createGlobalErrorCode();
        ((GlobalErrorCodeImpl)ec).setGlobalErrorCode(Arrays.asList(new Long[] { 1L, 0L, 22L, 33L }));
        re.getReturnError().setErrorCode(ec);

        buffer=parser.encode(re);
        encodedData = buffer.array();
        assertTrue(Arrays.equals(expected, encodedData));        
    }
}