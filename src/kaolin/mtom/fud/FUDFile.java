/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package kaolin.mtom.fud;

import javax.activation.DataHandler;
import javax.xml.bind.annotation.XmlMimeType;

/**
 * The class representing a file to transfer.
 * It contains a Data Handler, a reference and the length of the data being 
 * transfered. By default, the data handler will be used for octet-stream.
 * @see javax.activation.DataHandler
 * @author Desbocages
 */
public class FUDFile{
    /**
     * The data handler.
     * @see javax.activation.DataHandler
     */
    protected DataHandler fileHandler;
    /**
     * the reference that will help locating where to take/put the data to
     * transfer.
     */
    protected String reference;
    /**
     * The length of the data being transfered.
     */
    protected long fileLength;

    /**
     * @return the fileHandler
     * @see javax.activation.DataHandler
     */
    @XmlMimeType("application/octet-stream")
    public DataHandler getFileHandler() {
        return fileHandler;
    }

    /**
     * @param fileHandler the fileHandler to set
     * @see javax.activation.DataHandler
     */
    public void setFileHandler(DataHandler fileHandler) {
        this.fileHandler = fileHandler;
    }

    /**
     * @return the reference
     */
    public String getReference() {
        return reference;
    }

    /**
     * @param reference the reference to set
     */
    public void setReference(String reference) {
        this.reference = reference;
    }

    /**
     * @return the fileLength
     */
    public long getFileLength() {
        return fileLength;
    }

    /**
     * @param fileLength the fileLength to set
     */
    public void setFileLength(long fileLength) {
        this.fileLength = fileLength;
    }

}
