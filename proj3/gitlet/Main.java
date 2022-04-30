package gitlet;

import static gitlet.Command.init;
import static gitlet.Files.*;
import static gitlet.Utils.*;
import static gitlet.Command.*;

/** Driver class for Gitlet, the tiny stupid version-control system.
 *  @author Darren Wang
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND> .... */
    public static void main(String... args) {
        if (args.length == 0) {
            throw error("No command detected.");
        }
        String cases = args[0];
        if (cases.equals("init")) {
            validateArg(args, 1);
            initMain();
        } else if (cases.equals("add")) {
            validateArg(args, 2);
            addMain(args);
        } else if (cases.equals("commit")) {
            commitMain(args);
        } else if (cases.equals("rm")) {
            validateArg(args, 2);
            rm(args[1]);
        } else if (cases.equals("log")) {
            validateArg(args, 1);
            log();
        } else if (cases.equals("global-log")) {
            validateArg(args, 1);
            globalLog();
        } else if (cases.equals("find")) {
            validateArg(args, 2);
            find(args[1]);
        } else if (cases.equals("status")) {
            validateArg(args, 1);
            status();
        } else if (cases.equals("checkout")) {
            checkoutMain(args);
        } else if (cases.equals("branch")) {
            validateArg(args, 2);
            branch(args[1]);
        } else if (cases.equals("rm-branch")) {
            validateArg(args, 2);
            rmBranch(args[1]);
        } else if (cases.equals("reset")) {
            validateArg(args, 2);
            reset(args[1]);
        } else if (cases.equals("merge")) {
            validateArg(args, 2);
            merge(args[1]);
        } else {
            System.out.println("Invalid command.");
            return;
        }
    }

    static void initMain() {
        if (CWD.exists()) {
            System.out.println("A Gitlet version-control system" +
                    " already exists in the current directory.");
            return;
        }
        init();
    }

    static void addMain(String... args) {
        if (!join(args[1]).exists()) {
            System.out.println("File does not exist.");
            return;
        }
        add(args[1]);
    }

    static void commitMain(String... args) {
        if (args.length != 2) {
            System.out.println("Please enter a commit message.");
            return;
        }
        commit(args[1]);
    }

    static void checkoutMain(String... args) {
        if (args.length == 4) {
            checkoutPastFile(args[1], args[3]);
        } else if (args.length == 3) {
            checkoutHeadFile(args[2]);
        } else if (args.length == 2) {
            checkoutBranch(args[1]);
        } else {
            System.out.println("Not a valid input for checkout.");
            return;
        }
    }

    public static void validateArg(String[] args, int req) {
        if (args.length != req) {
            throw error("Invalid number of arguments.");
        }
    }

}
