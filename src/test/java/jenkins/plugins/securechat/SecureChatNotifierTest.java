package jenkins.plugins.securechat;

import hudson.model.Descriptor;
import hudson.util.FormValidation;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class SecureChatNotifierTest extends TestCase {

    private SecureChatNotifierStub.DescriptorImplStub descriptor;
    private SecureChatServiceStub secureChatServiceStub;
    private boolean response;
    private FormValidation.Kind expectedResult;

    @Before
    @Override
    public void setUp() {
        descriptor = new SecureChatNotifierStub.DescriptorImplStub();
    }

    public SecureChatNotifierTest(SecureChatServiceStub secureChatServiceStub, boolean response, FormValidation.Kind expectedResult) {
        this.secureChatServiceStub = secureChatServiceStub;
        this.response = response;
        this.expectedResult = expectedResult;
    }

    @Parameterized.Parameters
    public static Collection businessTypeKeys() {
        return Arrays.asList(new Object[][]{
                {new SecureChatServiceStub(), true, FormValidation.Kind.OK},
                {new SecureChatServiceStub(), false, FormValidation.Kind.ERROR},
                {null, false, FormValidation.Kind.ERROR}
        });
    }

    @Test
    public void testDoTestConnection() {
        if (secureChatServiceStub != null) {
            secureChatServiceStub.setResponse(response);
        }
        descriptor.setSecureChatService(secureChatServiceStub);
        try {
            FormValidation result = descriptor.doTestConnection("teamDomain", "authToken", "room", "buildServerUrl");
            assertEquals(result.kind, expectedResult);
        } catch (Descriptor.FormException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    public static class SecureChatServiceStub implements SecureChatService {

        private boolean response;

        public boolean publish(String message) {
            return response;
        }

        public boolean publish(String message, String color) {
            return response;
        }

        public void setResponse(boolean response) {
            this.response = response;
        }
    }
}
