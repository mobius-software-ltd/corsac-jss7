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
package org.restcomm.protocols.ss7.inap.api;
/**
 * @author yulian.oifa
 *
 */
public interface INAPOperationCode {
	//-- the operations are grouped by the identified ASEs.
	//-- SCF activation ASE
	//initialDP InitialDP ::= localValue 0
	int initialDP = 0;
	
	//-- Basic BCP DP ASE - from Q.1218 CS1
	//originationAttemptAuthorized OriginationAttemptAuthorized ::= localValue 1
	int originationAttemptAuthorized = 1;
	//collectedInformation CollectedInformation ::= localValue 2
	int collectedInformation = 2;
	//analysedInformation AnalysedInformation ::= localValue 3
	int analysedInformation = 3;
	//routeSelectFailure RouteSelectFailure ::= localValue 4
	int routeSelectFailure = 4;
	//oCalledPartyBusy OCalledPartyBusy ::= localValue 5
	int oCalledPartyBusy = 5;
	//oNoAnswer ONoAnswer ::= localValue 6
	int oNoAnswer = 6;
	//oAnswer OAnswer ::= localValue 7
	int oAnswer = 7;
	//oDisconnect ODisconnect ::= localValue 8
	int oDisconnect = 8;
	//termAttemptAuthorized TermAttemptAuthorized ::= localValue 9
	int termAttemptAuthorized = 9;
	//tBusy TBusy ::= localValue 10
	int tBusy = 10;
	//tNoAnswer TNoAnswer ::= localValue 11
	int tNoAnswer = 11;
	//tAnswer TAnswer ::= localValue 12
	int tAnswer = 12;
	//tDisconnect TDisconnect ::= localValue 13
	int tDisconnect = 13;

	//-- Advanced BCP DP ASE - from Q.1218 CS1
	//oMidCall OMidCall ::= localValue 14
	int oMidCall = 14;
	//tMidCall TMidCall ::= localValue 15
	int tMidCall = 15;
	
	//-- SCF/SRF activation of assist ASE
	//assistRequestInstructions AssistRequestInstructions ::= localValue 16
	int assistRequestInstructions = 16;
	   
	//-- Assist connection establishment ASE
	//establishTemporaryConnection EstablishTemporaryConnection ::= localValue 17
	int establishTemporaryConnection = 17;
	   
	//-- Generic disconnect resource ASE
	//disconnectForwardConnection DisconnectForwardConnection ::= localValue 18
	int disconnectForwardConnection = 18;
	   
	//-- Non-assisted connection establishment ASE
	//connectToResource ConnectToResource ::= localValue 19
	int connectToResource = 19;
	   
	//-- Connect ASE (elementary SSF function)
	//connect Connect ::= localValue 20
	int connect = 20;
	   
	//-- Call handling ASE (elementary SSF function)
	//holdCallInNetwork HoldCallInNetwork ::= localValue 21 - from Q.1218 CS1
	int holdCallInNetwork = 21;
	//releaseCall ReleaseCall ::= localValue 22
	int releaseCall = 22;
	   
	//-- BCSM Event handling ASE
	//requestReportBCSMEvent RequestReportBCSMEvent ::= localValue 23
	int requestReportBCSMEvent = 23;
	   
	//eventReportBCSM EventReportBCSM ::= localValue 24
	int eventReportBCSM = 24;
	   
	//-- Charging Event handling ASE
	//requestNotificationChargingEvent RequestNotificationChargingEvent ::= localValue 25
	int requestNotificationChargingEvent = 25;
	//eventNotificationCharging EventNotificationCharging ::= localValue 26
	int eventNotificationCharging = 26;
	
	//-- SSF call processing ASE
	//collectInformation CollectInformation ::= localValue 27
	int collectInformation = 27;
	//analyseInformation AnalyseInformation ::= localValue 28 - from Q.1218 CS1
	int analyseInformation = 28;
	//selectRoute SelectRoute ::= localValue 29 - from Q.1218 CS1
	int selectRoute = 29;
	//selectFacility SelectFacility ::= localValue 30 - from Q.1218 CS1
	int selectFacility = 30;
	//continue Continue ::= localValue 31
	int continueCode = 31;
	   
	//-- SCF call initiation ASE
	//initiateCallAttempt InitiateCallAttempt ::= localValue 32
	int initiateCallAttempt = 32;
	   	
	//-- Timer ASE
	//resetTimer ResetTimer ::= localValue 33
	int resetTimer = 33;
	   
	//-- Billing ASE
	//furnishChargingInformation FurnishChargingInformation ::= localValue 34
	int furnishChargingInformation = 34;
	   
	//-- Charging ASE
	//applyCharging ApplyCharging ::= localValue 35
	int applyCharging = 35;	   
	//applyChargingReport ApplyChargingReport ::= localValue 36
	int applyChargingReport = 36;
	   
	//-- Status reporting ASE - from Q.1218 CS1
	//requestCurrentStatusReport RequestCurrentStatusReport ::= localValue 37
	int requestCurrentStatusReport=37;
	//requestEveryStatusChangeReport RequestEveryStatusChangeReport ::= localValue 38
	int requestEveryStatusChangeReport=38;
	//requestFirstStatusMatchReport RequestFirstStatusMatchReport ::= localValue 39
	int requestFirstStatusMatchReport=39;
	//statusReport StatusReport ::= localValue 40
	int statusReport=40;
	
	//-- Traffic management ASE
	//callGap CallGap ::= localValue 41
	int callGap = 41;
	   
	//-- Service management ASE
	//activateServiceFiltering ActivateServiceFiltering ::= localValue 42
	int activateServiceFiltering = 42;
	//serviceFilteringResponse ServiceFilteringResponse ::= localValue 43
	int serviceFilteringResponse = 43;
	   
	//-- Call report ASE
	//callInformationReport CallInformationReport ::= localValue 44
	int callInformationReport = 44;
	//callInformationRequest CallInformationRequest ::= localValue 45
	int callInformationRequest = 45;
	   
	//-- Signalling control ASE
	//sendChargingInformation SendChargingInformation ::= localValue 46
	int sendChargingInformation = 46;
	   
	//-- Specialized resource control ASE
	//playAnnouncement PlayAnnouncement ::= localValue 47
	int playAnnouncement = 47;
	//promptAndCollectUserInformation PromptAndCollectUserInformation ::= localValue 48
	int promptAndCollectUserInformation = 48;
	//specializedResourceReport SpecializedResourceReport ::= localValue 49
	int specializedResourceReport = 49;
	//-- Cancel ASE
	//cancel Cancel ::= localValue 53
	int cancelCode = 53;
	//cancelStatusReportRequest CancelStatusReportRequest ::= localValue 54 - from ITU Q.1218 CS1
	int cancelStatusReportRequest = 54;
	
	//-- Activity Test ASE
	//activityTest ActivityTest ::= localValue 55
	int activityTest = 55;	
	
	//CS1+ Operations
	//callLimit CallLimit ::= localValue ‐1
	int callLimit = -1;
	//continueWithArgument ContinueWithArgument ::= localValue 88
	int continueWithArgument = 88;
	//dialogueUserInformation DialogueUserInformation ::= localValue ‐2
	int dialogueUserInformation = -2;
	//handOver HandOver ::= localValue ‐3
	int handOver = -3;
	//holdCallPartyConnection HoldCallPartyConnection ::= localValue ‐4
	int holdCallPartyConnection = -4;
	//reconnect Reconnect ::= localValue ‐5
	int reconnect = -5;
	//releaseCallPartyConnection ReleaseCallPartyConnection ::= localValue ‐6
	int releaseCallPartyConnection = -6;
	//signallingInformation SignallingInformation ::= localValue ‐8
	int signallingInformation = -8;
	//retrieve Retrieve ::= localValue 16
	int retrieve = 16;	
	//update Update ::= localValue 21
	int update = 21;		
}