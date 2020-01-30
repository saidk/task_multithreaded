# Multithreaded server access

Firstly, you need to configure Tomcat and Maven

## Maven

go to $MAVEN_HOME/conf/settings.xml and add this to servers tag

```bash
<server>
	  <id>maven-tomcat-war-deployment-server></id>
	  <username>war-deployer</username>
	  <password>maven-tomcat-plugin</password>
</server>
```

## Tomcat
In $TOMCAT/conf/tomcat_users.xml add a new user to <tomcat-users> tag

```bash
<user username="war-deployer" password="maven-tomcat-plugin"
                    roles="manager-gui, manager-script, manager-jmx" />
```

## Command Line
After all configurations run this command:

```bash
mvn install tomcat7:deploy
```

To run tests, run this command

```bash
mvn test
```
