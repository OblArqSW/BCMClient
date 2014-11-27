/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bcmclient.client;

import com.bcmclient.classes.Usuario;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import java.util.List;
import java.util.Properties;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import org.apache.log4j.Logger;

/**
 *
 * @author Nacho
 */
@Stateless
@LocalBean
public class WSClient {

    private final Logger log = Logger.getLogger(WSClient.class.getName());

    @Resource(lookup = "jms/ConnectionFactory")
    private ConnectionFactory connectionFactory;

    //@Resource(lookup = "jms/Queue")
    //private Queue queue;

    public WSClient() {
        

    }

    public boolean callWS() {
        Client client = ClientBuilder.newClient();
        WebTarget myResource = client.target("http://localhost:8080/BCMShippingWAR/services/usuario/ext/Idffa061570ed304d915377b73558ef66c");
        String response = myResource.request(MediaType.APPLICATION_JSON)
                .header("Content-Type", "application/json").get(String.class);

        return processUserList(response);
    }

    private boolean processUserList(String list) {
        String mensaje = "Hola";
        boolean retorno = false;
        Gson gson = new GsonBuilder().create();
        //Usuario usu = null;
        List<Usuario> lstUsu = null;
        try {
            lstUsu = gson.fromJson(list, new TypeToken<List<Usuario>>() {
            }.getType());
            //usu = gson.fromJson(list, Usuario.class);
            //Descomposición del JSON en los distintos atributos.
            for (Usuario usu : lstUsu) {
                String nombre = usu.getNombre();
                String alias = usu.getAliasCuenta();
                String cola = usu.getCola();
                mensaje = "Bienvenido " + nombre;
                if (usu != null)
                    retorno = enviarMensaje(nombre, alias, cola, mensaje);
            }
        } catch (JsonSyntaxException e) {
            //Cambiar mensaje.
            log.error(". Excepción: " + e.getMessage());
        }

        return retorno;
    }
    
    private boolean enviarMensaje(String nombre, String alias, String nombreCola, String mensaje) {
        boolean retorno = false;
        try {
            //Seteo las Properties para el contexto
            Properties props = new Properties();
            props.setProperty("java.naming.factory.initial",
                    "com.sun.enterprise.naming.SerialInitContextFactory");
            props.setProperty("java.naming.factory.url.pkgs", "com.sun.enterprise.naming");
            props.setProperty("java.naming.factory.state",
                    "com.sun.corba.ee.impl.presentation.rmi.JNDIStateFactoryImpl");
            props.setProperty("org.omg.CORBA.ORBInitialHost", "localhost");
            props.setProperty("org.omg.CORBA.ORBInitialPort", "3700");
            //Creo el Contexto para obtener los recursos del servidor
            InitialContext ic = new InitialContext(props);
            Queue queue = (Queue) ic.lookup(nombreCola);
            System.out.println("Mensaje a usuario - " + alias);
            try {
                Connection connection = connectionFactory.createConnection();
                Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
                MessageProducer producer = session.createProducer(queue);
                TextMessage message = session.createTextMessage();
                message.setText(mensaje);
                producer.send(message);
                session.close();
                connection.close();
                System.out.println("Mensaje a usuario - " + alias + " - Mensaje enviado!");
                retorno = true;
            } catch (JMSException ex) {
                System.out.println("Mensaje a usuario - " + alias + " - Error enviando mensaje");
                ex.printStackTrace();
            }
        } catch (Exception e) {

        }
        return retorno;
    }

}
