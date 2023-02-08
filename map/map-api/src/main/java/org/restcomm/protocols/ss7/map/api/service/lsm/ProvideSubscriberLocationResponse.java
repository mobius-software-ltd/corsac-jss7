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

package org.restcomm.protocols.ss7.map.api.service.lsm;

import org.restcomm.protocols.ss7.commonapp.api.primitives.CellGlobalIdOrServiceAreaIdOrLAI;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;

/**
<code>
ProvideSubscriberLocation-Res ::= SEQUENCE {
  locationEstimate                 Ext-GeographicalInformation,
  ageOfLocationEstimate            [0] AgeOfLocationInformation OPTIONAL,
  extensionContainer               [1] ExtensionContainer OPTIONAL,
  ... ,
  add-LocationEstimate             [2] Add-GeographicalInformation OPTIONAL,
  deferredmt-lrResponseIndicator   [3] NULL OPTIONAL,
  geranPositioningData             [4] PositioningDataInformation OPTIONAL,
  utranPositioningData             [5] UtranPositioningDataInfo OPTIONAL,
  cellIdOrSai                      [6] CellGlobalIdOrServiceAreaIdOrLAI OPTIONAL,
  sai-Present                      [7] NULL OPTIONAL,
  accuracyFulfilmentIndicator      [8] AccuracyFulfilmentIndicator OPTIONAL,
  velocityEstimate                 [9] VelocityEstimate OPTIONAL,
  mo-lrShortCircuitIndicator       [10] NULL OPTIONAL,
  geranGANSSpositioningData        [11] GeranGANSSpositioningData OPTIONAL,
  utranGANSSpositioningData        [12] UtranGANSSpositioningData OPTIONAL,
  targetServingNodeForHandover     [13] ServingNodeAddress OPTIONAL
}

-- if deferredmt-lrResponseIndicator is set, locationEstimate is ignored.

-- the add-LocationEstimate parameter shall not be sent to a node that did not indicate the
-- geographic shapes supported in the ProvideSubscriberLocation-Arg
-- The locationEstimate and the add-locationEstimate parameters shall not be sent if
-- the supportedGADShapes parameter has been received in ProvideSubscriberLocation-Arg
-- and the shape encoded in locationEstimate or add-LocationEstimate is not marked
-- as supported in supportedGADShapes. In such a case ProvideSubscriberLocation
-- shall be rejected with error FacilityNotSupported with additional indication
-- shapeOfLocationEstimateNotSupported.
-- sai-Present indicates that the cellIdOrSai parameter contains a Service Area Identity.

AgeOfLocationInformation ::= INTEGER (0..32767)
-- the value represents the elapsed time in minutes since the last
--network contact of the mobile station (i.e. the actuality of the
-- location information).
-- value "0" indicates that the MS is currently in contact with the
-- network
-- value "32767" indicates that the location information is at least
-- 32767 minutes old

</code>
 *
 *
 * @author amit bhayani
 * @author yulianoifa
 *
 */
public interface ProvideSubscriberLocationResponse extends LsmMessage {

	ExtGeographicalInformation getLocationEstimate();

	PositioningDataInformation getGeranPositioningData();

    UtranPositioningDataInfo getUtranPositioningData();

    Integer getAgeOfLocationEstimate();

    AddGeographicalInformation getAdditionalLocationEstimate();

    MAPExtensionContainer getExtensionContainer();

    boolean getDeferredMTLRResponseIndicator();

    CellGlobalIdOrServiceAreaIdOrLAI getCellIdOrSai();

    boolean getSaiPresent();

    AccuracyFulfilmentIndicator getAccuracyFulfilmentIndicator();

    VelocityEstimate getVelocityEstimate();

    boolean getMoLrShortCircuitIndicator();

    GeranGANSSpositioningData getGeranGANSSpositioningData();

    UtranGANSSpositioningData getUtranGANSSpositioningData();

    ServingNodeAddress getTargetServingNodeForHandover();

}
