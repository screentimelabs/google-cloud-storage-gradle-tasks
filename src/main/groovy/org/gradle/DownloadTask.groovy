package org.gradle

import com.google.api.services.storage.Storage
import org.gradle.api.tasks.TaskAction

/**
 * Created by stevevangasse on 18/09/15.
 */
class DownloadTask extends AbstractCloudStorageTask {

    String remoteFilePath
    File localFile
    String bucketName

    @TaskAction
    def downloadFile () {
        getStorageService { storageService ->

            Storage.Objects.Get getObject = storageService.objects().get(bucketName, remoteFilePath);

            getObject.getMediaHttpDownloader().setDirectDownloadEnabled(true);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            getObject.executeMediaAndDownloadTo(out);

            OutputStream outputStream = new FileOutputStream(localFile);
            out.writeTo(outputStream);
        }
    }
}
