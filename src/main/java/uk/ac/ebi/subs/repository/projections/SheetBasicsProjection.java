package uk.ac.ebi.subs.repository.projections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;
import uk.ac.ebi.subs.repository.model.sheets.Sheet;

import java.util.Date;

@Projection(name = "sheetBasics", types = {Sheet.class})
public interface SheetBasicsProjection {

    @Value("#{target.template.name}")
    String getTemplateName();

    int getTotalRowCount();

    int getProcessedRowCount();

    String getTargetType();

    String getSourceFileName();

    String getStatus();

    Long getVersion();

    Date getCreatedDate();

    Date getLastModifiedDate();

    String getCreatedBy();

    String getLastModifiedBy();

}
