package uk.ac.ebi.subs.repository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;
import uk.ac.ebi.subs.repository.model.templates.AttributeCapture;
import uk.ac.ebi.subs.repository.model.templates.FieldCapture;
import uk.ac.ebi.subs.repository.model.templates.JsonFieldType;
import uk.ac.ebi.subs.repository.model.templates.Template;
import uk.ac.ebi.subs.repository.repos.TemplateRepository;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class TemplateRepoTest {

    @Autowired
    private TemplateRepository templateRepository;

    private final String templateName = "default-sample-template";
    private final String templateType = "samples";
    private final Pageable pageable = new PageRequest(0, 10);
    
    @Before
    public void buildUp() {
        templateRepository.deleteAll();

        Template template = exampleTemplate();

        templateRepository.insert(template);

    }

    @After
    public void tearDown() {
        templateRepository.deleteAll();
    }

    @Test
    public void fetchByName() {

        Template t = templateRepository.findOneByName(templateName);

        Assert.notNull(t);
        System.out.println(t);
    }

    @Test
    public void fetchByType() {

        Page<Template> page = templateRepository.findByTargetType(templateType, pageable);

        Assert.notNull(page);
        Assert.isTrue(page.getTotalElements() == 1L);

        System.out.println(page.getContent().get(0));
    }
    
    @Test
    public void fetchByTags_testPefectMatch(){
        List<Template> page = templateRepository.findByTargetTypeAndTagsAllMatch(templateType, Arrays.asList("tag1","tag2"));
        
        Assert.isTrue(page.size() == 1);
    }

    @Test
    public void fetchByTags_testPartialTagMatch(){
        List<Template> page = templateRepository.findByTargetTypeAndTagsAllMatch(templateType, Arrays.asList("tag1"));

        Assert.isTrue(page.size() == 1);
    }

    @Test
    public void fetchByTags_testNoTagMatch(){
        List<Template> page = templateRepository.findByTargetTypeAndTagsAllMatch(templateType, Arrays.asList("tag3"));

        Assert.isTrue(page.isEmpty());
    }

    @Test
    public void fetchByTags_testSomeTagsMissingMatch(){
        List<Template> page = templateRepository.findByTargetTypeAndTagsAllMatch(templateType, Arrays.asList("tag1","tag3"));

        Assert.isTrue(page.isEmpty());
    }

    private Template exampleTemplate() {
        Template template = Template.builder()
                .name(templateName)
                .targetType(templateType)
                .build();

        template
                .add(
                        "unique name",
                        FieldCapture.builder().fieldName("alias").build()
                )
                .add("title",
                        FieldCapture.builder().fieldName("title").build()
                )
                .add(
                        "description",
                        FieldCapture.builder().fieldName("description").build()
                )
                .add("taxon",
                        FieldCapture.builder().fieldName("taxon").build()
                )
                .add("taxon id",
                        FieldCapture.builder().fieldName("taxon id").fieldType(JsonFieldType.IntegerNumber).build()
                )
                .addTag("tag1")
                .addTag("tag2");
        

        template.setDefaultCapture(
                AttributeCapture.builder().build()
        );

        return template;
    }

}
