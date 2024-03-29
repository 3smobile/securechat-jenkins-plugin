# secure.chat plugin for Jenkins

Started with a fork of the Slack plugin:

https://github.com/jenkinsci/slack-plugin

Which started with a fork of the HipChat plugin:

https://github.com/jlewallen/jenkins-hipchat-plugin

Which was, in turn, a fork of the Campfire plugin.

# Jenkins Instructions

1. Get a secure.chat account: https://secure.chat/
2. Configure the Jenkins integration: http://secure.chat/m/yourteam/admin/apps/edit/jenkins
3. Install this plugin on your Jenkins server
4. Configure it in your Jenkins job and **add it as a Post-build action**.

# Developer instructions

Install Maven and JDK.  This was last build with Maven 3.2.5 and OpenJDK
1.7.0\_75 on KUbuntu 14.04.

Run unit tests

    mvn test

Create an HPI file to install in Jenkins (HPI file will be in `target/securechat.hpi`).

    mvn package
