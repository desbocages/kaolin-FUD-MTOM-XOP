/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kaolin.mtom.client.fud;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import javax.activation.DataHandler;
import javax.imageio.ImageIO;
import kaloin.mtom.client.ds.ByteArrayDataSource;
import kaolin.mtom.fud.FUDFile;
import kaolin.mtom.fud.FUDImageFile;
import kaolin.mtom.fud.handling.FUDFileHandler;


class Uploader {

    FUDFileHandler uploadHandler;

    public Uploader(FUDFileHandler handler) {
        uploadHandler = handler;
    }

    public synchronized String uploadImage(FUDImageFile f) {
        return uploadHandler.uploadImage(f);
    }

    public synchronized String uploadImage(File f) {
        try {
            BufferedImage bimg = ImageIO.read(f);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bimg, "jpg", baos);
            baos.flush();
            byte[] data = baos.toByteArray();
            baos.close();
            FUDImageFile toUpload = new FUDImageFile();
            ByteArrayDataSource bads =
                    new ByteArrayDataSource(data, "image/jpeg");
            toUpload.setFileHandler(new DataHandler(bads));
            toUpload.setReference(f.getName());
            return uploadHandler.uploadImage(toUpload);
        } catch (Exception e) {
            return null;
        }
    }

    public synchronized String uploadImage(String ref, File f) {
        try {
            BufferedImage bimg = ImageIO.read(f);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bimg, "jpg", baos);
            baos.flush();
            byte[] data = baos.toByteArray();
            baos.close();
            FUDImageFile toUpload = new FUDImageFile();
            ByteArrayDataSource bads =
                    new ByteArrayDataSource(data, "image/jpeg");
            toUpload.setFileHandler(new DataHandler(bads));
            toUpload.setReference(ref);
            return uploadHandler.uploadImage(toUpload);
        } catch (Exception e) {
            return null;
        }
    }

    public synchronized String uploadImage(File f, String reference,
            String formatName, String contentType) {
        try {
            BufferedImage bimg = ImageIO.read(f);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bimg, formatName, baos);
            baos.flush();
            byte[] data = baos.toByteArray();
            baos.close();
            FUDImageFile toUpload = new FUDImageFile();
            ByteArrayDataSource bads =
                    new ByteArrayDataSource(data, contentType);
            toUpload.setFileHandler(new DataHandler(bads));
            toUpload.setReference(reference);
            return uploadHandler.uploadImage(toUpload);
        } catch (Exception e) {
            return null;
        }
    }

    public synchronized String uploadFile(FUDFile f) {
        return uploadHandler.uploadFile(f);
    }

}
