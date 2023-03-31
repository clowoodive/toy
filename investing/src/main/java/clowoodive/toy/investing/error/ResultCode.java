package clowoodive.toy.investing.error;

public enum ResultCode {
    InternalServerError(1000),

    BadServerData(2002),
    InvalidAmount(2003),
    InvalidUserId(2004),
    InvalidProductId(2005),
    BadPeriod(2006),
    BadInvestingData(2007),
    ExceededAmount(2100),
    BadReqParamUserId(2101),
    DuplicatedInvesting(2102),
    SoldOut(2103),
    ;

    private final int code;

    ResultCode(int val) {
        code = val;
    }

    public final int getCode() {
        return code;
    }
}
