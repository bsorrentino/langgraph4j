package org.bsc.langgraph4j.serializer.std;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

class ClassHolder implements Externalizable {

    private boolean isNull;
    private Class<?> clazz;

    public ClassHolder( ) {
        this.isNull = true;
        this.clazz = null;
    }
    public ClassHolder(Object value ) {
        this.isNull = value==null;
        this.clazz = (value==null) ? null : value.getClass();
    }

    public String getTypeName() {
        return ( clazz == null ) ? "null" : clazz.getName();
    }

    public Class<?> getType() {
        return clazz;
    }

    public boolean isNull() {
        return isNull;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeBoolean(isNull);
        if( !isNull ) {
            out.writeObject(clazz);
        }
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        isNull = in.readBoolean();
        clazz = isNull ? null : (Class<?>) in.readObject();
    }

}
