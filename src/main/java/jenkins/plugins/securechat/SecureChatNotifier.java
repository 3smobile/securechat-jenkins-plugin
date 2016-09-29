package jenkins.plugins.securechat;

import hudson.EnvVars;
import hudson.Extension;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.BuildListener;
import hudson.model.Descriptor;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Notifier;
import hudson.tasks.Publisher;
import hudson.util.FormValidation;
import jenkins.model.JenkinsLocationConfiguration;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

public class SecureChatNotifier extends Notifier {

    private static final Logger logger = Logger.getLogger(SecureChatNotifier.class.getName());

    private String integrationURL;
    private String buildServerUrl;
    private String sendAs;
    private boolean startNotification;
    private boolean notifySuccess;
    private boolean notifyAborted;
    private boolean notifyNotBuilt;
    private boolean notifyUnstable;
    private boolean notifyFailure;
    private boolean notifyBackToNormal;
    private boolean notifyRepeatedFailure;
    private boolean includeTestSummary;
    private CommitInfoChoice commitInfoChoice;
    private boolean includeCustomMessage;
    private String customMessage;

    @Override
    public DescriptorImpl getDescriptor() {
        return (DescriptorImpl) super.getDescriptor();
    }

    public String getIntegrationURL() {
        return integrationURL;
    }

    public String getBuildServerUrl() {
        if(buildServerUrl == null || buildServerUrl == "") {
            JenkinsLocationConfiguration jenkinsConfig = new JenkinsLocationConfiguration();
            return jenkinsConfig.getUrl();
        }
        else {
            return buildServerUrl;
        }
    }

    public String getSendAs() {
        return sendAs;
    }

    public boolean getStartNotification() {
        return startNotification;
    }

    public boolean getNotifySuccess() {
        return notifySuccess;
    }

    public CommitInfoChoice getCommitInfoChoice() {
        return commitInfoChoice;
    }

    public boolean getNotifyAborted() {
        return notifyAborted;
    }

    public boolean getNotifyFailure() {
        return notifyFailure;
    }

    public boolean getNotifyNotBuilt() {
        return notifyNotBuilt;
    }

    public boolean getNotifyUnstable() {
        return notifyUnstable;
    }

    public boolean getNotifyBackToNormal() {
        return notifyBackToNormal;
    }

    public boolean includeTestSummary() {
        return includeTestSummary;
    }

    public boolean getNotifyRepeatedFailure() {
        return notifyRepeatedFailure;
    }

    public boolean includeCustomMessage() {
        return includeCustomMessage;
    }

    public String getCustomMessage() {
        return customMessage;
    }

    @DataBoundConstructor
    public SecureChatNotifier(final String integrationURL, final String buildServerUrl,
                         final String sendAs, final boolean startNotification, final boolean notifyAborted, final boolean notifyFailure,
                         final boolean notifyNotBuilt, final boolean notifySuccess, final boolean notifyUnstable, final boolean notifyBackToNormal,
                         final boolean notifyRepeatedFailure, final boolean includeTestSummary, CommitInfoChoice commitInfoChoice,
                         boolean includeCustomMessage, String customMessage) {
        super();
        this.integrationURL = integrationURL;
        this.buildServerUrl = buildServerUrl;
        this.sendAs = sendAs;
        this.startNotification = startNotification;
        this.notifyAborted = notifyAborted;
        this.notifyFailure = notifyFailure;
        this.notifyNotBuilt = notifyNotBuilt;
        this.notifySuccess = notifySuccess;
        this.notifyUnstable = notifyUnstable;
        this.notifyBackToNormal = notifyBackToNormal;
        this.notifyRepeatedFailure = notifyRepeatedFailure;
        this.includeTestSummary = includeTestSummary;
        this.commitInfoChoice = commitInfoChoice;
        this.includeCustomMessage = includeCustomMessage;
        this.customMessage = customMessage;
    }

    public BuildStepMonitor getRequiredMonitorService() {
        return BuildStepMonitor.NONE;
    }

    public SecureChatService newSecureChatService(AbstractBuild r, BuildListener listener) {
        String integrationURL = this.integrationURL;
        if (StringUtils.isEmpty(integrationURL)) {
            integrationURL = getDescriptor().getIntegrationURL();
        }

        EnvVars env = null;
        try {
            env = r.getEnvironment(listener);
        } catch (Exception e) {
            listener.getLogger().println("Error retrieving environment vars: " + e.getMessage());
            env = new EnvVars();
        }
        integrationURL = env.expand(integrationURL);

        return new StandardSecureChatService(integrationURL);
    }

    @Override
    public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener) throws InterruptedException, IOException {
        return true;
    }

    @Override
    public boolean prebuild(AbstractBuild<?, ?> build, BuildListener listener) {
        if (startNotification) {
            Map<Descriptor<Publisher>, Publisher> map = build.getProject().getPublishersList().toMap();
            for (Publisher publisher : map.values()) {
                if (publisher instanceof SecureChatNotifier) {
                    logger.info("Invoking Started...");
                    new ActiveNotifier((SecureChatNotifier) publisher, listener).started(build);
                }
            }
        }
        return super.prebuild(build, listener);
    }

    @Extension
    public static class DescriptorImpl extends BuildStepDescriptor<Publisher> {

        private String integrationURL;
        private String buildServerUrl;
        private String sendAs;

        public static final CommitInfoChoice[] COMMIT_INFO_CHOICES = CommitInfoChoice.values();

        public DescriptorImpl() {
            load();
        }

        public String getIntegrationURL() {
            return integrationURL;
        }

        public String getBuildServerUrl() {
            if(buildServerUrl == null || buildServerUrl == "") {
                JenkinsLocationConfiguration jenkinsConfig = new JenkinsLocationConfiguration();
                return jenkinsConfig.getUrl();
            }
            else {
                return buildServerUrl;
            }
        }

        public String getSendAs() {
            return sendAs;
        }

        public boolean isApplicable(Class<? extends AbstractProject> aClass) {
            return true;
        }

        @Override
        public SecureChatNotifier newInstance(StaplerRequest sr, JSONObject json) {
            String integrationURL = sr.getParameter("secureChatIntegrationURL");
            boolean startNotification = "true".equals(sr.getParameter("secureChatStartNotification"));
            boolean notifySuccess = "true".equals(sr.getParameter("secureChatNotifySuccess"));
            boolean notifyAborted = "true".equals(sr.getParameter("secureChatNotifyAborted"));
            boolean notifyNotBuilt = "true".equals(sr.getParameter("secureChatNotifyNotBuilt"));
            boolean notifyUnstable = "true".equals(sr.getParameter("secureChatNotifyUnstable"));
            boolean notifyFailure = "true".equals(sr.getParameter("secureChatNotifyFailure"));
            boolean notifyBackToNormal = "true".equals(sr.getParameter("secureChatNotifyBackToNormal"));
            boolean notifyRepeatedFailure = "true".equals(sr.getParameter("secureChatNotifyRepeatedFailure"));
            boolean includeTestSummary = "true".equals(sr.getParameter("includeTestSummary"));
            CommitInfoChoice commitInfoChoice = CommitInfoChoice.forDisplayName(sr.getParameter("secureChatCommitInfoChoice"));
            boolean includeCustomMessage = "on".equals(sr.getParameter("includeCustomMessage"));
            String customMessage = sr.getParameter("customMessage");
            return new SecureChatNotifier(integrationURL, buildServerUrl, sendAs, startNotification, notifyAborted,
                    notifyFailure, notifyNotBuilt, notifySuccess, notifyUnstable, notifyBackToNormal, notifyRepeatedFailure,
                    includeTestSummary, commitInfoChoice, includeCustomMessage, customMessage);
        }

        @Override
        public boolean configure(StaplerRequest sr, JSONObject formData) throws FormException {
            integrationURL = sr.getParameter("secureChatIntegrationURL");
            buildServerUrl = sr.getParameter("secureChatBuildServerUrl");
            sendAs = sr.getParameter("secureChatSendAs");
            if(buildServerUrl == null || buildServerUrl == "") {
                JenkinsLocationConfiguration jenkinsConfig = new JenkinsLocationConfiguration();
                buildServerUrl = jenkinsConfig.getUrl();
            }
            if (buildServerUrl != null && !buildServerUrl.endsWith("/")) {
                buildServerUrl = buildServerUrl + "/";
            }
            save();
            return super.configure(sr, formData);
        }

        SecureChatService getSecureChatService(final String integrationURL) {
            return new StandardSecureChatService(integrationURL);
        }

        @Override
        public String getDisplayName() {
            return "secure.chat Notifications";
        }

        public FormValidation doTestConnection(@QueryParameter("secureChatIntegrationURL") final String integrationURL,
                                               @QueryParameter("secureChatBuildServerUrl") final String buildServerUrl) throws FormException {
            try {
                String targetURL = integrationURL;
                if (StringUtils.isEmpty(targetURL)) {
                    targetURL = this.integrationURL;
                }
                String targetBuildServerUrl = buildServerUrl;
                if (StringUtils.isEmpty(targetBuildServerUrl)) {
                    targetBuildServerUrl = this.buildServerUrl;
                }
                SecureChatService testSecureChatService = getSecureChatService(targetURL);
                String message = "secure.chat/Jenkins plugin: you're all set on " + targetBuildServerUrl;
                boolean success = testSecureChatService.publish(message, "good");
                return success ? FormValidation.ok("Success") : FormValidation.error("Failure");
            } catch (Exception e) {
                return FormValidation.error("Client error : " + e.getMessage());
            }
        }
    }
}
