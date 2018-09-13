package uk.ac.ebi.subs.repository.model.sheets;

import lombok.Data;
import lombok.NonNull;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;
import uk.ac.ebi.subs.data.component.Team;

import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;

@CompoundIndexes({
        @CompoundIndex(background = true, name = "submission_data_type", def = "{ 'submissionId': 1, 'template.targetType': 1}")
})
@Document
@Data
public class Spreadsheet {


    private String submissionId;
    private String checklistId;
    private String dataTypeId;

    private String sourceFileName;
    private String sheetName;

    private SheetStatusEnum status;
    private Team team;

    private String targetType;

    @Id
    private String id;

    @Version
    private Long version;
    @CreatedDate
    private Date createdDate;
    @LastModifiedDate
    private Date lastModifiedDate;
    @CreatedBy
    private String createdBy;
    @LastModifiedBy
    private String lastModifiedBy;

    public int getTotalRowCount() {
        return rows.size();
    }

    public int getProcessedRowCount() {
        return (int) rows.stream().filter(Row::isProcessed).count();
    }

    @NonNull
    private List<Row> rows = new LinkedList<>();

    private Row headerRow;

    public Row addRow(String[] row) {
        return this.addRow(new Row(row));

    }

    public Row addRow(List<String> row) {
        return this.addRow(new Row(row));
    }

    public Row addRow(Row row) {
        this.rows.add(row);
        return row;
    }

    public void deleteColumn(int columnIndex) {
        rows
                .stream()
                .forEach(row -> row.deleteCellByColumnIndex(columnIndex));
    }

    public void deleteColumnsPastIndex(int lastIndexToKeep) {
        rows
                .stream()
                .forEach(row -> row.deleteCellsPastColumnIndex(lastIndexToKeep));
    }

    public Optional<Integer> indexOfLastNonEmptyColumn() {
        Optional<Integer> lastNonEmptyColumn = rows
                .stream()
                .map(Row::columnIndexOflastNonEmptyCell)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .max(Comparator.naturalOrder());

        return lastNonEmptyColumn;
    }

    public void removeEmptyRows() {
        ListIterator<Row> rowIterator = rows.listIterator();

        while (rowIterator.hasNext()) {
            Row currentRow = rowIterator.next();
            Optional<Integer> lastNonEmptyIndex = currentRow.columnIndexOflastNonEmptyCell();

            if (!lastNonEmptyIndex.isPresent()) {
                rowIterator.remove();
            }
        }
    }

    public void removeColumnsPastLastNonEmpty() {
        Optional<Integer> lastNonEmptyColumn = indexOfLastNonEmptyColumn();

        if (lastNonEmptyColumn.isPresent()) {
            this.deleteColumnsPastIndex(lastNonEmptyColumn.get());
        } else {
            this.rows.clear();
        }
    }


}
