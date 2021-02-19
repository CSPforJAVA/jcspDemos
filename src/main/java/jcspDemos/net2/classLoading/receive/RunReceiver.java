package jcspDemos.net2.classLoading.receive;

import jcsp.lang.CSProcess;
import jcsp.net2.NetChannel;
import jcsp.net2.NetChannelInput;
import jcsp.net2.Node;
import jcsp.net2.mobile.CodeLoadingChannelFilter;
import jcsp.net2.tcpip.TCPIPNodeAddress;

public class RunReceiver {
    public static void main(String[] args) {
        String sendIP = "192.168.1.69";
        String receiveIP = "192.168.1.176";
        TCPIPNodeAddress po1IPAddress = new TCPIPNodeAddress(receiveIP, 1000);
        Node.getInstance().init(po1IPAddress);

//        NetChannelInput fromSend = NetChannel.numberedNet2One(1);
        NetChannelInput fromSend = NetChannel.numberedNet2One(1, new CodeLoadingChannelFilter.FilterRX());

        CSProcess receive = new Receiver(fromSend);

        receive.run();
    }

}
