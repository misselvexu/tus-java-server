package me.desair.tus.server;

import me.desair.tus.server.exception.TusException;
import me.desair.tus.server.upload.UploadIdFactory;
import me.desair.tus.server.upload.UploadStorageService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public abstract class AbstractTusFeature implements TusFeature {

    private final List<RequestValidator> requestValidators = new LinkedList<>();
    private final List<RequestHandler> requestHandlers = new LinkedList<>();

    public AbstractTusFeature() {
        initValidators(requestValidators);
        initRequestHandlers(requestHandlers);
    }

    protected abstract void initValidators(final List<RequestValidator> requestValidators);

    protected abstract void initRequestHandlers(final List<RequestHandler> requestHandlers);

    @Override
    public void validate(final HttpMethod method, final HttpServletRequest servletRequest, final UploadStorageService uploadStorageService, final UploadIdFactory idFactory) throws TusException {
        for (RequestValidator requestValidator : requestValidators) {
            if(requestValidator.supports(method)) {
                requestValidator.validate(method, servletRequest, uploadStorageService, idFactory);
            }
        }
    }

    @Override
    public void process(final HttpMethod method, final HttpServletRequest servletRequest, final TusServletResponse servletResponse, final UploadStorageService uploadStorageService, final UploadIdFactory idFactory) throws IOException {
        for (RequestHandler requestHandler : requestHandlers) {
            if(requestHandler.supports(method)) {
                requestHandler.process(method, servletRequest, servletResponse, uploadStorageService, idFactory);
            }
        }
    }
}
