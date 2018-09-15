package loginfx.database;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import loginfx.Main;

/**
 *
 * @author BURAK
 */
public class DatabaseUtil {

    public static final Database database = Main.dbase;

    //give the content of the database to a TableView object
    //(of course i stole it)
    public static void fillTable(TableView table) {
        table.getColumns().removeAll(table.getColumns());
        ObservableList<ObservableList> data = FXCollections.observableArrayList();
        try {
            ResultSet rs = database.getAll();
            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                //We are using non property style for making dynamic table
                final int j = i;
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1));
                col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                        return new SimpleStringProperty(param.getValue().get(j).toString());
                    }
                });
                table.getColumns().addAll(col);
            }
            //Data added to ObservableList *
            while (rs.next()) {
                //Iterate Row
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    //Iterate Column
                    row.add(rs.getString(i));
                }
                data.add(row);
            }
            //FINALLY ADDED TO TableView
            table.setItems(data);
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.exit(1);
        }
    }

    //get column names of users table
    public String[] getColumnNames() throws SQLException {
        ResultSet rs = database.getAll();
        ResultSetMetaData metaData = rs.getMetaData();
        List<String> columnNames = new ArrayList<String>();

        int columnCount = metaData.getColumnCount();
        for (int column = 1; column <= columnCount; column++) {
            columnNames.add(metaData.getColumnName(column));
        }
        return columnNames.toArray(new String[columnNames.size()]);
    }
}
