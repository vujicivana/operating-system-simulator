package shell;
	
import java.io.IOException;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class GUI extends Application {
	private static String textToShow;
	private static TextArea top=new TextArea();
	private static TextField bottom=new TextField();
	private static Button light;
	private static Button dark;
	private PipedInputStream input=new PipedInputStream();
	private PipedOutputStream output=new PipedOutputStream();
	private StringBuilder outSB=new StringBuilder();
	private OutputStream outputStream;
	private int length1=0;
	
	public static void main(String []args) {
		Shell.booting();
		launch(args);
	}
	public static void clear() {
		top.setText("");
		bottom.clear();
	}
	private void addTextToTop() {
		if(outSB.length()>0) {
			top.appendText(outSB.toString());
			outSB=new StringBuilder();
		}
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		input.connect(output);
		textToShow="";

		light=new Button("Light");
		light.setPrefSize(60, 5);
		
		dark=new Button("Dark");
		dark.setPrefSize(60, 5);
		
		HBox buttons=new HBox(5);
		buttons.setAlignment(Pos.TOP_RIGHT);
		
		buttons.getChildren().addAll(light,dark);
		
		top=new TextArea();
		top.setPrefSize(900,500);
		top.setEditable(false);
		top.setText("WELCOME!\nType HELP for list of commands.\n");
		
		bottom=new TextField();
		bottom.setPrefSize(900,70);
		
		bottom.setOnAction(e -> {
			
			try {
				
				byte array[]=bottom.getText().getBytes();
				output.write(array);
				length1=array.length;
				
				Commands.readCommand(input, length1);
				
				textToShow=textToShow + ">" +bottom.getText()+"\n";
				top.appendText(textToShow);
				Commands.getCommand();
				
				bottom.clear();
				textToShow="";
				
				
			}catch(IOException e1) {
				e1.printStackTrace();
			}
		});
		
		outputStream=new OutputStream() {

			@Override
			public void write(int b) throws IOException {
				// TODO Auto-generated method stub
				outSB.append((char)b);
				if(((char) b) == '\n')
					addTextToTop();
				
			}
			
		};
		
		Commands.setOut(outputStream);
		
		VBox root =new VBox(15);
		root.setPadding(new Insets(10,30,30,30));
		root.getChildren().addAll(buttons,top,bottom);
		VBox.setVgrow(top, Priority.ALWAYS);
		Scene scena=new Scene(root,1200,650);
		scena.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

		light.setOnAction(e -> {
			scena.getStylesheets().clear();
			scena.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		}
		);
		
		dark.setOnAction(e -> {
			scena.getStylesheets().clear();
			scena.getStylesheets().add(getClass().getResource("change.css").toExternalForm());
		}
		);
		
		primaryStage.setScene(scena);
		primaryStage.show();
		bottom.requestFocus();
		
	}
}
