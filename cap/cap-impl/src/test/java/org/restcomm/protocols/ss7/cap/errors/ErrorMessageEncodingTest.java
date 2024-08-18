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

package org.restcomm.protocols.ss7.cap.errors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.cap.api.errors.CancelProblem;
import org.restcomm.protocols.ss7.cap.api.errors.RequestedInfoErrorParameter;
import org.restcomm.protocols.ss7.cap.api.errors.TaskRefusedParameter;
import org.restcomm.protocols.ss7.cap.api.errors.UnavailableNetworkResource;
import org.junit.Test;

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
public class ErrorMessageEncodingTest {

    public byte[] getDataTaskRefused() {
        return new byte[] { 10, 1, 2 };
    }

    public byte[] getDataSystemFailure() {
        return new byte[] { 10, 1, 3 };
    }

    public byte[] getDataRequestedInfoError() {
        return new byte[] { 10, 1, 1 };
    }

    public byte[] getDataCancelFailed() {
        return new byte[] { 10, 1, 1 };
    }

    @Test
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(CAPErrorMessageTaskRefusedImpl.class);
    	
    	byte[] rawData = this.getDataTaskRefused();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof CAPErrorMessageTaskRefusedImpl);
        
        CAPErrorMessageTaskRefusedImpl elem = (CAPErrorMessageTaskRefusedImpl)result.getResult();        
        assertEquals(elem.getTaskRefusedParameter(), TaskRefusedParameter.congestion);

        parser.replaceClass(CAPErrorMessageSystemFailureImpl.class);
    	rawData = this.getDataSystemFailure();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof CAPErrorMessageSystemFailureImpl);
        
        CAPErrorMessageSystemFailureImpl elem2 = (CAPErrorMessageSystemFailureImpl)result.getResult();    
        assertEquals(elem2.getUnavailableNetworkResource(), UnavailableNetworkResource.resourceStatusFailure);
        
        parser.replaceClass(CAPErrorMessageRequestedInfoErrorImpl.class);
    	rawData = this.getDataRequestedInfoError();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof CAPErrorMessageRequestedInfoErrorImpl);
        
        CAPErrorMessageRequestedInfoErrorImpl elem3 = (CAPErrorMessageRequestedInfoErrorImpl)result.getResult();    
        assertEquals(elem3.getRequestedInfoErrorParameter(), RequestedInfoErrorParameter.unknownRequestedInfo);

        parser.replaceClass(CAPErrorMessageCancelFailedImpl.class);
    	rawData = this.getDataCancelFailed();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof CAPErrorMessageCancelFailedImpl);
        
        CAPErrorMessageCancelFailedImpl elem4 = (CAPErrorMessageCancelFailedImpl)result.getResult();    
        assertEquals(elem4.getCancelProblem(), CancelProblem.tooLate);
    }

    @Test
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	
        CAPErrorMessageTaskRefusedImpl elem = new CAPErrorMessageTaskRefusedImpl(TaskRefusedParameter.congestion);
        byte[] rawData = this.getDataTaskRefused();
        ByteBuf buffer=parser.encode(elem);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        CAPErrorMessageSystemFailureImpl elem2 = new CAPErrorMessageSystemFailureImpl(
                UnavailableNetworkResource.resourceStatusFailure);
        rawData = this.getDataSystemFailure();
        buffer=parser.encode(elem2);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
        
        CAPErrorMessageRequestedInfoErrorImpl elem3 = new CAPErrorMessageRequestedInfoErrorImpl(
                RequestedInfoErrorParameter.unknownRequestedInfo);
        rawData = this.getDataRequestedInfoError();
        buffer=parser.encode(elem3);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        CAPErrorMessageCancelFailedImpl elem4 = new CAPErrorMessageCancelFailedImpl(CancelProblem.tooLate);
        rawData = this.getDataCancelFailed();
        buffer=parser.encode(elem4);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}
