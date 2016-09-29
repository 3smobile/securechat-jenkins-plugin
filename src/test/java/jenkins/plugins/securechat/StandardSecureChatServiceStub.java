package jenkins.plugins.securechat;

public class StandardSecureChatServiceStub extends StandardSecureChatService {

    private HttpClientStub httpClientStub;

    public StandardSecureChatServiceStub(String integrationURL) {
        super(integrationURL);
    }

    @Override
    public HttpClientStub getHttpClient() {
        return httpClientStub;
    }

    public void setHttpClient(HttpClientStub httpClientStub) {
        this.httpClientStub = httpClientStub;
    }
}
