package by.afinny.apigateway.util.constant;

public enum JWT {

    KEY("jxgEQeXHuPq8VdbyYFNkANdudQ53YUn4"),
    HEADER("Authorization"),
    ACCESS_TOKEN_EXPIRATION("600000"),
    REFRESH_TOKEN_EXPIRATION("1800000"),
    UUID("uuid");

    private final String value;

    JWT(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}