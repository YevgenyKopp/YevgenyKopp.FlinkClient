package ru.kopp.client.api.rest;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.kopp.client.model.jar.FlinkJar;
import ru.kopp.client.model.jobs.FlinkJob;
import ru.kopp.client.model.jobs.FlinkJobSubmission;
import ru.kopp.client.model.plan.FlinkUploadedPlans;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

public class FlinkRestServiceTest {
    private File jarFile;
    private FlinkRestService flinkRestService;
    private List<FlinkJar> flinkJars;
    private String uploadedJarId;

    @Before
    public void setUp() throws URISyntaxException {
        URL jarFileUrl = getClass().getClassLoader().getResource("FlinkRestServiceTest.jar");
        if (jarFileUrl != null) {
            jarFile = new File(jarFileUrl.toURI());
        }

        flinkRestService = FlinkRestService.getFlinkRestService();
        flinkRestService.setMainUrl("http://localhost:8081");
        flinkRestService.connect();
    }

    @After
    public void tearDown() {
        flinkRestService.setMainUrl("");
        flinkRestService.close();
    }

    @Test
    public void jarsTest() {
        FlinkUploadedPlans uploadedJarPlans;
        FlinkResponse response;

        //upload jar
        response = flinkRestService.uploadJar(jarFile);
        Assert.assertEquals(200, response.getStatusCode());

        //get jars
        flinkJars = flinkRestService.getJars();
        Assert.assertFalse(flinkJars.isEmpty());

        //check jar existence
        Assert.assertNotNull(getUploadedJar());

        //get jar plan
        uploadedJarPlans = flinkRestService.getJarPlan(uploadedJarId);
        Assert.assertNotNull(uploadedJarPlans);

        //submit job
        int initialJobsSize = flinkRestService.getJobs().size();
        FlinkJobSubmission flinkJobSubmission = new FlinkJobSubmission();
        flinkJobSubmission.setAllowNonRestoredState(true);
        flinkJobSubmission.setParallelism(1);

        response = flinkRestService.submitJob(flinkJobSubmission, uploadedJarId);
        Assert.assertEquals(200, response.getStatusCode());

        //Check Completed
        List<FlinkJob> flinkJobs = flinkRestService.getJobs();
        Assert.assertEquals(1, flinkJobs.size() - initialJobsSize);

        //delete jar
        response = flinkRestService.deleteJar(uploadedJarId);
        Assert.assertEquals(200, response.getStatusCode());

        //check jar existence
        flinkJars = flinkRestService.getJars();
        Assert.assertNull(getUploadedJar());
    }

    private FlinkJar getUploadedJar() {
        FlinkJar uploadedJar = null;
        if (flinkJars.isEmpty()) return null;
        for (FlinkJar flinkJar : flinkJars) {
            if (flinkJar.getName().equals(jarFile.getName())) {
                uploadedJar = flinkJar;
                uploadedJarId = flinkJar.getId();
                break;
            }
        }
        return uploadedJar;
    }
}