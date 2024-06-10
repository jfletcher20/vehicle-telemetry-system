package edu.unizg.foi.nwtis.jfletcher20.vjezba_08_dz_3.jms;

import jakarta.annotation.Resource;
import jakarta.ejb.Stateless;
import jakarta.jms.Connection;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSException;
import jakarta.jms.MessageProducer;
import jakarta.jms.ObjectMessage;
import jakarta.jms.Queue;
import jakarta.jms.Session;

@Stateless
public class PrijenosnikKazni {

  @Resource(mappedName = "jms/nwtisCF")
  private ConnectionFactory connectionFactory;
  @Resource(mappedName = "jms/nwtisQ")
  private Queue queue;

  // TODO: should accept Poruka object, not string
  public boolean novaKazna(String poruka) {
    boolean status = true;

    try {
      Connection connection = connectionFactory.createConnection();
      Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
      MessageProducer messageProducer = session.createProducer(queue);
      ObjectMessage message = session.createObjectMessage();
      //TODO: erase later
      System.out.println("JMS šalje poruku - PrijenosnikKazni novaKazna: " + poruka);
      message.setObject(poruka);
      messageProducer.send(message);
      messageProducer.close();
      connection.close();
    } catch (JMSException ex) {
      ex.printStackTrace();
      status = false;
    }
    return status;
  }
}