package org.bsc.langgraph4j.serializer.std;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Optional;

import static java.util.Optional.ofNullable;

class StringHolder implements Externalizable {
    private boolean isNull;
    private String value;

    public StringHolder( ) {
        this.value = null;
        this.isNull = true;
    }
    public StringHolder( String value ) {
        this.value = value;
        this.isNull = value==null;
    }

    public boolean isNull() {
        return isNull;
    }
    public Optional<String> value() {
        return ofNullable(value);
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeBoolean(isNull);
        if( !isNull ) {
            out.writeUTF(value);
        }
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {

        isNull = in.readBoolean();
        value = isNull ? null : in.readUTF();

    }
}
