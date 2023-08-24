package Middleware.MobileRecharge.Request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "xmltt")
public class XmlBodyTT extends ReqBodyController {
    @JacksonXmlProperty(localName = "topupid")
    private String topupid;
    @JacksonXmlProperty(localName = "sender")
    private String sender;
    @JacksonXmlProperty(localName = "target")
    private String target;
    @JacksonXmlProperty(localName = "topup")
    private String topup;

    public XmlBodyTT(String transactionid, String agent, String paymentto, String amount) {
        this.topupid = transactionid;
        this.sender = agent;
        this.target = paymentto;
        this.topup = amount;
    }

    @Override
    @JsonIgnore
    public String getTransactionID() {
        return topupid;
    }

    @Override
    @JsonIgnore
    public String getSender() {
        return sender;
    }

    @Override
    @JsonIgnore
    public String getReceiver() {
        return target;
    }

    @Override
    @JsonIgnore
    public String getAmount() {
        return topup;
    }
}
