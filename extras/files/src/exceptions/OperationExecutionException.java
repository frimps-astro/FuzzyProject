package exceptions;

public class OperationExecutionException extends Error{
    public OperationExecutionException(String errorMessage){
        super(errorMessage);
    }
}
