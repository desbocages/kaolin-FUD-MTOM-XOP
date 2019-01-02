/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kaolin.mtom.fud.handling;

import kaolin.mtom.fud.FUDFile;
import kaolin.mtom.fud.FUDImageFile;

/**
 * A local interface that will be implemented to perform concrete operations on
 * data to upload or download.
 *
 * @author Desbocages
 */
public interface FUDHandlingManager {

    /**
     * This method will be implemented by the developer to identify the data and
     * build te file to send to client.
     *
     * @param reference the reference that will help identify the data to send.
     * @return the data to send to the client app.
     */
    public FUDFile doDownload(String reference);

    /**
     * This method will be implemented by the developer to identify the image's
     * data and build te file to send to client.
     *
     * @param reference the reference that will help identify the data to send.
     * @return the data to send to the client app.
     */
    public FUDImageFile doDownloadForImage(String reference);

    /**
     * This method will be used to upload a file from the client to the server.
     *
     * @param file the octet-stream data being transfered
     * @return a String returned by the developer to the client app as a status.
     */
    public String doUpload(FUDFile file);

    /**
     * This method will be used to upload a file from the client to the server.
     *
     * @param file the image data being transfered
     * @return a String returned by the developer to the client app as a status.
     */
    public String doUpload(FUDImageFile file);
}
