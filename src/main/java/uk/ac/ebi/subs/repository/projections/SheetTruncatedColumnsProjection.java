package uk.ac.ebi.subs.repository.projections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;
import uk.ac.ebi.subs.repository.model.sheets.Row;
import uk.ac.ebi.subs.repository.model.sheets.Spreadsheet;
import uk.ac.ebi.subs.repository.model.templates.Capture;

import java.util.List;

@Projection(name = "truncatedColumns", types = {Spreadsheet.class})
public interface SheetTruncatedColumnsProjection extends SheetBasicsProjection {


    @Value("#{target.firstRows}")
    List<Row> getFirstRows();

    List<Capture> getMappings();

}
