package Sender;

import jcsp.lang.CSProcess;
import jcsp.net2.NetChannelOutput;

public class Sender implements CSProcess {
    NetChannelOutput outChan;

    public Sender(NetChannelOutput outChan) {
        this.outChan = outChan;
    }

    @Override
    public void run() {
        System.out.println("Sender starting");
        for ( int i = 1; i<= 5; i++){
            ObjectA aObject = new ObjectA(i, i);
            outChan.write(aObject);
            System.out.println("Sent " + aObject.toString());
        }
        System.out.println("Sender terminated");
    }
}
