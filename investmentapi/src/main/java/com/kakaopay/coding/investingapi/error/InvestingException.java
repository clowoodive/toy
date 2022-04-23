package com.kakaopay.coding.investingapi.error;

public class InvestingException extends RuntimeException {
    private static final long serialVersionUID = 1340362425516757033L;

    public final ResultCode resultCode;
    public final String resultMessage;

    public InvestingException(ResultCode resultCode) {
        this.resultCode = resultCode;
        this.resultMessage = null;
    }

    public InvestingException(ResultCode resultCode, String message) {
        this.resultCode = resultCode;
        this.resultMessage = message;
    }

    public static class ResultBody {
        public final ResultCode resultCode;
        public final String resultMessage;

        public ResultBody(ResultCode resultCode, String message) {
            this.resultCode = resultCode;
            this.resultMessage = message;
        }
    }
}
