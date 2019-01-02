/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kaolin.mtom.fud.service;

import javax.jws.WebService;
import kaolin.mtom.fud.handling.FUDFileHandler;

/**
 * The web service to expose
 *
 * @author Desbocages
 */
@WebService(endpointInterface = "kaolin.mtom.fud.handling.FUDFileHandler")
public abstract class AbstractFUDServiceHandlerImpl  implements FUDFileHandler{
    
}
