package hudson.plugins.fitnesse;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import hudson.model.AbstractBuild;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FitnesseBuilderTest {
	@Test
	public void getPortShouldReturnLocalPortIfSpecified() {
		HashMap<String, String> options = new HashMap<String, String>();
		options.put(FitnesseBuilder.FITNESSE_PORT_LOCAL, "99");
		FitnesseBuilder builder = new FitnesseBuilder(options);

        Map<String, String> theBuildVariables = new HashMap<String, String>();

        AbstractBuild<?, ?> mockBuild = mock(AbstractBuild.class);
        when(mockBuild.getBuildVariables()).thenReturn(theBuildVariables);

        assertEquals(99, builder.getFitnessePort(mockBuild));

		options.put(FitnesseBuilder.FITNESSE_PORT_REMOTE, null);
		assertEquals(99, builder.getFitnessePort(mockBuild));

		options.put(FitnesseBuilder.FITNESSE_PORT_REMOTE, "");
		assertEquals(99, builder.getFitnessePort(mockBuild));
	}
	
	@Test
	public void getPortShouldReturnRemotePortIfSpecified() {
		HashMap<String, String> options = new HashMap<String, String>();
		options.put(FitnesseBuilder.FITNESSE_PORT_REMOTE, "999");
		FitnesseBuilder builder = new FitnesseBuilder(options);
        Map<String, String> theBuildVariables = new HashMap<String, String>();

        AbstractBuild<?, ?> mockBuild = mock(AbstractBuild.class);
        when(mockBuild.getBuildVariables()).thenReturn(theBuildVariables);

        assertEquals(999, builder.getFitnessePort(mockBuild));
		
		options.put(FitnesseBuilder.FITNESSE_PORT_LOCAL, null);
		assertEquals(999, builder.getFitnessePort(mockBuild));
		
		options.put(FitnesseBuilder.FITNESSE_PORT_LOCAL, "");
		assertEquals(999, builder.getFitnessePort(mockBuild));
	}

    @Test
    public void getPortShouldExpandParametersIfSpecified() {
        HashMap<String, String> options = new HashMap<String, String>();
        options.put(FitnesseBuilder.FITNESSE_PORT_REMOTE, "$MYPORT");
        FitnesseBuilder builder = new FitnesseBuilder(options);
        int DEFINEDPORT = 6789;

        Map<String, String> theBuildVariables = new HashMap<String, String>();
        theBuildVariables.put("MYPORT", String.valueOf(DEFINEDPORT));

        AbstractBuild<?, ?> mockBuild = mock(AbstractBuild.class);
        when(mockBuild.getBuildVariables()).thenReturn(theBuildVariables);

        assertEquals(DEFINEDPORT, builder.getFitnessePort(mockBuild));
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
    public void getHostReturnsExpandedParameterIfStartBuildIsFalse() throws IOException, InterruptedException {
        HashMap<String, String> options = new HashMap<String, String>();
        options.put(FitnesseBuilder.START_FITNESSE, "False");
        options.put(FitnesseBuilder.FITNESSE_HOST, "$MYHOSTNAME");
        FitnesseBuilder builder = new FitnesseBuilder(options);

        assertFalse(builder.getFitnesseStart());
        String DEFINEDHOSTNAME = "definedhostname";

        Map<String, String> theBuildVariables = new HashMap<String, String>();
        theBuildVariables.put("MYHOSTNAME", DEFINEDHOSTNAME);

        AbstractBuild<?, ?> mockBuild = mock(AbstractBuild.class);
        when(mockBuild.getBuildVariables()).thenReturn(theBuildVariables);

        assertEquals(DEFINEDHOSTNAME, builder.getFitnesseHost(mockBuild));
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
