package Middleware.MobileRecharge.Request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "param")
public class XmlBodyRobi extends ReqBodyController{
    @JacksonXmlProperty(localName = "transactionid")
    private String transactionid;
    @JacksonXmlProperty(localName = "paymentfrom")
    private String paymentfrom;
    @JacksonXmlProperty(localName = "topupacc")
    private String topupacc;
    @JacksonXmlProperty(localName = "amount")
    private String amount;

    public XmlBodyRobi(String transactionid, String agent, String paymentto, String amount) {
        this.transactionid = transactionid;
        this.paymentfrom = agent;
        this.topupacc = paymentto;
        this.amount = amount;
    }

    @Override
    @JsonIgnore
    public String getTransactionID() {
        return transactionid;
    }

    @Override
    @JsonIgnore
    public String getSender() {
        return paymentfrom;
    }

    @Override
    @JsonIgnore
    public String getReceiver() {
        return topupacc;
    }

    @Override
    @JsonIgnore
    public String getAmount() {
        return amount;
    }
}
