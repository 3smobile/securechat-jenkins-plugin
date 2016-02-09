package jenkins.plugins.securechat;

public class StandardSecureChatServiceStub extends StandardSecureChatService {

    private HttpClientStub httpClientStub;

    public StandardSecureChatServiceStub(String teamDomain, String token, String roomId) {
        super(teamDomain, token, roomId);
    }

    @Override
    public HttpClientStub getHttpClient() {
        return httpClientStub;
    }

    public void setHttpClient(HttpClientStub httpClientStub) {
        this.httpClientStub = httpClientStub;
    }
}
