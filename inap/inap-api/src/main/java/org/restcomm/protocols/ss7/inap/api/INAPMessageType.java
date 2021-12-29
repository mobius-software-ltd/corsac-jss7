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
public enum INAPMessageType {
   initialDP_Request,
   connect_Request,
   releaseCall_Request,
   eventReportBCSM_Request,
   requestReportBCSMEvent_Request,
   continue_Request,
   activityTest_Request,
   activityTest_Response,
   assistRequestInstructions_Request,
   establishTemporaryConnection_Request,
   disconnectForwardConnection_Request,
   disconnectForwardConnectionWithArgument_Request,
   connectToResource_Request,
   resetTimer_Request,
   furnishChargingInformation_Request,
   applyChargingReport_Request,
   applyCharging_Request,
   callInformationReport_Request,
   callInformationRequest_Request,
   sendChargingInformation_Request,
   specializedResourceReport_Request,
   playAnnouncement_Request,
   promptAndCollectUserInformation_Request,
   promptAndCollectUserInformation_Response,
   cancel_Request,
   continueWithArgument_Request,
   collectInformation_Request,
   callGap_Request,
   entityReleased_Request,
   disconnectLeg_Request,
   disconnectLeg_Response,
   moveLeg_Request,
   moveLeg_Response,
   splitLeg_Request,
   playTone_Request,
   initiateCallAttempt_Request,
   initiateCallAttempt_Response,
   collectInformationRequest_Request,
   cancelStatusReport_Request,
   callLimit_Request,
   analyzeInformation_Request,
   analyzedInformation_Request,
   activateServiceFiltering_Request,
   collectedInformation_Request,
   eventNotificationCharging_Request,
   dialogueUserInformation_Request,
   originationAttemptAuthorized_Request,
   oDisconnect_Request,
   oMidCall_Request,
   oNoAnswer_Request,
   oCalledPartyBusy_Request,
   oAnswer_Request,
   tDisconnect_Request,
   tMidCall_Request,
   tNoAnswer_Request,
   tAnswer_Request,
   tBusy_Request,
   termAttemptAuthorized_Request,
   handOver_Request,
   holdCallPartyConnection_Request,
   routeSelectFailure_Request,
   retrieve_Request,
   retrieve_Response,
   reconnect_Request,
   update_Request,
   update_Response,
   requestNotificationChargingEvent_Request,
   requestCurrentStatusReport_Request,
   requestCurrentStatusReport_Response,
   requestEveryStatusChangeReport_Request,
   requestFirstStatusMatchReport_Request,
   releaseCallPartyConnection_Request,
   statusReport_Request,
   selectFacility_Request,
   selectRoute_Request,
   signallingInformation_Request,
   serviceFiltering_Response;
}
