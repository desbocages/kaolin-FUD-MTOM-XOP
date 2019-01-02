package kaolin.mtom.fud.handling;

import java.lang.reflect.ParameterizedType;
import kaolin.mtom.fud.FUDFile;
import kaolin.mtom.fud.FUDImageFile;

/**
 * A concrete class to implement the upload/download logics.
 *
 * @author Desbocages
 * @param <T> the class implementing the local API to handle operations. It
 * should extend the <code>AbstractHandlingManagerImpl</code> class.
 * @see AbstractHandlingManagerImpl
 */
public class FUDHandler<T extends AbstractHandlingManagerImpl>
        extends AbstractHandler {

    /**
     * The class extending the <code>AbstractHandlingManagerImpl</code> class
     * that will be used to perform the concrete operations. It will be written
     * by the developer. It should have a default constructor, otherwise an
     * exception will be raised.
     */
    protected T handler;

    @SuppressWarnings("unchecked")
    public FUDHandler() {
        try {
            System.out.println(this.getClass().getGenericSuperclass());
            Class<T> c = (Class<T>)
                    ((ParameterizedType) this.getClass().getGenericSuperclass()
                            ).getActualTypeArguments()[0];
            handler = c.newInstance();
        } catch (Exception ex) {
            throw new IllegalArgumentException("Could not instanciate"
                    + " the given handler class.", ex);
        }
    }

    @Override
    public String uploadImage(FUDImageFile file) {
        if (file == null || file.getFileHandler() == null) {
            throw new IllegalArgumentException("The given FUD image file is"
                    + " invalid: null value or no data handler provided.");
        }
        return handler.doUpload(file);
    }

    @Override
    public String uploadFile(FUDFile file) {
        if (file == null || file.getFileHandler() == null) {
            throw new IllegalArgumentException("The given FUD file is invalid:"
                    + " null value or no data handler provided.");
        }
        return handler.doUpload(file);
    }

    @Override
    public FUDImageFile downloadImage(String ref) {
        if (ref == null || ref.isEmpty()) {
            throw new IllegalArgumentException("The given id string provided"
                    + " is invalid: null or empty value.");
        }
        return handler.doDownloadForImage(ref);
    }

    @Override
    public FUDFile downloadFile(String ref) {
        if (ref == null || ref.isEmpty()) {
            throw new IllegalArgumentException("The given id string provided"
                    + " is invalid: null or empty value.");
        }
        return handler.doDownload(ref);
    }

}
