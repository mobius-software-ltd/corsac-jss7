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

package org.restcomm.protocols.ss7.cap.dialog;

import org.restcomm.protocols.ss7.cap.api.dialog.ServingCheckData;
import org.restcomm.protocols.ss7.cap.api.dialog.ServingCheckResult;
import org.restcomm.protocols.ss7.tcap.asn.tx.ApplicationContextNameImpl;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class ServingCheckDataImpl implements ServingCheckData {

    private ServingCheckResult result;
    private ApplicationContextNameImpl alternativeApplicationContext = null;

    public ServingCheckDataImpl(ServingCheckResult result) {
        this.result = result;
    }

    public ServingCheckDataImpl(ServingCheckResult result, ApplicationContextNameImpl alternativeApplicationContext) {
        this.result = result;
        this.alternativeApplicationContext = alternativeApplicationContext;
    }

    public ServingCheckResult getResult() {
        return this.result;
    }

    public ApplicationContextNameImpl getAlternativeApplicationContext() {
        return this.alternativeApplicationContext;
    }
}
