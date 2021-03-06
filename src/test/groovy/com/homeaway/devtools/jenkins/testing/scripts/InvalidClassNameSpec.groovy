/*
 Copyright (c) 2020 Expedia Group.
 All rights reserved.  http://www.homeaway.com
 
 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at
 
   http://www.apache.org/licenses/LICENSE-2.0
 
 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

package com.homeaway.devtools.jenkins.testing.scripts;

import com.homeaway.devtools.jenkins.testing.InvalidlyNamedScriptWrapper
import com.homeaway.devtools.jenkins.testing.JenkinsPipelineSpecification

/**
 * Groovy script contents can be in files with any name.
 * Unlike compiled classes, the names don't have to be valid JVM class names.
 * 
 * Ensure that we can still test such scripts.
 *
 * @author awitt
 * @since 2.1.1
 */
public class InvalidClassNameSpec extends JenkinsPipelineSpecification {
	
	def "scripts whose names create invalid JVM class names are wrapped"() {
		setup:
			def a_script = loadPipelineScriptForTest( "com/homeaway/devtools/jenkins/testing/scripts/some-script.groovy" )
		expect:
			InvalidlyNamedScriptWrapper.class == a_script.getClass()
	}
	
	def "scripts whose names create valid JVM class names are not wrapped"() {
		setup:
			def a_script = loadPipelineScriptForTest( "com/homeaway/devtools/jenkins/testing/scripts/some_script.groovy" )
		expect:
			// Groovy generates subclasses of Script for each Script
			Script.class.isAssignableFrom( a_script.getClass() )
	}
	
	def "can run scripts whose names create invalid JVM class names"() {
		setup:
			def a_script = loadPipelineScriptForTest( "com/homeaway/devtools/jenkins/testing/scripts/some-script.groovy" )
		when:
			a_script.run()
		then:
			1 * getPipelineMock( "echo" )( "hello" )
			1 * getPipelineMock( "echo" )( "helped" )
	}
	
	def "can run methods in scripts whose names create invalid JVM class names"() {
		setup:
			def a_script = loadPipelineScriptForTest( "com/homeaway/devtools/jenkins/testing/scripts/some-script.groovy" )
		when:
			a_script.helper_method()
		then:
			1 * getPipelineMock( "echo" )( "helped" )
	}
	
	def "can get global variables in scripts whose names create invalid JVM class names"() {
		setup:
			def a_script = loadPipelineScriptForTest( "com/homeaway/devtools/jenkins/testing/scripts/some-script.groovy" )
		expect:
			"GLOBAL" == a_script.global_variable
	}
	
	def "can set script variables in scripts whose names create invalid JVM class names"() {
		setup:
			def a_script = loadPipelineScriptForTest( "com/homeaway/devtools/jenkins/testing/scripts/some-script.groovy" )
			a_script.getBinding().setVariable("new_property", "expected")
		expect:
			"expected" == a_script.getBinding().getVariable("new_property")
	}
	
}

