package com.zzy.resps;

import com.zzy.constants.Constants;


/**
 * Created by Sheep on 2019/11/8.
 */

public class BaseApiService {


    public BaseResponse setResultError(Integer code, String msg) {
        return setResult(code, msg, null);
    }

    // 返回错误，可以传msg
    public BaseResponse setResultError(String msg) {
        return setResult(Constants.HTTP_RES_CODE_500, msg, null);
    }

    // 返回成功，可以传data值
    public BaseResponse setResultSuccess(Object data) {
        return setResult(Constants.HTTP_RES_CODE_200, Constants.HTTP_RES_CODE_200_VALUE, data);
    }

    // 返回成功，沒有data值
    public BaseResponse setResultSuccess() {
        return setResult(Constants.HTTP_RES_CODE_200, Constants.HTTP_RES_CODE_200_VALUE, null);
    }

    // 返回成功，沒有data值
    public BaseResponse  setResultSuccess(String msg) {
        return setResult(Constants.HTTP_RES_CODE_200, msg, null);
    }

    // 通用封装
    public BaseResponse<Object> setResult(Integer code, String msg, Object data) {
        return new BaseResponse(code, msg, data);
    }


    // 调用数据库层判断
    public Boolean toDaoResult(int result) {
        return result > 0 ? true : false;
    }
}
