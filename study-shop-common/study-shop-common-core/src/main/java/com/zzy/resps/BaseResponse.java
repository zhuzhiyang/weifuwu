package com.zzy.resps;

import lombok.Data;

/**
 * Created by Sheep on 2019/11/8.
 */
@Data
public class BaseResponse<T> {


    private Integer code;
    private String msg;
    private T data;

    public BaseResponse() {

    }

    public BaseResponse(Integer rtnCode, String msg, T data) {
        super();
        this.code = rtnCode;
        this.msg = msg;
        this.data = data;
    }
}
