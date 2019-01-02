/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kaolin.mtom.fud.service;

import javax.jws.WebService;
import kaolin.mtom.fud.FUDFile;
import kaolin.mtom.fud.FUDImageFile;
import kaolin.mtom.fud.handling.AbstractHandlingManagerImpl;
import kaolin.mtom.fud.handling.FUDFileHandler;
import kaolin.mtom.fud.handling.FUDHandler;

/**
 * The web service to expose
 *
 * @author Desbocages
 * @param <T> the type of the handling manager, the concrete class that will
 * implement the local server operations.
 */
@WebService(endpointInterface = "kaolin.mtom.fud.handling.FUDFileHandler")
public abstract class AbstractFUDServiceImpl<T extends AbstractHandlingManagerImpl> implements FUDFileHandler {

    /**
     * the handler that will be used to perform upload/download tasks.
     */
    protected FUDHandler<T> handler;

    public AbstractFUDServiceImpl() {
    }

    @Override
    public String uploadImage(FUDImageFile file) {
        return getHandler().uploadImage(file);
    }

    @Override
    public String uploadFile(FUDFile file) {
        return getHandler().uploadFile(file);
    }

    @Override
    public FUDImageFile downloadImage(String ref) {
        return getHandler().downloadImage(ref);
    }

    @Override
    public FUDFile downloadFile(String ref) {
        return getHandler().downloadFile(ref);
    }

    /**
     * @return the handler
     */
    public FUDHandler<T> getHandler() {
        return handler;
    }

    /**
     * @param handler the handler to set
     */
    public void setHandler(FUDHandler<T> handler) {
        this.handler = handler;
    }

}
