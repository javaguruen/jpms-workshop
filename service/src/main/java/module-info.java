module com.jpmsworkshop.students.service {
    requires com.jpmsworkshop.students.api;
    requires spring.beans;
    requires spring.context;
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires java.sql;
    requires spring.jdbc;
    requires spring.web;
    requires spring.webmvc;
    opens com.jpmsworkshop.students.service;
/*
    requires guava;
    requires springfox.core;
    requires springfox.spi;
    requires springfox.spring.web;
    requires springfox.swagger2;
*/
}