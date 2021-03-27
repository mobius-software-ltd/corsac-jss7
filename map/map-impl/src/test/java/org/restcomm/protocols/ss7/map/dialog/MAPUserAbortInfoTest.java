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

package org.restcomm.protocols.ss7.map.dialog;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.map.api.dialog.MAPUserAbortChoiseImpl;
import org.restcomm.protocols.ss7.map.api.dialog.ProcedureCancellationReason;
import org.restcomm.protocols.ss7.map.api.dialog.ResourceUnavailableReason;
import org.restcomm.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
public class MAPUserAbortInfoTest {
	
    private byte[] getDataUserSpecificReason() {
        return new byte[] { (byte) 164, 2, (byte) 128, 0 };
    }

    private byte[] getUserResourceLimitationReason() {
        return new byte[] { (byte) 164, 2, (byte) 129, 0 };
    }

    private byte[] getResourceUnavailableReason() {
        return new byte[] { (byte) 164, 3, (byte) 130, 1, 0 };
    }

    private byte[] getProcedureCancellationReason() {
        return new byte[] { (byte) 164, 3, (byte) 131, 1, 4 };
        // return new byte[] { (byte) 0xA4, 0x05, (byte) 0x03, 0x03, 0x0A, 0x01, 0x04 };
    }

    private byte[] getDataFull() {
        return new byte[] { -92, 50, -125, 1, 3, 48, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33 };
    }

    @Test(groups = { "functional.decode", "dialog" })
    public void testUserSpecificReasonDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.loadClass(MAPUserAbortInfoImpl.class);
    	
        // The raw data is hand made
        byte[] data = getDataUserSpecificReason();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof MAPUserAbortInfoImpl);
        MAPUserAbortInfoImpl mapUserAbortInfo = (MAPUserAbortInfoImpl)result.getResult();
        
        MAPUserAbortChoiseImpl mapUserAbortChoice = mapUserAbortInfo.getUserAbortChoise();

        assertNotNull(mapUserAbortChoice);

        assertTrue(mapUserAbortChoice.isUserSpecificReason());
        assertFalse(mapUserAbortChoice.isProcedureCancellationReason());
        assertFalse(mapUserAbortChoice.isResourceUnavailableReason());
        assertFalse(mapUserAbortChoice.isUserResourceLimitation());

    }

    @Test(groups = { "functional.encode", "dialog" })
    public void testUserSpecificReasonEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.loadClass(MAPUserAbortInfoImpl.class);
    	
        MAPUserAbortInfoImpl mapUserAbortInfo = new MAPUserAbortInfoImpl();
        MAPUserAbortChoiseImpl mapUserAbortChoice = new MAPUserAbortChoiseImpl();
        mapUserAbortChoice.setUserSpecificReason();

        mapUserAbortInfo.setUserAbortChoise(mapUserAbortChoice);

        ByteBuf buffer=parser.encode(mapUserAbortInfo);
        byte[] data = new byte[buffer.readableBytes()];
        buffer.readBytes(data);
        assertTrue(Arrays.equals(getDataUserSpecificReason(), data));
    }

    @Test(groups = { "functional.decode", "dialog" })
    public void testUserResourceLimitationDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.loadClass(MAPUserAbortInfoImpl.class);
    	        
        // The raw data is hand made
    	byte[] data=getUserResourceLimitationReason();
    	ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof MAPUserAbortInfoImpl);
        MAPUserAbortInfoImpl mapUserAbortInfo = (MAPUserAbortInfoImpl)result.getResult();
        
        MAPUserAbortChoiseImpl mapUserAbortChoice = mapUserAbortInfo.getUserAbortChoise();

        assertNotNull(mapUserAbortChoice);

        assertFalse(mapUserAbortChoice.isUserSpecificReason());
        assertFalse(mapUserAbortChoice.isProcedureCancellationReason());
        assertFalse(mapUserAbortChoice.isResourceUnavailableReason());
        assertTrue(mapUserAbortChoice.isUserResourceLimitation());

    }

    @Test(groups = { "functional.encode", "dialog" })
    public void testUserResourceLimitationEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.loadClass(MAPUserAbortInfoImpl.class);
    	
        MAPUserAbortInfoImpl mapUserAbortInfo = new MAPUserAbortInfoImpl();

        MAPUserAbortChoiseImpl mapUserAbortChoice = new MAPUserAbortChoiseImpl();
        mapUserAbortChoice.setUserResourceLimitation();

        mapUserAbortInfo.setUserAbortChoise(mapUserAbortChoice);

        ByteBuf buffer=parser.encode(mapUserAbortInfo);
        byte[] data = new byte[buffer.readableBytes()];
        buffer.readBytes(data);
        assertTrue(Arrays.equals(getUserResourceLimitationReason(), data));
    }

    @Test(groups = { "functional.decode", "dialog" })
    public void testResourceUnavailableDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.loadClass(MAPUserAbortInfoImpl.class);
    	        
        // The raw data is hand made
        byte[] data = getResourceUnavailableReason();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof MAPUserAbortInfoImpl);
        MAPUserAbortInfoImpl mapUserAbortInfo = (MAPUserAbortInfoImpl)result.getResult();
        
        MAPUserAbortChoiseImpl mapUserAbortChoice = mapUserAbortInfo.getUserAbortChoise();

        assertNotNull(mapUserAbortChoice);

        assertFalse(mapUserAbortChoice.isUserSpecificReason());
        assertFalse(mapUserAbortChoice.isProcedureCancellationReason());
        assertTrue(mapUserAbortChoice.isResourceUnavailableReason());
        assertFalse(mapUserAbortChoice.isUserResourceLimitation());
        assertEquals(mapUserAbortChoice.getResourceUnavailableReason(), ResourceUnavailableReason.shortTermResourceLimitation);

    }

    @Test(groups = { "functional.encode", "dialog" })
    public void testResourceUnavailableEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.loadClass(MAPUserAbortInfoImpl.class);
    	
        MAPUserAbortInfoImpl mapUserAbortInfo = new MAPUserAbortInfoImpl();

        MAPUserAbortChoiseImpl mapUserAbortChoice = new MAPUserAbortChoiseImpl();
        mapUserAbortChoice.setResourceUnavailableReason(ResourceUnavailableReason.shortTermResourceLimitation);

        mapUserAbortInfo.setUserAbortChoise(mapUserAbortChoice);

        ByteBuf buffer=parser.encode(mapUserAbortInfo);
        byte[] data = new byte[buffer.readableBytes()];
        buffer.readBytes(data);
        assertTrue(Arrays.equals(getResourceUnavailableReason(), data));
    }

    @Test(groups = { "functional.decode", "dialog" })
    public void testProcedureCancellationReasonDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.loadClass(MAPUserAbortInfoImpl.class);
    	        
        // The raw data is hand made
        byte[] data = getProcedureCancellationReason();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof MAPUserAbortInfoImpl);
        MAPUserAbortInfoImpl mapUserAbortInfo = (MAPUserAbortInfoImpl)result.getResult();
        
        MAPUserAbortChoiseImpl mapUserAbortChoice = mapUserAbortInfo.getUserAbortChoise();

        assertNotNull(mapUserAbortChoice);

        assertFalse(mapUserAbortChoice.isUserSpecificReason());
        assertTrue(mapUserAbortChoice.isProcedureCancellationReason());
        assertFalse(mapUserAbortChoice.isResourceUnavailableReason());
        assertFalse(mapUserAbortChoice.isUserResourceLimitation());

        ProcedureCancellationReason procdCancellReasn = mapUserAbortChoice.getProcedureCancellationReason();

        assertNotNull(procdCancellReasn);

        assertEquals(ProcedureCancellationReason.associatedProcedureFailure, procdCancellReasn);

    }

    @Test(groups = { "functional.encode", "dialog" })
    public void testProcedureCancellationReasonEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.loadClass(MAPUserAbortInfoImpl.class);
    	
        MAPUserAbortInfoImpl mapUserAbortInfo = new MAPUserAbortInfoImpl();

        MAPUserAbortChoiseImpl mapUserAbortChoice = new MAPUserAbortChoiseImpl();
        mapUserAbortChoice.setProcedureCancellationReason(ProcedureCancellationReason.associatedProcedureFailure);

        mapUserAbortInfo.setUserAbortChoise(mapUserAbortChoice);

        ByteBuf buffer=parser.encode(mapUserAbortInfo);
        byte[] data = new byte[buffer.readableBytes()];
        buffer.readBytes(data);
        assertTrue(Arrays.equals(getProcedureCancellationReason(), data));
    }

    @Test(groups = { "functional.decode", "dialog" })
    public void testFullDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.loadClass(MAPUserAbortInfoImpl.class);
    	        
        // The raw data is hand made
        byte[] data = getDataFull();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof MAPUserAbortInfoImpl);
        MAPUserAbortInfoImpl mapUserAbortInfo = (MAPUserAbortInfoImpl)result.getResult();
        
        MAPUserAbortChoiseImpl mapUserAbortChoice = mapUserAbortInfo.getUserAbortChoise();

        assertNotNull(mapUserAbortChoice);

        assertFalse(mapUserAbortChoice.isUserSpecificReason());
        assertTrue(mapUserAbortChoice.isProcedureCancellationReason());
        assertFalse(mapUserAbortChoice.isResourceUnavailableReason());
        assertFalse(mapUserAbortChoice.isUserResourceLimitation());

        ProcedureCancellationReason procdCancellReasn = mapUserAbortChoice.getProcedureCancellationReason();

        assertNotNull(procdCancellReasn);

        assertEquals(ProcedureCancellationReason.callRelease, procdCancellReasn);

        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(mapUserAbortInfo.getExtensionContainer()));
    }

    @Test(groups = { "functional.encode", "dialog" })
    public void testFullEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.loadClass(MAPUserAbortInfoImpl.class);
    	
        MAPUserAbortInfoImpl mapUserAbortInfo = new MAPUserAbortInfoImpl();

        MAPUserAbortChoiseImpl mapUserAbortChoice = new MAPUserAbortChoiseImpl();
        mapUserAbortChoice.setProcedureCancellationReason(ProcedureCancellationReason.callRelease);

        mapUserAbortInfo.setUserAbortChoise(mapUserAbortChoice);
        mapUserAbortInfo.setExtensionContainer(MAPExtensionContainerTest.GetTestExtensionContainer());

        ByteBuf buffer=parser.encode(mapUserAbortInfo);
        byte[] data = new byte[buffer.readableBytes()];
        buffer.readBytes(data);
        
        assertTrue(Arrays.equals(getDataFull(), data));
    }
}