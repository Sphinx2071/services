package org.opendatakit.services.thrift_file_importer;

import android.util.Log;

import org.apache.thrift.TException;
import org.opendatakit.services.thrift_file_importer.generated.*;

public class FileServiceHandler implements FileService.Iface{

    public FileServiceHandler() {
    }

    @Override
    public void ping() throws TException {
        Log.d("ThriftServer", "ping() called");
    }

    @Override
    public TransferResult importFile(FilePayload f) throws TException {
        return new TransferResult();
    }

    @Override
    public FilePayload exportFile(String path) throws TException{
        return new FilePayload();
    }
}
