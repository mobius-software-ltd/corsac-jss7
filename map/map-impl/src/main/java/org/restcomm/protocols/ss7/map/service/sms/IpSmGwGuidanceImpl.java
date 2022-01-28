package org.restcomm.protocols.ss7.map.service.sms;

import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.service.sms.IpSmGwGuidance;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;

/**
 * @author eva ogallar
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class IpSmGwGuidanceImpl implements IpSmGwGuidance {
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=2,constructed=false,index=0)
	private ASNInteger minimumDeliveryTimeValue;
    
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=2,constructed=false,index=1)
	private ASNInteger recommendedDeliveryTimeValue;
	
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,index=-1,defaultImplementation = MAPExtensionContainerImpl.class)
	private MAPExtensionContainer extensionContainer;

    public IpSmGwGuidanceImpl() {
    }

    public IpSmGwGuidanceImpl(int minimumDeliveryTimeValue, int recommendedDeliveryTimeValue, MAPExtensionContainer extensionContainer) {
        this.extensionContainer = extensionContainer;
                
        this.minimumDeliveryTimeValue = new ASNInteger(minimumDeliveryTimeValue);
        this.recommendedDeliveryTimeValue = new ASNInteger(recommendedDeliveryTimeValue);        
    }

    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    public int getMinimumDeliveryTimeValue() {
    	if(minimumDeliveryTimeValue==null || minimumDeliveryTimeValue.getValue()==null)
    		return 0;
    	
        return minimumDeliveryTimeValue.getIntValue();
    }

    public int getRecommendedDeliveryTimeValue() {
    	if(recommendedDeliveryTimeValue==null || recommendedDeliveryTimeValue.getValue()==null)
    		return 0;
    	
        return recommendedDeliveryTimeValue.getIntValue();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("IpSmGwGuidance [");

        if(this.minimumDeliveryTimeValue!=null) {
	        sb.append("minimumDeliveryTimeValue=");
	        sb.append(minimumDeliveryTimeValue.getValue());
	        sb.append(", ");
        }
        
        if(recommendedDeliveryTimeValue!=null) {
	        sb.append("recommendedDeliveryTimeValue=");
	        sb.append(recommendedDeliveryTimeValue.getValue());
	        sb.append(", ");
        }
        
        if (this.extensionContainer != null) {
            sb.append(", extensionContainer=");
            sb.append(this.extensionContainer.toString());
        }

        sb.append("]");

        return sb.toString();
    }
}
