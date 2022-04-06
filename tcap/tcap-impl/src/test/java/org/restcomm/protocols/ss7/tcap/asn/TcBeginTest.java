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
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;

import org.restcomm.protocols.ss7.tcap.TCAPTestUtils;
import org.restcomm.protocols.ss7.tcap.asn.comp.ASNInvokeParameterImpl;
import org.restcomm.protocols.ss7.tcap.asn.comp.ASNReturnResultParameterImpl;
import org.restcomm.protocols.ss7.tcap.asn.comp.BaseComponent;
import org.restcomm.protocols.ss7.tcap.asn.comp.Invoke;
import org.restcomm.protocols.ss7.tcap.asn.comp.InvokeImpl;
import org.restcomm.protocols.ss7.tcap.asn.comp.OperationCode;
import org.restcomm.protocols.ss7.tcap.asn.comp.OperationCodeType;
import org.restcomm.protocols.ss7.tcap.asn.comp.RejectImpl;
import org.restcomm.protocols.ss7.tcap.asn.comp.ReturnErrorImpl;
import org.restcomm.protocols.ss7.tcap.asn.comp.ReturnResult;
import org.restcomm.protocols.ss7.tcap.asn.comp.ReturnResultImpl;
import org.restcomm.protocols.ss7.tcap.asn.comp.ReturnResultLast;
import org.restcomm.protocols.ss7.tcap.asn.comp.ReturnResultLastImpl;
import org.restcomm.protocols.ss7.tcap.asn.comp.TCBeginMessage;
import org.restcomm.protocols.ss7.tcap.asn.tx.ApplicationContextNameImpl;
import org.restcomm.protocols.ss7.tcap.asn.tx.DialogAbortAPDUImpl;
import org.restcomm.protocols.ss7.tcap.asn.tx.DialogRequestAPDUImpl;
import org.restcomm.protocols.ss7.tcap.asn.tx.DialogResponseAPDUImpl;
import org.restcomm.protocols.ss7.tcap.asn.tx.TCBeginMessageImpl;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNParser;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNException;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * @author amit bhayani
 * @author baranowb
 *
 */
@Test(groups = { "asn" })
public class TcBeginTest {

	ASNParser parser=new ASNParser();
    
	@BeforeClass
	public void setUp()
	{		
		parser.loadClass(TCBeginMessageImpl.class);
                
		parser.clearClassMapping(ASNInvokeParameterImpl.class);
		parser.clearClassMapping(ASNUserInformationObjectImpl.class);
		parser.clearClassMapping(ASNReturnResultParameterImpl.class);
		parser.registerAlternativeClassMapping(ASNInvokeParameterImpl.class, TCBeginTestASN1.class);
		parser.registerAlternativeClassMapping(ASNInvokeParameterImpl.class, TCEndTestASN.class);
		parser.registerAlternativeClassMapping(ASNUserInformationObjectImpl.class, UserInformationTestASN2.class);			
		parser.registerAlternativeClassMapping(ASNReturnResultParameterImpl.class, TCBeginTestASN3.class);
		
		parser.clearClassMapping(ASNDialogPortionObjectImpl.class);
    	parser.registerAlternativeClassMapping(ASNDialogPortionObjectImpl.class, DialogRequestAPDUImpl.class);
    	parser.registerAlternativeClassMapping(ASNDialogPortionObjectImpl.class, DialogResponseAPDUImpl.class);
    	parser.registerAlternativeClassMapping(ASNDialogPortionObjectImpl.class, DialogAbortAPDUImpl.class);
    	
    	parser.clearClassMapping(ASNComponentPortionObjectImpl.class);
    	parser.registerAlternativeClassMapping(ASNComponentPortionObjectImpl.class, InvokeImpl.class);
    	parser.registerAlternativeClassMapping(ASNComponentPortionObjectImpl.class, ReturnResultImpl.class);
    	parser.registerAlternativeClassMapping(ASNComponentPortionObjectImpl.class, ReturnResultLastImpl.class);
    	parser.registerAlternativeClassMapping(ASNComponentPortionObjectImpl.class, RejectImpl.class);
    	parser.registerAlternativeClassMapping(ASNComponentPortionObjectImpl.class, ReturnErrorImpl.class); 
	}
	
    /**
     * The data for this comes from nad1053.pcap USSD Wireshark Trace 2nd Packet
     *
     * @throws IOException
     * @throws ParseException
     */
    @Test(groups = { "functional.encode" })
    public void testBasicTCBeginEncode() throws ParseException {
    	
    	TCBeginMessage tcm = TcapFactory.createTCBeginMessage();
        // tcm.setOriginatingTransactionId(1358955064L);
        tcm.setOriginatingTransactionId(Unpooled.wrappedBuffer(new byte[] { 0x51, 0x00, 0x02, 0x38 }));

        // Create Dialog Portion
        DialogPortion dp = TcapFactory.createDialogPortion(); 
        dp.setUnidirectional(false);
        
        DialogRequestAPDUImpl diRequestAPDUImpl = new DialogRequestAPDUImpl();

        ApplicationContextNameImpl acn = new ApplicationContextNameImpl();
        acn.setOid(Arrays.asList(new Long[] { 0L, 4L, 0L, 0L, 1L, 0L, 19L, 2L }));

        diRequestAPDUImpl.setApplicationContextName(acn);

        UserInformationImpl userInfo = new UserInformationImpl();
        
        userInfo.setIdentifier(Arrays.asList(new Long[] { 0L, 4L, 0L, 0L, 1L, 1L, 1L, 1L }));
        userInfo.setChild(Unpooled.wrappedBuffer(new byte[] { (byte) 0xa0, (byte) 0x80, (byte) 0x80, 0x09, (byte) 0x96, 0x02, 0x24, (byte) 0x80,
                0x03, 0x00, (byte) 0x80, 0x00, (byte) 0xf2, (byte) 0x81, 0x07, (byte) 0x91, 0x13, 0x26, (byte) 0x98,
                (byte) 0x86, 0x03, (byte) 0xf0, 0x00, 0x00 }));

        diRequestAPDUImpl.setUserInformation(userInfo);

        dp.setDialogAPDU(diRequestAPDUImpl);

        tcm.setDialogPortion(dp);

        // INVOKE Component

        Invoke invComp = TcapFactory.createComponentInvoke();
        invComp.setInvokeId(-128);

        // Operation Code
        invComp.setOperationCode(591);

        // Sequence of Parameter
        ASNOctetString p1=new ASNOctetString(Unpooled.wrappedBuffer(new byte[] { 0x0f }),null,null,null,false);
        ASNOctetString p2=new ASNOctetString(Unpooled.wrappedBuffer(new byte[] { (byte) 0xaa, (byte) 0x98, (byte) 0xac, (byte) 0xa6, 0x5a, (byte) 0xcd, 0x62, 0x36, 0x19, 0x0e,
                0x37, (byte) 0xcb, (byte) 0xe5, 0x72, (byte) 0xb9, 0x11 }),null,null,null,false);
        ASNOctetString p3=new ASNOctetString(Unpooled.wrappedBuffer(new byte[] { (byte) 0x91, 0x13, 0x26, (byte) 0x88, (byte) 0x83, 0x00, (byte) 0xf2 }),null,null,null,false);

        TCBeginTestASN1 p = new TCBeginTestASN1();
        p.setO1(Arrays.asList(new ASNOctetString[] {p1, p2}));
        p.setO2(p3);

        invComp.setParameter(p);

        tcm.setComponents(Arrays.asList(new BaseComponent[] { invComp }));

        try {
        	parser.encode(tcm);
        }
        catch(ASNException ex) {
        	assertEquals(1,2);
        }
        
        //byte[] encodedData = aos.toByteArray();

        // Add more here?

    }

    @Test(groups = { "functional.encode", "functional.decode" })
    public void testBasicTCBegin() throws ParseException {

        // no idea how to check rest...?

        // trace
        byte[] b = new byte[] {
                // TCBegin
                0x62, 38,
                // oidTx
                // OrigTran ID (full)............ 145031169
                0x48, 4, 8, (byte) 0xA5, 0, 1,
                // dialog portion
                0x6B, 30,
                // extrnal tag
                0x28, 28,
                // oid
                0x06, 7, 0, 17, (byte) 134, 5, 1, 1, 1, (byte) // asn
                160, 17,
                // DialogPDU - Request
                0x60, 15, (byte) // protocol version
                0x80, 2, 7, (byte) 0x80, (byte) // acn
                161, 9,
                // oid
                6, 7, 4, 0, 1, 1, 1, 3, 0

        };

        Object value=null;
        try {
        	value=parser.decode(Unpooled.wrappedBuffer(b)).getResult();
        }
        catch(ASNException ex) {
        	assertEquals(1,2);
        }
        
        TCBeginMessage tcm = (TCBeginMessage)value;

        // assertEquals(145031169L, tcm.getOriginatingTransactionId(), "Originating transaction id does not match");
        assertTrue(InvokeTest.byteBufEquals(tcm.getOriginatingTransactionId(), Unpooled.wrappedBuffer(new byte[] { 8, (byte) 0xA5, 0, 1 })),
                "Originating transaction id does not match");
        assertNotNull(tcm.getDialogPortion(), "Dialog portion should not be null");

        assertNull(tcm.getComponents(), "Component portion should not be present");

        DialogPortion dp = tcm.getDialogPortion();

        assertFalse(dp.isUnidirectional(), "Dialog should not be Uni");

        DialogAPDU dapdu = dp.getDialogAPDU();
        assertNotNull(dapdu, "APDU should not be null");
        assertEquals(DialogAPDUType.Request, dapdu.getType(), "Wrong APDU type");
        // rest is checked in dialog portion type!
        try {
	        ByteBuf data=parser.encode(tcm);
	        byte[] encoded = data.array();
	
	        TCAPTestUtils.compareArrays(b, encoded);
        }
        catch(ASNException ex) {
        	assertEquals(1,2);
        }
    }

    @Test
    public void testBasicTCBegin1() throws ParseException, ASNException {

    	// no idea how to check rest...?

        // trace
        byte[] b = new byte[] { 0x62, 0x74, 0x48, 0x04, 0x1a, 0x00, 0x02, (byte) 0xe1, 0x6b, 0x54, 0x28, 0x52, 0x06, 0x07,
                0x00, 0x11, (byte) 0x86, 0x05, 0x01, 0x01, 0x01, (byte) 0xa0, 0x47, 0x60, 0x45, (byte) 0x80, 0x02, 0x07,
                (byte) 0x80, (byte) 0xa1, 0x09, 0x06, 0x07, 0x04, 0x00, 0x00, 0x01, 0x00, 0x13, 0x02, (byte) 0xbe, 0x34, 0x28,
                0x32, 0x06, 0x07, 0x04, 0x00, 0x00, 0x01, 0x01, 0x01, 0x01, (byte) 0xa0, 0x27, (byte) 0xa0, 0x25, (byte) 0x80,
                0x08, 0x33, 0x08, 0x05, 0x09, 0x13, 0x17, 0x65, (byte) 0xf0, (byte) 0x81, 0x07, (byte) 0x91, (byte) 0x81, 0x67,
                (byte) 0x83, (byte) 0x80, 0x38, (byte) 0xf4, (byte) 0x82, 0x07, (byte) 0x91, 0x05, 0x39, 0x07, 0x70, 0x63,
                (byte) 0xf6, (byte) 0x83, 0x07, (byte) 0x91, (byte) 0x81, 0x67, (byte) 0x83, (byte) 0x80, 0x48, (byte) 0xf7,
                0x6c, (byte) 0x80, (byte) 0xa1, 0x12, 0x02, 0x01, 0x00, 0x02, 0x01, 0x3b, 0x30, 0x0a, 0x04, 0x01, 0x0f, 0x04,
                0x05, 0x2a, 0x59, 0x6c, 0x36, 0x02, 0x00, 0x00 };

        // encoded data - we use only definite length now
        byte[] b2 = new byte[] { 98, 114, 72, 4, 26, 0, 2, -31, 107, 84, 40, 82, 6, 7, 0, 17, -122, 5, 1, 1, 1, -96, 71, 96,
                69, -128, 2, 7, -128, -95, 9, 6, 7, 4, 0, 0, 1, 0, 19, 2, -66, 52, 40, 50, 6, 7, 4, 0, 0, 1, 1, 1, 1, -96, 39,
                -96, 37, -128, 8, 51, 8, 5, 9, 19, 23, 101, -16, -127, 7, -111, -127, 103, -125, -128, 56, -12, -126, 7, -111,
                5, 57, 7, 112, 99, -10, -125, 7, -111, -127, 103, -125, -128, 72, -9, 108, 20, -95, 18, 2, 1, 0, 2, 1, 59, 48,
                10, 4, 1, 15, 4, 5, 42, 89, 108, 54, 2 };

        Object output=parser.decode(Unpooled.wrappedBuffer(b)).getResult();
        assertTrue(output instanceof TCBeginMessage, "Expected TCInvoke");
        TCBeginMessage tcm = (TCBeginMessageImpl)output;

        // assertEquals(436208353L, tcm.getOriginatingTransactionId(), "Originating transaction id does not match");
        assertTrue(InvokeTest.byteBufEquals(tcm.getOriginatingTransactionId(), Unpooled.wrappedBuffer(new byte[] { 0x1A, 0x00, 0x02, (byte) 0xE1 })),
                "Originating transaction id does not match");
        assertNotNull(tcm.getDialogPortion(), "Dialog portion should not be null");

        assertNotNull(tcm.getComponents(), "Component portion should be present");

        DialogPortion dp = tcm.getDialogPortion();

        assertFalse(dp.isUnidirectional(), "Dialog should not be Uni");

        DialogAPDU dapdu = dp.getDialogAPDU();
        assertNotNull(dapdu, "APDU should not be null");
        assertEquals(DialogAPDUType.Request, dapdu.getType(), "Wrong APDU type");
        // rest is checked in dialog portion type!
        ByteBuf outputBuf=parser.encode(tcm);
        byte[] encoded = outputBuf.array();
        TCAPTestUtils.compareArrays(b2, encoded);
    }

    @Test
    public void testTCBeginMessage_No_Dialog() throws ParseException, ASNException {

    	// no idea how to check rest...?

        // created by hand
        byte[] b = new byte[] {
                // TCBegin
                0x62, 46,
                // org txid
                // OrigTran ID (full)............ 145031169
                0x48, 0x04, 0x08, (byte) 0xA5, 0, 0x01,
                // dtx

                // dialog portion
                // empty
                // comp portion
                0x6C, 38,
                // invoke
                (byte) 0xA1, 6,
                // invoke ID
                0x02, 0x01, 0x01,
                // op code
                0x02, 0x01, 0x37,
                // return result
                (byte) 0xA7, 28,
                // inoke id
                0x02, 0x01, 0x02,
                // sequence start
                0x30, 23,
                // local operation
                0x02, 0x01, 0x01,
                // parameter
                (byte) 0xA0,// some tag.1
                18, (byte) 0x80,// some tag.1.1
                2, 0x11, 0x11, (byte) 0xA1,// some tag.1.2
                04, (byte) 0x82, // some tag.1.3 ?
                2, 0x00, 0x00, (byte) 0x82, 1,// some tag.1.4
                12, (byte) 0x83, // some tag.1.5
                2, 0x33, 0x33, (byte) 0xA1,// some trash here
        // tension indicator 2........ ???
        // use value.................. ???
        };

        Object output=parser.decode(Unpooled.wrappedBuffer(b)).getResult();
        assertTrue(output instanceof TCBeginMessage, "Expected TCBegin");
        TCBeginMessage tcm = (TCBeginMessageImpl)output;

        assertNull(tcm.getDialogPortion(), "Dialog portion should not be present");

        // assertEquals(145031169L, tcm.getOriginatingTransactionId(), "Originating transaction id does not match");
        assertTrue(InvokeTest.byteBufEquals(tcm.getOriginatingTransactionId(), Unpooled.wrappedBuffer(new byte[] { 0x08, (byte) 0xA5, 0, 0x01 })),
                "Originating transaction id does not match");
        // comp portion
        assertNotNull(tcm.getComponents(), "Component portion should be present");
        assertEquals(2, tcm.getComponents().size(), "Component count is wrong");
        BaseComponent c = tcm.getComponents().get(0);
        assertTrue(c instanceof Invoke, "Wrong component type");
        Invoke i = (Invoke)c;
        assertEquals(new Integer(1), i.getInvokeId(), "Wrong invoke ID");
        assertNull(i.getLinkedId(), "Linked ID is not null");

        c = tcm.getComponents().get(1);
        assertTrue(c instanceof ReturnResult, "Wrong component type");
        ReturnResult rr = (ReturnResult)c;
        assertEquals(new Integer(2), rr.getInvokeId(), "Wrong invoke ID");
        assertNotNull(rr.getOperationCode(), "Operation code should not be null");

        OperationCode ocs = rr.getOperationCode();

        assertEquals(OperationCodeType.Local, ocs.getOperationType(), "Wrong Operation Code type");
        assertEquals(new Integer(1), ocs.getLocalOperationCode(), "Wrong Operation Code");

        assertNotNull(rr.getParameter(), "Parameter should not be null");

        ByteBuf outputBuf=parser.encode(tcm);
        byte[] encoded = outputBuf.array();
        TCAPTestUtils.compareArrays(b, encoded);
    }

    @Test
    public void testTCBeginMessage_No_Component() throws ParseException, ASNException {

    	// created by hand
        byte[] b = new byte[] {
                // TCBegin
                0x62, 50,
                // org txid
                // OrigTran ID (full)............ 145031169
                0x48, 0x04, 0x08, (byte) 0xA5, 0, 0x01,

                // dialog portion
                0x6B, 42,
                // extrnal tag
                0x28, 40,
                // oid
                0x06, 7, 0, 17, (byte) 134, 5, 1, 1, 1, (byte) 160, // asn

                29, 0x61, // dialog response
                27,
                // protocol version
                (byte) 0x80, // protocol version

                2, 7, (byte) 0x80, (byte) 161,// acn
                9,
                // oid
                6, 7, 4, 0, 1, 1, 1, 3, 0,
                // result
                (byte) 0xA2, 0x03, 0x2, 0x1, (byte) 0x0,
                // result source diagnostic
                (byte) 0xA3, 5, (byte) 0x0A2, // provider
                3, 0x02,// int 2
                0x01, (byte) 0x2
        // no user info?
        };

        Object output=parser.decode(Unpooled.wrappedBuffer(b)).getResult();
        assertTrue(output instanceof TCBeginMessage, "Expected TCBegin");
        TCBeginMessage tcm = (TCBeginMessageImpl)output;

        assertNull(tcm.getComponents(), "Component portion should not be present");
        assertNotNull(tcm.getDialogPortion(), "Dialog portion should not be null");
        // assertEquals(145031169L, tcm.getOriginatingTransactionId(), "Originating transaction id does not match");
        assertTrue(InvokeTest.byteBufEquals(tcm.getOriginatingTransactionId(), Unpooled.wrappedBuffer(new byte[] { 0x08, (byte) 0xA5, 0, 0x01 })),
                "Originating transaction id does not match");

        assertFalse(tcm.getDialogPortion().isUnidirectional(), "Dialog should not be Uni");
        DialogAPDU _dapd = tcm.getDialogPortion().getDialogAPDU();
        assertEquals(DialogAPDUType.Response, _dapd.getType(), "Wrong dialog APDU type!");

        DialogResponseAPDUImpl dapd = (DialogResponseAPDUImpl) _dapd;

        // check nulls first
        assertNull(dapd.getUserInformation(), "UserInformation should not be present");

        // not nulls
        assertNotNull(dapd.getResult(), "Result should not be null");
        Result r = dapd.getResult();
        assertEquals(ResultType.Accepted, r.getResultType(), "Wrong result");

        assertNotNull(dapd.getResultSourceDiagnostic(), "Result Source Diagnostic should not be null");

        ResultSourceDiagnostic rsd = dapd.getResultSourceDiagnostic();
        assertNull(rsd.getDialogServiceUserType(), "User diagnostic should not be present");
        assertEquals(DialogServiceProviderType.NoCommonDialogPortion, rsd.getDialogServiceProviderType(),
                "Wrong provider diagnostic type");

        ByteBuf outputBuf=parser.encode(tcm);
        byte[] encoded = outputBuf.array();
        TCAPTestUtils.compareArrays(b, encoded);

    }

    @Test
    public void testTCBeginMessage_No_Nothing() throws ParseException, ASNException {

    	// no idea how to check rest...?

        // created by hand
        byte[] b = new byte[] {
                // TCBegin
                0x62, 6,
                // org txid
                // OrigTran ID (full)............ 145031169
                0x48, 0x04, 0x08, (byte) 0xA5, 0, 0x01,
        // didTx

        };

        Object output=parser.decode(Unpooled.wrappedBuffer(b)).getResult();
        assertTrue(output instanceof TCBeginMessage, "Expected TCBegin");
        TCBeginMessage tcm = (TCBeginMessageImpl)output;

        assertNull(tcm.getDialogPortion(), "Dialog portion should be null");
        assertNull(tcm.getComponents(), "Component portion should not be present");
        // assertEquals(145031169L, tcm.getOriginatingTransactionId(), "Originating transaction id does not match");
        assertTrue(InvokeTest.byteBufEquals(tcm.getOriginatingTransactionId(), Unpooled.wrappedBuffer(new byte[] { 0x08, (byte) 0xA5, 0, 0x01 })),
                "Originating transaction id does not match");

        ByteBuf outputBuf=parser.encode(tcm);
        byte[] encoded = outputBuf.array();
        TCAPTestUtils.compareArrays(b, encoded);
    }

    @Test
    public void testTCBeginMessage_All() throws ParseException, ASNException {

    	// no idea how to check rest...?

        // created by hand
        byte[] b = new byte[] {
                // TCBegin
                0x62, 91,
                // org txid
                // OrigTran ID (full)............ 145031169
                0x48, 0x04, 0x08, (byte) 0xA5, 0, 0x01,

                // dialog portion
                0x6B, 42,
                // extrnal tag
                0x28, 40,
                // oid
                0x06, 7, 0, 17, (byte) 134, 5, 1, 1, 1, (byte) 160, // asn

                29, 0x61, // dialog response
                27,
                // protocol version
                (byte) 0x80, // protocol version

                2, 7, (byte) 0x80, (byte) 161,// acn
                9,
                // oid
                6, 7, 4, 0, 1, 1, 1, 3, 0,
                // result
                (byte) 0xA2, 0x03, 0x2, 0x1, (byte) 0x01,
                // result source diagnostic
                (byte) 0xA3, 5, (byte) 0x0A2, // provider
                3, 0x02,// int 2
                0x01, (byte) 0x00,
                // no user info?
                // comp portion
                0x6C, 39,
                // invoke
                (byte) 0xA1, 6,
                // invoke ID
                0x02, 0x01, 0x01,
                // op code
                0x02, 0x01, 0x37,
                // return result last
                (byte) 0xA2, 29,
                // inoke id
                0x02, 0x01, 0x02,
                // sequence start
                0x30, 24,
                // local operation
                0x02, 0x02, 0x00, (byte) 0xFF,
                // parameter
                (byte) 0xA0,// some tag.1
                18, (byte) 0x80,// some tag.1.1
                2, 0x11, 0x11, (byte) 0xA1,// some tag.1.2
                04, (byte) 0x82, // some tag.1.3 ?
                2, 0x00, 0x00, (byte) 0x82, 1,// some tag.1.4
                12, (byte) 0x83, // some tag.1.5
                2, 0x33, 0x33, (byte) 0xA1,// some trash here
        // tension indicator 2........ ???
        // use value.................. ???

        };

        Object output=parser.decode(Unpooled.wrappedBuffer(b)).getResult();
        assertTrue(output instanceof TCBeginMessage, "Expected TCBegin");
        TCBeginMessage tcm = (TCBeginMessageImpl)output;

        // universal
        assertTrue(InvokeTest.byteBufEquals(tcm.getOriginatingTransactionId(), Unpooled.wrappedBuffer(new byte[] { 0x08, (byte) 0xA5, 0, 0x01 })),
                "Originating transaction id does not match");

        // dialog portion
        assertNotNull(tcm.getDialogPortion(), "Dialog portion should not be null");

        assertFalse(tcm.getDialogPortion().isUnidirectional(), "Dialog should not be Uni");
        DialogAPDU _dapd = tcm.getDialogPortion().getDialogAPDU();
        assertEquals(DialogAPDUType.Response, _dapd.getType(), "Wrong dialog APDU type!");

        DialogResponseAPDUImpl dapd = (DialogResponseAPDUImpl) _dapd;

        // check nulls first
        assertNull(dapd.getUserInformation(), "UserInformation should not be present");

        // not nulls
        assertNotNull(dapd.getResult(), "Result should not be null");
        Result r = dapd.getResult();
        assertEquals(ResultType.RejectedPermanent, r.getResultType(), "Wrong result");

        assertNotNull(dapd.getResultSourceDiagnostic(), "Result Source Diagnostic should not be null");

        ResultSourceDiagnostic rsd = dapd.getResultSourceDiagnostic();
        assertNull(rsd.getDialogServiceUserType(), "User diagnostic should not be present");
        assertEquals(DialogServiceProviderType.Null, rsd.getDialogServiceProviderType(), "Wrong provider diagnostic type");

        // comp portion
        assertNotNull(tcm.getComponents(), "Component portion should be present");
        assertEquals(2, tcm.getComponents().size(), "Component count is wrong");
        BaseComponent c = tcm.getComponents().get(0);
        assertTrue(c instanceof Invoke, "Wrong component type");
        Invoke i = (Invoke)c;
        assertEquals(new Integer(1), i.getInvokeId(), "Wrong invoke ID");
        assertNull(i.getLinkedId(), "Linked ID is not null");

        c = tcm.getComponents().get(1);
        assertTrue(c instanceof ReturnResultLast, "Wrong component type");
        ReturnResultLast rrl = (ReturnResultLast)c;
        assertEquals(new Integer(2), rrl.getInvokeId(), "Wrong invoke ID");
        assertNotNull(rrl.getOperationCode(), "Operation code should not be null");

        OperationCode ocs = rrl.getOperationCode();

        assertEquals(OperationCodeType.Local, ocs.getOperationType(), "Wrong Operation Code type");
        assertEquals(new Integer(0x00FF), ocs.getLocalOperationCode(), "Wrong Operation Code");

        assertNotNull(rrl.getParameter(), "Parameter should not be null");

        ByteBuf outputBuf=parser.encode(tcm);
        byte[] encoded = outputBuf.array();        
        TCAPTestUtils.compareArrays(b, encoded);
    }

    @Test
    public void testRealTrace() throws Exception {
    	byte[] TCAP = new byte[] {
                // TCBeginTag
                98,
                // len
                -127,
                -113,
                // Orig tx Tag
                72,
                // tx id len
                4,
                110,
                0,
                2,
                78,
                // Dialog portion tag
                107,
                // DP len
                30, 40, 28, 6, 7, 0, 17, -122, 5, 1, 1, 1, -96, 17, 96, 15, -128, 2, 7, -128, -95, 9,
                6,
                7,
                4,
                0,
                0,
                1,
                0,
                50,
                1,
                // Component portion
                108,
                // undefined len
                -128,
                // Invoke tag
                -95,
                // invoke len
                99, 2, 1, 0, 2, 1, 0, 48, 91, -128, 1, 8, -126, 8, -124, 16, -105, 32, -125, 32, 104, 6, -125, 7, 3, 19, 9, 50,
                38, 89, 24, -123, 1, 10, -118, 8, -124, -109, -105, 32, 115, 0, 2, 1, -69, 5, -128, 3, -128, -112, -93, -100,
                1, 12, -97, 50, 8, 82, 0, 7, 50, 1, 86, 4, -14, -65, 53, 3, -125, 1, 17, -97, 54, 5, -11, 51, 3, 0, 1, -97, 55,
                7, -111, -105, 32, 115, 0, 2, -15, -97, 57, 8, 2, 1, 80, 65, 49, -128, 116, 97,
                // end tag!
                0, 0 };
        
        Object output=parser.decode(Unpooled.wrappedBuffer(TCAP)).getResult();
        assertTrue(output instanceof TCBeginMessage, "Expected TCBegin");               
    }

    public static final String dump(byte[] buff, int size, boolean asBits) {
        String s = "";
        for (int i = 0; i < size; i++) {
            String ss = null;
            if (!asBits) {
                ss = Integer.toHexString(buff[i] & 0xff);
            } else {
                ss = Integer.toBinaryString(buff[i] & 0xff);
            }
            ss = fillInZeroPrefix(ss, asBits);
            s += " " + ss;
        }
        return s;
    }

    public static final String fillInZeroPrefix(String ss, boolean asBits) {
        if (asBits) {
            if (ss.length() < 8) {
                for (int j = ss.length(); j < 8; j++) {
                    ss = "0" + ss;
                }
            }
        } else {
            // hex
            if (ss.length() < 2) {

                ss = "0" + ss;
            }
        }

        return ss;
    }

    @Test(groups = { "functional.encode" })
    public void testA() throws ParseException, ASNException {
    	TCBeginMessage tcm = TcapFactory.createTCBeginMessage();
        // tcm.setOriginatingTransactionId(1358955064L);
        tcm.setOriginatingTransactionId(Unpooled.wrappedBuffer(new byte[] { 0x51, 0x00, 0x02, 0x38 }));

        // Create Dialog Portion
        DialogPortion dp = TcapFactory.createDialogPortion();

        dp.setUnidirectional(false);
        DialogRequestAPDUImpl diRequestAPDUImpl = new DialogRequestAPDUImpl();

        ApplicationContextNameImpl acn = new ApplicationContextNameImpl();
        acn.setOid(Arrays.asList(new Long[] { 0L, 4L, 0L, 0L, 1L, 0L, 19L, 2L }));

        diRequestAPDUImpl.setApplicationContextName(acn);
        UserInformationImpl userInfo = new UserInformationImpl();
        
        userInfo.setIdentifier(Arrays.asList(new Long[] { 0L, 4L, 0L, 0L, 1L, 1L, 1L, 1L }));
        
        UserInformationTestASN innerASN=new UserInformationTestASN(Unpooled.wrappedBuffer(new byte[] { (byte) 0x80, 0x09, (byte) 0x96, 0x02, 0x24, (byte) 0x80,
                0x03, 0x00, (byte) 0x80, 0x00, (byte) 0xf2, (byte) 0x81, 0x07, (byte) 0x91, 0x13, 0x26, (byte) 0x98,
                (byte) 0x86, 0x03, (byte) 0xf0 }));
        
        ASNUserInformationObjectImpl object=new ASNUserInformationObjectImpl(innerASN);
        userInfo.setChildAsObject(object);
        
        diRequestAPDUImpl.setUserInformation(userInfo);

        dp.setDialogAPDU(diRequestAPDUImpl);

        tcm.setDialogPortion(dp);

        // INVOKE Component

        Invoke invComp = TcapFactory.createComponentInvoke();
        invComp.setInvokeId(-128);

        // Operation Code
        invComp.setOperationCode(591);

        TCBeginTestASN2 parameter=new TCBeginTestASN2();
        ASNOctetString o1=new ASNOctetString(Unpooled.wrappedBuffer(new byte[] { 0x0f }),null,null,null,false);
        parameter.setO1(o1);
        
        ASNOctetString o2=new ASNOctetString(Unpooled.wrappedBuffer(new byte[] { (byte) 0xaa, (byte) 0x98, (byte) 0xac, (byte) 0xa6, 0x5a, (byte) 0xcd, 0x62, 0x36, 0x19, 0x0e,
                0x37, (byte) 0xcb, (byte) 0xe5, 0x72, (byte) 0xb9, 0x11 }),null,null,null,false);
        parameter.setO2(o2);
        
        ASNOctetString o3=new ASNOctetString(Unpooled.wrappedBuffer(new byte[] { (byte) 0x91, 0x13, 0x26, (byte) 0x88, (byte) 0x83, 0x00, (byte) 0xf2 }),null,null,null,false);
        parameter.setO2(o3);
        
        invComp.setParameter(parameter);

        tcm.setComponents(Arrays.asList(new BaseComponent[] { invComp }));

        parser.encode(tcm);
        
        //byte[] encodedData = aos.toByteArray();

        // Add more here?
    }
}