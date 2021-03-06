package com.ctrip.framework.apollo.core.exception;

public abstract class AbstractBaseException extends RuntimeException{

  /**
   * 
   */
  private static final long serialVersionUID = -1713129594004951820L;
  
  private String errorCode;
  
  public AbstractBaseException(){
    
  }
  
  public AbstractBaseException(String str){
    super(str);
  }
  
  public AbstractBaseException(String str, Exception e){
    super(str,e);
  }
  
  public AbstractBaseException(String str, String errorCode){
    super(str);
    this.setErrorCode(errorCode);
  }

  public String getErrorCode() {
    return errorCode;
  }

  public void setErrorCode(String errorCode) {
    this.errorCode = errorCode;
  }
}
