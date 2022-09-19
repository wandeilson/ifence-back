package br.edu.ifpb.dac.groupd.tests.suites;

import org.junit.platform.commons.annotation.Testable;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

import br.edu.ifpb.dac.groupd.tests.unit.UnitTestsSuite;

@Suite
@Testable
@SelectClasses(UnitTestsSuite.class)
@SuiteDisplayName("Suite de Testes Unitários")
public class SuiteUnitTests {}
