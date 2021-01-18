package com.youyu.conference.exception;

import com.youyu.conference.common.ResponseResult;

public class BizException extends RuntimeException {

	private static final long serialVersionUID = -1390600959255647338L;
	
	private ResponseResult content;
	
	public BizException(ResponseResult content) {
	    this.content = content;
    }
	
	public ResponseResult getContent() {
	    return this.content;
    }

}
