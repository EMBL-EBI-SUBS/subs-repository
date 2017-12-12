package uk.ac.ebi.subs.repository;

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
import uk.ac.ebi.subs.repository.model.sheets.Sheet;
import uk.ac.ebi.subs.repository.model.templates.Template;
import uk.ac.ebi.subs.repository.repos.SheetRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class SheetRepostoryTest {

    @Autowired
    private SheetRepository sheetRepository;

    @Before
    public void buildUp() {
        sheetRepository.deleteAll();

        Sheet sheet = exampleSheet();

        sheetRepository.insert(sheet);

    }

    private Sheet exampleSheet() {
        Sheet sheet = new Sheet();
        sheet.setTemplate(Template.builder().targetType("thing").name("bob").build());
        sheet.setHeaderRowIndex(1);
        sheet.addRow(new String[]{"header1", "header2", "header3", "header4"});
        sheet.addRow(new String[]{"a", "", "b", "c"});
        sheet.addRow(new String[]{"4", "", "5", "6"});
        return sheet;
    }

    @After
    public void tearDown() {
        sheetRepository.deleteAll();
    }

    @Test
    public void fetchAll() {

        Page<Sheet> sheets = sheetRepository.findAll(new PageRequest(0,10));

        Assert.notNull(sheets);
        Assert.isTrue(sheets.getTotalElements() == 1L);
        System.out.println(sheets.getContent());
    }

}
