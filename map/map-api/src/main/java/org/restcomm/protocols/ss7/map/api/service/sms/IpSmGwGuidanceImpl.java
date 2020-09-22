package org.restcomm.protocols.ss7.map.api.service.sms;

import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainerImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;

/**
 * @author eva ogallar
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class IpSmGwGuidanceImpl {
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=2,constructed=false,index=0)
	private ASNInteger minimumDeliveryTimeValue;
    
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=2,constructed=false,index=1)
	private ASNInteger recommendedDeliveryTimeValue;
	
    private MAPExtensionContainerImpl extensionContainer;

    public IpSmGwGuidanceImpl() {
    }

    public IpSmGwGuidanceImpl(int minimumDeliveryTimeValue, int recommendedDeliveryTimeValue, MAPExtensionContainerImpl extensionContainer) {
        this.extensionContainer = extensionContainer;
                
        this.minimumDeliveryTimeValue = new ASNInteger();
        this.minimumDeliveryTimeValue.setValue((long)minimumDeliveryTimeValue & 0x0FFFFFFFFL);
        this.recommendedDeliveryTimeValue = new ASNInteger();
        this.recommendedDeliveryTimeValue.setValue((long)recommendedDeliveryTimeValue & 0x0FFFFFFFFL);
    }

    public MAPExtensionContainerImpl getExtensionContainer() {
        return this.extensionContainer;
    }

    public int getMinimumDeliveryTimeValue() {
    	if(minimumDeliveryTimeValue==null)
    		return 0;
    	
        return minimumDeliveryTimeValue.getValue().intValue();
    }

    public int getRecommendedDeliveryTimeValue() {
    	if(recommendedDeliveryTimeValue==null)
    		return 0;
    	
        return recommendedDeliveryTimeValue.getValue().intValue();
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
