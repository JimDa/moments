package com.moments.auth.service;

import java.io.InputStream;

public interface StorageService {
    void saveFile(InputStream inputStream, String fileName);
}
