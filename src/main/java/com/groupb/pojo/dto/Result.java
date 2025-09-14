package com.groupb.pojo.dto;

import lombok.Data;

@Data
public class Result {
        private Integer code;//响应码
        private String msg;//错误信息
        private Object data;// 数据

        public static Result success(){
            Result result = new Result();
            result.code = 1;
            result.msg = "操作成功！";
            return result;
        }
        public static Result success(Object data){
            Result result = new Result();
            result.code = 1;
            result.msg = "操作成功！";
            result.data = data;
            return result;
        }
        public static Result error(String msg){
            Result result = new Result();
            result.code = 0;
            result.msg = msg;
            return result;
        }
}

