package com.tasknotification.tasknotification.model.singup;

public class SingUpResponse {

    public String getId      () { return id      ;}
    public String getReqId   () { return reqId   ;}
    public String getResult  () { return result  ;}
    public int    getCode    () { return code    ;}
    public String getMessage () { return message ;}

    public void setId      (String id      ) { this.id       = id      ;}
    public void setReqId   (String reqId   ) { this.reqId    = reqId   ;}
    public void setResult  (String result  ) { this.result   = result  ;}
    public void setCode    (int code       ) { this.code     = code    ;}
    public void setMessage (String message ) { this.message  = message ;}

    protected String id      ;
    protected String reqId   ;
    protected String result  ;
    protected int    code    ;
    protected String message ;

    public SingUpResponse() {
    }
}
