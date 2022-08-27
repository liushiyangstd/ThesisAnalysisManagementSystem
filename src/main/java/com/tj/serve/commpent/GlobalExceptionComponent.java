package com.tj.serve.commpent;

import com.tj.serve.exception.MyException;
import com.tj.serve.utils.R;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
@ControllerAdvice
public class GlobalExceptionComponent {
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public R error(Exception e){
        e.printStackTrace();
        return R.error();
    }
    @ExceptionHandler(MyException.class)
    @ResponseBody
    public R error(MyException e){
        return R.error().code(e.getCode()).message(e.getMessage());
    }
}
