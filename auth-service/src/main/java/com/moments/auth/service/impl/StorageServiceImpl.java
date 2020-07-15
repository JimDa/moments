package com.moments.auth.service.impl;


import com.moments.auth.service.StorageService;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class StorageServiceImpl implements StorageService {

    @Override
    public void saveFile(InputStream inputStream, String fileName) {
        OutputStream out = null;

        try {
            String path = "/Users/dapengchengsmac/Desktop/download";
            byte[] bs = new byte[1024];
            int len;
            File file = new File(path);
            if (!file.exists()) {
                file.mkdirs();
            }
            out = new FileOutputStream(file.getPath() + File.separator + fileName);

            while ((len = inputStream.read(bs)) != -1) {
                out.write(bs, 0, len);
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
                inputStream.close();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }
}
