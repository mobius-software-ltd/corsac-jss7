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
	   
	//-- Traffic management ASE
	//callGap CallGap ::= localValue 41
	int callGap = 41;
	   
	//-- Service management ASE
	//activateServiceFiltering ActivateServiceFiltering ::= localValue 42
	int activateServiceFiltering = 44;
	//serviceFilteringResponse ServiceFilteringResponse ::= localValue 43
	int serviceFilteringResponse = 45;
	   
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
	//-- Activity Test ASE
	//activityTest ActivityTest ::= localValue 55
	int activityTest = 55;	
}