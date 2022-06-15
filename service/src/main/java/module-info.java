module com.jpmsworkshop.students.service {
    requires java.sql;
    requires spring.beans;
    requires spring.jdbc;
    requires spring.context;
    opens com.jpmsworkshop.students.service;
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.web;
    requires spring.webmvc;
    requires com.jpmsworkshop.students.api;

}
