import model.AppConst;
import model.AppProps;
import services.impl.mainServImpl;
import view.impl.conUIimpl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class MainClass {

    public static void main(String[] args) {

        conUIimpl view = new conUIimpl();
        long timeStamp = System.currentTimeMillis();
        view.showMessage("Starting process");
        parseInputArguments(args);
        if (AppProps.PROPS.getFieldsCount() == 0) {
            view.showMessage("Empty fields number, aborted");
            System.exit(0);
        }
        createWorkDir();
        mainServImpl mainServ = new mainServImpl();
        mainServ.execute();
        view.showMessage("Total execution time: " + (System.currentTimeMillis() - timeStamp) / 1000 +" sec");

    }


    /**
     * Parsinng input arguments to fill Application Properties
     */
    private static void parseInputArguments(String[] args) {
        Integer fieldCount = null;
        if (args.length < 4) {
            System.out.println("Required parameters  not set ... \n");
            showHelp();
            return;
        }
        AppProps.PROPS.setDBUrl(args[0]);
        AppProps.PROPS.setUserName(args[1]);
        AppProps.PROPS.setUserPassword(args[2]);
        try {
            fieldCount = Integer.valueOf(args[3]);
            AppProps.PROPS.setFieldsCount(fieldCount);
        } catch (NumberFormatException e) {
            System.err.println("Integer format expected as field count...");
            showHelp();
            return;
        }
    }

    /**
     * Show help end examples how to use apllication
     */
    private static void showHelp() {
        System.out.println("Required parameters by order: databaseUrl userName password fieldsCount");
    }

    /**
     * Create work directory
     */
    private static void createWorkDir() {
        Path workDir = Paths.get(AppConst.WORK_DIR);

        if (Files.notExists(workDir)) {
            try {
                Files.createDirectory(workDir);
                File file = new File(AppConst.CONVERT_XLST_FILE);
                Files.write(Paths.get(file.toURI()), AppConst.TEMPLATE_XLST.getBytes("utf-8"), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            } catch (IOException e) {
                System.err.println("Can not create working directory [ " + AppConst.WORK_DIR + " ]");
            }

        }
    }
}
