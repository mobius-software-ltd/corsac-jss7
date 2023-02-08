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

package org.restcomm.protocols.ss7.map.api.service.mobility.pagingAndSearch;

import org.restcomm.protocols.ss7.commonapp.api.primitives.IMSI;
import org.restcomm.protocols.ss7.map.api.service.mobility.MobilityMessage;

/**
*
<code>
MAP_SEARCH_FOR_MS service

Parameter name             Request Indication  Response    Confirm
Invoke Id                  M       M(=)        M(=)        M(=)
IMSI                       M       M(=)
Current location area Id                       C           C(=)
User error                                     C           C(=)
Provider error                                             O

The following error causes defined in clause 7.6.1 shall be sent by the user if the search procedure fails, depending on the failure reason:
-   absent subscriber;
    this error cause is returned by the MSC if the MS does not respond to the paging request;
-   system failure;
-   this corresponds to the case where there is no call associated with the MAP_SEARCH_FOR_MS service, i.e. if the call has been released but the dialogue to the VLR has not been aborted;
-   busy subscriber;
-   unexpected data value.
</code>
*
*
* @author sergey vetyutnev
* @author yulianoifa
*
*/
public interface SearchForMsRequest extends MobilityMessage {

    IMSI getImsi();

}
