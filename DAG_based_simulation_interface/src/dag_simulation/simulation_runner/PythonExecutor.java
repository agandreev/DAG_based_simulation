package dag_simulation.simulation_runner;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;

public class PythonExecutor {
    private final ProcessBuilder process;
    private String directoryPath;

    public PythonExecutor(String directoryPath) {
        // enter it through file chooser
        this.directoryPath = directoryPath;
        process = new ProcessBuilder();
        process.redirectErrorStream(true);
    }

    public void setDirectoryPath(String path) {
        directoryPath = path;
    }

    public int installRequirements() throws IOException {
        String requirementsPath = directoryPath + "\\" + "requirements.txt";
        Process runningProcess =
                process.command("pip", "install", "-r", requirementsPath).start();
        try {
            return runningProcess.waitFor();
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
            return -1;
        }
    }

    public String runScript(ArrayList<String> arguments) throws IOException {
        String pythonFilePath = directoryPath + "\\" + "main.py";
        Process runningProcess =
                process.command(
                        "python", pythonFilePath, String.join(" ", arguments)).start();
        System.out.println(String.join(" ", arguments));
        Reader reader = new InputStreamReader(runningProcess.getInputStream());
        int ch;
        StringBuilder message = new StringBuilder();
        while ((ch = reader.read()) != -1)
            message.append((char) ch);
        reader.close();
        if (!(message.toString().isEmpty() || message.toString().isBlank())) {
            System.out.println(message);
            throw new IllegalArgumentException(message.toString());
        }
        try {
            if (runningProcess.waitFor() == 0) {
                return "";
            }
            return " ";
        } catch (InterruptedException e) {
            return e.getMessage();
        }
    }
}
