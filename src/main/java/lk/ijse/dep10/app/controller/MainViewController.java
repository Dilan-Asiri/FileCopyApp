package lk.ijse.dep10.app.controller;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.*;
import java.util.Optional;

public class MainViewController {

    @FXML
    private Button btnCopy;

    @FXML
    private Button btnDestination;

    @FXML
    private Button btnReset;

    @FXML
    private Button btnSource;

    @FXML
    private TextField txtDestination;
    private File sourceFile;
    private File destinationFolder;

    @FXML
    private TextField txtSource;
    public void initialize(){
        btnCopy.setDisable(true);

    }

    @FXML
    void btnCopyOnAction(ActionEvent event) throws IOException {
        File targeFile = new File(destinationFolder, sourceFile.getName());
        if(targeFile.exists()){
            Optional<ButtonType> result = new Alert(Alert.AlertType.CONFIRMATION, "File already exist please try again", ButtonType.YES, ButtonType.NO).showAndWait();

            if (result.isEmpty() || result.get()==ButtonType.NO) return;
        }
        btnCopy.getScene().getWindow().setHeight(300);
        Task task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                FileInputStream fis = new FileInputStream(sourceFile);
                FileOutputStream fos = new  FileOutputStream(targeFile);

                double write=0.0;
                while(true){
                    byte[] buffer=new byte[1024*10];
                    int read=fis.read(buffer);
                    if (read==-1) break;
                    fos.write(buffer,0,read);
                    write+=read;
                }
                fis.close();
                fos.close();
                return null;
            }
        };
        task.exceptionProperty().addListener(observable -> {
            task.getException().printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Something went wrong, please try again!").show();
        });
        new Thread(task).start();



    }

    @FXML
    void btnDestinationOnAction(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select the direction");
        destinationFolder = directoryChooser.showDialog(btnDestination.getScene().getWindow());
        enableCopyButton();
        if(destinationFolder ==null) return;
        txtDestination.setText(destinationFolder.getAbsolutePath());

    }

    @FXML
    void btnResetOnAction(ActionEvent event) {
        btnReset.getScene().getWindow().setHeight(300);
        txtSource.clear();
        txtDestination.clear();
        txtSource.requestFocus();
        sourceFile=null;
        destinationFolder=null;
        enableCopyButton();

    }

    @FXML
    void btnSourceOnAction(ActionEvent event) {
        FileChooser fileChooser=new FileChooser();
        fileChooser.setTitle("Select the FileChooser");
        sourceFile = fileChooser.showOpenDialog(btnSource.getScene().getWindow());
        enableCopyButton();
        if (sourceFile==null) return;
        txtSource.setText(sourceFile.getAbsolutePath());

    }

    private void enableCopyButton() {
        btnCopy.setDisable(sourceFile==null || destinationFolder ==null || sourceFile.getParentFile().equals(destinationFolder));
    }


    public void btnBrowseOnMouseEnetred(MouseEvent mouseEvent) {
    }

    public void btnBrowseOnMouseExited(MouseEvent mouseEvent) {

    }
}
