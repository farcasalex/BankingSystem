package util;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import java.lang.reflect.Field;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;

public class TableViewReflection {
    public static void objectPrint(Object object){

        for(Field field : object.getClass().getDeclaredFields()){
            field.setAccessible(true);
            Object value;
            try{
                value = field.get(object);
                System.out.println(field.getName() + " = " + value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public static void dataToTable(Object object, TableView table){
        if (table.getColumns().isEmpty() == true) {
            for(Field field : object.getClass().getDeclaredFields()){
                field.setAccessible(true);
                try{
                    TableColumn column = new TableColumn(field.getName());
                    table.getColumns().add(column);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        ObservableList<TableColumn> columns = table.getColumns();
        columns.forEach(column -> {
            column.setCellValueFactory(new PropertyValueFactory<>(column.getText()));
        });

        ObservableList data = table.getItems();
        data.add(object);
        table.setItems(data);

    }

    public static void emptyTable(TableView table){
        table.getItems().clear();
    }
}
