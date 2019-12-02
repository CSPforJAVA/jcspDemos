package Sender;

import java.io.Serializable;

public class ObjectA implements Serializable {
    int aData;
    ObjectB bObject;
    public ObjectA(int a, int b){
        aData = a;
        bObject = new ObjectB(b);
    }

    @Override
    public String toString() {
        return "ObjectA{" +
                "aData=" + aData +
                ", bObject=" + bObject.toString() +
                '}';
    }
}
