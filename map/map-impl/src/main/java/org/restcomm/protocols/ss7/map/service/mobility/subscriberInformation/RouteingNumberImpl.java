package org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation;

import org.restcomm.protocols.ss7.commonapp.primitives.TbcdStringImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.RouteingNumber;

/**
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
public class RouteingNumberImpl extends TbcdStringImpl implements RouteingNumber {
	public RouteingNumberImpl() {
        super(1, 5, "RouteingNumber" ,false);
    }

    public RouteingNumberImpl(String data) {
        super(1, 5, "RouteingNumber", data ,false);
    }

    public String getRouteingNumber() {
        return data;
    }
}