package jenkins.plugins.securechat;

public interface SecureChatService {
    boolean publish(String message);

    boolean publish(String message, String color);
}
