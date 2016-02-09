package jenkins.plugins.securechat;

import org.apache.http.HttpStatus;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class StandardSecureChatServiceTest {

    /**
     * Publish should generally not rethrow exceptions, or it will cause a build job to fail at end.
     */
    @Test
    public void publishWithBadHostShouldNotRethrowExceptions() {
        StandardSecureChatService service = new StandardSecureChatService("url");
        service.setHost("hostvaluethatwillcausepublishtofail");
        service.publish("message");
    }

    /**
     * Use a valid host, but an invalid team domain
     */
    @Test
    public void invalidTeamDomainShouldFail() {
        StandardSecureChatService service = new StandardSecureChatService("url");
        service.publish("message");
    }

    /**
     * Use a valid team domain, but a bad token
     */
    @Test
    public void invalidTokenShouldFail() {
        StandardSecureChatService service = new StandardSecureChatService("url");
        service.publish("message");
    }

    @Test
    public void publishToASingleRoomSendsASingleMessage() {
        StandardSecureChatServiceStub service = new StandardSecureChatServiceStub("url");
        HttpClientStub httpClientStub = new HttpClientStub();
        service.setHttpClient(httpClientStub);
        service.publish("message");
        assertEquals(1, service.getHttpClient().getNumberOfCallsToExecuteMethod());
    }

    @Test
    public void successfulPublishToASingleRoomReturnsTrue() {
        StandardSecureChatServiceStub service = new StandardSecureChatServiceStub("url");
        HttpClientStub httpClientStub = new HttpClientStub();
        httpClientStub.setHttpStatus(HttpStatus.SC_OK);
        service.setHttpClient(httpClientStub);
        assertTrue(service.publish("message"));
    }

    @Test
    public void failedPublishToASingleRoomReturnsFalse() {
        StandardSecureChatServiceStub service = new StandardSecureChatServiceStub("url");
        HttpClientStub httpClientStub = new HttpClientStub();
        httpClientStub.setHttpStatus(HttpStatus.SC_NOT_FOUND);
        service.setHttpClient(httpClientStub);
        assertFalse(service.publish("message"));
    }

    @Test
    public void publishToEmptyRoomReturnsTrue() {
        StandardSecureChatServiceStub service = new StandardSecureChatServiceStub("url");
        HttpClientStub httpClientStub = new HttpClientStub();
        httpClientStub.setHttpStatus(HttpStatus.SC_OK);
        service.setHttpClient(httpClientStub);
        assertTrue(service.publish("message"));
    }
}
