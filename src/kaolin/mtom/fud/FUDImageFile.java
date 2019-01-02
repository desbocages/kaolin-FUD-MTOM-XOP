/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package kaolin.mtom.fud;

import javax.activation.DataHandler;
import javax.xml.bind.annotation.XmlMimeType;

/**
 *
 * @author Desbocages
 */
public class FUDImageFile extends FUDFile{
    /**
     * The mime type is changed here to image/*
     * @see javax.activation.DataHandler
     * @return the fileHandler
     */
    @XmlMimeType("image/*")
    @Override
    public DataHandler getFileHandler() {
        return fileHandler;
    }
    
}
