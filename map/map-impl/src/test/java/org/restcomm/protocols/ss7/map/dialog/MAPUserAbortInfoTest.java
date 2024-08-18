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

package org.restcomm.protocols.ss7.map.dialog;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerTest;
import org.restcomm.protocols.ss7.map.api.dialog.MAPUserAbortChoice;
import org.restcomm.protocols.ss7.map.api.dialog.ProcedureCancellationReason;
import org.restcomm.protocols.ss7.map.api.dialog.ResourceUnavailableReason;
import org.junit.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author amit bhayani
 * @author sergey vetyutnev
 * @author yulianoifa
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
        return new byte[] { -92, 44, -125, 1, 3, 48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42,
                3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33 };
    }

    @Test
    public void testUserSpecificReasonDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.loadClass(MAPUserAbortInfoImpl.class);
    	
        // The raw data is hand made
        byte[] data = getDataUserSpecificReason();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof MAPUserAbortInfoImpl);
        MAPUserAbortInfoImpl mapUserAbortInfo = (MAPUserAbortInfoImpl)result.getResult();
        
        MAPUserAbortChoice mapUserAbortChoice = mapUserAbortInfo.getUserAbortChoise();

        assertNotNull(mapUserAbortChoice);

        assertTrue(mapUserAbortChoice.isUserSpecificReason());
        assertFalse(mapUserAbortChoice.isProcedureCancellationReason());
        assertFalse(mapUserAbortChoice.isResourceUnavailableReason());
        assertFalse(mapUserAbortChoice.isUserResourceLimitation());

    }

    @Test
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

    @Test
    public void testUserResourceLimitationDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.loadClass(MAPUserAbortInfoImpl.class);
    	        
        // The raw data is hand made
    	byte[] data=getUserResourceLimitationReason();
    	ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof MAPUserAbortInfoImpl);
        MAPUserAbortInfoImpl mapUserAbortInfo = (MAPUserAbortInfoImpl)result.getResult();
        
        MAPUserAbortChoice mapUserAbortChoice = mapUserAbortInfo.getUserAbortChoise();

        assertNotNull(mapUserAbortChoice);

        assertFalse(mapUserAbortChoice.isUserSpecificReason());
        assertFalse(mapUserAbortChoice.isProcedureCancellationReason());
        assertFalse(mapUserAbortChoice.isResourceUnavailableReason());
        assertTrue(mapUserAbortChoice.isUserResourceLimitation());

    }

    @Test
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

    @Test
    public void testResourceUnavailableDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.loadClass(MAPUserAbortInfoImpl.class);
    	        
        // The raw data is hand made
        byte[] data = getResourceUnavailableReason();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof MAPUserAbortInfoImpl);
        MAPUserAbortInfoImpl mapUserAbortInfo = (MAPUserAbortInfoImpl)result.getResult();
        
        MAPUserAbortChoice mapUserAbortChoice = mapUserAbortInfo.getUserAbortChoise();

        assertNotNull(mapUserAbortChoice);

        assertFalse(mapUserAbortChoice.isUserSpecificReason());
        assertFalse(mapUserAbortChoice.isProcedureCancellationReason());
        assertTrue(mapUserAbortChoice.isResourceUnavailableReason());
        assertFalse(mapUserAbortChoice.isUserResourceLimitation());
        assertEquals(mapUserAbortChoice.getResourceUnavailableReason(), ResourceUnavailableReason.shortTermResourceLimitation);

    }

    @Test
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

    @Test
    public void testProcedureCancellationReasonDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.loadClass(MAPUserAbortInfoImpl.class);
    	        
        // The raw data is hand made
        byte[] data = getProcedureCancellationReason();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof MAPUserAbortInfoImpl);
        MAPUserAbortInfoImpl mapUserAbortInfo = (MAPUserAbortInfoImpl)result.getResult();
        
        MAPUserAbortChoice mapUserAbortChoice = mapUserAbortInfo.getUserAbortChoise();

        assertNotNull(mapUserAbortChoice);

        assertFalse(mapUserAbortChoice.isUserSpecificReason());
        assertTrue(mapUserAbortChoice.isProcedureCancellationReason());
        assertFalse(mapUserAbortChoice.isResourceUnavailableReason());
        assertFalse(mapUserAbortChoice.isUserResourceLimitation());

        ProcedureCancellationReason procdCancellReasn = mapUserAbortChoice.getProcedureCancellationReason();

        assertNotNull(procdCancellReasn);

        assertEquals(ProcedureCancellationReason.associatedProcedureFailure, procdCancellReasn);

    }

    @Test
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

    @Test
    public void testFullDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.loadClass(MAPUserAbortInfoImpl.class);
    	        
        // The raw data is hand made
        byte[] data = getDataFull();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof MAPUserAbortInfoImpl);
        MAPUserAbortInfoImpl mapUserAbortInfo = (MAPUserAbortInfoImpl)result.getResult();
        
        MAPUserAbortChoice mapUserAbortChoice = mapUserAbortInfo.getUserAbortChoise();

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

    @Test
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