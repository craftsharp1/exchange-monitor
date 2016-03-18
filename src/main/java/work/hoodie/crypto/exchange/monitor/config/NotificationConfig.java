package work.hoodie.crypto.exchange.monitor.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.client.RestTemplate;
import work.hoodie.crypto.exchange.monitor.service.notification.NotificationTypeFinder;
import work.hoodie.crypto.exchange.monitor.service.notification.message.sender.EmailMessageSenderService;
import work.hoodie.crypto.exchange.monitor.service.notification.message.sender.SlackMessageSenderService;

import javax.annotation.PostConstruct;
import java.util.Properties;

@Slf4j
@Configuration("Notification")
@PropertySource("classpath:/email.properties")
public class NotificationConfig {

    //Properties File Settings
    @Value("${email.defaultEncoding:}")
    private String mailDefaultEncoding;
    @Value("${email.port:25}")
    private int mailPort;
    @Value("${email.debug:}")
    private String mailDebug;
    @Value("${email.smtp.auth:}")
    private String mailSmtpAuth;
    @Value("${email.smtp.socketFactory.class:}")
    private String mailSmtpSocketFactoryClass;
    @Value("${email.smtp.socketFactory.fallback:}")
    private String mailSmtpSocketFactoryFallback;
    @Value("${email.smtp.ssl:}")
    private String mailSmtpSsl;

    //Docker Container Settings
    @Value("${slack.url:}")
    private String slackUrl;

    @Value("${email.address:}")
    private String emailAddress;

    @Value("${email.host:}")
    private String mailHost;
    @Value("${email.username:}")
    private String mailUsername;
    @Value("${email.password:}")
    private String mailPassword;

    @Autowired
    private NotificationTypeFinder notificationTypeFinder;

    @PostConstruct
    public void init() {
        log.info("------- Notification Configuration -------");
        log.info("Notification Type: " + notificationTypeFinder.find());
        log.info("Slack Url: " + slackUrl);
        log.info("Email Address: " + emailAddress);
        log.info("SMTP Mail Host: " + mailHost);
        log.info("SMTP Mail Username: " + mailUsername);
        log.info("SMTP Mail Password: " + mailPassword);
        log.info("------------------------------------------");

    }


    @Bean
    public EmailMessageSenderService emailMessageSenderService() {

        JavaMailSenderImpl javaMailSenderImpl = new JavaMailSenderImpl();

        javaMailSenderImpl.setDefaultEncoding(mailDefaultEncoding);
        javaMailSenderImpl.setHost(mailHost);
        javaMailSenderImpl.setPort(mailPort);
        javaMailSenderImpl.setUsername(mailUsername);
        javaMailSenderImpl.setPassword(mailPassword);

        Properties javaMailProperties = new Properties();
        javaMailProperties.put("mail.debug", StringUtils.defaultString(mailDebug));
        javaMailProperties.put("mail.smtp.auth", StringUtils.defaultString(mailSmtpAuth));
        javaMailProperties.put("mail.smtp.socketFactory.class", StringUtils.defaultString(mailSmtpSocketFactoryClass));
        javaMailProperties.put("mail.smtp.socketFactory.fallback", StringUtils.defaultString(mailSmtpSocketFactoryFallback));
        javaMailProperties.put("mail.smtp.ssl", StringUtils.defaultString(mailSmtpSsl));

        javaMailSenderImpl.setJavaMailProperties(javaMailProperties);

        return new EmailMessageSenderService(javaMailSenderImpl);
    }

    @Bean
    public SlackMessageSenderService slackMessageSenderService() {
        return new SlackMessageSenderService(slackUrl, new RestTemplate());
    }
}
