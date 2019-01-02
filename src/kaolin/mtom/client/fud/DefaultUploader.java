/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kaolin.mtom.client.fud;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import javax.activation.DataHandler;
import javax.imageio.ImageIO;
import kaloin.mtom.client.ds.ByteArrayDataSource;
import kaolin.mtom.fud.FUDFile;
import kaolin.mtom.fud.FUDImageFile;
import kaolin.mtom.fud.handling.FUDFileHandler;

/**
 * This is a simple helper class to create content and upload.
 *
 * @author Yale
 */
public class DefaultUploader {

    /**
     * The contract that will help uploading files.
     */
    private final FUDFileHandler uploader;

    /**
     * Will help to build an uploader instance
     *
     * @param uploader the contract that will help sending files
     */
    public DefaultUploader(FUDFileHandler uploader) {
        this.uploader = uploader;
    }

    /**
     * This method will be used to construct an instance of FUDFile from an
     * array of bytes by precising the content type of the data being
     * transferred. This method can be used to transfer images.
     *
     * @param data the byte representation of the file being transferred
     * @param ref A reference that will help on the other side
     * @param contentType the content type of the file being transferred
     * @return The FUDFile to transfer
     */
    public FUDFile createContent(byte[] data, String ref, String contentType) {
        FUDFile toUpload = new FUDFile();
        ByteArrayDataSource bads = new ByteArrayDataSource(data, contentType);
        toUpload.setFileHandler(new DataHandler(bads));
        toUpload.setReference(ref);
        toUpload.setFileLength(data.length);
        return toUpload;
    }

    /**
     * This method will be used to construct an instance of FUDFile from a given
     * file by precising the content type of the data being transferred. This
     * method can be used to transfer images.
     *
     * @param f the file object representing the file to transfer
     * @param ref A reference that will help on the other side
     * @param contentType the content type of the file being transferred
     * @return The FUDFile to transfer
     * @throws IOException Can be thrown while accessing the file on disk.
     * @throws Exception Can occur for security or memory availability reasons
     */
    public FUDFile createContent(File f, String ref, String contentType)
            throws IOException, Exception {
        FUDFile toUpload = new FUDFile();
        byte[] data = Files.readAllBytes(f.toPath());
        ByteArrayDataSource bads = new ByteArrayDataSource(data, contentType);
        toUpload.setFileHandler(new DataHandler(bads));
        toUpload.setReference(ref);
        toUpload.setFileLength(data.length);
        return toUpload;
    }

    /**
     * This method will be used to construct an instance of FUDImageFile from a
     * given file by precising the content type of the data being transferred.
     *
     * @param bimg the BufferedImage object representing the image to send
     * @param ref A reference that will help on the other side
     * @param formatName The image format. i.e. PNG, JPG, etc.
     * @param contentType the content type of the file being transferred i.e
     * image/jpg
     * @return the FUDImageFile object to send
     * @throws IOException can occur during the output stream being populated
     * @throws Exception An illegal argument exception or other can be raised
     */
    public FUDImageFile createImageContent(BufferedImage bimg, String ref,
            String formatName, String contentType)
            throws IOException, Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bimg, formatName, baos);
        baos.flush();
        byte[] data = baos.toByteArray();
        baos.close();
        FUDImageFile toUpload = new FUDImageFile();
        ByteArrayDataSource bads = new ByteArrayDataSource(data, contentType);
        toUpload.setFileHandler(new DataHandler(bads));
        toUpload.setReference(ref);
        return toUpload;
    }

    /**
     * This method will be used to send a FUDFile object.
     *
     * @param file the FUDFile object to send
     * @return A string representing the result of the operation.
     * @throws Exception The SOAP fault raised, if any.
     */
    public String sendOctetStream(FUDFile file) throws Exception {
        return uploader.uploadFile(file);
    }

    /**
     * This method will be used to send a FUDImageFile object.
     *
     * @param file the FUDImageFile object to send
     * @return A string representing the result of the operation.
     * @throws Exception The SOAP fault raised, if any.
     */
    public String sendImage(FUDImageFile file) throws Exception {
        return uploader.uploadImage(file);
    }
}
