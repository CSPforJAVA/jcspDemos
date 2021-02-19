package jcspDemos.net2.classLoading.send;

import java.io.Serializable;

public class ObjectB implements Serializable {
    int bData;
    public ObjectB(int b){
        bData = b;
    }

    @Override
    public String toString() {
        return "ObjectB{" +
                "bData=" + bData +
                '}';
    }
}

