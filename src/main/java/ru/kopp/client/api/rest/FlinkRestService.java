package ru.kopp.client.api.rest;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import ru.kopp.client.model.jar.FlinkJar;
import ru.kopp.client.model.jar.FlinkUploadedJars;
import ru.kopp.client.model.jobs.FlinkJob;
import ru.kopp.client.model.jobs.FlinkJobSubmission;
import ru.kopp.client.model.jobs.FlinkJobsSet;
import ru.kopp.client.model.plan.FlinkUploadedPlans;
import ru.kopp.client.model.responses.FlinkConfig;
import ru.kopp.services.FlinkErrorService;
import ru.kopp.services.FlinkLoggerService;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

@SuppressWarnings({"FieldCanBeLocal", "unused"})
public class FlinkRestService {

    private static FlinkRestService flinkRestService;

    private final String flinkConfigUrl = "/config";
    private final String getFlinkJarsUrl = "/jars";
    private final String uploadJarUrl = "upload";
    private final String jarListUrl = "/jars/";
    private final String getJarPlanUrl = "/plan";
    private final String submitJarUrl = "/run";
    private final String getFlinkJobstUrl = "/jobs/overview";
    private final String jobListUrl = "/jobs/";
    private final String getJobExceptionsUrl = "/exceptions";

    private final Logger logger = FlinkLoggerService.getLogger();
    private final Moshi moshi = new Moshi.Builder().build();
    private final CloseableHttpClient httpClient;
    private String mainUrl;

    private FlinkRestService() {
        httpClient = HttpClients.createDefault();
    }

    public static FlinkRestService getFlinkRestService() {
        if (flinkRestService == null) flinkRestService = new FlinkRestService();
        return flinkRestService;
    }

    public String getMainUrl() {
        return mainUrl;
    }

    public void setMainUrl(String mainUrl) {
        this.mainUrl = mainUrl;
    }

    public FlinkConfig connect() {
        HttpGet httpGet = new HttpGet(mainUrl + getFlinkJarsUrl);
        FlinkConfig flinkConfig=new FlinkConfig();

        try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
            int status = response.getStatusLine().getStatusCode();
            HttpEntity entity = response.getEntity();

            JsonAdapter<FlinkConfig> jsonAdapter = moshi.adapter(FlinkConfig.class);
            flinkConfig=jsonAdapter.fromJson(EntityUtils.toString(entity));

            if(flinkConfig==null || flinkConfig.getFlinkVersion()==null)
                FlinkErrorService.showError(
                        "HTTP Request Failed",
                        new FlinkResponse("Failed to connect on " + mainUrl + "!")
                );

        } catch (IOException e) {
            FlinkErrorService.showError(
                    "HTTP Request Failed",
                    new FlinkResponse("Failed to connect on " + mainUrl + "!")
            );
        }
        return flinkConfig;
    }

    public void close() {
        try {
            httpClient.close();
        } catch (IOException e) {
            logger.warning("Error closing HTTP client connection!");
        }
    }

    public List<FlinkJar> getJars() {
        HttpGet httpGet = new HttpGet(mainUrl + getFlinkJarsUrl);
        List<FlinkJar> flinkJars = Collections.emptyList();

        try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
            HttpEntity entity = response.getEntity();

            JsonAdapter<FlinkUploadedJars> jsonAdapter = moshi.adapter(FlinkUploadedJars.class);

            FlinkUploadedJars flinkUploadedJars = jsonAdapter.fromJson(EntityUtils.toString(entity));

            if (flinkUploadedJars != null) flinkJars = flinkUploadedJars.getJars();
        } catch (IOException e) {
            logger.warning("Error performing HTTP GET request on URL " + mainUrl + getFlinkJarsUrl + "!");
        }
        return flinkJars;
    }

    public FlinkResponse uploadJar(File jarFile) {
        HttpPost httpPost = new HttpPost(mainUrl + jarListUrl + uploadJarUrl);

        MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
        entityBuilder.addBinaryBody("jarfile", jarFile);

        httpPost.setEntity(entityBuilder.build());

        try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
            httpPost.releaseConnection();
            int status = response.getStatusLine().getStatusCode();

            if (status == 200)
                logger.info("Jar file " + jarFile.getName() + " uploaded successfully.");

            return new FlinkResponse(
                    status,
                    response.toString(),
                    EntityUtils.toString(response.getEntity())
            );

        } catch (IOException e) {
            return new FlinkResponse("Error performing HTTP POST request on URL " + mainUrl + jarListUrl + uploadJarUrl + "!");
        }
    }

    public FlinkResponse deleteJar(String id) {
        HttpDelete httpDelete = new HttpDelete(mainUrl + jarListUrl + id);

        try (CloseableHttpResponse response = httpClient.execute(httpDelete)) {
            httpDelete.releaseConnection();
            int status = response.getStatusLine().getStatusCode();

            if (status == 200)
                logger.info("Jar file has been deleted successfully.");

            return new FlinkResponse(
                    status,
                    response.toString(),
                    EntityUtils.toString(response.getEntity())
            );

        } catch (IOException e) {
            return new FlinkResponse("Error performing HTTP DELETE request on URL " + mainUrl + jarListUrl + "$JarId$!");
        }
    }

    public FlinkUploadedPlans getJarPlan(String id) {
        HttpGet httpGet = new HttpGet(mainUrl + jarListUrl + id + getJarPlanUrl);
        FlinkUploadedPlans jarPlans = null;

        try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
            HttpEntity entity = response.getEntity();
            int status = response.getStatusLine().getStatusCode();

            if (status == 200)
                logger.info("Jar Plan received successfully.");

            JsonAdapter<FlinkUploadedPlans> jsonAdapter = moshi.adapter(FlinkUploadedPlans.class);
            jarPlans = jsonAdapter.fromJson(EntityUtils.toString(entity));

        } catch (IOException e) {
            FlinkErrorService.showError(
                    "HTTP Request Failed",
                    new FlinkResponse("Error performing HTTP GET request on URL " + mainUrl + jarListUrl + "$JarId$" + getJarPlanUrl + "!")
            );
        }
        return jarPlans;
    }

    public FlinkResponse submitJob(FlinkJobSubmission flinkJobSubmission, String jarId) {
        HttpPost httpPost = new HttpPost(mainUrl + jarListUrl + jarId + submitJarUrl);

        JsonAdapter<FlinkJobSubmission> jsonAdapter = moshi.adapter(FlinkJobSubmission.class);

        String json = jsonAdapter.toJson(flinkJobSubmission);
        try {
            httpPost.setEntity(new StringEntity(json));
        } catch (UnsupportedEncodingException e) {
            FlinkErrorService.showError(
                    "Encoding Error ",
                    new FlinkResponse("Unsupported encoding flink job detected!\n" + e.getMessage())
            );
        }

        try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
            httpPost.releaseConnection();
            int status = response.getStatusLine().getStatusCode();

            if (status == 200)
                logger.info("Job " + flinkJobSubmission.getEntryClass() + " submitted successfully.");

            return new FlinkResponse(
                    status,
                    response.toString(),
                    EntityUtils.toString(response.getEntity())
            );

        } catch (IOException e) {
            return new FlinkResponse("Bad Request on " + mainUrl + jarListUrl + "$JarId$" + submitJarUrl + "!");
        }
    }

    public List<FlinkJob> getJobs() {
        HttpGet httpGet = new HttpGet(mainUrl + getFlinkJobstUrl);
        List<FlinkJob> flinkJobs = Collections.emptyList();

        try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
            HttpEntity entity = response.getEntity();

            if (response.getStatusLine().getStatusCode() != 200)
                logger.warning("Failed to get job list\n" + response);
            else {
                JsonAdapter<FlinkJobsSet> jsonAdapter = moshi.adapter(FlinkJobsSet.class);

                FlinkJobsSet flinkJobsSet = jsonAdapter.fromJson(EntityUtils.toString(entity));

                if (flinkJobsSet != null) flinkJobs = flinkJobsSet.getFlinkJobs();
            }

        } catch (IOException e) {
            logger.warning("Error performing HTTP GET request on URL " + mainUrl + getFlinkJobstUrl + "!");
        }
        return flinkJobs;
    }

    public FlinkResponse getJobExceptions(String id) {
        HttpGet httpGet = new HttpGet(mainUrl + jobListUrl + id + getJobExceptionsUrl);

        try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
            HttpEntity entity = response.getEntity();
            int status = response.getStatusLine().getStatusCode();

            return new FlinkResponse(
                    status,
                    response.toString(),
                    EntityUtils.toString(response.getEntity())
            );

        } catch (IOException e) {
            return new FlinkResponse("Error performing HTTP GET request on URL " + mainUrl + jobListUrl + "$JarId$" + getJobExceptionsUrl + "!");
        }
    }
}