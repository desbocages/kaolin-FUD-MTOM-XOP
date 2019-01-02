/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kaolin.mtom.fud.handling;

import javax.jws.WebService;
import kaolin.mtom.fud.FUDFile;
import kaolin.mtom.fud.FUDImageFile;

/**
 * This Interface represents the API of this library. It defines the contract
 * for the applications that will remotely transfer files using the lib.
 *
 * @author Desbocages
 */
@WebService
public interface FUDFileHandler {

    /**
     * Will be used to upload images
     *
     * @param file the data being transfered
     * @see javax.activation.DataHandler
     * @see kaolin.mtom.fud.FUDImageFile
     * @return a String returned by the developer as an info about the op
     */
    public String uploadImage(FUDImageFile file);

    /**
     * Will be used to upload images
     *
     * @param file the data being transfered
     * @see kaolin.mtom.fud.FUDFile
     * @return a String returned by the developer as an info about the op
     */
    public String uploadFile(FUDFile file);

    /**
     * Will be used to download an image.
     *
     * @param ref the reference that will help locating the data to transfer
     * @see kaolin.mtom.fud.FUDImageFile
     * @return the data being downloaded.
     */
    public FUDImageFile downloadImage(String ref);

    /**
     * Will be used to download a certain file as Octet-stream.
     *
     * @param ref the reference that will help locating the data to transfer
     * @see kaolin.mtom.fud.FUDFile
     * @return the data being downloaded.
     */
    public FUDFile downloadFile(String ref);
}
