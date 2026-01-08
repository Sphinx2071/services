package org.opendatakit.services.thrift_file_importer;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.TProcessor;

import org.opendatakit.services.thrift_file_importer.generated.FileService;
import org.opendatakit.services.thrift_file_importer.FileServiceHandler;

public class ThriftServerService extends Service {
    private static final String TAG = "ThriftServerService";
    private static final int PORT = 9090;

    private Thread serverThread;
    private TSimpleServer server;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (serverThread == null) {
            startThriftServer();
        }
        return START_NOT_STICKY;
    }

    private void startThriftServer() {
        serverThread = new Thread(() -> {
            try {
                FileServiceHandler handler = new FileServiceHandler();
                TProcessor processor = new FileService.Processor<>(handler);

                TServerTransport serverTransport = new TServerSocket(PORT);
                TSimpleServer.Args args = new TSimpleServer.Args(serverTransport)
                        .processor(processor)
                        .protocolFactory(new TBinaryProtocol.Factory());

                server = new TSimpleServer(args);
                Log.d(TAG, "Thrift server starting on port " + PORT);
                server.serve();
            } catch (Exception e) {
                Log.e(TAG, "Error in Thrift server", e);
            }
        }, "ThriftServerThread");
        serverThread.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (server != null && server.isServing()) {
            server.stop();
        }
        if (serverThread != null) {
            serverThread.interrupt();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null; // Not using bound service for now
    }
}

