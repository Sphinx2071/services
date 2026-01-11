package org.opendatakit.services.thrift_file_importer;

import android.content.Context;
import android.util.Log;

import org.apache.thrift.TException;
import org.opendatakit.services.thrift_file_importer.generated.*;
import org.opendatakit.utilities.ODKFileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FileServiceHandler implements FileService.Iface {
    private final Context context;
    public FileServiceHandler(Context context) {
        this.context = context;
    }

    @Override
    public void ping() throws TException {
        Log.d("ThriftServer", "ping() called");
    }

    @Override
    public TransferResult importFile(FilePayload f) throws TException {
        String tempPath =  "temp_" + f.getFileName();
        File tempFile = new File(context.getFilesDir(), tempPath);

        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(f.getFileData());
        } catch (IOException e) {
            throw new TException(e);
        }

        TransferResult result = new TransferResult(false, "failed");

        if (fileVerified(tempFile, f)) {
            File dest = new File(ODKFileUtils.getOdkxFolder(), f.getRelativePath());

            File parent = dest.getParentFile();
            if(parent != null && !parent.exists()){
                if(!parent.mkdirs()){
                    result.message = "Failed to create parent directories";
                    return result;
                }
            }
            if(dest.exists() && !dest.delete()){
                result.message = "Failed to delete existing File";
                return result;
            }

            boolean move = tempFile.renameTo(dest);

            if (move) {
                result.success = true;
                result.message = "File imported";
            } else {
                if(!tempFile.delete()){
                    Log.e("ThriftServer", "Failed to delete " + tempFile.getName());
                }
                result.message = "Failed to move file into ODKX folder";
                return result;
            }
        }

        return result;
    }

    private static String md5File(File file) throws IOException, NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] buffer = new byte[8192];
        int read;

        try (InputStream is = new FileInputStream(file)) {
            while ((read = is.read(buffer)) != -1) {
                md.update(buffer, 0, read);
            }
        }

        byte[] digest = md.digest();
        StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    private boolean fileVerified(File temp, FilePayload fp) throws TException{
        boolean isSize = temp.length() == fp.fileSize;

        final String fileHash;
        try {
            fileHash = md5File(temp);
        } catch(IOException | NoSuchAlgorithmException e) {
            throw new TException(e);
        }

        boolean isHash = fileHash.equals(fp.getMd5_hash());

        return isSize && isHash;
    }

    @Override
    public FilePayload exportFile(String path) throws TException{
        return new FilePayload();
    }

}
