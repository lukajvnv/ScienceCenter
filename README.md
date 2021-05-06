# Science Center

This repo is created for the purpose of the Business Process Managment (BPM) course.

The goal was creating platform which supports publishing and sharing articles of scientific magazines.

Basic functionalities of the application are:
* User registration (possible roles are common user, author, reviewer, magazine's editor)
* Adding new magazine in system
* Publishing new article in the magazine
* Article processing by editor and reviewers
* Searching articles
* Requesting access to the article and paying if necessary
* Integration with 3rd party payment software

**Registration**, **Adding new magazine** and **publishing new article** are implemented as business process using Camunda BPM platform. Business models of these three processes are imported and integrated with Java.

## Technologies used
* [Angular](https://angular.io/docs)
* [Spring Boot](https://spring.io/projects/spring-boot)
* [Camunda Platform](https://docs.camunda.org/manual/7.15/)
