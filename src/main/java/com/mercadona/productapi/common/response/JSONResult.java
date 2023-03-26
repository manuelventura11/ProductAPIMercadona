package com.mercadona.productapi.common.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Basic information of a JSON response")
public class JSONResult<T>{
    protected Boolean success;
    protected Integer errorCode;
    protected String message;
    protected T response;

    public JSONResult()
    {
        this(Boolean.FALSE, null, null, null);
    }

    @SuppressWarnings("unchecked")
    public JSONResult(boolean success)
    {
        this(success, null, null, (T) (Boolean) success);
    }

    public JSONResult(Boolean success, String message)
    {
        this(success, null, message, null);
    }

    public JSONResult(T result)
    {
        this(Boolean.TRUE, null, null, result);
    }

    public JSONResult(Integer errorCode, String message)
    {
        this(Boolean.FALSE, errorCode, message, null);
    }

    public JSONResult(Boolean success, Integer errorCode, String message, T response)
    {
        this.success = success;
        this.errorCode = errorCode;
        this.message = message;
        this.response = response;
    }

    @ApiModelProperty(value = "If is success the request ", allowableValues = "true,false")
    public Boolean isSuccess()
    {
        return success;
    }

    public void setSuccess(Boolean success)
    {
        this.success = success;
    }

    @ApiModelProperty(value = "The error code")
    public Integer getErrorCode()
    {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode)
    {
        this.errorCode = errorCode;
    }

    @ApiModelProperty(value = "The response of the request")
    public T getResponse()
    {
        return response;
    }

    public void setResponse(T response)
    {
        this.response = response;
    }

    @ApiModelProperty(value = "Description of the error code")
    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

}
