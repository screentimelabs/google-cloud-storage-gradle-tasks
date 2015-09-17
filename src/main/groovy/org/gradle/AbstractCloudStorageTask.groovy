package org.gradle

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.http.HttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.storage.Storage
import org.gradle.api.DefaultTask

abstract class AbstractCloudStorageTask extends DefaultTask {

    def oauth2EmailAddress
    def privateKeyFile

    String SCOPE = "https://www.googleapis.com/auth/devstorage.read_write";


    def getStorageService(task) {
        JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        GoogleCredential credential = new GoogleCredential.Builder()
                .setTransport(httpTransport)
                .setJsonFactory(JSON_FACTORY)
                .setServiceAccountId(oauth2EmailAddress)
                .setServiceAccountPrivateKeyFromP12File(privateKeyFile)
                .setServiceAccountScopes(Collections.singleton(SCOPE))
                .build();

        Storage storageService = new Storage.Builder(httpTransport, JSON_FACTORY, credential)
                .setApplicationName("google-cloud-storage-gradle-tasks/1.0").build();

        task(storageService)
    }
}
