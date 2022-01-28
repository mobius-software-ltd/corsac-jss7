/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
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

package org.restcomm.protocols.ss7.map.errors;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.TeleserviceCodeValue;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerTest;
import org.restcomm.protocols.ss7.map.api.errors.AbsentSubscriberDiagnosticSM;
import org.restcomm.protocols.ss7.map.api.errors.AbsentSubscriberReason;
import org.restcomm.protocols.ss7.map.api.errors.AdditionalNetworkResource;
import org.restcomm.protocols.ss7.map.api.errors.AdditionalRoamingNotAllowedCause;
import org.restcomm.protocols.ss7.map.api.errors.CUGRejectCause;
import org.restcomm.protocols.ss7.map.api.errors.CallBarringCause;
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorCode;
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorMessage;
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorMessageAbsentSubscriber;
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorMessageAbsentSubscriberSM;
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorMessageBusySubscriber;
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorMessageCUGReject;
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorMessageCallBarred;
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorMessageFacilityNotSup;
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorMessagePositionMethodFailure;
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorMessageRoamingNotAllowed;
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorMessageSMDeliveryFailure;
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorMessageSubscriberBusyForMtSms;
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorMessageSystemFailure;
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorMessageUnauthorizedLCSClient;
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorMessageUnknownSubscriber;
import org.restcomm.protocols.ss7.map.api.errors.PWRegistrationFailureCause;
import org.restcomm.protocols.ss7.map.api.errors.PositionMethodFailureDiagnostic;
import org.restcomm.protocols.ss7.map.api.errors.RoamingNotAllowedCause;
import org.restcomm.protocols.ss7.map.api.errors.SMEnumeratedDeliveryFailureCause;
import org.restcomm.protocols.ss7.map.api.errors.UnauthorizedLCSClientDiagnostic;
import org.restcomm.protocols.ss7.map.api.errors.UnknownSubscriberDiagnostic;
import org.restcomm.protocols.ss7.map.api.primitives.NetworkResource;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SupplementaryCodeValue;
import org.restcomm.protocols.ss7.map.api.smstpdu.SmsDeliverReportTpdu;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.BasicServiceCodeImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.TeleserviceCodeImpl;
import org.restcomm.protocols.ss7.map.service.supplementary.SSCodeImpl;
import org.restcomm.protocols.ss7.map.service.supplementary.SSStatusImpl;
import org.restcomm.protocols.ss7.map.smstpdu.DataCodingSchemeImpl;
import org.restcomm.protocols.ss7.map.smstpdu.FailureCauseImpl;
import org.restcomm.protocols.ss7.map.smstpdu.ProtocolIdentifierImpl;
import org.restcomm.protocols.ss7.map.smstpdu.SmsDeliverReportTpduImpl;
import org.restcomm.protocols.ss7.map.smstpdu.UserDataImpl;
import org.restcomm.protocols.ss7.tcap.asn.comp.ErrorCodeImpl;
import org.restcomm.protocols.ss7.tcap.asn.comp.ErrorCodeType;
import org.restcomm.protocols.ss7.tcap.asn.comp.ReturnErrorImpl;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

/**
 *
 * @author sergey vetyutnev
 * @author amit bhayani
 * @author yulian oifa
 *
 */
public class MAPErrorMessageTest {
	
	byte[] dataExtContainerFull = { (byte)-93, 49, 2, 1, 1, 2, 1, 36, 48, 41, 48, 39, (byte) 160, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, (byte) 161, 3, 31, 32, 33 };
    byte[] dataSMDeliveryFailure = { (byte)-93, 11, 2, 1, 1, 2, 1, 32, 48, 3, 10, 1, 5 };
    byte[] dataSMDeliveryFailureFull = {(byte)-93, 68, 2, 1, 1, 2, 1, 32, 48, 60, 10, 1, 4, 4, 14, 0, -43, 7, 127, -10, 8, 1, 2, 0, 0, 0, 9, 9, 9, 48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33};
    byte[] dataSMDeliveryFailureV1 = {(byte)-93, 9, 2, 1, 1, 2, 1, 32, 10, 1, 1};
    byte[] dataAbsentSubscriberSM = {(byte)-93, 11, 2, 1, 1, 2, 1, 6, 48, 3, 2, 1, 1};
    byte[] dataAbsentSubscriberSMFull = {(byte)-93, 55, 2, 1, 1, 2, 1, 6, 48, 47, 2, 1, 0, 48, 39, (byte) 160, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, (byte) 161, 3, 31, 32, 33, (byte) 128, 1, 6 };
    byte[] dataSystemFailure = {(byte)-93, 9, 2, 1, 1, 2, 1, 34, 10, 1, 0};
    byte[] dataSystemFailureFull = {(byte)-93, 55, 2, 1, 1, 2, 1, 34, 48, 47, 10, 1, 2, (byte) 128, 1, 3, 48, 39, (byte) 160, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, (byte) 161, 3, 31, 32, 33};
    byte[] dataCallBarred = {(byte)-93, 11, 2, 1, 1, 2, 1, 13, 48, 3, 10, 1, 1};
    byte[] dataCallBarredFull = {(byte)-93, 54, 2, 1, 1, 2, 1, 13, 48, 46, 10, 1, 1, 48, 39, (byte) 160, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, (byte) 161, 3, 31, 32, 33, (byte) 129, 0};
    byte[] dataFacilityNotSupFull = {(byte)-93, 53, 2, 1, 1, 2, 1, 21, 48, 45, 48, 39, (byte) 160, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, (byte) 161, 3, 31, 32, 33, (byte) 128, 0, (byte) 129, 0 };
    byte[] dataUnknownSubscriberFull = {(byte)-93, 52, 2, 1, 1, 2, 1, 1, 48, 44, 48, 39, (byte) 160, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, (byte) 161, 3, 31, 32, 33, 10, 1, 1 };
    byte[] dataSubscriberBusyForMTSMSFull = {(byte)-93, 51, 2, 1, 1, 2, 1, 31, 48, 43, 48, 39, (byte) 160, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, (byte) 161, 3, 31, 32, 33, 5, 0};
    byte[] dataAbsentSubscriberFull = {(byte)-93, 52, 2, 1, 1, 2, 1, 27, 48, 44, 48, 39, (byte) 160, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, (byte) 161, 3, 31, 32, 33, (byte) 128, 1, 3};
    byte[] dataAbsentSubscriberV1 = {(byte)-93, 9, 2, 1, 1, 2, 1, 27, 1, 1, -1};
    byte[] dataUnauthorizedLCSClientFull = {(byte)-93, 52, 2, 1, 1, 2, 1, 53, 48, 44, (byte) 128, 1, 2, (byte) 161, 39, (byte) 160, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, (byte) 161, 3, 31, 32, 33};
    byte[] dataPositionMethodFailureFull = {(byte)-93, 52, 2, 1, 1, 2, 1, 54, 48, 44, (byte) 128, 1, 4, (byte) 161, 39, (byte) 160, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, (byte) 161, 3, 31, 32, 33};
    byte[] dataBusySubscriberFull = {(byte)-93, 53, 2, 1, 1, 2, 1, 45, 48, 45, 48, 39, (byte) 160, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, (byte) 161, 3, 31, 32, 33, (byte) 128, 0, (byte) 129, 0};
    byte[] dataCUGRejectFull = {(byte)-93, 52, 2, 1, 1, 2, 1, 15, 48, 44, 10, 1, 1, 48, 39, (byte) 160, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, (byte) 161, 3, 31, 32, 33};
    byte[] dataRoamingNotAllowedFull = {(byte)-93, 55, 2, 1, 1, 2, 1, 8, 48, 47, 10, 1, 0, 48, 39, (byte) 160, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, (byte) 161, 3, 31, 32, 33, (byte) 128, 1, 0};
    byte[] dataSsErrorStatusFull = {(byte)-93, 11, 2, 1, 1, 2, 1, 17, 48, 3, 4, 1, 6};
    byte[] dataSsIncompatibilityFull = {(byte)-93, 17, 2, 1, 1, 2, 1, 20, 48, 9, (byte)-127, 1, 33, (byte)-125, 1, 17, (byte)-124, 1, 9};
    byte[] dataPwRegistrationFailureFull = {(byte)-93, 9, 2, 1, 1, 2, 1, 37, 10, 1, 2};
    
    private byte[] uData = { 1, 2, 0, 0, 0, 9, 9, 9 };

    @Test(groups = { "functional.decode", "dialog.message" })
    public void testDecode() throws Exception {
		ASNParser parser=new ASNParser();
    	parser.loadClass(ReturnErrorImpl.class);
    	ErrorCodeImpl errorCode=new ErrorCodeImpl();
    	errorCode.setLocalErrorCode((long)MAPErrorCode.unexpectedDataValue);
    	parser.registerLocalMapping(ReturnErrorImpl.class, errorCode, MAPErrorMessageExtensionContainerImpl.class);
    	parser.registerAlternativeClassMapping(MAPErrorMessageExtensionContainerImpl.class, MAPErrorMessageExtensionContainerImpl.class);
    	
    	errorCode=new ErrorCodeImpl();
    	errorCode.setLocalErrorCode((long)MAPErrorCode.smDeliveryFailure);
    	parser.registerLocalMapping(ReturnErrorImpl.class, errorCode, MAPErrorMessageSMDeliveryFailure1Impl.class);
    	parser.registerAlternativeClassMapping(MAPErrorMessageSMDeliveryFailure1Impl.class, MAPErrorMessageSMDeliveryFailureImpl.class);
    	parser.registerAlternativeClassMapping(MAPErrorMessageSMDeliveryFailure1Impl.class, MAPErrorMessageSMDeliveryFailure1Impl.class);
        
    	errorCode=new ErrorCodeImpl();
    	errorCode.setLocalErrorCode((long)MAPErrorCode.absentSubscriberSM);
    	parser.registerLocalMapping(ReturnErrorImpl.class, errorCode, MAPErrorMessageAbsentSubscriberSMImpl.class);
    	parser.registerAlternativeClassMapping(MAPErrorMessageAbsentSubscriberSMImpl.class, MAPErrorMessageAbsentSubscriberSMImpl.class);
    	
    	errorCode=new ErrorCodeImpl();
    	errorCode.setLocalErrorCode((long)MAPErrorCode.systemFailure);
    	parser.registerLocalMapping(ReturnErrorImpl.class, errorCode, MAPErrorMessageSytemFailure1Impl.class);
    	parser.registerAlternativeClassMapping(MAPErrorMessageSytemFailure1Impl.class, MAPErrorMessageSytemFailure1Impl.class);
    	parser.registerAlternativeClassMapping(MAPErrorMessageSytemFailure1Impl.class, MAPErrorMessageSystemFailureImpl.class);
    	
    	errorCode=new ErrorCodeImpl();
    	errorCode.setLocalErrorCode((long)MAPErrorCode.callBarred);
    	parser.registerLocalMapping(ReturnErrorImpl.class, errorCode, MAPErrorMessageCallBarred1Impl.class);
    	parser.registerAlternativeClassMapping(MAPErrorMessageCallBarred1Impl.class, MAPErrorMessageCallBarred1Impl.class);
    	parser.registerAlternativeClassMapping(MAPErrorMessageCallBarred1Impl.class, MAPErrorMessageCallBarredImpl.class);
    	
    	errorCode=new ErrorCodeImpl();
    	errorCode.setLocalErrorCode((long)MAPErrorCode.facilityNotSupported);
    	parser.registerLocalMapping(ReturnErrorImpl.class, errorCode, MAPErrorMessageFacilityNotSupImpl.class);
    	parser.registerAlternativeClassMapping(MAPErrorMessageFacilityNotSupImpl.class, MAPErrorMessageFacilityNotSupImpl.class);
    	
    	errorCode=new ErrorCodeImpl();
    	errorCode.setLocalErrorCode((long)MAPErrorCode.unknownSubscriber);
    	parser.registerLocalMapping(ReturnErrorImpl.class, errorCode, MAPErrorMessageUnknownSubscriberImpl.class);
    	parser.registerAlternativeClassMapping(MAPErrorMessageUnknownSubscriberImpl.class, MAPErrorMessageUnknownSubscriberImpl.class);
    	
    	errorCode=new ErrorCodeImpl();
    	errorCode.setLocalErrorCode((long)MAPErrorCode.subscriberBusyForMTSMS);
    	parser.registerLocalMapping(ReturnErrorImpl.class, errorCode, MAPErrorMessageSubscriberBusyForMtSmsImpl.class);
    	parser.registerAlternativeClassMapping(MAPErrorMessageSubscriberBusyForMtSmsImpl.class, MAPErrorMessageSubscriberBusyForMtSmsImpl.class);
    	
    	errorCode=new ErrorCodeImpl();
    	errorCode.setLocalErrorCode((long)MAPErrorCode.absentSubscriber);
    	parser.registerLocalMapping(ReturnErrorImpl.class, errorCode, MAPErrorMessageAbsentSubscriber1Impl.class);
    	parser.registerAlternativeClassMapping(MAPErrorMessageAbsentSubscriber1Impl.class, MAPErrorMessageAbsentSubscriber1Impl.class);
    	parser.registerAlternativeClassMapping(MAPErrorMessageAbsentSubscriber1Impl.class, MAPErrorMessageAbsentSubscriberImpl.class);
        
    	errorCode=new ErrorCodeImpl();
    	errorCode.setLocalErrorCode((long)MAPErrorCode.unauthorizedLCSClient);
    	parser.registerLocalMapping(ReturnErrorImpl.class, errorCode, MAPErrorMessageUnauthorizedLCSClientImpl.class);
    	parser.registerAlternativeClassMapping(MAPErrorMessageUnauthorizedLCSClientImpl.class, MAPErrorMessageUnauthorizedLCSClientImpl.class);
    	
    	errorCode=new ErrorCodeImpl();
    	errorCode.setLocalErrorCode((long)MAPErrorCode.positionMethodFailure);
    	parser.registerLocalMapping(ReturnErrorImpl.class, errorCode, MAPErrorMessagePositionMethodFailureImpl.class);
    	parser.registerAlternativeClassMapping(MAPErrorMessagePositionMethodFailureImpl.class, MAPErrorMessagePositionMethodFailureImpl.class);
    	
    	errorCode=new ErrorCodeImpl();
    	errorCode.setLocalErrorCode((long)MAPErrorCode.busySubscriber);
    	parser.registerLocalMapping(ReturnErrorImpl.class, errorCode, MAPErrorMessageBusySubscriberImpl.class);
    	parser.registerAlternativeClassMapping(MAPErrorMessageBusySubscriberImpl.class, MAPErrorMessageBusySubscriberImpl.class);
    	
    	errorCode=new ErrorCodeImpl();
    	errorCode.setLocalErrorCode((long)MAPErrorCode.cugReject);
    	parser.registerLocalMapping(ReturnErrorImpl.class, errorCode, MAPErrorMessageCUGRejectImpl.class);
    	parser.registerAlternativeClassMapping(MAPErrorMessageCUGRejectImpl.class, MAPErrorMessageCUGRejectImpl.class);
    	
    	errorCode=new ErrorCodeImpl();
    	errorCode.setLocalErrorCode((long)MAPErrorCode.roamingNotAllowed);
    	parser.registerLocalMapping(ReturnErrorImpl.class, errorCode, MAPErrorMessageRoamingNotAllowedImpl.class);
    	parser.registerAlternativeClassMapping(MAPErrorMessageRoamingNotAllowedImpl.class, MAPErrorMessageRoamingNotAllowedImpl.class);
    	
    	errorCode=new ErrorCodeImpl();
    	errorCode.setLocalErrorCode((long)MAPErrorCode.ssErrorStatus);
    	parser.registerLocalMapping(ReturnErrorImpl.class, errorCode, MAPErrorMessageSsErrorStatusImpl.class);
    	parser.registerAlternativeClassMapping(MAPErrorMessageSsErrorStatusImpl.class, MAPErrorMessageSsErrorStatusImpl.class);
    	
    	errorCode=new ErrorCodeImpl();
    	errorCode.setLocalErrorCode((long)MAPErrorCode.ssIncompatibility);
    	parser.registerLocalMapping(ReturnErrorImpl.class, errorCode, MAPErrorMessageSsIncompatibilityImpl.class);
    	parser.registerAlternativeClassMapping(MAPErrorMessageSsIncompatibilityImpl.class, MAPErrorMessageSsIncompatibilityImpl.class);
    	
    	errorCode=new ErrorCodeImpl();
    	errorCode.setLocalErrorCode((long)MAPErrorCode.pwRegistrationFailure);
    	parser.registerLocalMapping(ReturnErrorImpl.class, errorCode, MAPErrorMessagePwRegistrationFailureImpl.class);
    	parser.registerAlternativeClassMapping(MAPErrorMessagePwRegistrationFailureImpl.class, MAPErrorMessagePwRegistrationFailureImpl.class);
    	
    	//EXT ERROR
    	ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(dataExtContainerFull));
        
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ReturnErrorImpl);
        ReturnErrorImpl re = (ReturnErrorImpl)result.getResult();
        assertEquals(re.getInvokeId(),Long.valueOf(1L));
        assertNotNull(re.getErrorCode());
        assertEquals(re.getErrorCode().getErrorType(),ErrorCodeType.Local);
        assertEquals(re.getErrorCode().getLocalErrorCode(),Long.valueOf(MAPErrorCode.unexpectedDataValue));
        assertNotNull(re.getParameter());
        assertTrue(re.getParameter() instanceof MAPErrorMessageImpl);
        assertTrue(re.getParameter() instanceof MAPErrorMessageExtensionContainerImpl);
        
        MAPErrorMessageExtensionContainerImpl errContainer=(MAPErrorMessageExtensionContainerImpl)re.getParameter();
        assertNotNull(errContainer.getExtensionContainer());        
        MAPExtensionContainer innerContainer =  errContainer.getExtensionContainer();
        assertNotNull(innerContainer.getPrivateExtensionList());
        assertNotNull(innerContainer.getPcsExtensions());
        assertEquals(innerContainer.getPrivateExtensionList().size(),3);
        assertNotNull(innerContainer.getPrivateExtensionList().get(0).getData());
        assertNull(innerContainer.getPrivateExtensionList().get(1).getData());
        assertNotNull(innerContainer.getPrivateExtensionList().get(2).getData());
        
        assertNotNull(innerContainer.getPcsExtensions()); 

        //SM FAILURE
        result=parser.decode(Unpooled.wrappedBuffer(dataSMDeliveryFailure));
        
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ReturnErrorImpl);
        re = (ReturnErrorImpl)result.getResult();
        assertEquals(re.getInvokeId(),Long.valueOf(1L));
        assertNotNull(re.getErrorCode());
        assertEquals(re.getErrorCode().getErrorType(),ErrorCodeType.Local);
        assertEquals(re.getErrorCode().getLocalErrorCode(),Long.valueOf(MAPErrorCode.smDeliveryFailure));
        assertNotNull(re.getParameter());
        assertTrue(re.getParameter() instanceof MAPErrorMessageImpl);
        assertTrue(re.getParameter() instanceof MAPErrorMessageSMDeliveryFailureImpl);
        MAPErrorMessageSMDeliveryFailure emSMDeliveryFailure=(MAPErrorMessageSMDeliveryFailureImpl)re.getParameter();
        assertEquals(emSMDeliveryFailure.getSMEnumeratedDeliveryFailureCause(),SMEnumeratedDeliveryFailureCause.invalidSMEAddress);
        assertNull(emSMDeliveryFailure.getSignalInfo());
        assertNull(emSMDeliveryFailure.getSmsDeliverReportTpdu());
        assertNull(emSMDeliveryFailure.getExtensionContainer());
        assertEquals(emSMDeliveryFailure.getMapProtocolVersion(), 3);
        
        //SM FAILURE FULL
        result=parser.decode(Unpooled.wrappedBuffer(dataSMDeliveryFailureFull));
        
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ReturnErrorImpl);
        re = (ReturnErrorImpl)result.getResult();
        assertEquals(re.getInvokeId(),Long.valueOf(1L));
        assertNotNull(re.getErrorCode());
        assertEquals(re.getErrorCode().getErrorType(),ErrorCodeType.Local);
        assertEquals(re.getErrorCode().getLocalErrorCode(),Long.valueOf(MAPErrorCode.smDeliveryFailure));
        assertNotNull(re.getParameter());
        assertTrue(re.getParameter() instanceof MAPErrorMessageImpl);
        assertTrue(re.getParameter() instanceof MAPErrorMessageSMDeliveryFailureImpl);
        
        emSMDeliveryFailure=(MAPErrorMessageSMDeliveryFailureImpl)re.getParameter();
        assertNotNull(emSMDeliveryFailure.getExtensionContainer());        
        innerContainer =  emSMDeliveryFailure.getExtensionContainer();
        assertNotNull(innerContainer.getPrivateExtensionList());
        assertNotNull(innerContainer.getPcsExtensions());
        assertEquals(innerContainer.getPrivateExtensionList().size(),3);
        assertNotNull(innerContainer.getPrivateExtensionList().get(0).getData());
        assertNull(innerContainer.getPrivateExtensionList().get(1).getData());
        assertNotNull(innerContainer.getPrivateExtensionList().get(2).getData());
        
        assertNotNull(innerContainer.getPcsExtensions()); 
        
        assertEquals(emSMDeliveryFailure.getSMEnumeratedDeliveryFailureCause(), SMEnumeratedDeliveryFailureCause.scCongestion);
        assertNotNull(emSMDeliveryFailure.getSignalInfo());
        assertNotNull(emSMDeliveryFailure.getExtensionContainer());
        SmsDeliverReportTpdu tpdu = emSMDeliveryFailure.getSmsDeliverReportTpdu();
        assertEquals(tpdu.getFailureCause().getCode(), 0xd5);
        assertEquals(tpdu.getParameterIndicator().getCode(), 7);
        assertEquals(tpdu.getProtocolIdentifier().getCode(), 127);
        assertEquals(tpdu.getDataCodingScheme().getCode(), 246);
        assertTrue(ByteBufUtil.equals(tpdu.getUserData().getEncodedData(), Unpooled.wrappedBuffer(uData)));
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(emSMDeliveryFailure.getExtensionContainer()));
        assertEquals(emSMDeliveryFailure.getMapProtocolVersion(), 3);
        
        //SM FAILURE V1
        result=parser.decode(Unpooled.wrappedBuffer(dataSMDeliveryFailureV1));
        
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ReturnErrorImpl);
        re = (ReturnErrorImpl)result.getResult();
        assertEquals(re.getInvokeId(),Long.valueOf(1L));
        assertNotNull(re.getErrorCode());
        assertEquals(re.getErrorCode().getErrorType(),ErrorCodeType.Local);
        assertEquals(re.getErrorCode().getLocalErrorCode(),Long.valueOf(MAPErrorCode.smDeliveryFailure));
        assertNotNull(re.getParameter());
        assertTrue(re.getParameter() instanceof MAPErrorMessage);
        assertTrue(re.getParameter() instanceof MAPErrorMessageSMDeliveryFailure1Impl);
        emSMDeliveryFailure=(MAPErrorMessageSMDeliveryFailure1Impl)re.getParameter();
        assertEquals(emSMDeliveryFailure.getSMEnumeratedDeliveryFailureCause(),SMEnumeratedDeliveryFailureCause.equipmentProtocolError);
        assertNull(emSMDeliveryFailure.getSignalInfo());
        assertNull(emSMDeliveryFailure.getExtensionContainer());
        assertEquals(emSMDeliveryFailure.getMapProtocolVersion(), 1);
        
        //Absent Subscriber SM
        result=parser.decode(Unpooled.wrappedBuffer(dataAbsentSubscriberSM));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ReturnErrorImpl);
        re = (ReturnErrorImpl)result.getResult();
        assertEquals(re.getInvokeId(),Long.valueOf(1L));
        assertNotNull(re.getErrorCode());
        assertEquals(re.getErrorCode().getErrorType(),ErrorCodeType.Local);
        assertEquals(re.getErrorCode().getLocalErrorCode(),Long.valueOf(MAPErrorCode.absentSubscriberSM));
        assertNotNull(re.getParameter());
        assertTrue(re.getParameter() instanceof MAPErrorMessage);
        assertTrue(re.getParameter() instanceof MAPErrorMessageAbsentSubscriberSMImpl);
        MAPErrorMessageAbsentSubscriberSM emAbsentSubscriberSMImpl = (MAPErrorMessageAbsentSubscriberSM)re.getParameter();
        assertEquals(emAbsentSubscriberSMImpl.getAbsentSubscriberDiagnosticSM(), AbsentSubscriberDiagnosticSM.IMSIDetached);
        assertNull(emAbsentSubscriberSMImpl.getAdditionalAbsentSubscriberDiagnosticSM());
        assertNull(emAbsentSubscriberSMImpl.getExtensionContainer());
        
        //Absent Subscriber SM FULL
        result=parser.decode(Unpooled.wrappedBuffer(dataAbsentSubscriberSMFull));
        
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ReturnErrorImpl);
        re = (ReturnErrorImpl)result.getResult();
        assertEquals(re.getInvokeId(),Long.valueOf(1L));
        assertNotNull(re.getErrorCode());
        assertEquals(re.getErrorCode().getErrorType(),ErrorCodeType.Local);
        assertEquals(re.getErrorCode().getLocalErrorCode(),Long.valueOf(MAPErrorCode.absentSubscriberSM));
        assertNotNull(re.getParameter());
        assertTrue(re.getParameter() instanceof MAPErrorMessageImpl);
        assertTrue(re.getParameter() instanceof MAPErrorMessageAbsentSubscriberSMImpl);
        
        emAbsentSubscriberSMImpl=(MAPErrorMessageAbsentSubscriberSM)re.getParameter();
        assertNotNull(emAbsentSubscriberSMImpl.getExtensionContainer());        
        innerContainer =  emAbsentSubscriberSMImpl.getExtensionContainer();
        assertNotNull(innerContainer.getPrivateExtensionList());
        assertNotNull(innerContainer.getPcsExtensions());
        assertEquals(innerContainer.getPrivateExtensionList().size(),3);
        assertNotNull(innerContainer.getPrivateExtensionList().get(0).getData());
        assertNull(innerContainer.getPrivateExtensionList().get(1).getData());
        assertNotNull(innerContainer.getPrivateExtensionList().get(2).getData());
        
        assertNotNull(innerContainer.getPcsExtensions()); 
        
        assertEquals(emAbsentSubscriberSMImpl.getAbsentSubscriberDiagnosticSM(),AbsentSubscriberDiagnosticSM.NoPagingResponseViaTheMSC);
        assertEquals(emAbsentSubscriberSMImpl.getAdditionalAbsentSubscriberDiagnosticSM(),AbsentSubscriberDiagnosticSM.GPRSDetached);
        
        //System FAILURE V1
        result=parser.decode(Unpooled.wrappedBuffer(dataSystemFailure));
        
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ReturnErrorImpl);
        re = (ReturnErrorImpl)result.getResult();
        assertEquals(re.getInvokeId(),Long.valueOf(1L));
        assertNotNull(re.getErrorCode());
        assertEquals(re.getErrorCode().getErrorType(),ErrorCodeType.Local);
        assertEquals(re.getErrorCode().getLocalErrorCode(),Long.valueOf(MAPErrorCode.systemFailure));
        assertNotNull(re.getParameter());
        assertTrue(re.getParameter() instanceof MAPErrorMessage);
        assertTrue(re.getParameter() instanceof MAPErrorMessageSystemFailure);
        MAPErrorMessageSystemFailure emSystemFailure=(MAPErrorMessageSystemFailure)re.getParameter();
        assertEquals(emSystemFailure.getMapProtocolVersion(), 2);
        assertEquals(emSystemFailure.getNetworkResource(), NetworkResource.plmn);
        assertNull(emSystemFailure.getAdditionalNetworkResource());
        assertNull(emSystemFailure.getExtensionContainer());
        
        //System FAILURE FULL
        result=parser.decode(Unpooled.wrappedBuffer(dataSystemFailureFull));
        
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ReturnErrorImpl);
        re = (ReturnErrorImpl)result.getResult();
        assertEquals(re.getInvokeId(),Long.valueOf(1L));
        assertNotNull(re.getErrorCode());
        assertEquals(re.getErrorCode().getErrorType(),ErrorCodeType.Local);
        assertEquals(re.getErrorCode().getLocalErrorCode(),Long.valueOf(MAPErrorCode.systemFailure));
        assertNotNull(re.getParameter());
        assertTrue(re.getParameter() instanceof MAPErrorMessageImpl);
        assertTrue(re.getParameter() instanceof MAPErrorMessageSystemFailure);
        
        emSystemFailure=(MAPErrorMessageSystemFailure)re.getParameter();
        assertNotNull(emSystemFailure.getExtensionContainer());        
        innerContainer =  emSystemFailure.getExtensionContainer();
        assertNotNull(innerContainer.getPrivateExtensionList());
        assertNotNull(innerContainer.getPcsExtensions());
        assertEquals(innerContainer.getPrivateExtensionList().size(),3);
        assertNotNull(innerContainer.getPrivateExtensionList().get(0).getData());
        assertNull(innerContainer.getPrivateExtensionList().get(1).getData());
        assertNotNull(innerContainer.getPrivateExtensionList().get(2).getData());
        
        assertNotNull(innerContainer.getPcsExtensions()); 
        
        assertEquals(emSystemFailure.getMapProtocolVersion(), 3);
        assertEquals(emSystemFailure.getNetworkResource(), NetworkResource.vlr);
        assertEquals(emSystemFailure.getAdditionalNetworkResource(), AdditionalNetworkResource.gsmSCF);
        
        //Call Barred
        result=parser.decode(Unpooled.wrappedBuffer(dataCallBarred));
        
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ReturnErrorImpl);
        re = (ReturnErrorImpl)result.getResult();
        assertEquals(re.getInvokeId(),Long.valueOf(1L));
        assertNotNull(re.getErrorCode());
        assertEquals(re.getErrorCode().getErrorType(),ErrorCodeType.Local);
        assertEquals(re.getErrorCode().getLocalErrorCode(),Long.valueOf(MAPErrorCode.callBarred));
        assertNotNull(re.getParameter());
        assertTrue(re.getParameter() instanceof MAPErrorMessage);
        assertTrue(re.getParameter() instanceof MAPErrorMessageCallBarred);
        MAPErrorMessageCallBarred emCallBarred=(MAPErrorMessageCallBarred)re.getParameter();
        assertEquals(emCallBarred.getMapProtocolVersion(), 3);
        assertEquals(emCallBarred.getCallBarringCause(), CallBarringCause.operatorBarring);
        assertEquals((boolean) emCallBarred.getUnauthorisedMessageOriginator(), false);
        assertNull(emCallBarred.getExtensionContainer());      
        
        //Call Barred FULL
        result=parser.decode(Unpooled.wrappedBuffer(dataCallBarredFull));
        
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ReturnErrorImpl);
        re = (ReturnErrorImpl)result.getResult();
        assertEquals(re.getInvokeId(),Long.valueOf(1L));
        assertNotNull(re.getErrorCode());
        assertEquals(re.getErrorCode().getErrorType(),ErrorCodeType.Local);
        assertEquals(re.getErrorCode().getLocalErrorCode(),Long.valueOf(MAPErrorCode.callBarred));
        assertNotNull(re.getParameter());
        assertTrue(re.getParameter() instanceof MAPErrorMessageImpl);
        assertTrue(re.getParameter() instanceof MAPErrorMessageCallBarred);
        
        emCallBarred=(MAPErrorMessageCallBarred)re.getParameter();
        assertNotNull(emCallBarred.getExtensionContainer());        
        innerContainer =  emCallBarred.getExtensionContainer();
        assertNotNull(innerContainer.getPrivateExtensionList());
        assertNotNull(innerContainer.getPcsExtensions());
        assertEquals(innerContainer.getPrivateExtensionList().size(),3);
        assertNotNull(innerContainer.getPrivateExtensionList().get(0).getData());
        assertNull(innerContainer.getPrivateExtensionList().get(1).getData());
        assertNotNull(innerContainer.getPrivateExtensionList().get(2).getData());
        
        assertNotNull(innerContainer.getPcsExtensions()); 
        
        assertEquals(emCallBarred.getMapProtocolVersion(), 3);
        assertEquals(emCallBarred.getCallBarringCause(), CallBarringCause.operatorBarring);
        assertEquals((boolean) emCallBarred.getUnauthorisedMessageOriginator(), true);   
        
        //Facility Not Supported
        result=parser.decode(Unpooled.wrappedBuffer(dataFacilityNotSupFull));
        
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ReturnErrorImpl);
        re = (ReturnErrorImpl)result.getResult();
        assertEquals(re.getInvokeId(),Long.valueOf(1L));
        assertNotNull(re.getErrorCode());
        assertEquals(re.getErrorCode().getErrorType(),ErrorCodeType.Local);
        assertEquals(re.getErrorCode().getLocalErrorCode(),Long.valueOf(MAPErrorCode.facilityNotSupported));
        assertNotNull(re.getParameter());
        assertTrue(re.getParameter() instanceof MAPErrorMessageImpl);
        assertTrue(re.getParameter() instanceof MAPErrorMessageFacilityNotSup);
        
        MAPErrorMessageFacilityNotSup emFacilityNotSup=(MAPErrorMessageFacilityNotSup)re.getParameter();
        assertNotNull(emFacilityNotSup.getExtensionContainer());        
        innerContainer =  emFacilityNotSup.getExtensionContainer();
        assertNotNull(innerContainer.getPrivateExtensionList());
        assertNotNull(innerContainer.getPcsExtensions());
        assertEquals(innerContainer.getPrivateExtensionList().size(),3);
        assertNotNull(innerContainer.getPrivateExtensionList().get(0).getData());
        assertNull(innerContainer.getPrivateExtensionList().get(1).getData());
        assertNotNull(innerContainer.getPrivateExtensionList().get(2).getData());
        
        assertNotNull(innerContainer.getPcsExtensions()); 
        
        assertEquals((boolean) emFacilityNotSup.getShapeOfLocationEstimateNotSupported(), true);
        assertEquals((boolean) emFacilityNotSup.getNeededLcsCapabilityNotSupportedInServingNode(), true);
        
        //Unknown Subscriber
        result=parser.decode(Unpooled.wrappedBuffer(dataUnknownSubscriberFull));
        
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ReturnErrorImpl);
        re = (ReturnErrorImpl)result.getResult();
        assertEquals(re.getInvokeId(),Long.valueOf(1L));
        assertNotNull(re.getErrorCode());
        assertEquals(re.getErrorCode().getErrorType(),ErrorCodeType.Local);
        assertEquals(re.getErrorCode().getLocalErrorCode(),Long.valueOf(MAPErrorCode.unknownSubscriber));
        assertNotNull(re.getParameter());
        assertTrue(re.getParameter() instanceof MAPErrorMessageImpl);
        assertTrue(re.getParameter() instanceof MAPErrorMessageUnknownSubscriber);
        
        MAPErrorMessageUnknownSubscriber emUnknownSubscriber=(MAPErrorMessageUnknownSubscriber)re.getParameter();
        assertNotNull(emUnknownSubscriber.getExtensionContainer());        
        innerContainer =  emUnknownSubscriber.getExtensionContainer();
        assertNotNull(innerContainer.getPrivateExtensionList());
        assertNotNull(innerContainer.getPcsExtensions());
        assertEquals(innerContainer.getPrivateExtensionList().size(),3);
        assertNotNull(innerContainer.getPrivateExtensionList().get(0).getData());
        assertNull(innerContainer.getPrivateExtensionList().get(1).getData());
        assertNotNull(innerContainer.getPrivateExtensionList().get(2).getData());
        
        assertNotNull(innerContainer.getPcsExtensions()); 
        
        assertEquals(emUnknownSubscriber.getUnknownSubscriberDiagnostic(), UnknownSubscriberDiagnostic.gprsSubscriptionUnknown);
        
        //Subscriber Busy For MT SMS
        result=parser.decode(Unpooled.wrappedBuffer(dataSubscriberBusyForMTSMSFull));
        
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ReturnErrorImpl);
        re = (ReturnErrorImpl)result.getResult();
        assertEquals(re.getInvokeId(),Long.valueOf(1L));
        assertNotNull(re.getErrorCode());
        assertEquals(re.getErrorCode().getErrorType(),ErrorCodeType.Local);
        assertEquals(re.getErrorCode().getLocalErrorCode(),Long.valueOf(MAPErrorCode.subscriberBusyForMTSMS));
        assertNotNull(re.getParameter());
        assertTrue(re.getParameter() instanceof MAPErrorMessageImpl);
        assertTrue(re.getParameter() instanceof MAPErrorMessageSubscriberBusyForMtSms);
        
        MAPErrorMessageSubscriberBusyForMtSms emSubscriberBusyForMtSms=(MAPErrorMessageSubscriberBusyForMtSms)re.getParameter();
        assertNotNull(emSubscriberBusyForMtSms.getExtensionContainer());        
        innerContainer =  emSubscriberBusyForMtSms.getExtensionContainer();
        assertNotNull(innerContainer.getPrivateExtensionList());
        assertNotNull(innerContainer.getPcsExtensions());
        assertEquals(innerContainer.getPrivateExtensionList().size(),3);
        assertNotNull(innerContainer.getPrivateExtensionList().get(0).getData());
        assertNull(innerContainer.getPrivateExtensionList().get(1).getData());
        assertNotNull(innerContainer.getPrivateExtensionList().get(2).getData());
        
        assertNotNull(innerContainer.getPcsExtensions()); 
        
        assertEquals((boolean) emSubscriberBusyForMtSms.getGprsConnectionSuspended(), true);
        
        //Absent Subscriber
        result=parser.decode(Unpooled.wrappedBuffer(dataAbsentSubscriberFull));
        
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ReturnErrorImpl);
        re = (ReturnErrorImpl)result.getResult();
        assertEquals(re.getInvokeId(),Long.valueOf(1L));
        assertNotNull(re.getErrorCode());
        assertEquals(re.getErrorCode().getErrorType(),ErrorCodeType.Local);
        assertEquals(re.getErrorCode().getLocalErrorCode(),Long.valueOf(MAPErrorCode.absentSubscriber));
        assertNotNull(re.getParameter());
        assertTrue(re.getParameter() instanceof MAPErrorMessageImpl);
        assertTrue(re.getParameter() instanceof MAPErrorMessageAbsentSubscriber);
        
        MAPErrorMessageAbsentSubscriber emAbsentSubscriber=(MAPErrorMessageAbsentSubscriber)re.getParameter();
        assertNotNull(emAbsentSubscriber.getExtensionContainer());        
        innerContainer =  emAbsentSubscriber.getExtensionContainer();
        assertNotNull(innerContainer.getPrivateExtensionList());
        assertNotNull(innerContainer.getPcsExtensions());
        assertEquals(innerContainer.getPrivateExtensionList().size(),3);
        assertNotNull(innerContainer.getPrivateExtensionList().get(0).getData());
        assertNull(innerContainer.getPrivateExtensionList().get(1).getData());
        assertNotNull(innerContainer.getPrivateExtensionList().get(2).getData());
        
        assertNotNull(innerContainer.getPcsExtensions()); 
        
        assertEquals(emAbsentSubscriber.getAbsentSubscriberReason(), AbsentSubscriberReason.purgedMS);
        assertNull(emAbsentSubscriber.getMwdSet());
        
        //Absent Subscriber V1
        result=parser.decode(Unpooled.wrappedBuffer(dataAbsentSubscriberV1));
        
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ReturnErrorImpl);
        re = (ReturnErrorImpl)result.getResult();
        assertEquals(re.getInvokeId(),Long.valueOf(1L));
        assertNotNull(re.getErrorCode());
        assertEquals(re.getErrorCode().getErrorType(),ErrorCodeType.Local);
        assertEquals(re.getErrorCode().getLocalErrorCode(),Long.valueOf(MAPErrorCode.absentSubscriber));
        assertNotNull(re.getParameter());
        assertTrue(re.getParameter() instanceof MAPErrorMessage);
        assertTrue(re.getParameter() instanceof MAPErrorMessageAbsentSubscriber1Impl);
        emAbsentSubscriber=(MAPErrorMessageAbsentSubscriber1Impl)re.getParameter();
        assertNull(emAbsentSubscriber.getExtensionContainer());
        assertNull(emAbsentSubscriber.getAbsentSubscriberReason());
        assertTrue(emAbsentSubscriber.getMwdSet()); 
        
        //Unauthorized LCS Client
        result=parser.decode(Unpooled.wrappedBuffer(dataUnauthorizedLCSClientFull));
        
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ReturnErrorImpl);
        re = (ReturnErrorImpl)result.getResult();
        assertEquals(re.getInvokeId(),Long.valueOf(1L));
        assertNotNull(re.getErrorCode());
        assertEquals(re.getErrorCode().getErrorType(),ErrorCodeType.Local);
        assertEquals(re.getErrorCode().getLocalErrorCode(),Long.valueOf(MAPErrorCode.unauthorizedLCSClient));
        assertNotNull(re.getParameter());
        assertTrue(re.getParameter() instanceof MAPErrorMessageImpl);
        assertTrue(re.getParameter() instanceof MAPErrorMessageUnauthorizedLCSClient);
        
        MAPErrorMessageUnauthorizedLCSClient emUnauthorizedLCSClient=(MAPErrorMessageUnauthorizedLCSClient)re.getParameter();
        assertNotNull(emUnauthorizedLCSClient.getExtensionContainer());        
        innerContainer =  emUnauthorizedLCSClient.getExtensionContainer();
        assertNotNull(innerContainer.getPrivateExtensionList());
        assertNotNull(innerContainer.getPcsExtensions());
        assertEquals(innerContainer.getPrivateExtensionList().size(),3);
        assertNotNull(innerContainer.getPrivateExtensionList().get(0).getData());
        assertNull(innerContainer.getPrivateExtensionList().get(1).getData());
        assertNotNull(innerContainer.getPrivateExtensionList().get(2).getData());
        
        assertNotNull(innerContainer.getPcsExtensions()); 
        
        assertEquals(emUnauthorizedLCSClient.getUnauthorizedLCSClientDiagnostic(),UnauthorizedLCSClientDiagnostic.callToClientNotSetup);
        
        //Position Method Failure
        result=parser.decode(Unpooled.wrappedBuffer(dataPositionMethodFailureFull));
        
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ReturnErrorImpl);
        re = (ReturnErrorImpl)result.getResult();
        assertEquals(re.getInvokeId(),Long.valueOf(1L));
        assertNotNull(re.getErrorCode());
        assertEquals(re.getErrorCode().getErrorType(),ErrorCodeType.Local);
        assertEquals(re.getErrorCode().getLocalErrorCode(),Long.valueOf(MAPErrorCode.positionMethodFailure));
        assertNotNull(re.getParameter());
        assertTrue(re.getParameter() instanceof MAPErrorMessageImpl);
        assertTrue(re.getParameter() instanceof MAPErrorMessagePositionMethodFailure);
        
        MAPErrorMessagePositionMethodFailure emPositionMethodFailure=(MAPErrorMessagePositionMethodFailure)re.getParameter();
        assertNotNull(emPositionMethodFailure.getExtensionContainer());        
        innerContainer =  emPositionMethodFailure.getExtensionContainer();
        assertNotNull(innerContainer.getPrivateExtensionList());
        assertNotNull(innerContainer.getPcsExtensions());
        assertEquals(innerContainer.getPrivateExtensionList().size(),3);
        assertNotNull(innerContainer.getPrivateExtensionList().get(0).getData());
        assertNull(innerContainer.getPrivateExtensionList().get(1).getData());
        assertNotNull(innerContainer.getPrivateExtensionList().get(2).getData());
        
        assertNotNull(innerContainer.getPcsExtensions()); 
        
        assertEquals(emPositionMethodFailure.getPositionMethodFailureDiagnostic(),PositionMethodFailureDiagnostic.locationProcedureNotCompleted);
        
        //Busy Subscriber
        result=parser.decode(Unpooled.wrappedBuffer(dataBusySubscriberFull));
        
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ReturnErrorImpl);
        re = (ReturnErrorImpl)result.getResult();
        assertEquals(re.getInvokeId(),Long.valueOf(1L));
        assertNotNull(re.getErrorCode());
        assertEquals(re.getErrorCode().getErrorType(),ErrorCodeType.Local);
        assertEquals(re.getErrorCode().getLocalErrorCode(),Long.valueOf(MAPErrorCode.busySubscriber));
        assertNotNull(re.getParameter());
        assertTrue(re.getParameter() instanceof MAPErrorMessageImpl);
        assertTrue(re.getParameter() instanceof MAPErrorMessageBusySubscriber);
        
        MAPErrorMessageBusySubscriber emBusySubscriber=(MAPErrorMessageBusySubscriber)re.getParameter();
        assertNotNull(emBusySubscriber.getExtensionContainer());        
        innerContainer =  emBusySubscriber.getExtensionContainer();
        assertNotNull(innerContainer.getPrivateExtensionList());
        assertNotNull(innerContainer.getPcsExtensions());
        assertEquals(innerContainer.getPrivateExtensionList().size(),3);
        assertNotNull(innerContainer.getPrivateExtensionList().get(0).getData());
        assertNull(innerContainer.getPrivateExtensionList().get(1).getData());
        assertNotNull(innerContainer.getPrivateExtensionList().get(2).getData());
        
        assertNotNull(innerContainer.getPcsExtensions()); 
        
        assertTrue(emBusySubscriber.getCcbsPossible());
        assertTrue(emBusySubscriber.getCcbsBusy());
        
        //CUG Reject
        result=parser.decode(Unpooled.wrappedBuffer(dataCUGRejectFull));
        
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ReturnErrorImpl);
        re = (ReturnErrorImpl)result.getResult();
        assertEquals(re.getInvokeId(),Long.valueOf(1L));
        assertNotNull(re.getErrorCode());
        assertEquals(re.getErrorCode().getErrorType(),ErrorCodeType.Local);
        assertEquals(re.getErrorCode().getLocalErrorCode(),Long.valueOf(MAPErrorCode.cugReject));
        assertNotNull(re.getParameter());
        assertTrue(re.getParameter() instanceof MAPErrorMessageImpl);
        assertTrue(re.getParameter() instanceof MAPErrorMessageCUGReject);
        
        MAPErrorMessageCUGReject emCUGReject=(MAPErrorMessageCUGReject)re.getParameter();
        assertNotNull(emCUGReject.getExtensionContainer());        
        innerContainer =  emCUGReject.getExtensionContainer();
        assertNotNull(innerContainer.getPrivateExtensionList());
        assertNotNull(innerContainer.getPcsExtensions());
        assertEquals(innerContainer.getPrivateExtensionList().size(),3);
        assertNotNull(innerContainer.getPrivateExtensionList().get(0).getData());
        assertNull(innerContainer.getPrivateExtensionList().get(1).getData());
        assertNotNull(innerContainer.getPrivateExtensionList().get(2).getData());
        
        assertNotNull(innerContainer.getPcsExtensions()); 
        
        assertEquals(emCUGReject.getCUGRejectCause(), CUGRejectCause.subscriberNotMemberOfCUG);
        
        //Roaming Not Allowed
        result=parser.decode(Unpooled.wrappedBuffer(dataRoamingNotAllowedFull));
        
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ReturnErrorImpl);
        re = (ReturnErrorImpl)result.getResult();
        assertEquals(re.getInvokeId(),Long.valueOf(1L));
        assertNotNull(re.getErrorCode());
        assertEquals(re.getErrorCode().getErrorType(),ErrorCodeType.Local);
        assertEquals(re.getErrorCode().getLocalErrorCode(),Long.valueOf(MAPErrorCode.roamingNotAllowed));
        assertNotNull(re.getParameter());
        assertTrue(re.getParameter() instanceof MAPErrorMessageImpl);
        assertTrue(re.getParameter() instanceof MAPErrorMessageRoamingNotAllowed);
        
        MAPErrorMessageRoamingNotAllowed emRoamingNotAllowed=(MAPErrorMessageRoamingNotAllowed)re.getParameter();
        assertNotNull(emRoamingNotAllowed.getExtensionContainer());        
        innerContainer =  emRoamingNotAllowed.getExtensionContainer();
        assertNotNull(innerContainer.getPrivateExtensionList());
        assertNotNull(innerContainer.getPcsExtensions());
        assertEquals(innerContainer.getPrivateExtensionList().size(),3);
        assertNotNull(innerContainer.getPrivateExtensionList().get(0).getData());
        assertNull(innerContainer.getPrivateExtensionList().get(1).getData());
        assertNotNull(innerContainer.getPrivateExtensionList().get(2).getData());
        
        assertNotNull(innerContainer.getPcsExtensions()); 
        
        assertEquals(emRoamingNotAllowed.getRoamingNotAllowedCause(), RoamingNotAllowedCause.plmnRoamingNotAllowed);
        assertEquals(emRoamingNotAllowed.getAdditionalRoamingNotAllowedCause(),AdditionalRoamingNotAllowedCause.supportedRATTypesNotAllowed);
        
        //SS Error Status
        result=parser.decode(Unpooled.wrappedBuffer(dataSsErrorStatusFull));
        
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ReturnErrorImpl);
        re = (ReturnErrorImpl)result.getResult();
        assertEquals(re.getInvokeId(),Long.valueOf(1L));
        assertNotNull(re.getErrorCode());
        assertEquals(re.getErrorCode().getErrorType(),ErrorCodeType.Local);
        assertEquals(re.getErrorCode().getLocalErrorCode(),Long.valueOf(MAPErrorCode.ssErrorStatus));
        assertNotNull(re.getParameter());
        assertTrue(re.getParameter() instanceof MAPErrorMessage);
        assertTrue(re.getParameter() instanceof MAPErrorMessageSsErrorStatusImpl);
        MAPErrorMessageSsErrorStatusImpl emSsErrorStatus=(MAPErrorMessageSsErrorStatusImpl)re.getParameter();
        assertFalse(emSsErrorStatus.getQBit());
        assertTrue(emSsErrorStatus.getPBit());
        assertTrue(emSsErrorStatus.getRBit());
        assertFalse(emSsErrorStatus.getABit());
        assertEquals(emSsErrorStatus.getData(), 6); 
        
        //SS Incompatibility
        result=parser.decode(Unpooled.wrappedBuffer(dataSsIncompatibilityFull));
        
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ReturnErrorImpl);
        re = (ReturnErrorImpl)result.getResult();
        assertEquals(re.getInvokeId(),Long.valueOf(1L));
        assertNotNull(re.getErrorCode());
        assertEquals(re.getErrorCode().getErrorType(),ErrorCodeType.Local);
        assertEquals(re.getErrorCode().getLocalErrorCode(),Long.valueOf(MAPErrorCode.ssIncompatibility));
        assertNotNull(re.getParameter());
        assertTrue(re.getParameter() instanceof MAPErrorMessage);
        assertTrue(re.getParameter() instanceof MAPErrorMessageSsIncompatibilityImpl);
        MAPErrorMessageSsIncompatibilityImpl emSsIncompatibility=(MAPErrorMessageSsIncompatibilityImpl)re.getParameter();
        assertEquals(emSsIncompatibility.getSSCode().getSupplementaryCodeValue(), SupplementaryCodeValue.cfu);
        assertEquals(emSsIncompatibility.getBasicService().getTeleservice().getTeleserviceCodeValue(),TeleserviceCodeValue.telephony);
        assertTrue(emSsIncompatibility.getSSStatus().getQBit());
        assertFalse(emSsIncompatibility.getSSStatus().getPBit());
        assertFalse(emSsIncompatibility.getSSStatus().getRBit());
        assertTrue(emSsIncompatibility.getSSStatus().getABit());
        
        //Pw Registration Failure
        result=parser.decode(Unpooled.wrappedBuffer(dataPwRegistrationFailureFull));
        
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ReturnErrorImpl);
        re = (ReturnErrorImpl)result.getResult();
        assertEquals(re.getInvokeId(),Long.valueOf(1L));
        assertNotNull(re.getErrorCode());
        assertEquals(re.getErrorCode().getErrorType(),ErrorCodeType.Local);
        assertEquals(re.getErrorCode().getLocalErrorCode(),Long.valueOf(MAPErrorCode.pwRegistrationFailure));
        assertNotNull(re.getParameter());
        assertTrue(re.getParameter() instanceof MAPErrorMessage);
        assertTrue(re.getParameter() instanceof MAPErrorMessagePwRegistrationFailureImpl);
        MAPErrorMessagePwRegistrationFailureImpl emPwRegistrationFailure=(MAPErrorMessagePwRegistrationFailureImpl)re.getParameter();
        assertEquals(emPwRegistrationFailure.getPWRegistrationFailureCause(), PWRegistrationFailureCause.newPasswordsMismatch);
	}
	
	@Test(groups = { "functional.encode", "dialog.message" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.loadClass(ReturnErrorImpl.class);
    	ErrorCodeImpl errorCode=new ErrorCodeImpl();
    	errorCode.setLocalErrorCode((long)MAPErrorCode.unexpectedDataValue);
    	parser.registerLocalMapping(ReturnErrorImpl.class, errorCode, MAPErrorMessageExtensionContainerImpl.class);
    	parser.registerAlternativeClassMapping(MAPErrorMessageExtensionContainerImpl.class, MAPErrorMessageExtensionContainerImpl.class);
    	
    	errorCode=new ErrorCodeImpl();
    	errorCode.setLocalErrorCode((long)MAPErrorCode.smDeliveryFailure);
    	parser.registerLocalMapping(ReturnErrorImpl.class, errorCode, MAPErrorMessageSMDeliveryFailure1Impl.class);
    	parser.registerAlternativeClassMapping(MAPErrorMessageSMDeliveryFailure1Impl.class, MAPErrorMessageSMDeliveryFailureImpl.class);
    	parser.registerAlternativeClassMapping(MAPErrorMessageSMDeliveryFailure1Impl.class, MAPErrorMessageSMDeliveryFailure1Impl.class);
        
    	errorCode=new ErrorCodeImpl();
    	errorCode.setLocalErrorCode((long)MAPErrorCode.absentSubscriberSM);
    	parser.registerLocalMapping(ReturnErrorImpl.class, errorCode, MAPErrorMessageAbsentSubscriberSMImpl.class);
    	parser.registerAlternativeClassMapping(MAPErrorMessageAbsentSubscriberSMImpl.class, MAPErrorMessageAbsentSubscriberSMImpl.class);
    	
    	errorCode=new ErrorCodeImpl();
    	errorCode.setLocalErrorCode((long)MAPErrorCode.systemFailure);
    	parser.registerLocalMapping(ReturnErrorImpl.class, errorCode, MAPErrorMessageSytemFailure1Impl.class);
    	parser.registerAlternativeClassMapping(MAPErrorMessageSytemFailure1Impl.class, MAPErrorMessageSytemFailure1Impl.class);
    	parser.registerAlternativeClassMapping(MAPErrorMessageSytemFailure1Impl.class, MAPErrorMessageSystemFailureImpl.class);
    	
    	errorCode=new ErrorCodeImpl();
    	errorCode.setLocalErrorCode((long)MAPErrorCode.callBarred);
    	parser.registerLocalMapping(ReturnErrorImpl.class, errorCode, MAPErrorMessageCallBarred1Impl.class);
    	parser.registerAlternativeClassMapping(MAPErrorMessageCallBarred1Impl.class, MAPErrorMessageCallBarred1Impl.class);
    	parser.registerAlternativeClassMapping(MAPErrorMessageCallBarred1Impl.class, MAPErrorMessageCallBarredImpl.class);
    	
    	errorCode=new ErrorCodeImpl();
    	errorCode.setLocalErrorCode((long)MAPErrorCode.facilityNotSupported);
    	parser.registerLocalMapping(ReturnErrorImpl.class, errorCode, MAPErrorMessageFacilityNotSupImpl.class);
    	parser.registerAlternativeClassMapping(MAPErrorMessageFacilityNotSupImpl.class, MAPErrorMessageFacilityNotSupImpl.class);
    	
    	errorCode=new ErrorCodeImpl();
    	errorCode.setLocalErrorCode((long)MAPErrorCode.unknownSubscriber);
    	parser.registerLocalMapping(ReturnErrorImpl.class, errorCode, MAPErrorMessageUnknownSubscriberImpl.class);
    	parser.registerAlternativeClassMapping(MAPErrorMessageUnknownSubscriberImpl.class, MAPErrorMessageUnknownSubscriberImpl.class);
    	
    	errorCode=new ErrorCodeImpl();
    	errorCode.setLocalErrorCode((long)MAPErrorCode.subscriberBusyForMTSMS);
    	parser.registerLocalMapping(ReturnErrorImpl.class, errorCode, MAPErrorMessageSubscriberBusyForMtSmsImpl.class);
    	parser.registerAlternativeClassMapping(MAPErrorMessageSubscriberBusyForMtSmsImpl.class, MAPErrorMessageSubscriberBusyForMtSmsImpl.class);
    	
    	errorCode=new ErrorCodeImpl();
    	errorCode.setLocalErrorCode((long)MAPErrorCode.absentSubscriber);
    	parser.registerLocalMapping(ReturnErrorImpl.class, errorCode, MAPErrorMessageAbsentSubscriber1Impl.class);
    	parser.registerAlternativeClassMapping(MAPErrorMessageAbsentSubscriber1Impl.class, MAPErrorMessageAbsentSubscriber1Impl.class);
    	parser.registerAlternativeClassMapping(MAPErrorMessageAbsentSubscriber1Impl.class, MAPErrorMessageAbsentSubscriberImpl.class);
        
    	errorCode=new ErrorCodeImpl();
    	errorCode.setLocalErrorCode((long)MAPErrorCode.unauthorizedLCSClient);
    	parser.registerLocalMapping(ReturnErrorImpl.class, errorCode, MAPErrorMessageUnauthorizedLCSClientImpl.class);
    	parser.registerAlternativeClassMapping(MAPErrorMessageUnauthorizedLCSClientImpl.class, MAPErrorMessageUnauthorizedLCSClientImpl.class);
    	
    	errorCode=new ErrorCodeImpl();
    	errorCode.setLocalErrorCode((long)MAPErrorCode.positionMethodFailure);
    	parser.registerLocalMapping(ReturnErrorImpl.class, errorCode, MAPErrorMessagePositionMethodFailureImpl.class);
    	parser.registerAlternativeClassMapping(MAPErrorMessagePositionMethodFailureImpl.class, MAPErrorMessagePositionMethodFailureImpl.class);
    	
    	errorCode=new ErrorCodeImpl();
    	errorCode.setLocalErrorCode((long)MAPErrorCode.busySubscriber);
    	parser.registerLocalMapping(ReturnErrorImpl.class, errorCode, MAPErrorMessageBusySubscriberImpl.class);
    	parser.registerAlternativeClassMapping(MAPErrorMessageBusySubscriberImpl.class, MAPErrorMessageBusySubscriberImpl.class);
    	
    	errorCode=new ErrorCodeImpl();
    	errorCode.setLocalErrorCode((long)MAPErrorCode.cugReject);
    	parser.registerLocalMapping(ReturnErrorImpl.class, errorCode, MAPErrorMessageCUGRejectImpl.class);
    	parser.registerAlternativeClassMapping(MAPErrorMessageCUGRejectImpl.class, MAPErrorMessageCUGRejectImpl.class);
    	
    	errorCode=new ErrorCodeImpl();
    	errorCode.setLocalErrorCode((long)MAPErrorCode.roamingNotAllowed);
    	parser.registerLocalMapping(ReturnErrorImpl.class, errorCode, MAPErrorMessageRoamingNotAllowedImpl.class);
    	parser.registerAlternativeClassMapping(MAPErrorMessageRoamingNotAllowedImpl.class, MAPErrorMessageRoamingNotAllowedImpl.class);
    	
    	errorCode=new ErrorCodeImpl();
    	errorCode.setLocalErrorCode((long)MAPErrorCode.ssErrorStatus);
    	parser.registerLocalMapping(ReturnErrorImpl.class, errorCode, MAPErrorMessageSsErrorStatusImpl.class);
    	parser.registerAlternativeClassMapping(MAPErrorMessageSsErrorStatusImpl.class, MAPErrorMessageSsErrorStatusImpl.class);
    	
    	errorCode=new ErrorCodeImpl();
    	errorCode.setLocalErrorCode((long)MAPErrorCode.ssIncompatibility);
    	parser.registerLocalMapping(ReturnErrorImpl.class, errorCode, MAPErrorMessageSsIncompatibilityImpl.class);
    	parser.registerAlternativeClassMapping(MAPErrorMessageSsIncompatibilityImpl.class, MAPErrorMessageSsIncompatibilityImpl.class);
    	
    	errorCode=new ErrorCodeImpl();
    	errorCode.setLocalErrorCode((long)MAPErrorCode.pwRegistrationFailure);
    	parser.registerLocalMapping(ReturnErrorImpl.class, errorCode, MAPErrorMessagePwRegistrationFailureImpl.class);
    	parser.registerAlternativeClassMapping(MAPErrorMessagePwRegistrationFailureImpl.class, MAPErrorMessagePwRegistrationFailureImpl.class);
    	
    	MAPErrorMessageFactoryImpl fact = new MAPErrorMessageFactoryImpl();
        
        //EXT ERROR
        MAPErrorMessage em = fact.createMAPErrorMessageExtensionContainer((long)MAPErrorCode.unexpectedDataValue,MAPExtensionContainerTest.GetTestExtensionContainer());
        ReturnErrorImpl re=new ReturnErrorImpl();
        re.setParameter(em);
        re.setInvokeId(1L);
        
        re.setErrorCode((long)MAPErrorCode.unexpectedDataValue);
        
        ByteBuf buffer=parser.encode(re);
        byte[] data=new byte[buffer.readableBytes()];
        buffer.readBytes(data);
        assertTrue(Arrays.equals(data, dataExtContainerFull));
        
        //SM FAILURE
        em = fact.createMAPErrorMessageSMDeliveryFailure((long)MAPErrorCode.smDeliveryFailure,SMEnumeratedDeliveryFailureCause.invalidSMEAddress, null, null);
        re=new ReturnErrorImpl();
        re.setParameter(em);
        re.setInvokeId(1L);
        
        re.setErrorCode((long)MAPErrorCode.smDeliveryFailure);
        buffer=parser.encode(re);
        data=new byte[buffer.readableBytes()];
        buffer.readBytes(data);
        assertTrue(Arrays.equals(data, dataSMDeliveryFailure));  
        
        //SM FAILURE FULL
        em = fact.createMAPErrorMessageSMDeliveryFailure(3,SMEnumeratedDeliveryFailureCause.scCongestion, null, MAPExtensionContainerTest.GetTestExtensionContainer());
        FailureCauseImpl failureCause = new FailureCauseImpl(213);
        ProtocolIdentifierImpl protocolIdentifier = new ProtocolIdentifierImpl(127);
        DataCodingSchemeImpl dataCodingScheme = new DataCodingSchemeImpl(246);
        UserDataImpl userData = new UserDataImpl(Unpooled.wrappedBuffer(uData), dataCodingScheme, uData.length, false, null);
        SmsDeliverReportTpduImpl tpdu = new SmsDeliverReportTpduImpl(failureCause, protocolIdentifier, userData);
        ((MAPErrorMessageSMDeliveryFailure)em).setSmsDeliverReportTpdu(tpdu);
        re=new ReturnErrorImpl();
        re.setParameter(em);
        re.setInvokeId(1L);
        
        re.setErrorCode((long)MAPErrorCode.smDeliveryFailure);
        buffer=parser.encode(re);
        data=new byte[buffer.readableBytes()];
        buffer.readBytes(data);
        assertTrue(Arrays.equals(data, dataSMDeliveryFailureFull)); 
        
        //SM FAILURE V1
        em = fact.createMAPErrorMessageSMDeliveryFailure(1,SMEnumeratedDeliveryFailureCause.equipmentProtocolError, null, null);
        re=new ReturnErrorImpl();
        re.setParameter(em);
        re.setInvokeId(1L);
        
        re.setErrorCode((long)MAPErrorCode.smDeliveryFailure);
        buffer=parser.encode(re);
        data=new byte[buffer.readableBytes()];
        buffer.readBytes(data);
        assertTrue(Arrays.equals(data, dataSMDeliveryFailureV1));  
        
        //Absent Subscriber SM
        em = fact.createMAPErrorMessageAbsentSubscriberSM(AbsentSubscriberDiagnosticSM.IMSIDetached, null, null);
        re=new ReturnErrorImpl();
        re.setParameter(em);
        re.setInvokeId(1L);
        
        re.setErrorCode((long)MAPErrorCode.absentSubscriberSM);
        buffer=parser.encode(re);
        data=new byte[buffer.readableBytes()];
        buffer.readBytes(data);
        assertTrue(Arrays.equals(data, dataAbsentSubscriberSM)); 
        
        //Absent Subscriber SM Full
        em = fact.createMAPErrorMessageAbsentSubscriberSM(AbsentSubscriberDiagnosticSM.NoPagingResponseViaTheMSC, MAPExtensionContainerTest.GetTestExtensionContainer(),AbsentSubscriberDiagnosticSM.GPRSDetached);
        re=new ReturnErrorImpl();
        re.setParameter(em);
        re.setInvokeId(1L);
        
        re.setErrorCode((long)MAPErrorCode.absentSubscriberSM);
        buffer=parser.encode(re);
        data=new byte[buffer.readableBytes()];
        buffer.readBytes(data);
        assertTrue(Arrays.equals(data, dataAbsentSubscriberSMFull)); 
        
        //System Failure
        em = fact.createMAPErrorMessageSystemFailure(2, NetworkResource.plmn, null, null);
        re=new ReturnErrorImpl();
        re.setParameter(em);
        re.setInvokeId(1L);
        
        re.setErrorCode((long)MAPErrorCode.systemFailure);
        buffer=parser.encode(re);
        data=new byte[buffer.readableBytes()];
        buffer.readBytes(data);
        assertTrue(Arrays.equals(data, dataSystemFailure));
        
        //System Failure Full
        em = fact.createMAPErrorMessageSystemFailure(3, NetworkResource.vlr,AdditionalNetworkResource.gsmSCF, MAPExtensionContainerTest.GetTestExtensionContainer());
        re=new ReturnErrorImpl();
        re.setParameter(em);
        re.setInvokeId(1L);
        
        re.setErrorCode((long)MAPErrorCode.systemFailure);
        buffer=parser.encode(re);
        data=new byte[buffer.readableBytes()];
        buffer.readBytes(data);
        assertTrue(Arrays.equals(data, dataSystemFailureFull)); 
        
        //Call Barred
        em = fact.createMAPErrorMessageCallBarred(3L, CallBarringCause.operatorBarring, null, null);re=new ReturnErrorImpl();
        re.setParameter(em);
        re.setInvokeId(1L);
        
        re.setErrorCode((long)MAPErrorCode.callBarred);
        buffer=parser.encode(re);
        data=new byte[buffer.readableBytes()];
        buffer.readBytes(data);
        assertTrue(Arrays.equals(data, dataCallBarred));  
        
        //Call Barred Full
        em = fact.createMAPErrorMessageCallBarred(3L, CallBarringCause.operatorBarring, MAPExtensionContainerTest.GetTestExtensionContainer(), true);
        re=new ReturnErrorImpl();
        re.setParameter(em);
        re.setInvokeId(1L);
        
        re.setErrorCode((long)MAPErrorCode.callBarred);
        buffer=parser.encode(re);
        data=new byte[buffer.readableBytes()];
        buffer.readBytes(data);
        assertTrue(Arrays.equals(data, dataCallBarredFull)); 
        
        //Facility Not Supported
        em = fact.createMAPErrorMessageFacilityNotSup(MAPExtensionContainerTest.GetTestExtensionContainer(), true, true);
        re.setParameter(em);
        re.setInvokeId(1L);
        
        re.setErrorCode((long)MAPErrorCode.facilityNotSupported);
        buffer=parser.encode(re);
        data=new byte[buffer.readableBytes()];
        buffer.readBytes(data);
        assertTrue(Arrays.equals(data, dataFacilityNotSupFull));  
        
        //Unknown Subscriber
        em = fact.createMAPErrorMessageUnknownSubscriber(MAPExtensionContainerTest.GetTestExtensionContainer(), UnknownSubscriberDiagnostic.gprsSubscriptionUnknown);
        re.setParameter(em);
        re.setInvokeId(1L);
        
        re.setErrorCode((long)MAPErrorCode.unknownSubscriber);
        buffer=parser.encode(re);
        data=new byte[buffer.readableBytes()];
        buffer.readBytes(data);
        assertTrue(Arrays.equals(data, dataUnknownSubscriberFull));  
        
        //Subscriber Busy For MT SMS
        em = fact.createMAPErrorMessageSubscriberBusyForMtSms(MAPExtensionContainerTest.GetTestExtensionContainer(), true);
        re.setParameter(em);
        re.setInvokeId(1L);
        
        re.setErrorCode((long)MAPErrorCode.subscriberBusyForMTSMS);
        buffer=parser.encode(re);
        data=new byte[buffer.readableBytes()];
        buffer.readBytes(data);
        assertTrue(Arrays.equals(data, dataSubscriberBusyForMTSMSFull));  
        
        //Absent Subscriber
        em = fact.createMAPErrorMessageAbsentSubscriber(MAPExtensionContainerTest.GetTestExtensionContainer(), AbsentSubscriberReason.purgedMS);
        re.setParameter(em);
        re.setInvokeId(1L);
        
        re.setErrorCode((long)MAPErrorCode.absentSubscriber);
        buffer=parser.encode(re);
        data=new byte[buffer.readableBytes()];
        buffer.readBytes(data);
        assertTrue(Arrays.equals(data, dataAbsentSubscriberFull)); 
        
        //Absent Subscriber V1
        em = fact.createMAPErrorMessageAbsentSubscriber(true);
        re.setParameter(em);
        re.setInvokeId(1L);
        
        re.setErrorCode((long)MAPErrorCode.absentSubscriber);
        buffer=parser.encode(re);
        data=new byte[buffer.readableBytes()];
        buffer.readBytes(data);
        assertTrue(Arrays.equals(data, dataAbsentSubscriberV1));
        
        //Unauthorized LCS Client
        em = fact.createMAPErrorMessageUnauthorizedLCSClient(UnauthorizedLCSClientDiagnostic.callToClientNotSetup, MAPExtensionContainerTest.GetTestExtensionContainer());
        re.setParameter(em);
        re.setInvokeId(1L);
        
        re.setErrorCode((long)MAPErrorCode.unauthorizedLCSClient);
        buffer=parser.encode(re);
        data=new byte[buffer.readableBytes()];
        buffer.readBytes(data);
        assertTrue(Arrays.equals(data, dataUnauthorizedLCSClientFull));   
        
        //Position Method Failure
        em = fact.createMAPErrorMessagePositionMethodFailure(PositionMethodFailureDiagnostic.locationProcedureNotCompleted,MAPExtensionContainerTest.GetTestExtensionContainer());
        re.setParameter(em);
        re.setInvokeId(1L);
        
        re.setErrorCode((long)MAPErrorCode.positionMethodFailure);
        buffer=parser.encode(re);
        data=new byte[buffer.readableBytes()];
        buffer.readBytes(data);
        assertTrue(Arrays.equals(data, dataPositionMethodFailureFull));
        
        //Busy Subscriber
        em = fact.createMAPErrorMessageBusySubscriber(MAPExtensionContainerTest.GetTestExtensionContainer(), true, true);
        re.setParameter(em);
        re.setInvokeId(1L);
        
        re.setErrorCode((long)MAPErrorCode.busySubscriber);
        buffer=parser.encode(re);
        data=new byte[buffer.readableBytes()];
        buffer.readBytes(data);
        assertTrue(Arrays.equals(data, dataBusySubscriberFull));
        
        //CUG Reject
        em = fact.createMAPErrorMessageCUGReject(CUGRejectCause.subscriberNotMemberOfCUG,MAPExtensionContainerTest.GetTestExtensionContainer());
        re.setParameter(em);
        re.setInvokeId(1L);
        
        re.setErrorCode((long)MAPErrorCode.cugReject);
        buffer=parser.encode(re);
        data=new byte[buffer.readableBytes()];
        buffer.readBytes(data);
        assertTrue(Arrays.equals(data, dataCUGRejectFull));
        
        //Roaming Not Allowed
        em = fact.createMAPErrorMessageRoamingNotAllowed(RoamingNotAllowedCause.plmnRoamingNotAllowed,MAPExtensionContainerTest.GetTestExtensionContainer(),AdditionalRoamingNotAllowedCause.supportedRATTypesNotAllowed);
        re.setParameter(em);
        re.setInvokeId(1L);
        
        re.setErrorCode((long)MAPErrorCode.roamingNotAllowed);
        buffer=parser.encode(re);
        data=new byte[buffer.readableBytes()];
        buffer.readBytes(data);
        assertTrue(Arrays.equals(data, dataRoamingNotAllowedFull));
        
        //SS Error Status
        em = fact.createMAPErrorMessageSsErrorStatus(false, true, true, false);
        re.setParameter(em);
        re.setInvokeId(1L);
        
        re.setErrorCode((long)MAPErrorCode.ssErrorStatus);
        buffer=parser.encode(re);
        data=new byte[buffer.readableBytes()];
        buffer.readBytes(data);
        assertTrue(Arrays.equals(data, dataSsErrorStatusFull));
        
        //SS incompatibility
        SSCodeImpl ssCode = new SSCodeImpl(SupplementaryCodeValue.cfu);
        TeleserviceCodeImpl teleservice = new TeleserviceCodeImpl(TeleserviceCodeValue.telephony);
        BasicServiceCodeImpl basicService = new BasicServiceCodeImpl(teleservice);
        SSStatusImpl ssStatus = new SSStatusImpl(true, false, false, true);
        em = fact.createMAPErrorMessageSsIncompatibility(ssCode, basicService, ssStatus);
        re.setParameter(em);
        re.setInvokeId(1L);
                
        re.setErrorCode((long)MAPErrorCode.ssIncompatibility);
        buffer=parser.encode(re);
        data=new byte[buffer.readableBytes()];
        buffer.readBytes(data);
        assertTrue(Arrays.equals(data, dataSsIncompatibilityFull));
        
        //Pw Registration Failure
        em = (MAPErrorMessageImpl) fact.createMAPErrorMessagePwRegistrationFailure(PWRegistrationFailureCause.newPasswordsMismatch);
        re.setParameter(em);
        re.setInvokeId(1L);
                
        re.setErrorCode((long)MAPErrorCode.pwRegistrationFailure);
        buffer=parser.encode(re);
        data=new byte[buffer.readableBytes()];
        buffer.readBytes(data);
        assertTrue(Arrays.equals(data, dataPwRegistrationFailureFull));
    }
}