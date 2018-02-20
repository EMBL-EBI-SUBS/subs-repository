package uk.ac.ebi.subs.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;
import uk.ac.ebi.subs.repository.model.SubmittablesBatch;
import uk.ac.ebi.subs.repository.repos.SubmittablesBatchRepository;
import uk.ac.ebi.subs.utils.JsonHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class SubmittablesBatchRepostoryTest {

    @Autowired
    private SubmittablesBatchRepository submittablesBatchRepository;

    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    SubmittablesBatch batch;

    @Before
    public void buildUp() {
        submittablesBatchRepository.deleteAll();

        SubmittablesBatch batch = exampleBatch();

        submittablesBatchRepository.insert(batch);

    }

    private SubmittablesBatch exampleBatch() {
        batch = new SubmittablesBatch();
        batch.setId("1234");
        batch.setName("my-samples.csv");

        batch.addDocument(document("{}"));
        batch.addDocument(document("[]"));

        batch.getDocuments().get(0).setProcessed(true);

        try {
            batch.setLastModifiedDate( sdf.parse("11-02-2012"));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        batch.setStatus("Completed");
        return batch;
    }



    private SubmittablesBatch.Document document(String json) {
        SubmittablesBatch.Document d = new SubmittablesBatch.Document();
        d.setDocument(JsonHelper.stringToJsonNode(json));
        return d;
    }

    @After
    public void tearDown() {
        submittablesBatchRepository.deleteAll();
    }

    @Test
    public void fetchAll() {

        Page<SubmittablesBatch> batches = submittablesBatchRepository.findAll(new PageRequest(0, 10));

        Assert.notNull(batches);
        Assert.isTrue(batches.getTotalElements() == 1L);
    }

    @Test
    public void removeByDateAndStatus_testDateAndStatusMatch() throws ParseException {
        //test data has last modified date of 11-02-2012 and Status = completed

        submittablesBatchRepository.removeByLastModifiedDateBeforeAndStatus(sdf.parse("12-02-2012"),"Completed");

        Assert.isNull(submittablesBatchRepository.findOne(batch.getId()) );
    }

    @Test
    public void removeByDateAndStatus_testDateDoesNotMatch() throws ParseException {
        //test data has last modified date of 11-02-2012 and Status = completed

        submittablesBatchRepository.removeByLastModifiedDateBeforeAndStatus(sdf.parse("10-02-2012"),"Completed");

        Assert.notNull(submittablesBatchRepository.findOne(batch.getId()) );
    }
}
