/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
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
package org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.restcomm.protocols.ss7.map.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.map.api.primitives.DiameterIdentityImpl;
import org.restcomm.protocols.ss7.map.api.primitives.FTNAddressStringImpl;
import org.restcomm.protocols.ss7.map.api.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNSubaddressStringImpl;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.primitives.NAEACICImpl;
import org.restcomm.protocols.ss7.map.api.primitives.NAEAPreferredCIImpl;
import org.restcomm.protocols.ss7.map.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.map.api.primitives.TimeImpl;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSClientExternalIDImpl;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSClientInternalID;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.LIPAPermission;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.PDPContextImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.SIPTOPermission;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.*;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSCodeImpl;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SupplementaryCodeValue;
import org.restcomm.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * 
 * @author Lasith Waruna Perera
 * 
 */
public class InsertSubscriberDataRequestTest {

    public byte[] getData() {
        return new byte[] { 48, -126, 18, 121, -128, 5, 17, 17, 33, 34, 34, -127, 4, -111, 34, 50, -12, -126, 1, 5, -125, 1, 1, -92, 3, 4, 1, 38, -90, 3, 4, 1, 16, -89, -127, -124, -96, -127, -127, 4, 1, 0, 48, 77, 48, 75, -126, 1, 38, -124, 1, 15, -123, 4, -111, 34, 34, -8, -120, 2, 2, 5, -122, 1, -92, -121, 1, 2, -87, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -118, 4, -111, 34, 34, -9, -96, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -88, 58, 3, 5, 4, 74, -43, 85, 80, 3, 2, 4, 80, 48, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -119, 0, -86, 4, 4, 2, 0, 2, -85, 62, 48, 60, 4, 3, -1, -1, -1, 5, 0, 48, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -128, 4, -11, -1, -1, -1, -84, 68, 48, 66, 4, 3, -1, -1, -1, 48, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, 3, 2, 5, -96, -128, 2, 7, -128, -127, 4, -11, -1, -1, -1, -83, -126, 3, 18, -96, 23, 48, 18, 48, 16, 10, 1, 4, 2, 1, 3, -128, 5, -111, 17, 34, 51, -13, -127, 1, 1, -128, 1, 2, -95, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -94, 111, 48, 58, 48, 3, 4, 1, 96, 4, 4, -111, 34, 50, -11, -96, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, 48, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -128, 0, -127, 0, -92, 98, 48, 96, 10, 1, 2, -96, 28, -128, 1, 1, -95, 12, 4, 4, -111, 34, 50, -12, 4, 4, -111, 34, 50, -11, -94, 9, 2, 1, 2, 2, 1, 4, 2, 1, 1, -95, 6, -126, 1, 38, -125, 1, 16, -126, 1, 0, -93, 3, 4, 1, 7, -92, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -91, 68, 48, 6, 4, 1, -125, 4, 1, 2, 2, 1, 3, -128, 4, -111, 34, 50, -11, -95, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -126, 0, -125, 0, -90, 120, -96, 64, 48, 62, -128, 1, 1, -127, 1, 3, -126, 4, -111, 34, 50, -11, -125, 1, 0, -92, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -127, 1, 8, -94, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -125, 0, -124, 0, -89, 22, 48, 17, 48, 15, 10, 1, 12, 2, 1, 3, -128, 4, -111, 34, 50, -11, -127, 1, 1, -128, 1, 2, -88, 21, 48, 19, 10, 1, 13, -96, 6, -126, 1, 38, -125, 1, 16, -95, 6, 4, 1, 7, 4, 1, 6, -87, 123, -96, 67, 48, 65, 4, 4, -111, 34, 50, -12, 2, 1, 7, 4, 4, -111, 34, 50, -11, 2, 1, 0, 48, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -127, 1, 2, -94, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -125, 0, -124, 0, -86, 120, -96, 64, 48, 62, -128, 1, 1, -127, 1, 3, -126, 4, -111, 34, 50, -11, -125, 1, 0, -92, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -127, 1, 8, -94, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -125, 0, -124, 0, -85, 13, 48, 11, 10, 1, 1, -96, 6, 10, 1, 0, 10, 1, 2, -82, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -81, 52, -128, 3, 15, 48, 5, -95, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -80, -127, -81, -95, 115, 48, 113, 2, 1, 1, -112, 2, 5, 3, -111, 3, 5, 6, 7, -110, 3, 4, 7, 7, -108, 2, 6, 7, -75, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -128, 2, 1, 7, -127, 2, 6, 5, -126, 2, 1, 8, -125, 2, 2, 6, -124, 1, 2, -123, 9, 48, 12, 17, 17, 119, 22, 62, 34, 12, -122, 2, 6, 5, -121, 3, 4, 6, 5, -120, 1, 0, -119, 1, 2, -94, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -125, 9, 48, 12, 17, 17, 119, 22, 62, 34, 12, -105, 0, -104, 1, 0, -71, 113, 5, 0, -127, 1, 1, -94, 59, 48, 57, -128, 3, 12, 34, 26, -127, 1, 5, -126, 0, -93, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -93, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -107, 0, -74, -126, 7, 41, -96, 6, 4, 4, -111, 34, 50, -11, -95, -126, 5, -128, 48, -126, 1, 92, 4, 1, 0, 4, 1, 15, -128, 1, 3, -95, 110, 48, 108, 48, 53, -128, 4, -111, 34, 34, -8, -95, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -128, 1, 0, -127, 1, 3, -94, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -94, 6, 10, 1, 0, 10, 1, 1, -93, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -92, 110, 48, 108, 48, 53, -128, 4, -111, 34, 34, -8, -95, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -128, 1, 0, -127, 1, 3, -94, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -91, 58, 48, 56, 2, 1, 1, -128, 1, 0, -127, 1, 3, -94, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, 48, -126, 1, 92, 4, 1, 96, 4, 1, 15, -128, 1, 3, -95, 110, 48, 108, 48, 53, -128, 4, -111, 34, 34, -8, -95, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -128, 1, 0, -127, 1, 3, -94, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -94, 6, 10, 1, 0, 10, 1, 1, -93, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -92, 110, 48, 108, 48, 53, -128, 4, -111, 34, 34, -8, -95, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -128, 1, 0, -127, 1, 3, -94, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -91, 58, 48, 56, 2, 1, 1, -128, 1, 0, -127, 1, 3, -94, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, 48, -126, 1, 92, 4, 1, 32, 4, 1, 15, -128, 1, 3, -95, 110, 48, 108, 48, 53, -128, 4, -111, 34, 34, -8, -95, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -128, 1, 0, -127, 1, 3, -94, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -94, 6, 10, 1, 0, 10, 1, 1, -93, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -92, 110, 48, 108, 48, 53, -128, 4, -111, 34, 34, -8, -95, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -128, 1, 0, -127, 1, 3, -94, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -91, 58, 48, 56, 2, 1, 1, -128, 1, 0, -127, 1, 3, -94, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, 48, -126, 1, 92, 4, 1, 16, 4, 1, 15, -128, 1, 3, -95, 110, 48, 108, 48, 53, -128, 4, -111, 34, 34, -8, -95, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -128, 1, 0, -127, 1, 3, -94, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -94, 6, 10, 1, 0, 10, 1, 1, -93, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -92, 110, 48, 108, 48, 53, -128, 4, -111, 34, 34, -8, -95, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -128, 1, 0, -127, 1, 3, -94, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -91, 58, 48, 56, 2, 1, 1, -128, 1, 0, -127, 1, 3, -94, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -94, 55, 48, 53, 4, 1, 0, 4, 1, 15, -96, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -93, -126, 1, 96, 48, -126, 1, 92, 4, 1, 0, 4, 1, 15, -128, 1, 3, -95, 110, 48, 108, 48, 53, -128, 4, -111, 34, 34, -8, -95, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -128, 1, 0, -127, 1, 3, -94, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -94, 6, 10, 1, 0, 10, 1, 1, -93, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -92, 110, 48, 108, 48, 53, -128, 4, -111, 34, 34, -8, -95, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -128, 1, 0, -127, 1, 3, -94, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -91, 58, 48, 56, 2, 1, 1, -128, 1, 0, -127, 1, 3, -94, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -102, 1, 21, -101, 1, 48, -68, 59, -128, 1, 0, -127, 1, 15, -126, 1, 2, -125, 1, 4, -92, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -99, 1, 4, -79, -126, 1, -18, -96, 120, -96, 64, 48, 62, -128, 1, 2, -127, 1, 3, -126, 4, -111, 34, 50, -11, -125, 1, 1, -92, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -127, 1, 8, -94, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -125, 0, -124, 0, -95, 120, -96, 64, 48, 62, -128, 1, 1, -127, 1, 3, -126, 4, -111, 34, 50, -11, -125, 1, 0, -92, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -127, 1, 8, -94, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -125, 0, -124, 0, -94, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -93, 116, -96, 64, 48, 62, -128, 1, 1, -127, 1, 3, -126, 4, -111, 34, 50, -11, -125, 1, 0, -92, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -127, 1, 8, -94, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -92, 13, 48, 11, 10, 1, 1, -96, 6, 10, 1, 0, 10, 1, 2, -91, 68, 48, 6, 4, 1, -125, 4, 1, 2, 2, 1, 3, -128, 4, -111, 34, 50, -11, -95, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -126, 0, -125, 0, -110, 2, 6, 5, -109, 2, 2, 84, -108, 1, -1, -65, 31, -126, 2, -125, -128, 9, 48, 12, 17, 17, 119, 22, 62, 34, 12, -126, 1, 4, -93, 53, -128, 1, 2, -127, 1, 4, -94, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -92, -126, 2, 1, 2, 1, 2, 5, 0, -95, -126, 1, -55, 48, -126, 1, -59, -128, 1, 1, -127, 1, 1, -126, 3, 5, 6, 7, -125, 2, 6, 7, -92, 108, -128, 1, 1, -95, 56, -128, 1, 1, -127, 1, -1, -126, 1, -1, -93, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -94, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -91, 69, -128, 3, 5, 6, 7, -127, 3, 5, 6, 7, -126, 10, 4, 1, 6, 8, 3, 2, 5, 6, 1, 7, -93, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -122, 1, 1, -120, 2, 6, 5, -87, 53, -128, 1, 2, -127, 1, 4, -94, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -86, 124, 48, 122, -128, 2, 6, 7, -95, 69, -128, 3, 5, 6, 7, -127, 3, 5, 6, 7, -126, 10, 4, 1, 6, 8, 3, 2, 5, 6, 1, 7, -93, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -94, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -85, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -116, 3, 5, 6, 7, -115, 9, 48, 12, 17, 17, 119, 22, 62, 34, 12, -114, 1, 0, -113, 1, 2, -94, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -122, 4, -111, 34, 34, -8, -91, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -121, 0, -120, 0, -65, 32, 68, 48, 66, 3, 5, 5, -128, 0, 0, 32, 4, 4, 10, 22, 41, 34, 48, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -96, 4, 4, 2, 6, 7, -97, 33, 0, -97, 34, 4, -111, 34, 34, -8, -97, 35, 9, 41, 42, 43, 44, 45, 46, 47, 48, 49, -97, 36, 1, 2, -97, 37, 0, -97, 38, 1, -1, -97, 39, 1, 2};
    }

    private byte[] getData1() {
        return new byte[] { 48, -126, 4, -124, -128, 5, 17, 17, 33, 34, 34, -127, 4, -111, 34, 50, -12, -126, 1, 5, -125, 1, 1, -92, 3, 4, 1, 38, -90, 3, 4, 1, 16, -89, -127, -124, -96, -127, -127, 4, 1, 0, 48, 77, 48, 75, -126, 1, 38, -124, 1, 15, -123, 4, -111, 34, 34, -8, -120, 2, 2, 5, -122, 1, -92, -121, 1, 2, -87, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -118, 4, -111, 34, 34, -9, -96, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -88, 58, 3, 5, 4, 74, -43, 85, 80, 3, 2, 4, 80, 48, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -119, 0, -86, 4, 4, 2, 0, 2, -85, 62, 48, 60, 4, 3, -1, -1, -1, 5, 0, 48, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -128, 4, -11, -1, -1, -1, -84, 68, 48, 66, 4, 3, -1, -1, -1, 48, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, 3, 2, 5, -96, -128, 2, 7, -128, -127, 4, -11, -1, -1, -1, -83, -126, 3, 18, -96, 23, 48, 18, 48, 16, 10, 1, 4, 2, 1, 3, -128, 5, -111, 17, 34, 51, -13, -127, 1, 1, -128, 1, 2, -95, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -94, 111, 48, 58, 48, 3, 4, 1, 96, 4, 4, -111, 34, 50, -11, -96, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, 48, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -128, 0, -127, 0, -92, 98, 48, 96, 10, 1, 2, -96, 28, -128, 1, 1, -95, 12, 4, 4, -111, 34, 50, -12, 4, 4, -111, 34, 50, -11, -94, 9, 2, 1, 2, 2, 1, 4, 2, 1, 1, -95, 6, -126, 1, 38, -125, 1, 16, -126, 1, 0, -93, 3, 4, 1, 7, -92, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -91, 68, 48, 6, 4, 1, -125, 4, 1, 2, 2, 1, 3, -128, 4, -111, 34, 50, -11, -95, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -126, 0, -125, 0, -90, 120, -96, 64, 48, 62, -128, 1, 1, -127, 1, 3, -126, 4, -111, 34, 50, -11, -125, 1, 0, -92, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -127, 1, 8, -94, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -125, 0, -124, 0, -89, 22, 48, 17, 48, 15, 10, 1, 12, 2, 1, 3, -128, 4, -111, 34, 50, -11, -127, 1, 1, -128, 1, 2, -88, 21, 48, 19, 10, 1, 13, -96, 6, -126, 1, 38, -125, 1, 16, -95, 6, 4, 1, 7, 4, 1, 6, -87, 123, -96, 67, 48, 65, 4, 4, -111, 34, 50, -12, 2, 1, 7, 4, 4, -111, 34, 50, -11, 2, 1, 0, 48, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -127, 1, 2, -94, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -125, 0, -124, 0, -86, 120, -96, 64, 48, 62, -128, 1, 1, -127, 1, 3, -126, 4, -111, 34, 50, -11, -125, 1, 0, -92, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -127, 1, 8, -94, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -125, 0, -124, 0, -85, 13, 48, 11, 10, 1, 1, -96, 6, 10, 1, 0, 10, 1, 2 };
    }

    private byte[] getISDNSubaddressStringData() {
        return new byte[] { 2, 5 };
    }

    public byte[] getNAEACICIData() {
        return new byte[] { 15, 48, 5 };
    };

    public byte[] getAPNOIReplacementData() {
        return new byte[] { 48, 12, 17, 17, 119, 22, 62, 34, 12 };
    };

    public byte[] getPDPTypeData() {
        return new byte[] { 5, 3 };
    };

    public byte[] getPDPAddressData() {
        return new byte[] { 5, 6, 7 };
    };

    public byte[] getPDPAddressData2() {
        return new byte[] { 4, 6, 5 };
    };

    public byte[] getQoSSubscribedData() {
        return new byte[] { 4, 7, 7 };
    };

    public byte[] getAPNData() {
        return new byte[] { 6, 7 };
    };

    public byte[] getExtQoSSubscribedData() {
        return new byte[] { 1, 7 };
    };

    public byte[] getExt2QoSSubscribedData() {
        return new byte[] { 1, 8 };
    };

    public byte[] getExt3QoSSubscribedData() {
        return new byte[] { 2, 6 };
    };

    public byte[] getChargingCharacteristicsData() {
        return new byte[] { 6, 5 };
    };

    public byte[] getExtPDPTypeData() {
        return new byte[] { 6, 5 };
    };

    public byte[] getDataLSAIdentity() {
        return new byte[] { 12, 34, 26 };
    };

    public byte[] getAgeIndicatorData() {
        return new byte[] { 48 };
    };

    public byte[] getFQDNData() {
        return new byte[] { 4, 1, 6, 8, 3, 2, 5, 6, 1, 7 };
    };

    public byte[] getTimeData() {
        return new byte[] { 10, 22, 41, 34 };
    };

    private byte[] getDiameterIdentity() {
        return new byte[] { 41, 42, 43, 44, 45, 46, 47, 48, 49 };
    }

    @Test(groups = { "functional.decode", "service.mobility.subscriberManagement" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(InsertSubscriberDataRequestImpl.class);
    	                
        // MAP Protocol Version 3 message Testing
        byte[] data = this.getData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof InsertSubscriberDataRequestImpl);
        InsertSubscriberDataRequestImpl prim = (InsertSubscriberDataRequestImpl)result.getResult(); 
        
        // imsi
        IMSIImpl imsi = prim.getImsi();
        assertTrue(imsi.getData().equals("1111122222"));

        // msisdn
        ISDNAddressStringImpl msisdn = prim.getMsisdn();
        assertTrue(msisdn.getAddress().equals("22234"));
        assertEquals(msisdn.getAddressNature(), AddressNature.international_number);
        assertEquals(msisdn.getNumberingPlan(), NumberingPlan.ISDN);

        // category
        CategoryImpl category = prim.getCategory();
        assertEquals(category.getData(), 5);

        // subscriberStatus
        SubscriberStatus subscriberStatus = prim.getSubscriberStatus();
        assertEquals(subscriberStatus, SubscriberStatus.operatorDeterminedBarring);

        // bearerServiceList
        List<ExtBearerServiceCodeImpl> bearerServiceList = prim.getBearerServiceList();
        assertNotNull(bearerServiceList);
        assertEquals(bearerServiceList.size(), 1);
        ExtBearerServiceCodeImpl extBearerServiceCode = bearerServiceList.get(0);
        assertEquals(extBearerServiceCode.getBearerServiceCodeValue(), BearerServiceCodeValue.padAccessCA_9600bps);

        // teleserviceList
        List<ExtTeleserviceCodeImpl> teleserviceList = prim.getTeleserviceList();
        assertNotNull(teleserviceList);
        assertEquals(teleserviceList.size(), 1);
        ExtTeleserviceCodeImpl extTeleserviceCode = teleserviceList.get(0);
        assertEquals(extTeleserviceCode.getTeleserviceCodeValue(), TeleserviceCodeValue.allSpeechTransmissionServices);

        // start provisionedSS
        List<ExtSSInfoImpl> provisionedSS = prim.getProvisionedSS();
        assertNotNull(provisionedSS);
        assertEquals(provisionedSS.size(), 1);
        ExtSSInfoImpl extSSInfo = provisionedSS.get(0);

        ExtForwInfoImpl forwardingInfo = extSSInfo.getForwardingInfo();
        ExtCallBarInfoImpl callBarringInfo = extSSInfo.getCallBarringInfo();
        CUGInfoImpl cugInfo = extSSInfo.getCugInfo();
        ExtSSDataImpl ssData = extSSInfo.getSsData();
        EMLPPInfoImpl emlppInfo = extSSInfo.getEmlppInfo();

        assertNotNull(forwardingInfo);
        assertNull(callBarringInfo);
        assertNull(cugInfo);
        assertNull(ssData);
        assertNull(emlppInfo);

        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(forwardingInfo.getExtensionContainer()));
        assertEquals(forwardingInfo.getSsCode().getSupplementaryCodeValue(), SupplementaryCodeValue.allSS);

        List<ExtForwFeatureImpl> forwardingFeatureList = forwardingInfo.getForwardingFeatureList();
        assertNotNull(forwardingFeatureList);
        assertEquals(forwardingFeatureList.size(), 1);
        ExtForwFeatureImpl extForwFeature = forwardingFeatureList.get(0);
        assertNotNull(extForwFeature);

        assertEquals(extForwFeature.getBasicService().getExtBearerService().getBearerServiceCodeValue(), BearerServiceCodeValue.padAccessCA_9600bps);
        assertNull(extForwFeature.getBasicService().getExtTeleservice());
        assertNotNull(extForwFeature.getSsStatus());
        assertTrue(extForwFeature.getSsStatus().getBitA());
        assertTrue(extForwFeature.getSsStatus().getBitP());
        assertTrue(extForwFeature.getSsStatus().getBitQ());
        assertTrue(extForwFeature.getSsStatus().getBitR());

        ISDNAddressStringImpl forwardedToNumber = extForwFeature.getForwardedToNumber();
        assertNotNull(forwardedToNumber);
        assertTrue(forwardedToNumber.getAddress().equals("22228"));
        assertEquals(forwardedToNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(forwardedToNumber.getNumberingPlan(), NumberingPlan.ISDN);

        assertTrue(Arrays.equals(extForwFeature.getForwardedToSubaddress().getData(), this.getISDNSubaddressStringData()));
        assertTrue(extForwFeature.getForwardingOptions().getNotificationToCallingParty());
        assertTrue(extForwFeature.getForwardingOptions().getNotificationToForwardingParty());
        assertTrue(!extForwFeature.getForwardingOptions().getRedirectingPresentation());
        assertEquals(extForwFeature.getForwardingOptions().getExtForwOptionsForwardingReason(), ExtForwOptionsForwardingReason.msBusy);
        assertNotNull(extForwFeature.getNoReplyConditionTime());
        assertEquals(extForwFeature.getNoReplyConditionTime().intValue(), 2);
        FTNAddressStringImpl longForwardedToNumber = extForwFeature.getLongForwardedToNumber();
        assertNotNull(longForwardedToNumber);
        assertTrue(longForwardedToNumber.getAddress().equals("22227"));
        assertEquals(longForwardedToNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(longForwardedToNumber.getNumberingPlan(), NumberingPlan.ISDN);
        // end provisionedSS

        // start odbData
        ODBDataImpl odbData = prim.getODBData();
        ODBGeneralDataImpl oDBGeneralData = odbData.getODBGeneralData();
        assertTrue(!oDBGeneralData.getAllOGCallsBarred());
        assertTrue(oDBGeneralData.getInternationalOGCallsBarred());
        assertTrue(!oDBGeneralData.getInternationalOGCallsNotToHPLMNCountryBarred());
        assertTrue(oDBGeneralData.getInterzonalOGCallsBarred());
        assertTrue(!oDBGeneralData.getInterzonalOGCallsNotToHPLMNCountryBarred());
        assertTrue(oDBGeneralData.getInterzonalOGCallsAndInternationalOGCallsNotToHPLMNCountryBarred());
        assertTrue(!oDBGeneralData.getPremiumRateInformationOGCallsBarred());
        assertTrue(oDBGeneralData.getPremiumRateEntertainementOGCallsBarred());
        assertTrue(!oDBGeneralData.getSsAccessBarred());
        assertTrue(oDBGeneralData.getAllECTBarred());
        assertTrue(!oDBGeneralData.getChargeableECTBarred());
        assertTrue(oDBGeneralData.getInternationalECTBarred());
        assertTrue(!oDBGeneralData.getInterzonalECTBarred());
        assertTrue(oDBGeneralData.getDoublyChargeableECTBarred());
        assertTrue(!oDBGeneralData.getMultipleECTBarred());
        assertTrue(oDBGeneralData.getAllPacketOrientedServicesBarred());
        assertTrue(!oDBGeneralData.getRoamerAccessToHPLMNAPBarred());
        assertTrue(oDBGeneralData.getRoamerAccessToVPLMNAPBarred());
        assertTrue(!oDBGeneralData.getRoamingOutsidePLMNOGCallsBarred());
        assertTrue(oDBGeneralData.getAllICCallsBarred());
        assertTrue(!oDBGeneralData.getRoamingOutsidePLMNICCallsBarred());
        assertTrue(oDBGeneralData.getRoamingOutsidePLMNICountryICCallsBarred());
        assertTrue(!oDBGeneralData.getRoamingOutsidePLMNBarred());
        assertTrue(oDBGeneralData.getRoamingOutsidePLMNCountryBarred());
        assertTrue(!oDBGeneralData.getRegistrationAllCFBarred());
        assertTrue(oDBGeneralData.getRegistrationCFNotToHPLMNBarred());
        assertTrue(!oDBGeneralData.getRegistrationInterzonalCFBarred());
        assertTrue(oDBGeneralData.getRegistrationInterzonalCFNotToHPLMNBarred());
        assertTrue(!oDBGeneralData.getRegistrationInternationalCFBarred());
        ODBHPLMNDataImpl odbHplmnData = odbData.getOdbHplmnData();
        assertTrue(!odbHplmnData.getPlmnSpecificBarringType1());
        assertTrue(odbHplmnData.getPlmnSpecificBarringType2());
        assertTrue(!odbHplmnData.getPlmnSpecificBarringType3());
        assertTrue(odbHplmnData.getPlmnSpecificBarringType4());
        assertNotNull(odbData.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(odbData.getExtensionContainer()));
        // end odbData

        // start roamingRestrictionDueToUnsupportedFeature
        assertTrue(prim.getRoamingRestrictionDueToUnsupportedFeature());

        // start regionalSubscriptionData
        List<ZoneCodeImpl> regionalSubscriptionData = prim.getRegionalSubscriptionData();
        assertNotNull(regionalSubscriptionData);
        assertEquals(regionalSubscriptionData.size(), 1);
        ZoneCodeImpl zoneCode = regionalSubscriptionData.get(0);
        assertEquals(zoneCode.getIntValue(), 2);
        // end regionalSubscriptionData

        // start vbsSubscriptionData
        List<VoiceBroadcastDataImpl> vbsSubscriptionData = prim.getVbsSubscriptionData();
        assertNotNull(vbsSubscriptionData);
        assertEquals(vbsSubscriptionData.size(), 1);
        VoiceBroadcastDataImpl voiceBroadcastData = vbsSubscriptionData.get(0);
        assertTrue(voiceBroadcastData.getGroupId().getGroupId().equals(""));
        assertTrue(voiceBroadcastData.getLongGroupId().getLongGroupId().equals("5"));
        assertTrue(voiceBroadcastData.getBroadcastInitEntitlement());
        assertNotNull(voiceBroadcastData.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(voiceBroadcastData.getExtensionContainer()));
        // end vbsSubscriptionData

        // start vgcsSubscriptionData
        List<VoiceGroupCallDataImpl> vgcsSubscriptionData = prim.getVgcsSubscriptionData();
        assertNotNull(vgcsSubscriptionData);
        assertEquals(vgcsSubscriptionData.size(), 1);
        VoiceGroupCallDataImpl voiceGroupCallData = vgcsSubscriptionData.get(0);
        assertTrue(voiceGroupCallData.getGroupId().getGroupId().equals(""));
        assertTrue(voiceGroupCallData.getLongGroupId().getLongGroupId().equals("5"));
        assertNotNull(voiceGroupCallData.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(voiceGroupCallData.getExtensionContainer()));
        assertTrue(voiceGroupCallData.getAdditionalSubscriptions().getEmergencyReset());
        assertFalse(voiceGroupCallData.getAdditionalSubscriptions().getEmergencyUplinkRequest());
        assertTrue(voiceGroupCallData.getAdditionalSubscriptions().getPrivilegedUplinkRequest());
        assertNotNull(voiceGroupCallData.getAdditionalInfo());
        assertTrue(voiceGroupCallData.getAdditionalInfo().isBitSet(0));
        // end vgcsSubscriptionData

        // start vlrCamelSubscriptionInfo
        VlrCamelSubscriptionInfoImpl vlrCamelSubscriptionInfo = prim.getVlrCamelSubscriptionInfo();

        OCSIImpl oCsi = vlrCamelSubscriptionInfo.getOCsi();
        List<OBcsmCamelTDPDataImpl> lst = oCsi.getOBcsmCamelTDPDataList();
        assertEquals(lst.size(), 1);
        OBcsmCamelTDPDataImpl cd = lst.get(0);
        assertEquals(cd.getOBcsmTriggerDetectionPoint(), OBcsmTriggerDetectionPoint.routeSelectFailure);
        assertEquals(cd.getServiceKey(), 3);
        assertEquals(cd.getGsmSCFAddress().getAddressNature(), AddressNature.international_number);
        assertEquals(cd.getGsmSCFAddress().getNumberingPlan(), NumberingPlan.ISDN);
        assertTrue(cd.getGsmSCFAddress().getAddress().equals("1122333"));
        assertEquals(cd.getDefaultCallHandling(), DefaultCallHandling.releaseCall);
        assertNull(cd.getExtensionContainer());

        assertNull(oCsi.getExtensionContainer());
        assertEquals((int) oCsi.getCamelCapabilityHandling(), 2);
        assertFalse(oCsi.getNotificationToCSE());
        assertFalse(oCsi.getCsiActive());

        assertNotNull(vlrCamelSubscriptionInfo.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(vlrCamelSubscriptionInfo.getExtensionContainer()));

        SSCSIImpl ssCsi = vlrCamelSubscriptionInfo.getSsCsi();
        SSCamelDataImpl ssCamelData = ssCsi.getSsCamelData();

        List<SSCodeImpl> ssEventList = ssCamelData.getSsEventList();
        assertNotNull(ssEventList);
        assertEquals(ssEventList.size(), 1);
        SSCodeImpl one = ssEventList.get(0);
        assertNotNull(one);
        assertEquals(one.getSupplementaryCodeValue(), SupplementaryCodeValue.allCommunityOfInterestSS);
        ISDNAddressStringImpl gsmSCFAddress = ssCamelData.getGsmSCFAddress();
        assertTrue(gsmSCFAddress.getAddress().equals("22235"));
        assertEquals(gsmSCFAddress.getAddressNature(), AddressNature.international_number);
        assertEquals(gsmSCFAddress.getNumberingPlan(), NumberingPlan.ISDN);
        assertNotNull(ssCamelData.getExtensionContainer());
        assertNotNull(ssCsi.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(ssCsi.getExtensionContainer()));
        assertTrue(ssCsi.getCsiActive());
        assertTrue(ssCsi.getNotificationToCSE());

        List<OBcsmCamelTdpCriteriaImpl> oBcsmCamelTDPCriteriaList = vlrCamelSubscriptionInfo.getOBcsmCamelTDPCriteriaList();
        assertNotNull(oBcsmCamelTDPCriteriaList);
        assertEquals(oBcsmCamelTDPCriteriaList.size(), 1);
        OBcsmCamelTdpCriteriaImpl oBcsmCamelTdpCriteria = oBcsmCamelTDPCriteriaList.get(0);
        assertNotNull(oBcsmCamelTdpCriteria);

        DestinationNumberCriteriaImpl destinationNumberCriteria = oBcsmCamelTdpCriteria.getDestinationNumberCriteria();
        List<ISDNAddressStringImpl> destinationNumberList = destinationNumberCriteria.getDestinationNumberList();
        assertNotNull(destinationNumberList);
        assertEquals(destinationNumberList.size(), 2);
        ISDNAddressStringImpl destinationNumberOne = destinationNumberList.get(0);
        assertNotNull(destinationNumberOne);
        assertTrue(destinationNumberOne.getAddress().equals("22234"));
        assertEquals(destinationNumberOne.getAddressNature(), AddressNature.international_number);
        assertEquals(destinationNumberOne.getNumberingPlan(), NumberingPlan.ISDN);
        ISDNAddressStringImpl destinationNumberTwo = destinationNumberList.get(1);
        assertNotNull(destinationNumberTwo);
        assertTrue(destinationNumberTwo.getAddress().equals("22235"));
        assertEquals(destinationNumberTwo.getAddressNature(), AddressNature.international_number);
        assertEquals(destinationNumberTwo.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(destinationNumberCriteria.getMatchType().getCode(), MatchType.enabling.getCode());
        List<Integer> destinationNumberLengthList = destinationNumberCriteria.getDestinationNumberLengthList();
        assertNotNull(destinationNumberLengthList);
        assertEquals(destinationNumberLengthList.size(), 3);
        assertEquals(destinationNumberLengthList.get(0).intValue(), 2);
        assertEquals(destinationNumberLengthList.get(1).intValue(), 4);
        assertEquals(destinationNumberLengthList.get(2).intValue(), 1);
        assertEquals(oBcsmCamelTdpCriteria.getOBcsmTriggerDetectionPoint(), OBcsmTriggerDetectionPoint.collectedInfo);
        assertNotNull(oBcsmCamelTdpCriteria.getBasicServiceCriteria());
        assertEquals(oBcsmCamelTdpCriteria.getBasicServiceCriteria().size(), 2);
        ExtBasicServiceCodeImpl basicServiceOne = oBcsmCamelTdpCriteria.getBasicServiceCriteria().get(0);
        assertNotNull(basicServiceOne);
        assertEquals(basicServiceOne.getExtBearerService().getBearerServiceCodeValue(), BearerServiceCodeValue.padAccessCA_9600bps);

        ExtBasicServiceCodeImpl basicServiceTwo = oBcsmCamelTdpCriteria.getBasicServiceCriteria().get(1);
        assertNotNull(basicServiceTwo);
        assertEquals(basicServiceTwo.getExtTeleservice().getTeleserviceCodeValue(), TeleserviceCodeValue.allSpeechTransmissionServices);

        assertEquals(oBcsmCamelTdpCriteria.getCallTypeCriteria(), CallTypeCriteria.forwarded);
        List<CauseValueImpl> oCauseValueCriteria = oBcsmCamelTdpCriteria.getOCauseValueCriteria();
        assertNotNull(oCauseValueCriteria);
        assertEquals(oCauseValueCriteria.size(), 1);
        assertNotNull(oCauseValueCriteria.get(0));
        assertEquals(oCauseValueCriteria.get(0).getData(), 7);

        assertFalse(vlrCamelSubscriptionInfo.getTifCsi());

        MCSIImpl mCsi = vlrCamelSubscriptionInfo.getMCsi();
        List<MMCodeImpl> mobilityTriggers = mCsi.getMobilityTriggers();
        assertNotNull(mobilityTriggers);
        assertEquals(mobilityTriggers.size(), 2);
        MMCodeImpl mmCode = mobilityTriggers.get(0);
        assertNotNull(mmCode);
        assertEquals(MMCodeValue.GPRSAttach, mmCode.getMMCodeValue());
        MMCodeImpl mmCode2 = mobilityTriggers.get(1);
        assertNotNull(mmCode2);
        assertEquals(MMCodeValue.IMSIAttach, mmCode2.getMMCodeValue());
        assertNotNull(mCsi.getServiceKey());
        assertEquals(mCsi.getServiceKey(), 3);
        ISDNAddressStringImpl gsmSCFAddressTwo = mCsi.getGsmSCFAddress();
        assertTrue(gsmSCFAddressTwo.getAddress().equals("22235"));
        assertEquals(gsmSCFAddressTwo.getAddressNature(), AddressNature.international_number);
        assertEquals(gsmSCFAddressTwo.getNumberingPlan(), NumberingPlan.ISDN);
        assertNotNull(mCsi.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(mCsi.getExtensionContainer()));
        assertTrue(mCsi.getCsiActive());
        assertTrue(mCsi.getNotificationToCSE());

        SMSCSIImpl smsCsi = vlrCamelSubscriptionInfo.getSmsCsi();
        List<SMSCAMELTDPDataImpl> smsCamelTdpDataList = smsCsi.getSmsCamelTdpDataList();
        assertNotNull(smsCamelTdpDataList);
        assertEquals(smsCamelTdpDataList.size(), 1);
        SMSCAMELTDPDataImpl smsCAMELTDPData = smsCamelTdpDataList.get(0);
        assertNotNull(smsCAMELTDPData);
        assertEquals(smsCAMELTDPData.getServiceKey(), 3);
        assertEquals(smsCAMELTDPData.getSMSTriggerDetectionPoint(), SMSTriggerDetectionPoint.smsCollectedInfo);
        ISDNAddressStringImpl gsmSCFAddressSmsCAMELTDPData = smsCAMELTDPData.getGsmSCFAddress();
        assertTrue(gsmSCFAddressSmsCAMELTDPData.getAddress().equals("22235"));
        assertEquals(gsmSCFAddressSmsCAMELTDPData.getAddressNature(), AddressNature.international_number);
        assertEquals(gsmSCFAddressSmsCAMELTDPData.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(smsCAMELTDPData.getDefaultSMSHandling(), DefaultSMSHandling.continueTransaction);
        assertNotNull(smsCAMELTDPData.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(smsCAMELTDPData.getExtensionContainer()));
        assertTrue(smsCsi.getCsiActive());
        assertTrue(smsCsi.getNotificationToCSE());
        assertEquals(smsCsi.getCamelCapabilityHandling().intValue(), 8);

        TCSIImpl vtCsi = vlrCamelSubscriptionInfo.getVtCsi();
        List<TBcsmCamelTDPDataImpl> tbcsmCamelTDPDatalst = vtCsi.getTBcsmCamelTDPDataList();
        assertEquals(lst.size(), 1);
        TBcsmCamelTDPDataImpl tbcsmCamelTDPData = tbcsmCamelTDPDatalst.get(0);
        assertEquals(tbcsmCamelTDPData.getTBcsmTriggerDetectionPoint(), TBcsmTriggerDetectionPoint.termAttemptAuthorized);
        assertEquals(tbcsmCamelTDPData.getServiceKey(), 3);
        assertEquals(tbcsmCamelTDPData.getGsmSCFAddress().getAddressNature(), AddressNature.international_number);
        assertEquals(tbcsmCamelTDPData.getGsmSCFAddress().getNumberingPlan(), NumberingPlan.ISDN);
        assertTrue(tbcsmCamelTDPData.getGsmSCFAddress().getAddress().equals("22235"));
        assertEquals(tbcsmCamelTDPData.getDefaultCallHandling(), DefaultCallHandling.releaseCall);
        assertNull(tbcsmCamelTDPData.getExtensionContainer());
        assertNull(vtCsi.getExtensionContainer());
        assertEquals((int) vtCsi.getCamelCapabilityHandling(), 2);
        assertFalse(vtCsi.getNotificationToCSE());
        assertFalse(vtCsi.getCsiActive());

        List<TBcsmCamelTdpCriteriaImpl> tBcsmCamelTdpCriteriaList = vlrCamelSubscriptionInfo.getTBcsmCamelTdpCriteriaList();
        assertNotNull(tBcsmCamelTdpCriteriaList);
        assertEquals(tBcsmCamelTdpCriteriaList.size(), 1);
        assertNotNull(tBcsmCamelTdpCriteriaList.get(0));
        TBcsmCamelTdpCriteriaImpl tbcsmCamelTdpCriteria = tBcsmCamelTdpCriteriaList.get(0);
        assertEquals(tbcsmCamelTdpCriteria.getTBcsmTriggerDetectionPoint(), TBcsmTriggerDetectionPoint.tBusy);
        assertNotNull(tbcsmCamelTdpCriteria.getBasicServiceCriteria());
        assertEquals(tbcsmCamelTdpCriteria.getBasicServiceCriteria().size(), 2);
        assertNotNull(tbcsmCamelTdpCriteria.getBasicServiceCriteria().get(0));
        assertNotNull(tbcsmCamelTdpCriteria.getBasicServiceCriteria().get(1));
        List<CauseValueImpl> oCauseValueCriteriaLst = tbcsmCamelTdpCriteria.getTCauseValueCriteria();
        assertNotNull(oCauseValueCriteriaLst);
        assertEquals(oCauseValueCriteriaLst.size(), 2);
        assertNotNull(oCauseValueCriteriaLst.get(0));
        assertEquals(oCauseValueCriteriaLst.get(0).getData(), 7);
        assertNotNull(oCauseValueCriteriaLst.get(1));
        assertEquals(oCauseValueCriteriaLst.get(1).getData(), 6);

        DCSIImpl dCsi = vlrCamelSubscriptionInfo.getDCsi();
        List<DPAnalysedInfoCriteriumImpl> dpAnalysedInfoCriteriaList = dCsi.getDPAnalysedInfoCriteriaList();
        assertNotNull(dpAnalysedInfoCriteriaList);
        assertEquals(dpAnalysedInfoCriteriaList.size(), 1);
        DPAnalysedInfoCriteriumImpl dpAnalysedInfoCriterium = dpAnalysedInfoCriteriaList.get(0);
        assertNotNull(dpAnalysedInfoCriterium);
        ISDNAddressStringImpl dialledNumber = dpAnalysedInfoCriterium.getDialledNumber();
        assertTrue(dialledNumber.getAddress().equals("22234"));
        assertEquals(dialledNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(dialledNumber.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(dpAnalysedInfoCriterium.getServiceKey(), 7);
        ISDNAddressStringImpl gsmSCFAddressDp = dpAnalysedInfoCriterium.getGsmSCFAddress();
        assertTrue(gsmSCFAddressDp.getAddress().equals("22235"));
        assertEquals(gsmSCFAddressDp.getAddressNature(), AddressNature.international_number);
        assertEquals(gsmSCFAddressDp.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(dpAnalysedInfoCriterium.getDefaultCallHandling(), DefaultCallHandling.continueCall);
        assertNotNull(dCsi.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(dCsi.getExtensionContainer()));
        assertEquals(dCsi.getCamelCapabilityHandling().intValue(), 2);
        assertTrue(dCsi.getCsiActive());
        assertTrue(dCsi.getNotificationToCSE());

        SMSCSIImpl mtSmsCSI = vlrCamelSubscriptionInfo.getMtSmsCSI();
        List<SMSCAMELTDPDataImpl> smsCamelTdpDataListOfmtSmsCSI = mtSmsCSI.getSmsCamelTdpDataList();
        assertNotNull(smsCamelTdpDataListOfmtSmsCSI);
        assertEquals(smsCamelTdpDataListOfmtSmsCSI.size(), 1);
        SMSCAMELTDPDataImpl smsCAMELTDPDataOfMtSmsCSI = smsCamelTdpDataListOfmtSmsCSI.get(0);
        assertNotNull(smsCAMELTDPDataOfMtSmsCSI);
        assertEquals(smsCAMELTDPDataOfMtSmsCSI.getServiceKey(), 3);
        assertEquals(smsCAMELTDPDataOfMtSmsCSI.getSMSTriggerDetectionPoint(), SMSTriggerDetectionPoint.smsCollectedInfo);
        ISDNAddressStringImpl gsmSCFAddressOfMtSmsCSI = smsCAMELTDPDataOfMtSmsCSI.getGsmSCFAddress();
        assertTrue(gsmSCFAddressOfMtSmsCSI.getAddress().equals("22235"));
        assertEquals(gsmSCFAddressOfMtSmsCSI.getAddressNature(), AddressNature.international_number);
        assertEquals(gsmSCFAddressOfMtSmsCSI.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(smsCAMELTDPDataOfMtSmsCSI.getDefaultSMSHandling(), DefaultSMSHandling.continueTransaction);
        assertNotNull(smsCAMELTDPDataOfMtSmsCSI.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(smsCAMELTDPDataOfMtSmsCSI.getExtensionContainer()));
        assertNotNull(mtSmsCSI.getExtensionContainer());
        assertTrue(mtSmsCSI.getCsiActive());
        assertTrue(mtSmsCSI.getNotificationToCSE());
        assertEquals(mtSmsCSI.getCamelCapabilityHandling().intValue(), 8);

        List<MTsmsCAMELTDPCriteriaImpl> mtSmsCamelTdpCriteriaList = vlrCamelSubscriptionInfo.getMtSmsCamelTdpCriteriaList();
        assertNotNull(mtSmsCamelTdpCriteriaList);
        assertEquals(mtSmsCamelTdpCriteriaList.size(), 1);
        MTsmsCAMELTDPCriteriaImpl mtsmsCAMELTDPCriteria = mtSmsCamelTdpCriteriaList.get(0);

        List<MTSMSTPDUType> tPDUTypeCriterion = mtsmsCAMELTDPCriteria.getTPDUTypeCriterion();
        assertNotNull(tPDUTypeCriterion);
        assertEquals(tPDUTypeCriterion.size(), 2);
        MTSMSTPDUType mtSMSTPDUTypeOne = tPDUTypeCriterion.get(0);
        assertNotNull(mtSMSTPDUTypeOne);
        assertEquals(mtSMSTPDUTypeOne, MTSMSTPDUType.smsDELIVER);

        MTSMSTPDUType mtSMSTPDUTypeTwo = tPDUTypeCriterion.get(1);
        assertNotNull(mtSMSTPDUTypeTwo);
        assertTrue(mtSMSTPDUTypeTwo == MTSMSTPDUType.smsSTATUSREPORT);
        assertEquals(mtsmsCAMELTDPCriteria.getSMSTriggerDetectionPoint(), SMSTriggerDetectionPoint.smsCollectedInfo);
        // end vlrCamelSubscriptionInfo

        // start naeaPreferredCI
        NAEAPreferredCIImpl naeaPreferredCI = prim.getNAEAPreferredCI();
        assertEquals(naeaPreferredCI.getNaeaPreferredCIC().getData(), this.getNAEACICIData());
        assertNotNull(naeaPreferredCI.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(naeaPreferredCI.getExtensionContainer()));
        // end naeaPreferredCI

        // start gprsSubscriptionData
        GPRSSubscriptionDataImpl gprsSubscriptionData = prim.getGPRSSubscriptionData();

        assertTrue(!gprsSubscriptionData.getCompleteDataListIncluded());
        List<PDPContextImpl> gprsDataList = gprsSubscriptionData.getGPRSDataList();
        assertNotNull(gprsDataList);
        assertEquals(gprsDataList.size(), 1);
        PDPContextImpl pdpContext = gprsDataList.get(0);
        assertNotNull(pdpContext);
        APNImpl apn = pdpContext.getAPN();
        assertTrue(Arrays.equals(apn.getData(), this.getAPNData()));
        APNOIReplacementImpl apnoiReplacement = pdpContext.getAPNOIReplacement();
        assertTrue(Arrays.equals(apnoiReplacement.getData(), this.getAPNOIReplacementData()));
        ChargingCharacteristicsImpl chargingCharacteristics = pdpContext.getChargingCharacteristics();
        assertTrue(Arrays.equals(chargingCharacteristics.getData(), this.getChargingCharacteristicsData()));

        Ext2QoSSubscribedImpl ext2QoSSubscribed = pdpContext.getExt2QoSSubscribed();
        assertTrue(Arrays.equals(ext2QoSSubscribed.getData(), this.getExt2QoSSubscribedData()));
        Ext3QoSSubscribedImpl ext3QoSSubscribed = pdpContext.getExt3QoSSubscribed();
        assertTrue(Arrays.equals(ext3QoSSubscribed.getData(), this.getExt3QoSSubscribedData()));
        Ext4QoSSubscribedImpl ext4QoSSubscribed = pdpContext.getExt4QoSSubscribed();
        assertEquals(ext4QoSSubscribed.getData(), 2);
        MAPExtensionContainerImpl pdpContextExtensionContainer = pdpContext.getExtensionContainer();
        assertNotNull(pdpContextExtensionContainer);
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(pdpContextExtensionContainer));
        PDPAddressImpl extpdpAddress = pdpContext.getExtPDPAddress();
        assertTrue(Arrays.equals(extpdpAddress.getData(), this.getPDPAddressData2()));
        ExtPDPTypeImpl extpdpType = pdpContext.getExtPDPType();
        assertTrue(Arrays.equals(extpdpType.getData(), this.getExtPDPTypeData()));
        ExtQoSSubscribedImpl extQoSSubscribed = pdpContext.getExtQoSSubscribed();
        assertTrue(Arrays.equals(extQoSSubscribed.getData(), this.getExtQoSSubscribedData()));
        assertEquals(pdpContext.getLIPAPermission(), LIPAPermission.lipaConditional);
        PDPAddressImpl pdpAddress = pdpContext.getPDPAddress();
        assertTrue(Arrays.equals(pdpAddress.getData(), this.getPDPAddressData()));
        assertEquals(pdpContext.getPDPContextId(), 1);
        PDPTypeImpl pdpType = pdpContext.getPDPType();
        assertTrue(Arrays.equals(pdpType.getData(), this.getPDPTypeData()));
        QoSSubscribedImpl qosSubscribed = pdpContext.getQoSSubscribed();
        assertTrue(Arrays.equals(qosSubscribed.getData(), this.getQoSSubscribedData()));
        assertEquals(pdpContext.getSIPTOPermission(), SIPTOPermission.siptoAllowed);

        APNOIReplacementImpl apnOiReplacement = gprsSubscriptionData.getApnOiReplacement();
        assertNotNull(apnOiReplacement);
        assertTrue(Arrays.equals(apnOiReplacement.getData(), this.getAPNOIReplacementData()));
        assertNotNull(gprsSubscriptionData.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(gprsSubscriptionData.getExtensionContainer()));
        // end gprsSubscriptionData

        // RoamingRestrictedInSgsnDueToUnsupportedFeature
        assertTrue(prim.getRoamingRestrictedInSgsnDueToUnsupportedFeature());

        // NetworkAccessMode
        assertEquals(prim.getNetworkAccessMode(), NetworkAccessMode.packetAndCircuit);

        // start lsaInformation
        LSAInformationImpl lsaInformation = prim.getLSAInformation();
        assertTrue(lsaInformation.getCompleteDataListIncluded());
        assertEquals(lsaInformation.getLSAOnlyAccessIndicator(), LSAOnlyAccessIndicator.accessOutsideLSAsRestricted);
        List<LSADataImpl> lsaDataList = lsaInformation.getLSADataList();
        assertNotNull(lsaDataList);
        assertEquals(lsaDataList.size(), 1);
        LSADataImpl lsaData = lsaDataList.get(0);
        assertTrue(Arrays.equals(lsaData.getLSAIdentity().getData(), this.getDataLSAIdentity()));
        assertEquals(lsaData.getLSAAttributes().getData(), 5);
        assertTrue(lsaData.getLsaActiveModeIndicator());
        assertNotNull(lsaData.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(lsaData.getExtensionContainer()));
        assertNotNull(lsaInformation.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(lsaInformation.getExtensionContainer()));
        // end lsaInformation

        // LmuIndicator
        assertTrue(prim.getLmuIndicator());

        // lcsPrivacyClass
        LCSInformationImpl lcsInformation = prim.getLCSInformation();
        List<ISDNAddressStringImpl> gmlcList = lcsInformation.getGmlcList();
        assertNotNull(gmlcList);
        assertEquals(gmlcList.size(), 1);
        ISDNAddressStringImpl isdnAddressString = gmlcList.get(0);
        assertTrue(isdnAddressString.getAddress().equals("22235"));
        assertEquals(isdnAddressString.getAddressNature(), AddressNature.international_number);
        assertEquals(isdnAddressString.getNumberingPlan(), NumberingPlan.ISDN);
        List<LCSPrivacyClassImpl> lcsPrivacyExceptionList = lcsInformation.getLcsPrivacyExceptionList();
        assertNotNull(lcsPrivacyExceptionList);
        assertEquals(lcsPrivacyExceptionList.size(), 4);
        LCSPrivacyClassImpl lcsPrivacyClass = lcsPrivacyExceptionList.get(0);
        assertEquals(lcsPrivacyClass.getSsCode().getSupplementaryCodeValue(), SupplementaryCodeValue.allSS);
        ExtSSStatusImpl ssStatus = lcsPrivacyClass.getSsStatus();
        assertTrue(ssStatus.getBitA());
        assertTrue(ssStatus.getBitP());
        assertTrue(ssStatus.getBitQ());
        assertTrue(ssStatus.getBitR());

        assertEquals(lcsPrivacyClass.getNotificationToMSUser(), NotificationToMSUser.locationNotAllowed);

        List<ExternalClientImpl> externalClientList = lcsPrivacyClass.getExternalClientList();
        assertNotNull(externalClientList);
        assertEquals(externalClientList.size(), 1);
        ExternalClientImpl externalClient = externalClientList.get(0);
        MAPExtensionContainerImpl extensionContainerExternalClient = externalClient.getExtensionContainer();
        LCSClientExternalIDImpl clientIdentity = externalClient.getClientIdentity();
        ISDNAddressStringImpl externalAddress = clientIdentity.getExternalAddress();
        assertTrue(externalAddress.getAddress().equals("22228"));
        assertEquals(externalAddress.getAddressNature(), AddressNature.international_number);
        assertEquals(externalAddress.getNumberingPlan(), NumberingPlan.ISDN);
        assertNotNull(clientIdentity.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(clientIdentity.getExtensionContainer()));
        assertEquals(externalClient.getGMLCRestriction(), GMLCRestriction.gmlcList);
        assertEquals(externalClient.getNotificationToMSUser(), NotificationToMSUser.locationNotAllowed);
        assertNotNull(extensionContainerExternalClient);
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainerExternalClient));

        List<LCSClientInternalID> plmnClientList = lcsPrivacyClass.getPLMNClientList();
        assertNotNull(plmnClientList);
        assertEquals(plmnClientList.size(), 2);
        assertEquals(plmnClientList.get(0), LCSClientInternalID.broadcastService);
        assertEquals(plmnClientList.get(1), LCSClientInternalID.oandMHPLMN);

        List<ExternalClientImpl> extExternalClientList = lcsPrivacyClass.getExtExternalClientList();
        assertNotNull(extExternalClientList);
        assertEquals(extExternalClientList.size(), 1);
        externalClient = extExternalClientList.get(0);
        clientIdentity = externalClient.getClientIdentity();
        externalAddress = clientIdentity.getExternalAddress();
        assertTrue(externalAddress.getAddress().equals("22228"));
        assertEquals(externalAddress.getAddressNature(), AddressNature.international_number);
        assertEquals(externalAddress.getNumberingPlan(), NumberingPlan.ISDN);
        assertNotNull(clientIdentity.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(clientIdentity.getExtensionContainer()));
        assertEquals(externalClient.getGMLCRestriction(), GMLCRestriction.gmlcList);
        assertEquals(externalClient.getNotificationToMSUser(), NotificationToMSUser.locationNotAllowed);
        assertNotNull(externalClient.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(externalClient.getExtensionContainer()));

        List<ServiceTypeImpl> serviceTypeList = lcsPrivacyClass.getServiceTypeList();
        assertNotNull(serviceTypeList);
        assertEquals(serviceTypeList.size(), 1);
        ServiceTypeImpl serviceType = serviceTypeList.get(0);
        assertEquals(serviceType.getServiceTypeIdentity(), 1);
        assertEquals(serviceType.getGMLCRestriction(), GMLCRestriction.gmlcList);
        assertEquals(serviceType.getNotificationToMSUser(), NotificationToMSUser.locationNotAllowed);
        assertNotNull(serviceType.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(serviceType.getExtensionContainer()));

        assertNotNull(lcsPrivacyClass.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(lcsPrivacyClass.getExtensionContainer()));
        List<MOLRClassImpl> molrList = lcsInformation.getMOLRList();
        assertNotNull(molrList);
        assertEquals(molrList.size(), 1);
        MOLRClassImpl molrClass = molrList.get(0);
        assertEquals(molrClass.getSsCode().getSupplementaryCodeValue(), SupplementaryCodeValue.allSS);
        assertTrue(molrClass.getSsStatus().getBitA());
        assertTrue(molrClass.getSsStatus().getBitP());
        assertTrue(molrClass.getSsStatus().getBitQ());
        assertTrue(molrClass.getSsStatus().getBitR());
        assertNotNull(molrClass.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(molrClass.getExtensionContainer()));

        List<LCSPrivacyClassImpl> addLcsPrivacyExceptionList = lcsInformation.getAddLcsPrivacyExceptionList();
        assertNotNull(addLcsPrivacyExceptionList);
        assertEquals(addLcsPrivacyExceptionList.size(), 1);
        lcsPrivacyClass = addLcsPrivacyExceptionList.get(0);
        assertEquals(lcsPrivacyClass.getSsCode().getSupplementaryCodeValue(), SupplementaryCodeValue.allSS);
        ssStatus = lcsPrivacyClass.getSsStatus();
        assertTrue(ssStatus.getBitA());
        assertTrue(ssStatus.getBitP());
        assertTrue(ssStatus.getBitQ());
        assertTrue(ssStatus.getBitR());

        assertEquals(lcsPrivacyClass.getNotificationToMSUser(), NotificationToMSUser.locationNotAllowed);

        externalClientList = lcsPrivacyClass.getExternalClientList();
        assertNotNull(externalClientList);
        assertEquals(externalClientList.size(), 1);
        externalClient = externalClientList.get(0);
        extensionContainerExternalClient = externalClient.getExtensionContainer();
        clientIdentity = externalClient.getClientIdentity();
        externalAddress = clientIdentity.getExternalAddress();
        assertTrue(externalAddress.getAddress().equals("22228"));
        assertEquals(externalAddress.getAddressNature(), AddressNature.international_number);
        assertEquals(externalAddress.getNumberingPlan(), NumberingPlan.ISDN);
        assertNotNull(clientIdentity.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(clientIdentity.getExtensionContainer()));
        assertEquals(externalClient.getGMLCRestriction(), GMLCRestriction.gmlcList);
        assertEquals(externalClient.getNotificationToMSUser(), NotificationToMSUser.locationNotAllowed);
        assertNotNull(extensionContainerExternalClient);
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainerExternalClient));

        plmnClientList = lcsPrivacyClass.getPLMNClientList();
        assertNotNull(plmnClientList);
        assertEquals(plmnClientList.size(), 2);
        assertEquals(plmnClientList.get(0), LCSClientInternalID.broadcastService);
        assertEquals(plmnClientList.get(1), LCSClientInternalID.oandMHPLMN);

        extExternalClientList = lcsPrivacyClass.getExtExternalClientList();
        assertNotNull(extExternalClientList);
        assertEquals(extExternalClientList.size(), 1);
        externalClient = extExternalClientList.get(0);
        clientIdentity = externalClient.getClientIdentity();
        externalAddress = clientIdentity.getExternalAddress();
        assertTrue(externalAddress.getAddress().equals("22228"));
        assertEquals(externalAddress.getAddressNature(), AddressNature.international_number);
        assertEquals(externalAddress.getNumberingPlan(), NumberingPlan.ISDN);
        assertNotNull(clientIdentity.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(clientIdentity.getExtensionContainer()));
        assertEquals(externalClient.getGMLCRestriction(), GMLCRestriction.gmlcList);
        assertEquals(externalClient.getNotificationToMSUser(), NotificationToMSUser.locationNotAllowed);
        assertNotNull(externalClient.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(externalClient.getExtensionContainer()));

        serviceTypeList = lcsPrivacyClass.getServiceTypeList();
        assertNotNull(serviceTypeList);
        assertEquals(serviceTypeList.size(), 1);
        serviceType = serviceTypeList.get(0);
        assertEquals(serviceType.getServiceTypeIdentity(), 1);
        assertEquals(serviceType.getGMLCRestriction(), GMLCRestriction.gmlcList);
        assertEquals(serviceType.getNotificationToMSUser(), NotificationToMSUser.locationNotAllowed);
        assertNotNull(serviceType.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(serviceType.getExtensionContainer()));

        assertNotNull(lcsPrivacyClass.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(lcsPrivacyClass.getExtensionContainer()));
        // end lcsPrivacyClass

        // istAlertTimer
        assertTrue(prim.getIstAlertTimer().equals(Integer.valueOf(21)));

        // superChargerSupportedInHLR
        AgeIndicatorImpl superChargerSupportedInHLR = prim.getSuperChargerSupportedInHLR();
        assertEquals(superChargerSupportedInHLR.getData(), this.getAgeIndicatorData());

        // start mcSsInfo
        MCSSInfoImpl mcSsInfo = prim.getMcSsInfo();
        assertEquals(mcSsInfo.getSSCode().getSupplementaryCodeValue(), SupplementaryCodeValue.allSS);
        ExtSSStatusImpl ssStatusMcSsInfo = mcSsInfo.getSSStatus();
        assertTrue(ssStatusMcSsInfo.getBitA());
        assertTrue(ssStatusMcSsInfo.getBitP());
        assertTrue(ssStatusMcSsInfo.getBitQ());
        assertTrue(ssStatusMcSsInfo.getBitR());

        assertEquals(mcSsInfo.getNbrSB(), 2);
        assertEquals(mcSsInfo.getNbrUser(), 4);
        assertNotNull(mcSsInfo.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(mcSsInfo.getExtensionContainer()));
        // end mcSsInfo

        // csAllocationRetentionPriority
        CSAllocationRetentionPriorityImpl csAllocationRetentionPriority = prim.getCSAllocationRetentionPriority();
        assertEquals(csAllocationRetentionPriority.getData(), 4);

        // end sgsnCamelSubscriptionInfo
        SGSNCAMELSubscriptionInfoImpl sgsnCamelSubscriptionInfo = prim.getSgsnCamelSubscriptionInfo();

        // sgsnCamelSubscriptionInfo
        GPRSCSIImpl gprsCsi = sgsnCamelSubscriptionInfo.getGprsCsi();
        assertNotNull(gprsCsi.getGPRSCamelTDPDataList());
        assertEquals(gprsCsi.getGPRSCamelTDPDataList().size(), 1);
        GPRSCamelTDPDataImpl gprsCamelTDPData = gprsCsi.getGPRSCamelTDPDataList().get(0);

        MAPExtensionContainerImpl extensionContainergprsCamelTDPData = gprsCamelTDPData.getExtensionContainer();
        ISDNAddressStringImpl gsmSCFAddressSgsnCamelSubscriptionInfo = gprsCamelTDPData.getGsmSCFAddress();
        assertTrue(gsmSCFAddressSgsnCamelSubscriptionInfo.getAddress().equals("22235"));
        assertEquals(gsmSCFAddressSgsnCamelSubscriptionInfo.getAddressNature(), AddressNature.international_number);
        assertEquals(gsmSCFAddressSgsnCamelSubscriptionInfo.getNumberingPlan(), NumberingPlan.ISDN);

        assertEquals(gprsCamelTDPData.getDefaultSessionHandling(), DefaultGPRSHandling.releaseTransaction);
        assertEquals(gprsCamelTDPData.getGPRSTriggerDetectionPoint(), GPRSTriggerDetectionPoint.attachChangeOfPosition);
        assertEquals(gprsCamelTDPData.getServiceKey(), 3);

        assertNotNull(extensionContainergprsCamelTDPData);
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainergprsCamelTDPData));

        assertEquals(gprsCsi.getCamelCapabilityHandling().intValue(), 8);
        assertNotNull(gprsCsi.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(gprsCsi.getExtensionContainer()));
        assertTrue(gprsCsi.getCsiActive());
        assertTrue(gprsCsi.getNotificationToCSE());

        SMSCSIImpl moSmsCsi = sgsnCamelSubscriptionInfo.getMoSmsCsi();
        List<SMSCAMELTDPDataImpl> smsCamelTdpDataListSgsnCamelSubscriptionInfo = moSmsCsi.getSmsCamelTdpDataList();
        assertNotNull(smsCamelTdpDataListSgsnCamelSubscriptionInfo);
        assertEquals(smsCamelTdpDataListSgsnCamelSubscriptionInfo.size(), 1);
        SMSCAMELTDPDataImpl oneSgsnCamelSubscriptionInfo = smsCamelTdpDataListSgsnCamelSubscriptionInfo.get(0);
        assertNotNull(oneSgsnCamelSubscriptionInfo);
        assertEquals(oneSgsnCamelSubscriptionInfo.getServiceKey(), 3);
        assertEquals(oneSgsnCamelSubscriptionInfo.getSMSTriggerDetectionPoint(), SMSTriggerDetectionPoint.smsCollectedInfo);
        ISDNAddressStringImpl gsmSCFAddressMoSmsCsi = oneSgsnCamelSubscriptionInfo.getGsmSCFAddress();
        assertTrue(gsmSCFAddressMoSmsCsi.getAddress().equals("22235"));
        assertEquals(gsmSCFAddressMoSmsCsi.getAddressNature(), AddressNature.international_number);
        assertEquals(gsmSCFAddressMoSmsCsi.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(oneSgsnCamelSubscriptionInfo.getDefaultSMSHandling(), DefaultSMSHandling.continueTransaction);
        assertNotNull(oneSgsnCamelSubscriptionInfo.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(oneSgsnCamelSubscriptionInfo.getExtensionContainer()));

        assertNotNull(moSmsCsi.getExtensionContainer());
        assertTrue(moSmsCsi.getCsiActive());
        assertTrue(moSmsCsi.getNotificationToCSE());
        assertEquals(moSmsCsi.getCamelCapabilityHandling().intValue(), 8);

        SMSCSIImpl mtSmsCsi = sgsnCamelSubscriptionInfo.getMtSmsCsi();
        List<SMSCAMELTDPDataImpl> smsCamelTdpDataListMtSmsCsi = mtSmsCsi.getSmsCamelTdpDataList();
        assertNotNull(smsCamelTdpDataListMtSmsCsi);
        assertEquals(smsCamelTdpDataListMtSmsCsi.size(), 1);
        SMSCAMELTDPDataImpl oneSmsCamelTdpDataListMtSmsCsi = smsCamelTdpDataListMtSmsCsi.get(0);
        assertNotNull(oneSmsCamelTdpDataListMtSmsCsi);
        assertEquals(oneSmsCamelTdpDataListMtSmsCsi.getServiceKey(), 3);
        assertEquals(oneSmsCamelTdpDataListMtSmsCsi.getSMSTriggerDetectionPoint(), SMSTriggerDetectionPoint.smsCollectedInfo);
        ISDNAddressStringImpl gsmSCFAddressmtSmsCsi = oneSgsnCamelSubscriptionInfo.getGsmSCFAddress();
        assertTrue(gsmSCFAddressmtSmsCsi.getAddress().equals("22235"));
        assertEquals(gsmSCFAddressmtSmsCsi.getAddressNature(), AddressNature.international_number);
        assertEquals(gsmSCFAddressmtSmsCsi.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(oneSgsnCamelSubscriptionInfo.getDefaultSMSHandling(), DefaultSMSHandling.continueTransaction);
        assertNotNull(oneSgsnCamelSubscriptionInfo.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(oneSgsnCamelSubscriptionInfo.getExtensionContainer()));

        assertNotNull(mtSmsCsi.getExtensionContainer());
        assertFalse(mtSmsCsi.getCsiActive());
        assertFalse(mtSmsCsi.getNotificationToCSE());
        assertEquals(mtSmsCsi.getCamelCapabilityHandling().intValue(), 8);

        List<MTsmsCAMELTDPCriteriaImpl> mtSmsCamelTdpCriteriaListSgsnCamelSubscriptionInfo = sgsnCamelSubscriptionInfo.getMtSmsCamelTdpCriteriaList();
        assertNotNull(mtSmsCamelTdpCriteriaListSgsnCamelSubscriptionInfo);
        assertEquals(mtSmsCamelTdpCriteriaListSgsnCamelSubscriptionInfo.size(), 1);
        MTsmsCAMELTDPCriteriaImpl mtSmsCAMELTDPCriteria = mtSmsCamelTdpCriteriaListSgsnCamelSubscriptionInfo.get(0);

        List<MTSMSTPDUType> tPDUTypeCriterionSgsnCamelSubscriptionInfo = mtSmsCAMELTDPCriteria.getTPDUTypeCriterion();
        assertNotNull(tPDUTypeCriterionSgsnCamelSubscriptionInfo);
        assertEquals(tPDUTypeCriterionSgsnCamelSubscriptionInfo.size(), 2);
        MTSMSTPDUType oneMTSMSTPDUType = tPDUTypeCriterion.get(0);
        assertNotNull(oneMTSMSTPDUType);
        assertEquals(oneMTSMSTPDUType, MTSMSTPDUType.smsDELIVER);

        MTSMSTPDUType twoMTSMSTPDUType = tPDUTypeCriterion.get(1);
        assertNotNull(twoMTSMSTPDUType);
        assertEquals(twoMTSMSTPDUType, MTSMSTPDUType.smsSTATUSREPORT);
        assertEquals(mtSmsCAMELTDPCriteria.getSMSTriggerDetectionPoint(), SMSTriggerDetectionPoint.smsCollectedInfo);

        MGCSIImpl mgCsi = sgsnCamelSubscriptionInfo.getMgCsi();

        List<MMCodeImpl> mobilityTriggersSgsnCamelSubscriptionInfo = mgCsi.getMobilityTriggers();
        assertNotNull(mobilityTriggersSgsnCamelSubscriptionInfo);
        assertEquals(mobilityTriggersSgsnCamelSubscriptionInfo.size(), 2);
        MMCodeImpl oneMMCode = mobilityTriggersSgsnCamelSubscriptionInfo.get(0);
        assertNotNull(oneMMCode);
        assertEquals(oneMMCode.getMMCodeValue(), MMCodeValue.GPRSAttach);
        MMCodeImpl twoMMCode = mobilityTriggers.get(1);
        assertNotNull(twoMMCode);
        assertEquals(twoMMCode.getMMCodeValue(), MMCodeValue.IMSIAttach);

        assertEquals(mgCsi.getServiceKey(), 3);

        ISDNAddressStringImpl gsmSCFAddressMgCsi = mgCsi.getGsmSCFAddress();
        assertTrue(gsmSCFAddressMgCsi.getAddress().equals("22235"));
        assertEquals(gsmSCFAddressMgCsi.getAddressNature(), AddressNature.international_number);
        assertEquals(gsmSCFAddressMgCsi.getNumberingPlan(), NumberingPlan.ISDN);

        assertNotNull(mgCsi.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(mgCsi.getExtensionContainer()));
        assertTrue(mgCsi.getCsiActive());
        assertTrue(mgCsi.getNotificationToCSE());

        assertNotNull(sgsnCamelSubscriptionInfo.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(sgsnCamelSubscriptionInfo.getExtensionContainer()));
        // end sgsnCamelSubscriptionInfo

        // chargingCharacteristics
        ChargingCharacteristicsImpl chargingCharacteristicsISD = prim.getChargingCharacteristics();
        assertTrue(Arrays.equals(chargingCharacteristicsISD.getData(), this.getChargingCharacteristicsData()));

        // start accessRestrictionData
        AccessRestrictionDataImpl accessRestrictionData = prim.getAccessRestrictionData();
        assertTrue(!accessRestrictionData.getUtranNotAllowed());
        assertTrue(accessRestrictionData.getGeranNotAllowed());
        assertTrue(!accessRestrictionData.getGanNotAllowed());
        assertTrue(accessRestrictionData.getIHspaEvolutionNotAllowed());
        assertTrue(!accessRestrictionData.getEUtranNotAllowed());
        assertTrue(accessRestrictionData.getHoToNon3GPPAccessNotAllowed());
        // end accessRestrictionData

        // icsIndicator
        Boolean icsIndicator = prim.getIcsIndicator();
        assertNotNull(icsIndicator);
        assertTrue(icsIndicator);

        // start epsSubscriptionData
        EPSSubscriptionDataImpl epsSubscriptionData = prim.getEpsSubscriptionData();
        AMBRImpl ambrepsSubscriptionData = epsSubscriptionData.getAmbr();
        MAPExtensionContainerImpl extensionContainerambrambrepsSubscriptionData = ambrepsSubscriptionData.getExtensionContainer();
        assertEquals(ambrepsSubscriptionData.getMaxRequestedBandwidthDL(), 4);
        assertEquals(ambrepsSubscriptionData.getMaxRequestedBandwidthUL(), 2);
        assertNotNull(extensionContainerambrambrepsSubscriptionData);
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainerambrambrepsSubscriptionData));
        assertTrue(Arrays.equals(epsSubscriptionData.getApnOiReplacement().getData(), this.getAPNOIReplacementData()));
        MAPExtensionContainerImpl epsSubscriptionDataMAPExtensionContainer = epsSubscriptionData.getExtensionContainer();
        assertNotNull(epsSubscriptionDataMAPExtensionContainer);
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(epsSubscriptionDataMAPExtensionContainer));

        assertTrue(epsSubscriptionData.getMpsCSPriority());
        assertTrue(epsSubscriptionData.getMpsEPSPriority());

        assertEquals(epsSubscriptionData.getRfspId().intValue(), 4);

        ISDNAddressStringImpl stnSr = epsSubscriptionData.getStnSr();
        assertTrue(stnSr.getAddress().equals("22228"));
        assertEquals(stnSr.getAddressNature(), AddressNature.international_number);
        assertEquals(stnSr.getNumberingPlan(), NumberingPlan.ISDN);

        APNConfigurationProfileImpl apnConfigurationProfile = epsSubscriptionData.getAPNConfigurationProfile();

        assertEquals(apnConfigurationProfile.getDefaultContext(), 2);
        assertTrue(apnConfigurationProfile.getCompleteDataListIncluded());
        MAPExtensionContainerImpl apnConfigurationProfileExtensionContainer = apnConfigurationProfile.getExtensionContainer();
        assertNotNull(apnConfigurationProfileExtensionContainer);
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(apnConfigurationProfileExtensionContainer));

        List<APNConfigurationImpl> ePSDataList = apnConfigurationProfile.getEPSDataList();
        assertNotNull(ePSDataList);
        assertEquals(ePSDataList.size(), 1);

        APNConfigurationImpl apnConfiguration = ePSDataList.get(0);
        assertEquals(apnConfiguration.getContextId(), 1);
        assertEquals(apnConfiguration.getPDNType().getPDNTypeValue(), PDNTypeValue.IPv4);
        PDPAddressImpl servedPartyIPIPv4Address = apnConfiguration.getServedPartyIPIPv4Address();
        assertNotNull(servedPartyIPIPv4Address);
        assertTrue(Arrays.equals(this.getPDPAddressData(), servedPartyIPIPv4Address.getData()));
        assertTrue(Arrays.equals(apnConfiguration.getApn().getData(), this.getAPNData()));

        EPSQoSSubscribedImpl ePSQoSSubscribed = apnConfiguration.getEPSQoSSubscribed();
        AllocationRetentionPriorityImpl allocationRetentionPriority = ePSQoSSubscribed.getAllocationRetentionPriority();
        MAPExtensionContainerImpl extensionContainerePSQoSSubscribed = ePSQoSSubscribed.getExtensionContainer();
        assertEquals(allocationRetentionPriority.getPriorityLevel(), 1);
        assertTrue(allocationRetentionPriority.getPreEmptionCapability());
        assertTrue(allocationRetentionPriority.getPreEmptionVulnerability());
        assertNotNull(allocationRetentionPriority.getExtensionContainer());
        ;
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(allocationRetentionPriority.getExtensionContainer()));
        assertNotNull(extensionContainerePSQoSSubscribed);
        assertEquals(ePSQoSSubscribed.getQoSClassIdentifier(), QoSClassIdentifier.QCI_1);
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainerePSQoSSubscribed));

        PDNGWIdentityImpl pdnGWIdentity = apnConfiguration.getPdnGwIdentity();
        PDPAddressImpl pdnGwIpv4Address = pdnGWIdentity.getPdnGwIpv4Address();
        assertNotNull(pdnGwIpv4Address);
        assertTrue(Arrays.equals(this.getPDPAddressData(), pdnGwIpv4Address.getData()));
        PDPAddressImpl pdnGwIpv6Address = pdnGWIdentity.getPdnGwIpv6Address();
        assertNotNull(pdnGwIpv6Address);
        assertTrue(Arrays.equals(this.getPDPAddressData(), pdnGwIpv6Address.getData()));
        FQDNImpl pdnGwName = pdnGWIdentity.getPdnGwName();
        assertNotNull(pdnGwName);
        assertTrue(Arrays.equals(this.getFQDNData(), pdnGwName.getData()));
        assertNotNull(pdnGWIdentity.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(pdnGWIdentity.getExtensionContainer()));

        assertEquals(apnConfiguration.getPdnGwAllocationType(), PDNGWAllocationType._dynamic);
        assertFalse(apnConfiguration.getVplmnAddressAllowed());
        assertTrue(Arrays.equals(this.getChargingCharacteristicsData(), apnConfiguration.getChargingCharacteristics().getData()));

        AMBRImpl ambr = apnConfiguration.getAmbr();
        MAPExtensionContainerImpl extensionContainerambr = ambr.getExtensionContainer();
        assertEquals(ambr.getMaxRequestedBandwidthDL(), 4);
        assertEquals(ambr.getMaxRequestedBandwidthUL(), 2);
        assertNotNull(extensionContainerambr);
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainerambr));

        List<SpecificAPNInfoImpl> specificAPNInfoList = apnConfiguration.getSpecificAPNInfoList();
        assertNotNull(specificAPNInfoList);
        assertEquals(specificAPNInfoList.size(), 1);
        SpecificAPNInfoImpl specificAPNInfo = specificAPNInfoList.get(0);

        PDNGWIdentityImpl pdnGWIdentitySpecificAPNInfo = specificAPNInfo.getPdnGwIdentity();
        PDPAddressImpl pdnGwIpv4AddressSpecificAPNInfo = pdnGWIdentitySpecificAPNInfo.getPdnGwIpv4Address();
        assertNotNull(pdnGwIpv4AddressSpecificAPNInfo);
        assertTrue(Arrays.equals(this.getPDPAddressData(), pdnGwIpv4AddressSpecificAPNInfo.getData()));
        PDPAddressImpl pdnGwIpv6AddressSpecificAPNInfo = pdnGWIdentitySpecificAPNInfo.getPdnGwIpv6Address();
        assertNotNull(pdnGwIpv6AddressSpecificAPNInfo);
        assertTrue(Arrays.equals(this.getPDPAddressData(), pdnGwIpv6AddressSpecificAPNInfo.getData()));
        FQDNImpl pdnGwNameSpecificAPNInfo = pdnGWIdentitySpecificAPNInfo.getPdnGwName();
        assertNotNull(pdnGwNameSpecificAPNInfo);
        assertTrue(Arrays.equals(this.getFQDNData(), pdnGwNameSpecificAPNInfo.getData()));
        assertNotNull(pdnGWIdentitySpecificAPNInfo.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(pdnGWIdentitySpecificAPNInfo.getExtensionContainer()));
        MAPExtensionContainerImpl extensionContainerspecificAPNInfo = specificAPNInfo.getExtensionContainer();
        assertNotNull(extensionContainerspecificAPNInfo);
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainerspecificAPNInfo));

        assertTrue(Arrays.equals(specificAPNInfo.getAPN().getData(), this.getAPNData()));

        PDPAddressImpl servedPartyIPIPv6Address = apnConfiguration.getServedPartyIPIPv6Address();
        assertNotNull(servedPartyIPIPv6Address);
        assertTrue(Arrays.equals(this.getPDPAddressData(), servedPartyIPIPv6Address.getData()));
        assertTrue(Arrays.equals(this.getAPNOIReplacementData(), apnConfiguration.getApnOiReplacement().getData()));
        assertEquals(apnConfiguration.getSiptoPermission(), SIPTOPermission.siptoAllowed);
        assertEquals(apnConfiguration.getLipaPermission(), LIPAPermission.lipaConditional);
        assertNotNull(apnConfiguration.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(apnConfiguration.getExtensionContainer()));
        // end epsSubscriptionData

        // start csgSubscriptionDataList
        List<CSGSubscriptionDataImpl> csgSubscriptionDataList = prim.getCsgSubscriptionDataList();
        assertNotNull(csgSubscriptionDataList);
        assertEquals(csgSubscriptionDataList.size(), 1);
        CSGSubscriptionDataImpl csgSubscriptionData = csgSubscriptionDataList.get(0);

        assertTrue(csgSubscriptionData.getCsgId().isBitSet(0));
        assertFalse(csgSubscriptionData.getCsgId().isBitSet(1));
        assertFalse(csgSubscriptionData.getCsgId().isBitSet(25));
        assertTrue(csgSubscriptionData.getCsgId().isBitSet(26));

        assertTrue(Arrays.equals(csgSubscriptionData.getExpirationDate().getData(), this.getTimeData()));

        List<APNImpl> lipaAllowedAPNList = csgSubscriptionData.getLipaAllowedAPNList();
        assertNotNull(lipaAllowedAPNList);
        assertEquals(lipaAllowedAPNList.size(), 1);
        assertTrue(Arrays.equals(lipaAllowedAPNList.get(0).getData(), this.getAPNData()));
        assertNotNull(csgSubscriptionData.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(csgSubscriptionData.getExtensionContainer()));
        // end csgSubscriptionDataList

        // ueReachabilityRequestIndicator
        assertTrue(prim.getUeReachabilityRequestIndicator());

        // start sgsnNumber
        ISDNAddressStringImpl sgsnNumber = prim.getSgsnNumber();
        assertNotNull(sgsnNumber);
        assertTrue(sgsnNumber.getAddress().equals("22228"));
        assertEquals(sgsnNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(sgsnNumber.getNumberingPlan(), NumberingPlan.ISDN);
        // end sgsnNumber

        // mmeName
        DiameterIdentityImpl mmeName = prim.getMmeName();
        assertTrue(Arrays.equals(mmeName.getData(), this.getDiameterIdentity()));

        // SubscribedPeriodicRAUTAUtimer
        assertTrue(prim.getSubscribedPeriodicRAUTAUtimer().equals(Long.valueOf(2)));

        // vplmnLIPAAllowed
        assertTrue(prim.getVplmnLIPAAllowed());

        // mdtUserConsent
        Boolean mdtUserConsent = prim.getMdtUserConsent();
        assertNotNull(mdtUserConsent);
        assertTrue(mdtUserConsent);

        // SubscribedPeriodicLAUtimer
        assertTrue(prim.getSubscribedPeriodicLAUtimer().equals(Long.valueOf(2)));

        // extensionContainer
        MAPExtensionContainerImpl extensionContainer = prim.getExtensionContainer();
        assertNotNull(extensionContainer);
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer));
        // End ISD MAP Protocol Version 3 message Testing

        // Start MAP Protocol Version 2 message Testing
        data = this.getData1();
        result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof InsertSubscriberDataRequestImpl);
        prim = (InsertSubscriberDataRequestImpl)result.getResult(); 
        
        // imsi
        imsi = prim.getImsi();
        assertTrue(imsi.getData().equals("1111122222"));

        // msisdn
        msisdn = prim.getMsisdn();
        assertTrue(msisdn.getAddress().equals("22234"));
        assertEquals(msisdn.getAddressNature(), AddressNature.international_number);
        assertEquals(msisdn.getNumberingPlan(), NumberingPlan.ISDN);

        // category
        category = prim.getCategory();
        assertEquals(category.getData(), 5);

        // subscriberStatus
        subscriberStatus = prim.getSubscriberStatus();
        assertEquals(subscriberStatus, SubscriberStatus.operatorDeterminedBarring);

        // bearerServiceList
        bearerServiceList = prim.getBearerServiceList();
        assertNotNull(bearerServiceList);
        assertEquals(bearerServiceList.size(), 1);
        extBearerServiceCode = bearerServiceList.get(0);
        assertEquals(extBearerServiceCode.getBearerServiceCodeValue(), BearerServiceCodeValue.padAccessCA_9600bps);

        // teleserviceList
        teleserviceList = prim.getTeleserviceList();
        assertNotNull(teleserviceList);
        assertEquals(teleserviceList.size(), 1);
        extTeleserviceCode = teleserviceList.get(0);
        assertEquals(extTeleserviceCode.getTeleserviceCodeValue(), TeleserviceCodeValue.allSpeechTransmissionServices);

        // start provisionedSS
        provisionedSS = prim.getProvisionedSS();
        assertNotNull(provisionedSS);
        assertEquals(provisionedSS.size(), 1);
        extSSInfo = provisionedSS.get(0);

        forwardingInfo = extSSInfo.getForwardingInfo();
        callBarringInfo = extSSInfo.getCallBarringInfo();
        cugInfo = extSSInfo.getCugInfo();
        ssData = extSSInfo.getSsData();
        emlppInfo = extSSInfo.getEmlppInfo();

        assertNotNull(forwardingInfo);
        assertNull(callBarringInfo);
        assertNull(cugInfo);
        assertNull(ssData);
        assertNull(emlppInfo);

        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(forwardingInfo.getExtensionContainer()));
        assertEquals(forwardingInfo.getSsCode().getSupplementaryCodeValue(), SupplementaryCodeValue.allSS);

        forwardingFeatureList = forwardingInfo.getForwardingFeatureList();
        ;
        assertNotNull(forwardingFeatureList);
        assertEquals(forwardingFeatureList.size(), 1);
        extForwFeature = forwardingFeatureList.get(0);
        assertNotNull(extForwFeature);

        assertEquals(extForwFeature.getBasicService().getExtBearerService().getBearerServiceCodeValue(), BearerServiceCodeValue.padAccessCA_9600bps);
        assertNull(extForwFeature.getBasicService().getExtTeleservice());
        assertNotNull(extForwFeature.getSsStatus());
        assertTrue(extForwFeature.getSsStatus().getBitA());
        assertTrue(extForwFeature.getSsStatus().getBitP());
        assertTrue(extForwFeature.getSsStatus().getBitQ());
        assertTrue(extForwFeature.getSsStatus().getBitR());

        forwardedToNumber = extForwFeature.getForwardedToNumber();
        assertNotNull(forwardedToNumber);
        assertTrue(forwardedToNumber.getAddress().equals("22228"));
        assertEquals(forwardedToNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(forwardedToNumber.getNumberingPlan(), NumberingPlan.ISDN);

        assertTrue(Arrays.equals(extForwFeature.getForwardedToSubaddress().getData(), this.getISDNSubaddressStringData()));
        assertTrue(extForwFeature.getForwardingOptions().getNotificationToCallingParty());
        assertTrue(extForwFeature.getForwardingOptions().getNotificationToForwardingParty());
        assertTrue(!extForwFeature.getForwardingOptions().getRedirectingPresentation());
        assertEquals(extForwFeature.getForwardingOptions().getExtForwOptionsForwardingReason(), ExtForwOptionsForwardingReason.msBusy);
        assertNotNull(extForwFeature.getNoReplyConditionTime());
        assertEquals(extForwFeature.getNoReplyConditionTime().intValue(), 2);
        longForwardedToNumber = extForwFeature.getLongForwardedToNumber();
        assertNotNull(longForwardedToNumber);
        assertTrue(longForwardedToNumber.getAddress().equals("22227"));
        assertEquals(longForwardedToNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(longForwardedToNumber.getNumberingPlan(), NumberingPlan.ISDN);
        // end provisionedSS

        // start odbData
        odbData = prim.getODBData();
        oDBGeneralData = odbData.getODBGeneralData();
        assertTrue(!oDBGeneralData.getAllOGCallsBarred());
        assertTrue(oDBGeneralData.getInternationalOGCallsBarred());
        assertTrue(!oDBGeneralData.getInternationalOGCallsNotToHPLMNCountryBarred());
        assertTrue(oDBGeneralData.getInterzonalOGCallsBarred());
        assertTrue(!oDBGeneralData.getInterzonalOGCallsNotToHPLMNCountryBarred());
        assertTrue(oDBGeneralData.getInterzonalOGCallsAndInternationalOGCallsNotToHPLMNCountryBarred());
        assertTrue(!oDBGeneralData.getPremiumRateInformationOGCallsBarred());
        assertTrue(oDBGeneralData.getPremiumRateEntertainementOGCallsBarred());
        assertTrue(!oDBGeneralData.getSsAccessBarred());
        assertTrue(oDBGeneralData.getAllECTBarred());
        assertTrue(!oDBGeneralData.getChargeableECTBarred());
        assertTrue(oDBGeneralData.getInternationalECTBarred());
        assertTrue(!oDBGeneralData.getInterzonalECTBarred());
        assertTrue(oDBGeneralData.getDoublyChargeableECTBarred());
        assertTrue(!oDBGeneralData.getMultipleECTBarred());
        assertTrue(oDBGeneralData.getAllPacketOrientedServicesBarred());
        assertTrue(!oDBGeneralData.getRoamerAccessToHPLMNAPBarred());
        assertTrue(oDBGeneralData.getRoamerAccessToVPLMNAPBarred());
        assertTrue(!oDBGeneralData.getRoamingOutsidePLMNOGCallsBarred());
        assertTrue(oDBGeneralData.getAllICCallsBarred());
        assertTrue(!oDBGeneralData.getRoamingOutsidePLMNICCallsBarred());
        assertTrue(oDBGeneralData.getRoamingOutsidePLMNICountryICCallsBarred());
        assertTrue(!oDBGeneralData.getRoamingOutsidePLMNBarred());
        assertTrue(oDBGeneralData.getRoamingOutsidePLMNCountryBarred());
        assertTrue(!oDBGeneralData.getRegistrationAllCFBarred());
        assertTrue(oDBGeneralData.getRegistrationCFNotToHPLMNBarred());
        assertTrue(!oDBGeneralData.getRegistrationInterzonalCFBarred());
        assertTrue(oDBGeneralData.getRegistrationInterzonalCFNotToHPLMNBarred());
        assertTrue(!oDBGeneralData.getRegistrationInternationalCFBarred());
        odbHplmnData = odbData.getOdbHplmnData();
        assertTrue(!odbHplmnData.getPlmnSpecificBarringType1());
        assertTrue(odbHplmnData.getPlmnSpecificBarringType2());
        assertTrue(!odbHplmnData.getPlmnSpecificBarringType3());
        assertTrue(odbHplmnData.getPlmnSpecificBarringType4());
        assertNotNull(odbData.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(odbData.getExtensionContainer()));
        // end odbData

        // start roamingRestrictionDueToUnsupportedFeature
        assertTrue(prim.getRoamingRestrictionDueToUnsupportedFeature());

        // start regionalSubscriptionData
        regionalSubscriptionData = prim.getRegionalSubscriptionData();
        assertNotNull(regionalSubscriptionData);
        assertEquals(regionalSubscriptionData.size(), 1);
        zoneCode = regionalSubscriptionData.get(0);
        assertEquals(zoneCode.getIntValue(), 2);
        // end regionalSubscriptionData

        // start vbsSubscriptionData
        vbsSubscriptionData = prim.getVbsSubscriptionData();
        assertNotNull(vbsSubscriptionData);
        assertEquals(vbsSubscriptionData.size(), 1);
        voiceBroadcastData = vbsSubscriptionData.get(0);
        assertTrue(voiceBroadcastData.getGroupId().getGroupId().equals(""));
        assertTrue(voiceBroadcastData.getLongGroupId().getLongGroupId().equals("5"));
        assertTrue(voiceBroadcastData.getBroadcastInitEntitlement());
        assertNotNull(voiceBroadcastData.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(voiceBroadcastData.getExtensionContainer()));
        // end vbsSubscriptionData

        // start vgcsSubscriptionData
        vgcsSubscriptionData = prim.getVgcsSubscriptionData();
        assertNotNull(vgcsSubscriptionData);
        assertEquals(vgcsSubscriptionData.size(), 1);
        voiceGroupCallData = vgcsSubscriptionData.get(0);
        assertTrue(voiceGroupCallData.getGroupId().getGroupId().equals(""));
        assertTrue(voiceGroupCallData.getLongGroupId().getLongGroupId().equals("5"));
        assertNotNull(voiceGroupCallData.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(voiceGroupCallData.getExtensionContainer()));
        assertTrue(voiceGroupCallData.getAdditionalSubscriptions().getEmergencyReset());
        assertFalse(voiceGroupCallData.getAdditionalSubscriptions().getEmergencyUplinkRequest());
        assertTrue(voiceGroupCallData.getAdditionalSubscriptions().getPrivilegedUplinkRequest());
        assertNotNull(voiceGroupCallData.getAdditionalInfo());
        assertTrue(voiceGroupCallData.getAdditionalInfo().isBitSet(0));
        // end vgcsSubscriptionData

        // start vlrCamelSubscriptionInfo
        vlrCamelSubscriptionInfo = prim.getVlrCamelSubscriptionInfo();

        oCsi = vlrCamelSubscriptionInfo.getOCsi();
        lst = oCsi.getOBcsmCamelTDPDataList();
        assertEquals(lst.size(), 1);
        cd = lst.get(0);
        assertEquals(cd.getOBcsmTriggerDetectionPoint(), OBcsmTriggerDetectionPoint.routeSelectFailure);
        assertEquals(cd.getServiceKey(), 3);
        assertEquals(cd.getGsmSCFAddress().getAddressNature(), AddressNature.international_number);
        assertEquals(cd.getGsmSCFAddress().getNumberingPlan(), NumberingPlan.ISDN);
        assertTrue(cd.getGsmSCFAddress().getAddress().equals("1122333"));
        assertEquals(cd.getDefaultCallHandling(), DefaultCallHandling.releaseCall);
        assertNull(cd.getExtensionContainer());

        assertNull(oCsi.getExtensionContainer());
        assertEquals((int) oCsi.getCamelCapabilityHandling(), 2);
        assertFalse(oCsi.getNotificationToCSE());
        assertFalse(oCsi.getCsiActive());

        assertNotNull(vlrCamelSubscriptionInfo.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(vlrCamelSubscriptionInfo.getExtensionContainer()));

        ssCsi = vlrCamelSubscriptionInfo.getSsCsi();
        ssCamelData = ssCsi.getSsCamelData();

        ssEventList = ssCamelData.getSsEventList();
        assertNotNull(ssEventList);
        assertEquals(ssEventList.size(), 1);
        one = ssEventList.get(0);
        assertNotNull(one);
        assertEquals(one.getSupplementaryCodeValue(), SupplementaryCodeValue.allCommunityOfInterestSS);
        gsmSCFAddress = ssCamelData.getGsmSCFAddress();
        assertTrue(gsmSCFAddress.getAddress().equals("22235"));
        assertEquals(gsmSCFAddress.getAddressNature(), AddressNature.international_number);
        assertEquals(gsmSCFAddress.getNumberingPlan(), NumberingPlan.ISDN);
        assertNotNull(ssCamelData.getExtensionContainer());
        assertNotNull(ssCsi.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(ssCsi.getExtensionContainer()));
        assertTrue(ssCsi.getCsiActive());
        assertTrue(ssCsi.getNotificationToCSE());

        oBcsmCamelTDPCriteriaList = vlrCamelSubscriptionInfo.getOBcsmCamelTDPCriteriaList();
        assertNotNull(oBcsmCamelTDPCriteriaList);
        assertEquals(oBcsmCamelTDPCriteriaList.size(), 1);
        oBcsmCamelTdpCriteria = oBcsmCamelTDPCriteriaList.get(0);
        assertNotNull(oBcsmCamelTdpCriteria);

        destinationNumberCriteria = oBcsmCamelTdpCriteria.getDestinationNumberCriteria();
        destinationNumberList = destinationNumberCriteria.getDestinationNumberList();
        assertNotNull(destinationNumberList);
        assertEquals(destinationNumberList.size(), 2);
        destinationNumberOne = destinationNumberList.get(0);
        assertNotNull(destinationNumberOne);
        assertTrue(destinationNumberOne.getAddress().equals("22234"));
        assertEquals(destinationNumberOne.getAddressNature(), AddressNature.international_number);
        assertEquals(destinationNumberOne.getNumberingPlan(), NumberingPlan.ISDN);
        destinationNumberTwo = destinationNumberList.get(1);
        assertNotNull(destinationNumberTwo);
        assertTrue(destinationNumberTwo.getAddress().equals("22235"));
        assertEquals(destinationNumberTwo.getAddressNature(), AddressNature.international_number);
        assertEquals(destinationNumberTwo.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(destinationNumberCriteria.getMatchType().getCode(), MatchType.enabling.getCode());
        destinationNumberLengthList = destinationNumberCriteria.getDestinationNumberLengthList();
        assertNotNull(destinationNumberLengthList);
        assertEquals(destinationNumberLengthList.size(), 3);
        assertEquals(destinationNumberLengthList.get(0).intValue(), 2);
        assertEquals(destinationNumberLengthList.get(1).intValue(), 4);
        assertEquals(destinationNumberLengthList.get(2).intValue(), 1);
        assertEquals(oBcsmCamelTdpCriteria.getOBcsmTriggerDetectionPoint(), OBcsmTriggerDetectionPoint.collectedInfo);
        assertNotNull(oBcsmCamelTdpCriteria.getBasicServiceCriteria());
        assertEquals(oBcsmCamelTdpCriteria.getBasicServiceCriteria().size(), 2);
        basicServiceOne = oBcsmCamelTdpCriteria.getBasicServiceCriteria().get(0);
        assertNotNull(basicServiceOne);
        assertEquals(basicServiceOne.getExtBearerService().getBearerServiceCodeValue(), BearerServiceCodeValue.padAccessCA_9600bps);

        basicServiceTwo = oBcsmCamelTdpCriteria.getBasicServiceCriteria().get(1);
        assertNotNull(basicServiceTwo);
        assertEquals(basicServiceTwo.getExtTeleservice().getTeleserviceCodeValue(), TeleserviceCodeValue.allSpeechTransmissionServices);

        assertEquals(oBcsmCamelTdpCriteria.getCallTypeCriteria(), CallTypeCriteria.forwarded);
        oCauseValueCriteria = oBcsmCamelTdpCriteria.getOCauseValueCriteria();
        assertNotNull(oCauseValueCriteria);
        assertEquals(oCauseValueCriteria.size(), 1);
        assertNotNull(oCauseValueCriteria.get(0));
        assertEquals(oCauseValueCriteria.get(0).getData(), 7);

        assertFalse(vlrCamelSubscriptionInfo.getTifCsi());

        mCsi = vlrCamelSubscriptionInfo.getMCsi();
        mobilityTriggers = mCsi.getMobilityTriggers();
        assertNotNull(mobilityTriggers);
        assertEquals(mobilityTriggers.size(), 2);
        mmCode = mobilityTriggers.get(0);
        assertNotNull(mmCode);
        assertEquals(MMCodeValue.GPRSAttach, mmCode.getMMCodeValue());
        mmCode2 = mobilityTriggers.get(1);
        assertNotNull(mmCode2);
        assertEquals(MMCodeValue.IMSIAttach, mmCode2.getMMCodeValue());
        assertNotNull(mCsi.getServiceKey());
        assertEquals(mCsi.getServiceKey(), 3);
        gsmSCFAddressTwo = mCsi.getGsmSCFAddress();
        assertTrue(gsmSCFAddressTwo.getAddress().equals("22235"));
        assertEquals(gsmSCFAddressTwo.getAddressNature(), AddressNature.international_number);
        assertEquals(gsmSCFAddressTwo.getNumberingPlan(), NumberingPlan.ISDN);
        assertNotNull(mCsi.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(mCsi.getExtensionContainer()));
        assertTrue(mCsi.getCsiActive());
        assertTrue(mCsi.getNotificationToCSE());

        smsCsi = vlrCamelSubscriptionInfo.getSmsCsi();
        smsCamelTdpDataList = smsCsi.getSmsCamelTdpDataList();
        assertNotNull(smsCamelTdpDataList);
        assertEquals(smsCamelTdpDataList.size(), 1);
        smsCAMELTDPData = smsCamelTdpDataList.get(0);
        assertNotNull(smsCAMELTDPData);
        assertEquals(smsCAMELTDPData.getServiceKey(), 3);
        assertEquals(smsCAMELTDPData.getSMSTriggerDetectionPoint(), SMSTriggerDetectionPoint.smsCollectedInfo);
        gsmSCFAddressSmsCAMELTDPData = smsCAMELTDPData.getGsmSCFAddress();
        assertTrue(gsmSCFAddressSmsCAMELTDPData.getAddress().equals("22235"));
        assertEquals(gsmSCFAddressSmsCAMELTDPData.getAddressNature(), AddressNature.international_number);
        assertEquals(gsmSCFAddressSmsCAMELTDPData.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(smsCAMELTDPData.getDefaultSMSHandling(), DefaultSMSHandling.continueTransaction);
        assertNotNull(smsCAMELTDPData.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(smsCAMELTDPData.getExtensionContainer()));
        assertTrue(smsCsi.getCsiActive());
        assertTrue(smsCsi.getNotificationToCSE());
        assertEquals(smsCsi.getCamelCapabilityHandling().intValue(), 8);

        vtCsi = vlrCamelSubscriptionInfo.getVtCsi();
        tbcsmCamelTDPDatalst = vtCsi.getTBcsmCamelTDPDataList();
        assertEquals(lst.size(), 1);
        tbcsmCamelTDPData = tbcsmCamelTDPDatalst.get(0);
        assertEquals(tbcsmCamelTDPData.getTBcsmTriggerDetectionPoint(), TBcsmTriggerDetectionPoint.termAttemptAuthorized);
        assertEquals(tbcsmCamelTDPData.getServiceKey(), 3);
        assertEquals(tbcsmCamelTDPData.getGsmSCFAddress().getAddressNature(), AddressNature.international_number);
        assertEquals(tbcsmCamelTDPData.getGsmSCFAddress().getNumberingPlan(), NumberingPlan.ISDN);
        assertTrue(tbcsmCamelTDPData.getGsmSCFAddress().getAddress().equals("22235"));
        assertEquals(tbcsmCamelTDPData.getDefaultCallHandling(), DefaultCallHandling.releaseCall);
        assertNull(tbcsmCamelTDPData.getExtensionContainer());
        assertNull(vtCsi.getExtensionContainer());
        assertEquals((int) vtCsi.getCamelCapabilityHandling(), 2);
        assertFalse(vtCsi.getNotificationToCSE());
        assertFalse(vtCsi.getCsiActive());

        tBcsmCamelTdpCriteriaList = vlrCamelSubscriptionInfo.getTBcsmCamelTdpCriteriaList();
        assertNotNull(tBcsmCamelTdpCriteriaList);
        assertEquals(tBcsmCamelTdpCriteriaList.size(), 1);
        assertNotNull(tBcsmCamelTdpCriteriaList.get(0));
        tbcsmCamelTdpCriteria = tBcsmCamelTdpCriteriaList.get(0);
        assertEquals(tbcsmCamelTdpCriteria.getTBcsmTriggerDetectionPoint(), TBcsmTriggerDetectionPoint.tBusy);
        assertNotNull(tbcsmCamelTdpCriteria.getBasicServiceCriteria());
        assertEquals(tbcsmCamelTdpCriteria.getBasicServiceCriteria().size(), 2);
        assertNotNull(tbcsmCamelTdpCriteria.getBasicServiceCriteria().get(0));
        assertNotNull(tbcsmCamelTdpCriteria.getBasicServiceCriteria().get(1));
        oCauseValueCriteriaLst = tbcsmCamelTdpCriteria.getTCauseValueCriteria();
        assertNotNull(oCauseValueCriteriaLst);
        assertEquals(oCauseValueCriteriaLst.size(), 2);
        assertNotNull(oCauseValueCriteriaLst.get(0));
        assertEquals(oCauseValueCriteriaLst.get(0).getData(), 7);
        assertNotNull(oCauseValueCriteriaLst.get(1));
        assertEquals(oCauseValueCriteriaLst.get(1).getData(), 6);

        dCsi = vlrCamelSubscriptionInfo.getDCsi();
        dpAnalysedInfoCriteriaList = dCsi.getDPAnalysedInfoCriteriaList();
        assertNotNull(dpAnalysedInfoCriteriaList);
        assertEquals(dpAnalysedInfoCriteriaList.size(), 1);
        dpAnalysedInfoCriterium = dpAnalysedInfoCriteriaList.get(0);
        assertNotNull(dpAnalysedInfoCriterium);
        dialledNumber = dpAnalysedInfoCriterium.getDialledNumber();
        assertTrue(dialledNumber.getAddress().equals("22234"));
        assertEquals(dialledNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(dialledNumber.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(dpAnalysedInfoCriterium.getServiceKey(), 7);
        gsmSCFAddressDp = dpAnalysedInfoCriterium.getGsmSCFAddress();
        assertTrue(gsmSCFAddressDp.getAddress().equals("22235"));
        assertEquals(gsmSCFAddressDp.getAddressNature(), AddressNature.international_number);
        assertEquals(gsmSCFAddressDp.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(dpAnalysedInfoCriterium.getDefaultCallHandling(), DefaultCallHandling.continueCall);
        assertNotNull(dCsi.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(dCsi.getExtensionContainer()));
        assertEquals(dCsi.getCamelCapabilityHandling().intValue(), 2);
        assertTrue(dCsi.getCsiActive());
        assertTrue(dCsi.getNotificationToCSE());

        mtSmsCSI = vlrCamelSubscriptionInfo.getMtSmsCSI();
        smsCamelTdpDataListOfmtSmsCSI = mtSmsCSI.getSmsCamelTdpDataList();
        assertNotNull(smsCamelTdpDataListOfmtSmsCSI);
        assertEquals(smsCamelTdpDataListOfmtSmsCSI.size(), 1);
        smsCAMELTDPDataOfMtSmsCSI = smsCamelTdpDataListOfmtSmsCSI.get(0);
        assertNotNull(smsCAMELTDPDataOfMtSmsCSI);
        assertEquals(smsCAMELTDPDataOfMtSmsCSI.getServiceKey(), 3);
        assertEquals(smsCAMELTDPDataOfMtSmsCSI.getSMSTriggerDetectionPoint(), SMSTriggerDetectionPoint.smsCollectedInfo);
        gsmSCFAddressOfMtSmsCSI = smsCAMELTDPDataOfMtSmsCSI.getGsmSCFAddress();
        assertTrue(gsmSCFAddressOfMtSmsCSI.getAddress().equals("22235"));
        assertEquals(gsmSCFAddressOfMtSmsCSI.getAddressNature(), AddressNature.international_number);
        assertEquals(gsmSCFAddressOfMtSmsCSI.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(smsCAMELTDPDataOfMtSmsCSI.getDefaultSMSHandling(), DefaultSMSHandling.continueTransaction);
        assertNotNull(smsCAMELTDPDataOfMtSmsCSI.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(smsCAMELTDPDataOfMtSmsCSI.getExtensionContainer()));
        assertNotNull(mtSmsCSI.getExtensionContainer());
        assertTrue(mtSmsCSI.getCsiActive());
        assertTrue(mtSmsCSI.getNotificationToCSE());
        assertEquals(mtSmsCSI.getCamelCapabilityHandling().intValue(), 8);

        mtSmsCamelTdpCriteriaList = vlrCamelSubscriptionInfo.getMtSmsCamelTdpCriteriaList();
        assertNotNull(mtSmsCamelTdpCriteriaList);
        assertEquals(mtSmsCamelTdpCriteriaList.size(), 1);
        mtsmsCAMELTDPCriteria = mtSmsCamelTdpCriteriaList.get(0);

        tPDUTypeCriterion = mtsmsCAMELTDPCriteria.getTPDUTypeCriterion();
        assertNotNull(tPDUTypeCriterion);
        assertEquals(tPDUTypeCriterion.size(), 2);
        mtSMSTPDUTypeOne = tPDUTypeCriterion.get(0);
        assertNotNull(mtSMSTPDUTypeOne);
        assertEquals(mtSMSTPDUTypeOne, MTSMSTPDUType.smsDELIVER);

        mtSMSTPDUTypeTwo = tPDUTypeCriterion.get(1);
        assertNotNull(mtSMSTPDUTypeTwo);
        assertTrue(mtSMSTPDUTypeTwo == MTSMSTPDUType.smsSTATUSREPORT);
        assertEquals(mtsmsCAMELTDPCriteria.getSMSTriggerDetectionPoint(), SMSTriggerDetectionPoint.smsCollectedInfo);
        // end vlrCamelSubscriptionInfo

    }

    @Test(groups = { "functional.encode", "service.mobility.subscriberManagement" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(InsertSubscriberDataRequestImpl.class);
    	
        MAPExtensionContainerImpl extensionContainer = MAPExtensionContainerTest.GetTestExtensionContainer();

        IMSIImpl imsi = new IMSIImpl("1111122222");
        ISDNAddressStringImpl msisdn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "22234");
        CategoryImpl category = new CategoryImpl(5);
        SubscriberStatus subscriberStatus = SubscriberStatus.operatorDeterminedBarring;

        // bearerServiceList
        ArrayList<ExtBearerServiceCodeImpl> bearerServiceList = new ArrayList<ExtBearerServiceCodeImpl>();
        ExtBearerServiceCodeImpl extBearerServiceCode = new ExtBearerServiceCodeImpl(BearerServiceCodeValue.padAccessCA_9600bps);
        bearerServiceList.add(extBearerServiceCode);

        // teleserviceList
        ArrayList<ExtTeleserviceCodeImpl> teleserviceList = new ArrayList<ExtTeleserviceCodeImpl>();
        ExtTeleserviceCodeImpl extTeleservice = new ExtTeleserviceCodeImpl(TeleserviceCodeValue.allSpeechTransmissionServices);
        teleserviceList.add(extTeleservice);

        // provisionedSS
        ArrayList<ExtSSInfoImpl> provisionedSS = new ArrayList<ExtSSInfoImpl>();
        SSCodeImpl ssCode = new SSCodeImpl(SupplementaryCodeValue.allSS);
        ArrayList<ExtForwFeatureImpl> forwardingFeatureList = new ArrayList<ExtForwFeatureImpl>();
        ExtBearerServiceCodeImpl b = new ExtBearerServiceCodeImpl(BearerServiceCodeValue.padAccessCA_9600bps);
        ExtBasicServiceCodeImpl basicService = new ExtBasicServiceCodeImpl(b);
        ExtSSStatusImpl ssStatus = new ExtSSStatusImpl(true, true, true, true);
        ISDNAddressStringImpl forwardedToNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "22228");
        ISDNSubaddressStringImpl forwardedToSubaddress = new ISDNSubaddressStringImpl(this.getISDNSubaddressStringData());
        ExtForwOptionsImpl forwardingOptions = new ExtForwOptionsImpl(true, false, true, ExtForwOptionsForwardingReason.msBusy);
        Integer noReplyConditionTime = 2;
        FTNAddressStringImpl longForwardedToNumber = new FTNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "22227");
        ExtForwFeatureImpl extForwFeature = new ExtForwFeatureImpl(basicService, ssStatus, forwardedToNumber, forwardedToSubaddress, forwardingOptions,
                noReplyConditionTime, extensionContainer, longForwardedToNumber);
        forwardingFeatureList.add(extForwFeature);
        ExtForwInfoImpl forwardingInfo = new ExtForwInfoImpl(ssCode, forwardingFeatureList, extensionContainer);
        ExtSSInfoImpl extSSInfo = new ExtSSInfoImpl(forwardingInfo);
        provisionedSS.add(extSSInfo);

        // odbData
        ODBGeneralDataImpl oDBGeneralData = new ODBGeneralDataImpl(false, true, false, true, false, true, false, true, false, true, false, true, false, true,
                false, true, false, true, false, true, false, true, false, true, false, true, false, true, false);
        ODBHPLMNDataImpl odbHplmnData = new ODBHPLMNDataImpl(false, true, false, true);
        ODBDataImpl odbData = new ODBDataImpl(oDBGeneralData, odbHplmnData, extensionContainer);

        // roamingRestrictionDueToUnsupportedFeature
        boolean roamingRestrictionDueToUnsupportedFeature = true;

        // regionalSubscriptionData
        ArrayList<ZoneCodeImpl> regionalSubscriptionData = new ArrayList<ZoneCodeImpl>();
        ZoneCodeImpl zoneCode = new ZoneCodeImpl(2);
        regionalSubscriptionData.add(zoneCode);

        // vbsSubscriptionData
        ArrayList<VoiceBroadcastDataImpl> vbsSubscriptionData = new ArrayList<VoiceBroadcastDataImpl>();
        GroupIdImpl groupId = new GroupIdImpl("4");
        boolean broadcastInitEntitlement = true;
        LongGroupIdImpl longGroupId = new LongGroupIdImpl("5");
        VoiceBroadcastDataImpl voiceBroadcastData = new VoiceBroadcastDataImpl(groupId, broadcastInitEntitlement, extensionContainer, longGroupId);
        vbsSubscriptionData.add(voiceBroadcastData);

        // vgcsSubscriptionData
        ArrayList<VoiceGroupCallDataImpl> vgcsSubscriptionData = new ArrayList<VoiceGroupCallDataImpl>();
        AdditionalSubscriptionsImpl additionalSubscriptions = new AdditionalSubscriptionsImpl(true, false, true);
        AdditionalInfoImpl additionalInfo = new AdditionalInfoImpl();
        additionalInfo.setBit(0);
        VoiceGroupCallDataImpl voiceGroupCallData = new VoiceGroupCallDataImpl(groupId, extensionContainer, additionalSubscriptions, additionalInfo,
                longGroupId);
        vgcsSubscriptionData.add(voiceGroupCallData);

        // start vlrCamelSubscriptionInfo
        TBcsmTriggerDetectionPoint tBcsmTriggerDetectionPoint = TBcsmTriggerDetectionPoint.tBusy;
        ArrayList<ExtBasicServiceCodeImpl> basicServiceCriteria = new ArrayList<ExtBasicServiceCodeImpl>();
        ExtBasicServiceCodeImpl basicServiceOne = new ExtBasicServiceCodeImpl(b);
        ExtBasicServiceCodeImpl basicServiceTwo = new ExtBasicServiceCodeImpl(extTeleservice);
        basicServiceCriteria.add(basicServiceOne);
        basicServiceCriteria.add(basicServiceTwo);

        ArrayList<CauseValueImpl> tCauseValueCriteria = new ArrayList<CauseValueImpl>();
        tCauseValueCriteria.add(new CauseValueImpl(7));
        tCauseValueCriteria.add(new CauseValueImpl(6));

        ISDNAddressStringImpl gsmSCFAddressOne = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "1122333");
        OBcsmCamelTDPDataImpl cind = new OBcsmCamelTDPDataImpl(OBcsmTriggerDetectionPoint.routeSelectFailure, 3, gsmSCFAddressOne,
                DefaultCallHandling.releaseCall, null);
        ArrayList<OBcsmCamelTDPDataImpl> lst = new ArrayList<OBcsmCamelTDPDataImpl>();
        lst.add(cind);

        OCSIImpl oCsi = new OCSIImpl(lst, null, 2, false, false);
        ArrayList<SSCodeImpl> ssEventList = new ArrayList<SSCodeImpl>();
        ssEventList.add(new SSCodeImpl(SupplementaryCodeValue.allCommunityOfInterestSS.getCode()));
        ISDNAddressStringImpl gsmSCFAddressTwo = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "22235");
        SSCamelDataImpl ssCamelData = new SSCamelDataImpl(ssEventList, gsmSCFAddressTwo, extensionContainer);
        boolean notificationToCSE = true;
        boolean csiActive = true;

        SSCSIImpl ssCsi = new SSCSIImpl(ssCamelData, extensionContainer, notificationToCSE, csiActive);

        ArrayList<OBcsmCamelTdpCriteriaImpl> oBcsmCamelTDPCriteriaList = new ArrayList<OBcsmCamelTdpCriteriaImpl>();
        OBcsmTriggerDetectionPoint oBcsmTriggerDetectionPoint = OBcsmTriggerDetectionPoint.collectedInfo;
        ISDNAddressStringImpl destinationNumberOne = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "22234");
        ISDNAddressStringImpl destinationNumberTwo = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "22235");
        ArrayList<ISDNAddressStringImpl> destinationNumberList = new ArrayList<ISDNAddressStringImpl>();
        destinationNumberList.add(destinationNumberOne);
        destinationNumberList.add(destinationNumberTwo);
        ArrayList<Integer> destinationNumberLengthList = new ArrayList<Integer>();
        destinationNumberLengthList.add(2);
        destinationNumberLengthList.add(4);
        destinationNumberLengthList.add(1);
        DestinationNumberCriteriaImpl destinationNumberCriteria = new DestinationNumberCriteriaImpl(MatchType.enabling, destinationNumberList,
                destinationNumberLengthList);

        CallTypeCriteria callTypeCriteria = CallTypeCriteria.forwarded;
        ArrayList<CauseValueImpl> oCauseValueCriteria = new ArrayList<CauseValueImpl>();
        oCauseValueCriteria.add(new CauseValueImpl(7));

        OBcsmCamelTdpCriteriaImpl oBcsmCamelTdpCriteria = new OBcsmCamelTdpCriteriaImpl(oBcsmTriggerDetectionPoint, destinationNumberCriteria,
                basicServiceCriteria, callTypeCriteria, oCauseValueCriteria, extensionContainer);
        oBcsmCamelTDPCriteriaList.add(oBcsmCamelTdpCriteria);

        boolean tifCsi = false;

        ArrayList<MMCodeImpl> mobilityTriggers = new ArrayList<MMCodeImpl>();
        Long serviceKey = 3L;
        ISDNAddressStringImpl gsmSCFAddress = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "22235");
        ;
        mobilityTriggers.add(new MMCodeImpl(MMCodeValue.GPRSAttach));
        mobilityTriggers.add(new MMCodeImpl(MMCodeValue.IMSIAttach));

        MCSIImpl mCsi = new MCSIImpl(mobilityTriggers, serviceKey, gsmSCFAddress, extensionContainer, notificationToCSE, csiActive);

        SMSTriggerDetectionPoint smsTriggerDetectionPoint = SMSTriggerDetectionPoint.smsCollectedInfo;
        DefaultSMSHandling defaultSMSHandling = DefaultSMSHandling.continueTransaction;

        ArrayList<SMSCAMELTDPDataImpl> smsCamelTdpDataList = new ArrayList<SMSCAMELTDPDataImpl>();
        SMSCAMELTDPDataImpl smsCAMELTDPData = new SMSCAMELTDPDataImpl(smsTriggerDetectionPoint, serviceKey, gsmSCFAddress, defaultSMSHandling,
                extensionContainer);
        smsCamelTdpDataList.add(smsCAMELTDPData);

        Integer camelCapabilityHandling = 8;

        SMSCSIImpl smsCsi = new SMSCSIImpl(smsCamelTdpDataList, camelCapabilityHandling, extensionContainer, notificationToCSE, csiActive);

        TBcsmCamelTDPDataImpl tBcsmCamelTDPData = new TBcsmCamelTDPDataImpl(TBcsmTriggerDetectionPoint.termAttemptAuthorized, 3, gsmSCFAddress,
                DefaultCallHandling.releaseCall, null);
        ArrayList<TBcsmCamelTDPDataImpl> tBcsmCamelTDPDatalst = new ArrayList<TBcsmCamelTDPDataImpl>();
        tBcsmCamelTDPDatalst.add(tBcsmCamelTDPData);
        TCSIImpl vtCsi = new TCSIImpl(tBcsmCamelTDPDatalst, null, 2, false, false);

        TBcsmCamelTdpCriteriaImpl tBcsmCamelTdpCriteria = new TBcsmCamelTdpCriteriaImpl(tBcsmTriggerDetectionPoint, basicServiceCriteria, tCauseValueCriteria);
        ArrayList<TBcsmCamelTdpCriteriaImpl> tBcsmCamelTdpCriteriaList = new ArrayList<TBcsmCamelTdpCriteriaImpl>();
        tBcsmCamelTdpCriteriaList.add(tBcsmCamelTdpCriteria);

        ISDNAddressStringImpl dialledNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "22234");

        DPAnalysedInfoCriteriumImpl dpAnalysedInfoCriterium = new DPAnalysedInfoCriteriumImpl(dialledNumber, 7, gsmSCFAddress,
                DefaultCallHandling.continueCall, extensionContainer);

        ArrayList<DPAnalysedInfoCriteriumImpl> dpAnalysedInfoCriteriaList = new ArrayList<DPAnalysedInfoCriteriumImpl>();
        dpAnalysedInfoCriteriaList.add(dpAnalysedInfoCriterium);

        DCSIImpl dCsi = new DCSIImpl(dpAnalysedInfoCriteriaList, 2, extensionContainer, true, true);

        SMSCSIImpl mtSmsCSI = new SMSCSIImpl(smsCamelTdpDataList, camelCapabilityHandling, extensionContainer, notificationToCSE, csiActive);
        ArrayList<MTsmsCAMELTDPCriteriaImpl> mtSmsCamelTdpCriteriaList = new ArrayList<MTsmsCAMELTDPCriteriaImpl>();
        ArrayList<MTSMSTPDUType> tPDUTypeCriterion = new ArrayList<MTSMSTPDUType>();
        tPDUTypeCriterion.add(MTSMSTPDUType.smsDELIVER);
        tPDUTypeCriterion.add(MTSMSTPDUType.smsSTATUSREPORT);

        MTsmsCAMELTDPCriteriaImpl mTsmsCAMELTDPCriteria = new MTsmsCAMELTDPCriteriaImpl(smsTriggerDetectionPoint, tPDUTypeCriterion);
        mtSmsCamelTdpCriteriaList.add(mTsmsCAMELTDPCriteria);

        VlrCamelSubscriptionInfoImpl vlrCamelSubscriptionInfo = new VlrCamelSubscriptionInfoImpl(oCsi, extensionContainer, ssCsi, oBcsmCamelTDPCriteriaList,
                tifCsi, mCsi, smsCsi, vtCsi, tBcsmCamelTdpCriteriaList, dCsi, mtSmsCSI, mtSmsCamelTdpCriteriaList);
        // end vlrCamelSubscriptionInfo

        // naeaPreferredCI
        NAEACICImpl naeaPreferredCIC = new NAEACICImpl(this.getNAEACICIData());
        NAEAPreferredCIImpl naeaPreferredCI = new NAEAPreferredCIImpl(naeaPreferredCIC, extensionContainer);

        // start gprsSubscriptionData
        int pdpContextId = 1;
        PDPTypeImpl pdpType = new PDPTypeImpl(this.getPDPTypeData());
        PDPAddressImpl pdpAddress = new PDPAddressImpl(this.getPDPAddressData());
        QoSSubscribedImpl qosSubscribed = new QoSSubscribedImpl(this.getQoSSubscribedData());
        boolean vplmnAddressAllowed = false;
        APNImpl apn = new APNImpl(this.getAPNData());
        ExtQoSSubscribedImpl extQoSSubscribed = new ExtQoSSubscribedImpl(this.getExtQoSSubscribedData());
        ChargingCharacteristicsImpl chargingCharacteristics = new ChargingCharacteristicsImpl(this.getChargingCharacteristicsData());
        Ext2QoSSubscribedImpl ext2QoSSubscribed = new Ext2QoSSubscribedImpl(this.getExt2QoSSubscribedData());
        Ext3QoSSubscribedImpl ext3QoSSubscribed = new Ext3QoSSubscribedImpl(this.getExt3QoSSubscribedData());
        Ext4QoSSubscribedImpl ext4QoSSubscribed = new Ext4QoSSubscribedImpl(2);
        APNOIReplacementImpl apnoiReplacement = new APNOIReplacementImpl(this.getAPNOIReplacementData());
        ExtPDPTypeImpl extpdpType = new ExtPDPTypeImpl(this.getExtPDPTypeData());
        PDPAddressImpl extpdpAddress = new PDPAddressImpl(this.getPDPAddressData2());
        
        SIPTOPermission sipToPermission = SIPTOPermission.siptoAllowed;
        LIPAPermission lipaPermission = LIPAPermission.lipaConditional;

        PDPContextImpl pdpContext = new PDPContextImpl(pdpContextId, pdpType, pdpAddress, qosSubscribed, vplmnAddressAllowed, apn, extensionContainer,
                extQoSSubscribed, chargingCharacteristics, ext2QoSSubscribed, ext3QoSSubscribed, ext4QoSSubscribed, apnoiReplacement, extpdpType,
                extpdpAddress, sipToPermission, lipaPermission);
        ArrayList<PDPContextImpl> gprsDataList = new ArrayList<PDPContextImpl>();
        gprsDataList.add(pdpContext);

        APNOIReplacementImpl apnOiReplacement = new APNOIReplacementImpl(this.getAPNOIReplacementData());
        GPRSSubscriptionDataImpl gprsSubscriptionData = new GPRSSubscriptionDataImpl(false, gprsDataList, extensionContainer, apnOiReplacement);
        // end gprsSubscriptionData

        // roamingRestrictedInSgsnDueToUnsupportedFeature
        boolean roamingRestrictedInSgsnDueToUnsupportedFeature = true;

        // networkAccessMode
        NetworkAccessMode networkAccessMode = NetworkAccessMode.packetAndCircuit;

        // start lsaInformation
        boolean completeDataListIncluded = true;
        LSAOnlyAccessIndicator lsaOnlyAccessIndicator = LSAOnlyAccessIndicator.accessOutsideLSAsRestricted;
        ArrayList<LSADataImpl> lsaDataList = new ArrayList<LSADataImpl>();
        LSAIdentityImpl lsaIdentity = new LSAIdentityImpl(this.getDataLSAIdentity());
        LSAAttributesImpl lsaAttributes = new LSAAttributesImpl(5);
        boolean lsaActiveModeIndicator = true;
        LSADataImpl lsaData = new LSADataImpl(lsaIdentity, lsaAttributes, lsaActiveModeIndicator, extensionContainer);
        lsaDataList.add(lsaData);
        LSAInformationImpl lsaInformation = new LSAInformationImpl(completeDataListIncluded, lsaOnlyAccessIndicator, lsaDataList, extensionContainer);
        // end lsaInformation

        // lmuIndicator
        boolean lmuIndicator = true;

        // start lcsInformation
        ArrayList<ISDNAddressStringImpl> gmlcList = new ArrayList<ISDNAddressStringImpl>();
        ISDNAddressStringImpl isdnAddressString = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "22235");
        gmlcList.add(isdnAddressString);

        ArrayList<LCSPrivacyClassImpl> lcsPrivacyExceptionList = new ArrayList<LCSPrivacyClassImpl>();
        NotificationToMSUser notificationToMSUser = NotificationToMSUser.locationNotAllowed;
        ArrayList<ExternalClientImpl> externalClientList = new ArrayList<ExternalClientImpl>();
        ISDNAddressStringImpl externalAddress = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "22228");
        LCSClientExternalIDImpl clientIdentity = new LCSClientExternalIDImpl(externalAddress, extensionContainer);
        GMLCRestriction gmlcRestriction = GMLCRestriction.gmlcList;
        ExternalClientImpl externalClient = new ExternalClientImpl(clientIdentity, gmlcRestriction, notificationToMSUser, extensionContainer);
        externalClientList.add(externalClient);

        ArrayList<LCSClientInternalID> plmnClientList = new ArrayList<LCSClientInternalID>();
        LCSClientInternalID lcsClientInternalIdOne = LCSClientInternalID.broadcastService;
        LCSClientInternalID lcsClientInternalIdTwo = LCSClientInternalID.oandMHPLMN;
        plmnClientList.add(lcsClientInternalIdOne);
        plmnClientList.add(lcsClientInternalIdTwo);

        ArrayList<ExternalClientImpl> extExternalClientList = new ArrayList<ExternalClientImpl>();
        extExternalClientList.add(externalClient);

        ArrayList<ServiceTypeImpl> serviceTypeList = new ArrayList<ServiceTypeImpl>();
        int serviceTypeIdentity = 1;
        ServiceTypeImpl serviceType = new ServiceTypeImpl(serviceTypeIdentity, gmlcRestriction, notificationToMSUser, extensionContainer);
        serviceTypeList.add(serviceType);

        SSCodeImpl ssCodeTwo = new SSCodeImpl(SupplementaryCodeValue.allCommunityOfInterestSS);
        SSCodeImpl ssCodeThree = new SSCodeImpl(SupplementaryCodeValue.allForwardingSS);
        SSCodeImpl ssCodeFour = new SSCodeImpl(SupplementaryCodeValue.allLineIdentificationSS);
        LCSPrivacyClassImpl lcsPrivacyClassOne = new LCSPrivacyClassImpl(ssCode, ssStatus, notificationToMSUser, externalClientList, plmnClientList,
                extensionContainer, extExternalClientList, serviceTypeList);
        LCSPrivacyClassImpl lcsPrivacyClassTwo = new LCSPrivacyClassImpl(ssCodeTwo, ssStatus, notificationToMSUser, externalClientList, plmnClientList,
                extensionContainer, extExternalClientList, serviceTypeList);
        LCSPrivacyClassImpl lcsPrivacyClassThree = new LCSPrivacyClassImpl(ssCodeThree, ssStatus, notificationToMSUser, externalClientList, plmnClientList,
                extensionContainer, extExternalClientList, serviceTypeList);
        LCSPrivacyClassImpl lcsPrivacyClassFour = new LCSPrivacyClassImpl(ssCodeFour, ssStatus, notificationToMSUser, externalClientList, plmnClientList,
                extensionContainer, extExternalClientList, serviceTypeList);

        lcsPrivacyExceptionList.add(lcsPrivacyClassOne);
        lcsPrivacyExceptionList.add(lcsPrivacyClassTwo);
        lcsPrivacyExceptionList.add(lcsPrivacyClassThree);
        lcsPrivacyExceptionList.add(lcsPrivacyClassFour);

        ArrayList<MOLRClassImpl> molrList = new ArrayList<MOLRClassImpl>();
        MOLRClassImpl molrClass = new MOLRClassImpl(ssCode, ssStatus, extensionContainer);
        molrList.add(molrClass);

        ArrayList<LCSPrivacyClassImpl> addLcsPrivacyExceptionList = new ArrayList<LCSPrivacyClassImpl>();
        addLcsPrivacyExceptionList.add(lcsPrivacyClassOne);

        LCSInformationImpl lcsInformation = new LCSInformationImpl(gmlcList, lcsPrivacyExceptionList, molrList, addLcsPrivacyExceptionList);
        // end lcsInformation

        // start istAlertTimer
        Integer istAlertTimer = 21;

        // superChargerSupportedInHLR
        AgeIndicatorImpl superChargerSupportedInHLR = new AgeIndicatorImpl(this.getAgeIndicatorData());

        // start mcSsInfo
        MCSSInfoImpl mcSsInfo = new MCSSInfoImpl(ssCode, ssStatus, 2, 4, extensionContainer);
        // end mcSsInfo

        // start csAllocationRetentionPriority
        CSAllocationRetentionPriorityImpl csAllocationRetentionPriority = new CSAllocationRetentionPriorityImpl(4);

        // start sgsnCamelSubscriptionInfo
        GPRSTriggerDetectionPoint gprsTriggerDetectionPoint = GPRSTriggerDetectionPoint.attachChangeOfPosition;
        DefaultGPRSHandling defaultSessionHandling = DefaultGPRSHandling.releaseTransaction;
        GPRSCamelTDPDataImpl gprsCamelTDPData = new GPRSCamelTDPDataImpl(gprsTriggerDetectionPoint, serviceKey, gsmSCFAddress, defaultSessionHandling,
                extensionContainer);
        ArrayList<GPRSCamelTDPDataImpl> gprsCamelTDPDataList = new ArrayList<GPRSCamelTDPDataImpl>();
        gprsCamelTDPDataList.add(gprsCamelTDPData);
        GPRSCSIImpl gprsCsi = new GPRSCSIImpl(gprsCamelTDPDataList, camelCapabilityHandling, extensionContainer, notificationToCSE, csiActive);
        SMSCSIImpl moSmsCsi = new SMSCSIImpl(smsCamelTdpDataList, camelCapabilityHandling, extensionContainer, notificationToCSE, csiActive);
        SMSCSIImpl mtSmsCsi = new SMSCSIImpl(smsCamelTdpDataList, camelCapabilityHandling, extensionContainer, false, false);
        MGCSIImpl mgCsi = new MGCSIImpl(mobilityTriggers, 3, gsmSCFAddress, extensionContainer, true, true);
        SGSNCAMELSubscriptionInfoImpl sgsnCamelSubscriptionInfo = new SGSNCAMELSubscriptionInfoImpl(gprsCsi, moSmsCsi, extensionContainer, mtSmsCsi,
                mtSmsCamelTdpCriteriaList, mgCsi);
        // end sgsnCamelSubscriptionInfo

        // accessRestrictionData
        AccessRestrictionDataImpl accessRestrictionData = new AccessRestrictionDataImpl(false, true, false, true, false, true);

        // icsIndicator
        Boolean icsIndicator = Boolean.TRUE;

        // start epsSubscriptionData
        Integer rfspId = 4;
        AMBRImpl ambr = new AMBRImpl(2, 4, extensionContainer);
        int defaultContext = 2;
        ArrayList<APNConfigurationImpl> ePSDataList = new ArrayList<APNConfigurationImpl>();
        int contextId = 1;
        PDNTypeImpl pDNType = new PDNTypeImpl(PDNTypeValue.IPv4);
        PDPAddressImpl servedPartyIPIPv4Address = new PDPAddressImpl(this.getPDPAddressData());
        QoSClassIdentifier qoSClassIdentifier = QoSClassIdentifier.QCI_1;
        AllocationRetentionPriorityImpl allocationRetentionPriority = new AllocationRetentionPriorityImpl(1, Boolean.TRUE, Boolean.TRUE, extensionContainer);
        EPSQoSSubscribedImpl ePSQoSSubscribed = new EPSQoSSubscribedImpl(qoSClassIdentifier, allocationRetentionPriority, extensionContainer);
        PDPAddressImpl pdnGwIpv4Address = new PDPAddressImpl(this.getPDPAddressData());
        PDPAddressImpl pdnGwIpv6Address = new PDPAddressImpl(this.getPDPAddressData());
        FQDNImpl pdnGwName = new FQDNImpl(this.getFQDNData());
        PDNGWIdentityImpl pdnGwIdentity = new PDNGWIdentityImpl(pdnGwIpv4Address, pdnGwIpv6Address, pdnGwName, extensionContainer);
        PDNGWAllocationType pdnGwAllocationType = PDNGWAllocationType._dynamic;
        SpecificAPNInfoImpl specificAPNInfo = new SpecificAPNInfoImpl(apn, pdnGwIdentity, extensionContainer);
        ArrayList<SpecificAPNInfoImpl> specificAPNInfoList = new ArrayList<SpecificAPNInfoImpl>();
        specificAPNInfoList.add(specificAPNInfo);
        PDPAddressImpl servedPartyIPIPv6Address = new PDPAddressImpl(this.getPDPAddressData());
        SIPTOPermission siptoPermission = SIPTOPermission.siptoAllowed;
        APNConfigurationImpl APNConfiguration = new APNConfigurationImpl(contextId, pDNType, servedPartyIPIPv4Address, apn, ePSQoSSubscribed, pdnGwIdentity,
                pdnGwAllocationType, vplmnAddressAllowed, chargingCharacteristics, ambr, specificAPNInfoList, extensionContainer, servedPartyIPIPv6Address,
                apnOiReplacement, siptoPermission, lipaPermission);
        ePSDataList.add(APNConfiguration);
        APNConfigurationProfileImpl apnConfigurationProfile = new APNConfigurationProfileImpl(defaultContext, completeDataListIncluded, ePSDataList,
                extensionContainer);
        ISDNAddressStringImpl stnSr = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "22228");
        boolean mpsCSPriority = true;
        boolean mpsEPSPriority = true;
        EPSSubscriptionDataImpl epsSubscriptionData = new EPSSubscriptionDataImpl(apnOiReplacement, rfspId, ambr, apnConfigurationProfile, stnSr,
                extensionContainer, mpsCSPriority, mpsEPSPriority);
        // end epsSubscriptionData

        // start csgSubscriptionDataList
        ArrayList<CSGSubscriptionDataImpl> csgSubscriptionDataList = new ArrayList<CSGSubscriptionDataImpl>();       
        CSGIdImpl csgId = new CSGIdImpl();
        csgId.setBit(0);
        csgId.setBit(26);
        TimeImpl expirationDate = new TimeImpl(this.getTimeData());
        ArrayList<APNImpl> lipaAllowedAPNList = new ArrayList<APNImpl>();
        lipaAllowedAPNList.add(apn);
        CSGSubscriptionDataImpl csgSubscriptionData = new CSGSubscriptionDataImpl(csgId, expirationDate, extensionContainer, lipaAllowedAPNList);
        csgSubscriptionDataList.add(csgSubscriptionData);
        // end csgSubscriptionDataList

        // ueReachabilityRequestIndicator
        boolean ueReachabilityRequestIndicator = true;

        // sgsnNumber
        ISDNAddressStringImpl sgsnNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "22228");

        // mmeName
        DiameterIdentityImpl mmeName = new DiameterIdentityImpl(this.getDiameterIdentity());

        // subscribedPeriodicRAUTAUtimer
        Long subscribedPeriodicRAUTAUtimer = 2L;

        // vplmnLIPAAllowed
        boolean vplmnLIPAAllowed = true;

        // mdtUserConsent
        Boolean mdtUserConsent = Boolean.TRUE;

        // subscribedPeriodicLAUtimer
        Long subscribedPeriodicLAUtimer = 2L;

        InsertSubscriberDataRequestImpl prim = new InsertSubscriberDataRequestImpl(3, imsi, msisdn, category, subscriberStatus, bearerServiceList,
                teleserviceList, provisionedSS, odbData, roamingRestrictionDueToUnsupportedFeature, regionalSubscriptionData, vbsSubscriptionData,
                vgcsSubscriptionData, vlrCamelSubscriptionInfo, extensionContainer, naeaPreferredCI, gprsSubscriptionData,
                roamingRestrictedInSgsnDueToUnsupportedFeature, networkAccessMode, lsaInformation, lmuIndicator, lcsInformation, istAlertTimer,
                superChargerSupportedInHLR, mcSsInfo, csAllocationRetentionPriority, sgsnCamelSubscriptionInfo, chargingCharacteristics, accessRestrictionData,
                icsIndicator, epsSubscriptionData, csgSubscriptionDataList, ueReachabilityRequestIndicator, sgsnNumber, mmeName, subscribedPeriodicRAUTAUtimer,
                vplmnLIPAAllowed, mdtUserConsent, subscribedPeriodicLAUtimer);

        ByteBuf buffer=parser.encode(prim);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        byte[] rawData=this.getData();        
        assertTrue(Arrays.equals(encodedData, rawData));

        prim = new InsertSubscriberDataRequestImpl(2, imsi, msisdn, category, subscriberStatus, bearerServiceList, teleserviceList, provisionedSS, odbData,
                roamingRestrictionDueToUnsupportedFeature, regionalSubscriptionData, vbsSubscriptionData, vgcsSubscriptionData, vlrCamelSubscriptionInfo);
        buffer=parser.encode(prim);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        rawData=this.getData1();
        assertTrue(Arrays.equals(encodedData, rawData));
    }
}
