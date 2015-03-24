Feature: As a developer in order to report unit tests of .net
  we run vstest.console.exe on the unit test files

  Background: 
    Given invoking OpenCoverTestExecutionCoverageSensor

  Scenario: No unit tests
    Given no unit tests in
    Then there should by 0 tests reported
