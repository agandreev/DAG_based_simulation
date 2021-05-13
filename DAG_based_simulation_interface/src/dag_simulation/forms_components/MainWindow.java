package dag_simulation.forms_components;

import dag_simulation.simulation_runner.PythonExecutor;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

public class MainWindow extends Application {
    private static PythonExecutor PYTHON_EXECUTOR;
    private static final String PATH_TO_PROJECT = "../dag_simulation_framework/";
    private static final String PATH_TO_FUNCTIONS = PATH_TO_PROJECT
            + "simulation_components/functions/";

    private static GridPane gridPane;
    private static HBox bottomTools;
    private static HBox imageBox;
    private static ImageView graphImage;
    private static Label resultOutPut;

    // all input fields
    private static TextField AGENTS_INPUT;
    private static TextField TRANSACTIONS_INPUT;
    private static TextField TRANSACTIONS_RATE_INPUT;
    private static TextField NETWORK_AGENT_DELAY_INPUT;
    private static TextField NETWORK_DELAY_INPUT;
    private static TextField ALPHA_INPUT;
    private static TextField WALKERS_QUANTITY_INPUT;

    private static TextField ATTACKER_TRANSACTIONS_QUANTITY_INPUT;
    private static TextField ATTACKER_START_TIME_INPUT;
    private static TextField ATTACKER_TRANSACTIONS_RATE_INPUT;
    private static TextField ATTACKER_CONFIRMATION_DELAY_INPUT;
    private static TextField ATTACKER_REFERENCES_INPUT;
    private static TextField ATTACKER_SPLITS_INPUT;


    private static ComboBox VALIDATING_ALGORITHM_BOX;
    private static ComboBox ATTACKER_TYPE_BOX;
    private static ComboBox WALKER_CHOICE_BOX;
    private static ComboBox START_CHOICE_BOX;

    //all labels to input fields
    private static final Label TITLE_LABLE =
            new Label("Customization: ");
    private static final Label AGENTS_QUANTITY_LABEL =
            new Label("Agents quantity: ");
    private static final Label TRANSACTIONS_QUANTITY_LABEL =
            new Label("Transactions quantity: ");
    private static final Label TRANSACTIONS_RATE_LABEL =
            new Label("Transactions rate: ");
    private static final Label NETWORK_AGENT_DELAY_LABEL =
            new Label("Agent delay: ");
    private static final Label NETWORK_DELAY_LABEL =
            new Label("Network delay: ");
    private static final Label ALPHA_LABEL =
            new Label("Alpha: ");
    private static final Label WALKERS_LABEL =
            new Label("Walkers quantity: ");
    private static final Label WALKER_CHOICE_LABEL =
            new Label("Walker's choice: ");
    private static final Label START_CHOICE_LABEL =
            new Label("Start type: ");
    private static final Label VALIDATING_ALGORITHM_LABEL =
            new Label("Validating algorithm: ");
    private static final Label ATTACKER_TYPE_LABEL =
            new Label("Attacker type: ");

    private static final Label ATTACKER_TRANSACTIONS_QUANTITY_LABEL =
            new Label("Attacker's transactions : ");
    private static final Label ATTACKER_START_TIME_LABEL =
            new Label("Attacker's start time: ");
    private static final Label ATTACKER_TRANSACTIONS_RATE_LABEL
            = new Label("Attacker's rate: ");
    private static final Label ATTACKER_CONFIRMATION_DELAY_LABEL
            = new Label("Attacker's delay: ");
    private static final Label ATTACKER_REFERENCES_LABEL
            = new Label("Attacker's reference quantity: ");
    private static final Label ATTACKER_SPLITS_LABEL
            = new Label("Attacker's split quantity: ");

    public static void main(String[] args) {
        PYTHON_EXECUTOR = new PythonExecutor(
                "C:\\Users\\Lenovo X1 Extreme\\PycharmProjects\\DagSimulationNetwork");
        try {
            if (PYTHON_EXECUTOR.installRequirements() != 0) {
                return;
            }
        } catch (IOException e) {
            AboutBox.display("Problems with requirements.txt");
            return;
        }
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Customize prime stage.
        primaryStage.setTitle("DAG based simulation");
        primaryStage.setOnCloseRequest(e -> {
            e.consume();
            Platform.exit();
        });

        // Use a VBox for the root node.
        final BorderPane rootNode = new BorderPane();

        // Create a scene.
        Scene tableScene = new Scene(rootNode, 300, 300);

        // Set the table scene on the stage.
        primaryStage.setScene(tableScene);

        // Create the menu bar.
        MenuBar menuBar = new MenuBar();

        //makeTable(); // Create table.
        //makeFileMenu(); // Create the File menu.
        //makeOptionsMenu(); // Create the Options menu.
        //makeHelpMenu(); // Create the Help menu.
        initializeAttackersInputs();
        initializeTextFields();

        makeToolBar();
        makeBottomTool(primaryStage);
        makeImageBox(primaryStage);

        ScrollPane scrollPane = new ScrollPane(gridPane);
        scrollPane.setFitToWidth(true);
        //gridPane.getChildren().add(scrollBar);

        // Add all elements to root node
        rootNode.setLeft(scrollPane);
        rootNode.setBottom(bottomTools);
        rootNode.setCenter(graphImage);
        //rootNode.getChildren().addAll(menuBar, toolGrid);

        // Show the stage and its scene.
        primaryStage.show();
    }

    private void makeToolBar() {
        // Create tool bar items.
        gridPane = new GridPane();
        initializeGridPane();
    }

    private void makeImageBox(Stage stage) {
        imageBox = new HBox();
        graphImage = new ImageView();
        graphImage.setFitHeight(600);
        graphImage.setFitWidth(900);
//        graphImage.fitWidthProperty().bind(stage.widthProperty());
//        graphImage.fitHeightProperty().bind(stage.heightProperty());
        imageBox.getChildren().add(graphImage);
    }

    private void makeBottomTool(Stage stage) {
        bottomTools = new HBox();
        bottomTools.setPadding(new Insets(10, 10, 10, 10));
        bottomTools.setAlignment(Pos.BASELINE_RIGHT);
        bottomTools.setSpacing(10);

        Button runButton = new Button("Run");
        runButton.setOnAction(e -> {
            try {
                String exceptionMessage = runPythonExecutor();
                if (exceptionMessage.length() != 0) {
                    AboutBox.display(exceptionMessage);
                } else {
                    try {
                        graphImage.setImage(new Image(new FileInputStream("graph.png")));
                        setResult();
                    } catch (FileNotFoundException fileNotFoundException) {
                        AboutBox.display("Graph picture not found!");
                    } catch (IOException ioException) {
                        AboutBox.display("Something wrong with your access right!");
                    }
                }
            } catch (IOException ioException) {
                AboutBox.display("Trouble in python configuration!");
            }
            catch (IllegalArgumentException illegalArgumentException) {
                AboutBox.display(illegalArgumentException.getMessage());
            }
        });

        Button loadOwnFunctionButton = new Button("Load function");
        loadOwnFunctionButton.setDisable(false);

        final FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter filter =
                new FileChooser.ExtensionFilter("PYTHON files (*.py)", "*.py");
        fileChooser.getExtensionFilters().add(filter);
        loadOwnFunctionButton.setOnAction(e -> {
            File file = fileChooser.showOpenDialog(stage);
            if (file != null) {
                Path source = Paths.get(file.getAbsolutePath());
                try {
                    Path newDirectory = new File(PATH_TO_FUNCTIONS).toPath();
                    Files.move(source, newDirectory.resolve(source.getFileName()),
                            StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException ioException) {
                    runButton.setDisable(true);
                    System.out.println(ioException.getMessage());
                    AboutBox.display("Something wrong with your access right!");
                }
                runButton.setDisable(false);
            }
        });

        final DirectoryChooser saveCSVnPNG = new DirectoryChooser();
        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> {
            File directory = saveCSVnPNG.showDialog(stage);
            if (directory != null) {
                try {
                    Path current = Paths.get("graph.png");
                    Path destination = Paths.get(directory.getAbsolutePath() + "\\graph.png");
                    Files.copy(current, destination, StandardCopyOption.REPLACE_EXISTING);

                    current = Paths.get("cumulative_weights.csv");
                    destination = Paths.get(directory.getAbsolutePath() +
                            "\\cumulative_weights.csv");
                    Files.copy(current, destination, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException ioException) {
                    AboutBox.display("Something wrong with your access right!");
                }
            }
        });

        resultOutPut = new Label();
        resultOutPut.setFont(new Font("Arial", 20));
        resultOutPut.setText("Information...");

        Region region1 = new Region();
        HBox.setHgrow(region1, Priority.ALWAYS);

        bottomTools.getChildren().addAll(resultOutPut, region1, saveButton, loadOwnFunctionButton,
                runButton);
    }

    private void setResult() throws IOException {
        StringBuilder str = new StringBuilder();
        String[] arguments = {"Time: ", "Legitimate weight: ", "Fake weight: "};

        BufferedReader reader = new BufferedReader(new FileReader("result.txt"));
        String currentLine;
        int counter = 0;
        while ((currentLine = reader.readLine()) != null) {
            str.append(arguments[counter]).append(currentLine).append("\t");
            counter++;
        }
        reader.close();

        resultOutPut.setText(str.toString());
    }

    private String runPythonExecutor() throws
            IOException, IllegalArgumentException{
        int attackerTransactionQuantity, attackerStartTime,
                attackerRate, attackerConfirmationDelay,
                attackerReferenceQuantity, attackerSplits;
        int transactionQuantity = Integer.parseInt(TRANSACTIONS_INPUT.getText());
        int agentQuantity = Integer.parseInt(AGENTS_INPUT.getText());
        double alpha = Double.parseDouble(ALPHA_INPUT.getText());
        int transactionRate = Integer.parseInt(TRANSACTIONS_RATE_INPUT.getText());
        int networkAgentDelay = Integer.parseInt(NETWORK_AGENT_DELAY_INPUT.getText());
        int networkDelay = Integer.parseInt(NETWORK_DELAY_INPUT.getText());
        String selectionType = (String) VALIDATING_ALGORITHM_BOX.getSelectionModel()
                .getSelectedItem();
        int walkerQuantity = Integer.parseInt(WALKERS_QUANTITY_INPUT.getText());
        String walkerType = (String) WALKER_CHOICE_BOX.getSelectionModel()
                .getSelectedItem();
        String startType = (String) START_CHOICE_BOX.getSelectionModel()
                .getSelectedItem();
        String attackerType = (String) ATTACKER_TYPE_BOX.getSelectionModel()
                .getSelectedItem();
        ArrayList<String> arguments = new ArrayList<>();
        arguments.add(Integer.toString(transactionQuantity));
        arguments.add(Integer.toString(agentQuantity));
        arguments.add(Integer.toString(transactionRate));
        arguments.add(Integer.toString(networkAgentDelay));
        arguments.add(Integer.toString(networkDelay));
        arguments.add(Double.toString(alpha));
        arguments.add(selectionType);
        arguments.add(Integer.toString(walkerQuantity));
        arguments.add(walkerType);
        arguments.add(startType);
        arguments.add(attackerType);

        if (!attackerType.equals("None")) {
            attackerTransactionQuantity = Integer.parseInt(
                    ATTACKER_TRANSACTIONS_QUANTITY_INPUT.getText());
            attackerStartTime = Integer.parseInt(
                    ATTACKER_START_TIME_INPUT.getText());
            attackerRate = Integer.parseInt(
                    ATTACKER_TRANSACTIONS_RATE_INPUT.getText());
            arguments.add(Integer.toString(attackerTransactionQuantity));
            arguments.add(Integer.toString(attackerStartTime));
            arguments.add(Integer.toString(attackerRate));
        }
        if (attackerType.equals("Outpace")) {
            attackerConfirmationDelay = Integer.parseInt(
                    ATTACKER_CONFIRMATION_DELAY_INPUT.getText());
            arguments.add(Integer.toString(attackerConfirmationDelay));
        }
        if (attackerType.equals("Parasite")) {
            attackerConfirmationDelay = Integer.parseInt(
                    ATTACKER_CONFIRMATION_DELAY_INPUT.getText());
            attackerReferenceQuantity = Integer.parseInt(
                    ATTACKER_REFERENCES_INPUT.getText());
            arguments.add(Integer.toString(attackerConfirmationDelay));
            arguments.add(Integer.toString(attackerReferenceQuantity));
        }
        if (attackerType.equals("Split")) {
            attackerSplits = Integer.parseInt(
                    ATTACKER_SPLITS_INPUT.getText());
            arguments.add(Integer.toString(attackerSplits));
        }

        return PYTHON_EXECUTOR.runScript(arguments);
    }

    private void initializeGridPane() {
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setVgap(20);
        gridPane.setHgap(8);

        TITLE_LABLE.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        GridPane.setConstraints(TITLE_LABLE, 0, 0);
        GridPane.setConstraints(AGENTS_QUANTITY_LABEL, 0, 1);
        GridPane.setConstraints(TRANSACTIONS_QUANTITY_LABEL, 0, 2);
        GridPane.setConstraints(TRANSACTIONS_RATE_LABEL, 0, 3);
        GridPane.setConstraints(NETWORK_AGENT_DELAY_LABEL, 0, 4);
        GridPane.setConstraints(NETWORK_DELAY_LABEL, 0, 5);
        GridPane.setConstraints(ALPHA_LABEL, 0, 6);
        GridPane.setConstraints(VALIDATING_ALGORITHM_LABEL, 0, 7);
        GridPane.setConstraints(WALKERS_LABEL, 0, 8);
        GridPane.setConstraints(WALKER_CHOICE_LABEL, 0, 9);
        GridPane.setConstraints(START_CHOICE_LABEL, 0, 10);
        GridPane.setConstraints(ATTACKER_TYPE_LABEL, 0, 11);

        GridPane.setConstraints(AGENTS_INPUT, 1, 1);
        GridPane.setConstraints(TRANSACTIONS_INPUT, 1, 2);
        GridPane.setConstraints(TRANSACTIONS_RATE_INPUT, 1, 3);
        GridPane.setConstraints(NETWORK_AGENT_DELAY_INPUT, 1, 4);
        GridPane.setConstraints(NETWORK_DELAY_INPUT, 1, 5);
        GridPane.setConstraints(ALPHA_INPUT, 1, 6);
        GridPane.setConstraints(VALIDATING_ALGORITHM_BOX, 1, 7);
        GridPane.setConstraints(WALKERS_QUANTITY_INPUT, 1, 8);
        GridPane.setConstraints(WALKER_CHOICE_BOX, 1, 9);
        GridPane.setConstraints(START_CHOICE_BOX, 1, 10);
        GridPane.setConstraints(ATTACKER_TYPE_BOX, 1, 11);

        gridPane.getChildren().addAll(TITLE_LABLE, TRANSACTIONS_QUANTITY_LABEL,
                TRANSACTIONS_INPUT, AGENTS_QUANTITY_LABEL, AGENTS_INPUT,
                TRANSACTIONS_RATE_LABEL, TRANSACTIONS_RATE_INPUT, NETWORK_AGENT_DELAY_LABEL,
                NETWORK_AGENT_DELAY_INPUT, NETWORK_DELAY_LABEL,
                NETWORK_DELAY_INPUT, ALPHA_LABEL, ALPHA_INPUT, VALIDATING_ALGORITHM_LABEL,
                VALIDATING_ALGORITHM_BOX, WALKERS_QUANTITY_INPUT, WALKERS_LABEL,
                WALKER_CHOICE_LABEL, WALKER_CHOICE_BOX, START_CHOICE_LABEL,
                START_CHOICE_BOX, ATTACKER_TYPE_LABEL, ATTACKER_TYPE_BOX);
    }

    private static void initializeTextFields() {
        TRANSACTIONS_INPUT = new TextField();
        TRANSACTIONS_INPUT.setText("50");

        AGENTS_INPUT = new TextField();
        AGENTS_INPUT.setText("2");

        TRANSACTIONS_RATE_INPUT = new TextField();
        TRANSACTIONS_RATE_INPUT.setText("10");

        NETWORK_AGENT_DELAY_INPUT = new TextField();
        NETWORK_AGENT_DELAY_INPUT.setText("1");

        NETWORK_DELAY_INPUT = new TextField();
        NETWORK_DELAY_INPUT.setText("1");

        ALPHA_INPUT = new TextField();
        ALPHA_INPUT.setText("0.1");

        ObservableList<String> validatingOptions =
                FXCollections.observableArrayList(
                        "MCMC",
                        "URTC",
                        "OWN"
                );
        VALIDATING_ALGORITHM_BOX = new ComboBox(validatingOptions);
        VALIDATING_ALGORITHM_BOX.getSelectionModel().selectFirst();

        ObservableList<String> attackingOptions =
                FXCollections.observableArrayList(
                        "None",
                        "Outpace",
                        "Parasite",
                        "Split"
                );

        WALKERS_QUANTITY_INPUT = new TextField("2");

        ObservableList<String> walkerOptions =
                FXCollections.observableArrayList(
                        "None",
                        "Last",
                        "First",
                        "Random"
                );
        WALKER_CHOICE_BOX = new ComboBox(walkerOptions);
        WALKER_CHOICE_BOX.getSelectionModel().selectFirst();

        ObservableList<String> startOptions =
                FXCollections.observableArrayList(
                        "None",
                        "Number",
                        "Time"
                );
        START_CHOICE_BOX = new ComboBox(startOptions);
        START_CHOICE_BOX.getSelectionModel().selectFirst();

        ATTACKER_TYPE_BOX = new ComboBox(attackingOptions);
        ATTACKER_TYPE_BOX.getSelectionModel().selectFirst();
        ATTACKER_TYPE_BOX.setOnAction(e -> {
            String attackerType = (String) ATTACKER_TYPE_BOX.getSelectionModel().getSelectedItem();
            if (attackerType.equals("None")) {
                cleanAttackersInputs();
            }
            if (attackerType.equals("Outpace")) {
                cleanAttackersInputs();
                addOutpaceOptions(11);
            }
            if (attackerType.equals("Parasite")) {
                cleanAttackersInputs();
                addParasiteOptions(11);
            }
            if (attackerType.equals("Split")) {
                cleanAttackersInputs();
                addSplitOptions(11);
            }
        });
    }

    private static void addOutpaceOptions(int startIndex) {
        int fixNum = startIndex;
        GridPane.setConstraints(ATTACKER_TRANSACTIONS_QUANTITY_LABEL, 0, ++startIndex);
        GridPane.setConstraints(ATTACKER_START_TIME_LABEL, 0, ++startIndex);
        GridPane.setConstraints(ATTACKER_TRANSACTIONS_RATE_LABEL, 0, ++startIndex);
        GridPane.setConstraints(ATTACKER_CONFIRMATION_DELAY_LABEL, 0, ++startIndex);
        startIndex = fixNum;

        GridPane.setConstraints(ATTACKER_TRANSACTIONS_QUANTITY_INPUT, 1, ++startIndex);
        GridPane.setConstraints(ATTACKER_START_TIME_INPUT, 1, ++startIndex);
        GridPane.setConstraints(ATTACKER_TRANSACTIONS_RATE_INPUT, 1, ++startIndex);
        GridPane.setConstraints(ATTACKER_CONFIRMATION_DELAY_INPUT, 1, ++startIndex);

        gridPane.getChildren().addAll(ATTACKER_TRANSACTIONS_QUANTITY_LABEL,
                ATTACKER_TRANSACTIONS_QUANTITY_INPUT, ATTACKER_START_TIME_LABEL,
                ATTACKER_START_TIME_INPUT, ATTACKER_TRANSACTIONS_RATE_LABEL,
                ATTACKER_TRANSACTIONS_RATE_INPUT, ATTACKER_CONFIRMATION_DELAY_LABEL,
                ATTACKER_CONFIRMATION_DELAY_INPUT);
    }

    private static void addParasiteOptions(int startIndex) {
        int fixNum = startIndex;
        GridPane.setConstraints(ATTACKER_TRANSACTIONS_QUANTITY_LABEL, 0, ++startIndex);
        GridPane.setConstraints(ATTACKER_START_TIME_LABEL, 0, ++startIndex);
        GridPane.setConstraints(ATTACKER_TRANSACTIONS_RATE_LABEL, 0, ++startIndex);
        GridPane.setConstraints(ATTACKER_CONFIRMATION_DELAY_LABEL, 0, ++startIndex);
        GridPane.setConstraints(ATTACKER_REFERENCES_LABEL, 0, ++startIndex);
        startIndex = fixNum;

        GridPane.setConstraints(ATTACKER_TRANSACTIONS_QUANTITY_INPUT, 1, ++startIndex);
        GridPane.setConstraints(ATTACKER_START_TIME_INPUT, 1, ++startIndex);
        GridPane.setConstraints(ATTACKER_TRANSACTIONS_RATE_INPUT, 1, ++startIndex);
        GridPane.setConstraints(ATTACKER_CONFIRMATION_DELAY_INPUT, 1, ++startIndex);
        GridPane.setConstraints(ATTACKER_REFERENCES_INPUT, 1, ++startIndex);

        gridPane.getChildren().addAll(ATTACKER_TRANSACTIONS_QUANTITY_LABEL,
                ATTACKER_TRANSACTIONS_QUANTITY_INPUT, ATTACKER_START_TIME_LABEL,
                ATTACKER_START_TIME_INPUT, ATTACKER_TRANSACTIONS_RATE_LABEL,
                ATTACKER_TRANSACTIONS_RATE_INPUT, ATTACKER_CONFIRMATION_DELAY_LABEL,
                ATTACKER_CONFIRMATION_DELAY_INPUT, ATTACKER_REFERENCES_LABEL,
                ATTACKER_REFERENCES_INPUT);
    }

    private static void addSplitOptions(int startIndex) {
        int fixNum = startIndex;
        GridPane.setConstraints(ATTACKER_TRANSACTIONS_QUANTITY_LABEL, 0, ++startIndex);
        GridPane.setConstraints(ATTACKER_START_TIME_LABEL, 0, ++startIndex);
        GridPane.setConstraints(ATTACKER_TRANSACTIONS_RATE_LABEL, 0, ++startIndex);
        GridPane.setConstraints(ATTACKER_SPLITS_LABEL, 0, ++startIndex);
        startIndex = fixNum;

        GridPane.setConstraints(ATTACKER_TRANSACTIONS_QUANTITY_INPUT, 1, ++startIndex);
        GridPane.setConstraints(ATTACKER_START_TIME_INPUT, 1, ++startIndex);
        GridPane.setConstraints(ATTACKER_TRANSACTIONS_RATE_INPUT, 1, ++startIndex);
        GridPane.setConstraints(ATTACKER_SPLITS_INPUT, 1, ++startIndex);

        gridPane.getChildren().addAll(ATTACKER_TRANSACTIONS_QUANTITY_LABEL,
                ATTACKER_TRANSACTIONS_QUANTITY_INPUT, ATTACKER_START_TIME_LABEL,
                ATTACKER_START_TIME_INPUT, ATTACKER_TRANSACTIONS_RATE_LABEL,
                ATTACKER_TRANSACTIONS_RATE_INPUT, ATTACKER_SPLITS_LABEL,
                ATTACKER_SPLITS_INPUT);
    }

    private static void initializeAttackersInputs() {
        ATTACKER_TRANSACTIONS_QUANTITY_INPUT = new TextField();
        ATTACKER_TRANSACTIONS_QUANTITY_INPUT.setText("10");
        ATTACKER_START_TIME_INPUT = new TextField();
        ATTACKER_START_TIME_INPUT.setText("1");
        ATTACKER_TRANSACTIONS_RATE_INPUT = new TextField();
        ATTACKER_TRANSACTIONS_RATE_INPUT.setText("5");
        ATTACKER_CONFIRMATION_DELAY_INPUT = new TextField();
        ATTACKER_CONFIRMATION_DELAY_INPUT.setText("2");
        ATTACKER_REFERENCES_INPUT = new TextField();
        ATTACKER_REFERENCES_INPUT.setText("5");
        ATTACKER_SPLITS_INPUT = new TextField();
        ATTACKER_SPLITS_INPUT.setText("2");
    }

    private static void cleanAttackersInputs() {
        gridPane.getChildren().removeAll(ATTACKER_TRANSACTIONS_QUANTITY_LABEL,
                ATTACKER_TRANSACTIONS_QUANTITY_INPUT, ATTACKER_START_TIME_LABEL,
                ATTACKER_START_TIME_INPUT, ATTACKER_TRANSACTIONS_RATE_LABEL,
                ATTACKER_TRANSACTIONS_RATE_INPUT, ATTACKER_CONFIRMATION_DELAY_LABEL,
                ATTACKER_CONFIRMATION_DELAY_INPUT, ATTACKER_REFERENCES_LABEL,
                ATTACKER_REFERENCES_INPUT, ATTACKER_SPLITS_LABEL,
                ATTACKER_SPLITS_INPUT);
    }
}
