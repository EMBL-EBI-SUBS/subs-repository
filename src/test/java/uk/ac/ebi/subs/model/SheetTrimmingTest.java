package uk.ac.ebi.subs.model;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import uk.ac.ebi.subs.repository.model.sheets.Spreadsheet;

import java.util.Optional;

@RunWith(JUnit4.class)
public class SheetTrimmingTest {

    private Spreadsheet sheet;

    @Before
    public void buildUp() {
        sheet = new Spreadsheet();
        sheet.addRow(new String[]{"a1", "b1", "c1"});
        sheet.addRow(new String[]{"a2", "b2", "c2"});
        sheet.addRow(new String[]{"a3", "b3"});
    }

    @Test
    public void trimMiddleColumn() {
        Spreadsheet expected = new Spreadsheet();
        expected.addRow(new String[]{"a1", "c1"});
        expected.addRow(new String[]{"a2", "c2"});
        expected.addRow(new String[]{"a3"});

        sheet.deleteColumn(1);

        Assert.assertEquals(expected, sheet);
    }

    @Test
    public void trimAfterMiddleColumn() {
        Spreadsheet expected = new Spreadsheet();
        expected.addRow(new String[]{"a1", "b1"});
        expected.addRow(new String[]{"a2", "b2"});
        expected.addRow(new String[]{"a3", "b3"});

        sheet.deleteColumnsPastIndex(1);

        Assert.assertEquals(expected, sheet);
    }

    @Test
    public void lastNonEmptyColumn() {
        sheet = new Spreadsheet();
        sheet.addRow(new String[]{"a1", " ", "b1"});
        sheet.addRow(new String[]{"a2", null, "b2"});
        sheet.addRow(new String[]{"a3", "", "b3", "", null, "     "});

        Optional<Integer> expectedLastNonEmptyColumn = Optional.of(2);

        Assert.assertEquals(expectedLastNonEmptyColumn, sheet.indexOfLastNonEmptyColumn());
    }

    @Test
    public void removeEmptyRows() {
        sheet = new Spreadsheet();
        sheet.addRow(new String[]{"a1", " ", "b1"});
        sheet.addRow(new String[]{});
        sheet.addRow(new String[]{"", null, "     "});

        Spreadsheet expected = new Spreadsheet();
        expected.addRow(new String[]{"a1", " ", "b1"});

        sheet.removeEmptyRows();

        Assert.assertEquals(expected, sheet);
    }

    @Test
    public void trimToUsedSpace() {
        sheet = new Spreadsheet();
        sheet.addRow(new String[]{"a1", " ", "c1", null, null, null});
        sheet.addRow(new String[]{"a2", "b2", "c2"});
        sheet.addRow(new String[]{"", null, "     "});

        Spreadsheet expected = new Spreadsheet();
        expected.addRow(new String[]{"a1", " ", "c1"});
        expected.addRow(new String[]{"a2", "b2", "c2"});
        expected.addRow(new String[]{"", null, "     "});

        sheet.removeColumnsPastLastNonEmpty();

        Assert.assertEquals(expected, sheet);
    }


}
