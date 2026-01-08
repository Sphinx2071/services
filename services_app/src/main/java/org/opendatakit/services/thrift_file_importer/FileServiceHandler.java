package org.opendatakit.services.thrift_file_importer;

import android.util.Log;

import org.apache.thrift.TException;
import org.opendatakit.services.thrift_file_importer.generated.File;
import org.opendatakit.services.thrift_file_importer.generated.FileService;

public class FileServiceHandler implements FileService.Iface{

    public FileServiceHandler() {
    }

    @Override
    public void ping() throws TException {
        Log.d("ThriftServer", "ping() called");
    }

    @Override
    public String upload(File f) throws TException {
        return "";
    }
}
