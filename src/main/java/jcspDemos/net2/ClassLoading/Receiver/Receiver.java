package Receiver;

import jcsp.lang.CSProcess;
import jcsp.net2.NetChannelInput;

public class Receiver implements CSProcess {

    NetChannelInput inChan;

    public Receiver(NetChannelInput inChan) {
        this.inChan = inChan;
    }

    @Override
    public void run() {
        System.out.println("Receiver starting");
        for ( int i = 1; i<= 5; i++){
            Object aObject =  inChan.read();
            System.out.println("Received " + aObject.toString());
        }
        System.out.println("Receiver terminated");

    }
}
