namespace java org.opendatakit.services.thrift_file_importer

struct FilePayload {
    1: string fileName;
    2: string relativePath;
    3: i64 fileSize;
    4: string md5_hash;
    5: binary fileData;
}

struct TransferResult {
    1: bool success;
    2: string message;
}

service FileService {
    void ping(),
    TransferResult importFile(1: FilePayload f)
    FilePayload exportFile(1: string relativePath)
}
/*
Client stub makes an RPC (import).

Server loop accepts the request.

Processor looks up the method.

Processor calls the handler implementation.

Handler executes your code and returns.

Processor serializes the result.

Server sends it back, client stub deserializes, and callback gets the result.
 */