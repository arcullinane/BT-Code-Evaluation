BT Code Evaluation 2017/2018
Node Status Reporting
Issue 2.1

Author: Andrew Cullinane
Language: Java 8
Tests: JUnit 5

This program takes input as a .txt file of node status information, collates all the information given, and generates a node status report. Each data point must be given on a single line and comprise of the following three elements;

Time Received, Time Generated, Node Status

Getting Started

The NodeStatusReport class contains a main method to which the .txt data file can passed as an argument on the command line. For example

$ java NodeStatusReport input.txt

Tests

The BTCodeTests class contains all the JUnit tests for this project. 