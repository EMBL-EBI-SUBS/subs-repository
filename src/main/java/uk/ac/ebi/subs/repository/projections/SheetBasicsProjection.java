package uk.ac.ebi.subs.repository.projections;

import org.springframework.data.rest.core.config.Projection;
import uk.ac.ebi.subs.data.component.Team;
import uk.ac.ebi.subs.repository.model.sheets.SheetStatusEnum;
import uk.ac.ebi.subs.repository.model.sheets.Spreadsheet;

import java.util.Date;

@Projection(name = "sheetBasics", types = {Spreadsheet.class})
public interface SheetBasicsProjection {

    String getSubmissionId();
    String getChecklistId();
    String getDataTypeId();
    String getSourceFileName();
    String getSheetName();
    SheetStatusEnum getStatus();
    Team getTeam();
    String getTargetType();
    Long getVersion();
    Date getCreatedDate();
    Date getLastModifiedDate();
    String getCreatedBy();
    String getLastModifiedBy();

    int getTotalRowCount();
    int getProcessedRowCount();

}
