package clowoodive.toy.investing.error;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class InvestingException extends RuntimeException {
    private final ResultCode code;
    private final String message;

    public InvestingException(ResultCode resultCode) {
        this.code = resultCode;
        this.message = null;
    }

    @RequiredArgsConstructor
    public static class ResultBody {
        @JsonProperty("result_code")
        private final int resultCode;
        @JsonProperty("result_message")
        private final String resultMessage;
    }
}
