/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kaolin.mtom.client.fud;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import javax.imageio.ImageIO;
import kaolin.mtom.fud.FUDFile;
import kaolin.mtom.fud.FUDImageFile;
import kaolin.mtom.fud.handling.FUDFileHandler;

/**
 * This is a simple helper class to create content and download.
 *
 * @author Desbocages
 */
public class Downloader {

    /**
     * The contract that will help downloading files.
     */
    FUDFileHandler downloadHandler;

    /**
     * Will help to build an uploader instance
     *
     * @param handler the contract that will help receiving files
     */
    public Downloader(FUDFileHandler handler) {
        downloadHandler = handler;
    }

    /**
     * Will help downloading an image by providing a reference to the data.
     *
     * @param reference the reference to index datum on the server.
     * @return a buffered image object representing the downloaded image.
     * @throws Exception a SOAP fault raised if any, or a Runtime Exception or
     * an IOException.
     */
    public BufferedImage downloadImage(String reference) throws Exception {
        FUDImageFile file = downloadHandler.downloadImage(reference);
        if (file == null
                || file.getFileHandler() == null
                || file.getFileHandler().getInputStream() == null) {
            throw new RuntimeException("No data.");
        }
        InputStream in = file.getFileHandler().getInputStream();
        return ImageIO.read(in);
    }

    /**
     * Will help downloading an file by providing a reference to the data.
     *
     * @param reference the reference to index datum on the server.
     * @return an input stream object bearing the downloaded data.
     * @throws Exception a SOAP fault raised if any, or a Runtime Exception or
     * an IOException.
     */
    public InputStream downloadOctetStream(String reference)
            throws Exception {
        FUDFile file = downloadHandler.downloadFile(reference);
        if (file == null
                || file.getFileHandler() == null
                || file.getFileHandler().getInputStream() == null) {
            throw new RuntimeException("No data could be downloaded.");
        }
        InputStream in = file.getFileHandler().getInputStream();
        return in;
    }

}
