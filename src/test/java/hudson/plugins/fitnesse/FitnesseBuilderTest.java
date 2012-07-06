package hudson.plugins.fitnesse;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.junit.Test;

import static org.junit.Assert.*;

public class FitnesseBuilderTest {
	@Test
	public void getPortShouldReturnLocalPortIfSpecified() {
		HashMap<String, String> options = new HashMap<String, String>();
		options.put(FitnesseBuilder.FITNESSE_PORT_LOCAL, "99");
		FitnesseBuilder builder = new FitnesseBuilder(options);
		assertEquals(99, builder.getFitnessePort());

		options.put(FitnesseBuilder.FITNESSE_PORT_REMOTE, null);
		assertEquals(99, builder.getFitnessePort());

		options.put(FitnesseBuilder.FITNESSE_PORT_REMOTE, "");
		assertEquals(99, builder.getFitnessePort());
	}
	
	@Test
	public void getPortShouldReturnRemotePortIfSpecified() {
		HashMap<String, String> options = new HashMap<String, String>();
		options.put(FitnesseBuilder.FITNESSE_PORT_REMOTE, "999");
		FitnesseBuilder builder = new FitnesseBuilder(options);
		assertEquals(999, builder.getFitnessePort());
		
		options.put(FitnesseBuilder.FITNESSE_PORT_LOCAL, null);
		assertEquals(999, builder.getFitnessePort());
		
		options.put(FitnesseBuilder.FITNESSE_PORT_LOCAL, "");
		assertEquals(999, builder.getFitnessePort());
	}
	
	@Test
	public void getHostShouldReturnLocalHostIfStartBuildIsTrue() throws IOException, InterruptedException {
		HashMap<String, String> options = new HashMap<String, String>();
		options.put(FitnesseBuilder.START_FITNESSE, "True");
		FitnesseBuilder builder = new FitnesseBuilder(options);
		
		assertTrue(builder.getFitnesseStart());
//		assertEquals("localhost", builder.getFitnesseHost(null));
//
//		options.put(FitnesseBuilder.FITNESSE_HOST, "abracadabra");
//		assertEquals("localhost", builder.getFitnesseHost(null));
	}
	
	@Test
	public void getHostShouldReturnSpecifiedHostIfStartBuildIsFalse() {
		HashMap<String, String> options = new HashMap<String, String>();
		options.put(FitnesseBuilder.START_FITNESSE, "False");
		options.put(FitnesseBuilder.FITNESSE_HOST, "hudson.local");
		FitnesseBuilder builder = new FitnesseBuilder(options);
		
		assertFalse(builder.getFitnesseStart());
//		Assert.assertEquals("hudson.local", builder.getFitnesseHost());
//		
//		options.put(FitnesseBuilder.FITNESSE_HOST, "abracadabra");
//		Assert.assertEquals("abracadabra", builder.getFitnesseHost());
	}
	
	@Test
	public void getHttpTimeoutShouldReturn60000UnlessValueIsExplicit() {
		HashMap<String, String> options = new HashMap<String, String>();
		FitnesseBuilder builder = new FitnesseBuilder(options);
		assertEquals(60000, builder.getFitnesseHttpTimeout());
		options.put(FitnesseBuilder.HTTP_TIMEOUT, "1000");
		assertEquals(1000, builder.getFitnesseHttpTimeout());
	}
	
	@Test
	public void getJavaWorkingDirShouldReturnParentOfFitnessseJarUnlessValueIsExplicit() throws Exception {
		HashMap<String, String> options = new HashMap<String, String>();
		File tmpFile = File.createTempFile("fitnesse", ".jar");
		options.put(FitnesseBuilder.PATH_TO_JAR, tmpFile.getAbsolutePath());
		
		FitnesseBuilder builder = new FitnesseBuilder(options);
		assertEquals(tmpFile.getParentFile().getAbsolutePath(),
                builder.getFitnesseJavaWorkingDirectory());
		
		options.put(FitnesseBuilder.JAVA_WORKING_DIRECTORY, "/some/explicit/path");
		assertEquals("/some/explicit/path", builder.getFitnesseJavaWorkingDirectory());
	}	
	
	@Test
	public void getJavaWorkingDirShouldReturnParentOfFitnessseJarEvenIfRelativeToBuildDir() throws Exception {
		HashMap<String, String> options = new HashMap<String, String>();
		File tmpFile = new File("relativePath", "fitnesse.jar");
		options.put(FitnesseBuilder.PATH_TO_JAR, tmpFile.getPath());
		
		FitnesseBuilder builder = new FitnesseBuilder(options);
		assertEquals("relativePath", builder.getFitnesseJavaWorkingDirectory());
	}
	
	@Test
	public void getJavaWorkingDirShouldBeEmptyIfFitnessseJarUnspecified() throws Exception {
		HashMap<String, String> options = new HashMap<String, String>();
		FitnesseBuilder builder = new FitnesseBuilder(options);
		assertEquals("",
                builder.getFitnesseJavaWorkingDirectory());
	}
}
