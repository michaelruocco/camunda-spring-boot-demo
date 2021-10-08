package uk.co.mruoc.demo.adapter.camunda;

public class ProcessInstanceNotFoundException extends RuntimeException {

    public ProcessInstanceNotFoundException(String id) {
        super(id);
    }

}
