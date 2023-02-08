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

package org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement;

import org.restcomm.protocols.ss7.commonapp.primitives.TbcdStringWithFillerImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.GroupId;

/**
 *
 * @author Lasith Waruna Perera
 * @author yulianoifa
 *
 */
public class GroupIdImpl extends TbcdStringWithFillerImpl implements GroupId {
	public GroupIdImpl() {
        super(3, 3, "GroupId");
    }

    public GroupIdImpl(String data) {
        super(3, 3, "GroupId", data);
    }

    public String getGroupId() {
        return this.data;
    }

}
