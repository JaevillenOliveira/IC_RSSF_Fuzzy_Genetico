package server;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jaevillen
 */
public class TcpConnection {
    
    ServerSocket server;
    Socket client;

    public TcpConnection() throws IOException {
        this.server = new ServerSocket(3322);
        System.out.println("Server started at 3322");
    }
     
   /**
     *
     */
    public void waitCon() throws IOException{
        while (true){
            this.client = server.accept();
            
            this.readMessage(client);
        }
    }
    
    public void readMessage(Socket client) throws IOException{
        client.getInputStream();

    }
    
}
