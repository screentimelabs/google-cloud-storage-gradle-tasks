package org.gradle

import com.google.api.client.http.InputStreamContent
import com.google.api.services.storage.Storage
import com.google.api.services.storage.model.ObjectAccessControl
import com.google.api.services.storage.model.StorageObject
import org.gradle.api.tasks.TaskAction

/**
 * Created by stevevangasse on 17/09/15.
 */
class UploadTask extends AbstractCloudStorageTask {

    String remoteFilePath
    String contentType
    File localFile
    String bucketName

    @TaskAction
    def uploadFile () {
        InputStream stream = new FileInputStream(localFile)
        InputStreamContent contentStream = new InputStreamContent(contentType, stream)
        StorageObject objectMetadata = new StorageObject()
        // Set the destination object name
                .setName(remoteFilePath)
        // Set the access control list to publicly read-only
                .setAcl(Arrays.asList(
                new ObjectAccessControl().setEntity("allUsers").setRole("READER")));

        getStorageService { storageService ->

            Storage.Objects.Insert insertRequest = storageService.objects().insert(
                    bucketName, objectMetadata, contentStream);

            insertRequest.execute();
        }
    }
}
