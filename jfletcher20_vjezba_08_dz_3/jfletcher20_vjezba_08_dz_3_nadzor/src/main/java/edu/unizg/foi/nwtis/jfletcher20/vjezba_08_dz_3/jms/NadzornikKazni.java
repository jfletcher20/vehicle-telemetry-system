package edu.unizg.foi.nwtis.jfletcher20.vjezba_08_dz_3.jms;

import edu.unizg.foi.nwtis.jfletcher20.vjezba_08_dz_3.websocket.WebSocketPosluzitelj;
import jakarta.ejb.ActivationConfigProperty;
import jakarta.ejb.MessageDriven;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;
import jakarta.jms.ObjectMessage;

@MessageDriven(mappedName = "jms/nwtisQ", activationConfig = {
    @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")})
public class NadzornikKazni implements MessageListener {

  public NadzornikKazni() {}

  public void onMessage(Message message) {
    ObjectMessage msg = null;

    if (message instanceof ObjectMessage) {
      try {
        msg = (ObjectMessage) message;
        System.out.println("Stigla poruka:" + message.getJMSMessageID() + " "
            + new java.util.Date(message.getJMSTimestamp()));
        WebSocketPosluzitelj.send(msg.getObject().toString()); // TODO: jmeter testing throws errors still for some reason
      } catch (JMSException ex) {
        ex.printStackTrace();
      }
    }
  }
}