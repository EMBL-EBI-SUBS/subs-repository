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
import uk.ac.ebi.subs.repository.model.sheets.Row;
import uk.ac.ebi.subs.repository.model.sheets.Spreadsheet;
import uk.ac.ebi.subs.repository.repos.SpreadsheetRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class SheetRepostoryTest {

    @Autowired
    private SpreadsheetRepository spreadsheetRepository;

    @Before
    public void buildUp() {
        spreadsheetRepository.deleteAll();

        Spreadsheet sheet = exampleSheet();

        spreadsheetRepository.insert(sheet);

    }

    private Spreadsheet exampleSheet() {
        Spreadsheet sheet = new Spreadsheet();
        sheet.setHeaderRow(new Row(new String[]{"header1", "header2", "header3", "header4"}));
        sheet.addRow(new String[]{"a", "", "b", "c"});
        sheet.addRow(new String[]{"4", "", "5", "6"});

        return sheet;
    }

    @After
    public void tearDown() {
        spreadsheetRepository.deleteAll();
    }

    @Test
    public void fetchAll() {

        Page<Spreadsheet> sheets = spreadsheetRepository.findAll(new PageRequest(0,10));

        Assert.notNull(sheets, "[Assertion failed] - sheets argument required; it must not be null");
        Assert.isTrue(sheets.getTotalElements() == 1L, "[Assertion failed] - there should be only 1 spreadsheet");
        System.out.println(sheets.getContent());
    }

    @Test
    public void findBySubmissionIdAndDataTypeIdOrderByCreatedDateDesc(){
        spreadsheetRepository.findBySubmissionIdAndDataTypeIdOrderByCreatedDateDesc("bob","bob",
                new PageRequest(0,10));
    }
}
