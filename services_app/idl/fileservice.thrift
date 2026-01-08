namespace java org.opendatakit.services.thrift_file_importer

struct Message {
    1: string text
}

service StringService {
    void ping(),
    string send(1: Message s)
}

struct File {
    1: string fileName;
    2: i64 fileSize;
    3: string md5_hash;
    4: binary fileData;
}

service FileService {
    void ping(),
    string upload(1: File f)
}
/*
Client stub makes an RPC (upload(file)).

Server loop accepts the request.

Processor looks up the method.

Processor calls the handler implementation.

Handler executes your code and returns.

Processor serializes the result.

Server sends it back, client stub deserializes, and callback gets the result.
 */