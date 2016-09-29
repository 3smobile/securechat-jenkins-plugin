package jenkins.plugins.securechat;

public class SecureChatNotifierStub extends SecureChatNotifier {

    public SecureChatNotifierStub(String integrationURL, String buildServerUrl,
                             String sendAs, boolean startNotification, boolean notifyAborted, boolean notifyFailure,
                             boolean notifyNotBuilt, boolean notifySuccess, boolean notifyUnstable, boolean notifyBackToNormal,
                             boolean notifyRepeatedFailure, boolean includeTestSummary, CommitInfoChoice commitInfoChoice,
                             boolean includeCustomMessage, String customMessage) {
        super(integrationURL, buildServerUrl, sendAs, startNotification, notifyAborted, notifyFailure,
                notifyNotBuilt, notifySuccess, notifyUnstable, notifyBackToNormal, notifyRepeatedFailure,
                includeTestSummary, commitInfoChoice, includeCustomMessage, customMessage);
    }

    public static class DescriptorImplStub extends SecureChatNotifier.DescriptorImpl {

        private SecureChatService secureChatService;

        @Override
        public synchronized void load() {
        }

        @Override
        SecureChatService getSecureChatService(final String integrationURL) {
            return secureChatService;
        }

        public void setSecureChatService(SecureChatService secureChatService) {
            this.secureChatService = secureChatService;
        }
    }
}
