package jcspDemos.net2.classLoading.send;

import jcsp.lang.CSProcess;
import jcsp.net2.NetChannel;
import jcsp.net2.NetChannelOutput;
import jcsp.net2.Node;
import jcsp.net2.mobile.CodeLoadingChannelFilter;
import jcsp.net2.tcpip.TCPIPNodeAddress;

public class RunSender {
    public static void main(String[] args) {
        String sendIP = "192.168.1.69";
        String receiveIP = "192.168.1.176";
        TCPIPNodeAddress sendAddress = new TCPIPNodeAddress(sendIP, 1000);
        Node.getInstance().init(sendAddress);

        TCPIPNodeAddress po1IPAddress = new TCPIPNodeAddress(receiveIP, 1000);
//        NetChannelOutput toNext = NetChannel.one2net(po1IPAddress, 1);
        NetChannelOutput toNext = NetChannel.one2net(po1IPAddress, 1, new CodeLoadingChannelFilter.FilterTX());

        CSProcess send = new Sender(toNext);

        send.run();

    }
}
