package Enumerators;


public enum MovementResult {
    SUCCESFULL(0),
    INVALIDACCOUNT(1),
    INVALIDCONCEPT(2),
    INSUFFICIENTFUNDS(3),
    UNSPECIFIEDSQLERROR(999);
    
    //ATTRIBUTES
    private int numVal;
    private String message;
    
    //getters
    public int getNumVal(){
        return this.numVal;
    }
    
    public String getMessage(){
        switch(this){
            case SUCCESFULL : this.message = "The Operation was succesfull";break;
            case INVALIDACCOUNT : this.message = "Invalid Account";break;
            case INVALIDCONCEPT : this.message = "Invalid Concept";break;
            case INSUFFICIENTFUNDS : this.message = "Insufficient funds";break;
            case UNSPECIFIEDSQLERROR : this.message = "Unespecified SQL error";break;
        }
        return this.message;
    }
    
    MovementResult(int numVal){
        this.numVal = numVal;
        this.message = "";
    }
}
