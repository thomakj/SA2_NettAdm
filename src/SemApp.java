import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;

import javax.xml.namespace.QName;
import javax.xml.soap.*;

public class SemApp {

	private static SOAPBody sendSoapMessage(String endpoint, String args0, String args1) throws SOAPException{
		SOAPFactory soapFactory =  SOAPFactory.newInstance();
		SOAPMessage message = MessageFactory.newInstance().createMessage();
		SOAPHeader header = message.getSOAPHeader();
		SOAPBody body = message.getSOAPBody();
		header.detachNode();
		
		if(args0 != null && args1 != null){

			Name bodyName = soapFactory.createName(
					"sendValueResponse", "ns",
					"http://managementserver.com");
			SOAPBodyElement bodyElement = body.addBodyElement(bodyName);

			Name reqElement1 = soapFactory.createName("args0");
			SOAPElement symbol1 = bodyElement.addChildElement(reqElement1);
			symbol1.addTextNode(args0);
			
			Name reqElement2 = soapFactory.createName("args1");
			SOAPElement symbol2 = bodyElement.addChildElement(reqElement2);
			symbol2.addTextNode(args1);

		}
		SOAPEnvelope envelope = message.getSOAPPart().getEnvelope();
		envelope.setAttribute("namespace","http://managementserver.com");

		SOAPConnection connection = SOAPConnectionFactory.newInstance().createConnection();
		SOAPMessage response = connection.call(message, endpoint);

		connection.close();

		SOAPBody responseBody = response.getSOAPBody();

		return responseBody;
	}
	
	private static Iterator<SOAPElement> getObjectWithRequestId()throws SOAPException{
		String endpoint = "http://ttm4128.item.ntnu.no:8080/axis2/services/ManagementServer/getObject";
		SOAPBody responseBody = sendSoapMessage(endpoint,null,null);
		if(responseBody.getFault()!=null){
			System.out.println(responseBody.getFault().getFaultString());
			return null;
		}
		SOAPBodyElement responseElement = (SOAPBodyElement)responseBody.getChildElements().next();
		return responseElement.getChildElements();
		
	}
	
	private static String SendValue(String reqId,String value) throws SOAPException{
		String endpoint = "http://ttm4128.item.ntnu.no:8080/axis2/services/ManagementServer/sendValue";
		SOAPBody responseBody1 = sendSoapMessage(endpoint,reqId,value);
		SOAPBodyElement responseElement1 = (SOAPBodyElement)responseBody1.getChildElements().next();
		Iterator<SOAPElement> returns1 = responseElement1.getChildElements();
		if(responseBody1.getFault()!=null){
			System.out.println(responseBody1.getFault().getFaultString());
			return responseBody1.getFault().getFaultString();
		}else{
			SOAPElement element1 = (SOAPElement)returns1.next();
			return element1.getValue();
		}
	}
	
	public static void main(String[] args) throws SOAPException {
		System.out.println("Asks the server for an objectID and a request ID");
		Iterator<SOAPElement> returns = getObjectWithRequestId();
		
		if(returns != null){
			SOAPElement cnmpObj = (SOAPElement)returns.next();
			SOAPElement reqId = (SOAPElement)returns.next();
			System.out.println("Response from server -> CNMP objID: <"+cnmpObj.getValue()+"> Request ID: <"+reqId.getValue()+">");
			System.out.println("");
			System.out.println("Don't worry about the red text!");
			Jena2 jena = new Jena2();
			System.out.println("");
			System.out.println("Translates the CNMP objId to snmp OID....");
			String snmpOid = jena.getMibOid(cnmpObj.getValue());
			System.out.println("<"+cnmpObj.getValue()+"> is equal to <"+ snmpOid+">");
			System.out.println("");
			System.out.println("Executing snmpGet for OID: <"+snmpOid+">");
			ExecuteSnmp res = new ExecuteSnmp();
			
			String snmpResult = res.snmpGet(snmpOid);
			System.out.println("Result from snmpGet: <"+snmpResult+ "> of the OID: <"+snmpOid+">");
			String value = snmpResult.split("= ")[1];
			System.out.println("Sending value: <"+value+"> to Server");
			
			String ackFromSendValue = SendValue(reqId.getValue(), value);
			System.out.println("Ack from sendValue: \""+ackFromSendValue+"\"");
			
		}








	}

	private static String getXmlFromSOAPMessage(SOAPMessage msg) throws SOAPException, IOException {
		ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
		msg.writeTo(byteArrayOS);
		return new String(byteArrayOS.toByteArray());
	}



}
