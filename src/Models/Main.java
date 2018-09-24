package Models;

import Enumerators.MovementResult;
import Exceptions.RecordNotFoundException;
import Frames.FrameAccount;
import Models.Account;
import Models.Movement;

public class Main {

    public static void main(String[] args) {
       
    //  Account acc = new Account("Mers","Shane");
      //  System.out.println(acc.add().getMessage());
        
       
       /*try{
           Account acc = new Account(6);
           System.out.println("Name     : " + acc.getFullName());
           System.out.println("Balance  : " + acc.getBalanceFormatted());
           System.out.println(acc.deposit(10000).getMessage());
           System.out.println("New balance : " + acc.getBalanceFormatted());
       }
       catch(RecordNotFoundException ex){
           System.out.println(ex.getMessage());
       }*/
        
        // ** Retiro **
        try{
           Account cuentaB = new Account(10);
           System.out.println("Name     : " + cuentaB.getFullName());
           System.out.println("Balance  : " + cuentaB.getBalanceFormatted());
           System.out.println(cuentaB.withdrawal(1000).getMessage());
           System.out.println("New balance : " + cuentaB.getBalanceFormatted());
       }
       catch(RecordNotFoundException ex){
           System.out.println(ex.getMessage());
       }
        
        
       
//      **Movimientos** 
        try{
           Account cuentaC = new Account(10);
           System.out.println("Name     : " + cuentaC.getFullName());
           System.out.println("Balance  : " + cuentaC.getBalanceFormatted());
           for(Movement m : cuentaC.getMovements()){
               System.out.println(m);
           }
       }
       catch(RecordNotFoundException ex){
           System.out.println(ex.getMessage());
       }
       
    new FrameAccount().show();   
    }
        
    
      
        
       
            

    }
    //new FrameAccount().show();

