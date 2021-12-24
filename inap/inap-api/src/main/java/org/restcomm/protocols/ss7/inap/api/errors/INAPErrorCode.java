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
package org.restcomm.protocols.ss7.inap.api.errors;

/**
 * INAP Error codes Carried by ReturnError primitive
 *
 * @author yulian.oifa
 *
 */
public interface INAPErrorCode {
   int minimalCodeValue = 0;
   int maximumCodeValue = 100;
   
// -- The operation has been canceled.
   int canceled = 0;
   // -- The operation failed to be canceled.
   int cancelFailed = 1;
   // -- The establish temporary connection failed.
   int eTCFailed = 3;
   // -- The caller response was not as expected.
   int improperCallerResponse = 4;
   // -- The Service Logic Program could not be found in the gsmSCF.
   int missingCustomerRecord = 6;
   // -- An expected optional parameter was not received.
   int missingParameter = 7;
   // -- The parameter was not as expected (e.g. missing or out of range).
   int parameterOutOfRange = 8;
   // -- The requested information cannot be found.
   int requestedInfoError = 10;
   // -- The operation could not be completed due to a system failure at the serving physical entity.
   int systemFailure = 11;
   // -- An entity normally capable of the task requested cannot or chooses not to perform the task at
   // -- this time. This includes error situations like congestion and unobtainable address as used in
   // -- e.g. the connect operation.)
   int taskRefused = 12;
   // -- A requested resource is not available at the serving entity.
   int unavailableResource = 13;
   // -- An incorrect sequence of Components was received (e.g. 'DisconnectForwardConnection'
   // -- followed by 'PlayAnnouncement').
   int unexpectedComponentSequence = 14;
   // -- The data value was not as expected (e.g. route number expected but billing number received)
   int unexpectedDataValue = 15;
   // -- A parameter received was not expected.
   int unexpectedParameter = 16;
   // -- Leg not known to the gsmSSF.
   int unknownLegID = 17;
   
   //from Q.1218 CS1
   //-- Resource whose status is being requested is not known to the serving entity.
   int unknownResource = 18;
   
   //Ericsson CS1+
   //congestion Congestion ::= localValue 2
   int congestion=2;
   //errorInParameterValue ErrorInParameterValue ::= localValue 3
   int errorInParameterValue=3;
   //executionError ExecutionError ::= localValue 4
   int executionError=4;
   //illegalCombinationOfParameters IllegalCombinationOfParameters ::= localValue 7
   int illegalCombinationOfParameters=7; 
   //infoNotAvailable InfoNotAvailable ::= localValue 8
   int infoNotAvailable=8;
   //invalidDataItemID InvalidDataItemID ::= localValue 10
   int invalidDataItemID=10;
   //notAuthorized NotAuthorized ::= localValue 12
   int notAuthorized=12;
   //parameterMissing ParameterMissing ::= localValue 13
	int parameterMissing=13;
   //otherError OtherError ::= localValue 100
   int otherError=100;      
}